package net.xalcon.torchmaster.minecraft.light.feralflare;

import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FeralFlareLanternLifecycleTest {
    @Test
    void clientSideTickOnlyAdvancesCounter() {
        FeralFlareLanternLifecycle.TickDecision decision = FeralFlareLanternLifecycle.beginTick(
                true, 19, 2, 20, 0, 10);

        assertFalse(decision.runCycle());
        assertEquals(20, decision.outcome().ticks());
        assertEquals(2, decision.outcome().checkIndex());
        assertFalse(decision.outcome().dirty());
    }

    @Test
    void serverTickAtRateStartsCycleAndResetsTicks() {
        FeralFlareLanternLifecycle.TickDecision decision = FeralFlareLanternLifecycle.beginTick(
                false, 19, 2, 20, 0, 10);

        assertTrue(decision.runCycle());
        assertEquals(0, decision.outcome().ticks());
        assertEquals(2, decision.outcome().checkIndex());
        assertFalse(decision.outcome().dirty());
    }

    @Test
    void hardcapOverflowDoesNotResetTicks() {
        FeralFlareLanternLifecycle.TickDecision decision = FeralFlareLanternLifecycle.beginTick(
                false, 19, 2, 20, 11, 10);

        assertFalse(decision.runCycle());
        assertEquals(20, decision.outcome().ticks());
        assertEquals(2, decision.outcome().checkIndex());
    }

    @Test
    void checkIndexWrapsThroughChildLights() {
        assertEquals(0, FeralFlareLanternLifecycle.nextCheckIndex(2, 3));
        assertEquals(2, FeralFlareLanternLifecycle.nextCheckIndex(1, 3));
    }

    @Test
    void missingTrackedChildShouldBeRemoved() {
        assertTrue(FeralFlareLanternLifecycle.shouldRemoveTrackedChild(new BlockPos(1, 2, 3), false));
        assertFalse(FeralFlareLanternLifecycle.shouldRemoveTrackedChild(new BlockPos(1, 2, 3), true));
        assertFalse(FeralFlareLanternLifecycle.shouldRemoveTrackedChild(null, false));
    }
}
