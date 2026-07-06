package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.BlockPosView;
import net.xalcon.torchmaster.port.ConfigView;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.LightInfo;
import net.xalcon.torchmaster.port.SpawnReason;
import net.xalcon.torchmaster.port.Vec3View;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LightStoreRuntimeTest
{
    private static final EntityTypeKey ZOMBIE = EntityTypeKey.parse("minecraft:zombie");
    private static final EntityTypeKey SKELETON = EntityTypeKey.parse("minecraft:skeleton");

    @Test
    void emptyRuntimeDoesNotBlock()
    {
        LightStoreRuntime runtime = new LightStoreRuntime();

        assertFalse(runtime.shouldBlockEntity(context(filter(ZOMBIE), filter(SKELETON)), ZOMBIE, new Vec3View(0.5, 64, 0.5)));
        assertFalse(runtime.shouldBlockVillageSiege(context(filter(ZOMBIE), filter(SKELETON)), new Vec3View(0.5, 64, 0.5)));
    }

    @Test
    void megaTorchBlocksFilteredEntity()
    {
        LightStoreRuntime runtime = new LightStoreRuntime();
        runtime.registerLight("mega", light(LightKind.MEGA_TORCH, 0, 64, 0));

        assertTrue(runtime.shouldBlockEntity(context(filter(ZOMBIE), filter(SKELETON)), ZOMBIE, new Vec3View(3.5, 64, 3.5)));
    }

    @Test
    void diamondBaseMegaTorchBlocksNaturalPositionButNotSpawnerEntity()
    {
        LightStoreRuntime runtime = new LightStoreRuntime();
        runtime.registerLight("mega", light(LightKind.MEGA_TORCH, 0, 64, 0, true));

        LightStoreRuntimeContext context = context(filter(ZOMBIE), filter(SKELETON));
        assertTrue(runtime.shouldBlockNaturalSpawnPosition(context, new Vec3View(3.5, 64, 3.5)));
        assertTrue(runtime.shouldBlockEntity(context, ZOMBIE, new Vec3View(3.5, 64, 3.5), SpawnReason.NATURAL));
        assertFalse(runtime.shouldBlockEntity(context, ZOMBIE, new Vec3View(3.5, 64, 3.5), SpawnReason.SPAWNER));
    }

    @Test
    void diamondBaseMegaTorchBlocksNaturalSpawnChunks()
    {
        LightStoreRuntime runtime = new LightStoreRuntime();
        runtime.registerLight("mega", light(LightKind.MEGA_TORCH, 0, 64, 0, true));

        LightStoreRuntimeContext context = context(filter(ZOMBIE), filter(SKELETON));
        assertTrue(runtime.shouldBlockNaturalSpawnChunk(context, 1, 0));
        assertFalse(runtime.shouldBlockNaturalSpawnChunk(context, 2, 0));
    }

    @Test
    void dreadLampUsesDreadLampFilter()
    {
        LightStoreRuntime runtime = new LightStoreRuntime();
        runtime.registerLight("dread", light(LightKind.DREAD_LAMP, 0, 64, 0));

        assertTrue(runtime.shouldBlockEntity(context(filter(ZOMBIE), filter(SKELETON)), SKELETON, new Vec3View(7.5, 64, 7.5)));
        assertFalse(runtime.shouldBlockEntity(context(filter(ZOMBIE), filter(SKELETON)), ZOMBIE, new Vec3View(7.5, 64, 7.5)));
    }

    @Test
    void unregisterRemovesBlockingLight()
    {
        LightStoreRuntime runtime = new LightStoreRuntime();
        runtime.registerLight("mega", light(LightKind.MEGA_TORCH, 0, 64, 0));
        runtime.unregisterLight("mega");

        assertFalse(runtime.getLight("mega").isPresent());
        assertFalse(runtime.shouldBlockEntity(context(filter(ZOMBIE), filter(SKELETON)), ZOMBIE, new Vec3View(3.5, 64, 3.5)));
    }

    @Test
    void villageSiegeUsesMegaTorchOnly()
    {
        LightStoreRuntime runtime = new LightStoreRuntime();
        runtime.registerLight("dread", light(LightKind.DREAD_LAMP, 0, 64, 0));
        assertFalse(runtime.shouldBlockVillageSiege(context(filter(ZOMBIE), filter(SKELETON)), new Vec3View(1.5, 64, 1.5)));

        runtime.registerLight("mega", light(LightKind.MEGA_TORCH, 0, 64, 0));
        assertTrue(runtime.shouldBlockVillageSiege(context(filter(ZOMBIE), filter(SKELETON)), new Vec3View(1.5, 64, 1.5)));
    }

    @Test
    void entriesExposeRegisteredLights()
    {
        LightStoreRuntime runtime = new LightStoreRuntime();
        runtime.registerLight("mega", light(LightKind.MEGA_TORCH, 0, 64, 0));

        LightInfo[] entries = runtime.entries();
        assertEquals(1, entries.length);
        assertEquals("Mega Torch", entries[0].name());
        assertArrayEquals(new int[]{0, 64, 0}, new int[]{entries[0].position().x(), entries[0].position().y(), entries[0].position().z()});
    }

    private static LightStoreRuntimeContext context(EntityFilter megaTorchFilter, EntityFilter dreadLampFilter)
    {
        return new LightStoreRuntimeContext(config(), megaTorchFilter, dreadLampFilter);
    }

    private static EntityFilter filter(EntityTypeKey entityType)
    {
        EntityFilter filter = new EntityFilter();
        filter.register(entityType);
        return filter;
    }

    private static ConfigView config()
    {
        return new ConfigView()
        {
            @Override
            public int feralFlareRadius()
            {
                return 16;
            }

            @Override
            public int dreadLampRadius()
            {
                return 8;
            }

            @Override
            public int megaTorchRadius()
            {
                return 4;
            }

            @Override
            public boolean aggressiveSpawnChecks()
            {
                return true;
            }

            @Override
            public boolean blockOnlyNaturalSpawns()
            {
                return true;
            }

            @Override
            public boolean blockVillageSieges()
            {
                return true;
            }
        };
    }

    private static LightEntry light(LightKind kind, int x, int y, int z)
    {
        return light(kind, x, y, z, false);
    }

    private static LightEntry light(LightKind kind, int x, int y, int z, boolean naturalSpawnOnly)
    {
        return new LightEntry()
        {
            @Override
            public LightKind kind()
            {
                return kind;
            }

            @Override
            public BlockPosView position()
            {
                return new BlockPosView(x, y, z);
            }

            @Override
            public String displayName()
            {
                switch (kind) {
                    case MEGA_TORCH:
                        return "Mega Torch";
                    case DREAD_LAMP:
                        return "Dread Lamp";
                    default:
                    return "Light";
                }
            }

            @Override
            public boolean blocksNaturalSpawnsOnly()
            {
                return naturalSpawnOnly;
            }
        };
    }
}
