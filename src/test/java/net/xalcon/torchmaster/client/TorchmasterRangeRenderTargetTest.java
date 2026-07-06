package net.xalcon.torchmaster.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterRangeRenderTargetTest
{
    @Test
    void cameraOffsetTranslatesWorldOppositeCamera()
    {
        TorchmasterRangeRenderTarget.CameraOffset offset = TorchmasterRangeRenderTarget.cameraOffset(1.5, -2.0, 3.25);

        assertEquals(-1.5, offset.x, 0.0001);
        assertEquals(2.0, offset.y, 0.0001);
        assertEquals(-3.25, offset.z, 0.0001);
    }

    @Test
    void legacySessionStateKeepsLineRenderingSetup()
    {
        TorchmasterRangeRenderTarget.LegacySessionState state = TorchmasterRangeRenderTarget.legacySessionState();

        assertTrue(state.disableTexture);
        assertTrue(state.enableBlend);
        assertEquals(TorchmasterLineBoxRenderer.LINE_WIDTH, state.lineWidth, 0.0001);
    }
}
