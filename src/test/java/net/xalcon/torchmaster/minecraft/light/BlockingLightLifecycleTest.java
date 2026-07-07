package net.xalcon.torchmaster.minecraft.light;

import net.xalcon.torchmaster.domain.LightKind;
import net.xalcon.torchmaster.domain.LightEntry;
import net.xalcon.torchmaster.domain.LightSettings;
import net.xalcon.torchmaster.minecraft.light.megatorch.MegatorchBlockingLight;
import net.xalcon.torchmaster.minecraft.storage.LightStoreBridge;
import net.xalcon.torchmaster.port.BlockPosView;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.LightAccessEntry;
import net.xalcon.torchmaster.port.LightInfo;
import net.xalcon.torchmaster.port.SpawnReason;
import net.xalcon.torchmaster.port.Vec3View;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockingLightLifecycleTest {
    @Test
    void registerDelegatesKeyAndLightToStore() {
        FakeLightStore store = new FakeLightStore();
        MinecraftBlockingLight light = light();

        BlockingLightLifecycle.register(store, "MT_1_64_2", light);

        assertEquals("MT_1_64_2", store.registeredKey);
        assertSame(light, store.registeredLight);
    }

    @Test
    void unregisterDelegatesKeyToStore() {
        FakeLightStore store = new FakeLightStore();

        BlockingLightLifecycle.unregister(store, "DL_3_64_4");

        assertEquals("DL_3_64_4", store.unregisteredKey);
    }

    @Test
    void unregisterReportsWhetherLightExisted() {
        FakeLightStore store = new FakeLightStore();

        assertFalse(BlockingLightLifecycle.unregister(store, "DL_3_64_4"));

        store.existingLight = light();
        assertTrue(BlockingLightLifecycle.unregister(store, "DL_3_64_4"));
    }

    @Test
    void registerPreservesExistingOwnerWhenOwnerNotSpecified() {
        UUID ownerUuid = UUID.fromString("314ec87f-6d36-4820-b958-2924005c7388");
        UUID allowedUuid = UUID.fromString("8d958391-9fd6-48f0-9f3a-8e1da2781870");
        FakeLightStore store = new FakeLightStore();
        store.existingLight = new MegatorchBlockingLight(new BlockPos(1, 64, 2), false,
                LightSettings.configured(false, 1, 2, 3, 4, 5, 6), Optional.of(ownerUuid),
                java.util.Collections.singletonList(new LightAccessEntry(allowedUuid, "Alex")), true);

        BlockingLightLifecycle.register(store, "MT_1_64_2", new MegatorchBlockingLight(new BlockPos(1, 64, 2), true));

        assertEquals(Optional.of(ownerUuid), store.registeredLight.ownerUuid());
        assertEquals(allowedUuid, store.registeredLight.allowedPlayers().get(0).uuid());
        assertEquals(1, store.registeredLight.settings().rangeWest());
        assertTrue(store.registeredLight.blocksNaturalSpawnsOnly());
        assertTrue(store.registeredLight.rangeVisible());
    }

    @Test
    void registerUsesOwnerOverrideForPlacedLight() {
        UUID ownerUuid = UUID.fromString("e6be6e34-e1fd-492a-bd0e-4e5e8021bf7c");
        FakeLightStore store = new FakeLightStore();

        BlockingLightLifecycle.register(store, "MT_1_64_2", new MegatorchBlockingLight(new BlockPos(1, 64, 2)),
                Optional.of(ownerUuid));

        assertEquals(Optional.of(ownerUuid), store.registeredLight.ownerUuid());
    }

    private static MinecraftBlockingLight light() {
        return new MinecraftBlockingLight() {
            @Override
            public String getLightSerializerType() {
                return "test";
            }

            @Override
            public LightKind kind() {
                return LightKind.MEGA_TORCH;
            }

            @Override
            public BlockPosView position() {
                return new BlockPosView(1, 64, 2);
            }

            @Override
            public String displayName() {
                return "Mega Torch";
            }

            @Override
            public MinecraftBlockingLight withSettings(LightSettings settings) {
                return this;
            }

            @Override
            public MinecraftBlockingLight withOwner(Optional<UUID> ownerUuid) {
                return this;
            }
        };
    }

    private static final class FakeLightStore implements LightStoreBridge {
        private String registeredKey;
        private MinecraftBlockingLight registeredLight;
        private String unregisteredKey;
        private MinecraftBlockingLight existingLight;

        @Override
        public boolean shouldBlockEntityType(EntityTypeKey entityType, Vec3View pos, SpawnReason spawnType) {
            return false;
        }

        @Override
        public boolean shouldBlockVillageZombieRaid(Vec3View pos) {
            return false;
        }

        @Override
        public boolean shouldBlockNaturalSpawnPosition(Vec3View pos) {
            return false;
        }

        @Override
        public boolean shouldBlockNaturalSpawnChunk(int chunkX, int chunkZ) {
            return false;
        }

        @Override
        public void registerLight(String lightKey, LightEntry light) {
            this.registeredKey = lightKey;
            this.registeredLight = (MinecraftBlockingLight)light;
        }

        @Override
        public Optional<LightEntry> getLight(String lightKey) {
            return Optional.ofNullable(existingLight);
        }

        @Override
        public void unregisterLight(String lightKey) {
            this.unregisteredKey = lightKey;
        }

        @Override
        public LightInfo[] getEntries() {
            return new LightInfo[0];
        }
    }
}
