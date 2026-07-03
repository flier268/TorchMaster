package net.xalcon.torchmaster.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NaturalSpawner;
import net.xalcon.torchmaster.utils.MobWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerMixin
{
    @WrapOperation(
            method = "net/minecraft/world/level/NaturalSpawner.isValidPositionForMob(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Mob;D)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;checkSpawnRules(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;)Z"
            )
    )
    private static boolean torchmaster_isValidPositionForMob_checkSpawnRules(Mob mob, LevelAccessor level, EntitySpawnReason spawnReason, Operation<Boolean> original)
    {
        return MobWrapper.checkSpawnRules(mob, level, spawnReason, original);
    }

    @WrapOperation(
            method = "spawnMobsForChunkGeneration(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/Holder;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/util/RandomSource;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;checkSpawnRules(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;)Z"
            )
    )
    private static boolean torchmaster_spawnMobsForChunkGeneration_checkSpawnRules(Mob mob, LevelAccessor level, EntitySpawnReason spawnReason, Operation<Boolean> original)
    {
        return MobWrapper.checkSpawnRules(mob, level, spawnReason, original);
    }
}
