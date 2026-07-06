package net.xalcon.torchmaster.client;

import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

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
        assertSame(TorchmasterLineBoxRenderer.RANGE_STYLE, plan.entries().get(0).style);
        assertBox(plan.entries().get(0).box, 8, 18, 28, 13, 23, 33);
        assertSame(TorchmasterLineBoxRenderer.SAMPLE_STYLE, plan.entries().get(1).style);
        assertBox(plan.entries().get(1).box, 1, 2, 3, 2, 3, 4);
        assertBox(plan.entries().get(2).box, -1, -2, -3, 0, -1, -2);
    }

    @Test
    void emptySnapshotsProduceEmptyPlan()
    {
        assertEquals(0, TorchmasterRangeRenderPlan.fromSnapshots(Collections.emptyList()).entries().size());
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
}
