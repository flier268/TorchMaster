package net.xalcon.torchmaster.minecraft.storage;

//? if >=1.16.5 {
import net.minecraft.nbt.NbtCompound;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}

final class SavedLightStoreStateBridge
{
    private SavedLightStoreStateBridge()
    {
    }

    static SavedLightStore create()
    {
        return new SavedLightStore();
    }

    static SavedLightStore create(String storageId)
    {
        return new SavedLightStore(storageId);
    }

    //? if >=1.16.5
    static SavedLightStore load(NbtCompound tag)
    //? if <1.16.5
    //static SavedLightStore load(CompoundTag tag)
    {
        SavedLightStore store = create();
        read(store, tag);
        return store;
    }

    //? if >=1.16.5
    static NbtCompound write(SavedLightStore store)
    //? if <1.16.5
    //static CompoundTag write(SavedLightStore store)
    {
        //? if >=1.16.5
        return write(store, new NbtCompound());
        //? if <1.16.5
        //return write(store, new CompoundTag());
    }

    //? if >=1.16.5
    static NbtCompound write(SavedLightStore store, NbtCompound tag)
    //? if <1.16.5
    //static CompoundTag write(SavedLightStore store, CompoundTag tag)
    {
        return writeIntoExistingTag(store, tag);
    }

    //? if >=1.16.5
    static NbtCompound writeIntoExistingTag(SavedLightStore store, NbtCompound tag)
    //? if <1.16.5
    //static CompoundTag writeIntoExistingTag(SavedLightStore store, CompoundTag tag)
    {
        return SavedLightStoreNbtBridge.save(store.runtime(), tag);
    }

    //? if >=1.16.5
    static void read(SavedLightStore store, NbtCompound tag)
    //? if <1.16.5
    //static void read(SavedLightStore store, CompoundTag tag)
    {
        readIntoExistingStore(store, tag);
    }

    //? if >=1.16.5
    static void readIntoExistingStore(SavedLightStore store, NbtCompound tag)
    //? if <1.16.5
    //static void readIntoExistingStore(SavedLightStore store, CompoundTag tag)
    {
        SavedLightStoreNbtBridge.load(store.runtime(), tag);
    }
}
