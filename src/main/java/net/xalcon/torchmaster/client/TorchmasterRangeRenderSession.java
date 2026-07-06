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

final class TorchmasterRangeRenderSession
{
    //? if >=1.21.11
    //private static final RenderLayer LINE_LAYER = RenderLayer.of("torchmaster_lines", RenderSetup.builder(RenderPipelines.LINES).build());

    private TorchmasterRangeRenderSession()
    {
    }

    static boolean shouldRender(boolean hasPlayer, boolean playerSneaking)
    {
        return hasPlayer && !playerSneaking;
    }

    private static boolean shouldRender(MinecraftClient minecraft)
    {
        return shouldRender(minecraft.player != null, minecraft.player != null && minecraft.player.isSneaking());
    }

    static TorchmasterRangeRenderPlan plan(World level)
    {
        return TorchmasterRangeRenderPlan.fromSnapshots(TorchmasterLightRangeDisplay.snapshots(level));
    }

    //? if >=1.15 {
    static void render(World level, Camera camera, MatrixStack poseStack, VertexConsumerProvider bufferSource)
    {
        if (!shouldRender(MinecraftClient.getInstance())) {
            return;
        }

        VertexConsumer lineBuffer = lineBuffer(bufferSource);
        poseStack.push();
        translateToCamera(poseStack, camera);
        submitPlan(poseStack, lineBuffer, plan(level));
        poseStack.pop();
    }

    static void flushLines(VertexConsumerProvider.Immediate bufferSource)
    {
        //? if >=1.21.11 {
        /*bufferSource.draw(LINE_LAYER);
        *///?} else {
        bufferSource.draw(RenderLayer.getLines());
        //?}
    }

    private static VertexConsumer lineBuffer(VertexConsumerProvider bufferSource)
    {
        //? if >=1.21.11 {
        /*return bufferSource.getBuffer(LINE_LAYER);
        *///?} else {
        return bufferSource.getBuffer(RenderLayer.getLines());
        //?}
    }

    private static void translateToCamera(MatrixStack poseStack, Camera camera)
    {
        //? if >=1.21.11 {
        /*poseStack.translate(-camera.getCameraPos().x, -camera.getCameraPos().y, -camera.getCameraPos().z);
        *///?} else {
        poseStack.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);
        //?}
    }

    private static void submitPlan(MatrixStack poseStack, VertexConsumer lineBuffer, TorchmasterRangeRenderPlan plan)
    {
        for (TorchmasterRangeRenderPlan.Entry entry : plan.entries()) {
            TorchmasterRangeLineSubmitter.submitBox(poseStack, lineBuffer, entry.box, entry.style);
        }
    }
    //?} else {
    /*static void render(World level, Camera camera)
    {
        if (!shouldRender(MinecraftClient.getInstance())) {
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.lineWidth(TorchmasterLineBoxRenderer.LINE_WIDTH);
        buffer.begin(1, VertexFormats.POSITION_COLOR);
        for (TorchmasterRangeRenderPlan.Entry entry : plan(level).entries()) {
            TorchmasterRangeLineSubmitter.submitBox(camera, buffer, entry.box, entry.style);
        }
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture();
    }
    *///?}
}
