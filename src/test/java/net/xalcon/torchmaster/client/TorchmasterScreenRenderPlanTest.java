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
        assertLeft(plan.leftLabels()[0], "screen.torchmaster.light.title", 276, 251, TorchmasterPanelRenderer.TITLE_COLOR);
        assertLeft(plan.leftLabels()[1], "block.torchmaster.megatorch", 276, 265, TorchmasterPanelRenderer.LABEL_COLOR);
        assertEquals(0, plan.centeredLabels().length);
        assertEquals(2, plan.leftLabels().length);
        assertEquals(2, plan.fills().length);
        assertFill(plan.fills()[0], layout.rangeSwatchLeft() - 1, layout.rangeSwatchTop() - 1,
                layout.rangeSwatchLeft() + layout.rangeSwatchSize() + 1, layout.rangeSwatchTop() + layout.rangeSwatchSize() + 1,
                TorchmasterPanelRenderer.FRAME_DARK_COLOR);
        assertFill(plan.fills()[1], layout.rangeSwatchLeft(), layout.rangeSwatchTop(), layout.rangeSwatchLeft() + layout.rangeSwatchSize(),
                layout.rangeSwatchTop() + layout.rangeSwatchSize(), TorchmasterLineBoxRenderer.rangeColor(pos));
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
        TorchmasterLightScreenLayout layout = new TorchmasterLightScreenLayout(800, 600, 300, 300);
        LightSettingsView settings = LightSettingsView.present(true, true, true, 16, 16, 12, 12, 8, 8, 64, false, false, new LightAccessEntry[] {
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
        TorchmasterLightScreenLayout layout = new TorchmasterLightScreenLayout(800, 600, 300, 300);
        LightSettingsView settings = LightSettingsView.present(true, true, true, 64, 64, 64, 64, 64, 64, 64, true, false, new LightAccessEntry[0]);

        TorchmasterScreenRenderPlan plan = TorchmasterLightScreenPresenter.plan(layout, "block.torchmaster.megatorch", 64,
                new BlockPos(10, 20, 30), settings);

        assertTrue(Arrays.stream(plan.leftLabels()).anyMatch(label -> "screen.torchmaster.light.maxRadiusChunks".equals(label.text.translationKey())));
        assertTrue(Arrays.stream(plan.leftLabels()).anyMatch(label -> "screen.torchmaster.light.radiusChunkUnit".equals(label.text.translationKey())));
        assertTrue(Arrays.stream(plan.leftLabels()).anyMatch(label -> "screen.torchmaster.light.rangeWest".equals(label.text.translationKey()) && label.x == layout.radiusDirectionLabelX(0)));
        assertTrue(Arrays.stream(plan.leftLabels()).anyMatch(label -> "screen.torchmaster.light.rangeEast".equals(label.text.translationKey()) && label.x == layout.radiusDirectionLabelX(1)));
        assertTrue(Arrays.stream(plan.leftLabels()).anyMatch(label -> "screen.torchmaster.light.rangeNorth".equals(label.text.translationKey()) && label.x == layout.radiusDirectionLabelX(0)));
        assertTrue(Arrays.stream(plan.leftLabels()).anyMatch(label -> "screen.torchmaster.light.rangeSouth".equals(label.text.translationKey()) && label.x == layout.radiusDirectionLabelX(1)));
        assertTrue(Arrays.stream(plan.centeredLabels()).anyMatch(label -> "4".equals(label.text.literalValue()) && label.x == layout.rangeValueCenterX(0)));
        assertTrue(Arrays.stream(plan.centeredLabels()).anyMatch(label -> "4".equals(label.text.literalValue()) && label.x == layout.rangeValueCenterX(1)));
        assertFalse(Arrays.stream(plan.leftLabels()).anyMatch(label -> "screen.torchmaster.light.rangeDown".equals(label.text.translationKey())));
        assertFalse(Arrays.stream(plan.leftLabels()).anyMatch(label -> "screen.torchmaster.light.rangeUp".equals(label.text.translationKey())));
        TorchmasterScreenRenderPlan.LeftLabel maxRadius = Arrays.stream(plan.leftLabels())
                .filter(label -> "screen.torchmaster.light.maxRadiusChunks".equals(label.text.translationKey()))
                .findFirst()
                .orElseThrow(AssertionError::new);
        assertEquals(1, maxRadius.text.args().length);
        assertEquals(4, maxRadius.text.args()[0]);
    }

    @Test
    void lightLayoutKeepsSwatchOutsideSwitches()
    {
        TorchmasterLightScreenLayout layout = new TorchmasterLightScreenLayout(800, 600, 300, 300);
        TorchmasterPanelRenderer.Fill[] fills = TorchmasterLightScreenPresenter.plan(layout, "block.torchmaster.megatorch", 64,
                new BlockPos(10, 20, 30), LightSettingsView.present(true, true, true, 64, 64, 64, 64, 64, 64, 64, false, false,
                        new LightAccessEntry[0])).fills();
        TorchmasterPanelRenderer.Fill swatchFrame = fills[fills.length - 2];

        assertFalse(intersects(swatchFrame.left, swatchFrame.top, swatchFrame.right, swatchFrame.bottom,
                layout.enabledSwitchX(), layout.enabledSwitchY(), layout.enabledSwitchX() + layout.switchWidth(),
                layout.enabledSwitchY() + layout.switchHeight()));
        assertTrue(layout.radiusFieldX(0) + 64 <= layout.radiusFieldX(1));
        assertEquals(layout.radiusLabelY() - 4, layout.visibilityY());
        assertTrue(layout.visibilityLabelX() > layout.panelLeft() + 26);
        assertTrue(layout.visibilityLabelX() < layout.rangeSwatchLeft());
        assertEquals(layout.visibilitySwitchX() - layout.rangeSwatchSize() - 8, layout.rangeSwatchLeft());
        assertTrue(layout.rangeSwatchLeft() + layout.rangeSwatchSize() < layout.visibilitySwitchX());
        assertTrue(layout.footerButtonX(0) + layout.footerButtonWidth() <= layout.footerButtonX(1));
        assertTrue(layout.footerButtonX(1) + layout.footerButtonWidth() <= layout.footerButtonX(2));
        assertTrue(layout.footerButtonX(2) + layout.footerButtonWidth() <= layout.panelRight() - 26);
        assertEquals(77, layout.footerButtonWidth());
    }

    @Test
    void switchControlRendersTrackThumbAndHitBox()
    {
        TorchmasterPanelRenderer.Fill[] on = TorchmasterSwitchControl.fills(10, 20, true, true);
        TorchmasterPanelRenderer.Fill[] off = TorchmasterSwitchControl.fills(10, 20, false, true);

        assertEquals(4, on.length);
        assertFill(on[0], 14, 20, 42, 34, 0xFF2D9CDB);
        assertFill(on[2], 36, 22, 42, 32, 0xFFFFFFFF);
        assertFill(off[0], 14, 20, 42, 34, 0xFF606060);
        assertFill(off[2], 14, 22, 20, 32, 0xFFFFFFFF);
        assertTrue(TorchmasterSwitchControl.contains(10, 20, 10, 20));
        assertTrue(TorchmasterSwitchControl.contains(10, 20, 45, 33));
        assertFalse(TorchmasterSwitchControl.contains(10, 20, 46, 33));
    }

    @Test
    void compactLightLayoutScrollsOnlyWhenViewportIsTooShort()
    {
        TorchmasterLightScreenLayout normal = new TorchmasterLightScreenLayout(800, 600, 340, 246);
        TorchmasterLightScreenLayout shortViewport = new TorchmasterLightScreenLayout(800, 160, 340, 246);
        TorchmasterLightScreenLayout scrolled = new TorchmasterLightScreenLayout(800, 160, 340, 246, 18);

        assertEquals(0, normal.maxScroll());
        assertTrue(shortViewport.maxScroll() > 0);
        assertEquals(shortViewport.radiusFieldY(0) - 18, scrolled.radiusFieldY(0));
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
