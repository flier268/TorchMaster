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
        TorchmasterScreenRenderPlan.LeftLabel[] leftLabels = labels(layout, panelLeft, blockKey, settings);
        TorchmasterScreenRenderPlan.CenteredLabel[] centeredLabels = centeredLabels(layout, settings);
        return new TorchmasterScreenRenderPlan(
                TorchmasterPanelRenderer.background(panelLeft, panelTop, panelRight, panelBottom),
                panelLeft,
                panelTop,
                panelRight,
                panelBottom,
                centeredLabels,
                leftLabels,
                fills(layout, pos, settings));
    }

    private static TorchmasterPanelRenderer.Fill[] fills(TorchmasterLightScreenLayout layout, BlockPos pos, LightSettingsView settings)
    {
        List<TorchmasterPanelRenderer.Fill> fills = new ArrayList<>();
        if (settings != null) {
            int contentLeft = layout.panelLeft() + 26;
            int contentRight = layout.panelRight() - 26;
            fills.add(TorchmasterPanelRenderer.fill(contentLeft, layout.contentViewportTop() - 8, contentRight,
                    layout.contentViewportTop() - 7, TorchmasterPanelRenderer.FRAME_DARK_COLOR));
            fills.add(TorchmasterPanelRenderer.fill(contentLeft, layout.footerY() - 8, contentRight,
                    layout.footerY() - 7, TorchmasterPanelRenderer.FRAME_DARK_COLOR));
            addScrollbar(layout, fills);
            addSwitch(fills, TorchmasterSwitchControl.fills(layout.enabledSwitchX(), layout.enabledSwitchY(), settings.enabled(), settings.editable()));
            addSwitch(fills, TorchmasterSwitchControl.fills(layout.visibilitySwitchX(), layout.visibilityY(), settings.rangeVisible(), true));
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

    private static void addSwitch(List<TorchmasterPanelRenderer.Fill> fills, TorchmasterPanelRenderer.Fill[] switchFills)
    {
        for (TorchmasterPanelRenderer.Fill fill : switchFills) {
            fills.add(fill);
        }
    }

    private static void addScrollbar(TorchmasterLightScreenLayout layout, List<TorchmasterPanelRenderer.Fill> fills)
    {
        int maxScroll = layout.maxScroll();
        if (maxScroll <= 0) {
            return;
        }
        int trackLeft = layout.panelRight() - 10;
        int trackTop = layout.scrollbarTop();
        int trackBottom = layout.scrollbarBottom();
        int trackHeight = Math.max(1, trackBottom - trackTop);
        int contentHeight = trackHeight + maxScroll;
        int thumbHeight = Math.max(12, trackHeight * trackHeight / Math.max(trackHeight, contentHeight));
        int thumbTop = trackTop + (trackHeight - thumbHeight) * layout.scrollOffset() / maxScroll;
        fills.add(TorchmasterPanelRenderer.fill(trackLeft, trackTop, trackLeft + 3, trackBottom, TorchmasterPanelRenderer.FRAME_DARK_COLOR));
        fills.add(TorchmasterPanelRenderer.fill(trackLeft, thumbTop, trackLeft + 3, thumbTop + thumbHeight, TorchmasterPanelRenderer.RANGE_COLOR));
    }

    private static TorchmasterScreenRenderPlan.LeftLabel[] labels(TorchmasterLightScreenLayout layout, int panelLeft, String blockKey,
            LightSettingsView settings)
    {
        List<TorchmasterScreenRenderPlan.LeftLabel> labels = new ArrayList<>();
        labels.add(TorchmasterScreenRenderPlan.left(CompatText.translatable(TorchmasterLightScreenModel.TITLE_KEY), panelLeft + 26, layout.titleY(),
                TorchmasterPanelRenderer.TITLE_COLOR));
        labels.add(TorchmasterScreenRenderPlan.left(CompatText.translatable(blockKey), panelLeft + 26, layout.blockY(),
                TorchmasterPanelRenderer.LABEL_COLOR));
        if (settings == null) {
            return labels.toArray(new TorchmasterScreenRenderPlan.LeftLabel[0]);
        }
        labels.add(TorchmasterScreenRenderPlan.left(CompatText.translatable("screen.torchmaster.light.enabled"), layout.enabledLabelX(),
                layout.enabledY() + 4, TorchmasterPanelRenderer.LABEL_COLOR));
        labels.add(TorchmasterScreenRenderPlan.left(radiusUnitLabel(settings), panelLeft + 26, layout.radiusLabelY(), TorchmasterPanelRenderer.LABEL_COLOR));
        labels.add(TorchmasterScreenRenderPlan.left(CompatText.translatable("screen.torchmaster.light.showRange"),
                layout.visibilityLabelX(), layout.radiusLabelY(), TorchmasterPanelRenderer.UNIT_COLOR));
        addRangeLabel(labels, layout, 0, "screen.torchmaster.light.rangeWest");
        addRangeLabel(labels, layout, 1, "screen.torchmaster.light.rangeEast");
        if (!settings.chunkAligned()) {
            addRangeLabel(labels, layout, 2, "screen.torchmaster.light.rangeDown");
            addRangeLabel(labels, layout, 3, "screen.torchmaster.light.rangeUp");
        }
        addRangeLabel(labels, layout, settings.chunkAligned() ? 2 : 4, "screen.torchmaster.light.rangeNorth");
        addRangeLabel(labels, layout, settings.chunkAligned() ? 3 : 5, "screen.torchmaster.light.rangeSouth");
        labels.add(TorchmasterScreenRenderPlan.left(maxRadiusLabel(settings), panelLeft + 26, layout.limitY(), TorchmasterPanelRenderer.UNIT_COLOR));
        labels.add(TorchmasterScreenRenderPlan.left(CompatText.literal(settings.editable() ? "🔓" : "🔒"),
                layout.lockX(), layout.editableY(), settings.editable() ? TorchmasterPanelRenderer.RANGE_COLOR : TorchmasterPanelRenderer.UNIT_COLOR));
        return labels.toArray(new TorchmasterScreenRenderPlan.LeftLabel[0]);
    }

    private static TorchmasterScreenRenderPlan.CenteredLabel[] centeredLabels(TorchmasterLightScreenLayout layout, LightSettingsView settings)
    {
        if (settings == null) {
            return new TorchmasterScreenRenderPlan.CenteredLabel[0];
        }
        List<TorchmasterScreenRenderPlan.CenteredLabel> labels = new ArrayList<>();
        addRangeValue(labels, layout, 0, settings.rangeWest(), settings.chunkAligned());
        addRangeValue(labels, layout, 1, settings.rangeEast(), settings.chunkAligned());
        if (!settings.chunkAligned()) {
            addRangeValue(labels, layout, 2, settings.rangeDown(), false);
            addRangeValue(labels, layout, 3, settings.rangeUp(), false);
        }
        addRangeValue(labels, layout, settings.chunkAligned() ? 2 : 4, settings.rangeNorth(), settings.chunkAligned());
        addRangeValue(labels, layout, settings.chunkAligned() ? 3 : 5, settings.rangeSouth(), settings.chunkAligned());
        return labels.toArray(new TorchmasterScreenRenderPlan.CenteredLabel[0]);
    }

    private static void addRangeLabel(List<TorchmasterScreenRenderPlan.LeftLabel> labels, TorchmasterLightScreenLayout layout, int slot,
            String directionKey)
    {
        int column = slot % 2;
        int row = slot / 2;
        int y = layout.radiusFieldY(row) + 6;
        labels.add(TorchmasterScreenRenderPlan.left(CompatText.translatable(directionKey), layout.radiusDirectionLabelX(column), y,
                TorchmasterPanelRenderer.UNIT_COLOR));
    }

    private static void addRangeValue(List<TorchmasterScreenRenderPlan.CenteredLabel> labels, TorchmasterLightScreenLayout layout, int slot,
            int blockRange, boolean chunkAligned)
    {
        int column = slot % 2;
        int row = slot / 2;
        labels.add(TorchmasterScreenRenderPlan.centered(CompatText.literal(Integer.toString(displayRange(blockRange, chunkAligned))),
                layout.rangeValueCenterX(column), layout.radiusFieldY(row) + 6, TorchmasterPanelRenderer.LABEL_COLOR, false));
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

    private static int displayRange(int radius, boolean chunkAligned)
    {
        return chunkAligned ? chunkRadius(radius) : Math.max(0, radius);
    }
}
