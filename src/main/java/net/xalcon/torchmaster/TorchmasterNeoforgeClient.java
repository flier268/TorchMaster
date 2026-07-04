package net.xalcon.torchmaster;

//? if neoforge && >=1.21.8 {
/*import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.xalcon.torchmaster.client.TorchmasterLightRangeDisplay;
import net.xalcon.torchmaster.client.TorchmasterLightRangeRenderer;
import net.xalcon.torchmaster.client.TorchmasterLightScreen;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class TorchmasterNeoforgeClient
{
    static
    {
        TorchmasterClientBridge.setLightScreenOpener(TorchmasterLightScreen::open);
    }

    private TorchmasterNeoforgeClient()
    {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        TorchmasterLightRangeDisplay.tick();
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent.AfterTranslucentBlocks event)
    {
        renderRange(event.getPoseStack());
    }

    private static void renderRange(PoseStack poseStack)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        TorchmasterLightRangeRenderer.render(minecraft.level, minecraft.gameRenderer.getMainCamera(), poseStack);
    }
}
*///?} elif neoforge {
/*import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.xalcon.torchmaster.client.TorchmasterLightRangeDisplay;
import net.xalcon.torchmaster.client.TorchmasterLightRangeRenderer;
import net.xalcon.torchmaster.client.TorchmasterLightScreen;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class TorchmasterNeoforgeClient
{
    static
    {
        TorchmasterClientBridge.setLightScreenOpener(TorchmasterLightScreen::open);
    }

    private TorchmasterNeoforgeClient()
    {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        TorchmasterLightRangeDisplay.tick();
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event)
    {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        renderRange(event.getPoseStack());
    }

    private static void renderRange(PoseStack poseStack)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        TorchmasterLightRangeRenderer.render(minecraft.level, minecraft.gameRenderer.getMainCamera(), poseStack);
    }
}
*///?}
