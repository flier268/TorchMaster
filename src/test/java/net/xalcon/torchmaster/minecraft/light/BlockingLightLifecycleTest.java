package net.xalcon.torchmaster.minecraft.light;

import net.xalcon.torchmaster.domain.LightKind;
import net.xalcon.torchmaster.domain.LightEntry;
import net.xalcon.torchmaster.minecraft.storage.LightStoreBridge;
import net.xalcon.torchmaster.port.BlockPosView;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.LightInfo;
import net.xalcon.torchmaster.port.SpawnReason;
import net.xalcon.torchmaster.port.Vec3View;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

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
        };
    }

    private static final class FakeLightStore implements LightStoreBridge {
        private String registeredKey;
        private MinecraftBlockingLight registeredLight;
        private String unregisteredKey;

        @Override
        public boolean shouldBlockEntityType(EntityTypeKey entityType, Vec3View pos, SpawnReason spawnType) {
            return false;
        }

        @Override
        public boolean shouldBlockVillageZombieRaid(Vec3View pos) {
            return false;
        }

        @Override
        public void registerLight(String lightKey, LightEntry light) {
            this.registeredKey = lightKey;
            this.registeredLight = (MinecraftBlockingLight)light;
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
