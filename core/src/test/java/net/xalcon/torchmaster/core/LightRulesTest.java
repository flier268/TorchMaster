package net.xalcon.torchmaster.core;

import net.xalcon.torchmaster.adapter.BlockPosView;
import net.xalcon.torchmaster.adapter.EntityTypeKey;
import net.xalcon.torchmaster.adapter.Vec3View;
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
}
