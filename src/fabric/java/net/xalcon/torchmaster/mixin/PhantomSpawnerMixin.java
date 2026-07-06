package net.xalcon.torchmaster.mixin;

import net.minecraft.block.BlockState;
//? if >=1.16.5 {
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
//?} else {
/*import net.minecraft.entity.Entity;
*///?}
import net.minecraft.fluid.FluidState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.SpawnHelper;
//? if >=1.18.2
import net.minecraft.world.spawner.PhantomSpawner;
//? if <1.18.2
//import net.minecraft.world.gen.PhantomSpawner;
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
                    //? if <1.16.5
                    //value = "NEW",
                    //? if <1.16.5
                    //target = "net/minecraft/util/math/BlockPos"
                    //? if >=1.16.5 <1.20.6
                    //value = "INVOKE",
                    //? if >=1.16.5 <1.20.6
                    //target = "Lnet/minecraft/entity/player/PlayerEntity;getBlockPos()Lnet/minecraft/util/math/BlockPos;"
                    //? if >=1.20.6
                    value = "INVOKE",
                    //? if >=1.20.6
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;getBlockPos()Lnet/minecraft/util/math/BlockPos;"
            )
    )
    //? if >=1.20.6
    private BlockPos torchmaster_tick_playerBlockPosition(ServerPlayerEntity player)
    //? if >=1.16.5 <1.20.6
    //private BlockPos torchmaster_tick_playerBlockPosition(PlayerEntity player)
    //? if <1.16.5
    //private BlockPos torchmaster_tick_playerBlockPosition(Entity player)
    {
        torchmaster$currentPhantomPlayer.set((ServerPlayerEntity) player);
        //? if >=1.16.5
        return player.getBlockPos();
        //? if <1.16.5
        //return new BlockPos(player);
    }

    @Redirect(
            //? if >=1.21.11
            //method = "spawn(Lnet/minecraft/server/world/ServerWorld;Z)V",
            //? if <1.21.11
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            at = @At(
                    value = "INVOKE",
                    //? if >=1.16.5
                    target = "Lnet/minecraft/world/SpawnHelper;isClearForSpawn(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;Lnet/minecraft/entity/EntityType;)Z"
                    //? if <1.16.5
                    //target = "Lnet/minecraft/world/SpawnHelper;isClearForSpawn(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)Z"
            )
    )
    //? if >=1.16.5
    private boolean torchmaster_tick_NaturalSpawner__isValidEmptySpawnBlock(BlockView block, BlockPos pos, BlockState blockState, FluidState fluidState, EntityType<?> entityType)
    //? if <1.16.5
    //private boolean torchmaster_tick_NaturalSpawner__isValidEmptySpawnBlock(BlockView block, BlockPos pos, BlockState blockState, FluidState fluidState)
    {
        ServerPlayerEntity player = torchmaster$currentPhantomPlayer.get();
        if(player == null) {
            //? if >=1.16.5
            return SpawnHelper.isClearForSpawn(block, pos, blockState, fluidState, entityType);
            //? if <1.16.5
            //return SpawnHelper.isClearForSpawn(block, pos, blockState, fluidState);
        }

        return MinecraftEventResultDecisions.resolve(
                FabricSpawnEventHooks.checkPhantomSpawn(player),
                //? if >=1.16.5
                () -> SpawnHelper.isClearForSpawn(block, pos, blockState, fluidState, entityType)
                //? if <1.16.5
                //() -> SpawnHelper.isClearForSpawn(block, pos, blockState, fluidState)
        );
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
