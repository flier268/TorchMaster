package net.xalcon.torchmaster.minecraft.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinecraftLightStoreAccessTest {
    @Test
    void storageIdStaysStableAcrossMinecraftVersions() {
        assertEquals("torchmaster_lights", MinecraftLightStoreAccess.storageId());
    }
}
