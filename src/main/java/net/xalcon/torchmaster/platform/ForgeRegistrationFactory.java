package net.xalcon.torchmaster.platform;

//? if forge && >=1.19.3 {
/*import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.Supplier;

public class ForgeRegistrationFactory extends AbstractForgeRegistrationFactory {

    @Override
    public <T> RegistrationProvider<T> create(RegistryKey<? extends Registry<T>> resourceKey, String modId) {
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
                public RegistryKey<I> getResourceKey() {
                    return (RegistryKey<I>)obj.getKey();
                }

                @Override
                public Identifier getId() {
                    return obj.getId();
                }

                @Override
                public I get() {
                    return obj.get();
                }

                @Override
                public RegistryEntry<I> asHolder() {
                    return (RegistryEntry<I>) obj.getHolder().orElseThrow();
                }
            };
            return track(ro);
        }
    }
}
*///?} else if forge && >=1.19 {
/*import net.minecraft.util.Identifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ForgeRegistrationFactory extends AbstractLegacyForgeRegistrationFactory {

    @SuppressWarnings("rawtypes")
    @Override
    protected IForgeRegistry blockEntityTypeRegistry() {
        return ForgeRegistries.BLOCK_ENTITY_TYPES;
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
            final net.minecraftforge.registries.RegistryObject obj = registry.register(name, supplier);
            final RegistryObject<I> ro = new RegistryObject<I>() {

                @Override
                public Object getResourceKey() {
                    return null;
                }

                @Override
                public Identifier getId() {
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
*///?} else if forge && >=1.18 {
/*import net.minecraft.util.Identifier;
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
            final net.minecraftforge.registries.RegistryObject obj = registry.register(name, supplier);
            final RegistryObject<I> ro = new RegistryObject<I>() {

                @Override
                public Object getResourceKey() {
                    return null;
                }

                @Override
                public Identifier getId() {
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
*///?} else if forge && >=1.17 {
/*import net.minecraft.util.Identifier;
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
                public Object getResourceKey() {
                    return null;
                }

                @Override
                public Identifier getId() {
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
*///?} else if forge {
/*import net.minecraft.util.Identifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ForgeRegistrationFactory extends AbstractLegacyForgeRegistrationFactory {

    @SuppressWarnings("rawtypes")
    @Override
    protected IForgeRegistry blockEntityTypeRegistry() {
        return ForgeRegistries.TILE_ENTITIES;
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
            final net.minecraftforge.fml.RegistryObject obj = registry.register(name, supplier);
            final RegistryObject<I> ro = new RegistryObject<I>() {

                @Override
                public Object getResourceKey() {
                    return null;
                }

                @Override
                public Identifier getId() {
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
*///?}
