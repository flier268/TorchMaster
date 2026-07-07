package net.xalcon.torchmaster;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.xalcon.torchmaster.client.TorchmasterClientEventAdapter;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class TorchmasterNeoforgeClient
{
    static
    {
        TorchmasterClientEventAdapter.initializeNeoForgeClient();
    }

    private TorchmasterNeoforgeClient()
    {
    }

    public static void onClientEvent(Event event)
    {
        TorchmasterClientEventAdapter.onNeoForgeEvent(event);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        TorchmasterClientEventAdapter.onNeoForgeClientTick(event);
    }

    @SubscribeEvent
    //? if >=1.21.8
    //public static void onRenderLevelStage(RenderLevelStageEvent.AfterTranslucentBlocks event)
    //? if <1.21.8
    public static void onRenderLevelStage(RenderLevelStageEvent event)
    {
        TorchmasterClientEventAdapter.onNeoForgeRenderLevelStage(event);
    }
}
