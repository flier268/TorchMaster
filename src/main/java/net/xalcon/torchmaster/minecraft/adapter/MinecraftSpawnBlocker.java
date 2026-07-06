package net.xalcon.torchmaster.minecraft.adapter;

import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.xalcon.torchmaster.minecraft.storage.LightStoreBridge;
import net.xalcon.torchmaster.minecraft.storage.MinecraftLightStoreAccess;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.SpawnReason;
import net.xalcon.torchmaster.port.Vec3View;

public final class MinecraftSpawnBlocker {
    private MinecraftSpawnBlocker() {
    }

    public static boolean shouldBlockEntity(World level, EntityType<?> entityType, Vec3d location, SpawnReason spawnReason) {
        EntityTypeKey entityTypeKey = MinecraftAdapterViews.entityTypeKey(EntityType.getId(entityType));
        Vec3View position = MinecraftAdapterViews.vec3(location);
        return MinecraftLightStoreAccess.get(level)
                .map(store -> shouldBlockEntity(store, entityTypeKey, position, spawnReason))
                .orElse(false);
    }

    public static boolean shouldBlockVillageSiege(World level, Vec3d attemptedSpawnPos) {
        Vec3View position = MinecraftAdapterViews.vec3(attemptedSpawnPos);
        return MinecraftLightStoreAccess.get(level)
                .map(store -> shouldBlockVillageSiege(store, position))
                .orElse(false);
    }

    public static boolean shouldBlockNaturalSpawnPosition(World level, BlockPos pos) {
        Vec3View position = new Vec3View(pos.getX(), pos.getY(), pos.getZ());
        return MinecraftLightStoreAccess.get(level)
                .map(store -> shouldBlockNaturalSpawnPosition(store, position))
                .orElse(false);
    }

    public static boolean shouldBlockNaturalSpawnChunk(World level, ChunkPos chunkPos) {
        return MinecraftLightStoreAccess.get(level)
                .map(store -> shouldBlockNaturalSpawnChunk(store, chunkPos.x, chunkPos.z))
                .orElse(false);
    }

    static boolean shouldBlockEntity(LightStoreBridge store, EntityTypeKey entityType, Vec3View position, SpawnReason spawnReason) {
        return store.shouldBlockEntityType(entityType, position, spawnReason);
    }

    static boolean shouldBlockVillageSiege(LightStoreBridge store, Vec3View attemptedSpawnPos) {
        return store.shouldBlockVillageZombieRaid(attemptedSpawnPos);
    }

    static boolean shouldBlockNaturalSpawnPosition(LightStoreBridge store, Vec3View position) {
        return store.shouldBlockNaturalSpawnPosition(position);
    }

    static boolean shouldBlockNaturalSpawnChunk(LightStoreBridge store, int chunkX, int chunkZ) {
        return store.shouldBlockNaturalSpawnChunk(chunkX, chunkZ);
    }
}
