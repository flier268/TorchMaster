package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.BlockPosView;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.Vec3View;

public final class LightRules {
    private LightRules() {
    }

    public static boolean blocksEntity(LightKind kind, EntityFilter filter, EntityTypeKey entityType, Vec3View position, BlockPosView lightPos, int radius) {
        if (!filter.contains(entityType)) {
            return false;
        }

        switch (kind) {
            case MEGA_TORCH:
            case DREAD_LAMP:
                return DistanceLogic.isInCubicRange(position, lightPos, radius);
            default:
                return false;
        }
    }

    public static boolean blocksVillageSiege(LightKind kind, Vec3View position, BlockPosView lightPos, int radius) {
        return kind == LightKind.MEGA_TORCH && DistanceLogic.isInCubicRange(position, lightPos, radius);
    }

    public static boolean blocksNaturalSpawnPosition(LightKind kind, boolean naturalSpawnOnly, Vec3View position, BlockPosView lightPos, int radius) {
        return naturalSpawnOnly
                && kind == LightKind.MEGA_TORCH
                && DistanceLogic.isInCubicRange(position, lightPos, radius);
    }

    public static boolean blocksNaturalSpawnChunk(LightKind kind, boolean naturalSpawnOnly, int chunkX, int chunkZ, BlockPosView lightPos, int radius) {
        if (!naturalSpawnOnly || kind != LightKind.MEGA_TORCH) {
            return false;
        }

        int chunkRadius = (radius + 15) >> 4;
        int lightChunkX = lightPos.x() >> 4;
        int lightChunkZ = lightPos.z() >> 4;
        return Math.abs(chunkX - lightChunkX) <= chunkRadius
                && Math.abs(chunkZ - lightChunkZ) <= chunkRadius;
    }
}
