package net.xalcon.torchmaster;

//? if forge && >=1.19 {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.client.TorchmasterClientEventAdapter;
import net.xalcon.torchmaster.client.TorchmasterClientLifecycle;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class TorchmasterForgeClient
{
    static
    {
        TorchmasterClientLifecycle.installLightScreenOpener();
    }

    private TorchmasterForgeClient()
    {
    }

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
	    TorchmasterClientEventAdapter.onForgeClientTick(event);
	}

	@SubscribeEvent
	public static void onRenderLevelStage(RenderLevelStageEvent event)
	{
	    TorchmasterClientEventAdapter.onForgeRenderLevelStage(event);
	}
}
*///?} else if forge && >=1.15 {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
	import net.minecraftforge.event.TickEvent;
	import net.minecraftforge.eventbus.api.SubscribeEvent;
	import net.minecraftforge.fml.common.Mod;
	import net.xalcon.torchmaster.client.TorchmasterClientEventAdapter;
	import net.xalcon.torchmaster.client.TorchmasterClientLifecycle;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class TorchmasterForgeClient
{
    static
    {
        TorchmasterClientLifecycle.installLightScreenOpener();
    }

    private TorchmasterForgeClient()
    {
    }

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
	    TorchmasterClientEventAdapter.onForgeClientTick(event);
	}

	@SubscribeEvent
	public static void onRenderWorldLast(RenderWorldLastEvent event)
	{
	    TorchmasterClientEventAdapter.onForgeRenderWorldLast(event);
	}
}
*///?} else if forge {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
	import net.minecraftforge.event.TickEvent;
	import net.minecraftforge.eventbus.api.SubscribeEvent;
	import net.minecraftforge.fml.common.Mod;
	import net.xalcon.torchmaster.client.TorchmasterClientEventAdapter;
	import net.xalcon.torchmaster.client.TorchmasterClientLifecycle;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class TorchmasterForgeClient
{
    static
    {
        TorchmasterClientLifecycle.installLightScreenOpener();
    }

    private TorchmasterForgeClient()
    {
    }

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
	    TorchmasterClientEventAdapter.onForgeClientTick(event);
	}

	@SubscribeEvent
	public static void onRenderWorldLast(RenderWorldLastEvent event)
	{
	    TorchmasterClientEventAdapter.onForgeRenderWorldLast(event);
	}
}
*///?}
