package net.xalcon.torchmaster.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterRangeRenderBackendTest
{
    @Test
    void activeBackendUsesExpectedLineLayerAndFlushTarget()
    {
        //? if >=1.21.11 {
        /*assertEquals(TorchmasterRangeRenderBackendDescriptor.LineLayerChoice.CUSTOM_PIPELINE, TorchmasterRangeRenderBackend.active().lineLayer());
        assertEquals(TorchmasterRangeRenderBackendDescriptor.FlushTarget.CUSTOM_PIPELINE, TorchmasterRangeRenderBackend.active().flushTarget());
        *///?} else {
        assertEquals(TorchmasterRangeRenderBackendDescriptor.LineLayerChoice.VANILLA_LINES, TorchmasterRangeRenderBackend.active().lineLayer());
        assertEquals(TorchmasterRangeRenderBackendDescriptor.FlushTarget.VANILLA_LINES, TorchmasterRangeRenderBackend.active().flushTarget());
        //?}
    }

    @Test
    void cameraOffsetTranslatesWorldOppositeCamera()
    {
        TorchmasterRangeRenderBackendDescriptor.CameraOffset offset = TorchmasterRangeRenderBackend.active().cameraOffset(1.5, -2.0, 3.25);

        assertEquals(-1.5, offset.x, 0.0001);
        assertEquals(2.0, offset.y, 0.0001);
        assertEquals(-3.25, offset.z, 0.0001);
    }

    @Test
    void legacySessionStateKeepsLineRenderingSetup()
    {
        TorchmasterRangeRenderBackendDescriptor descriptor = TorchmasterRangeRenderBackend.active();
        TorchmasterRangeRenderBackendDescriptor.LegacySessionState state = descriptor.legacySessionState();

        assertTrue(state.disableTexture);
        assertTrue(state.enableBlend);
        assertEquals(TorchmasterLineBoxRenderer.LINE_WIDTH, state.lineWidth, 0.0001);
        assertEquals(TorchmasterLineBoxRenderer.LINE_WIDTH, descriptor.lineWidth(), 0.0001);
    }
}
