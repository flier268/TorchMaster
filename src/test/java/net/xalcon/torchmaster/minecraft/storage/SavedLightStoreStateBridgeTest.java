package net.xalcon.torchmaster.minecraft.storage;

//? if >=1.16.5 {
import net.minecraft.nbt.NbtCompound;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}
import net.xalcon.torchmaster.domain.LightKind;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.minecraft.storage.PersistedLightEntry;
import net.xalcon.torchmaster.port.BlockPosView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SavedLightStoreStateBridgeTest
{
    private static final String SERIALIZER_KEY = "test_phase15_light";

    @BeforeAll
    static void registerSerializer()
    {
        if (!LightSerializerRegistry.getLightSerializer(SERIALIZER_KEY).isPresent()) {
            LightSerializerRegistry.registerLightSerializer(TestSerializer.INSTANCE);
        }
    }

    @Test
    void writeReturnsProvidedTag()
    {
        SavedLightStore store = new SavedLightStore();
        store.registerLight("MT_1_64_2", new TestLight(new BlockPosView(1, 64, 2)));
        //? if >=1.16.5
        NbtCompound tag = new NbtCompound();
        //? if <1.16.5
        //CompoundTag tag = new CompoundTag();

        assertSame(tag, SavedLightStoreStateBridge.write(store, tag));
    }

    @Test
    void readRestoresLightsIntoStore()
    {
        SavedLightStore store = new SavedLightStore();
        store.registerLight("MT_1_64_2", new TestLight(new BlockPosView(1, 64, 2)));
        //? if >=1.16.5
        NbtCompound tag = SavedLightStoreStateBridge.write(store, new NbtCompound());
        //? if <1.16.5
        //CompoundTag tag = SavedLightStoreStateBridge.write(store, new CompoundTag());

        SavedLightStore loaded = new SavedLightStore();
        SavedLightStoreStateBridge.read(loaded, tag);

        assertTrue(loaded.runtime().getLight("MT_1_64_2").isPresent());
    }

    @Test
    void existingTagAndStoreRouteRoundTrips()
    {
        SavedLightStore store = new SavedLightStore();
        store.registerLight("MT_1_64_2", new TestLight(new BlockPosView(1, 64, 2)));
        //? if >=1.16.5
        NbtCompound tag = new NbtCompound();
        //? if <1.16.5
        //CompoundTag tag = new CompoundTag();

        assertSame(tag, SavedLightStoreStateBridge.writeIntoExistingTag(store, tag));
        SavedLightStore loaded = new SavedLightStore();
        SavedLightStoreStateBridge.readIntoExistingStore(loaded, tag);

        assertTrue(loaded.runtime().getLight("MT_1_64_2").isPresent());
    }

    @Test
    void loadCreatesStoreAndRestoresLights()
    {
        SavedLightStore store = SavedLightStoreStateBridge.create();
        store.registerLight("MT_1_64_2", new TestLight(new BlockPosView(1, 64, 2)));
        //? if >=1.16.5
        NbtCompound tag = SavedLightStoreStateBridge.write(store, new NbtCompound());
        //? if <1.16.5
        //CompoundTag tag = SavedLightStoreStateBridge.write(store, new CompoundTag());

        SavedLightStore loaded = SavedLightStoreStateBridge.load(tag);

        assertTrue(loaded.runtime().getLight("MT_1_64_2").isPresent());
    }

    @Test
    void readIntoExistingStoreRestoresRuntime()
    {
        SavedLightStore store = new SavedLightStore();
        store.registerLight("MT_1_64_2", new TestLight(new BlockPosView(1, 64, 2)));
        //? if >=1.16.5
        NbtCompound tag = SavedLightStoreStateBridge.write(store, new NbtCompound());
        //? if <1.16.5
        //CompoundTag tag = SavedLightStoreStateBridge.write(store, new CompoundTag());

        SavedLightStore loaded = new SavedLightStore();
        SavedLightStoreStateBridge.readIntoExistingStore(loaded, tag);

        assertTrue(loaded.runtime().getLight("MT_1_64_2").isPresent());
    }

    private static final class TestLight implements MinecraftBlockingLight
    {
        private final BlockPosView position;

        private TestLight(BlockPosView position)
        {
            this.position = position;
        }

        @Override
        public String getLightSerializerType()
        {
            return SERIALIZER_KEY;
        }

        @Override
        public LightKind kind()
        {
            return LightKind.MEGA_TORCH;
        }

        @Override
        public BlockPosView position()
        {
            return position;
        }

        @Override
        public String displayName()
        {
            return "Test Light";
        }
    }

    private static final class TestSerializer implements LightNbtSerializer
    {
        private static final TestSerializer INSTANCE = new TestSerializer();

        private TestSerializer()
        {
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
            return Optional.of(new TestLight(new BlockPosView(0, 64, 0)));
        }

        @Override
        public String getSerializerKey()
        {
            return SERIALIZER_KEY;
        }
    }
}
