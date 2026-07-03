package net.xalcon.torchmaster.core;

import net.xalcon.torchmaster.adapter.BlockPosView;

public interface LightEntry {
    LightKind kind();

    BlockPosView position();

    String displayName();
}
