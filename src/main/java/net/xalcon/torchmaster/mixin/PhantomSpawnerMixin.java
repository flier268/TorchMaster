package net.xalcon.torchmaster.mixin;

//? if fabric && >=1.20.6 {
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.spawner.PhantomSpawner;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftEventResultDecisions;
import net.xalcon.torchmaster.minecraft.spawn.FabricSpawnEventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
//? if >=1.21.11
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if <1.21.11
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {
    private static final ThreadLocal<ServerPlayerEntity> torchmaster$currentPhantomPlayer = new ThreadLocal<>();

    @Redirect(
            //? if >=1.21.11
            //method = "spawn(Lnet/minecraft/server/world/ServerWorld;Z)V",
            //? if <1.21.11
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;getBlockPos()Lnet/minecraft/util/math/BlockPos;"
            )
    )
    private BlockPos torchmaster_tick_playerBlockPosition(ServerPlayerEntity player)
    {
        torchmaster$currentPhantomPlayer.set(player);
        return player.getBlockPos();
    }

    @Redirect(
            //? if >=1.21.11
            //method = "spawn(Lnet/minecraft/server/world/ServerWorld;Z)V",
            //? if <1.21.11
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/SpawnHelper;isClearForSpawn(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;Lnet/minecraft/entity/EntityType;)Z"
            )
    )
    private boolean torchmaster_tick_NaturalSpawner__isValidEmptySpawnBlock(BlockView block, BlockPos pos, BlockState blockState, FluidState fluidState, EntityType<?> entityType)
    {
        ServerPlayerEntity player = torchmaster$currentPhantomPlayer.get();
        if(player == null)
            return SpawnHelper.isClearForSpawn(block, pos, blockState, fluidState, entityType);

        return MinecraftEventResultDecisions.resolve(
                FabricSpawnEventHooks.checkPhantomSpawn(player),
                () -> SpawnHelper.isClearForSpawn(block, pos, blockState, fluidState, entityType));
    }

    //? if >=1.21.11 {
    /*@Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;Z)V", at = @At("RETURN"))
    private void torchmaster_tick_clearCurrentPlayer(ServerWorld level, boolean spawnEnemies, CallbackInfo ci)
    {
        torchmaster$currentPhantomPlayer.remove();
    }
    *///?} else {
    @Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I", at = @At("RETURN"))
    private void torchmaster_tick_clearCurrentPlayer(ServerWorld level, boolean spawnEnemies, boolean spawnFriendlies, CallbackInfoReturnable<Integer> cir)
    {
        torchmaster$currentPhantomPlayer.remove();
    }
    //?}
}
//?} else if fabric && >=1.18.2 {
/*import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.spawner.PhantomSpawner;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftEventResultDecisions;
import net.xalcon.torchmaster.minecraft.spawn.FabricSpawnEventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {
    private static final ThreadLocal<ServerPlayerEntity> torchmaster$currentPhantomPlayer = new ThreadLocal<>();

    @Redirect(
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getBlockPos()Lnet/minecraft/util/math/BlockPos;"
            )
    )
    private BlockPos torchmaster_tick_playerBlockPosition(PlayerEntity player)
    {
        torchmaster$currentPhantomPlayer.set((ServerPlayerEntity) player);
        return player.getBlockPos();
    }

    @Redirect(
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/SpawnHelper;isClearForSpawn(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;Lnet/minecraft/entity/EntityType;)Z"
            )
    )
    private boolean torchmaster_tick_NaturalSpawner__isValidEmptySpawnBlock(BlockView block, BlockPos pos, BlockState blockState, FluidState fluidState, EntityType<?> entityType)
    {
        ServerPlayerEntity player = torchmaster$currentPhantomPlayer.get();
        if(player == null)
            return SpawnHelper.isClearForSpawn(block, pos, blockState, fluidState, entityType);

        return MinecraftEventResultDecisions.resolve(
                FabricSpawnEventHooks.checkPhantomSpawn(player),
                () -> SpawnHelper.isClearForSpawn(block, pos, blockState, fluidState, entityType));
    }

    @Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I", at = @At("RETURN"))
    private void torchmaster_tick_clearCurrentPlayer(ServerWorld level, boolean spawnEnemies, boolean spawnFriendlies, CallbackInfoReturnable<Integer> cir)
    {
        torchmaster$currentPhantomPlayer.remove();
    }
}
*///?} else if fabric && >=1.16.5 {
/*import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.gen.PhantomSpawner;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftEventResultDecisions;
import net.xalcon.torchmaster.minecraft.spawn.FabricSpawnEventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {
    private static final ThreadLocal<ServerPlayerEntity> torchmaster$currentPhantomPlayer = new ThreadLocal<>();

    @Redirect(
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getBlockPos()Lnet/minecraft/util/math/BlockPos;"
            )
    )
    private BlockPos torchmaster_tick_playerBlockPosition(PlayerEntity player)
    {
        torchmaster$currentPhantomPlayer.set((ServerPlayerEntity) player);
        return player.getBlockPos();
    }

    @Redirect(
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/SpawnHelper;isClearForSpawn(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;Lnet/minecraft/entity/EntityType;)Z"
            )
    )
    private boolean torchmaster_tick_NaturalSpawner__isValidEmptySpawnBlock(BlockView block, BlockPos pos, BlockState blockState, FluidState fluidState, EntityType<?> entityType)
    {
        ServerPlayerEntity player = torchmaster$currentPhantomPlayer.get();
        if(player == null)
            return SpawnHelper.isClearForSpawn(block, pos, blockState, fluidState, entityType);

        return MinecraftEventResultDecisions.resolve(
                FabricSpawnEventHooks.checkPhantomSpawn(player),
                () -> SpawnHelper.isClearForSpawn(block, pos, blockState, fluidState, entityType));
    }

    @Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I", at = @At("RETURN"))
    private void torchmaster_tick_clearCurrentPlayer(ServerWorld level, boolean spawnEnemies, boolean spawnFriendlies, CallbackInfoReturnable<Integer> cir)
    {
        torchmaster$currentPhantomPlayer.remove();
    }
}
*///?} else if fabric {
/*import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.gen.PhantomSpawner;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftEventResultDecisions;
import net.xalcon.torchmaster.minecraft.spawn.FabricSpawnEventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {
    private static final ThreadLocal<ServerPlayerEntity> torchmaster$currentPhantomPlayer = new ThreadLocal<>();

    @Redirect(
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/util/math/BlockPos"
            )
    )
    private BlockPos torchmaster_tick_playerBlockPosition(Entity player)
    {
        torchmaster$currentPhantomPlayer.set((ServerPlayerEntity) player);
        return new BlockPos(player);
    }

    @Redirect(
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/SpawnHelper;isClearForSpawn(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)Z"
            )
    )
    private boolean torchmaster_tick_NaturalSpawner__isValidEmptySpawnBlock(BlockView block, BlockPos pos, BlockState blockState, FluidState fluidState)
    {
        ServerPlayerEntity player = torchmaster$currentPhantomPlayer.get();
        if(player == null)
            return SpawnHelper.isClearForSpawn(block, pos, blockState, fluidState);

        return MinecraftEventResultDecisions.resolve(
                FabricSpawnEventHooks.checkPhantomSpawn(player),
                () -> SpawnHelper.isClearForSpawn(block, pos, blockState, fluidState));
    }

    @Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I", at = @At("RETURN"))
    private void torchmaster_tick_clearCurrentPlayer(ServerWorld level, boolean spawnEnemies, boolean spawnFriendlies, CallbackInfoReturnable<Integer> cir)
    {
        torchmaster$currentPhantomPlayer.remove();
    }
}
*///?}
