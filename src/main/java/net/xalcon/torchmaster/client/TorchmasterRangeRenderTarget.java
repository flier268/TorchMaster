package net.xalcon.torchmaster.client;

//? if <1.15 {
/*import com.mojang.blaze3d.platform.GlStateManager;
*///?}
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

final class TorchmasterRangeRenderTarget
{
    //? if >=1.21.11
    //private static final RenderLayer LINE_LAYER = RenderLayer.of("torchmaster_lines", RenderSetup.builder(RenderPipelines.LINES).build());

    private TorchmasterRangeRenderTarget()
    {
    }

    static CameraOffset cameraOffset(double x, double y, double z)
    {
        return TorchmasterRangeRenderBackend.cameraOffset(x, y, z);
    }

    static LegacySessionState legacySessionState()
    {
        return TorchmasterRangeRenderBackend.legacySessionState();
    }

    //? if >=1.15 {
    static VertexConsumer lineBuffer(VertexConsumerProvider bufferSource)
    {
        TorchmasterRangeRenderBackend.LineLayerChoice layer = TorchmasterRangeRenderBackend.activeLineLayer();
        //? if >=1.21.11 {
        /*if (layer == TorchmasterRangeRenderBackend.LineLayerChoice.CUSTOM_PIPELINE) {
            return bufferSource.getBuffer(LINE_LAYER);
        }
        *///?} else {
        if (layer == TorchmasterRangeRenderBackend.LineLayerChoice.VANILLA_LINES) {
            return bufferSource.getBuffer(RenderLayer.getLines());
        }
        //?}
        throw new IllegalStateException("Unsupported range render line layer " + layer);
    }

    static void flushLines(VertexConsumerProvider.Immediate bufferSource)
    {
        TorchmasterRangeRenderBackend.FlushTarget target = TorchmasterRangeRenderBackend.activeFlushTarget();
        //? if >=1.21.11 {
        /*if (target == TorchmasterRangeRenderBackend.FlushTarget.CUSTOM_PIPELINE) {
            bufferSource.draw(LINE_LAYER);
            return;
        }
        *///?} else {
        if (target == TorchmasterRangeRenderBackend.FlushTarget.VANILLA_LINES) {
            bufferSource.draw(RenderLayer.getLines());
            return;
        }
        //?}
        throw new IllegalStateException("Unsupported range render flush target " + target);
    }

    static void translateToCamera(MatrixStack poseStack, Camera camera)
    {
        //? if >=1.21.11 {
        /*CameraOffset offset = cameraOffset(camera.getCameraPos().x, camera.getCameraPos().y, camera.getCameraPos().z);
        *///?} else {
        CameraOffset offset = cameraOffset(camera.getPos().x, camera.getPos().y, camera.getPos().z);
        //?}
        poseStack.translate(offset.x, offset.y, offset.z);
    }
    //?} else {
    /*static LegacyTarget beginLegacy()
    {
        LegacySessionState state = legacySessionState();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        if (state.disableTexture) {
            GlStateManager.disableTexture();
        }
        if (state.enableBlend) {
            GlStateManager.enableBlend();
        }
        GlStateManager.lineWidth(state.lineWidth);
        buffer.begin(1, VertexFormats.POSITION_COLOR);
        return new LegacyTarget(tessellator, buffer, state);
    }

    static void endLegacy(LegacyTarget target)
    {
        target.tessellator.draw();
        if (target.state.enableBlend) {
            GlStateManager.disableBlend();
        }
        if (target.state.disableTexture) {
            GlStateManager.enableTexture();
        }
    }
    *///?}

    static final class CameraOffset
    {
        final double x;
        final double y;
        final double z;

        CameraOffset(double x, double y, double z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    static final class LegacySessionState
    {
        final boolean disableTexture;
        final boolean enableBlend;
        final float lineWidth;

        LegacySessionState(boolean disableTexture, boolean enableBlend, float lineWidth)
        {
            this.disableTexture = disableTexture;
            this.enableBlend = enableBlend;
            this.lineWidth = lineWidth;
        }
    }

    //? if <1.15 {
    /*static final class LegacyTarget
    {
        final Tessellator tessellator;
        final BufferBuilder buffer;
        final LegacySessionState state;

        private LegacyTarget(Tessellator tessellator, BufferBuilder buffer, LegacySessionState state)
        {
            this.tessellator = tessellator;
            this.buffer = buffer;
            this.state = state;
        }
    }
    *///?}
}
