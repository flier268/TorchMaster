package net.xalcon.torchmaster.client;

final class TorchmasterConfigScreenActions
{
    private static final int BUTTON_GAP = 4;

    private TorchmasterConfigScreenActions()
    {
    }

    static ButtonDescriptor[] bottomButtons(TorchmasterConfigScreenLayout layout, int screenHeight, int buttonHeight)
    {
        int buttonWidth = layout.bottomButtonWidth();
        int totalButtonWidth = buttonWidth * 3 + BUTTON_GAP * 2;
        int buttonX = layout.panelLeft() + (layout.panelWidth() - totalButtonWidth) / 2;
        int buttonY = screenHeight - 28;
        return new ButtonDescriptor[] {
                new ButtonDescriptor(Action.SAVE, "screen.torchmaster.config.save", buttonX, buttonY, buttonWidth, buttonHeight),
                new ButtonDescriptor(Action.RESET, "screen.torchmaster.config.reset", buttonX + buttonWidth + BUTTON_GAP, buttonY, buttonWidth, buttonHeight),
                new ButtonDescriptor(Action.DONE, "gui.done", buttonX + (buttonWidth + BUTTON_GAP) * 2, buttonY, buttonWidth, buttonHeight)
        };
    }

    enum Action
    {
        SAVE,
        RESET,
        DONE
    }

    static final class ButtonDescriptor
    {
        final Action action;
        final String translationKey;
        final int x;
        final int y;
        final int width;
        final int height;

        private ButtonDescriptor(Action action, String translationKey, int x, int y, int width, int height)
        {
            this.action = action;
            this.translationKey = translationKey;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
}
