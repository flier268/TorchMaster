package net.xalcon.torchmaster.minecraft.adapter;

import net.xalcon.torchmaster.events.EventResult;

import java.util.function.BooleanSupplier;

public final class MinecraftEventResultDecisions
{
    private MinecraftEventResultDecisions()
    {
    }

    public static boolean resolve(EventResult result, BooleanSupplier fallback)
    {
        switch (result) {
            case ALLOW:
                return true;
            case DENY:
                return false;
            case DEFAULT:
            default:
                return fallback.getAsBoolean();
        }
    }
}
