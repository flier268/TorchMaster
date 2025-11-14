package net.xalcon.torchmaster.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.xalcon.torchmaster.utils.PatrolSpawnerWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PatrolSpawner.class)
public abstract class PatrolSpawnerMixin {

    @Inject(
            method = "spawnPatrolMember(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;Z)Z",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/world/entity/monster/PatrollingMonster.setPos (DDD)V"
            ),
            cancellable = true
    )
    private void torchmaster_spawnPatrolMember_afterPillagerCreate(ServerLevel level, BlockPos pos, RandomSource random, boolean leader, CallbackInfoReturnable<Boolean> cir, @Local LocalRef<PatrollingMonster> patrollingMonster)
    {
        PatrolSpawnerWrapper.spawnPatrolMemberBeforeSetPos(level, pos, random, leader, cir, patrollingMonster);
    }

}
