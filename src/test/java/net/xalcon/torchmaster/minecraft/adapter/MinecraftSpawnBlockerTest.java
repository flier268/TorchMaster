package net.xalcon.torchmaster.minecraft.adapter;

import net.xalcon.torchmaster.domain.LightEntry;
import net.xalcon.torchmaster.minecraft.storage.LightStoreBridge;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.LightInfo;
import net.xalcon.torchmaster.port.SpawnReason;
import net.xalcon.torchmaster.port.Vec3View;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinecraftSpawnBlockerTest {
    @Test
    void entityCheckDelegatesToLightStore() {
        FakeLightStore store = new FakeLightStore(true, false);
        EntityTypeKey entityType = EntityTypeKey.parse("minecraft:zombie");
        Vec3View position = new Vec3View(1.5, 64.0, -2.5);

        assertTrue(MinecraftSpawnBlocker.shouldBlockEntity(store, entityType, position, SpawnReason.NATURAL));
        assertEquals(entityType, store.entityType);
        assertSame(position, store.position);
        assertEquals(SpawnReason.NATURAL, store.spawnReason);
    }

    @Test
    void villageSiegeCheckDelegatesToLightStore() {
        FakeLightStore store = new FakeLightStore(false, true);
        Vec3View position = new Vec3View(8.5, 64.0, 8.5);

        assertTrue(MinecraftSpawnBlocker.shouldBlockVillageSiege(store, position));
        assertSame(position, store.villageSiegePosition);
    }

    @Test
    void returnsStoreDecisionWithoutForcingDeny() {
        FakeLightStore store = new FakeLightStore(false, false);

        assertFalse(MinecraftSpawnBlocker.shouldBlockEntity(
                store,
                EntityTypeKey.parse("minecraft:skeleton"),
                new Vec3View(0.0, 64.0, 0.0),
                SpawnReason.SPAWNER));
    }

    private static final class FakeLightStore implements LightStoreBridge {
        private final boolean blockEntity;
        private final boolean blockVillageSiege;
        private EntityTypeKey entityType;
        private Vec3View position;
        private SpawnReason spawnReason;
        private Vec3View villageSiegePosition;

        private FakeLightStore(boolean blockEntity, boolean blockVillageSiege) {
            this.blockEntity = blockEntity;
            this.blockVillageSiege = blockVillageSiege;
        }

        @Override
        public boolean shouldBlockEntityType(EntityTypeKey entityType, Vec3View pos, SpawnReason spawnType) {
            this.entityType = entityType;
            this.position = pos;
            this.spawnReason = spawnType;
            return blockEntity;
        }

        @Override
        public boolean shouldBlockVillageZombieRaid(Vec3View pos) {
            this.villageSiegePosition = pos;
            return blockVillageSiege;
        }

        @Override
        public void registerLight(String lightKey, LightEntry light) {
        }

        @Override
        public void unregisterLight(String lightKey) {
        }

        @Override
        public LightInfo[] getEntries() {
            return new LightInfo[0];
        }
    }

}
