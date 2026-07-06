package net.xalcon.torchmaster.client;

final class TorchmasterLightScreenPresenter
{
    private TorchmasterLightScreenPresenter()
    {
    }

    static TorchmasterScreenRenderPlan plan(TorchmasterLightScreenLayout layout, TorchmasterLightScreenModel model)
    {
        return plan(layout, model.blockKey(), model.radius());
    }

    static TorchmasterScreenRenderPlan plan(TorchmasterLightScreenLayout layout, String blockKey, int radius)
    {
        int panelLeft = layout.panelLeft();
        int panelTop = layout.panelTop();
        int panelRight = layout.panelRight();
        int panelBottom = layout.panelBottom();
        int centerX = layout.centerX();
        return new TorchmasterScreenRenderPlan(
                TorchmasterPanelRenderer.background(panelLeft, panelTop, panelRight, panelBottom),
                panelLeft,
                panelTop,
                panelRight,
                panelBottom,
                new TorchmasterScreenRenderPlan.CenteredLabel[] {
                        TorchmasterScreenRenderPlan.centered(CompatText.translatable(TorchmasterLightScreenModel.TITLE_KEY), centerX, panelTop + 10, TorchmasterPanelRenderer.TITLE_COLOR, true),
                        TorchmasterScreenRenderPlan.centered(CompatText.translatable(blockKey), centerX, panelTop + 28, TorchmasterPanelRenderer.LABEL_COLOR, true),
                        TorchmasterScreenRenderPlan.centered(CompatText.translatable(TorchmasterLightScreenModel.RANGE_KEY, radius), centerX, panelTop + 42, TorchmasterPanelRenderer.RANGE_COLOR, true)
                },
                new TorchmasterScreenRenderPlan.LeftLabel[0]);
    }
}
