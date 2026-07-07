package net.xalcon.torchmaster.domain;

public final class LightSettings
{
    private static final LightSettings UNCONFIGURED = new LightSettings(false, true, 0, 0, 0, 0, 0, 0);

    private final boolean configured;
    private final boolean enabled;
    private final int rangeWest;
    private final int rangeEast;
    private final int rangeDown;
    private final int rangeUp;
    private final int rangeNorth;
    private final int rangeSouth;

    private LightSettings(boolean configured, boolean enabled, int rangeWest, int rangeEast, int rangeDown, int rangeUp, int rangeNorth,
            int rangeSouth)
    {
        this.configured = configured;
        this.enabled = enabled;
        this.rangeWest = Math.max(0, rangeWest);
        this.rangeEast = Math.max(0, rangeEast);
        this.rangeDown = Math.max(0, rangeDown);
        this.rangeUp = Math.max(0, rangeUp);
        this.rangeNorth = Math.max(0, rangeNorth);
        this.rangeSouth = Math.max(0, rangeSouth);
    }

    public static LightSettings unconfigured()
    {
        return UNCONFIGURED;
    }

    public static LightSettings configured(boolean enabled, int rangeWest, int rangeEast, int rangeDown, int rangeUp, int rangeNorth,
            int rangeSouth)
    {
        return new LightSettings(true, enabled, rangeWest, rangeEast, rangeDown, rangeUp, rangeNorth, rangeSouth);
    }

    public static LightSettings defaultFor(int globalRadius)
    {
        int normalized = Math.max(0, globalRadius);
        return new LightSettings(false, true, normalized, normalized, normalized, normalized, normalized, normalized);
    }

    public boolean configured()
    {
        return configured;
    }

    public boolean enabled()
    {
        return enabled;
    }

    public int rangeWest()
    {
        return rangeWest;
    }

    public int rangeEast()
    {
        return rangeEast;
    }

    public int rangeDown()
    {
        return rangeDown;
    }

    public int rangeUp()
    {
        return rangeUp;
    }

    public int rangeNorth()
    {
        return rangeNorth;
    }

    public int rangeSouth()
    {
        return rangeSouth;
    }

    public LightSettings effective(int globalRadius)
    {
        int max = Math.max(0, globalRadius);
        if (!configured) {
            return defaultFor(max);
        }
        return new LightSettings(true, enabled, clamp(rangeWest, max), clamp(rangeEast, max), clamp(rangeDown, max), clamp(rangeUp, max),
                clamp(rangeNorth, max), clamp(rangeSouth, max));
    }

    private static int clamp(int value, int max)
    {
        return Math.max(0, Math.min(value, max));
    }
}
