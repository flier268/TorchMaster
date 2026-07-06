package net.xalcon.torchmaster;

//? if >=1.19 {
/*import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.Event;
*///?} else {
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
//?}

public final class TorchmasterForgeLifecycleAdapter
{
    private TorchmasterForgeLifecycleAdapter()
    {
    }

    public static void onForgeEvent(Event event)
    {
        //? if >=1.19 {
        /*if (event instanceof LevelEvent.Load) {
            TorchmasterRuntime.onWorldLoaded();
        }
        *///?} else {
        if (event instanceof WorldEvent.Load) {
            TorchmasterRuntime.onWorldLoaded();
        }
        //?}
    }
}
