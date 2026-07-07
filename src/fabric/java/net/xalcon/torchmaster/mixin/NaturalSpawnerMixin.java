package net.xalcon.torchmaster.mixin;

//? if fabric && <1.16.5 {
/*import net.minecraft.entity.SpawnType;
import net.minecraft.entity.EntityCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
*///?}
//? if fabric && >=1.16.5 {
import net.minecraft.entity.SpawnReason;
//? if >=1.21.11
//import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.WorldChunk;
//?}
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.SpawnHelper;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftEventResultDecisions;
import net.xalcon.torchmaster.minecraft.spawn.FabricSpawnEventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >=1.21.11
//import java.util.List;

//? if fabric
@Mixin(SpawnHelper.class)
public abstract class NaturalSpawnerMixin
{
    //? if fabric && <1.16.5 {
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
        if (FabricSpawnEventHooks.shouldSkipNaturalSpawnChunk(level, chunk.getPos())) {
            ci.cancel();
        }
    }
    *///?} elif fabric && <1.21.11 {
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

    //? if fabric && >=1.16.5 {
    private static void torchmaster$cancelDiamondBaseBlockedNaturalSpawnChunk(ServerWorld level, WorldChunk chunk, CallbackInfo ci)
    {
        if (FabricSpawnEventHooks.shouldSkipNaturalSpawnChunk(level, chunk.getPos())) {
            ci.cancel();
        }
    }
    //?}

    //? if fabric {
    @Redirect(
            //? if >=1.16.5
            method = "isValidSpawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;D)Z",
            //? if <1.16.5
            //method = "spawnEntitiesInChunk",
            at = @At(
                    value = "INVOKE",
                    //? if >=1.16.5
                    target = "Lnet/minecraft/entity/mob/MobEntity;canSpawn(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;)Z"
                    //? if <1.16.5
                    //target = "Lnet/minecraft/entity/mob/MobEntity;canSpawn(Lnet/minecraft/world/IWorld;Lnet/minecraft/entity/SpawnType;)Z"
            )
    )
    //? if >=1.16.5
    private static boolean torchmaster_isValidPositionForMob_checkSpawnRules(MobEntity mob, WorldAccess level, SpawnReason spawnReason)
    //? if <1.16.5
    //private static boolean torchmaster_isValidPositionForMob_checkSpawnRules(MobEntity mob, IWorld level, SpawnType spawnReason)
    {
        return MinecraftEventResultDecisions.resolve(
                FabricSpawnEventHooks.checkSpawnRules(mob, spawnReason),
                () -> mob.canSpawn(level, spawnReason));
    }

    @Redirect(
            //? if >=1.19.4
            method = "populateEntities(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/random/Random;)V",
            //? if >=1.19 <1.19.4
            //method = "populateEntities(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/registry/RegistryEntry;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/random/Random;)V",
            //? if >=1.18 <1.19
            //method = "populateEntities(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/registry/RegistryEntry;Lnet/minecraft/util/math/ChunkPos;Ljava/util/Random;)V",
            //? if >=1.17 <1.18
            //method = "populateEntities(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/biome/Biome;Lnet/minecraft/util/math/ChunkPos;Ljava/util/Random;)V",
            //? if >=1.16.5 <1.17
            //method = "populateEntities(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/biome/Biome;IILjava/util/Random;)V",
            //? if <1.16.5
            //method = "populateEntities",
            at = @At(
                    value = "INVOKE",
                    //? if >=1.16.5
                    target = "Lnet/minecraft/entity/mob/MobEntity;canSpawn(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;)Z"
                    //? if <1.16.5
                    //target = "Lnet/minecraft/entity/mob/MobEntity;canSpawn(Lnet/minecraft/world/IWorld;Lnet/minecraft/entity/SpawnType;)Z"
            )
    )
    //? if >=1.16.5
    private static boolean torchmaster_spawnMobsForChunkGeneration_checkSpawnRules(MobEntity mob, WorldAccess level, SpawnReason spawnReason)
    //? if <1.16.5
    //private static boolean torchmaster_spawnMobsForChunkGeneration_checkSpawnRules(MobEntity mob, IWorld level, SpawnType spawnReason)
    {
        return MinecraftEventResultDecisions.resolve(
                FabricSpawnEventHooks.checkSpawnRules(mob, spawnReason),
                () -> mob.canSpawn(level, spawnReason));
    }
    //?}
}
