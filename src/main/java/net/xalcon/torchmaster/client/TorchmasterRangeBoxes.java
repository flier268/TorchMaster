package net.xalcon.torchmaster.client;

import net.minecraft.util.math.BlockPos;

public final class TorchmasterRangeBoxes
{
    private TorchmasterRangeBoxes()
    {
    }

    public static Box rangeBox(BlockPos center, int radius)
    {
        return new Box(
                center.getX() - radius,
                center.getY() - radius,
                center.getZ() - radius,
                center.getX() + radius + 1,
                center.getY() + radius + 1,
                center.getZ() + radius + 1);
    }

    public static Box sampleBox(BlockPos pos)
    {
        return new Box(
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                pos.getX() + 1,
                pos.getY() + 1,
                pos.getZ() + 1);
    }

    public static final class Box
    {
        public final double minX;
        public final double minY;
        public final double minZ;
        public final double maxX;
        public final double maxY;
        public final double maxZ;

        private Box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
        {
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
        }
    }
}
