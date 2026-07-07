package net.xalcon.torchmaster.minecraft.light;

import io.netty.buffer.Unpooled;
//? if >=1.16
import net.minecraft.network.PacketByteBuf;
//? if <1.16
//import net.minecraft.util.PacketByteBuf;
import net.xalcon.torchmaster.domain.LightSettings;
import net.xalcon.torchmaster.port.LightSettingsView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LightSettingsPacketCodecTest
{
    @Test
    void snapshotRoundTripPreservesSettingsApplicability()
    {
        LightSettingsView missing = roundTrip(LightSettingsView.missing(8));
        LightSettingsView previewRemoved = roundTrip(LightSettingsView.previewRemoved(8));
        LightSettingsView syncStart = roundTrip(LightSettingsView.previewSyncStartMarker());
        LightSettingsView syncEnd = roundTrip(LightSettingsView.previewSyncEndMarker());

        assertFalse(missing.found());
        assertTrue(missing.appliesToSettings());
        assertFalse(previewRemoved.found());
        assertFalse(previewRemoved.appliesToSettings());
        assertTrue(syncStart.previewSyncStart());
        assertFalse(syncStart.previewSyncEnd());
        assertFalse(syncStart.appliesToSettings());
        assertFalse(syncEnd.previewSyncStart());
        assertTrue(syncEnd.previewSyncEnd());
        assertFalse(syncEnd.appliesToSettings());
    }

    @Test
    void settingsRoundTripPreservesDirectionalRanges()
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        LightSettingsPacketCodec.writeSettings(buf, LightSettings.configured(false, 1, 2, 3, 4, 5, 6));
        LightSettings settings = LightSettingsPacketCodec.readSettings(buf);

        assertFalse(settings.enabled());
        assertEquals(1, settings.rangeWest());
        assertEquals(2, settings.rangeEast());
        assertEquals(3, settings.rangeDown());
        assertEquals(4, settings.rangeUp());
        assertEquals(5, settings.rangeNorth());
        assertEquals(6, settings.rangeSouth());
    }

    @Test
    void snapshotRoundTripPreservesDirectionalRanges()
    {
        LightSettingsView snapshot = roundTrip(LightSettingsView.present(true, true, true, 1, 2, 3, 4, 5, 6, 16, false, true,
                new net.xalcon.torchmaster.port.LightAccessEntry[0]));

        assertTrue(snapshot.found());
        assertTrue(snapshot.rangeVisible());
        assertEquals(1, snapshot.rangeWest());
        assertEquals(2, snapshot.rangeEast());
        assertEquals(3, snapshot.rangeDown());
        assertEquals(4, snapshot.rangeUp());
        assertEquals(5, snapshot.rangeNorth());
        assertEquals(6, snapshot.rangeSouth());
    }

    private static LightSettingsView roundTrip(LightSettingsView snapshot)
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        LightSettingsPacketCodec.writeSnapshot(buf, snapshot);
        return LightSettingsPacketCodec.readSnapshot(buf);
    }
}
