package net.xalcon.torchmaster.client;

import net.minecraft.util.math.BlockPos;
import net.xalcon.torchmaster.port.LightAccessEntry;
import net.xalcon.torchmaster.port.LightSettingsView;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterScreenRenderPlanTest
{
    @Test
    void lightPresenterKeepsTitleBlockAndRangeLabels()
    {
        TorchmasterLightScreenLayout layout = new TorchmasterLightScreenLayout(800, 600, 300, 118);
        BlockPos pos = new BlockPos(10, 20, 30);

        TorchmasterScreenRenderPlan plan = TorchmasterLightScreenPresenter.plan(layout, "block.torchmaster.megatorch", 64, pos);

        assertEquals(250, plan.frameLeft);
        assertEquals(241, plan.frameTop);
        assertEquals(550, plan.frameRight);
        assertEquals(359, plan.frameBottom);
        assertEquals(TorchmasterPanelRenderer.BACKGROUND_COLOR, plan.background.color);
        assertCentered(plan.centeredLabels()[0], "screen.torchmaster.light.title", 400, 253, TorchmasterPanelRenderer.TITLE_COLOR);
        assertCentered(plan.centeredLabels()[1], "block.torchmaster.megatorch", 400, 270, TorchmasterPanelRenderer.LABEL_COLOR);
        assertCentered(plan.centeredLabels()[2], "screen.torchmaster.light.range", 400, 285, TorchmasterPanelRenderer.RANGE_COLOR);
        assertEquals(64, plan.centeredLabels()[2].text.args()[0]);
        assertEquals(0, plan.leftLabels().length);
        assertEquals(2, plan.fills().length);
        assertFill(plan.fills()[0], 517, 281, 531, 295, TorchmasterPanelRenderer.FRAME_DARK_COLOR);
        assertFill(plan.fills()[1], 518, 282, 530, 294, TorchmasterLineBoxRenderer.rangeColor(pos));
    }

    @Test
    void configPresenterKeepsVisibleEntryLabelsAndStatus()
    {
        TorchmasterConfigScreenLayout layout = new TorchmasterConfigScreenLayout(800, 240);
        TestRow hidden = new TestRow("hidden.key", 48, 48, false);
        TestRow visible = new TestRow("visible.key", "visible.unit", 80, 80, true);

        TorchmasterScreenRenderPlan plan = TorchmasterConfigScreenPresenter.plan(layout, Arrays.asList(hidden, visible),
                CompatText.translatable("status.key"), 0xFF55FF55);

        assertCentered(plan.centeredLabels()[0], "screen.torchmaster.config.globalTitle", 400, 14, TorchmasterPanelRenderer.TITLE_COLOR);
        assertCentered(plan.centeredLabels()[1], "status.key", 400, 192, 0xFF55FF55);
        assertEquals(1, plan.leftLabels().length);
        assertLeft(plan.leftLabels()[0], "screen.torchmaster.config.labelWithUnit", layout.panelLeft() + 12, 86, TorchmasterPanelRenderer.LABEL_COLOR);
        assertEquals("visible.key", ((CompatText)plan.leftLabels()[0].text.args()[0]).translationKey());
        assertEquals("visible.unit", ((CompatText)plan.leftLabels()[0].text.args()[1]).translationKey());
        assertEquals(0, plan.fills().length);
    }

    @Test
    void lightPresenterKeepsAccessDetailsOutOfMainPanel()
    {
        TorchmasterLightScreenLayout layout = new TorchmasterLightScreenLayout(800, 600, 300, 246);
        LightSettingsView settings = LightSettingsView.present(true, true, true, 16, 12, 8, 64, new LightAccessEntry[] {
                new LightAccessEntry(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Kuku"),
                new LightAccessEntry(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Alex"),
                new LightAccessEntry(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Steve"),
                new LightAccessEntry(UUID.fromString("00000000-0000-0000-0000-000000000004"), "Kai")
        });

        TorchmasterScreenRenderPlan plan = TorchmasterLightScreenPresenter.plan(layout, "block.torchmaster.megatorch", 64,
                new BlockPos(10, 20, 30), settings);

        assertFalse(Arrays.stream(plan.leftLabels()).anyMatch(label -> "screen.torchmaster.light.accessTitle".equals(label.text.translationKey())));
        assertFalse(Arrays.stream(plan.leftLabels()).anyMatch(label -> "screen.torchmaster.light.accessManageable".equals(label.text.translationKey())));
        assertFalse(Arrays.stream(plan.leftLabels()).anyMatch(label -> "Kuku".equals(label.text.literalValue())));
        assertFalse(Arrays.stream(plan.leftLabels()).anyMatch(label -> "Alex".equals(label.text.literalValue())));
        assertTrue(Arrays.stream(plan.leftLabels()).anyMatch(label -> "🔓".equals(label.text.literalValue())));
    }

    @Test
    void lightPresenterShowsChunkUnitForDiamondBaseMegaTorch()
    {
        TorchmasterLightScreenLayout layout = new TorchmasterLightScreenLayout(800, 600, 300, 246);
        LightSettingsView settings = LightSettingsView.present(true, true, true, 64, 64, 64, 64, true, new LightAccessEntry[0]);

        TorchmasterScreenRenderPlan plan = TorchmasterLightScreenPresenter.plan(layout, "block.torchmaster.megatorch", 64,
                new BlockPos(10, 20, 30), settings);

        assertCentered(plan.centeredLabels()[2], "screen.torchmaster.light.rangeChunks", 400, layout.rangeY(), TorchmasterPanelRenderer.RANGE_COLOR);
        assertEquals(4, plan.centeredLabels()[2].text.args()[0]);
        assertTrue(Arrays.stream(plan.leftLabels()).anyMatch(label -> "screen.torchmaster.light.maxRadiusChunks".equals(label.text.translationKey())));
        assertTrue(Arrays.stream(plan.leftLabels()).anyMatch(label -> "screen.torchmaster.light.radiusChunkUnit".equals(label.text.translationKey())));
        assertTrue(Arrays.stream(plan.leftLabels()).anyMatch(label -> "X".equals(label.text.literalValue()) && label.x == layout.radiusFieldX(0)));
        assertTrue(Arrays.stream(plan.leftLabels()).anyMatch(label -> "Z".equals(label.text.literalValue()) && label.x == layout.radiusFieldX(1)));
        assertFalse(Arrays.stream(plan.leftLabels()).anyMatch(label -> "Y".equals(label.text.literalValue())));
        TorchmasterScreenRenderPlan.LeftLabel maxRadius = Arrays.stream(plan.leftLabels())
                .filter(label -> "screen.torchmaster.light.maxRadiusChunks".equals(label.text.translationKey()))
                .findFirst()
                .orElseThrow(AssertionError::new);
        assertEquals(1, maxRadius.text.args().length);
        assertEquals(4, maxRadius.text.args()[0]);
    }

    @Test
    void lightLayoutKeepsSwatchOutsideEnabledButton()
    {
        TorchmasterLightScreenLayout layout = new TorchmasterLightScreenLayout(800, 600, 300, 246);
        TorchmasterPanelRenderer.Fill[] fills = TorchmasterLightScreenPresenter.plan(layout, "block.torchmaster.megatorch", 64,
                new BlockPos(10, 20, 30), LightSettingsView.present(true, true, true, 64, 64, 64, 64, new LightAccessEntry[0])).fills();
        TorchmasterPanelRenderer.Fill swatchFrame = fills[fills.length - 2];
        int enabledLeft = layout.enabledButtonX();
        int enabledTop = layout.enabledY();

        assertFalse(intersects(swatchFrame.left, swatchFrame.top, swatchFrame.right, swatchFrame.bottom,
                enabledLeft, enabledTop, enabledLeft + layout.enabledButtonWidth(), enabledTop + 20));
        assertTrue(layout.radiusFieldX(0) + 64 <= layout.radiusFieldX(1));
        assertTrue(layout.radiusFieldX(1) + 64 <= layout.radiusFieldX(2));
        assertFalse(intersects(layout.accessButtonX(), layout.accessButtonY(), layout.accessButtonX() + layout.accessButtonWidth(), layout.accessButtonY() + 20,
                layout.footerButtonX(0), layout.footerY(), layout.footerButtonX(2) + layout.footerButtonWidth(), layout.footerY() + 20));
        assertTrue(layout.accessButtonWidth() < layout.panelWidth() - 36);
    }

    @Test
    void chunkAlignedRadiusInputUsesChunkUnits()
    {
        assertEquals(4, TorchmasterLightScreen.displayRadius(64, true));
        assertEquals(64, TorchmasterLightScreen.readRadiusValue("4", 16, 128, true));
        assertEquals(0, TorchmasterLightScreen.readRadiusValue("0", 16, 128, true));
        assertEquals(128, TorchmasterLightScreen.readRadiusValue("99", 16, 128, true));
        assertEquals(32, TorchmasterLightScreen.readRadiusValue("32", 16, 128, false));
    }

    private static boolean intersects(int left, int top, int right, int bottom, int otherLeft, int otherTop, int otherRight, int otherBottom)
    {
        return left < otherRight && right > otherLeft && top < otherBottom && bottom > otherTop;
    }

    private static void assertCentered(TorchmasterScreenRenderPlan.CenteredLabel label, String key, int x, int y, int color)
    {
        assertEquals(key, label.text.translationKey());
        assertEquals(x, label.x);
        assertEquals(y, label.y);
        assertEquals(color, label.color);
        assertTrue(label.shadow);
    }

    private static void assertLeft(TorchmasterScreenRenderPlan.LeftLabel label, String key, int x, int y, int color)
    {
        assertEquals(key, label.text.translationKey());
        assertEquals(x, label.x);
        assertEquals(y, label.y);
        assertEquals(color, label.color);
    }

    private static void assertFill(TorchmasterPanelRenderer.Fill fill, int left, int top, int right, int bottom, int color)
    {
        assertEquals(left, fill.left);
        assertEquals(top, fill.top);
        assertEquals(right, fill.right);
        assertEquals(bottom, fill.bottom);
        assertEquals(color, fill.color);
    }

    private static final class TestRow extends TorchmasterConfigWidgetRows.Row
    {
        private TestRow(String translationKey, int baseY, int y, boolean visible)
        {
            this(translationKey, "", baseY, y, visible);
        }

        private TestRow(String translationKey, String unitKey, int baseY, int y, boolean visible)
        {
            super(TorchmasterConfigEntries.EntryDefinition.integer(translationKey, 0, unitKey), baseY);
            this.y = y;
            this.visible = visible;
        }

        @Override
        void setPosition(TorchmasterConfigScreenLayout layout, int fieldX, int y)
        {
        }

        @Override
        void setVisible(boolean visible)
        {
            this.visible = visible;
        }

        @Override
        void setEditable(boolean editable)
        {
        }

        @Override
        TorchmasterConfigEntries.ReadResult read(TorchmasterConfigEntries.Collector collector)
        {
            return TorchmasterConfigEntries.ReadResult.success();
        }
    }

}
