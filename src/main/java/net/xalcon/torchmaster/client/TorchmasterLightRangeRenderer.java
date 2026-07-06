package net.xalcon.torchmaster.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
//? if >=1.15
import net.minecraft.client.render.VertexConsumerProvider;
//? if >=1.15
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.World;

public final class TorchmasterLightRangeRenderer
{
    private TorchmasterLightRangeRenderer()
    {
    }

    //? if >=1.15 {
    public static void render(World level, Camera camera, MatrixStack poseStack)
    {
        VertexConsumerProvider.Immediate bufferSource = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        renderAndFlush(level, camera, poseStack, bufferSource);
    }
    //?} else {
    /*public static void render(World level, Camera camera)
    {
        TorchmasterRangeRenderSession.render(level, camera);
    }
    *///?}

    //? if >=1.15 {
    public static void renderAndFlush(World level, Camera camera, MatrixStack poseStack, VertexConsumerProvider.Immediate bufferSource)
    {
        render(level, camera, poseStack, bufferSource);
        TorchmasterRangeRenderSession.flushLines(bufferSource);
    }

    public static void render(World level, Camera camera, MatrixStack poseStack, VertexConsumerProvider bufferSource)
    {
        TorchmasterRangeRenderSession.render(level, camera, poseStack, bufferSource);
    }

    //?}
}
