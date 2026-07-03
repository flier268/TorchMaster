package net.xalcon.torchmaster.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.Supplier;

public class ForgeRegistrationFactory extends AbstractForgeRegistrationFactory {

    @Override
    public <T> RegistrationProvider<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
        final DeferredRegister<T> register = DeferredRegister.create(resourceKey, modId);
        register.register(Objects.requireNonNull(modEventBus(modId)));
        return new Provider<>(modId, register);
    }

    private static class Provider<T> extends AbstractForgeRegistrationFactory.Provider<T> {
        private final DeferredRegister<T> registry;

        private Provider(String modId, DeferredRegister<T> registry) {
            super(modId);
            this.registry = registry;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier) {
            final net.minecraftforge.registries.RegistryObject<I> obj = registry.<I>register(name, supplier);
            final RegistryObject<I> ro = new RegistryObject<I>() {

                @Override
                public ResourceKey<I> getResourceKey() {
                    return (ResourceKey<I>)obj.getKey();
                }

                @Override
                public ResourceLocation getId() {
                    return obj.getId();
                }

                @Override
                public I get() {
                    return obj.get();
                }

            };
            return track(ro);
        }
    }
}
