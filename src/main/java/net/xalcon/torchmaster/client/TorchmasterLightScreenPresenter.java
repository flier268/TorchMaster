package net.xalcon.torchmaster.client;

import net.minecraft.util.math.BlockPos;

final class TorchmasterLightScreenPresenter
{
    private TorchmasterLightScreenPresenter()
    {
    }

    static TorchmasterScreenRenderPlan plan(TorchmasterLightScreenLayout layout, TorchmasterLightScreenModel model, BlockPos pos)
    {
        return plan(layout, model.blockKey(), model.radius(), pos);
    }

    static TorchmasterScreenRenderPlan plan(TorchmasterLightScreenLayout layout, String blockKey, int radius, BlockPos pos)
    {
        int panelLeft = layout.panelLeft();
        int panelTop = layout.panelTop();
        int panelRight = layout.panelRight();
        int panelBottom = layout.panelBottom();
        int centerX = layout.centerX();
        int swatchLeft = layout.rangeSwatchLeft();
        int swatchTop = layout.rangeSwatchTop();
        int swatchSize = layout.rangeSwatchSize();
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
                new TorchmasterScreenRenderPlan.LeftLabel[0],
                new TorchmasterPanelRenderer.Fill[] {
                        TorchmasterPanelRenderer.fill(swatchLeft - 1, swatchTop - 1, swatchLeft + swatchSize + 1,
                                swatchTop + swatchSize + 1, TorchmasterPanelRenderer.FRAME_DARK_COLOR),
                        TorchmasterPanelRenderer.fill(swatchLeft, swatchTop, swatchLeft + swatchSize,
                                swatchTop + swatchSize, TorchmasterLineBoxRenderer.rangeColor(pos))
                });
    }
}
