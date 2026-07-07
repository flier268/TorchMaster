package net.xalcon.torchmaster.minecraft.light.megatorch;

//? if >=1.16.5
import net.minecraft.nbt.NbtCompound;
//? if <1.16.5
//import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.xalcon.torchmaster.domain.LightSettings;
import net.xalcon.torchmaster.minecraft.storage.PersistedLightEntry;
import net.xalcon.torchmaster.port.LightAccessEntry;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void roundTripsPerLightSettings()
    {
        MegatorchBlockingLight light = new MegatorchBlockingLight(new BlockPos(1, 64, 2), true,
                LightSettings.configured(false, 1, 2, 3, 4, 5, 6));

        //? if >=1.16.5
        NbtCompound tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        assertTrue(tag.contains("rangeWest"));
        assertTrue(tag.contains("rangeSouth"));
        Optional<PersistedLightEntry> loaded = MegatorchSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        MegatorchBlockingLight loadedLight = assertInstanceOf(MegatorchBlockingLight.class, loaded.get());
        assertTrue(loadedLight.settings().configured());
        assertFalse(loadedLight.settings().enabled());
        assertEquals(1, loadedLight.settings().rangeWest());
        assertEquals(2, loadedLight.settings().rangeEast());
        assertEquals(3, loadedLight.settings().rangeDown());
        assertEquals(4, loadedLight.settings().rangeUp());
        assertEquals(5, loadedLight.settings().rangeNorth());
        assertEquals(6, loadedLight.settings().rangeSouth());
        assertTrue(loadedLight.hasDiamondBase());
    }

    @Test
    void missingPerLightSettingsStayUnconfigured()
    {
        MegatorchBlockingLight light = new MegatorchBlockingLight(new BlockPos(1, 64, 2));

        //? if >=1.16.5
        NbtCompound tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = MegatorchSerializer.INSTANCE.serializeLight(light);

        Optional<PersistedLightEntry> loaded = MegatorchSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        MegatorchBlockingLight loadedLight = assertInstanceOf(MegatorchBlockingLight.class, loaded.get());
        assertFalse(loadedLight.settings().configured());
    }

    @Test
    void roundTripsOwnerUuid()
    {
        UUID ownerUuid = UUID.fromString("4a95301d-dd8d-45ca-a52e-b1258936b620");
        MegatorchBlockingLight light = new MegatorchBlockingLight(new BlockPos(1, 64, 2), true,
                LightSettings.unconfigured(), Optional.of(ownerUuid));

        //? if >=1.16.5
        NbtCompound tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        Optional<PersistedLightEntry> loaded = MegatorchSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        MegatorchBlockingLight loadedLight = assertInstanceOf(MegatorchBlockingLight.class, loaded.get());
        assertEquals(Optional.of(ownerUuid), loadedLight.ownerUuid());
    }

    @Test
    void missingOwnerUuidStaysEmpty()
    {
        MegatorchBlockingLight light = new MegatorchBlockingLight(new BlockPos(1, 64, 2));

        //? if >=1.16.5
        NbtCompound tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        Optional<PersistedLightEntry> loaded = MegatorchSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        MegatorchBlockingLight loadedLight = assertInstanceOf(MegatorchBlockingLight.class, loaded.get());
        assertFalse(loadedLight.ownerUuid().isPresent());
    }

    @Test
    void roundTripsAllowedPlayers()
    {
        UUID first = UUID.fromString("828cc39b-f472-4066-91ea-3ae07fb8990b");
        UUID second = UUID.fromString("0b1ba2f5-f953-49da-8dd3-ac2f5db96e55");
        MegatorchBlockingLight light = new MegatorchBlockingLight(new BlockPos(1, 64, 2), true,
                LightSettings.unconfigured(), Optional.empty(), Arrays.asList(
                        new LightAccessEntry(first, "Kuku"),
                        new LightAccessEntry(second, "Alex")));

        //? if >=1.16.5
        NbtCompound tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        Optional<PersistedLightEntry> loaded = MegatorchSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        MegatorchBlockingLight loadedLight = assertInstanceOf(MegatorchBlockingLight.class, loaded.get());
        assertEquals(2, loadedLight.allowedPlayers().size());
        assertEquals(first, loadedLight.allowedPlayers().get(0).uuid());
        assertEquals("Kuku", loadedLight.allowedPlayers().get(0).name());
        assertEquals(second, loadedLight.allowedPlayers().get(1).uuid());
    }

    @Test
    void missingAllowedPlayersStayEmpty()
    {
        MegatorchBlockingLight light = new MegatorchBlockingLight(new BlockPos(1, 64, 2));

        //? if >=1.16.5
        NbtCompound tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        Optional<PersistedLightEntry> loaded = MegatorchSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        MegatorchBlockingLight loadedLight = assertInstanceOf(MegatorchBlockingLight.class, loaded.get());
        assertTrue(loadedLight.allowedPlayers().isEmpty());
    }

    @Test
    void roundTripsRangeVisible()
    {
        MegatorchBlockingLight light = new MegatorchBlockingLight(new BlockPos(1, 64, 2), true,
                LightSettings.unconfigured(), Optional.empty(), java.util.Collections.emptyList(), true);

        //? if >=1.16.5
        NbtCompound tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        Optional<PersistedLightEntry> loaded = MegatorchSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        MegatorchBlockingLight loadedLight = assertInstanceOf(MegatorchBlockingLight.class, loaded.get());
        assertTrue(loadedLight.rangeVisible());
    }

    @Test
    void missingRangeVisibleDefaultsToFalse()
    {
        MegatorchBlockingLight light = new MegatorchBlockingLight(new BlockPos(1, 64, 2));

        //? if >=1.16.5
        NbtCompound tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = MegatorchSerializer.INSTANCE.serializeLight(light);
        Optional<PersistedLightEntry> loaded = MegatorchSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        MegatorchBlockingLight loadedLight = assertInstanceOf(MegatorchBlockingLight.class, loaded.get());
        assertFalse(loadedLight.rangeVisible());
    }
}
