package net.xalcon.torchmaster.minecraft.storage;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SavedLightStoreStateFactoryPolicyTest
{
    @Test
    void legacyPersistentStateUsesRequestedStorageId() throws IOException
    {
        String storeSource = readSource("src/main/java/net/xalcon/torchmaster/minecraft/storage/SavedLightStore.java");
        String factorySource = readSource("src/main/java/net/xalcon/torchmaster/minecraft/storage/SavedLightStoreStateFactory.java");

        assertTrue(storeSource.contains("super(storageId)"),
                "Minecraft <1.17 PersistentState caches new states by getId(); use the requested storageId, not a fixed id.");
        assertTrue(factorySource.contains("() -> SavedLightStoreStateBridge.create(storageId)"),
                "Minecraft <1.17 must create SavedLightStore with the same id used for getOrCreate lookup.");
    }

    private static String readSource(String path) throws IOException
    {
        return new String(Files.readAllBytes(repoRoot().resolve(path)), StandardCharsets.UTF_8);
    }

    private static Path repoRoot()
    {
        Path current = Paths.get("").toAbsolutePath();
        while (current != null && !Files.exists(current.resolve("settings.gradle.kts"))) {
            current = current.getParent();
        }
        if (current == null) {
            throw new IllegalStateException("Unable to locate repository root");
        }
        return current;
    }
}
