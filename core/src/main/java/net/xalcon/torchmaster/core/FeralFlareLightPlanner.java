package net.xalcon.torchmaster.core;

import net.xalcon.torchmaster.adapter.BlockPosView;

public final class FeralFlareLightPlanner {
    private FeralFlareLightPlanner() {
    }

    public static boolean shouldPlaceLight(int currentLightCount, int hardcap, int lightLevel, int minimumLightLevel) {
        return currentLightCount < hardcap && lightLevel < minimumLightLevel;
    }

    public static boolean shouldRemoveChildLight(BlockPosView childLight, boolean blockStillExists) {
        return childLight != null && !blockStillExists;
    }
}
