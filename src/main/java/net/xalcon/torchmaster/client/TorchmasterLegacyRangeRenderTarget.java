package net.xalcon.torchmaster.client;

//? if <1.15 {
/*import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
*///?}

final class TorchmasterLegacyRangeRenderTarget
{
    private TorchmasterLegacyRangeRenderTarget()
    {
    }

    //? if <1.15 {
    /*static LegacyTarget begin(TorchmasterRangeRenderBackendDescriptor.LegacySessionState state)
    {
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

    static void end(LegacyTarget target)
    {
        target.tessellator.draw();
        if (target.state.enableBlend) {
            GlStateManager.disableBlend();
        }
        if (target.state.disableTexture) {
            GlStateManager.enableTexture();
        }
    }

    static final class LegacyTarget
    {
        final Tessellator tessellator;
        final BufferBuilder buffer;
        final TorchmasterRangeRenderBackendDescriptor.LegacySessionState state;

        private LegacyTarget(Tessellator tessellator, BufferBuilder buffer, TorchmasterRangeRenderBackendDescriptor.LegacySessionState state)
        {
            this.tessellator = tessellator;
            this.buffer = buffer;
            this.state = state;
        }
    }
    *///?}
}
