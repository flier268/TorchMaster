package net.xalcon.torchmaster.common.logic.entityblocking;

import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.common.ModCaps;

import java.util.concurrent.atomic.AtomicBoolean;

public final class NaturalSpawnBlocker
{
    private NaturalSpawnBlocker()
    {
    }

    public static boolean shouldSkipNaturalSpawn(ServerWorld world, EntityType<?> entityType, BlockPos pos)
    {
        AtomicBoolean blocked = new AtomicBoolean(false);

        world.getCapability(ModCaps.TEB_REGISTRY).ifPresent(reg -> blocked.set(reg.shouldBlockEntity(entityType, pos)));

        boolean log = TorchmasterConfig.GENERAL.logSpawnChecks.get();
        if(log && blocked.get())
        {
            Torchmaster.Log.debug("EarlyNaturalSpawn - Blocking natural spawn of {} at {}", entityType.getRegistryName(), pos);
        }

        return blocked.get();
    }

    public static boolean shouldSkipNaturalSpawnPosition(ServerWorld world, BlockPos pos)
    {
        AtomicBoolean blocked = new AtomicBoolean(false);

        world.getCapability(ModCaps.TEB_REGISTRY).ifPresent(reg -> blocked.set(reg.shouldBlockNaturalSpawnPosition(pos)));

        boolean log = TorchmasterConfig.GENERAL.logSpawnChecks.get();
        if(log && blocked.get())
        {
            Torchmaster.Log.debug("EarlyNaturalSpawnPosition - Blocking natural spawn position at {}", pos);
        }

        return blocked.get();
    }

    public static boolean shouldSkipNaturalSpawnChunk(ServerWorld world, ChunkPos chunkPos)
    {
        AtomicBoolean blocked = new AtomicBoolean(false);

        world.getCapability(ModCaps.TEB_REGISTRY).ifPresent(reg -> blocked.set(reg.shouldBlockNaturalSpawnChunk(chunkPos.x, chunkPos.z)));

        boolean log = TorchmasterConfig.GENERAL.logSpawnChecks.get();
        if(log && blocked.get())
        {
            Torchmaster.Log.debug("EarlyNaturalSpawnChunk - Blocking natural spawn chunk {}", chunkPos);
        }

        return blocked.get();
    }
}
