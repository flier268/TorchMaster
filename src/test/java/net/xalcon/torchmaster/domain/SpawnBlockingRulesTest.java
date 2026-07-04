package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.ConfigView;
import net.xalcon.torchmaster.port.EventResultPort;
import net.xalcon.torchmaster.port.SpawnReason;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpawnBlockingRulesTest {
    @Test
    void intentionalSpawnIsSkipped() {
        assertTrue(SpawnBlockingRules.shouldSkipEntitySpawnCheck(
                SpawnReason.SPAWN_EGG,
                EventResultPort.DEFAULT,
                config(true, false, true)
        ));
    }

    @Test
    void existingAllowIsRespectedWhenAggressiveChecksAreDisabled() {
        assertTrue(SpawnBlockingRules.shouldSkipEntitySpawnCheck(
                SpawnReason.NATURAL,
                EventResultPort.ALLOW,
                config(false, false, true)
        ));
    }

    @Test
    void artificialSpawnIsSkippedWhenOnlyNaturalSpawnsAreBlocked() {
        assertTrue(SpawnBlockingRules.shouldSkipEntitySpawnCheck(
                SpawnReason.SPAWNER,
                EventResultPort.DEFAULT,
                config(true, true, true)
        ));
    }

    @Test
    void naturalSpawnContinuesThroughChecks() {
        assertFalse(SpawnBlockingRules.shouldSkipEntitySpawnCheck(
                SpawnReason.NATURAL,
                EventResultPort.DEFAULT,
                config(true, true, true)
        ));
    }

    @Test
    void villageSiegesAreSkippedWhenDisabled() {
        assertTrue(SpawnBlockingRules.shouldSkipVillageSiegeCheck(
                EventResultPort.DEFAULT,
                config(true, false, false)
        ));
    }

    @Test
    void feralFlarePlansPlacementAndRemoval() {
        assertTrue(FeralFlareLightPlanner.shouldPlaceLight(2, 10, 4, 8));
        assertFalse(FeralFlareLightPlanner.shouldPlaceLight(10, 10, 4, 8));
        assertTrue(FeralFlareLightPlanner.shouldRemoveChildLight(new net.xalcon.torchmaster.port.BlockPosView(1, 2, 3), false));
    }

    private static ConfigView config(boolean aggressive, boolean blockOnlyNatural, boolean blockVillageSieges) {
        return new ConfigView() {
            @Override
            public int feralFlareRadius() {
                return 16;
            }

            @Override
            public int dreadLampRadius() {
                return 32;
            }

            @Override
            public int megaTorchRadius() {
                return 64;
            }

            @Override
            public boolean aggressiveSpawnChecks() {
                return aggressive;
            }

            @Override
            public boolean blockOnlyNaturalSpawns() {
                return blockOnlyNatural;
            }

            @Override
            public boolean blockVillageSieges() {
                return blockVillageSieges;
            }
        };
    }
}
