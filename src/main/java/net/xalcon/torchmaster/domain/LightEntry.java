package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.BlockPosView;

public interface LightEntry {
    LightKind kind();

    BlockPosView position();

    String displayName();

    default boolean blocksNaturalSpawnsOnly()
    {
        return false;
    }

    default LightSettings settings()
    {
        return LightSettings.unconfigured();
    }

    default LightEntry withSettings(LightSettings settings)
    {
        throw new UnsupportedOperationException("Light entry does not support per-light settings");
    }

    default LightControlState controlState()
    {
        return LightControlState.empty();
    }

    default LightEntry withControlState(LightControlState controlState)
    {
        throw new UnsupportedOperationException("Light entry does not support per-light control state");
    }
}
