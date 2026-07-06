package net.xalcon.torchmaster.minecraft.spawn;

import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.port.ConfigView;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.EventResultPort;
import net.xalcon.torchmaster.port.SpawnReason;
import net.xalcon.torchmaster.port.Vec3View;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MinecraftSpawnEventHooksTest
{
    @Test
    void entityHookAppliesDenyToContainer()
    {
        EventResultContainer container = new EventResultContainer(EventResult.DEFAULT);

        MinecraftSpawnEventHooks.applyEntitySpawn(entityContext(EventResultPort.DEFAULT), () -> true, container,
                new TestServices(config(true, false, true)));

        assertEquals(EventResult.DENY, container.getResult());
    }

    @Test
    void entityHookLeavesDefaultWhenRuntimeSkips()
    {
        AtomicBoolean called = new AtomicBoolean(false);
        EventResultContainer container = new EventResultContainer(EventResult.DEFAULT);

        MinecraftSpawnEventHooks.applyEntitySpawn(entityContext(EventResultPort.DEFAULT, SpawnReason.SPAWN_EGG), () -> {
            called.set(true);
            return true;
        }, container, new TestServices(config(true, true, true)));

        assertEquals(EventResult.DEFAULT, container.getResult());
        assertFalse(called.get());
    }

    @Test
    void phantomHookPreservesExistingAllowWhenNotAggressive()
    {
        AtomicBoolean called = new AtomicBoolean(false);
        EventResultContainer container = new EventResultContainer(EventResult.ALLOW);

        MinecraftSpawnEventHooks.applyPhantomSpawn(phantomContext(EventResultPort.ALLOW), () -> {
            called.set(true);
            return true;
        }, container, new TestServices(config(false, false, true)));

        assertEquals(EventResult.ALLOW, container.getResult());
        assertFalse(called.get());
    }

    @Test
    void villageSiegeHookAppliesDenyWhenEnabled()
    {
        EventResultContainer container = new EventResultContainer(EventResult.DEFAULT);

        MinecraftSpawnEventHooks.applyVillageSiege(MinecraftSpawnEventContext.villageSiege(new Vec3View(1, 64, 2), EventResultPort.DEFAULT),
                () -> true, container, new TestServices(config(true, false, true)));

        assertEquals(EventResult.DENY, container.getResult());
    }

    private static MinecraftSpawnEventContext entityContext(EventResultPort result)
    {
        return entityContext(result, SpawnReason.NATURAL);
    }

    private static MinecraftSpawnEventContext entityContext(EventResultPort result, SpawnReason spawnReason)
    {
        return MinecraftSpawnEventContext.entity(new EntityTypeKey("minecraft", "zombie"), new Vec3View(1, 64, 2), spawnReason, result);
    }

    private static MinecraftSpawnEventContext phantomContext(EventResultPort result)
    {
        return MinecraftSpawnEventContext.phantom(new EntityTypeKey("minecraft", "phantom"), new Vec3View(1, 64, 2), SpawnReason.NATURAL, result);
    }

    private static ConfigView config(boolean aggressive, boolean blockOnlyNatural, boolean blockVillageSieges)
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
                return 32;
            }

            @Override
            public int megaTorchRadius()
            {
                return 64;
            }

            @Override
            public boolean aggressiveSpawnChecks()
            {
                return aggressive;
            }

            @Override
            public boolean blockOnlyNaturalSpawns()
            {
                return blockOnlyNatural;
            }

            @Override
            public boolean blockVillageSieges()
            {
                return blockVillageSieges;
            }
        };
    }

    private static final class TestServices implements MinecraftSpawnRuntimeServices
    {
        private final ConfigView config;

        private TestServices(ConfigView config)
        {
            this.config = config;
        }

        @Override
        public ConfigView config()
        {
            return config;
        }

        @Override
        public void debug(String message, Object... args)
        {
        }
    }
}
