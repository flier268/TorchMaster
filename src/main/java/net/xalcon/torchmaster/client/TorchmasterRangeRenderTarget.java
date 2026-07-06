package net.xalcon.torchmaster.client;

import net.minecraft.client.render.Camera;
//? if >=1.15
import net.minecraft.client.render.VertexConsumer;
//? if >=1.15
import net.minecraft.client.render.VertexConsumerProvider;
//? if >=1.15
import net.minecraft.client.util.math.MatrixStack;

final class TorchmasterRangeRenderTarget
{
    private TorchmasterRangeRenderTarget()
    {
    }

    static TorchmasterRangeRenderBackendDescriptor.CameraOffset cameraOffset(double x, double y, double z)
    {
        return TorchmasterRangeRenderBackend.cameraOffset(x, y, z);
    }

    static TorchmasterRangeRenderBackendDescriptor.LegacySessionState legacySessionState()
    {
        return TorchmasterRangeRenderBackend.legacySessionState();
    }

    //? if >=1.15 {
    static VertexConsumer lineBuffer(VertexConsumerProvider bufferSource)
    {
        TorchmasterRangeRenderBackendDescriptor.LineLayerChoice layer = TorchmasterRangeRenderBackend.active().lineLayer();
        //? if >=1.21.11 {
        /*return TorchmasterLatestRangeRenderTarget.lineBuffer(bufferSource, layer);
        *///?} else {
        return TorchmasterVanillaRangeRenderTarget.lineBuffer(bufferSource, layer);
        //?}
    }

    static void flushLines(VertexConsumerProvider.Immediate bufferSource)
    {
        TorchmasterRangeRenderBackendDescriptor.FlushTarget target = TorchmasterRangeRenderBackend.active().flushTarget();
        //? if >=1.21.11 {
        /*TorchmasterLatestRangeRenderTarget.flushLines(bufferSource, target);
        *///?} else {
        TorchmasterVanillaRangeRenderTarget.flushLines(bufferSource, target);
        //?}
    }

    static void translateToCamera(MatrixStack poseStack, Camera camera)
    {
        //? if >=1.21.11 {
        /*TorchmasterRangeRenderBackendDescriptor.CameraOffset offset = cameraOffset(camera.getCameraPos().x, camera.getCameraPos().y, camera.getCameraPos().z);
        *///?} else {
        TorchmasterRangeRenderBackendDescriptor.CameraOffset offset = cameraOffset(camera.getPos().x, camera.getPos().y, camera.getPos().z);
        //?}
        poseStack.translate(offset.x, offset.y, offset.z);
    }
    //?} else {
    /*static TorchmasterLegacyRangeRenderTarget.LegacyTarget beginLegacy()
    {
        return TorchmasterLegacyRangeRenderTarget.begin(legacySessionState());
    }

    static void endLegacy(TorchmasterLegacyRangeRenderTarget.LegacyTarget target)
    {
        TorchmasterLegacyRangeRenderTarget.end(target);
    }
    *///?}
}
