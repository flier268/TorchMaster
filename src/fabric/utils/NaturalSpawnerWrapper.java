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
import net.xalcon.torchmaster.events.SpawnEventBridge;

public class NaturalSpawnerWrapper {

    public static boolean isValidEmptySpawnBlock(BlockGetter block, BlockPos pos, BlockState blockState, FluidState fluidState, EntityType<?> entityType, Operation<Boolean> original, ServerPlayer player)
    {
        var container = new EventResultContainer(EventResult.DEFAULT);
        SpawnEventBridge.onPlayerSpawnPhantoms(player, player.position(), container);
        return switch(container.getResult())
        {
            // Make sure we call the origínal
            // otherwise we skip other mods down the chain (if any)
            // We may still run into compat issues in cases where we deny or explicitly allow the spawn,
            // but I'll look at those when necessary.
            case DEFAULT -> original.call(block, pos, blockState, fluidState, entityType);
            case ALLOW -> true;
            case DENY -> false;
        };
    }
}
