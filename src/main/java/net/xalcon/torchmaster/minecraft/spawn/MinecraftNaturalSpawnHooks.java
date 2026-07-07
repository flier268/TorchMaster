package net.xalcon.torchmaster.minecraft.spawn;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnBlocker;

public final class MinecraftNaturalSpawnHooks
{
    private MinecraftNaturalSpawnHooks()
    {
    }

    public static boolean shouldSkipNaturalSpawnChunk(World level, ChunkPos chunkPos)
    {
        return MinecraftSpawnBlocker.shouldBlockNaturalSpawnChunk(level, chunkPos);
    }
}
