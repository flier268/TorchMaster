package net.xalcon.torchmaster.adapter;

public interface LightStore {
    boolean blocksEntity(EntityTypeKey entityType, Vec3View position, SpawnReason spawnReason);

    boolean blocksVillageSiege(Vec3View position);
}
