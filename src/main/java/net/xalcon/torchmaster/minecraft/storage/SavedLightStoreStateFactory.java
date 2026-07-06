package net.xalcon.torchmaster.minecraft.storage;

//? if >=1.21.11
//import com.mojang.serialization.Codec;
//? if >=1.21.11
//import net.minecraft.datafixer.DataFixTypes;
//? if >=1.16.5
import net.minecraft.nbt.NbtCompound;
//? if >=1.20.6
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.PersistentState;
//? if >=1.21.11
//import net.minecraft.world.PersistentStateType;

final class SavedLightStoreStateFactory
{
    private SavedLightStoreStateFactory()
    {
    }

    //? if >=1.21.11 {
    /*private static final Codec<SavedLightStore> CODEC = NbtCompound.CODEC.xmap(
            SavedLightStoreStateFactory::load,
            store -> {
                NbtCompound tag = new NbtCompound();
                store.saveInto(tag);
                return tag;
            });

    static PersistentStateType<SavedLightStore> type(String id)
    {
        return new PersistentStateType<>(id, SavedLightStore::new, CODEC, DataFixTypes.SAVED_DATA_MAP_DATA);
    }
    *///?} elif >=1.20.6 {
    static final PersistentState.Type<SavedLightStore> FACTORY = new PersistentState.Type<>(SavedLightStore::new, SavedLightStoreStateFactory::load, null);

    static SavedLightStore load(NbtCompound tag, RegistryWrapper.WrapperLookup provider)
    {
        return load(tag);
    }
    //?}

    //? if >=1.16.5 {
    static SavedLightStore load(NbtCompound tag)
    {
        SavedLightStore store = new SavedLightStore();
        store.loadFrom(tag);
        return store;
    }
    //?}
}
