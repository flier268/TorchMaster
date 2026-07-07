package net.xalcon.torchmaster.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.block.BlockState;
//? if >=1.19.4
import net.minecraft.registry.RegistryKey;
//? if >=1.16.5 && <1.19.4
//import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.blocks.MegaTorchBlock;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.port.LightSettingsView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class TorchmasterLightRangeDisplay
{
    private static final int RANDOM_REFRESH_INTERVAL_TICKS = 10;
    private static final int MAX_RANDOM_AIR_BLOCKS = 2000;
    private static final int AIR_SAMPLE_DIVISOR = 5;
    private static final Map<LightKey, Display> DISPLAYS = new HashMap<>();
    private static final Random RANDOM = new Random();

    private TorchmasterLightRangeDisplay()
    {
    }

    //? if >=1.16.5
    public static boolean isVisible(RegistryKey<World> dimension, BlockPos pos)
    //? if <1.16.5
    //public static boolean isVisible(Object dimension, BlockPos pos)
    {
        return isVisible(dimensionKey(dimension), pos);
    }

    //? if >=1.16.5
    public static boolean toggle(RegistryKey<World> dimension, BlockPos pos, LightType lightType, int radius)
    //? if <1.16.5
    //public static boolean toggle(Object dimension, BlockPos pos, LightType lightType, int radius)
    {
        return toggle(dimension, pos, lightType, radius, radius, radius, radius, radius, radius);
    }

    //? if >=1.16.5
    public static boolean toggle(RegistryKey<World> dimension, BlockPos pos, LightType lightType, int rangeWest, int rangeEast, int rangeDown, int rangeUp, int rangeNorth, int rangeSouth)
    //? if <1.16.5
    //public static boolean toggle(Object dimension, BlockPos pos, LightType lightType, int rangeWest, int rangeEast, int rangeDown, int rangeUp, int rangeNorth, int rangeSouth)
    {
        String dimensionKey = dimensionKey(dimension);
        if (!toggle(dimensionKey, pos, lightType, rangeWest, rangeEast, rangeDown, rangeUp, rangeNorth, rangeSouth)) {
            return false;
        }
        Display display = DISPLAYS.get(new LightKey(dimensionKey, pos));
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.world != null && display != null) {
            refreshRandomAirBlocks(minecraft.world, display);
        }
        return true;
    }

    //? if >=1.16.5
    public static void setVisible(RegistryKey<World> dimension, BlockPos pos, LightType lightType, boolean visible, int rangeWest, int rangeEast, int rangeDown, int rangeUp, int rangeNorth, int rangeSouth)
    //? if <1.16.5
    //public static void setVisible(Object dimension, BlockPos pos, LightType lightType, boolean visible, int rangeWest, int rangeEast, int rangeDown, int rangeUp, int rangeNorth, int rangeSouth)
    {
        String dimensionKey = dimensionKey(dimension);
        setVisible(dimensionKey, pos, lightType, visible, rangeWest, rangeEast, rangeDown, rangeUp, rangeNorth, rangeSouth, true);
        Display display = DISPLAYS.get(new LightKey(dimensionKey, pos));
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (display != null && minecraft.world != null && dimensionKey.equals(dimensionKey(minecraft.world))) {
            refreshRandomAirBlocks(minecraft.world, display);
        }
    }

    public static void applyCurrentWorld(BlockPos pos, LightType lightType, LightSettingsView snapshot)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.world == null) {
            return;
        }
        if (snapshot.previewSyncStart()) {
            beginServerSync(dimensionKey(minecraft.world));
            return;
        }
        if (snapshot.previewSyncEnd()) {
            endServerSync(dimensionKey(minecraft.world));
            return;
        }
        if (!snapshot.found()) {
            //? if >=1.16.5 {
            removeServerSynced(minecraft.world.getRegistryKey(), pos);
            //?} else if <1.16.5 {
            /*removeServerSynced(minecraft.world.getDimension().getType(), pos);
            *///?}
            return;
        }
        //? if >=1.16.5 {
        setVisible(minecraft.world.getRegistryKey(), pos, lightType, snapshot.rangeVisible(), snapshot.rangeWest(), snapshot.rangeEast(),
                snapshot.rangeDown(), snapshot.rangeUp(), snapshot.rangeNorth(), snapshot.rangeSouth());
        //?} else if <1.16.5 {
        /*setVisible(minecraft.world.getDimension().getType(), pos, lightType, snapshot.rangeVisible(), snapshot.rangeWest(), snapshot.rangeEast(), snapshot.rangeDown(), snapshot.rangeUp(), snapshot.rangeNorth(), snapshot.rangeSouth());
        *///?}
    }

    public static void clear()
    {
        DISPLAYS.clear();
    }

    //? if >=1.16.5
    public static void remove(RegistryKey<World> dimension, BlockPos pos)
    //? if <1.16.5
    //public static void remove(Object dimension, BlockPos pos)
    {
        remove(dimensionKey(dimension), pos);
    }

    //? if >=1.16.5
    public static void removeServerSynced(RegistryKey<World> dimension, BlockPos pos)
    //? if <1.16.5
    //public static void removeServerSynced(Object dimension, BlockPos pos)
    {
        removeServerSynced(dimensionKey(dimension), pos);
    }

    //? if >=1.16.5
    public static void refresh(RegistryKey<World> dimension, BlockPos pos, LightType lightType, int rangeWest, int rangeEast, int rangeDown, int rangeUp, int rangeNorth, int rangeSouth)
    //? if <1.16.5
    //public static void refresh(Object dimension, BlockPos pos, LightType lightType, int rangeWest, int rangeEast, int rangeDown, int rangeUp, int rangeNorth, int rangeSouth)
    {
        LightKey key = new LightKey(dimensionKey(dimension), pos);
        Display display = DISPLAYS.get(key);
        if (display == null) {
            return;
        }
        display.lightType = lightType;
        display.rangeWest = Math.max(0, rangeWest);
        display.rangeEast = Math.max(0, rangeEast);
        display.rangeDown = Math.max(0, rangeDown);
        display.rangeUp = Math.max(0, rangeUp);
        display.rangeNorth = Math.max(0, rangeNorth);
        display.rangeSouth = Math.max(0, rangeSouth);
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.world != null && key.dimension.equals(dimensionKey(minecraft.world))) {
            refreshRandomAirBlocks(minecraft.world, display);
        } else {
            display.randomAirBlocks.clear();
            display.ticks = RANDOM_REFRESH_INTERVAL_TICKS;
        }
    }

    static boolean isVisible(String dimension, BlockPos pos)
    {
        return DISPLAYS.containsKey(new LightKey(dimension, pos));
    }

    static boolean isServerSynced(String dimension, BlockPos pos)
    {
        Display display = DISPLAYS.get(new LightKey(dimension, pos));
        return display != null && display.serverSynced;
    }

    static boolean toggle(String dimension, BlockPos pos, LightType lightType, int rangeWest, int rangeEast, int rangeDown, int rangeUp,
            int rangeNorth, int rangeSouth)
    {
        LightKey key = new LightKey(dimension, pos);
        if (DISPLAYS.remove(key) != null) {
            return false;
        }
        Display display = new Display(pos, lightType, Math.max(0, rangeWest), Math.max(0, rangeEast), Math.max(0, rangeDown),
                Math.max(0, rangeUp), Math.max(0, rangeNorth), Math.max(0, rangeSouth), false);
        DISPLAYS.put(key, display);
        return true;
    }

    static void setVisible(String dimension, BlockPos pos, LightType lightType, boolean visible, int rangeWest, int rangeEast, int rangeDown,
            int rangeUp, int rangeNorth, int rangeSouth, boolean serverSynced)
    {
        if (!visible) {
            if (serverSynced) {
                removeServerSynced(dimension, pos);
            } else {
                remove(dimension, pos);
            }
            return;
        }
        LightKey key = new LightKey(dimension, pos);
        Display display = DISPLAYS.get(key);
        if (display == null) {
            display = new Display(pos, lightType, Math.max(0, rangeWest), Math.max(0, rangeEast), Math.max(0, rangeDown),
                    Math.max(0, rangeUp), Math.max(0, rangeNorth), Math.max(0, rangeSouth), serverSynced);
            DISPLAYS.put(key, display);
        } else {
            display.lightType = lightType;
            display.rangeWest = Math.max(0, rangeWest);
            display.rangeEast = Math.max(0, rangeEast);
            display.rangeDown = Math.max(0, rangeDown);
            display.rangeUp = Math.max(0, rangeUp);
            display.rangeNorth = Math.max(0, rangeNorth);
            display.rangeSouth = Math.max(0, rangeSouth);
            display.serverSynced = serverSynced;
        }
        if (serverSynced) {
            display.serverSyncSeen = true;
        }
    }

    static void remove(String dimension, BlockPos pos)
    {
        DISPLAYS.remove(new LightKey(dimension, pos));
    }

    static void removeServerSynced(String dimension, BlockPos pos)
    {
        LightKey key = new LightKey(dimension, pos);
        Display display = DISPLAYS.get(key);
        if (display != null && display.serverSynced) {
            DISPLAYS.remove(key);
        }
    }

    static void beginServerSync(String dimension)
    {
        for (Map.Entry<LightKey, Display> entry : DISPLAYS.entrySet()) {
            if (entry.getKey().dimension.equals(dimension) && entry.getValue().serverSynced) {
                entry.getValue().serverSyncSeen = false;
            }
        }
    }

    static void endServerSync(String dimension)
    {
        Iterator<Map.Entry<LightKey, Display>> iterator = DISPLAYS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<LightKey, Display> entry = iterator.next();
            if (entry.getKey().dimension.equals(dimension) && entry.getValue().serverSynced && !entry.getValue().serverSyncSeen) {
                iterator.remove();
            }
        }
    }

    public static void refreshRadii(ITorchmasterConfig config)
    {
        if (DISPLAYS.isEmpty()) {
            return;
        }

        MinecraftClient minecraft = MinecraftClient.getInstance();
        World currentWorld = minecraft.world;
        String currentDimension = currentWorld == null ? null : dimensionKey(currentWorld);
        for (Map.Entry<LightKey, Display> entry : DISPLAYS.entrySet()) {
            Display display = entry.getValue();
            int radius = Math.max(0, TorchmasterLightScreenModel.radius(display.lightType, config));
            display.rangeWest = radius;
            display.rangeEast = radius;
            display.rangeDown = radius;
            display.rangeUp = radius;
            display.rangeNorth = radius;
            display.rangeSouth = radius;
            if (currentWorld != null && entry.getKey().dimension.equals(currentDimension)) {
                refreshRandomAirBlocks(currentWorld, display);
            } else {
                display.randomAirBlocks.clear();
                display.ticks = RANDOM_REFRESH_INTERVAL_TICKS;
            }
        }
    }

    public static void tick()
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.world == null || minecraft.player == null || DISPLAYS.isEmpty()) {
            return;
        }
        if (minecraft.player.isSneaking()) {
            return;
        }

        String currentDimension = dimensionKey(minecraft.world);
        Iterator<Map.Entry<LightKey, Display>> iterator = DISPLAYS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<LightKey, Display> entry = iterator.next();
            if (!entry.getKey().dimension.equals(currentDimension)) {
                continue;
            }

            Display display = entry.getValue();
            LightPresence presence = lightPresence(minecraft.world, display.pos, display.lightType);
            if (presence == LightPresence.MISSING) {
                iterator.remove();
                continue;
            }

            if (presence == LightPresence.PRESENT && display.tick()) {
                refreshRandomAirBlocks(minecraft.world, display);
            }
        }
    }

    public static List<RangeSnapshot> snapshots(World level)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player == null || minecraft.player.isSneaking() || DISPLAYS.isEmpty()) {
            return Collections.emptyList();
        }

        String currentDimension = dimensionKey(level);
        List<RangeSnapshot> snapshots = new ArrayList<>();
        Iterator<Map.Entry<LightKey, Display>> iterator = DISPLAYS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<LightKey, Display> entry = iterator.next();
            if (!entry.getKey().dimension.equals(currentDimension)) {
                continue;
            }
            Display display = entry.getValue();
            LightPresence presence = lightPresence(level, display.pos, display.lightType);
            if (presence == LightPresence.MISSING) {
                iterator.remove();
                continue;
            }
            if (presence == LightPresence.UNLOADED) {
                continue;
            }
            snapshots.add(new RangeSnapshot(display.pos, rangeBox(level, display), new ArrayList<>(display.randomAirBlocks)));
        }
        return snapshots;
    }

    static LightPresence lightPresence(boolean chunkLoaded, boolean expectedLight)
    {
        if (!chunkLoaded) {
            return LightPresence.UNLOADED;
        }
        return expectedLight ? LightPresence.PRESENT : LightPresence.MISSING;
    }

    private static LightPresence lightPresence(World level, BlockPos pos, LightType lightType)
    {
        boolean chunkLoaded = isLightChunkLoaded(level, pos);
        return lightPresence(chunkLoaded, chunkLoaded && isExpectedLight(level, pos, lightType));
    }

    private static boolean isLightChunkLoaded(World level, BlockPos pos)
    {
        //? if >=1.16.5 {
        return level.isChunkLoaded(pos);
        //?} else if <1.16.5 {
        /*return level.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4);
        *///?}
    }

    private static boolean isExpectedLight(World level, BlockPos pos, LightType lightType)
    {
        return lightType.matchesBlock(level.getBlockState(pos).getBlock());
    }

    private static void refreshRandomAirBlocks(World level, Display display)
    {
        display.randomAirBlocks.clear();
        TorchmasterRangeBoxes.Box box = rangeBox(level, display);
        long width = Math.max(1L, (long)Math.ceil(box.maxX - box.minX));
        long height = Math.max(1L, (long)Math.ceil(box.maxY - box.minY));
        long depth = Math.max(1L, (long)Math.ceil(box.maxZ - box.minZ));
        int target = sampleTarget(width, height, depth);
        int maxAttempts = target * 8;
        int attempts = 0;

        while (display.randomAirBlocks.size() < target && attempts++ < maxAttempts) {
            BlockPos pos = new BlockPos(
                    randomCoordinate(box.minX, width),
                    randomCoordinate(box.minY, height),
                    randomCoordinate(box.minZ, depth));
            if (isAir(level, pos)) {
                display.randomAirBlocks.add(pos.toImmutable());
            }
        }
    }

    private static TorchmasterRangeBoxes.Box rangeBox(World level, Display display)
    {
        if (display.lightType == LightType.MegaTorch && hasDiamondBase(level, display.pos)) {
            return diamondBaseRangeBox(level, display.pos, display.rangeWest, display.rangeEast, display.rangeNorth, display.rangeSouth);
        }
        return TorchmasterRangeBoxes.box(
                display.pos.getX() - display.rangeWest,
                display.pos.getY() - display.rangeDown,
                display.pos.getZ() - display.rangeNorth,
                display.pos.getX() + display.rangeEast + 1,
                display.pos.getY() + display.rangeUp + 1,
                display.pos.getZ() + display.rangeSouth + 1);
    }

    private static boolean hasDiamondBase(World level, BlockPos pos)
    {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof MegaTorchBlock && state.get(MegaTorchBlock.DIAMOND_BASE);
    }

    private static TorchmasterRangeBoxes.Box diamondBaseRangeBox(World level, BlockPos pos, int rangeWest, int rangeEast, int rangeNorth,
            int rangeSouth)
    {
        int chunkWest = chunkRadius(rangeWest);
        int chunkEast = chunkRadius(rangeEast);
        int chunkNorth = chunkRadius(rangeNorth);
        int chunkSouth = chunkRadius(rangeSouth);
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        int minChunkX = chunkX - chunkWest;
        int maxChunkX = chunkX + chunkEast;
        int minChunkZ = chunkZ - chunkNorth;
        int maxChunkZ = chunkZ + chunkSouth;
        int minY = worldBottomY(level);
        int maxY = worldTopYExclusive(level);
        return TorchmasterRangeBoxes.box(
                minChunkX << 4,
                minY,
                minChunkZ << 4,
                (maxChunkX + 1) << 4,
                maxY,
                (maxChunkZ + 1) << 4);
    }

    private static int chunkRadius(int radius)
    {
        return (Math.max(0, radius) + 15) >> 4;
    }

    private static int worldBottomY(World level)
    {
        //? if >=1.17 {
        return level.getBottomY();
        //?} else if <1.17 {
        /*return 0;
        *///?}
    }

    private static int worldTopYExclusive(World level)
    {
        //? if >=1.17 {
        return addClamped(level.getBottomY(), level.getHeight());
        //?} else if <1.17 {
        /*return 256;
        *///?}
    }

    private static int randomCoordinate(double min, long size)
    {
        return addClamped((long)Math.floor(min), (long)Math.floor(RANDOM.nextDouble() * size));
    }

    private static int sampleTarget(long width, long height, long depth)
    {
        long volume = cappedMultiply(cappedMultiply(width, height), depth);
        return (int)Math.min(MAX_RANDOM_AIR_BLOCKS, Math.max(1L, volume / AIR_SAMPLE_DIVISOR));
    }

    private static long cappedMultiply(long left, long right)
    {
        if (left != 0L && right > Long.MAX_VALUE / left) {
            return Long.MAX_VALUE;
        }
        return left * right;
    }

    private static int addClamped(long value, long offset)
    {
        long result = value + offset;
        if (result > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (result < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int)result;
    }

    private static boolean isAir(World level, BlockPos pos)
    {
        //? if >=1.16.5 {
        return level.isAir(pos);
        //?} else if <1.16.5 {
        /*return level.getBlockState(pos).isAir();
        *///?}
    }

    private static String dimensionKey(World level)
    {
        //? if >=1.16.5 {
        return dimensionKey(level.getRegistryKey());
        //?} else if <1.16.5 {
        /*return dimensionKey(level.getDimension().getType());
        *///?}
    }

    //? if >=1.16.5
    private static String dimensionKey(RegistryKey<World> dimension)
    //? if <1.16.5
    //private static String dimensionKey(Object dimension)
    {
        //? if <1.16.5
        /*return dimension == null ? "legacy" : dimension.toString();
*/        /**///? if fabric && forge && >=1.21.11 {
        /*return dimension.identifier().toString();
	*///?} else if >=1.16.5 {
        return dimension.getValue().toString();
        //?}
    }

    public static final class RangeSnapshot
    {
        public final BlockPos pos;
        public final int radius;
        public final TorchmasterRangeBoxes.Box rangeBox;
        public final List<BlockPos> randomAirBlocks;

        RangeSnapshot(BlockPos pos, int radius, List<BlockPos> randomAirBlocks)
        {
            this(pos, TorchmasterRangeBoxes.rangeBox(pos, radius), radius, randomAirBlocks);
        }

        RangeSnapshot(BlockPos pos, TorchmasterRangeBoxes.Box rangeBox, List<BlockPos> randomAirBlocks)
        {
            this(pos, rangeBox, 0, randomAirBlocks);
        }

        private RangeSnapshot(BlockPos pos, TorchmasterRangeBoxes.Box rangeBox, int radius, List<BlockPos> randomAirBlocks)
        {
            this.pos = pos;
            this.radius = radius;
            this.rangeBox = rangeBox;
            this.randomAirBlocks = randomAirBlocks;
        }
    }

    enum LightPresence
    {
        PRESENT,
        UNLOADED,
        MISSING
    }

    private static final class Display
    {
        private final BlockPos pos;
        private LightType lightType;
        private int rangeWest;
        private int rangeEast;
        private int rangeDown;
        private int rangeUp;
        private int rangeNorth;
        private int rangeSouth;
        private final List<BlockPos> randomAirBlocks = new ArrayList<>();
        private int ticks;
        private boolean serverSynced;
        private boolean serverSyncSeen;

        private Display(BlockPos pos, LightType lightType, int radius)
        {
            this(pos, lightType, radius, radius, radius, radius, radius, radius, false);
        }

        private Display(BlockPos pos, LightType lightType, int rangeWest, int rangeEast, int rangeDown, int rangeUp, int rangeNorth,
                int rangeSouth, boolean serverSynced)
        {
            this.pos = pos.toImmutable();
            this.lightType = lightType;
            this.rangeWest = rangeWest;
            this.rangeEast = rangeEast;
            this.rangeDown = rangeDown;
            this.rangeUp = rangeUp;
            this.rangeNorth = rangeNorth;
            this.rangeSouth = rangeSouth;
            this.serverSynced = serverSynced;
            this.serverSyncSeen = serverSynced;
        }

        private boolean tick()
        {
            ticks++;
            if (ticks < RANDOM_REFRESH_INTERVAL_TICKS) {
                return false;
            }
            ticks = 0;
            return true;
        }
    }

    private static final class LightKey
    {
        private final String dimension;
        private final BlockPos pos;

        private LightKey(String dimension, BlockPos pos)
        {
            this.dimension = dimension;
            this.pos = pos.toImmutable();
        }

        @Override
        public boolean equals(Object other)
        {
            if (this == other) {
                return true;
            }
            if (!(other instanceof LightKey)) {
                return false;
            }
            LightKey key = (LightKey)other;
            return dimension.equals(key.dimension) && pos.equals(key.pos);
        }

        @Override
        public int hashCode()
        {
            return 31 * dimension.hashCode() + pos.hashCode();
        }
    }
}
