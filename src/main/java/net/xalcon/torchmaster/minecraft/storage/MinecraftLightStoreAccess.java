package net.xalcon.torchmaster.minecraft.storage;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.Optional;

public final class MinecraftLightStoreAccess {
    private MinecraftLightStoreAccess() {
    }

    public static Optional<LightStoreBridge> get(World level) {
        if (level instanceof ServerWorld) {
            ServerWorld serverLevel = (ServerWorld)level;
            return Optional.of(SavedLightStoreStateFactory.get(serverLevel, storageId()));
        }
        return Optional.empty();
    }

    static String storageId() {
        return "torchmaster_lights";
    }
}
