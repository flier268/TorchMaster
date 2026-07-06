package net.xalcon.torchmaster.client;

public final class TorchmasterLineBoxRenderer
{
    public static final Style RANGE_STYLE = new Style(0.15F, 0.85F, 1.0F, 0.85F);
    public static final Style SAMPLE_STYLE = new Style(0.25F, 1.0F, 0.45F, 0.42F);
    public static final float LINE_WIDTH = 2.0F;

    private TorchmasterLineBoxRenderer()
    {
    }

    public static Line[] lines(TorchmasterRangeBoxes.Box box)
    {
        return new Line[] {
                new Line(box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ),
                new Line(box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ),
                new Line(box.maxX, box.minY, box.maxZ, box.minX, box.minY, box.maxZ),
                new Line(box.minX, box.minY, box.maxZ, box.minX, box.minY, box.minZ),
                new Line(box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ),
                new Line(box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ),
                new Line(box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ),
                new Line(box.minX, box.maxY, box.maxZ, box.minX, box.maxY, box.minZ),
                new Line(box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ),
                new Line(box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ),
                new Line(box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ),
                new Line(box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ)
        };
    }

    public static final class Line
    {
        public final double startX;
        public final double startY;
        public final double startZ;
        public final double endX;
        public final double endY;
        public final double endZ;

        private Line(double startX, double startY, double startZ, double endX, double endY, double endZ)
        {
            this.startX = startX;
            this.startY = startY;
            this.startZ = startZ;
            this.endX = endX;
            this.endY = endY;
            this.endZ = endZ;
        }
    }

    public static final class Style
    {
        public final float red;
        public final float green;
        public final float blue;
        public final float alpha;

        private Style(float red, float green, float blue, float alpha)
        {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }
    }
}
