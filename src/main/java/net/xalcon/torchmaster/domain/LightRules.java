package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.BlockPosView;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.Vec3View;

public final class LightRules {
    private LightRules() {
    }

    public static boolean blocksEntity(LightKind kind, EntityFilter filter, EntityTypeKey entityType, Vec3View position, BlockPosView lightPos, int radius) {
        return blocksEntity(kind, filter, entityType, position, lightPos, LightSettings.defaultFor(radius));
    }

    public static boolean blocksEntity(LightKind kind, EntityFilter filter, EntityTypeKey entityType, Vec3View position, BlockPosView lightPos, LightSettings settings) {
        if (!settings.enabled()) {
            return false;
        }
        if (!filter.contains(entityType)) {
            return false;
        }

        switch (kind) {
            case MEGA_TORCH:
            case DREAD_LAMP:
                return isInCuboidRange(position, lightPos, settings);
            default:
                return false;
        }
    }

    public static boolean blocksVillageSiege(LightKind kind, Vec3View position, BlockPosView lightPos, int radius) {
        return blocksVillageSiege(kind, position, lightPos, LightSettings.defaultFor(radius));
    }

    public static boolean blocksVillageSiege(LightKind kind, Vec3View position, BlockPosView lightPos, LightSettings settings) {
        return settings.enabled() && kind == LightKind.MEGA_TORCH && isInCuboidRange(position, lightPos, settings);
    }

    public static boolean blocksNaturalSpawnPosition(LightKind kind, boolean naturalSpawnOnly, Vec3View position, BlockPosView lightPos, int radius) {
        return blocksNaturalSpawnPosition(kind, naturalSpawnOnly, position, lightPos, LightSettings.defaultFor(radius));
    }

    public static boolean blocksNaturalSpawnPosition(LightKind kind, boolean naturalSpawnOnly, Vec3View position, BlockPosView lightPos, LightSettings settings) {
        if (!naturalSpawnOnly || !settings.enabled() || kind != LightKind.MEGA_TORCH) {
            return false;
        }
        return blocksNaturalSpawnChunk(kind, true, chunkCoordinate(position.x()), chunkCoordinate(position.z()), lightPos, settings);
    }

    public static boolean blocksNaturalSpawnChunk(LightKind kind, boolean naturalSpawnOnly, int chunkX, int chunkZ, BlockPosView lightPos, int radius) {
        return blocksNaturalSpawnChunk(kind, naturalSpawnOnly, chunkX, chunkZ, lightPos, LightSettings.defaultFor(radius));
    }

    public static boolean blocksNaturalSpawnChunk(LightKind kind, boolean naturalSpawnOnly, int chunkX, int chunkZ, BlockPosView lightPos, LightSettings settings) {
        if (!naturalSpawnOnly || kind != LightKind.MEGA_TORCH) {
            return false;
        }
        if (!settings.enabled()) {
            return false;
        }

        int chunkRadiusX = chunkRadius(settings.radiusX());
        int chunkRadiusZ = chunkRadius(settings.radiusZ());
        int lightChunkX = lightPos.x() >> 4;
        int lightChunkZ = lightPos.z() >> 4;
        return isInChunkRadius(chunkX, lightChunkX, chunkRadiusX)
                && isInChunkRadius(chunkZ, lightChunkZ, chunkRadiusZ);
    }

    private static boolean isInCuboidRange(Vec3View position, BlockPosView lightPos, LightSettings settings)
    {
        return position.x() >= lightPos.x() - settings.radiusX()
                && position.x() <= lightPos.x() + settings.radiusX() + 1
                && position.y() >= lightPos.y() - settings.radiusY()
                && position.y() <= lightPos.y() + settings.radiusY() + 1
                && position.z() >= lightPos.z() - settings.radiusZ()
                && position.z() <= lightPos.z() + settings.radiusZ() + 1;
    }

    private static int chunkCoordinate(double coordinate)
    {
        return ((int)Math.floor(coordinate)) >> 4;
    }

    private static int chunkRadius(int radius)
    {
        return (Math.max(0, radius) + 15) >> 4;
    }

    private static boolean isInChunkRadius(int chunk, int centerChunk, int radius)
    {
        return Math.abs(chunk - centerChunk) <= radius;
    }
}
