package net.xalcon.torchmaster.client;

import net.minecraft.client.MinecraftClient;
//? if >=1.19.4
import net.minecraft.registry.RegistryKey;
//? if >=1.16.5 && <1.19.4
//import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.blocks.LightType;

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
        return DISPLAYS.containsKey(new LightKey(dimensionKey(dimension), pos));
    }

    //? if >=1.16.5
    public static boolean toggle(RegistryKey<World> dimension, BlockPos pos, LightType lightType, int radius)
    //? if <1.16.5
    //public static boolean toggle(Object dimension, BlockPos pos, LightType lightType, int radius)
    {
        LightKey key = new LightKey(dimensionKey(dimension), pos);
        if (DISPLAYS.remove(key) != null) {
            return false;
        }
        Display display = new Display(pos, lightType, Math.max(0, radius));
        DISPLAYS.put(key, display);
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.world != null) {
            refreshRandomAirBlocks(minecraft.world, display);
        }
        return true;
    }

    //? if >=1.16.5
    public static void remove(RegistryKey<World> dimension, BlockPos pos)
    //? if <1.16.5
    //public static void remove(Object dimension, BlockPos pos)
    {
        DISPLAYS.remove(new LightKey(dimensionKey(dimension), pos));
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
            if (!isExpectedLight(minecraft.world, display.pos, display.lightType)) {
                iterator.remove();
                continue;
            }

            if (display.tick()) {
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
            if (!isExpectedLight(level, display.pos, display.lightType)) {
                iterator.remove();
                continue;
            }
            snapshots.add(new RangeSnapshot(display.pos, display.radius, new ArrayList<>(display.randomAirBlocks)));
        }
        return snapshots;
    }

    private static boolean isExpectedLight(World level, BlockPos pos, LightType lightType)
    {
        return lightType.matchesBlock(level.getBlockState(pos).getBlock());
    }

    private static void refreshRandomAirBlocks(World level, Display display)
    {
        display.randomAirBlocks.clear();
        int radius = display.radius;
        long diameter = (long)radius * 2L + 1L;
        int target = sampleTarget(diameter);
        int maxAttempts = target * 8;
        int attempts = 0;

        while (display.randomAirBlocks.size() < target && attempts++ < maxAttempts) {
            int dx = randomOffset(radius, diameter);
            int dy = randomOffset(radius, diameter);
            int dz = randomOffset(radius, diameter);
            BlockPos pos = new BlockPos(
                    addClamped(display.pos.getX(), dx),
                    addClamped(display.pos.getY(), dy),
                    addClamped(display.pos.getZ(), dz));
            if (isAir(level, pos)) {
                display.randomAirBlocks.add(pos.toImmutable());
            }
        }
    }

    private static int randomOffset(int radius, long diameter)
    {
        return (int)(Math.floor(RANDOM.nextDouble() * diameter) - radius);
    }

    private static int sampleTarget(long diameter)
    {
        long volume = cappedMultiply(cappedMultiply(diameter, diameter), diameter);
        return (int)Math.min(MAX_RANDOM_AIR_BLOCKS, Math.max(1L, volume / AIR_SAMPLE_DIVISOR));
    }

    private static long cappedMultiply(long left, long right)
    {
        if (left != 0L && right > Long.MAX_VALUE / left) {
            return Long.MAX_VALUE;
        }
        return left * right;
    }

    private static int addClamped(int value, int offset)
    {
        long result = (long)value + offset;
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
        //?} else {
        /*return level.getBlockState(pos).isAir();
        *///?}
    }

    private static String dimensionKey(World level)
    {
        //? if >=1.16.5 {
        return dimensionKey(level.getRegistryKey());
        //?} else {
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
        public final List<BlockPos> randomAirBlocks;

        private RangeSnapshot(BlockPos pos, int radius, List<BlockPos> randomAirBlocks)
        {
            this.pos = pos;
            this.radius = radius;
            this.randomAirBlocks = randomAirBlocks;
        }
    }

    private static final class Display
    {
        private final BlockPos pos;
        private final LightType lightType;
        private final int radius;
        private final List<BlockPos> randomAirBlocks = new ArrayList<>();
        private int ticks;

        private Display(BlockPos pos, LightType lightType, int radius)
        {
            this.pos = pos.toImmutable();
            this.lightType = lightType;
            this.radius = radius;
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
