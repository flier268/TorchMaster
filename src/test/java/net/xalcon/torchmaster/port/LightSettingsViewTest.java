package net.xalcon.torchmaster.port;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LightSettingsViewTest
{
    @Test
    void missingSnapshotAppliesToSettingsBecauseTheBlockIsGone()
    {
        LightSettingsView snapshot = LightSettingsView.missing(8);

        assertFalse(snapshot.found());
        assertTrue(snapshot.appliesToSettings());
    }

    @Test
    void previewRemovedSnapshotOnlyClearsTheRangeDisplay()
    {
        LightSettingsView snapshot = LightSettingsView.previewRemoved(8);

        assertFalse(snapshot.found());
        assertFalse(snapshot.appliesToSettings());
        assertFalse(snapshot.previewSyncStart());
        assertFalse(snapshot.previewSyncEnd());
    }

    @Test
    void previewSyncMarkersOnlyControlRangeReconciliation()
    {
        LightSettingsView start = LightSettingsView.previewSyncStartMarker();
        LightSettingsView end = LightSettingsView.previewSyncEndMarker();

        assertFalse(start.found());
        assertFalse(start.appliesToSettings());
        assertTrue(start.previewSyncStart());
        assertFalse(start.previewSyncEnd());
        assertFalse(end.found());
        assertFalse(end.appliesToSettings());
        assertFalse(end.previewSyncStart());
        assertTrue(end.previewSyncEnd());
    }
}
