package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.BlockPosView;

public interface LightEntry {
    LightKind kind();

    BlockPosView position();

    String displayName();
}
