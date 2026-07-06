package net.xalcon.torchmaster.client;

import net.minecraft.util.math.BlockPos;

public final class TorchmasterLineBoxRenderer
{
    private static final float RANGE_MIN_SATURATION = 0.45F;
    private static final float RANGE_SATURATION_SPAN = 0.35F;
    private static final float RANGE_MIN_BRIGHTNESS = 0.75F;
    private static final float RANGE_BRIGHTNESS_SPAN = 0.25F;
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

    public static Style rangeStyle(BlockPos center)
    {
        long hash = coordinateHash(center);
        float hue = component(hash, 0);
        float saturation = RANGE_MIN_SATURATION + component(hash, 21) * RANGE_SATURATION_SPAN;
        float brightness = RANGE_MIN_BRIGHTNESS + component(hash, 42) * RANGE_BRIGHTNESS_SPAN;
        return hsv(hue, saturation, brightness, RANGE_STYLE.alpha);
    }

    public static Style sampleStyle(BlockPos center)
    {
        return rangeStyle(center).withAlpha(SAMPLE_STYLE.alpha);
    }

    static long coordinateHash(BlockPos pos)
    {
        long hash = 0xCBF29CE484222325L;
        hash = mix(hash, pos.getX());
        hash = mix(hash, pos.getY());
        hash = mix(hash, pos.getZ());
        return avalanche(hash);
    }

    private static long mix(long hash, int value)
    {
        hash ^= value;
        return hash * 0x100000001B3L;
    }

    private static long avalanche(long hash)
    {
        hash ^= hash >>> 33;
        hash *= 0xFF51AFD7ED558CCDL;
        hash ^= hash >>> 33;
        hash *= 0xC4CEB9FE1A85EC53L;
        hash ^= hash >>> 33;
        return hash;
    }

    private static float component(long hash, int shift)
    {
        return ((hash >>> shift) & 0xFFFFFL) / 1048575.0F;
    }

    private static Style hsv(float hue, float saturation, float brightness, float alpha)
    {
        float scaledHue = hue * 6.0F;
        int sector = (int)Math.floor(scaledHue);
        float fraction = scaledHue - sector;
        float p = brightness * (1.0F - saturation);
        float q = brightness * (1.0F - saturation * fraction);
        float t = brightness * (1.0F - saturation * (1.0F - fraction));

        switch (sector % 6) {
            case 0:
                return new Style(brightness, t, p, alpha);
            case 1:
                return new Style(q, brightness, p, alpha);
            case 2:
                return new Style(p, brightness, t, alpha);
            case 3:
                return new Style(p, q, brightness, alpha);
            case 4:
                return new Style(t, p, brightness, alpha);
            default:
                return new Style(brightness, p, q, alpha);
        }
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

        private Style withAlpha(float alpha)
        {
            return new Style(red, green, blue, alpha);
        }
    }
}
