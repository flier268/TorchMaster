package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.BlockPosView;
import net.xalcon.torchmaster.port.Vec3View;

public final class DistanceLogic {
    private DistanceLogic() {
    }

    public static boolean isInCubicRange(Vec3View position, BlockPosView origin, int range) {
        double minX = origin.x() - range;
        double minY = origin.y() - range;
        double minZ = origin.z() - range;
        double maxX = origin.x() + range + 1;
        double maxY = origin.y() + range + 1;
        double maxZ = origin.z() + range + 1;

        return minX <= position.x() && maxX >= position.x()
                && minY <= position.y() && maxY >= position.y()
                && minZ <= position.z() && maxZ >= position.z();
    }

    public static boolean isInCylinderRange(Vec3View position, BlockPosView origin, int range) {
        double dx = origin.x() + 0.5 - position.x();
        double dy = Math.abs(origin.y() + 0.5 - position.y());
        double dz = origin.z() + 0.5 - position.z();

        return (dx * dx + dz * dz) <= range && dy <= range;
    }
}
