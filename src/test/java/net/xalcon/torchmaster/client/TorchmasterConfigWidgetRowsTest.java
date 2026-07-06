package net.xalcon.torchmaster.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterConfigWidgetRowsTest
{
    @Test
    void maxScrollUsesConfiguredViewportReservation()
    {
        TorchmasterConfigScreenLayout layout = new TorchmasterConfigScreenLayout(800, 220);

        assertEquals(226, TorchmasterConfigWidgetRows.maxScroll(11, layout, 220));
    }

    @Test
    void scrollOffsetClampsToViewport()
    {
        TorchmasterConfigScreenLayout layout = new TorchmasterConfigScreenLayout(800, 220);

        assertEquals(32, TorchmasterConfigWidgetRows.scrollOffset(0, -1.0D, 11, layout, 220));
        assertEquals(0, TorchmasterConfigWidgetRows.scrollOffset(10, 1.0D, 11, layout, 220));
        assertEquals(226, TorchmasterConfigWidgetRows.scrollOffset(220, -10.0D, 11, layout, 220));
    }

    @Test
    void visibilityKeepsRowsInsideViewport()
    {
        assertTrue(TorchmasterConfigWidgetRows.isVisible(12, 32, 162, 20));
        assertTrue(TorchmasterConfigWidgetRows.isVisible(142, 32, 162, 20));
        assertFalse(TorchmasterConfigWidgetRows.isVisible(11, 32, 162, 20));
        assertFalse(TorchmasterConfigWidgetRows.isVisible(143, 32, 162, 20));
    }
}
