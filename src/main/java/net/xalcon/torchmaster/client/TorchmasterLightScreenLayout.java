package net.xalcon.torchmaster.client;

final class TorchmasterLightScreenLayout
{
    private final int width;
    private final int height;
    private final int panelWidth;
    private final int panelHeight;

    TorchmasterLightScreenLayout(int width, int height, int panelWidth, int panelHeight)
    {
        this.width = width;
        this.height = height;
        this.panelWidth = Math.min(panelWidth, Math.max(160, width - 16));
        this.panelHeight = Math.min(panelHeight, Math.max(120, height - 8));
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
        return panelRight() - 32;
    }

    int rangeSwatchTop()
    {
        return rangeY() - 3;
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
        return panelTop() + 12;
    }

    int blockY()
    {
        return panelTop() + 29;
    }

    int rangeY()
    {
        return panelTop() + 44;
    }

    int visibilityY()
    {
        return panelTop() + 64;
    }

    int enabledY()
    {
        return panelTop() + 96;
    }

    int enabledButtonX()
    {
        return panelRight() - 130;
    }

    int enabledButtonWidth()
    {
        return 104;
    }

    int radiusLabelY()
    {
        return panelTop() + 124;
    }

    int radiusAxisLabelY()
    {
        return panelTop() + 141;
    }

    int radiusFieldY()
    {
        return panelTop() + 153;
    }

    int radiusFieldX(int index)
    {
        return panelLeft() + 26 + index * radiusSlotWidth();
    }

    int limitY()
    {
        return panelTop() + 180;
    }

    int editableY()
    {
        return panelTop() + 180;
    }

    int lockX()
    {
        return panelRight() - 40;
    }

    int accessButtonY()
    {
        return footerY() - 26;
    }

    int accessButtonX()
    {
        return panelLeft() + 26;
    }

    int accessButtonWidth()
    {
        return Math.max(104, panelWidth - 52);
    }

    int footerButtonWidth()
    {
        return Math.max(40, (panelWidth - 68) / 3);
    }

    int footerButtonX(int index)
    {
        return panelLeft() + 26 + index * (footerButtonWidth() + 8);
    }

    int footerY()
    {
        return panelBottom() - 28;
    }

    private int radiusSlotWidth()
    {
        return Math.max(76, (panelWidth - 52) / 3);
    }

    int visibleAccessRows(int requestedRows)
    {
        return 0;
    }
}
