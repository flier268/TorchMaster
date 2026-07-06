package net.xalcon.torchmaster.client;

final class TorchmasterConfigScreenLayout
{
    private static final int MAX_PANEL_WIDTH = 470;
    private static final int SIDE_MARGIN = 12;
    private static final int WIDE_ROW_HEIGHT = 32;
    private static final int COMPACT_ROW_HEIGHT = 44;
    private static final int COMPACT_BREAKPOINT = 420;
    private static final int WIDE_FIELD_WIDTH = 150;
    private static final int WIDE_LIST_FIELD_WIDTH = 240;
    private static final int BUTTON_WIDTH = 96;

    private final int screenWidth;
    private final int screenHeight;
    private final int panelWidth;
    private final int panelLeft;
    private final boolean compact;
    private final int fieldWidth;
    private final int listFieldWidth;

    TorchmasterConfigScreenLayout(int screenWidth, int screenHeight)
    {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.panelWidth = Math.max(180, Math.min(MAX_PANEL_WIDTH, screenWidth - SIDE_MARGIN * 2));
        this.panelLeft = Math.max(SIDE_MARGIN, (screenWidth - panelWidth) / 2);
        this.compact = panelWidth < COMPACT_BREAKPOINT;
        this.fieldWidth = compact ? Math.max(80, panelWidth - 24) : Math.min(WIDE_FIELD_WIDTH, Math.max(110, panelWidth / 3));
        this.listFieldWidth = compact ? Math.max(80, panelWidth - 24) : Math.min(WIDE_LIST_FIELD_WIDTH, Math.max(160, panelWidth / 2));
    }

    int screenWidth()
    {
        return screenWidth;
    }

    int screenHeight()
    {
        return screenHeight;
    }

    int panelWidth()
    {
        return panelWidth;
    }

    int panelLeft()
    {
        return panelLeft;
    }

    int panelRight()
    {
        return panelLeft + panelWidth;
    }

    int panelTop()
    {
        return 32;
    }

    int panelBottom()
    {
        return screenHeight - 34;
    }

    boolean compact()
    {
        return compact;
    }

    int rowHeight()
    {
        return compact ? COMPACT_ROW_HEIGHT : WIDE_ROW_HEIGHT;
    }

    int fieldWidth()
    {
        return fieldWidth;
    }

    int listFieldWidth()
    {
        return listFieldWidth;
    }

    int fieldX()
    {
        return compact ? panelLeft + 12 : panelLeft + panelWidth - fieldWidth - 12;
    }

    int listFieldX()
    {
        return compact ? panelLeft + 12 : panelLeft + panelWidth - listFieldWidth - 12;
    }

    int widgetY(int rowY)
    {
        return compact ? rowY + 16 : rowY;
    }

    int booleanButtonWidth()
    {
        return compact ? fieldWidth : BUTTON_WIDTH;
    }

    int booleanButtonX(int fieldX)
    {
        return compact ? fieldX : fieldX + fieldWidth - booleanButtonWidth();
    }

    int bottomButtonWidth()
    {
        return Math.min(100, Math.max(52, (panelWidth - 32) / 3));
    }
}
