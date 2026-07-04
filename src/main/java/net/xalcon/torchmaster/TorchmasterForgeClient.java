package net.xalcon.torchmaster;

//? if forge && >=1.19 {
/*import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.client.TorchmasterLightRangeDisplay;
import net.xalcon.torchmaster.client.TorchmasterLightRangeRenderer;
import net.xalcon.torchmaster.client.TorchmasterLightScreen;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class TorchmasterForgeClient
{
    static
    {
        TorchmasterClientBridge.setLightScreenOpener(TorchmasterLightScreen::open);
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
*///?} elif forge && >=1.18 {
/*import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.client.TorchmasterLightRangeDisplay;
import net.xalcon.torchmaster.client.TorchmasterLightRangeRenderer;
import net.xalcon.torchmaster.client.TorchmasterLightScreen;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class TorchmasterForgeClient
{
    static
    {
        TorchmasterClientBridge.setLightScreenOpener(TorchmasterLightScreen::open);
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
        TorchmasterLightRangeDisplay.tick();
    }

    @SubscribeEvent
    public static void onRenderLevelLast(RenderLevelLastEvent event)
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
*///?} elif forge {
/*import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.client.TorchmasterLightRangeDisplay;
import net.xalcon.torchmaster.client.TorchmasterLightRangeRenderer;
import net.xalcon.torchmaster.client.TorchmasterLightScreen;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class TorchmasterForgeClient
{
    static
    {
        TorchmasterClientBridge.setLightScreenOpener(TorchmasterLightScreen::open);
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
        TorchmasterLightRangeDisplay.tick();
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event)
    {
        renderRange(event.getMatrixStack());
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
