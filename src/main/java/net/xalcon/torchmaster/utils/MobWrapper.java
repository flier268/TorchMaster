package net.xalcon.torchmaster.utils;

//? if fabric && >=1.21.2 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelAccessor;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.SpawnEventBridge;

public abstract class MobWrapper
{
    public static boolean checkSpawnRules(Mob mob, LevelAccessor level, EntitySpawnReason spawnReason, Operation<Boolean> original)
    {
        var container = new EventResultContainer(EventResult.DEFAULT);
        SpawnEventBridge.onCheckSpawn(spawnReason, mob, mob.position(), container);
        return switch(container.getResult())
        {
            case DEFAULT -> original.call(mob, level, spawnReason);
            case ALLOW -> true;
            case DENY -> false;
        };
    }
}
*///?} else if fabric {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.SpawnEventBridge;

public abstract class MobWrapper
{
    public static boolean checkSpawnRules(Mob mob, LevelAccessor level, MobSpawnType spawnReason, Operation<Boolean> original)
    {
        var container = new EventResultContainer(EventResult.DEFAULT);
        SpawnEventBridge.onCheckSpawn(spawnReason, mob, mob.position(), container);
        return switch(container.getResult())
        {
            case DEFAULT -> original.call(mob, level, spawnReason);
            case ALLOW -> true;
            case DENY -> false;
        };
    }
}
//?}
