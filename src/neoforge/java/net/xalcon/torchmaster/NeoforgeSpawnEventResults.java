package net.xalcon.torchmaster;

import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSpawnPhantomsEvent;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnEventContainers;

final class NeoforgeSpawnEventResults
{
    private NeoforgeSpawnEventResults()
    {
    }

    static EventResultContainer positionCheckContainer(MobSpawnEvent.PositionCheck.Result result)
    {
        return MinecraftSpawnEventContainers.container(fromPositionCheck(result));
    }

    static MobSpawnEvent.PositionCheck.Result toPositionCheck(EventResultContainer container)
    {
        switch (MinecraftSpawnEventContainers.result(container)) {
            case ALLOW:
                return MobSpawnEvent.PositionCheck.Result.SUCCEED;
            case DENY:
                return MobSpawnEvent.PositionCheck.Result.FAIL;
            case DEFAULT:
            default:
                return MobSpawnEvent.PositionCheck.Result.DEFAULT;
        }
    }

    static EventResultContainer phantomContainer(PlayerSpawnPhantomsEvent.Result result)
    {
        return MinecraftSpawnEventContainers.container(fromPhantom(result));
    }

    static PlayerSpawnPhantomsEvent.Result toPhantom(EventResultContainer container)
    {
        switch (MinecraftSpawnEventContainers.result(container)) {
            case ALLOW:
                return PlayerSpawnPhantomsEvent.Result.ALLOW;
            case DENY:
                return PlayerSpawnPhantomsEvent.Result.DENY;
            case DEFAULT:
            default:
                return PlayerSpawnPhantomsEvent.Result.DEFAULT;
        }
    }

    private static EventResult fromPositionCheck(MobSpawnEvent.PositionCheck.Result result)
    {
        switch (result) {
            case SUCCEED:
                return EventResult.ALLOW;
            case FAIL:
                return EventResult.DENY;
            case DEFAULT:
            default:
                return EventResult.DEFAULT;
        }
    }

    private static EventResult fromPhantom(PlayerSpawnPhantomsEvent.Result result)
    {
        switch (result) {
            case ALLOW:
                return EventResult.ALLOW;
            case DENY:
                return EventResult.DENY;
            case DEFAULT:
            default:
                return EventResult.DEFAULT;
        }
    }
}
