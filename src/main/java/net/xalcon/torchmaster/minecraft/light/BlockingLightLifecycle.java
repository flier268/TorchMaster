package net.xalcon.torchmaster.minecraft.light;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.domain.LightControlState;
import net.xalcon.torchmaster.minecraft.storage.LightStoreBridge;
import net.xalcon.torchmaster.minecraft.storage.MinecraftLightStoreAccess;

import java.util.Optional;
import java.util.UUID;

public final class BlockingLightLifecycle {
    @FunctionalInterface
    public interface RemovalListener {
        void onRemoved(World level, BlockPos pos, LightType lightType);
    }

    private static RemovalListener removalListener = (level, pos, lightType) -> {};

    private BlockingLightLifecycle() {
    }

    public static void setRemovalListener(RemovalListener listener) {
        removalListener = listener == null ? (level, pos, lightType) -> {} : listener;
    }

    public static void register(World level, BlockPos pos, LightType lightType) {
        MinecraftLightStoreAccess.get(level)
                .ifPresent(registry -> register(registry, lightType.key(pos), lightType.createLight(pos)));
    }

    public static void register(World level, String lightKey, MinecraftBlockingLight light) {
        MinecraftLightStoreAccess.get(level)
                .ifPresent(registry -> register(registry, lightKey, light));
    }

    public static void register(World level, String lightKey, MinecraftBlockingLight light, UUID ownerUuid) {
        MinecraftLightStoreAccess.get(level)
                .ifPresent(registry -> register(registry, lightKey, light, Optional.ofNullable(ownerUuid)));
    }

    public static void unregister(World level, BlockPos pos, LightType lightType) {
        MinecraftLightStoreAccess.get(level)
                .ifPresent(registry -> {
                    if (unregister(registry, lightType.key(pos))) {
                        removalListener.onRemoved(level, pos, lightType);
                    }
                });
    }

    static void register(LightStoreBridge registry, String lightKey, MinecraftBlockingLight light) {
        register(registry, lightKey, light, Optional.empty());
    }

    static void register(LightStoreBridge registry, String lightKey, MinecraftBlockingLight light, Optional<UUID> ownerOverride) {
        Optional<MinecraftBlockingLight> existingLight = registry.getLight(lightKey)
                .filter(MinecraftBlockingLight.class::isInstance)
                .map(MinecraftBlockingLight.class::cast);
        Optional<UUID> ownerUuid = ownerOverride.isPresent()
                ? ownerOverride
                : existingLight.flatMap(MinecraftBlockingLight::ownerUuid);
        LightControlState controlState = existingLight
                .map(MinecraftBlockingLight::controlState)
                .orElse(light.controlState())
                .withOwner(ownerUuid);
        MinecraftBlockingLight registered = (MinecraftBlockingLight)light
                .withSettings(existingLight.map(MinecraftBlockingLight::settings).orElse(light.settings()));
        registered = registered.withControlState(controlState);
        registry.registerLight(lightKey, registered);
    }

    static boolean unregister(LightStoreBridge registry, String lightKey) {
        boolean existed = registry.getLight(lightKey).isPresent();
        registry.unregisterLight(lightKey);
        return existed;
    }
}
