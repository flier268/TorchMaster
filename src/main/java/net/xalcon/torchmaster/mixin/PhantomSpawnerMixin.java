package net.xalcon.torchmaster.mixin;

//? if fabric && >=1.21.9 {
/*import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.material.FluidState;
import net.xalcon.torchmaster.utils.NaturalSpawnerWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {
    private static final ThreadLocal<ServerPlayer> torchmaster$currentPhantomPlayer = new ThreadLocal<>();

    @Redirect(
            method = "tick(Lnet/minecraft/server/level/ServerLevel;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;blockPosition()Lnet/minecraft/core/BlockPos;"
            )
    )
    private BlockPos torchmaster_tick_playerBlockPosition(ServerPlayer player)
    {
        torchmaster$currentPhantomPlayer.set(player);
        return player.blockPosition();
    }

    @Redirect(
            method = "tick(Lnet/minecraft/server/level/ServerLevel;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/NaturalSpawner;isValidEmptySpawnBlock(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/entity/EntityType;)Z"
            )
    )
    private static boolean torchmaster_tick_NaturalSpawner__isValidEmptySpawnBlock(BlockGetter block, BlockPos pos, BlockState blockState, FluidState fluidState, EntityType<?> entityType)
    {
        ServerPlayer player = torchmaster$currentPhantomPlayer.get();
        if(player == null)
            return NaturalSpawner.isValidEmptySpawnBlock(block, pos, blockState, fluidState, entityType);

        return switch(NaturalSpawnerWrapper.isValidEmptySpawnBlock(player))
        {
            case DEFAULT -> NaturalSpawner.isValidEmptySpawnBlock(block, pos, blockState, fluidState, entityType);
            case ALLOW -> true;
            case DENY -> false;
        };
    }

    @Inject(method = "tick(Lnet/minecraft/server/level/ServerLevel;Z)V", at = @At("RETURN"))
    private void torchmaster_tick_clearCurrentPlayer(ServerLevel level, boolean spawnEnemies, CallbackInfo ci)
    {
        torchmaster$currentPhantomPlayer.remove();
    }
}
*///?} else if fabric && >=1.21.5 {
/*import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.material.FluidState;
import net.xalcon.torchmaster.utils.NaturalSpawnerWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {
    private static final ThreadLocal<ServerPlayer> torchmaster$currentPhantomPlayer = new ThreadLocal<>();

    @Redirect(
            method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;blockPosition()Lnet/minecraft/core/BlockPos;"
            )
    )
    private BlockPos torchmaster_tick_playerBlockPosition(ServerPlayer player)
    {
        torchmaster$currentPhantomPlayer.set(player);
        return player.blockPosition();
    }

    @Redirect(
            method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/NaturalSpawner;isValidEmptySpawnBlock(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/entity/EntityType;)Z"
            )
    )
    private static boolean torchmaster_tick_NaturalSpawner__isValidEmptySpawnBlock(BlockGetter block, BlockPos pos, BlockState blockState, FluidState fluidState, EntityType<?> entityType)
    {
        ServerPlayer player = torchmaster$currentPhantomPlayer.get();
        if(player == null)
            return NaturalSpawner.isValidEmptySpawnBlock(block, pos, blockState, fluidState, entityType);

        return switch(NaturalSpawnerWrapper.isValidEmptySpawnBlock(player))
        {
            case DEFAULT -> NaturalSpawner.isValidEmptySpawnBlock(block, pos, blockState, fluidState, entityType);
            case ALLOW -> true;
            case DENY -> false;
        };
    }

    @Inject(method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)V", at = @At("RETURN"))
    private void torchmaster_tick_clearCurrentPlayer(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies, CallbackInfo ci)
    {
        torchmaster$currentPhantomPlayer.remove();
    }
}
*///?} else if fabric && >=1.21 {
/*import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.material.FluidState;
import net.xalcon.torchmaster.utils.NaturalSpawnerWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {
    private static final ThreadLocal<ServerPlayer> torchmaster$currentPhantomPlayer = new ThreadLocal<>();

    @Redirect(
            method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;blockPosition()Lnet/minecraft/core/BlockPos;"
            )
    )
    private BlockPos torchmaster_tick_playerBlockPosition(ServerPlayer player)
    {
        torchmaster$currentPhantomPlayer.set(player);
        return player.blockPosition();
    }

    @Redirect(
            method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/NaturalSpawner;isValidEmptySpawnBlock(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/entity/EntityType;)Z"
            )
    )
    private static boolean torchmaster_tick_NaturalSpawner__isValidEmptySpawnBlock(BlockGetter block, BlockPos pos, BlockState blockState, FluidState fluidState, EntityType<?> entityType)
    {
        ServerPlayer player = torchmaster$currentPhantomPlayer.get();
        if(player == null)
            return NaturalSpawner.isValidEmptySpawnBlock(block, pos, blockState, fluidState, entityType);

        return switch(NaturalSpawnerWrapper.isValidEmptySpawnBlock(player))
        {
            case DEFAULT -> NaturalSpawner.isValidEmptySpawnBlock(block, pos, blockState, fluidState, entityType);
            case ALLOW -> true;
            case DENY -> false;
        };
    }

    @Inject(method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I", at = @At("RETURN"))
    private void torchmaster_tick_clearCurrentPlayer(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies, CallbackInfoReturnable<Integer> cir)
    {
        torchmaster$currentPhantomPlayer.remove();
    }
}
*///?} else if fabric {
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.material.FluidState;
import net.xalcon.torchmaster.utils.NaturalSpawnerWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {
    private static final ThreadLocal<ServerPlayer> torchmaster$currentPhantomPlayer = new ThreadLocal<>();

    @Redirect(
            method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;blockPosition()Lnet/minecraft/core/BlockPos;"
            )
    )
    private BlockPos torchmaster_tick_playerBlockPosition(Player player)
    {
        torchmaster$currentPhantomPlayer.set((ServerPlayer) player);
        return player.blockPosition();
    }

    @Redirect(
            method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/NaturalSpawner;isValidEmptySpawnBlock(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/entity/EntityType;)Z"
            )
    )
    private static boolean torchmaster_tick_NaturalSpawner__isValidEmptySpawnBlock(BlockGetter block, BlockPos pos, BlockState blockState, FluidState fluidState, EntityType<?> entityType)
    {
        ServerPlayer player = torchmaster$currentPhantomPlayer.get();
        if(player == null)
            return NaturalSpawner.isValidEmptySpawnBlock(block, pos, blockState, fluidState, entityType);

        return switch(NaturalSpawnerWrapper.isValidEmptySpawnBlock(player))
        {
            case DEFAULT -> NaturalSpawner.isValidEmptySpawnBlock(block, pos, blockState, fluidState, entityType);
            case ALLOW -> true;
            case DENY -> false;
        };
    }

    @Inject(method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I", at = @At("RETURN"))
    private void torchmaster_tick_clearCurrentPlayer(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies, CallbackInfoReturnable<Integer> cir)
    {
        torchmaster$currentPhantomPlayer.remove();
    }
}
//?}
