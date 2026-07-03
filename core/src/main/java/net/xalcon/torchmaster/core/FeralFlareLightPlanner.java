package net.xalcon.torchmaster.core;

import net.xalcon.torchmaster.adapter.BlockPosView;

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
}
