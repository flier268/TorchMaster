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
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
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
        return Math.max(20, (height - panelHeight) / 2);
    }

    int panelRight()
    {
        return panelLeft() + panelWidth;
    }

    int panelBottom()
    {
        return panelTop() + panelHeight;
    }
}
