package net.xalcon.torchmaster.minecraft.spawn;

import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.port.EventResultPort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinecraftSpawnEventContainersTest
{
    @Test
    void defaultContainerStartsDefault()
    {
        EventResultContainer container = MinecraftSpawnEventContainers.defaultContainer();

        assertEquals(EventResult.DEFAULT, MinecraftSpawnEventContainers.result(container));
        assertEquals(EventResultPort.DEFAULT, MinecraftSpawnEventContainers.portResult(container));
    }

    @Test
    void invokeDefaultReturnsMutatedResult()
    {
        EventResult result = MinecraftSpawnEventContainers.invokeDefault(container ->
                container.setResult(EventResult.ALLOW));

        assertEquals(EventResult.ALLOW, result);
    }

    @Test
    void deniesReadsContainerResult()
    {
        assertFalse(MinecraftSpawnEventContainers.denies(MinecraftSpawnEventContainers.container(EventResult.ALLOW)));
        assertTrue(MinecraftSpawnEventContainers.denies(MinecraftSpawnEventContainers.container(EventResult.DENY)));
    }

    @Test
    void deniesDefaultReturnsCallbackDecision()
    {
        assertTrue(MinecraftSpawnEventContainers.deniesDefault(container ->
                container.setResult(EventResult.DENY)));
        assertFalse(MinecraftSpawnEventContainers.deniesDefault(container -> {
        }));
    }
}
