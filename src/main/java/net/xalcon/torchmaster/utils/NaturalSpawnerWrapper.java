package net.xalcon.torchmaster.utils;

//? if fabric {
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.SpawnEventBridge;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnEventContainers;

public class NaturalSpawnerWrapper {

    public static EventResult isValidEmptySpawnBlock(ServerPlayerEntity player)
    {
        //? if >=1.21.11 {
        /*Vec3d position = player.getEntityPos();
        *///?} else {
        Vec3d position = player.getPos();
        //?}
        return MinecraftSpawnEventContainers.invokeDefault(container ->
                SpawnEventBridge.onPlayerSpawnPhantoms(player, position, container));
    }
}
//?}
