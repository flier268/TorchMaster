package net.xalcon.torchmaster.minecraft.storage;

//? if >=1.21.11
//import com.mojang.serialization.Codec;
//? if >=1.21.11
//import net.minecraft.datafixer.DataFixTypes;
//? if >=1.16.5
import net.minecraft.nbt.NbtCompound;
//? if >=1.20.6
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
//? if >=1.21.11
//import net.minecraft.world.PersistentStateType;

final class SavedLightStoreStateFactory
{
    private SavedLightStoreStateFactory()
    {
    }

    static SavedLightStore get(ServerWorld serverLevel, String storageId)
    {
        //? if >=1.21.11 {
        /*return serverLevel.getPersistentStateManager().getOrCreate(type(storageId));
        *///?} elif fabric && forge && >=1.21.5 {
        /*return serverLevel.getDataStorage().computeIfAbsent(type(storageId));
        *///?} elif >=1.20.6 {
        return serverLevel.getPersistentStateManager().getOrCreate(FACTORY, storageId);
        //?} elif >=1.17 {
        /*return serverLevel.getPersistentStateManager().getOrCreate(SavedLightStoreStateFactory::load, SavedLightStore::new, storageId);
        *///?} else {
        /*return serverLevel.getPersistentStateManager().getOrCreate(SavedLightStore::new, storageId);
        *///?}
    }

    //? if >=1.21.11 {
	    /*private static final Codec<SavedLightStore> CODEC = NbtCompound.CODEC.xmap(
	            SavedLightStoreStateFactory::load,
	            store -> {
	                NbtCompound tag = new NbtCompound();
	                store.writeState(tag);
	                return tag;
	            });

    static PersistentStateType<SavedLightStore> type(String id)
    {
        return new PersistentStateType<>(id, SavedLightStore::new, CODEC, DataFixTypes.SAVED_DATA_MAP_DATA);
    }
    *///?} elif >=1.20.6 {
    private static final PersistentState.Type<SavedLightStore> FACTORY = new PersistentState.Type<>(SavedLightStore::new, SavedLightStoreStateFactory::load, null);

    static SavedLightStore load(NbtCompound tag, RegistryWrapper.WrapperLookup provider)
    {
        return load(tag);
    }
    //?}

    //? if >=1.16.5 {
    static SavedLightStore load(NbtCompound tag)
    {
        SavedLightStore store = new SavedLightStore();
        store.readState(tag);
        return store;
    }
    //?}
}
