package net.xalcon.torchmaster.client;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterConfigWidgetAdapterTest
{
    @Test
    void integerTextAcceptsOnlySignedIntegerDrafts()
    {
        assertTrue(TorchmasterConfigWidgetAdapter.isIntegerText(""));
        assertTrue(TorchmasterConfigWidgetAdapter.isIntegerText("-"));
        assertTrue(TorchmasterConfigWidgetAdapter.isIntegerText("-12"));
        assertTrue(TorchmasterConfigWidgetAdapter.isIntegerText("42"));
        assertFalse(TorchmasterConfigWidgetAdapter.isIntegerText("4.2"));
        assertFalse(TorchmasterConfigWidgetAdapter.isIntegerText("abc"));
    }

    @Test
    void listTextKeepsConfigUiJoinFormat()
    {
        assertEquals("", TorchmasterConfigWidgetAdapter.listText(Collections.emptyList()));
        assertEquals("+minecraft:zombie, -minecraft:phantom",
                TorchmasterConfigWidgetAdapter.listText(Arrays.asList("+minecraft:zombie", "-minecraft:phantom")));
    }
}
