package net.xalcon.torchmaster.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TorchmasterPanelRendererTest
{
    @Test
    void backgroundUsesSharedPanelColor()
    {
        TorchmasterPanelRenderer.Fill fill = TorchmasterPanelRenderer.background(10, 20, 30, 40);

        assertFill(fill, 10, 20, 30, 40, TorchmasterPanelRenderer.BACKGROUND_COLOR);
    }

    @Test
    void frameProducesFourOnePixelEdges()
    {
        TorchmasterPanelRenderer.Fill[] frame = TorchmasterPanelRenderer.frame(10, 20, 30, 40);

        assertEquals(4, frame.length);
        assertFill(frame[0], 10, 20, 30, 21, TorchmasterPanelRenderer.FRAME_LIGHT_COLOR);
        assertFill(frame[1], 10, 20, 11, 40, TorchmasterPanelRenderer.FRAME_LIGHT_COLOR);
        assertFill(frame[2], 10, 39, 30, 40, TorchmasterPanelRenderer.FRAME_DARK_COLOR);
        assertFill(frame[3], 29, 20, 30, 40, TorchmasterPanelRenderer.FRAME_DARK_COLOR);
    }

    private static void assertFill(TorchmasterPanelRenderer.Fill fill, int left, int top, int right, int bottom, int color)
    {
        assertEquals(left, fill.left);
        assertEquals(top, fill.top);
        assertEquals(right, fill.right);
        assertEquals(bottom, fill.bottom);
        assertEquals(color, fill.color);
    }
}
