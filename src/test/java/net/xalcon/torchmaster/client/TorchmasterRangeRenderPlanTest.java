package net.xalcon.torchmaster.client;

import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterRangeRenderPlanTest
{
    @Test
    void snapshotsBecomeRangeAndSampleEntries()
    {
        TorchmasterLightRangeDisplay.RangeSnapshot snapshot = new TorchmasterLightRangeDisplay.RangeSnapshot(
                new BlockPos(10, 20, 30),
                2,
                Arrays.asList(new BlockPos(1, 2, 3), new BlockPos(-1, -2, -3)));

        TorchmasterRangeRenderPlan plan = TorchmasterRangeRenderPlan.fromSnapshots(Collections.singletonList(snapshot));

        assertEquals(3, plan.entries().size());
        assertStyle(plan.entries().get(0).style, TorchmasterLineBoxRenderer.rangeStyle(snapshot.pos));
        assertBox(plan.entries().get(0).box, 8, 18, 28, 13, 23, 33);
        assertStyle(plan.entries().get(1).style, TorchmasterLineBoxRenderer.sampleStyle(snapshot.pos));
        assertBox(plan.entries().get(1).box, 1, 2, 3, 2, 3, 4);
        assertStyle(plan.entries().get(2).style, TorchmasterLineBoxRenderer.sampleStyle(snapshot.pos));
        assertBox(plan.entries().get(2).box, -1, -2, -3, 0, -1, -2);
    }

    @Test
    void emptySnapshotsProduceEmptyPlan()
    {
        assertEquals(0, TorchmasterRangeRenderPlan.fromSnapshots(Collections.emptyList()).entries().size());
    }

    @Test
    void snapshotCanProvideEffectiveRangeBox()
    {
        TorchmasterRangeBoxes.Box effectiveBox = TorchmasterRangeBoxes.box(-32, -4, -16, 48, 13, 64);
        TorchmasterLightRangeDisplay.RangeSnapshot snapshot = new TorchmasterLightRangeDisplay.RangeSnapshot(
                new BlockPos(10, 4, 30),
                effectiveBox,
                Collections.emptyList());

        TorchmasterRangeRenderPlan plan = TorchmasterRangeRenderPlan.fromSnapshots(Collections.singletonList(snapshot));

        assertEquals(1, plan.entries().size());
        assertBox(plan.entries().get(0).box, -32, -4, -16, 48, 13, 64);
    }

    @Test
    void rangeStyleIsDeterministicAndBrightEnough()
    {
        BlockPos pos = new BlockPos(10, 20, 30);
        TorchmasterLineBoxRenderer.Style style = TorchmasterLineBoxRenderer.rangeStyle(pos);

        assertStyle(style, TorchmasterLineBoxRenderer.rangeStyle(pos));
        assertTrue(Math.max(style.red, Math.max(style.green, style.blue)) >= 0.72F);
        assertTrue(style.red + style.green + style.blue >= 0.9F);
    }

    @Test
    void sampleStyleUsesRangeColorWithSampleAlpha()
    {
        BlockPos pos = new BlockPos(10, 20, 30);
        TorchmasterLineBoxRenderer.Style range = TorchmasterLineBoxRenderer.rangeStyle(pos);
        TorchmasterLineBoxRenderer.Style sample = TorchmasterLineBoxRenderer.sampleStyle(pos);

        assertEquals(range.red, sample.red, 0.000001F);
        assertEquals(range.green, sample.green, 0.000001F);
        assertEquals(range.blue, sample.blue, 0.000001F);
        assertEquals(TorchmasterLineBoxRenderer.SAMPLE_STYLE.alpha, sample.alpha, 0.000001F);
    }

    private static void assertBox(TorchmasterRangeBoxes.Box box, double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
        assertEquals(minX, box.minX, 0.0D);
        assertEquals(minY, box.minY, 0.0D);
        assertEquals(minZ, box.minZ, 0.0D);
        assertEquals(maxX, box.maxX, 0.0D);
        assertEquals(maxY, box.maxY, 0.0D);
        assertEquals(maxZ, box.maxZ, 0.0D);
    }

    private static void assertStyle(TorchmasterLineBoxRenderer.Style actual, TorchmasterLineBoxRenderer.Style expected)
    {
        assertEquals(expected.red, actual.red, 0.000001F);
        assertEquals(expected.green, actual.green, 0.000001F);
        assertEquals(expected.blue, actual.blue, 0.000001F);
        assertEquals(expected.alpha, actual.alpha, 0.000001F);
    }
}
