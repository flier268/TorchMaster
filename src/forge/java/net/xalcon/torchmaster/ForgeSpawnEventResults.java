package net.xalcon.torchmaster;

import net.minecraftforge.eventbus.api.Event;
//? if >=1.19.4
/*import net.minecraftforge.event.entity.living.MobSpawnEvent;*/
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.minecraft.spawn.MinecraftSpawnEventContainers;

final class ForgeSpawnEventResults
{
    private ForgeSpawnEventResults()
    {
    }

    static EventResultContainer container(Event.Result result)
    {
        return MinecraftSpawnEventContainers.container(fromForgeResult(result));
    }

    static Event.Result toForgeResult(EventResultContainer container)
    {
        switch (MinecraftSpawnEventContainers.result(container)) {
            case ALLOW:
                return Event.Result.ALLOW;
            case DENY:
                return Event.Result.DENY;
            case DEFAULT:
            default:
                return Event.Result.DEFAULT;
        }
    }

    //? if >=1.19.4 {
    /*static void applyFinalizeSpawnResult(MobSpawnEvent.FinalizeSpawn event, EventResultContainer container)
    {
        switch (MinecraftSpawnEventContainers.result(container)) {
            case DENY:
                event.setSpawnCancelled(true);
                break;
            case ALLOW:
                event.setSpawnCancelled(false);
                break;
            case DEFAULT:
            default:
                break;
        }
    }
    *///?}

    private static EventResult fromForgeResult(Event.Result result)
    {
        switch (result) {
            case ALLOW:
                return EventResult.ALLOW;
            case DENY:
                return EventResult.DENY;
            case DEFAULT:
            default:
                return EventResult.DEFAULT;
        }
    }
}
