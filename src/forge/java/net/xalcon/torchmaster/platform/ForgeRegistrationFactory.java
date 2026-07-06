package net.xalcon.torchmaster.platform;

//? if >=1.19.3 {
/*import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import java.util.Objects;
*///?} else {
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
//?}
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

//? if >=1.19.3
/*public class ForgeRegistrationFactory extends AbstractForgeRegistrationFactory {*/
//? if <1.19.3
public class ForgeRegistrationFactory extends AbstractLegacyForgeRegistrationFactory {

    //? if >=1.19.3 {
    /*@Override
    public <T> RegistrationProvider<T> create(RegistryKey<? extends Registry<T>> resourceKey, String modId) {
        final DeferredRegister<T> register = DeferredRegister.create(resourceKey, modId);
        register.register(Objects.requireNonNull(modEventBus(modId)));
        return new Provider<>(modId, register);
    }
    *///?}

    //? if <1.19.3 {
    @SuppressWarnings("rawtypes")
    @Override
    protected IForgeRegistry blockEntityTypeRegistry() {
        //? if >=1.19
        /*return ForgeRegistries.BLOCK_ENTITY_TYPES;*/
        //? if >=1.17 <1.19
        //return ForgeRegistries.BLOCK_ENTITIES;
        //? if <1.17
        return ForgeRegistries.TILE_ENTITIES;
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected <T> RegistrationProvider<T> provider(String modId, DeferredRegister register) {
        return new Provider<>(modId, register);
    }
    //?}

    private static class Provider<T> extends AbstractForgeRegistrationFactory.Provider<T> {
        //? if >=1.19.3
        /*private final DeferredRegister<T> registry;*/
        //? if <1.19.3 {
        @SuppressWarnings("rawtypes")
        private final DeferredRegister registry;
        //?}

        //? if >=1.19.3 {
        /*private Provider(String modId, DeferredRegister<T> registry) {
            super(modId);
            this.registry = registry;
        }
        *///?} else {
        @SuppressWarnings("rawtypes")
        private Provider(String modId, DeferredRegister registry) {
            super(modId);
            this.registry = registry;
        }
        //?}

        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier) {
            //? if >=1.19.3
            /*final net.minecraftforge.registries.RegistryObject<I> obj = registry.<I>register(name, supplier);*/
            //? if >=1.18 <1.19.3
            //final net.minecraftforge.registries.RegistryObject obj = registry.register(name, supplier);
            //? if >=1.17 <1.18
            //final net.minecraftforge.fmllegacy.RegistryObject obj = registry.register(name, supplier);
            //? if <1.17
            final net.minecraftforge.fml.RegistryObject obj = registry.register(name, supplier);
            final RegistryObject<I> ro = new RegistryObject<I>() {

                @Override
                //? if >=1.19.3
                /*public RegistryKey<I> getResourceKey() {*/
                //? if <1.19.3
                public Object getResourceKey() {
                    //? if >=1.19.3
                    /*return (RegistryKey<I>) obj.getKey();*/
                    //? if <1.19.3
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

                //? if >=1.19.3 {
                /*@Override
                public RegistryEntry<I> asHolder() {
                    return (RegistryEntry<I>) obj.getHolder().orElseThrow();
                }
                *///?}
            };
            return track(ro);
        }
    }
}
