package net.xalcon.torchmaster.platform;

//? if forge && <1.19.3 {
/*import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public abstract class AbstractLegacyForgeRegistrationFactory extends AbstractForgeRegistrationFactory {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> RegistrationProvider<T> create(Object resourceKey, String modId) {
        //? if >=1.15.2 {
        DeferredRegister register = DeferredRegister.create(forgeRegistry(resourceKey), modId);
        //?} else {
        /^DeferredRegister register = new DeferredRegister(forgeRegistry(resourceKey), modId);^/
        //?}
        register.register(Objects.requireNonNull(modEventBus(modId)));
        return provider(modId, register);
    }

    @SuppressWarnings("rawtypes")
    protected abstract <T> RegistrationProvider<T> provider(String modId, DeferredRegister register);

    @SuppressWarnings("rawtypes")
    protected IForgeRegistry forgeRegistry(Object resourceKey) {
        if(resourceKey == Registry.ITEM)
            return ForgeRegistries.ITEMS;
        if(resourceKey == Registry.BLOCK)
            return ForgeRegistries.BLOCKS;
        //? if >=1.15 {
        if(resourceKey == Registry.BLOCK_ENTITY_TYPE)
            return blockEntityTypeRegistry();
        //?} else {
        /^if(resourceKey == Registry.BLOCK_ENTITY)
            return blockEntityTypeRegistry();
        ^///?}

        throw new IllegalArgumentException("Unsupported Forge registry " + resourceKey);
    }

    @SuppressWarnings("rawtypes")
    protected abstract IForgeRegistry blockEntityTypeRegistry();
}
*///?}
