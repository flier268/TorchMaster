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
            String dimensionIdentifier = dimensionIdentifier(level);
            String storageId = storageId(dimensionIdentifier);
            //? if >=1.21.11 {
            /*return Optional.of(serverLevel.getPersistentStateManager().getOrCreate(SavedLightStore.type(storageId)));
            *///?} elif fabric && forge && >=1.21.5 {
            /*return Optional.of(serverLevel.getDataStorage().computeIfAbsent(SavedLightStore.type(storageId)));
            *///?} elif >=1.20.6 {
            return Optional.of(serverLevel.getPersistentStateManager().getOrCreate(SavedLightStore.Factory, storageId));
            //?} elif >=1.17 {
            /*return Optional.of(serverLevel.getPersistentStateManager().getOrCreate(SavedLightStore::load, SavedLightStore::new, storageId));
            *///?} else {
            /*return Optional.of(serverLevel.getPersistentStateManager().getOrCreate(SavedLightStore::new, storageId));
            *///?}
        }
        return Optional.empty();
    }

    static String storageId(String dimensionIdentifier) {
        return "torchmaster_lights_" + dimensionIdentifier;
    }

    private static String dimensionIdentifier(World level) {
        //? if fabric && forge && >=1.21.11 {
        /*return level.dimension().identifier().toDebugFileName();
        *///?} elif >=1.17 {
        return level.getRegistryKey().getValue().toUnderscoreSeparatedString();
        //?} else {
        /*return "overworld";
        *///?}
    }
}
