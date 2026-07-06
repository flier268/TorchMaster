package net.xalcon.torchmaster.client;

import java.util.ArrayList;
import java.util.List;

final class TorchmasterConfigScreenPresenter
{
    private static final String TITLE_KEY = "screen.torchmaster.config.title";

    private TorchmasterConfigScreenPresenter()
    {
    }

    static TorchmasterScreenRenderPlan plan(TorchmasterConfigScreenLayout layout, List<TorchmasterConfigWidgetRows.Row> rows,
            CompatText status, int statusColor)
    {
        int left = layout.panelLeft();
        int right = layout.panelRight();
        int top = layout.panelTop();
        int bottom = layout.panelBottom();
        List<TorchmasterScreenRenderPlan.LeftLabel> labels = new ArrayList<>();
        for (TorchmasterConfigWidgetRows.Row row : rows) {
            if (row.visible()) {
                labels.add(TorchmasterScreenRenderPlan.left(
                        CompatText.translatable(row.translationKey()),
                        left + 12,
                        layout.compact() ? row.y() : row.y() + 6,
                        TorchmasterPanelRenderer.LABEL_COLOR));
            }
        }
        return new TorchmasterScreenRenderPlan(
                TorchmasterPanelRenderer.background(left, top, right, bottom),
                left,
                top,
                right,
                bottom,
                new TorchmasterScreenRenderPlan.CenteredLabel[] {
                        TorchmasterScreenRenderPlan.centered(CompatText.translatable(TITLE_KEY), layout.screenWidth() / 2, 14, TorchmasterPanelRenderer.TITLE_COLOR, true),
                        TorchmasterScreenRenderPlan.centered(status, layout.screenWidth() / 2, layout.screenHeight() - 48, statusColor, true)
                },
                labels.toArray(new TorchmasterScreenRenderPlan.LeftLabel[labels.size()]));
    }
}
