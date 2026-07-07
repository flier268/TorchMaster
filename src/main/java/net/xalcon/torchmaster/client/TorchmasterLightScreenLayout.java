package net.xalcon.torchmaster.client;

final class TorchmasterLightScreenLayout
{
    private final int width;
    private final int height;
    private final int panelWidth;
    private final int panelHeight;
    private final int scrollOffset;

    TorchmasterLightScreenLayout(int width, int height, int panelWidth, int panelHeight)
    {
        this(width, height, panelWidth, panelHeight, 0);
    }

    TorchmasterLightScreenLayout(int width, int height, int panelWidth, int panelHeight, int scrollOffset)
    {
        this.width = width;
        this.height = height;
        this.panelWidth = Math.min(panelWidth, Math.max(160, width - 16));
        this.panelHeight = Math.min(panelHeight, Math.max(120, height - 8));
        this.scrollOffset = Math.max(0, scrollOffset);
    }

    int centerX()
    {
        return width / 2;
    }

    int panelLeft()
    {
        return (width - panelWidth) / 2;
    }

    int panelTop()
    {
        return Math.max(4, (height - panelHeight) / 2);
    }

    int panelRight()
    {
        return panelLeft() + panelWidth;
    }

    int panelWidth()
    {
        return panelWidth;
    }

    int panelBottom()
    {
        return panelTop() + panelHeight;
    }

    int rangeSwatchLeft()
    {
        return visibilitySwitchX() - rangeSwatchSize() - 8;
    }

    int rangeSwatchTop()
    {
        return visibilityY() + 1;
    }

    int rangeSwatchSize()
    {
        return 12;
    }

    int settingsButtonX()
    {
        return panelRight() - 30;
    }

    int settingsButtonY()
    {
        return panelTop() + 10;
    }

    int titleY()
    {
        return panelTop() + 10;
    }

    int blockY()
    {
        return panelTop() + 24;
    }

    int visibilityY()
    {
        return radiusLabelY() - 4;
    }

    int visibilityLabelX()
    {
        return panelRight() - 142;
    }

    int visibilitySwitchX()
    {
        return panelRight() - 58;
    }

    int switchWidth()
    {
        return TorchmasterSwitchControl.WIDTH;
    }

    int switchHeight()
    {
        return TorchmasterSwitchControl.HEIGHT;
    }

    int enabledLabelX()
    {
        return panelLeft() + 26;
    }

    int enabledY()
    {
        return contentY(64);
    }

    int enabledSwitchX()
    {
        return panelLeft() + 92;
    }

    int enabledSwitchY()
    {
        return enabledY() + 2;
    }

    int radiusLabelY()
    {
        return contentY(84);
    }

    int radiusAxisLabelY()
    {
        return radiusFieldY(0) + 6;
    }

    int radiusFieldY()
    {
        return radiusFieldY(0);
    }

    int radiusFieldY(int row)
    {
        return contentY(104 + row * 24);
    }

    int radiusFieldX(int index)
    {
        return rangeMinusX(index);
    }

    int radiusDirectionLabelX(int index)
    {
        return panelLeft() + 26 + index * radiusSlotWidth();
    }

    int rangeMinusX(int index)
    {
        return radiusDirectionLabelX(index) + 44;
    }

    int rangeValueX(int index)
    {
        return rangeMinusX(index) + 25;
    }

    int rangeValueCenterX(int index)
    {
        return rangeValueX(index) + 10;
    }

    int rangePlusX(int index)
    {
        return rangeValueX(index) + 30;
    }

    int rangeButtonWidth()
    {
        return 20;
    }

    int limitY()
    {
        return contentY(176);
    }

    int editableY()
    {
        return limitY();
    }

    int lockX()
    {
        return panelRight() - 40;
    }

    int accessButtonY()
    {
        return settingsButtonY();
    }

    int accessButtonX()
    {
        return panelRight() - 92;
    }

    int accessButtonWidth()
    {
        return 56;
    }

    int footerButtonWidth()
    {
        return Math.max(60, (panelWidth - 68) / 3);
    }

    int footerButtonX(int index)
    {
        return panelLeft() + 26 + index * (footerButtonWidth() + 8);
    }

    int footerY()
    {
        return panelBottom() - 28;
    }

    int scrollOffset()
    {
        return scrollOffset;
    }

    int maxScroll()
    {
        return Math.max(0, contentBaseBottom() - contentViewportBottom());
    }

    int contentViewportTop()
    {
        return panelTop() + 54;
    }

    int contentViewportBottom()
    {
        return footerY() - 8;
    }

    int scrollbarTop()
    {
        return contentViewportTop();
    }

    int scrollbarBottom()
    {
        return contentViewportBottom();
    }

    private int radiusSlotWidth()
    {
        return Math.max(120, (panelWidth - 52) / 2);
    }

    private int contentY(int baseOffset)
    {
        return panelTop() + baseOffset - scrollOffset;
    }

    private int contentBaseBottom()
    {
        return panelTop() + 187;
    }

    int visibleAccessRows(int requestedRows)
    {
        return 0;
    }
}
