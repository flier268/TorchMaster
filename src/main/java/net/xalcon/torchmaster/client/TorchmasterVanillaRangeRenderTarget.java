package net.xalcon.torchmaster.client;

//? if >=1.15 <1.21.11 {
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
//?}

final class TorchmasterVanillaRangeRenderTarget
{
    private TorchmasterVanillaRangeRenderTarget()
    {
    }

    //? if >=1.15 <1.21.11 {
    static VertexConsumer lineBuffer(VertexConsumerProvider bufferSource, TorchmasterRangeRenderBackendDescriptor.LineLayerChoice layer)
    {
        if (layer == TorchmasterRangeRenderBackendDescriptor.LineLayerChoice.VANILLA_LINES) {
            return bufferSource.getBuffer(RenderLayer.getLines());
        }
        throw new IllegalStateException("Unsupported range render line layer " + layer);
    }

    static void flushLines(VertexConsumerProvider.Immediate bufferSource, TorchmasterRangeRenderBackendDescriptor.FlushTarget target)
    {
        if (target == TorchmasterRangeRenderBackendDescriptor.FlushTarget.VANILLA_LINES) {
            bufferSource.draw(RenderLayer.getLines());
            return;
        }
        throw new IllegalStateException("Unsupported range render flush target " + target);
    }
    //?}
}
