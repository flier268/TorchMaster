package net.xalcon.torchmaster.minecraft.adapter;

import net.xalcon.torchmaster.port.EventResultPort;
import net.xalcon.torchmaster.events.EventResult;

public final class MinecraftEventResultAdapter {
    private MinecraftEventResultAdapter() {
    }

    public static EventResultPort toPort(EventResult result) {
        switch (result) {
            case DEFAULT:
                return EventResultPort.DEFAULT;
            case ALLOW:
                return EventResultPort.ALLOW;
            case DENY:
                return EventResultPort.DENY;
            default:
                return EventResultPort.DEFAULT;
        }
    }
}
