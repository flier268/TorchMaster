package net.xalcon.torchmaster.mixin;

//? if fabric && >=1.20.6 {
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.WorldAccess;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftEventResultDecisions;
import net.xalcon.torchmaster.utils.MobWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MobSpawnerLogic.class, priority = 100)
public abstract class BaseSpawnerMixin
{
    @Redirect(
            method = "serverTick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;canSpawn(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;)Z"
            )
    )
    private static boolean torchmaster_serverTick_checkSpawnRules(MobEntity mob, WorldAccess level, SpawnReason spawnReason)
    {
        return MinecraftEventResultDecisions.resolve(
                MobWrapper.checkSpawnRules(mob, spawnReason),
                () -> mob.canSpawn(level, spawnReason));
    }
}
//?} else if fabric && >=1.17 {
/*import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.MobSpawnerLogic;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftEventResultDecisions;
import net.xalcon.torchmaster.utils.MobWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MobSpawnerLogic.class, priority = 100)
public abstract class BaseSpawnerMixin
{
    @Redirect(
            method = "serverTick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;canSpawn(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;)Z"
            )
    )
    private static boolean torchmaster_serverTick_checkSpawnRules(MobEntity mob, WorldAccess level, SpawnReason spawnReason)
    {
        return MinecraftEventResultDecisions.resolve(
                MobWrapper.checkSpawnRules(mob, spawnReason),
                () -> mob.canSpawn(level, spawnReason));
    }
}
*///?} else if fabric {
/*import net.minecraft.world.MobSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MobSpawnerLogic.class, priority = 100)
public abstract class BaseSpawnerMixin
{
}
*///?}
