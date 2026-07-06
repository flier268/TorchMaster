package net.xalcon.torchmaster.client;

import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        assertCentered(plan.centeredLabels()[0], "screen.torchmaster.light.title", 400, 251, TorchmasterPanelRenderer.TITLE_COLOR);
        assertCentered(plan.centeredLabels()[1], "block.torchmaster.megatorch", 400, 269, TorchmasterPanelRenderer.LABEL_COLOR);
        assertCentered(plan.centeredLabels()[2], "screen.torchmaster.light.range", 400, 283, TorchmasterPanelRenderer.RANGE_COLOR);
        assertEquals(64, plan.centeredLabels()[2].text.args()[0]);
        assertEquals(0, plan.leftLabels().length);
        assertEquals(2, plan.fills().length);
        assertFill(plan.fills()[0], 481, 297, 497, 313, TorchmasterPanelRenderer.FRAME_DARK_COLOR);
        assertFill(plan.fills()[1], 482, 298, 496, 312, TorchmasterLineBoxRenderer.rangeColor(pos));
    }

    @Test
    void configPresenterKeepsVisibleEntryLabelsAndStatus()
    {
        TorchmasterConfigScreenLayout layout = new TorchmasterConfigScreenLayout(800, 240);
        TestRow hidden = new TestRow("hidden.key", 48, 48, false);
        TestRow visible = new TestRow("visible.key", "visible.unit", 80, 80, true);

        TorchmasterScreenRenderPlan plan = TorchmasterConfigScreenPresenter.plan(layout, Arrays.asList(hidden, visible),
                CompatText.translatable("status.key"), 0xFF55FF55);

        assertCentered(plan.centeredLabels()[0], "screen.torchmaster.config.title", 400, 14, TorchmasterPanelRenderer.TITLE_COLOR);
        assertCentered(plan.centeredLabels()[1], "status.key", 400, 192, 0xFF55FF55);
        assertEquals(2, plan.leftLabels().length);
        assertLeft(plan.leftLabels()[0], "visible.key", layout.panelLeft() + 12, 86, TorchmasterPanelRenderer.LABEL_COLOR);
        assertLeft(plan.leftLabels()[1], "visible.unit", layout.fieldX(), 70, TorchmasterPanelRenderer.UNIT_COLOR);
        assertEquals(0, plan.fills().length);
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
        TorchmasterConfigEntries.ReadResult read(TorchmasterConfigEntries.Collector collector)
        {
            return TorchmasterConfigEntries.ReadResult.success();
        }
    }

}
