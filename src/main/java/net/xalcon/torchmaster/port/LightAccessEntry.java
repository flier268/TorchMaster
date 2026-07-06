package net.xalcon.torchmaster.port;

import java.util.UUID;

public final class LightAccessEntry
{
    private final UUID uuid;
    private final String name;

    public LightAccessEntry(UUID uuid, String name)
    {
        this.uuid = uuid;
        this.name = name == null ? "" : name;
    }

    public UUID uuid()
    {
        return uuid;
    }

    public String uuidString()
    {
        return uuid == null ? "" : uuid.toString();
    }

    public String name()
    {
        return name;
    }
}
