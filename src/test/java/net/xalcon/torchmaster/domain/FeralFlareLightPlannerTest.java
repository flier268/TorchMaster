package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.BlockPosView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeralFlareLightPlannerTest {
    @Test
    void clientSideNeverTicks() {
        assertFalse(FeralFlareLightPlanner.shouldTick(true, 20, 20));
    }

    @Test
    void tickRateControlsServerTick() {
        assertFalse(FeralFlareLightPlanner.shouldTick(false, 19, 20));
        assertTrue(FeralFlareLightPlanner.shouldTick(false, 20, 20));
        assertFalse(FeralFlareLightPlanner.shouldTick(false, 20, 0));
    }

    @Test
    void clientSideTickOnlyAdvancesCounter() {
        FeralFlareLightPlanner.TickDecision decision = FeralFlareLightPlanner.beginTick(
                true, 19, 2, 20, 0, 10);

        assertFalse(decision.runCycle());
        assertEquals(20, decision.ticks());
        assertEquals(2, decision.checkIndex());
    }

    @Test
    void serverTickAtRateStartsCycleAndResetsTicks() {
        FeralFlareLightPlanner.TickDecision decision = FeralFlareLightPlanner.beginTick(
                false, 19, 2, 20, 0, 10);

        assertTrue(decision.runCycle());
        assertEquals(0, decision.ticks());
        assertEquals(2, decision.checkIndex());
    }

    @Test
    void hardcapOverflowDoesNotResetTicks() {
        FeralFlareLightPlanner.TickDecision decision = FeralFlareLightPlanner.beginTick(
                false, 19, 2, 20, 11, 10);

        assertFalse(decision.runCycle());
        assertEquals(20, decision.ticks());
        assertEquals(2, decision.checkIndex());
    }

    @Test
    void placementRequiresCapacityAndLowLight() {
        assertTrue(FeralFlareLightPlanner.shouldPlaceLight(2, 10, 4, 8));
        assertFalse(FeralFlareLightPlanner.shouldPlaceLight(10, 10, 4, 8));
        assertFalse(FeralFlareLightPlanner.shouldPlaceLight(2, 10, 8, 8));
    }

    @Test
    void removalRequiresKnownChildAndMissingBlock() {
        assertTrue(FeralFlareLightPlanner.shouldRemoveChildLight(new BlockPosView(1, 2, 3), false));
        assertFalse(FeralFlareLightPlanner.shouldRemoveChildLight(new BlockPosView(1, 2, 3), true));
        assertFalse(FeralFlareLightPlanner.shouldRemoveChildLight(null, false));
    }

    @Test
    void checkIndexWrapsThroughChildLights() {
        assertEquals(0, FeralFlareLightPlanner.nextCheckIndex(2, 3));
        assertEquals(2, FeralFlareLightPlanner.nextCheckIndex(1, 3));
    }

    @Test
    void encodesAndDecodesRelativePosition() {
        BlockPosView origin = new BlockPosView(100, 64, -100);
        BlockPosView target = new BlockPosView(96, 70, -90);

        int encoded = FeralFlareLightPlanner.encodeRelativePosition(origin, target);
        BlockPosView decoded = FeralFlareLightPlanner.decodeRelativePosition(origin, encoded);

        assertEquals(target.x(), decoded.x());
        assertEquals(target.y(), decoded.y());
        assertEquals(target.z(), decoded.z());
    }

    @Test
    void relativePositionUsesSignedByteRange() {
        BlockPosView origin = new BlockPosView(0, 0, 0);
        BlockPosView target = new BlockPosView(-128, 127, -1);

        int encoded = FeralFlareLightPlanner.encodeRelativePosition(origin, target);
        BlockPosView decoded = FeralFlareLightPlanner.decodeRelativePosition(origin, encoded);

        assertEquals(-128, decoded.x());
        assertEquals(127, decoded.y());
        assertEquals(-1, decoded.z());
    }
}
