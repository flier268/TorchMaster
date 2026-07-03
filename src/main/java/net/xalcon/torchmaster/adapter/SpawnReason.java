package net.xalcon.torchmaster.adapter;

public enum SpawnReason {
    SPAWN_EGG(true, false),
    BREEDING(true, false),
    DISPENSER(true, false),
    BUCKET(true, false),
    CONVERSION(true, false),
    TRIGGERED(true, false),
    COMMAND(true, false),
    EVENT(true, false),
    TRIAL_SPAWNER(true, false),
    NATURAL(false, true),
    CHUNK_GENERATION(false, true),
    PATROL(false, true),
    SPAWNER(false, false),
    STRUCTURE(false, false),
    MOB_SUMMONED(false, false),
    REINFORCEMENT(false, false),
    JOCKEY(false, false),
    UNKNOWN(false, true);

    private final boolean intentional;
    private final boolean natural;

    SpawnReason(boolean intentional, boolean natural) {
        this.intentional = intentional;
        this.natural = natural;
    }

    public boolean isIntentional() {
        return intentional;
    }

    public boolean isNatural() {
        return natural;
    }
}
