package net.xalcon.torchmaster.minecraft.light;

import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.domain.LightEntry;
import net.xalcon.torchmaster.domain.LightSettings;
import net.xalcon.torchmaster.domain.LightSettingsUseCase;
import net.xalcon.torchmaster.minecraft.storage.LightStoreBridge;
import net.xalcon.torchmaster.minecraft.storage.MinecraftLightStoreAccess;
import net.xalcon.torchmaster.port.LightAccessEntry;
import net.xalcon.torchmaster.port.LightSettingsView;

import java.util.Optional;

public final class LightSettingsService
{
    private LightSettingsService()
    {
    }

    public static LightSettingsView snapshot(World level, BlockPos pos, LightType lightType, boolean editable)
    {
        int globalMax = globalMax(lightType);
        Optional<LightEntry> light = light(level, pos, lightType);
        if (!light.isPresent()) {
            return LightSettingsView.missing(globalMax);
        }
        LightSettings settings = light.get().settings().effective(globalMax);
        return LightSettingsView.present(editable, false, settings.enabled(), settings.rangeWest(), settings.rangeEast(), settings.rangeDown(),
                settings.rangeUp(), settings.rangeNorth(), settings.rangeSouth(), globalMax, light.get().blocksNaturalSpawnsOnly(),
                light.get().controlState().rangeVisible(), new LightAccessEntry[0]);
    }

    public static LightSettingsView snapshot(World level, BlockPos pos, LightType lightType, ServerPlayerEntity player)
    {
        return useCase(level).snapshot(lightType.key(pos), globalMax(lightType), actor(player));
    }

    public static LightSettingsView snapshot(World level, LightEntry light, LightType lightType, ServerPlayerEntity player)
    {
        return useCase(level).snapshot(light, globalMax(lightType), actor(player));
    }

    public static LightSettingsView fallbackSnapshot(LightType lightType)
    {
        return useCase(null).fallbackSnapshot(globalMax(lightType));
    }

    public static LightSettingsView missingSnapshot(LightType lightType)
    {
        return LightSettingsView.missing(globalMax(lightType));
    }

    public static LightSettingsView previewRemovedSnapshot(LightType lightType)
    {
        return LightSettingsView.previewRemoved(globalMax(lightType));
    }

    public static boolean update(World level, BlockPos pos, LightType lightType, ServerPlayerEntity player, LightSettings settings)
    {
        if (!matchesLight(level, pos, lightType)) {
            return false;
        }
        return useCase(level).updateSettings(lightType.key(pos), globalMax(lightType), actor(player), settings);
    }

    public static boolean updateRangeVisible(World level, BlockPos pos, LightType lightType, ServerPlayerEntity player, boolean rangeVisible)
    {
        if (!matchesLight(level, pos, lightType)) {
            return false;
        }
        return useCase(level).updateRangeVisible(lightType.key(pos), actor(player), rangeVisible);
    }

    public static boolean addAccess(World level, BlockPos pos, LightType lightType, ServerPlayerEntity player, String playerName)
    {
        if (!matchesLight(level, pos, lightType)) {
            return false;
        }
        return useCase(level).addAccess(lightType.key(pos), actor(player), playerName);
    }

    public static boolean removeAccess(World level, BlockPos pos, LightType lightType, ServerPlayerEntity player, String playerUuid)
    {
        if (!matchesLight(level, pos, lightType)) {
            return false;
        }
        return useCase(level).removeAccess(lightType.key(pos), actor(player), playerUuid);
    }

    public static LightEntry[] visibleRangeEntries(World level)
    {
        return useCase(level).visibleRangeEntries();
    }

    public static boolean canEdit(ServerPlayerEntity player)
    {
        return actor(player).operator();
    }

    public static boolean canEdit(ServerPlayerEntity player, LightEntry light)
    {
        return useCase(player == null ? null : player.getEntityWorld()).canEdit(actor(player), light);
    }

    public static boolean canManageAccess(ServerPlayerEntity player, LightEntry light)
    {
        return useCase(player == null ? null : player.getEntityWorld()).canManageAccess(actor(player), light);
    }

    private static LightSettingsUseCase useCase(World level)
    {
        return new LightSettingsUseCase(repository(level), playerDirectory(level), TorchmasterRuntime.getConfig().getRestrictLightSettingsToOwner());
    }

    private static LightSettingsUseCase.LightRepository repository(World level)
    {
        return new LightSettingsUseCase.LightRepository()
        {
            @Override
            public Optional<LightEntry> find(String lightKey)
            {
                return store(level).flatMap(store -> store.getLight(lightKey));
            }

            @Override
            public boolean save(String lightKey, LightEntry light)
            {
                return store(level).map(store -> {
                    store.registerLight(lightKey, light);
                    return true;
                }).orElse(false);
            }

            @Override
            public LightEntry[] entries()
            {
                return store(level).map(LightStoreBridge::lightEntries).orElse(new LightEntry[0]);
            }
        };
    }

    private static LightSettingsUseCase.PlayerDirectory playerDirectory(World level)
    {
        return playerName -> {
            if (level == null || playerName == null || playerName.trim().isEmpty()) {
                return Optional.empty();
            }
            //? if >=1.21.11 {
            /*ServerPlayerEntity target = level.getServer().getPlayerManager().getPlayer(playerName.trim());
            *///?} else {
            ServerPlayerEntity target = level.getServer().getPlayerManager().getPlayer(playerName.trim());
            //?}
            if (target == null) {
                return Optional.empty();
            }
            return Optional.of(new LightSettingsUseCase.PlayerRef(target.getUuid(), profileName(target)));
        };
    }

    private static Optional<LightStoreBridge> store(World level)
    {
        return level == null ? Optional.empty() : MinecraftLightStoreAccess.get(level);
    }

    private static Optional<LightEntry> light(World level, BlockPos pos, LightType lightType)
    {
        return store(level).flatMap(store -> store.getLight(lightType.key(pos)));
    }

    private static LightSettingsUseCase.Actor actor(ServerPlayerEntity player)
    {
        return new LightSettingsUseCase.Actor(player == null ? null : player.getUuid(), isOperator(player));
    }

    private static boolean isOperator(ServerPlayerEntity player)
    {
        if (player == null) {
            return false;
        }
        //? if >=1.21.11 {
        /*return player.getPermissions().hasPermission(new net.minecraft.command.permission.Permission.Level(net.minecraft.command.permission.PermissionLevel.fromLevel(2)));
        *///?} else {
        return player.server.getPlayerManager().isOperator(player.getGameProfile());
        //?}
    }

    private static String profileName(ServerPlayerEntity player)
    {
        //? if >=1.21.11 {
        /*return player.getGameProfile().name();
        *///?} else {
        return player.getGameProfile().getName();
        //?}
    }

    private static boolean matchesLight(World level, BlockPos pos, LightType lightType)
    {
        if (level == null) {
            return false;
        }
        BlockState state = level.getBlockState(pos);
        return lightType.matchesBlock(state.getBlock());
    }

    private static int globalMax(LightType lightType)
    {
        switch (lightType.kind()) {
            case MEGA_TORCH:
                return TorchmasterRuntime.getConfig().getMegaTorchRadius();
            case DREAD_LAMP:
                return TorchmasterRuntime.getConfig().getDreadLampRadius();
            default:
                return 0;
        }
    }
}
