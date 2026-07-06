package net.xalcon.torchmaster;

//? if neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
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
	public static void onClientTick(ClientTickEvent.Post event)
	{
	    TorchmasterClientEventAdapter.onNeoForgeClientTick(event);
	}

    //? if >=1.21.8 {
    @SubscribeEvent
	public static void onRenderLevelStage(RenderLevelStageEvent.AfterTranslucentBlocks event)
	{
	    TorchmasterClientEventAdapter.onNeoForgeRenderLevelStage(event);
	}
    //?} else {
    /^@SubscribeEvent^/
    /^public static void onRenderLevelStage(RenderLevelStageEvent event)^/
    /^{^/
	/^    TorchmasterClientEventAdapter.onNeoForgeRenderLevelStage(event);^/
	/^}^/
    //?}
}
*///?}
