package net.xalcon.torchmaster;

//? if forge && >=1.19.4 {
/*import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.TorchmasterEventHandler;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ForgeEventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event)
    {
        EventResultContainer container = new EventResultContainer(EventResult.DEFAULT);

        TorchmasterEventHandler.onCheckSpawn(
                event.getSpawnType(),
                event.getEntity(),
                new Vec3(event.getX(), event.getY(), event.getZ()),
                container);

        if(container.getResult() == EventResult.DENY)
            event.setSpawnCancelled(true);
        else if(container.getResult() == EventResult.ALLOW)
            event.setSpawnCancelled(false);
    }
}
*///?} else if forge {
/*import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.TorchmasterEventHandler;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ForgeEventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        EventResultContainer container = new EventResultContainer(fromForgeResult(event.getResult()));

        TorchmasterEventHandler.onCheckSpawn(
                event.getSpawnReason(),
                event.getEntity(),
                new Vec3(event.getX(), event.getY(), event.getZ()),
                container);

        event.setResult(toForgeResult(container.getResult()));
    }

    private static EventResult fromForgeResult(Event.Result result)
    {
        switch(result)
        {
            case ALLOW:
                return EventResult.ALLOW;
            case DENY:
                return EventResult.DENY;
            case DEFAULT:
            default:
                return EventResult.DEFAULT;
        }
    }

    private static Event.Result toForgeResult(EventResult result)
    {
        switch(result)
        {
            case ALLOW:
                return Event.Result.ALLOW;
            case DENY:
                return Event.Result.DENY;
            case DEFAULT:
            default:
                return Event.Result.DEFAULT;
        }
    }
}
*///?}
