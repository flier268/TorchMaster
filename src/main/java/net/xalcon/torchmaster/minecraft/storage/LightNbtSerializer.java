package net.xalcon.torchmaster.minecraft.storage;

import net.minecraft.nbt.CompoundTag;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;

import java.util.Optional;

public interface LightNbtSerializer
{
    CompoundTag serializeLight(MinecraftBlockingLight light);
    Optional<MinecraftBlockingLight> deserializeLight(CompoundTag nbt);
    String getSerializerKey();
}
