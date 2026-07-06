package net.xalcon.torchmaster.port;

import java.util.Arrays;

public final class LightSettingsView
{
    private static final LightAccessEntry[] EMPTY_ACCESS = new LightAccessEntry[0];

    private final boolean found;
    private final boolean editable;
    private final boolean accessManageable;
    private final boolean enabled;
    private final int radiusX;
    private final int radiusY;
    private final int radiusZ;
    private final int globalMax;
    private final boolean chunkAligned;
    private final boolean rangeVisible;
    private final boolean appliesToSettings;
    private final boolean previewSyncStart;
    private final boolean previewSyncEnd;
    private final LightAccessEntry[] accessEntries;

    private LightSettingsView(boolean found, boolean editable, boolean accessManageable, boolean enabled, int radiusX, int radiusY, int radiusZ,
            int globalMax, boolean chunkAligned, boolean rangeVisible, boolean appliesToSettings, boolean previewSyncStart, boolean previewSyncEnd,
            LightAccessEntry[] accessEntries)
    {
        this.found = found;
        this.editable = editable;
        this.accessManageable = accessManageable;
        this.enabled = enabled;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.radiusZ = radiusZ;
        this.globalMax = globalMax;
        this.chunkAligned = chunkAligned;
        this.rangeVisible = rangeVisible;
        this.appliesToSettings = appliesToSettings;
        this.previewSyncStart = previewSyncStart;
        this.previewSyncEnd = previewSyncEnd;
        this.accessEntries = accessEntries == null ? EMPTY_ACCESS : Arrays.copyOf(accessEntries, accessEntries.length);
    }

    public static LightSettingsView missing(int globalMax)
    {
        return new LightSettingsView(false, false, false, true, globalMax, globalMax, globalMax, globalMax, false, false, true, false, false, EMPTY_ACCESS);
    }

    public static LightSettingsView previewRemoved(int globalMax)
    {
        return new LightSettingsView(false, false, false, true, globalMax, globalMax, globalMax, globalMax, false, false, false, false, false, EMPTY_ACCESS);
    }

    public static LightSettingsView previewSyncStartMarker()
    {
        return new LightSettingsView(false, false, false, true, 0, 0, 0, 0, false, false, false, true, false, EMPTY_ACCESS);
    }

    public static LightSettingsView previewSyncEndMarker()
    {
        return new LightSettingsView(false, false, false, true, 0, 0, 0, 0, false, false, false, false, true, EMPTY_ACCESS);
    }

    public static LightSettingsView present(boolean editable, boolean enabled, int radiusX, int radiusY, int radiusZ, int globalMax)
    {
        return present(editable, false, enabled, radiusX, radiusY, radiusZ, globalMax, false, false, EMPTY_ACCESS);
    }

    public static LightSettingsView present(boolean editable, boolean accessManageable, boolean enabled, int radiusX, int radiusY, int radiusZ,
            int globalMax, LightAccessEntry[] accessEntries)
    {
        return present(editable, accessManageable, enabled, radiusX, radiusY, radiusZ, globalMax, false, false, accessEntries);
    }

    public static LightSettingsView present(boolean editable, boolean accessManageable, boolean enabled, int radiusX, int radiusY, int radiusZ,
            int globalMax, boolean chunkAligned, LightAccessEntry[] accessEntries)
    {
        return present(editable, accessManageable, enabled, radiusX, radiusY, radiusZ, globalMax, chunkAligned, false, accessEntries);
    }

    public static LightSettingsView present(boolean editable, boolean accessManageable, boolean enabled, int radiusX, int radiusY, int radiusZ,
            int globalMax, boolean chunkAligned, boolean rangeVisible, LightAccessEntry[] accessEntries)
    {
        return new LightSettingsView(true, editable, accessManageable, enabled, radiusX, radiusY, radiusZ, globalMax, chunkAligned, rangeVisible, true,
                false, false, accessEntries);
    }

    public boolean found()
    {
        return found;
    }

    public boolean editable()
    {
        return editable;
    }

    public boolean accessManageable()
    {
        return accessManageable;
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

    public int globalMax()
    {
        return globalMax;
    }

    public boolean chunkAligned()
    {
        return chunkAligned;
    }

    public boolean rangeVisible()
    {
        return rangeVisible;
    }

    public boolean appliesToSettings()
    {
        return appliesToSettings;
    }

    public boolean previewSyncStart()
    {
        return previewSyncStart;
    }

    public boolean previewSyncEnd()
    {
        return previewSyncEnd;
    }

    public LightAccessEntry[] accessEntries()
    {
        return Arrays.copyOf(accessEntries, accessEntries.length);
    }
}
