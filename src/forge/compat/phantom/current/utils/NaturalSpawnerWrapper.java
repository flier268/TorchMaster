package net.xalcon.torchmaster.utils;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.TorchmasterEventHandler;

public class NaturalSpawnerWrapper {

    public static boolean isValidEmptySpawnBlock(BlockGetter block, BlockPos pos, BlockState blockState, FluidState fluidState, EntityType<?> entityType, Operation<Boolean> original, ServerPlayer player)
    {
        EventResultContainer container = new EventResultContainer(EventResult.DEFAULT);
        TorchmasterEventHandler.onPlayerSpawnPhantoms(player, player.position(), container);
        switch(container.getResult())
        {
            case DEFAULT:
                return original.call(block, pos, blockState, fluidState, entityType);
            case ALLOW:
                return true;
            case DENY:
                return false;
            default:
                throw new IllegalStateException("Unhandled event result " + container.getResult());
        }
    }
}
