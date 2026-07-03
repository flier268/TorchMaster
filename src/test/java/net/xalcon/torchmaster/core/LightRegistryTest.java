package net.xalcon.torchmaster.core;

import net.xalcon.torchmaster.adapter.BlockPosView;
import net.xalcon.torchmaster.adapter.ConfigView;
import net.xalcon.torchmaster.adapter.EntityTypeKey;
import net.xalcon.torchmaster.adapter.Vec3View;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LightRegistryTest {
    private static final EntityTypeKey ZOMBIE = EntityTypeKey.parse("minecraft:zombie");
    private static final EntityTypeKey SKELETON = EntityTypeKey.parse("minecraft:skeleton");

    @Test
    void emptyRegistryDoesNotBlock() {
        LightRegistry registry = new LightRegistry();

        assertFalse(registry.blocksEntity(filter(ZOMBIE), filter(SKELETON), config(), ZOMBIE, new Vec3View(0.5, 64, 0.5)));
        assertFalse(registry.blocksVillageSiege(config(), new Vec3View(0.5, 64, 0.5)));
    }

    @Test
    void registeredMegaTorchBlocksFilteredEntity() {
        LightRegistry registry = new LightRegistry();
        registry.register("mega", light(LightKind.MEGA_TORCH, 0, 64, 0));

        assertTrue(registry.blocksEntity(filter(ZOMBIE), filter(SKELETON), config(), ZOMBIE, new Vec3View(3.5, 64, 3.5)));
    }

    @Test
    void registeredDreadLampUsesDreadLampFilter() {
        LightRegistry registry = new LightRegistry();
        registry.register("dread", light(LightKind.DREAD_LAMP, 0, 64, 0));

        assertTrue(registry.blocksEntity(filter(ZOMBIE), filter(SKELETON), config(), SKELETON, new Vec3View(7.5, 64, 7.5)));
        assertFalse(registry.blocksEntity(filter(ZOMBIE), filter(SKELETON), config(), ZOMBIE, new Vec3View(7.5, 64, 7.5)));
    }

    @Test
    void unregisterRemovesLight() {
        LightRegistry registry = new LightRegistry();
        registry.register("mega", light(LightKind.MEGA_TORCH, 0, 64, 0));
        registry.unregister("mega");

        assertFalse(registry.get("mega").isPresent());
        assertFalse(registry.blocksEntity(filter(ZOMBIE), filter(SKELETON), config(), ZOMBIE, new Vec3View(3.5, 64, 3.5)));
    }

    @Test
    void megaTorchBlocksVillageSiegeButDreadLampDoesNot() {
        LightRegistry registry = new LightRegistry();
        registry.register("dread", light(LightKind.DREAD_LAMP, 0, 64, 0));
        assertFalse(registry.blocksVillageSiege(config(), new Vec3View(1.5, 64, 1.5)));

        registry.register("mega", light(LightKind.MEGA_TORCH, 0, 64, 0));
        assertTrue(registry.blocksVillageSiege(config(), new Vec3View(1.5, 64, 1.5)));
    }

    @Test
    void entriesExposeRegisteredLights() {
        LightRegistry registry = new LightRegistry();
        registry.register("mega", light(LightKind.MEGA_TORCH, 0, 64, 0));

        assertEquals(1, registry.entries().size());
        assertEquals("Mega Torch", registry.entries().iterator().next().displayName());
    }

    private static EntityFilter filter(EntityTypeKey entityType) {
        EntityFilter filter = new EntityFilter();
        filter.register(entityType);
        return filter;
    }

    private static ConfigView config() {
        return new ConfigView() {
            @Override
            public int feralFlareRadius() {
                return 16;
            }

            @Override
            public int dreadLampRadius() {
                return 8;
            }

            @Override
            public int megaTorchRadius() {
                return 4;
            }

            @Override
            public boolean aggressiveSpawnChecks() {
                return true;
            }

            @Override
            public boolean blockOnlyNaturalSpawns() {
                return true;
            }

            @Override
            public boolean blockVillageSieges() {
                return true;
            }
        };
    }

    private static LightEntry light(LightKind kind, int x, int y, int z) {
        return new LightEntry() {
            @Override
            public LightKind kind() {
                return kind;
            }

            @Override
            public BlockPosView position() {
                return new BlockPosView(x, y, z);
            }

            @Override
            public String displayName() {
                switch (kind) {
                    case MEGA_TORCH:
                        return "Mega Torch";
                    case DREAD_LAMP:
                        return "Dread Lamp";
                    default:
                        return "Light";
                }
            }
        };
    }
}
