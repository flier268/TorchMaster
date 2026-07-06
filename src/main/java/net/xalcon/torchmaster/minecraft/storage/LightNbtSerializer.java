package net.xalcon.torchmaster.minecraft.storage;

//? if >=1.16.5
import net.minecraft.nbt.NbtCompound;
//? if <1.16.5
//import net.minecraft.nbt.CompoundTag;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;

import java.util.Optional;

public interface LightNbtSerializer
{
    //? if >=1.16.5
    NbtCompound serializeLight(MinecraftBlockingLight light);
    //? if <1.16.5
    //CompoundTag serializeLight(MinecraftBlockingLight light);
    //? if >=1.16.5
    Optional<MinecraftBlockingLight> deserializeLight(NbtCompound nbt);
    //? if <1.16.5
    //Optional<MinecraftBlockingLight> deserializeLight(CompoundTag nbt);
    String getSerializerKey();
}
