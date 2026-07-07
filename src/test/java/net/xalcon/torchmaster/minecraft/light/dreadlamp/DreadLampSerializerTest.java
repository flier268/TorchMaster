package net.xalcon.torchmaster.minecraft.light.dreadlamp;

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

class DreadLampSerializerTest
{
    @Test
    void roundTripsPerLightSettings()
    {
        DreadLampBlockingLight light = new DreadLampBlockingLight(new BlockPos(1, 64, 2),
                LightSettings.configured(false, 1, 2, 3, 4, 5, 6));

        //? if >=1.16.5
        NbtCompound tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        assertTrue(tag.contains("rangeWest"));
        assertTrue(tag.contains("rangeSouth"));
        Optional<PersistedLightEntry> loaded = DreadLampSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        DreadLampBlockingLight loadedLight = assertInstanceOf(DreadLampBlockingLight.class, loaded.get());
        assertTrue(loadedLight.settings().configured());
        assertFalse(loadedLight.settings().enabled());
        assertEquals(1, loadedLight.settings().rangeWest());
        assertEquals(2, loadedLight.settings().rangeEast());
        assertEquals(3, loadedLight.settings().rangeDown());
        assertEquals(4, loadedLight.settings().rangeUp());
        assertEquals(5, loadedLight.settings().rangeNorth());
        assertEquals(6, loadedLight.settings().rangeSouth());
    }

    @Test
    void missingPerLightSettingsStayUnconfigured()
    {
        DreadLampBlockingLight light = new DreadLampBlockingLight(new BlockPos(1, 64, 2));

        //? if >=1.16.5
        NbtCompound tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        Optional<PersistedLightEntry> loaded = DreadLampSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        DreadLampBlockingLight loadedLight = assertInstanceOf(DreadLampBlockingLight.class, loaded.get());
        assertFalse(loadedLight.settings().configured());
    }

    @Test
    void roundTripsOwnerUuid()
    {
        UUID ownerUuid = UUID.fromString("eeeb8156-99e7-468e-9215-0d5e3f24db86");
        DreadLampBlockingLight light = new DreadLampBlockingLight(new BlockPos(1, 64, 2),
                LightSettings.unconfigured(), Optional.of(ownerUuid));

        //? if >=1.16.5
        NbtCompound tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        Optional<PersistedLightEntry> loaded = DreadLampSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        DreadLampBlockingLight loadedLight = assertInstanceOf(DreadLampBlockingLight.class, loaded.get());
        assertEquals(Optional.of(ownerUuid), loadedLight.ownerUuid());
    }

    @Test
    void missingOwnerUuidStaysEmpty()
    {
        DreadLampBlockingLight light = new DreadLampBlockingLight(new BlockPos(1, 64, 2));

        //? if >=1.16.5
        NbtCompound tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        Optional<PersistedLightEntry> loaded = DreadLampSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        DreadLampBlockingLight loadedLight = assertInstanceOf(DreadLampBlockingLight.class, loaded.get());
        assertFalse(loadedLight.ownerUuid().isPresent());
    }

    @Test
    void roundTripsAllowedPlayers()
    {
        UUID first = UUID.fromString("abc22af7-6a56-4719-927a-9cf7c3a56b54");
        UUID second = UUID.fromString("65a760b7-a1d4-446c-a2e0-5c55f8b8c8c4");
        DreadLampBlockingLight light = new DreadLampBlockingLight(new BlockPos(1, 64, 2),
                LightSettings.unconfigured(), Optional.empty(), Arrays.asList(
                        new LightAccessEntry(first, "Kuku"),
                        new LightAccessEntry(second, "Alex")));

        //? if >=1.16.5
        NbtCompound tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        Optional<PersistedLightEntry> loaded = DreadLampSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        DreadLampBlockingLight loadedLight = assertInstanceOf(DreadLampBlockingLight.class, loaded.get());
        assertEquals(2, loadedLight.allowedPlayers().size());
        assertEquals(first, loadedLight.allowedPlayers().get(0).uuid());
        assertEquals("Kuku", loadedLight.allowedPlayers().get(0).name());
        assertEquals(second, loadedLight.allowedPlayers().get(1).uuid());
    }

    @Test
    void missingAllowedPlayersStayEmpty()
    {
        DreadLampBlockingLight light = new DreadLampBlockingLight(new BlockPos(1, 64, 2));

        //? if >=1.16.5
        NbtCompound tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        Optional<PersistedLightEntry> loaded = DreadLampSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        DreadLampBlockingLight loadedLight = assertInstanceOf(DreadLampBlockingLight.class, loaded.get());
        assertTrue(loadedLight.allowedPlayers().isEmpty());
    }

    @Test
    void roundTripsRangeVisible()
    {
        DreadLampBlockingLight light = new DreadLampBlockingLight(new BlockPos(1, 64, 2),
                LightSettings.unconfigured(), Optional.empty(), java.util.Collections.emptyList(), true);

        //? if >=1.16.5
        NbtCompound tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        Optional<PersistedLightEntry> loaded = DreadLampSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        DreadLampBlockingLight loadedLight = assertInstanceOf(DreadLampBlockingLight.class, loaded.get());
        assertTrue(loadedLight.rangeVisible());
    }

    @Test
    void missingRangeVisibleDefaultsToFalse()
    {
        DreadLampBlockingLight light = new DreadLampBlockingLight(new BlockPos(1, 64, 2));

        //? if >=1.16.5
        NbtCompound tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        //? if <1.16.5
        //CompoundTag tag = DreadLampSerializer.INSTANCE.serializeLight(light);
        Optional<PersistedLightEntry> loaded = DreadLampSerializer.INSTANCE.deserializeLight(tag);

        assertTrue(loaded.isPresent());
        DreadLampBlockingLight loadedLight = assertInstanceOf(DreadLampBlockingLight.class, loaded.get());
        assertFalse(loadedLight.rangeVisible());
    }
}
