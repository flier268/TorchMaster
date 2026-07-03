package net.xalcon.torchmaster.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ForgeRegistrationFactory extends AbstractLegacyForgeRegistrationFactory {

    @SuppressWarnings("rawtypes")
    @Override
    protected IForgeRegistry blockEntityTypeRegistry() {
        return ForgeRegistries.BLOCK_ENTITIES;
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected <T> RegistrationProvider<T> provider(String modId, DeferredRegister register) {
        return new Provider<>(modId, register);
    }

    private static class Provider<T> extends AbstractForgeRegistrationFactory.Provider<T> {
        @SuppressWarnings("rawtypes")
        private final DeferredRegister registry;

        @SuppressWarnings("rawtypes")
        private Provider(String modId, DeferredRegister registry) {
            super(modId);
            this.registry = registry;
        }

        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier) {
            final net.minecraftforge.fmllegacy.RegistryObject obj = registry.register(name, supplier);
            final RegistryObject<I> ro = new RegistryObject<I>() {

                @Override
                public ResourceKey<I> getResourceKey() {
                    return null;
                }

                @Override
                public ResourceLocation getId() {
                    return obj.getId();
                }

                @Override
                public I get() {
                    return (I)obj.get();
                }
            };
            return track(ro);
        }
    }
}
