package net.xalcon.torchmaster.minecraft.storage;

import net.xalcon.torchmaster.domain.LightEntry;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.LightInfo;
import net.xalcon.torchmaster.port.SpawnReason;
import net.xalcon.torchmaster.port.Vec3View;

import java.util.Optional;

public interface LightStoreBridge
{
    boolean shouldBlockEntityType(EntityTypeKey entityType, Vec3View pos, SpawnReason spawnType);
    boolean shouldBlockVillageZombieRaid(Vec3View pos);
    boolean shouldBlockNaturalSpawnPosition(Vec3View pos);
    boolean shouldBlockNaturalSpawnChunk(int chunkX, int chunkZ);

    /**
     * Warning: The persisted light instance should not be directly attached to any chunk data!
     * This means, there should be no tileentities or entities implementing this interface!
     * The registry will call every instance on spawn checks, and since the registry doesnt care about chunks
     * it might cause chunk loads, memory leaks or other unexpected behaviors.
     * If a light implementation is only supposed to work while a chunk is loaded, registration
     * needs to be handled accordingly.
     *
     * Registrations are persisted across world loads, a registration is therefore only needed once.
     * @param lightKey a dimension-unique key for the given light
     * @param light the light instance
     */
    void registerLight(String lightKey, LightEntry light);
    Optional<LightEntry> getLight(String lightKey);
    void unregisterLight(String lightKey);

    LightInfo[] getEntries();

    default LightEntry[] lightEntries()
    {
        return new LightEntry[0];
    }
}
