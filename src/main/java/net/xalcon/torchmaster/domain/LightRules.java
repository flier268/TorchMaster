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
}
