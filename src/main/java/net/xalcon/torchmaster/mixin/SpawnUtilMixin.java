package net.xalcon.torchmaster.mixin;

//? if fabric && >=1.21.2 {
/*import net.minecraft.util.SpawnUtil;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelAccessor;
import net.xalcon.torchmaster.utils.MobWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpawnUtil.class)
public abstract class SpawnUtilMixin
{
    @Redirect(
            method = "trySpawnMob",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;checkSpawnRules(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;)Z"
            )
    )
    private static boolean torchmaster_trySpawnMob_checkSpawnRules(Mob mob, LevelAccessor level, EntitySpawnReason spawnReason)
    {
        return switch(MobWrapper.checkSpawnRules(mob, spawnReason))
        {
            case DEFAULT -> mob.checkSpawnRules(level, spawnReason);
            case ALLOW -> true;
            case DENY -> false;
        };
    }
}
*///?} else if fabric {
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.xalcon.torchmaster.utils.MobWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpawnUtil.class)
public abstract class SpawnUtilMixin
{
    @Redirect(
            method = "trySpawnMob",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;checkSpawnRules(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;)Z"
            )
    )
    private static boolean torchmaster_trySpawnMob_checkSpawnRules(Mob mob, LevelAccessor level, MobSpawnType spawnReason)
    {
        return switch(MobWrapper.checkSpawnRules(mob, spawnReason))
        {
            case DEFAULT -> mob.checkSpawnRules(level, spawnReason);
            case ALLOW -> true;
            case DENY -> false;
        };
    }
}
//?}
