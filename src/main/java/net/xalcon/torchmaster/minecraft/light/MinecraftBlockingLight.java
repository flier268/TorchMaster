package net.xalcon.torchmaster.minecraft.light;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.xalcon.torchmaster.domain.LightControlState;
import net.xalcon.torchmaster.minecraft.storage.PersistedLightEntry;
import net.xalcon.torchmaster.port.LightAccessEntry;

public interface MinecraftBlockingLight extends PersistedLightEntry {
    default Optional<UUID> ownerUuid()
    {
        return Optional.empty();
    }

    default MinecraftBlockingLight withOwner(Optional<UUID> ownerUuid)
    {
        return this;
    }

    default List<LightAccessEntry> allowedPlayers()
    {
        return Collections.emptyList();
    }

    default MinecraftBlockingLight withAllowedPlayers(List<LightAccessEntry> allowedPlayers)
    {
        return this;
    }

    default boolean rangeVisible()
    {
        return false;
    }

    default MinecraftBlockingLight withRangeVisible(boolean rangeVisible)
    {
        return this;
    }

    @Override
    default LightControlState controlState()
    {
        return LightControlState.of(ownerUuid(), allowedPlayers(), rangeVisible());
    }

    @Override
    default MinecraftBlockingLight withControlState(LightControlState controlState)
    {
        if (controlState == null) {
            return this;
        }
        return withOwner(controlState.ownerUuid())
                .withAllowedPlayers(controlState.allowedPlayers())
                .withRangeVisible(controlState.rangeVisible());
    }
}
