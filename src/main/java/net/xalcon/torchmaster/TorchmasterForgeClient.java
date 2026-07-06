package net.xalcon.torchmaster;

//? if forge && >=1.19 {
/*import net.minecraft.client.util.math.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        TorchmasterClientLifecycle.onEndClientTick();
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event)
    {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        //? if >=1.20.6 {
        MatrixStack poseStack = new MatrixStack();
        poseStack.multiplyPositionMatrix(event.getPoseStack());
        TorchmasterClientLifecycle.renderCurrentRange(poseStack);
        //?} else {
        /^TorchmasterClientLifecycle.renderCurrentRange(event.getPoseStack());^/
        //?}
    }
}
*///?} else if forge && >=1.15 {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        TorchmasterClientLifecycle.onEndClientTick();
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event)
    {
        TorchmasterClientLifecycle.renderCurrentRange(event.getMatrixStack());
    }
}
*///?} else if forge {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        TorchmasterClientLifecycle.onEndClientTick();
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event)
    {
        TorchmasterClientLifecycle.renderCurrentRange();
    }
}
*///?}
