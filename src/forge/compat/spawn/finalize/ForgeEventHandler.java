package net.xalcon.torchmaster;

import net.minecraft.world.phys.Vec3;
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
