package net.xalcon.torchmaster;

import net.minecraftforge.event.village.VillageSiegeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.TorchmasterEventHandler;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ForgeVillageEventHandler
{
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onVillageSiegeEvent(VillageSiegeEvent event)
    {
        EventResultContainer container = new EventResultContainer(EventResult.DEFAULT);

        TorchmasterEventHandler.onVillageSiege(event.getWorld(), event.getAttemptedSpawnPos(), container);

        if(container.getResult() == EventResult.DENY)
            event.setCanceled(true);
    }
}
