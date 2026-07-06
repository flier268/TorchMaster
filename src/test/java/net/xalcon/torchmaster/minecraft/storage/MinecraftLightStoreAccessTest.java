package net.xalcon.torchmaster.minecraft.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinecraftLightStoreAccessTest {
    @Test
    void storageIdKeepsLegacyPrefix() {
        assertEquals("torchmaster_lights_minecraft_overworld", MinecraftLightStoreAccess.storageId("minecraft_overworld"));
    }
}
