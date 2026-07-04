//? if >=1.16 {
package net.xalcon.torchmaster.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.xalcon.torchmaster.ModRegistry;
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

    public static boolean isVisible(ResourceKey<Level> dimension, BlockPos pos)
    {
        return DISPLAYS.containsKey(new LightKey(dimensionKey(dimension), pos));
    }

    public static boolean toggle(ResourceKey<Level> dimension, BlockPos pos, LightType lightType, int radius)
    {
        LightKey key = new LightKey(dimensionKey(dimension), pos);
        if (DISPLAYS.remove(key) != null) {
            return false;
        }
        Display display = new Display(pos, lightType, Math.max(0, radius));
        DISPLAYS.put(key, display);
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) {
            refreshRandomAirBlocks(minecraft.level, display);
        }
        return true;
    }

    public static void remove(ResourceKey<Level> dimension, BlockPos pos)
    {
        DISPLAYS.remove(new LightKey(dimensionKey(dimension), pos));
    }

    public static void tick()
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null || DISPLAYS.isEmpty()) {
            return;
        }
        if (minecraft.player.isShiftKeyDown()) {
            return;
        }

        long gameTime = minecraft.level.getGameTime();
        String currentDimension = dimensionKey(minecraft.level.dimension());
        Iterator<Map.Entry<LightKey, Display>> iterator = DISPLAYS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<LightKey, Display> entry = iterator.next();
            if (!entry.getKey().dimension.equals(currentDimension)) {
                continue;
            }

            Display display = entry.getValue();
            if (!isExpectedLight(minecraft.level, display.pos, display.lightType)) {
                iterator.remove();
                continue;
            }

            if (gameTime % RANDOM_REFRESH_INTERVAL_TICKS == 0) {
                refreshRandomAirBlocks(minecraft.level, display);
            }
        }
    }

    public static List<RangeSnapshot> snapshots(Level level)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.player.isShiftKeyDown() || DISPLAYS.isEmpty()) {
            return Collections.emptyList();
        }

        String currentDimension = dimensionKey(level.dimension());
        List<RangeSnapshot> snapshots = new ArrayList<>();
        for (Map.Entry<LightKey, Display> entry : DISPLAYS.entrySet()) {
            if (!entry.getKey().dimension.equals(currentDimension)) {
                continue;
            }
            Display display = entry.getValue();
            snapshots.add(new RangeSnapshot(display.pos, display.radius, new ArrayList<>(display.randomAirBlocks)));
        }
        return snapshots;
    }

    private static boolean isExpectedLight(Level level, BlockPos pos, LightType lightType)
    {
        if (lightType == LightType.MegaTorch) {
            return level.getBlockState(pos).getBlock() == ModRegistry.blockMegaTorch.get();
        }
        if (lightType == LightType.DreadLamp) {
            return level.getBlockState(pos).getBlock() == ModRegistry.blockDreadLamp.get();
        }
        return false;
    }

    private static void refreshRandomAirBlocks(Level level, Display display)
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
            if (level.isEmptyBlock(pos)) {
                display.randomAirBlocks.add(pos.immutable());
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

    private static String dimensionKey(ResourceKey<Level> dimension)
    {
        //? if >=1.21.11 {
        /*return dimension.identifier().toString();
*///?} else {
        return dimension.location().toString();
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

        private Display(BlockPos pos, LightType lightType, int radius)
        {
            this.pos = pos.immutable();
            this.lightType = lightType;
            this.radius = radius;
        }
    }

    private static final class LightKey
    {
        private final String dimension;
        private final BlockPos pos;

        private LightKey(String dimension, BlockPos pos)
        {
            this.dimension = dimension;
            this.pos = pos.immutable();
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
//?}
