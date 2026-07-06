package net.xalcon.torchmaster.minecraft.light;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.domain.LightEntry;
import net.xalcon.torchmaster.port.LightSettingsView;

import java.util.ArrayList;
import java.util.List;

public final class LightPreviewSyncService
{
    private static final LightSettingsNetworkHandler.SnapshotSender NOOP_SENDER = (player, pos, lightType, snapshot) -> {};
    private static final BlockPos RESET_POS = new BlockPos(0, 0, 0);
    private static LightSettingsNetworkHandler.SnapshotSender sender = NOOP_SENDER;

    private LightPreviewSyncService()
    {
    }

    public static void setSender(LightSettingsNetworkHandler.SnapshotSender snapshotSender)
    {
        sender = snapshotSender == null ? NOOP_SENDER : snapshotSender;
    }

    public static void syncPlayer(ServerPlayerEntity player)
    {
        if (player == null) {
            return;
        }
        sender.send(player, RESET_POS, LightType.MegaTorch, LightSettingsView.previewSyncStartMarker());
        for (PreviewSnapshot snapshot : currentSnapshots(player)) {
            sender.send(player, snapshot.pos, snapshot.lightType, snapshot.snapshot);
        }
        sender.send(player, RESET_POS, LightType.MegaTorch, LightSettingsView.previewSyncEndMarker());
    }

    public static void syncWorld(World level)
    {
        if (!(level instanceof ServerWorld)) {
            return;
        }
        ServerWorld serverWorld = (ServerWorld)level;
        for (ServerPlayerEntity player : serverWorld.getServer().getPlayerManager().getPlayerList()) {
            if (player.getEntityWorld() == level) {
                syncPlayer(player);
            }
        }
    }

    private static List<PreviewSnapshot> currentSnapshots(ServerPlayerEntity player)
    {
        World level = player.getEntityWorld();
        List<PreviewSnapshot> snapshots = new ArrayList<>();
        for (LightEntry light : LightSettingsService.visibleRangeEntries(level)) {
            LightType lightType = LightType.forKind(light.kind());
            BlockPos pos = new BlockPos(light.position().x(), light.position().y(), light.position().z());
            if (!LightSettingsNetworkHandler.shouldReceiveWorldBroadcast(player, level, pos)) {
                continue;
            }
            LightSettingsView snapshot = LightSettingsService.snapshot(level, light, lightType, player);
            snapshots.add(new PreviewSnapshot(pos, lightType, snapshot));
        }
        return snapshots;
    }

    static final class PreviewSnapshot
    {
        final BlockPos pos;
        final LightType lightType;
        final LightSettingsView snapshot;

        PreviewSnapshot(BlockPos pos, LightType lightType, LightSettingsView snapshot)
        {
            this.pos = pos.toImmutable();
            this.lightType = lightType;
            this.snapshot = snapshot;
        }
    }
}
