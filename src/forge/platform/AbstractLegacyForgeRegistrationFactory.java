package net.xalcon.torchmaster.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

public abstract class AbstractLegacyForgeRegistrationFactory extends AbstractForgeRegistrationFactory {

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> RegistrationProvider<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
        DeferredRegister register = DeferredRegister.create(forgeRegistry(resourceKey), modId);
        register.register(Objects.requireNonNull(modEventBus(modId)));
        return provider(modId, register);
    }

    @SuppressWarnings("rawtypes")
    protected abstract <T> RegistrationProvider<T> provider(String modId, DeferredRegister register);

    @SuppressWarnings("rawtypes")
    protected IForgeRegistry forgeRegistry(ResourceKey<? extends Registry<?>> resourceKey) {
        String path = resourceKey.location().getPath();
        switch (path) {
            case "block":
                return ForgeRegistries.BLOCKS;
            case "item":
                return ForgeRegistries.ITEMS;
            case "block_entity_type":
                return blockEntityTypeRegistry();
            default:
                throw new IllegalArgumentException("Unsupported Forge registry " + resourceKey.location());
        }
    }

    @SuppressWarnings("rawtypes")
    protected abstract IForgeRegistry blockEntityTypeRegistry();
}
