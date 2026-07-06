package net.xalcon.torchmaster.client;

import net.minecraft.client.MinecraftClient;
//? if >=1.15
import net.minecraft.client.render.VertexConsumerProvider;
//? if >=1.15
import net.minecraft.client.util.math.MatrixStack;
import net.xalcon.torchmaster.TorchmasterClientBridge;

public final class TorchmasterClientLifecycle
{
    private TorchmasterClientLifecycle()
    {
    }

    public static void installLightScreenOpener()
    {
        TorchmasterClientBridge.setLightScreenOpener(TorchmasterLightScreen::open);
    }

    public static void onEndClientTick()
    {
        TorchmasterLightRangeDisplay.tick();
    }

    //? if >=1.15 {
    public static void renderCurrentRange(MatrixStack poseStack)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.world == null) {
            return;
        }
        TorchmasterLightRangeRenderer.render(minecraft.world, minecraft.gameRenderer.getCamera(), poseStack);
    }

    public static void renderCurrentRange(MatrixStack poseStack, VertexConsumerProvider bufferSource)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.world == null) {
            return;
        }
        TorchmasterLightRangeRenderer.render(minecraft.world, minecraft.gameRenderer.getCamera(), poseStack, bufferSource);
    }
    //?} else {
    /*public static void renderCurrentRange()
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.world == null) {
            return;
        }
        TorchmasterLightRangeRenderer.render(minecraft.world, minecraft.gameRenderer.getCamera());
    }
    *///?}
}
