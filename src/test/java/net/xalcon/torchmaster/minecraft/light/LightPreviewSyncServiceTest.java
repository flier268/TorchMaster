package net.xalcon.torchmaster.minecraft.light;

import net.xalcon.torchmaster.port.LightSettingsView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LightPreviewSyncServiceTest
{
    @Test
    void visibleRangeSyncUsesExplicitStartAndEndMarkers()
    {
        LightSettingsView start = LightSettingsView.previewSyncStartMarker();
        LightSettingsView end = LightSettingsView.previewSyncEndMarker();

        assertTrue(start.previewSyncStart());
        assertFalse(start.previewSyncEnd());
        assertFalse(start.appliesToSettings());
        assertFalse(end.previewSyncStart());
        assertTrue(end.previewSyncEnd());
        assertFalse(end.appliesToSettings());
    }
}
