package net.xalcon.torchmaster.platform;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractForgeRegistrationFactory implements RegistrationProvider.Factory {

    protected IEventBus modEventBus(String modId) {
        Optional<? extends ModContainer> containerOpt = ModList.get().getModContainerById(modId);
        if (!containerOpt.isPresent())
            throw new NullPointerException("Cannot find mod container for id " + modId);

        ModContainer container = containerOpt.get();
        if (container instanceof FMLModContainer)
            return ((FMLModContainer)container).getEventBus();

        throw new ClassCastException("The container of the mod " + modId + " is not a FML one!");
    }

    protected abstract static class Provider<T> implements RegistrationProvider<T> {
        private final String modId;
        private final Set<RegistryObject<T>> entries = new HashSet<>();
        private final Set<RegistryObject<T>> entriesView = Collections.unmodifiableSet(entries);

        protected Provider(String modId) {
            this.modId = modId;
        }

        @Override
        public String getModId() {
            return modId;
        }

        @Override
        public Set<RegistryObject<T>> getEntries() {
            return entriesView;
        }

        @SuppressWarnings("unchecked")
        protected <I extends T> RegistryObject<I> track(RegistryObject<I> entry) {
            entries.add((RegistryObject<T>) entry);
            return entry;
        }
    }
}
