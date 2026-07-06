package net.xalcon.torchmaster.minecraft.spawn;

import net.xalcon.torchmaster.domain.SpawnBlockingRules;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;

import java.util.function.BooleanSupplier;

public final class MinecraftSpawnEventRuntime
{
    private MinecraftSpawnEventRuntime()
    {
    }

    public static boolean shouldDenyEntitySpawn(MinecraftSpawnEventContext context, BooleanSupplier blockCheck)
    {
        return shouldDenyEntitySpawn(context, blockCheck, MinecraftSpawnRuntimeServices.DEFAULT);
    }

    public static boolean shouldDenyPhantomSpawn(MinecraftSpawnEventContext context, BooleanSupplier blockCheck)
    {
        return shouldDenyPhantomSpawn(context, blockCheck, MinecraftSpawnRuntimeServices.DEFAULT);
    }

    public static boolean shouldDenyVillageSiege(MinecraftSpawnEventContext context, BooleanSupplier blockCheck)
    {
        return shouldDenyVillageSiege(context, blockCheck, MinecraftSpawnRuntimeServices.DEFAULT);
    }

    public static void applyDeny(EventResultContainer container, boolean deny)
    {
        if (deny) {
            container.setResult(EventResult.DENY);
        }
    }

    static boolean shouldDenyEntitySpawn(MinecraftSpawnEventContext context, BooleanSupplier blockCheck, MinecraftSpawnRuntimeServices services)
    {
        services.debug("CheckSpawn - Reason: {}, Type: {}, Pos: {}/{}/{}",
                context.spawnReason(), context.entityType(), context.position().x(), context.position().y(), context.position().z());
        if (SpawnBlockingRules.shouldSkipEntitySpawnCheck(context.spawnReason(), context.currentResult(), services.config())) {
            return false;
        }
        return resolveBlockCheck(context, blockCheck, services, "spawn of {}", context.entityType());
    }

    static boolean shouldDenyPhantomSpawn(MinecraftSpawnEventContext context, BooleanSupplier blockCheck, MinecraftSpawnRuntimeServices services)
    {
        services.debug("PlayerSpawnPhantoms - Pos: {}/{}/{}", context.position().x(), context.position().y(), context.position().z());
        if (SpawnBlockingRules.shouldSkipPhantomSpawnCheck(context.currentResult(), services.config())) {
            return false;
        }
        return resolveBlockCheck(context, blockCheck, services, "spawn of {}", context.entityType());
    }

    static boolean shouldDenyVillageSiege(MinecraftSpawnEventContext context, BooleanSupplier blockCheck, MinecraftSpawnRuntimeServices services)
    {
        services.debug("VillageSiegeEvent - Pos: {}", context.position());
        if (SpawnBlockingRules.shouldSkipVillageSiegeCheck(context.currentResult(), services.config())) {
            return false;
        }
        return resolveBlockCheck(context, blockCheck, services, "village siege @ {}", context.position());
    }

    private static boolean resolveBlockCheck(MinecraftSpawnEventContext context, BooleanSupplier blockCheck, MinecraftSpawnRuntimeServices services,
            String action, Object detail)
    {
        boolean denied = blockCheck.getAsBoolean();
        services.debug((denied ? "Blocking " : "Allowed ") + action, detail);
        return denied;
    }
}
