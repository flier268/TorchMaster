package net.xalcon.torchmaster.mixin;

//? if forge && <1.16.5 {
/*import net.minecraft.entity.EntityCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.WorldChunk;
*///?}
//? if forge && >=1.16.5 {
import net.minecraft.server.world.ServerWorld;
//? if >=1.21.11
//import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.WorldChunk;
//?}
import net.xalcon.torchmaster.minecraft.spawn.MinecraftNaturalSpawnHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >=1.21.11
//import java.util.List;

//? if forge
@Mixin(SpawnHelper.class)
public abstract class ForgeNaturalSpawnerMixin
{
    //? if forge && <1.16.5 {
    /*@Inject(
            method = "spawnEntitiesInChunk(Lnet/minecraft/entity/EntityCategory;Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void torchmaster_spawnEntitiesInChunk_skipDiamondBaseBlockedNaturalSpawnChunk(
            EntityCategory category,
            World level,
            WorldChunk chunk,
            BlockPos pos,
            CallbackInfo ci)
    {
        if (MinecraftNaturalSpawnHooks.shouldSkipNaturalSpawnChunk(level, chunk.getPos())) {
            ci.cancel();
        }
    }
    *///?} elif forge && <1.21.11 {
    @Inject(
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/SpawnHelper$Info;ZZZ)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void torchmaster_spawn_skipDiamondBaseBlockedNaturalSpawnChunk(
            ServerWorld level,
            WorldChunk chunk,
            SpawnHelper.Info info,
            boolean spawnAnimals,
            boolean spawnMonsters,
            boolean rare,
            CallbackInfo ci)
    {
        torchmaster$cancelDiamondBaseBlockedNaturalSpawnChunk(level, chunk, ci);
    }
    //?} else {
    /*@Inject(
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/SpawnHelper$Info;Ljava/util/List;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void torchmaster_spawn_skipDiamondBaseBlockedNaturalSpawnChunk(
            ServerWorld level,
            WorldChunk chunk,
            SpawnHelper.Info info,
            List<SpawnGroup> spawnGroups,
            CallbackInfo ci)
    {
        torchmaster$cancelDiamondBaseBlockedNaturalSpawnChunk(level, chunk, ci);
    }
    *///?}

    //? if forge && >=1.16.5 {
    private static void torchmaster$cancelDiamondBaseBlockedNaturalSpawnChunk(ServerWorld level, WorldChunk chunk, CallbackInfo ci)
    {
        if (MinecraftNaturalSpawnHooks.shouldSkipNaturalSpawnChunk(level, chunk.getPos())) {
            ci.cancel();
        }
    }
    //?}
}
