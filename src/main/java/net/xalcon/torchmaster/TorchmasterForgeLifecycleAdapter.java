package net.xalcon.torchmaster;

//? if forge && >=1.19 {
/*import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.Event;
*///?} else if forge {
/*import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
*///?}

public final class TorchmasterForgeLifecycleAdapter
{
    private TorchmasterForgeLifecycleAdapter()
    {
    }

    //? if forge && >=1.19 {
    /*public static void onForgeEvent(Event event)
    {
        if (event instanceof LevelEvent.Load) {
            TorchmasterRuntime.onWorldLoaded();
        }
    }
    *///?} else if forge {
    /*public static void onForgeEvent(Event event)
    {
        if (event instanceof WorldEvent.Load) {
            TorchmasterRuntime.onWorldLoaded();
        }
    }
    *///?}
}
