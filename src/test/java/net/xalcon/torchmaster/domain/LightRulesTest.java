package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.BlockPosView;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.Vec3View;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LightRulesTest {
    @Test
    void megaTorchBlocksFilteredEntityInsideCubicRange() {
        EntityFilter filter = new EntityFilter();
        EntityTypeKey zombie = EntityTypeKey.parse("minecraft:zombie");
        filter.register(zombie);

        assertTrue(LightRules.blocksEntity(
                LightKind.MEGA_TORCH,
                filter,
                zombie,
                new Vec3View(3.5, 64.0, 3.5),
                new BlockPosView(0, 64, 0),
                4
        ));
    }

    @Test
    void cubicRangeIncludesBlockUpperEdge() {
        EntityFilter filter = new EntityFilter();
        EntityTypeKey zombie = EntityTypeKey.parse("minecraft:zombie");
        filter.register(zombie);

        assertTrue(LightRules.blocksEntity(
                LightKind.MEGA_TORCH,
                filter,
                zombie,
                new Vec3View(5.0, 69.0, 5.0),
                new BlockPosView(0, 64, 0),
                4
        ));
    }

    @Test
    void cubicRangeRejectsPositionOutsideRange() {
        EntityFilter filter = new EntityFilter();
        EntityTypeKey zombie = EntityTypeKey.parse("minecraft:zombie");
        filter.register(zombie);

        assertFalse(LightRules.blocksEntity(
                LightKind.MEGA_TORCH,
                filter,
                zombie,
                new Vec3View(5.01, 64.0, 0.5),
                new BlockPosView(0, 64, 0),
                4
        ));
    }

    @Test
    void cuboidRangeUsesSeparateAxisLimits() {
        EntityFilter filter = new EntityFilter();
        EntityTypeKey zombie = EntityTypeKey.parse("minecraft:zombie");
        filter.register(zombie);
        LightSettings settings = LightSettings.configured(true, 4, 1, 8);

        assertTrue(LightRules.blocksEntity(
                LightKind.MEGA_TORCH,
                filter,
                zombie,
                new Vec3View(4.5, 65.0, 8.5),
                new BlockPosView(0, 64, 0),
                settings
        ));
        assertFalse(LightRules.blocksEntity(
                LightKind.MEGA_TORCH,
                filter,
                zombie,
                new Vec3View(4.5, 66.01, 8.5),
                new BlockPosView(0, 64, 0),
                settings
        ));
    }

    @Test
    void disabledLightSettingsNeverBlock() {
        EntityFilter filter = new EntityFilter();
        EntityTypeKey zombie = EntityTypeKey.parse("minecraft:zombie");
        filter.register(zombie);
        LightSettings settings = LightSettings.configured(false, 64, 64, 64);

        assertFalse(LightRules.blocksEntity(
                LightKind.MEGA_TORCH,
                filter,
                zombie,
                new Vec3View(0.5, 64.0, 0.5),
                new BlockPosView(0, 64, 0),
                settings
        ));
        assertFalse(LightRules.blocksVillageSiege(
                LightKind.MEGA_TORCH,
                new Vec3View(0.5, 64.0, 0.5),
                new BlockPosView(0, 64, 0),
                settings
        ));
    }

    @Test
    void dreadLampDoesNotBlockUnfilteredEntity() {
        EntityFilter filter = new EntityFilter();

        assertFalse(LightRules.blocksEntity(
                LightKind.DREAD_LAMP,
                filter,
                EntityTypeKey.parse("minecraft:skeleton"),
                new Vec3View(1.0, 64.0, 1.0),
                new BlockPosView(0, 64, 0),
                8
        ));
    }

    @Test
    void dreadLampDoesNotBlockVillageSieges() {
        assertFalse(LightRules.blocksVillageSiege(
                LightKind.DREAD_LAMP,
                new Vec3View(1.0, 64.0, 1.0),
                new BlockPosView(0, 64, 0),
                8
        ));
    }

    @Test
    void megaTorchBlocksVillageSiegeInsideRange() {
        assertTrue(LightRules.blocksVillageSiege(
                LightKind.MEGA_TORCH,
                new Vec3View(1.0, 64.0, 1.0),
                new BlockPosView(0, 64, 0),
                8
        ));
    }

    @Test
    void naturalSpawnPositionRequiresNaturalSpawnOnlyMegaTorch() {
        assertTrue(LightRules.blocksNaturalSpawnPosition(
                LightKind.MEGA_TORCH,
                true,
                new Vec3View(1.0, 300.0, 1.0),
                new BlockPosView(0, 64, 0),
                8
        ));
        assertFalse(LightRules.blocksNaturalSpawnPosition(
                LightKind.MEGA_TORCH,
                false,
                new Vec3View(1.0, 64.0, 1.0),
                new BlockPosView(0, 64, 0),
                8
        ));
    }

    @Test
    void naturalSpawnChunkUsesChunkRadiusRoundedFromBlocks() {
        assertTrue(LightRules.blocksNaturalSpawnChunk(
                LightKind.MEGA_TORCH,
                true,
                4,
                0,
                new BlockPosView(0, 64, 0),
                64
        ));
        assertFalse(LightRules.blocksNaturalSpawnChunk(
                LightKind.MEGA_TORCH,
                true,
                5,
                0,
                new BlockPosView(0, 64, 0),
                64
        ));
    }

    @Test
    void naturalSpawnChunkZeroRadiusIsCurrentChunkOnly() {
        LightSettings settings = LightSettings.configured(true, 0, 1, 0);

        assertTrue(LightRules.blocksNaturalSpawnChunk(
                LightKind.MEGA_TORCH,
                true,
                0,
                0,
                new BlockPosView(0, 64, 0),
                settings
        ));
        assertFalse(LightRules.blocksNaturalSpawnChunk(
                LightKind.MEGA_TORCH,
                true,
                1,
                0,
                new BlockPosView(0, 64, 0),
                settings
        ));
    }

    @Test
    void naturalSpawnChunkUsesSeparateHorizontalAxisLimits() {
        LightSettings settings = LightSettings.configured(true, 48, 1, 32);

        assertTrue(LightRules.blocksNaturalSpawnChunk(
                LightKind.MEGA_TORCH,
                true,
                3,
                2,
                new BlockPosView(0, 64, 0),
                settings
        ));
        assertFalse(LightRules.blocksNaturalSpawnChunk(
                LightKind.MEGA_TORCH,
                true,
                4,
                2,
                new BlockPosView(0, 64, 0),
                settings
        ));
    }
}
