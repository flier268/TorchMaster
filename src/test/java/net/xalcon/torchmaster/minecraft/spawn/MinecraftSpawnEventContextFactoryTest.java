package net.xalcon.torchmaster.minecraft.spawn;

import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.EventResultPort;
import net.xalcon.torchmaster.port.SpawnReason;
import net.xalcon.torchmaster.port.Vec3View;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MinecraftSpawnEventContextFactoryTest
{
    @Test
    void entityContextUsesConvertedIdentityReasonPositionAndResult()
    {
        MinecraftSpawnEventContext context = MinecraftSpawnEventContextFactory.entity(
                new EntityTypeKey("minecraft", "zombie"),
                new Vec3View(1.5, 64.0, -2.25),
                SpawnReason.NATURAL,
                EventResultPort.ALLOW);

        assertEquals(MinecraftSpawnEventContext.Kind.ENTITY, context.kind());
        assertEquals("minecraft", context.entityType().namespace());
        assertEquals("zombie", context.entityType().path());
        assertEquals(1.5, context.position().x(), 0.0001);
        assertEquals(64.0, context.position().y(), 0.0001);
        assertEquals(-2.25, context.position().z(), 0.0001);
        assertEquals(SpawnReason.NATURAL, context.spawnReason());
        assertEquals(EventResultPort.ALLOW, context.currentResult());
    }

    @Test
    void phantomContextUsesPhantomIdentity()
    {
        MinecraftSpawnEventContext context = MinecraftSpawnEventContextFactory.phantom(
                new EntityTypeKey("minecraft", "phantom"),
                new Vec3View(0, 70, 0),
                SpawnReason.NATURAL,
                EventResultPort.DEFAULT);

        assertEquals(MinecraftSpawnEventContext.Kind.PHANTOM, context.kind());
        assertEquals("minecraft", context.entityType().namespace());
        assertEquals("phantom", context.entityType().path());
        assertEquals(SpawnReason.NATURAL, context.spawnReason());
        assertEquals(EventResultPort.DEFAULT, context.currentResult());
    }

    @Test
    void villageSiegeContextHasNoEntityOrReason()
    {
        MinecraftSpawnEventContext context = MinecraftSpawnEventContextFactory.villageSiege(
                new Vec3View(-4, 65, 8),
                EventResultPort.DENY);

        assertEquals(MinecraftSpawnEventContext.Kind.VILLAGE_SIEGE, context.kind());
        assertNull(context.entityType());
        assertNull(context.spawnReason());
        assertEquals(-4, context.position().x(), 0.0001);
        assertEquals(65, context.position().y(), 0.0001);
        assertEquals(8, context.position().z(), 0.0001);
        assertEquals(EventResultPort.DENY, context.currentResult());
    }
}
