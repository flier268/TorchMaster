package net.xalcon.torchmaster.minecraft.spawn;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
//? if >=1.16.5
import net.minecraft.entity.SpawnReason;
//? if <1.16.5
//import net.minecraft.entity.SpawnType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnBlocker;

import java.util.function.BooleanSupplier;

public final class MinecraftSpawnEventHooks
{
    private MinecraftSpawnEventHooks()
    {
    }

    //? if fabric && forge && >=1.21.2 {
    /*public static void onCheckSpawn(final EntitySpawnReason spawnType, final Entity entity, final Vec3 location, final EventResultContainer container)
    *///?} else {
    //? if >=1.16.5
    public static void onCheckSpawn(final SpawnReason spawnType, final Entity entity, final Vec3d location, final EventResultContainer container)
    //? if <1.16.5
    //public static void onCheckSpawn(final SpawnType spawnType, final Entity entity, final Vec3d location, final EventResultContainer container)
    //?}
    {
        //? if fabric && forge && >=1.21.2 {
        /*Level level = entity.level();
        *///?} else {
        World level = entity.getEntityWorld();
        //?}
        MinecraftSpawnEventContext context = MinecraftSpawnEventContextFactory.entity(entity.getType(), location, spawnType, container);
        applyEntitySpawn(context, () -> MinecraftSpawnBlocker.shouldBlockEntity(level, entity.getType(), location, context.spawnReason()), container);
    }

    public static void onPlayerSpawnPhantoms(PlayerEntity player, final Vec3d location, final EventResultContainer container)
    {
        //? if fabric && forge && >=1.21.2 {
        /*Level level = player.level();
        *///?} else {
        World level = player.getEntityWorld();
        //?}

        MinecraftSpawnEventContext context = MinecraftSpawnEventContextFactory.phantom(location,
                //? if fabric && forge && >=1.21.2 {
                /*EntitySpawnReason.NATURAL
                *///?} else {
                //? if >=1.16.5
                SpawnReason.NATURAL
                //? if <1.16.5
                //SpawnType.NATURAL
                //?}
                , container);
        applyPhantomSpawn(context, () -> MinecraftSpawnBlocker.shouldBlockEntity(
                level,
                EntityType.PHANTOM,
                //? if >=1.21.11
                //player.getEntityPos()
                //? if <1.21.11
                player.getPos()
                ,
                context.spawnReason()), container);
    }

    public static void onVillageSiege(World level, Vec3d attemptedSpawnPos, EventResultContainer container)
    {
        MinecraftSpawnEventContext context = MinecraftSpawnEventContextFactory.villageSiege(attemptedSpawnPos, container);
        applyVillageSiege(context, () -> MinecraftSpawnBlocker.shouldBlockVillageSiege(level, attemptedSpawnPos), container);
    }

    static void applyEntitySpawn(MinecraftSpawnEventContext context, BooleanSupplier blockCheck, EventResultContainer container)
    {
        applyEntitySpawn(context, blockCheck, container, MinecraftSpawnRuntimeServices.DEFAULT);
    }

    static void applyPhantomSpawn(MinecraftSpawnEventContext context, BooleanSupplier blockCheck, EventResultContainer container)
    {
        applyPhantomSpawn(context, blockCheck, container, MinecraftSpawnRuntimeServices.DEFAULT);
    }

    static void applyVillageSiege(MinecraftSpawnEventContext context, BooleanSupplier blockCheck, EventResultContainer container)
    {
        applyVillageSiege(context, blockCheck, container, MinecraftSpawnRuntimeServices.DEFAULT);
    }

    static void applyEntitySpawn(MinecraftSpawnEventContext context, BooleanSupplier blockCheck, EventResultContainer container,
            MinecraftSpawnRuntimeServices services)
    {
        MinecraftSpawnEventRuntime.applyDeny(container, MinecraftSpawnEventRuntime.shouldDenyEntitySpawn(context, blockCheck, services));
    }

    static void applyPhantomSpawn(MinecraftSpawnEventContext context, BooleanSupplier blockCheck, EventResultContainer container,
            MinecraftSpawnRuntimeServices services)
    {
        MinecraftSpawnEventRuntime.applyDeny(container, MinecraftSpawnEventRuntime.shouldDenyPhantomSpawn(context, blockCheck, services));
    }

    static void applyVillageSiege(MinecraftSpawnEventContext context, BooleanSupplier blockCheck, EventResultContainer container,
            MinecraftSpawnRuntimeServices services)
    {
        MinecraftSpawnEventRuntime.applyDeny(container, MinecraftSpawnEventRuntime.shouldDenyVillageSiege(context, blockCheck, services));
    }
}
