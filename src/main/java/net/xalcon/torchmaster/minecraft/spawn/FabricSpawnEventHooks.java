package net.xalcon.torchmaster.minecraft.spawn;

// Fabric mixins keep redirect signatures local; this helper owns the shared hook body.
//? if fabric && >=1.16.5 {
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnBlocker;
//?} else if fabric {
/*import net.minecraft.entity.SpawnType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnBlocker;
*///?}

public final class FabricSpawnEventHooks
{
    private FabricSpawnEventHooks()
    {
    }

    //? if fabric && >=1.16.5 {
    public static EventResult checkSpawnRules(MobEntity mob, SpawnReason spawnReason)
    {
        return MinecraftSpawnEventContainers.invokeDefault(container -> MinecraftSpawnEventHooks.onCheckSpawn(spawnReason, mob,
                //? if >=1.21.11
                //mob.getEntityPos()
                //? if <1.21.11
                mob.getPos()
                , container));
    }
    //?} else if fabric {
    /*public static EventResult checkSpawnRules(MobEntity mob, SpawnType spawnReason)
    {
        return MinecraftSpawnEventContainers.invokeDefault(container ->
                MinecraftSpawnEventHooks.onCheckSpawn(spawnReason, mob, mob.getPos(), container));
    }
    *///?}

    //? if fabric {
    public static EventResult checkPhantomSpawn(ServerPlayerEntity player)
    {
        //? if >=1.21.11 {
        /*Vec3d position = player.getEntityPos();
        *///?} else {
        Vec3d position = player.getPos();
        //?}
        return MinecraftSpawnEventContainers.invokeDefault(container ->
                MinecraftSpawnEventHooks.onPlayerSpawnPhantoms(player, position, container));
    }
    //?}

    //? if fabric {
    public static boolean shouldSkipNaturalSpawnPosition(World level, BlockPos pos)
    {
        return MinecraftSpawnBlocker.shouldBlockNaturalSpawnPosition(level, pos);
    }

    public static boolean shouldSkipNaturalSpawnChunk(World level, ChunkPos chunkPos)
    {
        return MinecraftNaturalSpawnHooks.shouldSkipNaturalSpawnChunk(level, chunkPos);
    }
    //?}
}
