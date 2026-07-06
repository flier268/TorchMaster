package net.xalcon.torchmaster.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterConfigScreenLayoutTest
{
    @Test
    void wideLayoutKeepsFieldsOnRight()
    {
        TorchmasterConfigScreenLayout layout = new TorchmasterConfigScreenLayout(800, 600);

        assertEquals(470, layout.panelWidth());
        assertEquals(165, layout.panelLeft());
        assertFalse(layout.compact());
        assertEquals(32, layout.rowHeight());
        assertEquals(150, layout.fieldWidth());
        assertEquals(235, layout.listFieldWidth());
        assertEquals(473, layout.fieldX());
        assertEquals(388, layout.listFieldX());
        assertEquals(96, layout.booleanButtonWidth());
        assertEquals(527, layout.booleanButtonX(layout.fieldX()));
    }

    @Test
    void compactLayoutUsesFullWidthFields()
    {
        TorchmasterConfigScreenLayout layout = new TorchmasterConfigScreenLayout(360, 240);

        assertEquals(336, layout.panelWidth());
        assertEquals(12, layout.panelLeft());
        assertTrue(layout.compact());
        assertEquals(44, layout.rowHeight());
        assertEquals(312, layout.fieldWidth());
        assertEquals(312, layout.listFieldWidth());
        assertEquals(24, layout.fieldX());
        assertEquals(24, layout.listFieldX());
        assertEquals(312, layout.booleanButtonWidth());
        assertEquals(24, layout.booleanButtonX(layout.fieldX()));
        assertEquals(64, layout.widgetY(48));
    }

    @Test
    void bottomButtonsStayInsidePanel()
    {
        TorchmasterConfigScreenLayout layout = new TorchmasterConfigScreenLayout(220, 200);
        int totalButtonWidth = layout.bottomButtonWidth() * 3 + 8;

        assertTrue(totalButtonWidth <= layout.panelWidth());
    }

    @Test
    void scrollMathUsesSameRowHeightAsLayout()
    {
        TorchmasterConfigScreenLayout wide = new TorchmasterConfigScreenLayout(800, 220);
        TorchmasterConfigScreenLayout compact = new TorchmasterConfigScreenLayout(360, 220);

        assertEquals(32, TorchmasterConfigWidgetRows.scrollOffset(0, -1.0D, 11, wide, 220));
        assertEquals(44, TorchmasterConfigWidgetRows.scrollOffset(0, -1.0D, 11, compact, 220));
    }
}
