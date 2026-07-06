package net.xalcon.torchmaster.client;

import net.minecraft.util.math.BlockPos;
import net.xalcon.torchmaster.port.LightSettingsView;

import java.util.ArrayList;
import java.util.List;

final class TorchmasterLightScreenPresenter
{
    private TorchmasterLightScreenPresenter()
    {
    }

    static TorchmasterScreenRenderPlan plan(TorchmasterLightScreenLayout layout, TorchmasterLightScreenModel model, BlockPos pos)
    {
        return plan(layout, model.blockKey(), model.radius(), pos, null);
    }

    static TorchmasterScreenRenderPlan plan(TorchmasterLightScreenLayout layout, String blockKey, int radius, BlockPos pos)
    {
        return plan(layout, blockKey, radius, pos, null);
    }

    static TorchmasterScreenRenderPlan plan(TorchmasterLightScreenLayout layout, String blockKey, int radius, BlockPos pos, LightSettingsView settings)
    {
        int panelLeft = layout.panelLeft();
        int panelTop = layout.panelTop();
        int panelRight = layout.panelRight();
        int panelBottom = layout.panelBottom();
        int centerX = layout.centerX();
        TorchmasterScreenRenderPlan.LeftLabel[] leftLabels = settings == null ? new TorchmasterScreenRenderPlan.LeftLabel[0] : labels(layout, panelLeft, settings);
        return new TorchmasterScreenRenderPlan(
                TorchmasterPanelRenderer.background(panelLeft, panelTop, panelRight, panelBottom),
                panelLeft,
                panelTop,
                panelRight,
                panelBottom,
                new TorchmasterScreenRenderPlan.CenteredLabel[] {
                        TorchmasterScreenRenderPlan.centered(CompatText.translatable(TorchmasterLightScreenModel.TITLE_KEY), centerX, layout.titleY(), TorchmasterPanelRenderer.TITLE_COLOR, true),
                        TorchmasterScreenRenderPlan.centered(CompatText.translatable(blockKey), centerX, layout.blockY(), TorchmasterPanelRenderer.LABEL_COLOR, true),
                        TorchmasterScreenRenderPlan.centered(rangeLabel(radius, settings), centerX, layout.rangeY(), TorchmasterPanelRenderer.RANGE_COLOR, true)
                },
                leftLabels,
                fills(layout, pos, settings != null));
    }

    private static TorchmasterPanelRenderer.Fill[] fills(TorchmasterLightScreenLayout layout, BlockPos pos, boolean fullPanel)
    {
        List<TorchmasterPanelRenderer.Fill> fills = new ArrayList<>();
        if (fullPanel) {
            int contentLeft = layout.panelLeft() + 26;
            int contentRight = layout.panelRight() - 26;
            fills.add(TorchmasterPanelRenderer.fill(contentLeft, layout.visibilityY() - 8, contentRight,
                    layout.visibilityY() - 7, TorchmasterPanelRenderer.FRAME_DARK_COLOR));
            fills.add(TorchmasterPanelRenderer.fill(contentLeft, layout.enabledY() - 10, contentRight,
                    layout.enabledY() - 9, TorchmasterPanelRenderer.FRAME_LIGHT_COLOR));
            fills.add(TorchmasterPanelRenderer.fill(contentLeft, layout.radiusLabelY() - 10, contentRight,
                    layout.radiusLabelY() - 9, TorchmasterPanelRenderer.FRAME_DARK_COLOR));
            fills.add(TorchmasterPanelRenderer.fill(contentLeft, layout.accessButtonY() - 10, contentRight,
                    layout.accessButtonY() - 9, TorchmasterPanelRenderer.FRAME_DARK_COLOR));
        }

        int swatchLeft = layout.rangeSwatchLeft();
        int swatchTop = layout.rangeSwatchTop();
        int swatchSize = layout.rangeSwatchSize();
        fills.add(TorchmasterPanelRenderer.fill(swatchLeft - 1, swatchTop - 1, swatchLeft + swatchSize + 1,
                swatchTop + swatchSize + 1, TorchmasterPanelRenderer.FRAME_DARK_COLOR));
        fills.add(TorchmasterPanelRenderer.fill(swatchLeft, swatchTop, swatchLeft + swatchSize,
                swatchTop + swatchSize, TorchmasterLineBoxRenderer.rangeColor(pos)));
        return fills.toArray(new TorchmasterPanelRenderer.Fill[0]);
    }

    private static TorchmasterScreenRenderPlan.LeftLabel[] labels(TorchmasterLightScreenLayout layout, int panelLeft, LightSettingsView settings)
    {
        List<TorchmasterScreenRenderPlan.LeftLabel> labels = new ArrayList<>();
        labels.add(TorchmasterScreenRenderPlan.left(CompatText.translatable("screen.torchmaster.light.enabled"), panelLeft + 26, layout.enabledY() + 6, TorchmasterPanelRenderer.LABEL_COLOR));
        labels.add(TorchmasterScreenRenderPlan.left(radiusUnitLabel(settings), panelLeft + 26, layout.radiusLabelY(), TorchmasterPanelRenderer.LABEL_COLOR));
        labels.add(TorchmasterScreenRenderPlan.left(CompatText.literal("X"), layout.radiusFieldX(0), layout.radiusAxisLabelY(), TorchmasterPanelRenderer.UNIT_COLOR));
        if (!settings.chunkAligned()) {
            labels.add(TorchmasterScreenRenderPlan.left(CompatText.literal("Y"), layout.radiusFieldX(1), layout.radiusAxisLabelY(), TorchmasterPanelRenderer.UNIT_COLOR));
        }
        labels.add(TorchmasterScreenRenderPlan.left(CompatText.literal("Z"), layout.radiusFieldX(settings.chunkAligned() ? 1 : 2), layout.radiusAxisLabelY(), TorchmasterPanelRenderer.UNIT_COLOR));
        labels.add(TorchmasterScreenRenderPlan.left(maxRadiusLabel(settings), panelLeft + 26, layout.limitY(), TorchmasterPanelRenderer.UNIT_COLOR));
        labels.add(TorchmasterScreenRenderPlan.left(CompatText.literal(settings.editable() ? "🔓" : "🔒"),
                layout.lockX(), layout.editableY(), settings.editable() ? TorchmasterPanelRenderer.RANGE_COLOR : TorchmasterPanelRenderer.UNIT_COLOR));
        return labels.toArray(new TorchmasterScreenRenderPlan.LeftLabel[0]);
    }

    private static CompatText rangeLabel(int radius, LightSettingsView settings)
    {
        if (settings != null && settings.chunkAligned()) {
            return CompatText.translatable("screen.torchmaster.light.rangeChunks", chunkRadius(radius));
        }
        return CompatText.translatable(TorchmasterLightScreenModel.RANGE_KEY, radius);
    }

    private static CompatText radiusUnitLabel(LightSettingsView settings)
    {
        return CompatText.translatable(settings.chunkAligned() ? "screen.torchmaster.light.radiusChunkUnit" : "screen.torchmaster.light.radiusBlockUnit");
    }

    private static CompatText maxRadiusLabel(LightSettingsView settings)
    {
        if (settings.chunkAligned()) {
            return CompatText.translatable("screen.torchmaster.light.maxRadiusChunks", chunkRadius(settings.globalMax()));
        }
        return CompatText.translatable("screen.torchmaster.light.maxRadius", settings.globalMax());
    }

    private static int chunkRadius(int radius)
    {
        return (Math.max(0, radius) + 15) >> 4;
    }
}
