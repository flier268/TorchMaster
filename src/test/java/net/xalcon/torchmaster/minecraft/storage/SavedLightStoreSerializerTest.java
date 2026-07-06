package net.xalcon.torchmaster.minecraft.storage;

//? if >=1.16.5 {
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
*///?}
import net.xalcon.torchmaster.domain.LightKind;
import net.xalcon.torchmaster.domain.LightRegistry;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.minecraft.storage.PersistedLightEntry;
import net.xalcon.torchmaster.port.BlockPosView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SavedLightStoreSerializerTest {
    private static final String SERIALIZER_KEY = "test_phase6_light";

    @BeforeAll
    static void registerSerializer() {
        if (!LightSerializerRegistry.getLightSerializer(SERIALIZER_KEY).isPresent()) {
            LightSerializerRegistry.registerLightSerializer(TestSerializer.INSTANCE);
        }
    }

    @Test
    void savesLightEntriesWithLegacyMetadataKeys() {
        LightRegistry registry = new LightRegistry();
        registry.register("MT_1_64_2", new TestLight(new BlockPosView(1, 64, 2), "Mega Torch"));
        registry.register("DL_3_65_4", new TestLight(new BlockPosView(3, 65, 4), "Dread Lamp"));
        //? if >=1.16.5
        NbtCompound tag = new NbtCompound();
        //? if <1.16.5
        //CompoundTag tag = new CompoundTag();

        SavedLightStoreSerializer.saveInto(tag, registry);

        //? if >=1.16.5
        NbtList lights = lightList(tag);
        //? if <1.16.5
        //ListTag lights = lightList(tag);
        assertEquals(2, lights.size());
        assertEquals("MT_1_64_2", stringValue(compoundAt(lights, 0), "_key"));
        assertEquals(SERIALIZER_KEY, stringValue(compoundAt(lights, 0), "_type"));
        assertEquals("DL_3_65_4", stringValue(compoundAt(lights, 1), "_key"));
        assertEquals(SERIALIZER_KEY, stringValue(compoundAt(lights, 1), "_type"));
    }

    @Test
    void loadsSavedLightEntriesBackIntoRegistry() {
        LightRegistry registry = new LightRegistry();
        registry.register("MT_1_64_2", new TestLight(new BlockPosView(1, 64, 2), "Mega Torch"));
        registry.register("DL_3_65_4", new TestLight(new BlockPosView(3, 65, 4), "Dread Lamp"));
        //? if >=1.16.5
        NbtCompound tag = new NbtCompound();
        //? if <1.16.5
        //CompoundTag tag = new CompoundTag();
        SavedLightStoreSerializer.saveInto(tag, registry);

        LightRegistry loaded = new LightRegistry();
        SavedLightStoreSerializer.loadInto(loaded, tag);

        assertTrue(loaded.get("MT_1_64_2").isPresent());
        assertTrue(loaded.get("DL_3_65_4").isPresent());
    }

    //? if >=1.16.5
    private static NbtList lightList(NbtCompound tag) {
    //? if <1.16.5
    //private static ListTag lightList(CompoundTag tag) {
        //? if >=1.21.11 {
        /*return tag.getListOrEmpty("lights");
        *///?} else if >=1.16.5 {
        return tag.getList("lights", 10);
        //?} else {
        /*return tag.getList("lights", 10);
        *///?}
    }

    //? if >=1.16.5
    private static NbtCompound compoundAt(NbtList list, int index) {
    //? if <1.16.5
    //private static CompoundTag compoundAt(ListTag list, int index) {
        //? if >=1.21.11 {
        /*return list.getCompoundOrEmpty(index);
        *///?} else if >=1.16.5 {
        return list.getCompound(index);
        //?} else {
        /*return list.getCompound(index);
        *///?}
    }

    //? if >=1.16.5
    private static String stringValue(NbtCompound tag, String key) {
    //? if <1.16.5
    //private static String stringValue(CompoundTag tag, String key) {
        //? if >=1.21.11 {
        /*return tag.getString(key, "");
        *///?} else if >=1.16.5 {
        return tag.getString(key);
        //?} else {
        /*return tag.getString(key);
        *///?}
    }

    private static final class TestLight implements MinecraftBlockingLight {
        private final BlockPosView position;
        private final String displayName;

        private TestLight(BlockPosView position, String displayName) {
            this.position = position;
            this.displayName = displayName;
        }

        @Override
        public String getLightSerializerType() {
            return SERIALIZER_KEY;
        }

        @Override
        public LightKind kind() {
            return LightKind.MEGA_TORCH;
        }

        @Override
        public BlockPosView position() {
            return position;
        }

        @Override
        public String displayName() {
            return displayName;
        }
    }

    private static final class TestSerializer implements LightNbtSerializer {
        private static final TestSerializer INSTANCE = new TestSerializer();

        private TestSerializer() {
        }

        @Override
        //? if >=1.16.5
        public NbtCompound serializeLight(PersistedLightEntry light) {
        //? if <1.16.5
        //public CompoundTag serializeLight(PersistedLightEntry light) {
            //? if >=1.16.5
            return new NbtCompound();
            //? if <1.16.5
            //return new CompoundTag();
        }

        @Override
        //? if >=1.16.5
        public Optional<PersistedLightEntry> deserializeLight(NbtCompound nbt) {
        //? if <1.16.5
        //public Optional<PersistedLightEntry> deserializeLight(CompoundTag nbt) {
            return Optional.of(new TestLight(new BlockPosView(0, 64, 0), "Loaded Light"));
        }

        @Override
        public String getSerializerKey() {
            return SERIALIZER_KEY;
        }
    }
}
