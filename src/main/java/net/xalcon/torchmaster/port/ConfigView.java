package net.xalcon.torchmaster.port;

public interface ConfigView {
    int feralFlareRadius();

    int dreadLampRadius();

    int megaTorchRadius();

    boolean aggressiveSpawnChecks();

    boolean blockOnlyNaturalSpawns();

    boolean blockVillageSieges();
}
