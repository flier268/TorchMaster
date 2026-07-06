package net.xalcon.torchmaster.minecraft.adapter;

import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;

import java.util.function.Consumer;

public final class MinecraftSpawnEventContainers
{
    private MinecraftSpawnEventContainers()
    {
    }

    public static EventResultContainer defaultContainer()
    {
        return container(EventResult.DEFAULT);
    }

    public static EventResultContainer container(EventResult result)
    {
        return new EventResultContainer(result);
    }

    public static EventResult result(EventResultContainer container)
    {
        return container.getResult();
    }

    public static EventResult invokeDefault(Consumer<EventResultContainer> callback)
    {
        EventResultContainer container = defaultContainer();
        callback.accept(container);
        return result(container);
    }

    public static boolean denies(EventResultContainer container)
    {
        return result(container) == EventResult.DENY;
    }

    public static boolean deniesDefault(Consumer<EventResultContainer> callback)
    {
        return invokeDefault(callback) == EventResult.DENY;
    }
}
