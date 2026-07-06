package net.xalcon.torchmaster.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TorchmasterConfigScreenActionsTest
{
    @Test
    void bottomButtonsKeepExistingOrderAndGeometry()
    {
        TorchmasterConfigScreenLayout layout = new TorchmasterConfigScreenLayout(800, 240);

        TorchmasterConfigScreenActions.ButtonDescriptor[] buttons = TorchmasterConfigScreenActions.bottomButtons(layout, 240, 20);

        assertEquals(3, buttons.length);
        assertButton(buttons[0], TorchmasterConfigScreenActions.Action.SAVE, "screen.torchmaster.config.save", 246, 212, 100, 20);
        assertButton(buttons[1], TorchmasterConfigScreenActions.Action.RESET, "screen.torchmaster.config.reset", 350, 212, 100, 20);
        assertButton(buttons[2], TorchmasterConfigScreenActions.Action.DONE, "gui.done", 454, 212, 100, 20);
    }

    private static void assertButton(TorchmasterConfigScreenActions.ButtonDescriptor button, TorchmasterConfigScreenActions.Action action,
            String translationKey, int x, int y, int width, int height)
    {
        assertEquals(action, button.action);
        assertEquals(translationKey, button.translationKey);
        assertEquals(x, button.x);
        assertEquals(y, button.y);
        assertEquals(width, button.width);
        assertEquals(height, button.height);
    }
}
