package net.xalcon.torchmaster.client;

import net.minecraft.client.MinecraftClient;
//? if >=1.19.4
import net.minecraft.registry.RegistryKey;
//? if >=1.16.5 && <1.19.4
//import net.minecraft.util.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
//? if <1.16.5
//import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.World;
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.domain.LightSettings;
import net.xalcon.torchmaster.minecraft.light.LightPreviewSyncService;
import net.xalcon.torchmaster.minecraft.light.LightSettingsService;
import net.xalcon.torchmaster.port.LightAccessEntry;
import net.xalcon.torchmaster.port.LightSettingsView;

public final class TorchmasterLightScreenController
{
    private final BlockPos pos;
    //? if >=1.16.5
    private final RegistryKey<World> dimension;
    //? if <1.16.5
    //private final Object dimension;
    private final LightType lightType;
    private LightSettingsView settings;

    TorchmasterLightScreenController(BlockPos pos,
            //? if >=1.16.5
            RegistryKey<World> dimension
            //? if <1.16.5
            //Object dimension
            , LightType lightType)
    {
        this.pos = pos.toImmutable();
        this.dimension = dimension;
        this.lightType = lightType;
        this.settings = loadSettings();
    }

    LightSettingsView settings()
    {
        return settings;
    }

    void queryRemoteIfNeeded()
    {
        if (remoteServer()) {
            LightSettingsClientNetworking.query(pos, lightType);
        }
    }

    boolean toggleVisibility()
    {
        boolean visible = !settings.rangeVisible();
        ServerWorld level = serverWorld();
        ServerPlayerEntity player = serverPlayer();
        if (level == null || player == null) {
            LightSettingsClientNetworking.rangeVisible(pos, lightType, visible);
            return false;
        }
        if (!LightSettingsService.updateRangeVisible(level, pos, lightType, player, visible)) {
            return false;
        }
        settings = LightSettingsService.snapshot(level, pos, lightType, player);
        TorchmasterLightRangeDisplay.setVisible(dimension, pos, lightType, settings.rangeVisible(), settings.rangeWest(), settings.rangeEast(),
                settings.rangeDown(), settings.rangeUp(), settings.rangeNorth(), settings.rangeSouth());
        LightPreviewSyncService.syncWorld(level);
        return true;
    }

    void setDraft(boolean enabled, int rangeWest, int rangeEast, int rangeDown, int rangeUp, int rangeNorth, int rangeSouth)
    {
        settings = LightSettingsView.present(true, settings.accessManageable(), enabled, rangeWest, rangeEast, rangeDown, rangeUp, rangeNorth,
                rangeSouth, settings.globalMax(), settings.chunkAligned(), settings.rangeVisible(), settings.accessEntries());
    }

    boolean applySettings(LightSettings requested)
    {
        ServerWorld level = serverWorld();
        ServerPlayerEntity player = serverPlayer();
        if (level == null || player == null) {
            LightSettingsClientNetworking.update(pos, lightType, requested);
            return false;
        }
        if (!LightSettingsService.update(level, pos, lightType, player, requested)) {
            return false;
        }
        settings = LightSettingsService.snapshot(level, pos, lightType, player);
        LightPreviewSyncService.syncWorld(level);
        return true;
    }

    void resetDraft()
    {
        settings = LightSettingsView.present(true, settings.accessManageable(), true, settings.globalMax(), settings.globalMax(), settings.globalMax(),
                settings.globalMax(), settings.globalMax(), settings.globalMax(), settings.globalMax(), settings.chunkAligned(),
                settings.rangeVisible(), settings.accessEntries());
    }

    boolean applySnapshot(BlockPos snapshotPos, LightType snapshotType, LightSettingsView snapshot)
    {
        TorchmasterLightRangeDisplay.applyCurrentWorld(snapshotPos, snapshotType, snapshot);
        if (!pos.equals(snapshotPos) || lightType != snapshotType) {
            return false;
        }
        settings = snapshot;
        return true;
    }

    void refreshVisibleRange()
    {
        if (TorchmasterLightRangeDisplay.isVisible(dimension, pos)) {
            TorchmasterLightRangeDisplay.refresh(dimension, pos, lightType, settings.rangeWest(), settings.rangeEast(), settings.rangeDown(),
                    settings.rangeUp(), settings.rangeNorth(), settings.rangeSouth());
        }
    }

    boolean addAccess(String playerName)
    {
        if (!settings.accessManageable()) {
            return false;
        }
        playerName = playerName == null ? "" : playerName.trim();
        if (playerName.isEmpty()) {
            return false;
        }
        ServerWorld level = serverWorld();
        ServerPlayerEntity player = serverPlayer();
        if (level == null || player == null) {
            LightSettingsClientNetworking.addAccess(pos, lightType, playerName);
            return false;
        }
        if (!LightSettingsService.addAccess(level, pos, lightType, player, playerName)) {
            return false;
        }
        settings = LightSettingsService.snapshot(level, pos, lightType, player);
        return true;
    }

    boolean removeAccess(int index)
    {
        LightAccessEntry[] accessEntries = settings.accessEntries();
        if (!settings.accessManageable() || index < 0 || index >= accessEntries.length) {
            return false;
        }
        ServerWorld level = serverWorld();
        ServerPlayerEntity player = serverPlayer();
        if (level == null || player == null) {
            LightSettingsClientNetworking.removeAccess(pos, lightType, accessEntries[index].uuidString());
            return false;
        }
        if (!LightSettingsService.removeAccess(level, pos, lightType, player, accessEntries[index].uuidString())) {
            return false;
        }
        settings = LightSettingsService.snapshot(level, pos, lightType, player);
        return true;
    }

    private LightSettingsView loadSettings()
    {
        ServerWorld level = serverWorld();
        ServerPlayerEntity player = serverPlayer();
        if (level == null || player == null) {
            LightSettingsView fallback = LightSettingsService.fallbackSnapshot(lightType);
            return LightSettingsView.present(false, fallback.enabled(), fallback.rangeWest(), fallback.rangeEast(), fallback.rangeDown(),
                    fallback.rangeUp(), fallback.rangeNorth(), fallback.rangeSouth(), fallback.globalMax());
        }
        return LightSettingsService.snapshot(level, pos, lightType, player);
    }

    private boolean remoteServer()
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        return minecraft.world != null && minecraft.getServer() == null;
    }

    private ServerWorld serverWorld()
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.getServer() == null) {
            return null;
        }
        //? if >=1.16.5 {
        return minecraft.getServer().getWorld(dimension);
        //?} else {
        /*return minecraft.getServer().getWorld((DimensionType)dimension);
        *///?}
    }

    private ServerPlayerEntity serverPlayer()
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.getServer() == null || minecraft.player == null) {
            return null;
        }
        return minecraft.getServer().getPlayerManager().getPlayer(minecraft.player.getUuid());
    }
}
