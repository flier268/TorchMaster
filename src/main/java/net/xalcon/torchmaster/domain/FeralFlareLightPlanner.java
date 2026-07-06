package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.BlockPosView;

public final class FeralFlareLightPlanner {
    private FeralFlareLightPlanner() {
    }

    public static boolean shouldTick(boolean clientSide, int ticks, int tickRate) {
        return !clientSide && tickRate > 0 && ticks % tickRate == 0;
    }

    public static boolean shouldPlaceLight(int currentLightCount, int hardcap, int lightLevel, int minimumLightLevel) {
        return currentLightCount < hardcap && lightLevel < minimumLightLevel;
    }

    public static boolean shouldRemoveChildLight(BlockPosView childLight, boolean blockStillExists) {
        return childLight != null && !blockStillExists;
    }

    public static TickDecision beginTick(
            boolean clientSide,
            int ticks,
            int checkIndex,
            int tickRate,
            int childLightCount,
            int hardcap
    ) {
        int nextTicks = ticks + 1;
        if (!shouldTick(clientSide, nextTicks, tickRate)) {
            return TickDecision.skip(nextTicks, checkIndex);
        }
        if (childLightCount > hardcap) {
            return TickDecision.skip(nextTicks, checkIndex);
        }
        return TickDecision.run(0, checkIndex);
    }

    public static int nextCheckIndex(int checkIndex, int childLightCount) {
        return (checkIndex + 1) % childLightCount;
    }

    public static int encodeRelativePosition(BlockPosView origin, BlockPosView target) {
        int x = target.x() - origin.x();
        int y = target.y() - origin.y();
        int z = target.z() - origin.z();
        return ((x & 0xFF) << 16) + ((y & 0xFF) << 8) + (z & 0xFF);
    }

    public static BlockPosView decodeRelativePosition(BlockPosView origin, int encoded) {
        int x = (byte)((encoded >> 16) & 0xFF);
        int y = (byte)((encoded >> 8) & 0xFF);
        int z = (byte)(encoded & 0xFF);
        return new BlockPosView(origin.x() + x, origin.y() + y, origin.z() + z);
    }

    public static final class TickDecision {
        private final boolean runCycle;
        private final int ticks;
        private final int checkIndex;

        private TickDecision(boolean runCycle, int ticks, int checkIndex) {
            this.runCycle = runCycle;
            this.ticks = ticks;
            this.checkIndex = checkIndex;
        }

        private static TickDecision run(int ticks, int checkIndex) {
            return new TickDecision(true, ticks, checkIndex);
        }

        private static TickDecision skip(int ticks, int checkIndex) {
            return new TickDecision(false, ticks, checkIndex);
        }

        public boolean runCycle() {
            return runCycle;
        }

        public int ticks() {
            return ticks;
        }

        public int checkIndex() {
            return checkIndex;
        }
    }
}
