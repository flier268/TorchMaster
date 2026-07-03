package net.xalcon.torchmaster.mixin;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.xalcon.torchmaster.common.logic.entityblocking.NaturalSpawnBlocker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldEntitySpawner.class)
public abstract class WorldEntitySpawnerMixin
{
    @Inject(method = "func_234979_a_", at = @At("HEAD"), cancellable = true)
    private static void torchmaster$skipDiamondBaseBlockedNaturalSpawnChunk(
        ServerWorld world,
        Chunk chunk,
        WorldEntitySpawner.EntityDensityManager densityManager,
        boolean spawnHostiles,
        boolean spawnPassives,
        boolean spawnAnimals,
        CallbackInfo ci)
    {
        if(NaturalSpawnBlocker.shouldSkipNaturalSpawnChunk(world, chunk.getPos()))
            ci.cancel();
    }

    @Invoker("func_234978_a_")
    private static boolean torchmaster$invokeCanSpawnAtPosition(ServerWorld world, IChunk chunk, BlockPos.Mutable pos, double distance)
    {
        throw new AssertionError();
    }

    @Redirect(
        method = "func_234966_a_",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/spawner/WorldEntitySpawner;func_234978_a_(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/chunk/IChunk;Lnet/minecraft/util/math/BlockPos$Mutable;D)Z"
        )
    )
    private static boolean torchmaster$skipDiamondBaseBlockedNaturalSpawnPosition(
        ServerWorld world,
        IChunk chunk,
        BlockPos.Mutable pos,
        double distance,
        EntityClassification classification,
        ServerWorld originalWorld,
        IChunk spawningChunk,
        BlockPos initialPos,
        WorldEntitySpawner.IDensityCheck densityCheck,
        WorldEntitySpawner.IOnSpawnDensityAdder densityAdder)
    {
        return !NaturalSpawnBlocker.shouldSkipNaturalSpawnPosition(world, pos)
            && torchmaster$invokeCanSpawnAtPosition(world, chunk, pos, distance);
    }

    @Redirect(
        method = "func_234966_a_",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/spawner/WorldEntitySpawner$IDensityCheck;test(Lnet/minecraft/entity/EntityType;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/chunk/IChunk;)Z"
        )
    )
    private static boolean torchmaster$skipMegaTorchBlockedNaturalSpawn(
        WorldEntitySpawner.IDensityCheck densityCheck,
        EntityType<?> entityType,
        BlockPos pos,
        IChunk chunk,
        EntityClassification classification,
        ServerWorld world,
        IChunk spawningChunk,
        BlockPos initialPos,
        WorldEntitySpawner.IDensityCheck originalDensityCheck,
        WorldEntitySpawner.IOnSpawnDensityAdder densityAdder)
    {
        return !NaturalSpawnBlocker.shouldSkipNaturalSpawn(world, entityType, pos)
            && densityCheck.test(entityType, pos, chunk);
    }
}
