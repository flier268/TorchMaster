package net.xalcon.torchmaster.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.material.FluidState;
import net.xalcon.torchmaster.utils.NaturalSpawnerWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {

    @WrapOperation(
            method = "tick(Lnet/minecraft/server/level/ServerLevel;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/world/level/NaturalSpawner.isValidEmptySpawnBlock(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/entity/EntityType;)Z"
            )
    )
    private static boolean torchmaster_tick_NaturalSpawner__isValidEmptySpawnBlock(BlockGetter block, BlockPos pos, BlockState blockState, FluidState fluidState, EntityType<?> entityType, Operation<Boolean> original, @Local ServerPlayer player)
    {
        return NaturalSpawnerWrapper.isValidEmptySpawnBlock(block, pos, blockState, fluidState, entityType, original, player);
    }
}
