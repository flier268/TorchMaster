package net.xalcon.torchmaster.domain;

public final class LightSettings
{
    private static final LightSettings UNCONFIGURED = new LightSettings(false, true, 0, 0, 0);

    private final boolean configured;
    private final boolean enabled;
    private final int radiusX;
    private final int radiusY;
    private final int radiusZ;

    private LightSettings(boolean configured, boolean enabled, int radiusX, int radiusY, int radiusZ)
    {
        this.configured = configured;
        this.enabled = enabled;
        this.radiusX = Math.max(0, radiusX);
        this.radiusY = Math.max(0, radiusY);
        this.radiusZ = Math.max(0, radiusZ);
    }

    public static LightSettings unconfigured()
    {
        return UNCONFIGURED;
    }

    public static LightSettings configured(boolean enabled, int radiusX, int radiusY, int radiusZ)
    {
        return new LightSettings(true, enabled, radiusX, radiusY, radiusZ);
    }

    public static LightSettings defaultFor(int globalRadius)
    {
        int normalized = Math.max(0, globalRadius);
        return new LightSettings(false, true, normalized, normalized, normalized);
    }

    public boolean configured()
    {
        return configured;
    }

    public boolean enabled()
    {
        return enabled;
    }

    public int radiusX()
    {
        return radiusX;
    }

    public int radiusY()
    {
        return radiusY;
    }

    public int radiusZ()
    {
        return radiusZ;
    }

    public LightSettings effective(int globalRadius)
    {
        int max = Math.max(0, globalRadius);
        if (!configured) {
            return defaultFor(max);
        }
        return new LightSettings(true, enabled, clamp(radiusX, max), clamp(radiusY, max), clamp(radiusZ, max));
    }

    private static int clamp(int value, int max)
    {
        return Math.max(0, Math.min(value, max));
    }
}
