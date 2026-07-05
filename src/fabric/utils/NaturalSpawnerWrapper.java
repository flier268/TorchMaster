package net.xalcon.torchmaster.utils;

import net.minecraft.server.level.ServerPlayer;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.SpawnEventBridge;

public class NaturalSpawnerWrapper {

    public static EventResult isValidEmptySpawnBlock(ServerPlayer player)
    {
        var container = new EventResultContainer(EventResult.DEFAULT);
        SpawnEventBridge.onPlayerSpawnPhantoms(player, player.position(), container);
        return container.getResult();
    }
}
