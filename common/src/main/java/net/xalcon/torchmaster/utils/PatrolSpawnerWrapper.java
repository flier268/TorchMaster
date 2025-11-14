package net.xalcon.torchmaster.utils;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.TorchmasterEventHandler;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class PatrolSpawnerWrapper {
    public static void spawnPatrolMemberBeforeSetPos(ServerLevel level, BlockPos pos, RandomSource random, boolean leader, CallbackInfoReturnable<Boolean> cir, LocalRef<PatrollingMonster> patrollingMonster)
    {
        var container = new EventResultContainer(EventResult.DEFAULT);
        TorchmasterEventHandler.onPatrolMemberSpawn(level, pos.getBottomCenter(), leader, patrollingMonster.get(), container);
        if(container.getResult() == EventResult.DENY)
        {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
