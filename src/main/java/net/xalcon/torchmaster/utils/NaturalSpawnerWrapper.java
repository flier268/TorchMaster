package net.xalcon.torchmaster.utils;

//? if fabric {
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.SpawnEventBridge;

public class NaturalSpawnerWrapper {

    public static EventResult isValidEmptySpawnBlock(ServerPlayerEntity player)
    {
        EventResultContainer container = new EventResultContainer(EventResult.DEFAULT);
        //? if >=1.21.11 {
        /*Vec3d position = player.getEntityPos();
        *///?} else {
        Vec3d position = player.getPos();
        //?}
        SpawnEventBridge.onPlayerSpawnPhantoms(player, position, container);
        return container.getResult();
    }
}
//?}
