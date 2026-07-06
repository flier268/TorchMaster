package net.xalcon.torchmaster.minecraft.adapter;

import net.xalcon.torchmaster.events.EventResult;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinecraftEventResultDecisionsTest
{
    @Test
    void allowBypassesFallback()
    {
        AtomicBoolean fallbackCalled = new AtomicBoolean(false);

        boolean result = MinecraftEventResultDecisions.resolve(EventResult.ALLOW, () -> {
            fallbackCalled.set(true);
            return false;
        });

        assertTrue(result);
        assertFalse(fallbackCalled.get());
    }

    @Test
    void denyBypassesFallback()
    {
        AtomicBoolean fallbackCalled = new AtomicBoolean(false);

        boolean result = MinecraftEventResultDecisions.resolve(EventResult.DENY, () -> {
            fallbackCalled.set(true);
            return true;
        });

        assertFalse(result);
        assertFalse(fallbackCalled.get());
    }

    @Test
    void defaultUsesFallback()
    {
        AtomicBoolean fallbackCalled = new AtomicBoolean(false);

        boolean result = MinecraftEventResultDecisions.resolve(EventResult.DEFAULT, () -> {
            fallbackCalled.set(true);
            return true;
        });

        assertTrue(result);
        assertTrue(fallbackCalled.get());
    }
}
