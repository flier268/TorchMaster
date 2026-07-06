package net.xalcon.torchmaster.mixin;

//? if fabric && >=1.16.5 {
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.WorldAccess;
import net.xalcon.torchmaster.utils.MobWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpawnHelper.class)
public abstract class NaturalSpawnerMixin
{
    @Redirect(
            method = "isValidSpawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;D)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;canSpawn(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;)Z"
            )
    )
    private static boolean torchmaster_isValidPositionForMob_checkSpawnRules(MobEntity mob, WorldAccess level, SpawnReason spawnReason)
    {
        switch(MobWrapper.checkSpawnRules(mob, spawnReason)) {
            case ALLOW:
                return true;
            case DENY:
                return false;
            case DEFAULT:
            default:
                return mob.canSpawn(level, spawnReason);
        }
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
            //? if <1.17
            //method = "populateEntities(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/biome/Biome;IILjava/util/Random;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;canSpawn(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;)Z"
            )
    )
    private static boolean torchmaster_spawnMobsForChunkGeneration_checkSpawnRules(MobEntity mob, WorldAccess level, SpawnReason spawnReason)
    {
        switch(MobWrapper.checkSpawnRules(mob, spawnReason)) {
            case ALLOW:
                return true;
            case DENY:
                return false;
            case DEFAULT:
            default:
                return mob.canSpawn(level, spawnReason);
        }
    }
}
//?} else if fabric {
/*import net.minecraft.entity.SpawnType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.IWorld;
import net.minecraft.world.SpawnHelper;
import net.xalcon.torchmaster.utils.MobWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpawnHelper.class)
public abstract class NaturalSpawnerMixin
{
    @Redirect(
            method = "spawnEntitiesInChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;canSpawn(Lnet/minecraft/world/IWorld;Lnet/minecraft/entity/SpawnType;)Z"
            )
    )
    private static boolean torchmaster_isValidPositionForMob_checkSpawnRules(MobEntity mob, IWorld level, SpawnType spawnReason)
    {
        switch(MobWrapper.checkSpawnRules(mob, spawnReason)) {
            case ALLOW:
                return true;
            case DENY:
                return false;
            case DEFAULT:
            default:
                return mob.canSpawn(level, spawnReason);
        }
    }

    @Redirect(
            method = "populateEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;canSpawn(Lnet/minecraft/world/IWorld;Lnet/minecraft/entity/SpawnType;)Z"
            )
    )
    private static boolean torchmaster_spawnMobsForChunkGeneration_checkSpawnRules(MobEntity mob, IWorld level, SpawnType spawnReason)
    {
        switch(MobWrapper.checkSpawnRules(mob, spawnReason)) {
            case ALLOW:
                return true;
            case DENY:
                return false;
            case DEFAULT:
            default:
                return mob.canSpawn(level, spawnReason);
        }
    }
}
*///?}
