package net.xalcon.torchmaster.client;

import net.minecraft.client.MinecraftClient;
//? if <1.15
/*import net.minecraft.client.render.BufferBuilder;*/
import net.minecraft.client.render.Camera;
//? if >=1.15
import net.minecraft.client.render.VertexConsumer;
//? if >=1.15
import net.minecraft.client.render.VertexConsumerProvider;
//? if >=1.15
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.World;

final class TorchmasterRangeRenderSession
{
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

        VertexConsumer lineBuffer = TorchmasterRangeRenderTarget.lineBuffer(bufferSource);
        poseStack.push();
        TorchmasterRangeRenderTarget.translateToCamera(poseStack, camera);
        submitPlan(poseStack, lineBuffer, plan(level));
        poseStack.pop();
    }

    static void flushLines(VertexConsumerProvider.Immediate bufferSource)
    {
        TorchmasterRangeRenderTarget.flushLines(bufferSource);
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

        TorchmasterRangeRenderTarget.LegacyTarget target = TorchmasterRangeRenderTarget.beginLegacy();
        BufferBuilder buffer = target.buffer;
        for (TorchmasterRangeRenderPlan.Entry entry : plan(level).entries()) {
            TorchmasterRangeLineSubmitter.submitBox(camera, buffer, entry.box, entry.style);
        }
        TorchmasterRangeRenderTarget.endLegacy(target);
    }
    *///?}
}
