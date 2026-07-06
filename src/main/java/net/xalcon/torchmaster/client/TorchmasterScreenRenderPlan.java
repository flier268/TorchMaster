package net.xalcon.torchmaster.client;

final class TorchmasterScreenRenderPlan
{
    final TorchmasterPanelRenderer.Fill background;
    final int frameLeft;
    final int frameTop;
    final int frameRight;
    final int frameBottom;
    private final CenteredLabel[] centeredLabels;
    private final LeftLabel[] leftLabels;

    TorchmasterScreenRenderPlan(TorchmasterPanelRenderer.Fill background, int frameLeft, int frameTop, int frameRight, int frameBottom,
            CenteredLabel[] centeredLabels, LeftLabel[] leftLabels)
    {
        this.background = background;
        this.frameLeft = frameLeft;
        this.frameTop = frameTop;
        this.frameRight = frameRight;
        this.frameBottom = frameBottom;
        this.centeredLabels = centeredLabels;
        this.leftLabels = leftLabels;
    }

    CenteredLabel[] centeredLabels()
    {
        return centeredLabels;
    }

    LeftLabel[] leftLabels()
    {
        return leftLabels;
    }

    static CenteredLabel centered(CompatText text, int x, int y, int color, boolean shadow)
    {
        return new CenteredLabel(text, x, y, color, shadow);
    }

    static LeftLabel left(CompatText text, int x, int y, int color)
    {
        return new LeftLabel(text, x, y, color);
    }

    static final class CenteredLabel
    {
        final CompatText text;
        final int x;
        final int y;
        final int color;
        final boolean shadow;

        private CenteredLabel(CompatText text, int x, int y, int color, boolean shadow)
        {
            this.text = text;
            this.x = x;
            this.y = y;
            this.color = color;
            this.shadow = shadow;
        }
    }

    static final class LeftLabel
    {
        final CompatText text;
        final int x;
        final int y;
        final int color;

        private LeftLabel(CompatText text, int x, int y, int color)
        {
            this.text = text;
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }
}
