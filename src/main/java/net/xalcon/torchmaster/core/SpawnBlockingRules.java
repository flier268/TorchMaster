package net.xalcon.torchmaster.core;

import net.xalcon.torchmaster.adapter.ConfigView;
import net.xalcon.torchmaster.adapter.EventResultPort;
import net.xalcon.torchmaster.adapter.SpawnReason;

public final class SpawnBlockingRules {
    private SpawnBlockingRules() {
    }

    public static boolean shouldSkipEntitySpawnCheck(SpawnReason spawnReason, EventResultPort currentResult, ConfigView config) {
        if (spawnReason.isIntentional()) {
            return true;
        }

        if (!config.aggressiveSpawnChecks() && currentResult == EventResultPort.ALLOW) {
            return true;
        }

        return config.blockOnlyNaturalSpawns() && !spawnReason.isNatural();
    }

    public static boolean shouldSkipPhantomSpawnCheck(EventResultPort currentResult, ConfigView config) {
        return !config.aggressiveSpawnChecks() && currentResult == EventResultPort.ALLOW;
    }

    public static boolean shouldSkipVillageSiegeCheck(EventResultPort currentResult, ConfigView config) {
        if (!config.blockVillageSieges()) {
            return true;
        }

        return !config.aggressiveSpawnChecks() && currentResult == EventResultPort.ALLOW;
    }
}
