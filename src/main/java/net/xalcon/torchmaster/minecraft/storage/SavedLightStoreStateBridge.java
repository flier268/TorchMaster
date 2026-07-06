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

    //? if >=1.16.5
    static NbtCompound write(SavedLightStore store, NbtCompound tag)
    //? if <1.16.5
    //static CompoundTag write(SavedLightStore store, CompoundTag tag)
    {
        return SavedLightStoreNbtBridge.save(store.lights(), tag);
    }

    //? if >=1.16.5
    static void read(SavedLightStore store, NbtCompound tag)
    //? if <1.16.5
    //static void read(SavedLightStore store, CompoundTag tag)
    {
        SavedLightStoreNbtBridge.load(store.lights(), tag);
    }
}
