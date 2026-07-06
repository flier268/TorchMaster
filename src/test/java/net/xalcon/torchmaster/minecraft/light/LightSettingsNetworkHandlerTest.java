package net.xalcon.torchmaster.minecraft.light;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LightSettingsNetworkHandlerTest
{
    @Test
    void chunkBroadcastRangeIncludesHundredChunkSquare()
    {
        assertTrue(LightSettingsNetworkHandler.isWithinChunkRadius(0, 0, 100, 100, 100));
        assertTrue(LightSettingsNetworkHandler.isWithinChunkRadius(0, 0, -100, -100, 100));
        assertFalse(LightSettingsNetworkHandler.isWithinChunkRadius(0, 0, 101, 0, 100));
        assertFalse(LightSettingsNetworkHandler.isWithinChunkRadius(0, 0, 0, -101, 100));
    }

    @Test
    void negativeRadiusOnlyIncludesSameChunk()
    {
        assertTrue(LightSettingsNetworkHandler.isWithinChunkRadius(4, -3, 4, -3, -1));
        assertFalse(LightSettingsNetworkHandler.isWithinChunkRadius(4, -3, 5, -3, -1));
    }
}
