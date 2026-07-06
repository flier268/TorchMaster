package net.xalcon.torchmaster.minecraft.storage;

//? if >=1.16.5
import net.minecraft.nbt.NbtCompound;
//? if <1.16.5
//import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public interface LightNbtSerializer
{
    //? if >=1.16.5
    NbtCompound serializeLight(PersistedLightEntry light);
    //? if <1.16.5
    //CompoundTag serializeLight(PersistedLightEntry light);
    //? if >=1.16.5
    Optional<PersistedLightEntry> deserializeLight(NbtCompound nbt);
    //? if <1.16.5
    //Optional<PersistedLightEntry> deserializeLight(CompoundTag nbt);
    String getSerializerKey();
}
