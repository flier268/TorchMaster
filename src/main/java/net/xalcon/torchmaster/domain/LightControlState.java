package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.LightAccessEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class LightControlState
{
    private static final LightControlState EMPTY = new LightControlState(Optional.empty(), Collections.emptyList(), false);

    private final Optional<UUID> ownerUuid;
    private final List<LightAccessEntry> allowedPlayers;
    private final boolean rangeVisible;

    private LightControlState(Optional<UUID> ownerUuid, List<LightAccessEntry> allowedPlayers, boolean rangeVisible)
    {
        this.ownerUuid = ownerUuid == null ? Optional.empty() : ownerUuid;
        this.allowedPlayers = Collections.unmodifiableList(new ArrayList<>(allowedPlayers == null ? Collections.emptyList() : allowedPlayers));
        this.rangeVisible = rangeVisible;
    }

    public static LightControlState empty()
    {
        return EMPTY;
    }

    public static LightControlState of(Optional<UUID> ownerUuid, List<LightAccessEntry> allowedPlayers, boolean rangeVisible)
    {
        return new LightControlState(ownerUuid, allowedPlayers, rangeVisible);
    }

    public Optional<UUID> ownerUuid()
    {
        return ownerUuid;
    }

    public List<LightAccessEntry> allowedPlayers()
    {
        return allowedPlayers;
    }

    public boolean rangeVisible()
    {
        return rangeVisible;
    }

    public LightControlState withOwner(Optional<UUID> ownerUuid)
    {
        return new LightControlState(ownerUuid, allowedPlayers, rangeVisible);
    }

    public LightControlState withAllowedPlayers(List<LightAccessEntry> allowedPlayers)
    {
        return new LightControlState(ownerUuid, allowedPlayers, rangeVisible);
    }

    public LightControlState withRangeVisible(boolean rangeVisible)
    {
        return new LightControlState(ownerUuid, allowedPlayers, rangeVisible);
    }
}
