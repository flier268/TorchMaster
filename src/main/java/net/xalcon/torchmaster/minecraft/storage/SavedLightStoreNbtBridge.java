package net.xalcon.torchmaster.minecraft.storage;

//? if >=1.16.5 {
import net.minecraft.nbt.NbtCompound;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}
import net.xalcon.torchmaster.domain.LightStoreRuntime;

final class SavedLightStoreNbtBridge
{
    private SavedLightStoreNbtBridge()
    {
    }

    //? if >=1.16.5
    static NbtCompound save(LightStoreRuntime runtime, NbtCompound tag)
    //? if <1.16.5
    //static CompoundTag save(LightStoreRuntime runtime, CompoundTag tag)
    {
        SavedLightStoreSerializer.saveInto(tag, runtime.registry());
        return tag;
    }

    //? if >=1.16.5
    static void load(LightStoreRuntime runtime, NbtCompound tag)
    //? if <1.16.5
    //static void load(LightStoreRuntime runtime, CompoundTag tag)
    {
        SavedLightStoreSerializer.loadInto(runtime.registry(), tag);
    }
}
