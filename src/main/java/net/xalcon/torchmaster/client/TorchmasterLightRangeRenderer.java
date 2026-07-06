package net.xalcon.torchmaster.client;

//? if <1.15 {
/*import com.mojang.blaze3d.platform.GlStateManager;
*///?}
import net.minecraft.client.MinecraftClient;
//? if >=1.21.11
//import net.minecraft.client.gl.RenderPipelines;
//? if <1.15
/*import net.minecraft.client.render.BufferBuilder;*/
import net.minecraft.client.render.Camera;
//? if >=1.15
import net.minecraft.client.render.RenderLayer;
//? if >=1.21.11
//import net.minecraft.client.render.RenderSetup;
//? if >=1.15
import net.minecraft.client.render.VertexConsumer;
//? if >=1.15
import net.minecraft.client.render.VertexConsumerProvider;
//? if <1.15
/*import net.minecraft.client.render.Tessellator;*/
//? if <1.15
/*import net.minecraft.client.render.VertexFormats;*/
//? if >=1.15
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.World;

public final class TorchmasterLightRangeRenderer
{
    //? if >=1.21.11
    //private static final RenderLayer LINE_LAYER = RenderLayer.of("torchmaster_lines", RenderSetup.builder(RenderPipelines.LINES).build());

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
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player == null || minecraft.player.isSneaking()) {
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.lineWidth(TorchmasterLineBoxRenderer.LINE_WIDTH);
        buffer.begin(1, VertexFormats.POSITION_COLOR);
        TorchmasterRangeRenderPlan plan = TorchmasterRangeRenderPlan.fromSnapshots(TorchmasterLightRangeDisplay.snapshots(level));
        for (TorchmasterRangeRenderPlan.Entry entry : plan.entries()) {
            TorchmasterRangeLineSubmitter.submitBox(camera, buffer, entry.box, entry.style);
        }
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture();
    }
    *///?}

    //? if >=1.15 {
    public static void renderAndFlush(World level, Camera camera, MatrixStack poseStack, VertexConsumerProvider.Immediate bufferSource)
    {
        render(level, camera, poseStack, bufferSource);
        flushLines(bufferSource);
    }

    public static void render(World level, Camera camera, MatrixStack poseStack, VertexConsumerProvider bufferSource)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player == null || minecraft.player.isSneaking()) {
            return;
        }

        //? if >=1.21.11 {
        /*VertexConsumer lineBuffer = bufferSource.getBuffer(LINE_LAYER);
        *///?} else {
        VertexConsumer lineBuffer = bufferSource.getBuffer(RenderLayer.getLines());
        //?}

        poseStack.push();
        //? if >=1.21.11 {
        /*poseStack.translate(-camera.getCameraPos().x, -camera.getCameraPos().y, -camera.getCameraPos().z);
        *///?} else {
        poseStack.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);
        //?}
        TorchmasterRangeRenderPlan plan = TorchmasterRangeRenderPlan.fromSnapshots(TorchmasterLightRangeDisplay.snapshots(level));
        for (TorchmasterRangeRenderPlan.Entry entry : plan.entries()) {
            TorchmasterRangeLineSubmitter.submitBox(poseStack, lineBuffer, entry.box, entry.style);
        }
        poseStack.pop();
    }

    private static void flushLines(VertexConsumerProvider.Immediate bufferSource)
    {
        //? if >=1.21.11 {
        /*bufferSource.draw(LINE_LAYER);
        *///?} else {
        bufferSource.draw(RenderLayer.getLines());
        //?}
    }

    //?}
}
