package net.xalcon.torchmaster;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.xalcon.torchmaster.client.TorchmasterClientEventAdapter;
import net.xalcon.torchmaster.client.TorchmasterClientLifecycle;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class TorchmasterNeoforgeClient
{
    static
    {
        TorchmasterClientLifecycle.installLightScreenOpener();
    }

    private TorchmasterNeoforgeClient()
    {
    }

    @SubscribeEvent
    public static void onClientEvent(Event event)
    {
        TorchmasterClientEventAdapter.onNeoForgeEvent(event);
    }
}
