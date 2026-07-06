package net.xalcon.torchmaster.minecraft.light;

import io.netty.buffer.Unpooled;
//? if >=1.16
import net.minecraft.network.PacketByteBuf;
//? if <1.16
//import net.minecraft.util.PacketByteBuf;
import net.xalcon.torchmaster.port.LightSettingsView;
import org.junit.jupiter.api.Test;

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

    private static LightSettingsView roundTrip(LightSettingsView snapshot)
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        LightSettingsPacketCodec.writeSnapshot(buf, snapshot);
        return LightSettingsPacketCodec.readSnapshot(buf);
    }
}
