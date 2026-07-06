package net.xalcon.torchmaster.minecraft.storage;

//? if >=1.16.5 {
import net.minecraft.nbt.NbtCompound;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}
import net.xalcon.torchmaster.domain.LightRegistry;

final class SavedLightStoreNbtBridge
{
    private SavedLightStoreNbtBridge()
    {
    }

    //? if >=1.16.5
    static NbtCompound save(LightRegistry lights, NbtCompound tag)
    //? if <1.16.5
    //static CompoundTag save(LightRegistry lights, CompoundTag tag)
    {
        SavedLightStoreSerializer.saveInto(tag, lights);
        return tag;
    }

    //? if >=1.16.5
    static void load(LightRegistry lights, NbtCompound tag)
    //? if <1.16.5
    //static void load(LightRegistry lights, CompoundTag tag)
    {
        SavedLightStoreSerializer.loadInto(lights, tag);
    }
}
