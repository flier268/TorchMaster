package net.xalcon.torchmaster;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.client.TorchmasterClientEventAdapter;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class TorchmasterForgeClient
{
    static
    {
        TorchmasterClientEventAdapter.initializeForgeClient();
    }

    private TorchmasterForgeClient()
    {
    }

    @SubscribeEvent
    public static void onClientEvent(Event event)
    {
        TorchmasterClientEventAdapter.onForgeEvent(event);
    }
}
