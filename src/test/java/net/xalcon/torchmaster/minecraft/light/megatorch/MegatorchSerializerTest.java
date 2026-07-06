package net.xalcon.torchmaster.minecraft.light.megatorch;

//? if >=1.16.5
import net.minecraft.nbt.NbtCompound;
//? if <1.16.5
//import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.xalcon.torchmaster.minecraft.storage.PersistedLightEntry;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MegatorchSerializerTest
{
    @Test
    void roundTripsDiamondBase()
    {
        MegatorchBlockingLight light = new MegatorchBlockingLight(new BlockPos(1, 64, 2), true);

        //? if >=1.16.5
        NbtCompound tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        Optional<PersistedLightEntry> loaded = MegatorchSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        MegatorchBlockingLight loadedLight = assertInstanceOf(MegatorchBlockingLight.class, loaded.get());
        assertTrue(loadedLight.hasDiamondBase());
        assertTrue(loadedLight.blocksNaturalSpawnsOnly());
    }

    @Test
    void missingDiamondBaseDefaultsToFalse()
    {
        MegatorchBlockingLight light = new MegatorchBlockingLight(new BlockPos(1, 64, 2));

        //? if >=1.16.5
        NbtCompound tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        tag.remove("diamondBase");

        Optional<PersistedLightEntry> loaded = MegatorchSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        MegatorchBlockingLight loadedLight = assertInstanceOf(MegatorchBlockingLight.class, loaded.get());
        assertFalse(loadedLight.hasDiamondBase());
        assertFalse(loadedLight.blocksNaturalSpawnsOnly());
    }
}
