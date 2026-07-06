package net.xalcon.torchmaster.platform;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
//? if >=1.19.3 {
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
//?}
//? if >=1.19.3
import net.minecraft.registry.Registry;
//? if <1.19.3
//import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;

public class FabricRegistrationFactory implements RegistrationProvider.Factory {

    //? if >=1.19.3 {
    @Override
    public <T> RegistrationProvider<T> create(RegistryKey<? extends Registry<T>> resourceKey, String modId) {
        return new Provider<>(modId, resourceKey);
    }
    //?} else {
    /*@Override
    @SuppressWarnings("unchecked")
    public <T> RegistrationProvider<T> create(Object resourceKey, String modId) {
        return new Provider<>(modId, (Registry<T>) resourceKey);
    }
    *///?}

    @Override
    public <T> RegistrationProvider<T> create(Registry<T> registry, String modId) {
        return new Provider<>(modId, registry);
    }

    private static class Provider<T> implements RegistrationProvider<T> {
        private final String modId;
        private final Registry<T> registry;

        private final Set<RegistryObject<T>> entries = new HashSet<>();
        private final Set<RegistryObject<T>> entriesView = Collections.unmodifiableSet(entries);

        //? if >=1.19.3 {
        @SuppressWarnings({"unchecked"})
        private Provider(String modId, RegistryKey<? extends Registry<T>> key) {
            this.modId = modId;

            final var reg = Registries.REGISTRIES.get(key.getValue());
            if (reg == null) {
                throw new RuntimeException("Registry with name " + key.getValue() + " was not found!");
            }
            registry = (Registry<T>) reg;
        }
        //?}

        private Provider(String modId, Registry<T> registry) {
            this.modId = modId;
            this.registry = registry;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier) {
            //? if >=1.19.3 {
            final var rl = Identifier.of(modId, name);
            final var obj = Registry.register(registry, rl, supplier.get());
            //?} else {
            /*final Identifier rl = new Identifier(modId, name);
            final I obj = Registry.register(registry, rl, supplier.get());
            *///?}
            final RegistryObject<I> ro = new RegistryObject<I>() {
                //? if >=1.19.3
                final RegistryKey<I> key = RegistryKey.of((RegistryKey<? extends Registry<I>>) registry.getKey(), rl);

                @Override
                //? if >=1.19.3
                public RegistryKey<I> getResourceKey() {
                //? if <1.19.3
                //public Object getResourceKey() {
                    //? if >=1.19.3
                    return key;
                    //? if <1.19.3
                    //return rl;
                }

                @Override
                public Identifier getId() {
                    return rl;
                }

                @Override
                public I get() {
                    return obj;
                }

                //? if >=1.19.3 {
                @Override
                public RegistryEntry<I> asHolder() {
                    return (RegistryEntry<I>) registry.getEntry(obj);
                }
                //?}
            };
            entries.add((RegistryObject<T>) ro);
            return ro;
        }

        @Override
        public Collection<RegistryObject<T>> getEntries() {
            return entriesView;
        }

        @Override
        public String getModId() {
            return modId;
        }
    }
}
