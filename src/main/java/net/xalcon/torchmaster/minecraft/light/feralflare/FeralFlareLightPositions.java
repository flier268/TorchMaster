package net.xalcon.torchmaster.minecraft.light.feralflare;

import net.minecraft.util.math.BlockPos;
import net.xalcon.torchmaster.domain.FeralFlareLightPlanner;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftAdapterViews;

import java.util.ArrayList;
import java.util.List;

public final class FeralFlareLightPositions {
    private FeralFlareLightPositions() {
    }

    public static int[] encode(BlockPos origin, List<BlockPos> childLights) {
        int[] encoded = new int[childLights.size()];
        for (int i = 0; i < childLights.size(); i++) {
            encoded[i] = FeralFlareLightPlanner.encodeRelativePosition(
                    MinecraftAdapterViews.blockPos(origin),
                    MinecraftAdapterViews.blockPos(childLights.get(i)));
        }
        return encoded;
    }

    public static List<BlockPos> decode(BlockPos origin, int[] encodedLights) {
        List<BlockPos> childLights = new ArrayList<>(encodedLights.length);
        for (int encodedLight : encodedLights) {
            childLights.add(MinecraftAdapterViews.blockPos(
                    FeralFlareLightPlanner.decodeRelativePosition(MinecraftAdapterViews.blockPos(origin), encodedLight)));
        }
        return childLights;
    }
}
