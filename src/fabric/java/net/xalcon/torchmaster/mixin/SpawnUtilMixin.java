package net.xalcon.torchmaster.mixin;

//? if fabric {
//? if >=1.19.2 {
import net.minecraft.entity.LargeEntitySpawnHelper;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.WorldAccess;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftEventResultDecisions;
import net.xalcon.torchmaster.minecraft.spawn.FabricSpawnEventHooks;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
//?} else {
/*import net.minecraft.world.MobSpawnerLogic;
*///?}
import org.spongepowered.asm.mixin.Mixin;
//?}

//? if fabric && >=1.19.2
@Mixin(LargeEntitySpawnHelper.class)
//? if fabric && <1.19.2
//@Mixin(value = MobSpawnerLogic.class, priority = 100)
public abstract class SpawnUtilMixin
{
    //? if fabric && >=1.19.2 {
    @Redirect(
            method = "trySpawnAt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;canSpawn(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;)Z"
            )
    )
    private static boolean torchmaster_trySpawnMob_checkSpawnRules(MobEntity mob, WorldAccess level, SpawnReason spawnReason)
    {
        return MinecraftEventResultDecisions.resolve(
                FabricSpawnEventHooks.checkSpawnRules(mob, spawnReason),
                () -> mob.canSpawn(level, spawnReason));
    }
    //?}
}
