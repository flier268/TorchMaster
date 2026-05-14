package net.xalcon.torchmaster;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.xalcon.torchmaster.client.VolumeRendererOverlay;

@EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class TorchmasterClientEventHandler {
    @SubscribeEvent
    public static void onRenderLevelStageEvent(RenderLevelStageEvent event)
    {
        if(event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER) return;

        VolumeRendererOverlay.onRenderLevel(event.getCamera());
    }
}
