package net.xalcon.torchmaster.minecraft.light;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.domain.LightSettings;
import net.xalcon.torchmaster.port.LightSettingsView;

public final class LightSettingsNetworkHandler
{
    public static final int WORLD_BROADCAST_CHUNK_RADIUS = 100;

    @FunctionalInterface
    public interface SnapshotSender
    {
        void send(ServerPlayerEntity player, BlockPos pos, LightType lightType, LightSettingsView snapshot);
    }

    private LightSettingsNetworkHandler()
    {
    }

    public static void handleQuery(ServerPlayerEntity player, BlockPos pos, LightType lightType, SnapshotSender sender)
    {
        World level = player.getEntityWorld();
        LightSettingsView snapshot = LightSettingsService.snapshot(level, pos, lightType, player);
        sender.send(player, pos, lightType, snapshot);
    }

    public static void handleUpdate(ServerPlayerEntity player, BlockPos pos, LightType lightType, LightSettings settings, SnapshotSender sender)
    {
        World level = player.getEntityWorld();
        boolean updated = LightSettingsService.update(level, pos, lightType, player, settings);
        LightSettingsView snapshot = LightSettingsService.snapshot(level, pos, lightType, player);
        sender.send(player, pos, lightType, snapshot);
        if (updated) {
            LightPreviewSyncService.syncWorld(level);
        }
    }

    public static void handleRangeVisible(ServerPlayerEntity player, BlockPos pos, LightType lightType, boolean rangeVisible, SnapshotSender sender)
    {
        World level = player.getEntityWorld();
        boolean updated = LightSettingsService.updateRangeVisible(level, pos, lightType, player, rangeVisible);
        LightSettingsView snapshot = LightSettingsService.snapshot(level, pos, lightType, player);
        sender.send(player, pos, lightType, snapshot);
        if (updated) {
            LightPreviewSyncService.syncWorld(level);
        }
    }

    public static void handleVisibleRangesQuery(ServerPlayerEntity player, SnapshotSender sender)
    {
        LightPreviewSyncService.syncPlayer(player);
    }

    public static void handleAddAccess(ServerPlayerEntity player, BlockPos pos, LightType lightType, String playerName, SnapshotSender sender)
    {
        World level = player.getEntityWorld();
        LightSettingsService.addAccess(level, pos, lightType, player, playerName);
        LightSettingsView snapshot = LightSettingsService.snapshot(level, pos, lightType, player);
        sender.send(player, pos, lightType, snapshot);
    }

    public static void handleRemoveAccess(ServerPlayerEntity player, BlockPos pos, LightType lightType, String playerUuid, SnapshotSender sender)
    {
        World level = player.getEntityWorld();
        LightSettingsService.removeAccess(level, pos, lightType, player, playerUuid);
        LightSettingsView snapshot = LightSettingsService.snapshot(level, pos, lightType, player);
        sender.send(player, pos, lightType, snapshot);
    }

    public static boolean shouldReceiveWorldBroadcast(ServerPlayerEntity player, World level, BlockPos source)
    {
        if (player.getEntityWorld() != level) {
            return false;
        }
        BlockPos playerPos = player.getBlockPos();
        return isWithinChunkRadius(playerPos.getX() >> 4, playerPos.getZ() >> 4, source.getX() >> 4, source.getZ() >> 4,
                WORLD_BROADCAST_CHUNK_RADIUS);
    }

    static boolean isWithinChunkRadius(int playerChunkX, int playerChunkZ, int sourceChunkX, int sourceChunkZ, int radius)
    {
        int maxDistance = Math.max(0, radius);
        return Math.abs(playerChunkX - sourceChunkX) <= maxDistance && Math.abs(playerChunkZ - sourceChunkZ) <= maxDistance;
    }
}
