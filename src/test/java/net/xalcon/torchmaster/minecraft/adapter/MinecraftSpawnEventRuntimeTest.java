package net.xalcon.torchmaster.minecraft.adapter;

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
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinecraftSpawnEventRuntimeTest
{
    @Test
    void entitySpawnSkipDoesNotCallBlockCheck()
    {
        AtomicBoolean called = new AtomicBoolean(false);
        MinecraftSpawnEventContext context = MinecraftSpawnEventContext.entity(
                new EntityTypeKey("minecraft", "zombie"),
                new Vec3View(1, 64, 2),
                SpawnReason.SPAWN_EGG,
                EventResultPort.DEFAULT);

        boolean denied = MinecraftSpawnEventRuntime.shouldDenyEntitySpawn(context, () -> {
            called.set(true);
            return true;
        }, new TestServices(config(true, false, true)));

        assertFalse(denied);
        assertFalse(called.get());
    }

    @Test
    void entitySpawnDeniesWhenBlockCheckMatches()
    {
        MinecraftSpawnEventContext context = MinecraftSpawnEventContext.entity(
                new EntityTypeKey("minecraft", "zombie"),
                new Vec3View(1, 64, 2),
                SpawnReason.NATURAL,
                EventResultPort.DEFAULT);

        assertTrue(MinecraftSpawnEventRuntime.shouldDenyEntitySpawn(context, () -> true, new TestServices(config(true, false, true))));
    }

    @Test
    void phantomSpawnRespectsExistingAllowWhenNotAggressive()
    {
        AtomicBoolean called = new AtomicBoolean(false);
        MinecraftSpawnEventContext context = MinecraftSpawnEventContext.phantom(
                new EntityTypeKey("minecraft", "phantom"),
                new Vec3View(1, 64, 2),
                SpawnReason.NATURAL,
                EventResultPort.ALLOW);

        boolean denied = MinecraftSpawnEventRuntime.shouldDenyPhantomSpawn(context, () -> {
            called.set(true);
            return true;
        }, new TestServices(config(false, false, true)));

        assertFalse(denied);
        assertFalse(called.get());
    }

    @Test
    void villageSiegeSkipRespectsDisabledConfig()
    {
        AtomicBoolean called = new AtomicBoolean(false);
        MinecraftSpawnEventContext context = MinecraftSpawnEventContext.villageSiege(
                new Vec3View(1, 64, 2),
                EventResultPort.DEFAULT);

        boolean denied = MinecraftSpawnEventRuntime.shouldDenyVillageSiege(context, () -> {
            called.set(true);
            return true;
        }, new TestServices(config(true, false, false)));

        assertFalse(denied);
        assertFalse(called.get());
    }

    @Test
    void applyDenyOnlyChangesContainerWhenDenied()
    {
        EventResultContainer container = new EventResultContainer(EventResult.DEFAULT);

        MinecraftSpawnEventRuntime.applyDeny(container, false);
        assertEquals(EventResult.DEFAULT, container.getResult());

        MinecraftSpawnEventRuntime.applyDeny(container, true);
        assertEquals(EventResult.DENY, container.getResult());
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

    private static final class TestServices implements MinecraftSpawnEventRuntime.RuntimeServices
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
