package net.xalcon.torchmaster.client;

//? if >=1.21.11 {
/*import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
*///?}

final class TorchmasterLatestRangeRenderTarget
{
    //? if >=1.21.11
    /*private static final RenderLayer LINE_LAYER = RenderLayer.of("torchmaster_lines", RenderSetup.builder(RenderPipelines.LINES).build());*/

    private TorchmasterLatestRangeRenderTarget()
    {
    }

    //? if >=1.21.11 {
    /*static VertexConsumer lineBuffer(VertexConsumerProvider bufferSource, TorchmasterRangeRenderBackendDescriptor.LineLayerChoice layer)
    {
        if (layer == TorchmasterRangeRenderBackendDescriptor.LineLayerChoice.CUSTOM_PIPELINE) {
            return bufferSource.getBuffer(LINE_LAYER);
        }
        throw new IllegalStateException("Unsupported range render line layer " + layer);
    }

    static void flushLines(VertexConsumerProvider.Immediate bufferSource, TorchmasterRangeRenderBackendDescriptor.FlushTarget target)
    {
        if (target == TorchmasterRangeRenderBackendDescriptor.FlushTarget.CUSTOM_PIPELINE) {
            bufferSource.draw(LINE_LAYER);
            return;
        }
        throw new IllegalStateException("Unsupported range render flush target " + target);
    }
    *///?}
}
