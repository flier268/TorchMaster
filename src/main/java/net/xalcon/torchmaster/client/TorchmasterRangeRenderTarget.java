package net.xalcon.torchmaster.client;

//? if >=1.21.11
//import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.Camera;
//? if >=1.15
import net.minecraft.client.render.RenderLayer;
//? if >=1.21.11
//import net.minecraft.client.render.RenderSetup;
//? if >=1.15
import net.minecraft.client.render.VertexConsumer;
//? if >=1.15
import net.minecraft.client.render.VertexConsumerProvider;
//? if >=1.15
import net.minecraft.client.util.math.MatrixStack;

final class TorchmasterRangeRenderTarget
{
    //? if >=1.21.11
    //private static final RenderLayer LINE_LAYER = RenderLayer.of("torchmaster_lines", RenderSetup.builder(RenderPipelines.LINES).build());

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
        /*if (layer == TorchmasterRangeRenderBackendDescriptor.LineLayerChoice.CUSTOM_PIPELINE) {
            return bufferSource.getBuffer(LINE_LAYER);
        }
        *///?} else {
        if (layer == TorchmasterRangeRenderBackendDescriptor.LineLayerChoice.VANILLA_LINES) {
            return bufferSource.getBuffer(RenderLayer.getLines());
        }
        //?}
        throw new IllegalStateException("Unsupported range render line layer " + layer);
    }

    static void flushLines(VertexConsumerProvider.Immediate bufferSource)
    {
        TorchmasterRangeRenderBackendDescriptor.FlushTarget target = TorchmasterRangeRenderBackend.active().flushTarget();
        //? if >=1.21.11 {
        /*if (target == TorchmasterRangeRenderBackendDescriptor.FlushTarget.CUSTOM_PIPELINE) {
            bufferSource.draw(LINE_LAYER);
            return;
        }
        *///?} else {
        if (target == TorchmasterRangeRenderBackendDescriptor.FlushTarget.VANILLA_LINES) {
            bufferSource.draw(RenderLayer.getLines());
            return;
        }
        //?}
        throw new IllegalStateException("Unsupported range render flush target " + target);
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
