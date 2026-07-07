package net.xalcon.torchmaster.mixin;

//? if >=1.21.11
//import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.WorldChunk;
import net.xalcon.torchmaster.minecraft.spawn.MinecraftNaturalSpawnHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >=1.21.11
//import java.util.List;

//? if neoforge
@Mixin(SpawnHelper.class)
public abstract class NeoforgeNaturalSpawnerMixin
{
    //? if neoforge && <1.21.11 {
    /*@Inject(
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/SpawnHelper$Info;ZZZ)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void torchmaster_spawn_skipDiamondBaseBlockedNaturalSpawnChunk(
            ServerWorld level,
            WorldChunk chunk,
            SpawnHelper.Info info,
            boolean spawnAnimals,
            boolean spawnMonsters,
            boolean rare,
            CallbackInfo ci)
    {
        torchmaster$cancelDiamondBaseBlockedNaturalSpawnChunk(level, chunk, ci);
    }
    *///?} else {
    @Inject(
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/SpawnHelper$Info;Ljava/util/List;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void torchmaster_spawn_skipDiamondBaseBlockedNaturalSpawnChunk(
            ServerWorld level,
            WorldChunk chunk,
            SpawnHelper.Info info,
            List<SpawnGroup> spawnGroups,
            CallbackInfo ci)
    {
        torchmaster$cancelDiamondBaseBlockedNaturalSpawnChunk(level, chunk, ci);
    }
    //?}

    private static void torchmaster$cancelDiamondBaseBlockedNaturalSpawnChunk(ServerWorld level, WorldChunk chunk, CallbackInfo ci)
    {
        if (MinecraftNaturalSpawnHooks.shouldSkipNaturalSpawnChunk(level, chunk.getPos())) {
            ci.cancel();
        }
    }
}
