package net.xalcon.torchmaster.minecraft.light;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.minecraft.storage.LightStoreBridge;
import net.xalcon.torchmaster.minecraft.storage.MinecraftLightStoreAccess;

public final class BlockingLightLifecycle {
    private BlockingLightLifecycle() {
    }

    public static void register(World level, BlockPos pos, LightType lightType) {
        MinecraftLightStoreAccess.get(level)
                .ifPresent(registry -> register(registry, lightType.key(pos), lightType.createLight(pos)));
    }

    public static void unregister(World level, BlockPos pos, LightType lightType) {
        MinecraftLightStoreAccess.get(level)
                .ifPresent(registry -> unregister(registry, lightType.key(pos)));
    }

    static void register(LightStoreBridge registry, String lightKey, MinecraftBlockingLight light) {
        registry.registerLight(lightKey, light);
    }

    static void unregister(LightStoreBridge registry, String lightKey) {
        registry.unregisterLight(lightKey);
    }
}
