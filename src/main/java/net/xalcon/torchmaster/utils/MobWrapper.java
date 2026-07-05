package net.xalcon.torchmaster.utils;

//? if fabric && >=1.21.2 {
/*import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.SpawnEventBridge;

public abstract class MobWrapper
{
    public static EventResult checkSpawnRules(Mob mob, EntitySpawnReason spawnReason)
    {
        var container = new EventResultContainer(EventResult.DEFAULT);
        SpawnEventBridge.onCheckSpawn(spawnReason, mob, mob.position(), container);
        return container.getResult();
    }
}
*///?} else if fabric {
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.SpawnEventBridge;

public abstract class MobWrapper
{
    public static EventResult checkSpawnRules(Mob mob, MobSpawnType spawnReason)
    {
        var container = new EventResultContainer(EventResult.DEFAULT);
        SpawnEventBridge.onCheckSpawn(spawnReason, mob, mob.position(), container);
        return container.getResult();
    }
}
//?}
