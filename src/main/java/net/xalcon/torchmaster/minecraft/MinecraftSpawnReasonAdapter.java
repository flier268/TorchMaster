package net.xalcon.torchmaster.minecraft;

//? if >=1.21.2 {
/*import net.minecraft.world.entity.EntitySpawnReason;
*///?} else {
import net.minecraft.world.entity.MobSpawnType;
//?}
import net.xalcon.torchmaster.adapter.SpawnReason;

public final class MinecraftSpawnReasonAdapter {
    private MinecraftSpawnReasonAdapter() {
    }

    //? if >=1.21.2 {
    /*public static SpawnReason toPort(EntitySpawnReason spawnType) {
        switch (spawnType) {
            case SPAWN_ITEM_USE:
                return SpawnReason.SPAWN_EGG;
            case BREEDING:
                return SpawnReason.BREEDING;
            case DISPENSER:
                return SpawnReason.DISPENSER;
            case BUCKET:
                return SpawnReason.BUCKET;
            case CONVERSION:
                return SpawnReason.CONVERSION;
            case TRIGGERED:
                return SpawnReason.TRIGGERED;
            case COMMAND:
                return SpawnReason.COMMAND;
            case EVENT:
                return SpawnReason.EVENT;
            case TRIAL_SPAWNER:
                return SpawnReason.TRIAL_SPAWNER;
            case NATURAL:
                return SpawnReason.NATURAL;
            case CHUNK_GENERATION:
                return SpawnReason.CHUNK_GENERATION;
            case PATROL:
                return SpawnReason.PATROL;
            case SPAWNER:
                return SpawnReason.SPAWNER;
            case STRUCTURE:
                return SpawnReason.STRUCTURE;
            case MOB_SUMMONED:
                return SpawnReason.MOB_SUMMONED;
            case REINFORCEMENT:
                return SpawnReason.REINFORCEMENT;
            case JOCKEY:
                return SpawnReason.JOCKEY;
            default:
                return SpawnReason.NATURAL;
        }
    }
    *///?} else {
    public static SpawnReason toPort(MobSpawnType spawnType) {
        switch (spawnType) {
            case SPAWN_EGG:
                return SpawnReason.SPAWN_EGG;
            case BREEDING:
                return SpawnReason.BREEDING;
            case DISPENSER:
                return SpawnReason.DISPENSER;
            case BUCKET:
                return SpawnReason.BUCKET;
            case CONVERSION:
                return SpawnReason.CONVERSION;
            case TRIGGERED:
                return SpawnReason.TRIGGERED;
            case COMMAND:
                return SpawnReason.COMMAND;
            case EVENT:
                return SpawnReason.EVENT;
            //? if >=1.21 {
            case TRIAL_SPAWNER:
                return SpawnReason.TRIAL_SPAWNER;
//?}
            case NATURAL:
                return SpawnReason.NATURAL;
            case CHUNK_GENERATION:
                return SpawnReason.CHUNK_GENERATION;
            case PATROL:
                return SpawnReason.PATROL;
            case SPAWNER:
                return SpawnReason.SPAWNER;
            case STRUCTURE:
                return SpawnReason.STRUCTURE;
            case MOB_SUMMONED:
                return SpawnReason.MOB_SUMMONED;
            case REINFORCEMENT:
                return SpawnReason.REINFORCEMENT;
            case JOCKEY:
                return SpawnReason.JOCKEY;
            default:
                return SpawnReason.NATURAL;
        }
    }
    //?}
}
