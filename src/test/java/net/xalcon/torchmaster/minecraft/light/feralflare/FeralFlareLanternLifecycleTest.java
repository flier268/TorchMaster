package net.xalcon.torchmaster.minecraft.light.feralflare;

import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeralFlareLanternLifecycleTest {
    @Test
    void missingTrackedChildShouldBeRemoved() {
        assertTrue(FeralFlareLanternLifecycle.shouldRemoveTrackedChild(new BlockPos(1, 2, 3), false));
        assertFalse(FeralFlareLanternLifecycle.shouldRemoveTrackedChild(new BlockPos(1, 2, 3), true));
        assertFalse(FeralFlareLanternLifecycle.shouldRemoveTrackedChild(null, false));
    }
}
