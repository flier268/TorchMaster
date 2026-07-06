package net.xalcon.torchmaster.minecraft.storage;

import net.xalcon.torchmaster.domain.LightEntry;

public interface PersistedLightEntry extends LightEntry
{
    String getLightSerializerType();
}
