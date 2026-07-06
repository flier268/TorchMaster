package net.xalcon.torchmaster.utils;

//? if fabric && >=1.16.5 {
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.SpawnEventBridge;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnEventContainers;

public abstract class MobWrapper
{
    public static EventResult checkSpawnRules(MobEntity mob, SpawnReason spawnReason)
    {
        return MinecraftSpawnEventContainers.invokeDefault(container -> SpawnEventBridge.onCheckSpawn(spawnReason, mob,
                //? if >=1.21.11
                //mob.getEntityPos()
                //? if <1.21.11
                mob.getPos()
                , container));
    }
}
//?} else if fabric {
/*import net.minecraft.entity.SpawnType;
import net.minecraft.entity.mob.MobEntity;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.SpawnEventBridge;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnEventContainers;

public abstract class MobWrapper
{
    public static EventResult checkSpawnRules(MobEntity mob, SpawnType spawnReason)
    {
        return MinecraftSpawnEventContainers.invokeDefault(container ->
                SpawnEventBridge.onCheckSpawn(spawnReason, mob, mob.getPos(), container));
    }
}
*///?}
