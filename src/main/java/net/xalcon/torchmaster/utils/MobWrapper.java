package net.xalcon.torchmaster.utils;

//? if fabric && >=1.16.5 {
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.SpawnEventBridge;

public abstract class MobWrapper
{
    public static EventResult checkSpawnRules(MobEntity mob, SpawnReason spawnReason)
    {
        EventResultContainer container = new EventResultContainer(EventResult.DEFAULT);
        SpawnEventBridge.onCheckSpawn(spawnReason, mob,
                //? if >=1.21.11
                //mob.getEntityPos()
                //? if <1.21.11
                mob.getPos()
                , container);
        return container.getResult();
    }
}
//?} else if fabric {
/*import net.minecraft.entity.SpawnType;
import net.minecraft.entity.mob.MobEntity;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.SpawnEventBridge;

public abstract class MobWrapper
{
    public static EventResult checkSpawnRules(MobEntity mob, SpawnType spawnReason)
    {
        EventResultContainer container = new EventResultContainer(EventResult.DEFAULT);
        SpawnEventBridge.onCheckSpawn(spawnReason, mob, mob.getPos(), container);
        return container.getResult();
    }
}
*///?}
