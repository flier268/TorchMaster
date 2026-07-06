package net.xalcon.torchmaster.network;

//? if >=1.19 {
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
//?}
//? if >=1.20.6
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
//? if <1.20.6
//import io.netty.buffer.Unpooled;
//? if <1.19 {
/*import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
*///?}
//? if >=1.16
import net.minecraft.network.PacketByteBuf;
//? if <1.16
//import net.minecraft.util.PacketByteBuf;
//? if >=1.20.6 {
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
//?}
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.client.LightSettingsClientNetworking;
import net.xalcon.torchmaster.client.TorchmasterLightScreen;
import net.xalcon.torchmaster.domain.LightSettings;
import net.xalcon.torchmaster.minecraft.light.BlockingLightLifecycle;
import net.xalcon.torchmaster.minecraft.light.LightPreviewSyncService;
import net.xalcon.torchmaster.minecraft.light.LightSettingsNetworkHandler;
import net.xalcon.torchmaster.minecraft.light.LightSettingsPacketCodec;
import net.xalcon.torchmaster.port.LightSettingsView;

public final class FabricLightSettingsNetworking
{
    private static final Identifier REQUEST_ID = identifier("light_settings_request");
    private static final Identifier SNAPSHOT_ID = identifier("light_settings_snapshot");

    private FabricLightSettingsNetworking()
    {
    }

    private static Identifier identifier(String path)
    {
        //? if >=1.20.6 {
        return Identifier.of(Constants.MOD_ID, path);
        //?} else if <1.20.6 {
        /*return new Identifier(Constants.MOD_ID, path);
        *///?}
    }

    public static void registerServer()
    {
        LightPreviewSyncService.setSender(FabricLightSettingsNetworking::sendSnapshotToPlayer);
        BlockingLightLifecycle.setRemovalListener((level, pos, lightType) -> LightPreviewSyncService.syncWorld(level));
        //? if >=1.20.6 {
        PayloadTypeRegistry.playC2S().register(RequestPayload.ID, RequestPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SnapshotPayload.ID, SnapshotPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(RequestPayload.ID, (payload, context) -> context.server().execute(() -> handleRequest(context.player(),
                payload.action, payload.pos, payload.lightType, payload.settings, payload.accessValue,
                (player, pos, lightType, snapshot) -> ServerPlayNetworking.send(player, new SnapshotPayload(pos, lightType, snapshot)))));
        //?} else if >=1.19 {
        /*ServerPlayNetworking.registerGlobalReceiver(REQUEST_ID, (server, player, handler, buf, responseSender) -> {
            int action = buf.readInt();
            BlockPos pos = buf.readBlockPos();
            LightType lightType = LightSettingsPacketCodec.readType(buf);
            LightSettings settings = action == LightSettingsPacketCodec.UPDATE ? LightSettingsPacketCodec.readSettings(buf) : LightSettings.unconfigured();
            String accessValue = action == LightSettingsPacketCodec.ADD_ACCESS || action == LightSettingsPacketCodec.REMOVE_ACCESS
                    || action == LightSettingsPacketCodec.RANGE_VISIBLE ? buf.readString() : "";
            server.execute(() -> handleRequest(player, action, pos, lightType, settings, accessValue,
                    FabricLightSettingsNetworking::sendLegacySnapshot));
        });
        *///?} else if <1.19 {
        /*ServerSidePacketRegistry.INSTANCE.register(REQUEST_ID, (context, buf) -> {
            int action = buf.readInt();
            BlockPos pos = buf.readBlockPos();
            LightType lightType = LightSettingsPacketCodec.readType(buf);
            LightSettings settings = action == LightSettingsPacketCodec.UPDATE ? LightSettingsPacketCodec.readSettings(buf) : LightSettings.unconfigured();
            String accessValue = action == LightSettingsPacketCodec.ADD_ACCESS || action == LightSettingsPacketCodec.REMOVE_ACCESS
                    || action == LightSettingsPacketCodec.RANGE_VISIBLE ? buf.readString() : "";
            context.getTaskQueue().execute(() -> handleRequest((ServerPlayerEntity)context.getPlayer(), action, pos, lightType, settings, accessValue,
                    FabricLightSettingsNetworking::sendLegacySnapshot));
        });
        *///?}
    }

    public static void registerClient()
    {
        //? if >=1.20.6 {
        ClientPlayNetworking.registerGlobalReceiver(SnapshotPayload.ID, (payload, context) ->
                context.client().execute(() -> TorchmasterLightScreen.receiveSnapshot(payload.pos, payload.lightType, payload.snapshot)));
        LightSettingsClientNetworking.setQuerySender((pos, lightType, ignored) ->
                ClientPlayNetworking.send(new RequestPayload(LightSettingsPacketCodec.QUERY, pos, lightType, LightSettings.unconfigured(), "")));
        LightSettingsClientNetworking.setVisibleRangesQuerySender(() ->
                ClientPlayNetworking.send(new RequestPayload(LightSettingsPacketCodec.VISIBLE_RANGES_QUERY, new BlockPos(0, 0, 0), LightType.MegaTorch, LightSettings.unconfigured(), "")));
        LightSettingsClientNetworking.setUpdateSender((pos, lightType, settings) ->
                ClientPlayNetworking.send(new RequestPayload(LightSettingsPacketCodec.UPDATE, pos, lightType, settings, "")));
        LightSettingsClientNetworking.setRangeVisibleSender((pos, lightType, visible) ->
                ClientPlayNetworking.send(new RequestPayload(LightSettingsPacketCodec.RANGE_VISIBLE, pos, lightType, LightSettings.unconfigured(), visible)));
        LightSettingsClientNetworking.setAddAccessSender((pos, lightType, playerName) ->
                ClientPlayNetworking.send(new RequestPayload(LightSettingsPacketCodec.ADD_ACCESS, pos, lightType, LightSettings.unconfigured(), playerName)));
        LightSettingsClientNetworking.setRemoveAccessSender((pos, lightType, playerUuid) ->
                ClientPlayNetworking.send(new RequestPayload(LightSettingsPacketCodec.REMOVE_ACCESS, pos, lightType, LightSettings.unconfigured(), playerUuid)));
        //?} else if >=1.19 {
        /*ClientPlayNetworking.registerGlobalReceiver(SNAPSHOT_ID, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            LightType lightType = LightSettingsPacketCodec.readType(buf);
            LightSettingsView snapshot = LightSettingsPacketCodec.readSnapshot(buf);
            client.execute(() -> TorchmasterLightScreen.receiveSnapshot(pos, lightType, snapshot));
        });
        LightSettingsClientNetworking.setQuerySender((pos, lightType, ignored) ->
                sendLegacyRequest(LightSettingsPacketCodec.QUERY, pos, lightType, LightSettings.unconfigured(), ""));
        LightSettingsClientNetworking.setVisibleRangesQuerySender(() ->
                sendLegacyRequest(LightSettingsPacketCodec.VISIBLE_RANGES_QUERY, new BlockPos(0, 0, 0), LightType.MegaTorch, LightSettings.unconfigured(), ""));
        LightSettingsClientNetworking.setUpdateSender((pos, lightType, settings) ->
                sendLegacyRequest(LightSettingsPacketCodec.UPDATE, pos, lightType, settings, ""));
        LightSettingsClientNetworking.setRangeVisibleSender((pos, lightType, visible) ->
                sendLegacyRequest(LightSettingsPacketCodec.RANGE_VISIBLE, pos, lightType, LightSettings.unconfigured(), visible));
        LightSettingsClientNetworking.setAddAccessSender((pos, lightType, playerName) ->
                sendLegacyRequest(LightSettingsPacketCodec.ADD_ACCESS, pos, lightType, LightSettings.unconfigured(), playerName));
        LightSettingsClientNetworking.setRemoveAccessSender((pos, lightType, playerUuid) ->
                sendLegacyRequest(LightSettingsPacketCodec.REMOVE_ACCESS, pos, lightType, LightSettings.unconfigured(), playerUuid));
        *///?} else if <1.19 {
        /*ClientSidePacketRegistry.INSTANCE.register(SNAPSHOT_ID, (context, buf) -> {
            BlockPos pos = buf.readBlockPos();
            LightType lightType = LightSettingsPacketCodec.readType(buf);
            LightSettingsView snapshot = LightSettingsPacketCodec.readSnapshot(buf);
            context.getTaskQueue().execute(() -> TorchmasterLightScreen.receiveSnapshot(pos, lightType, snapshot));
        });
        LightSettingsClientNetworking.setQuerySender((pos, lightType, ignored) ->
                sendLegacyRequest(LightSettingsPacketCodec.QUERY, pos, lightType, LightSettings.unconfigured(), ""));
        LightSettingsClientNetworking.setVisibleRangesQuerySender(() ->
                sendLegacyRequest(LightSettingsPacketCodec.VISIBLE_RANGES_QUERY, new BlockPos(0, 0, 0), LightType.MegaTorch, LightSettings.unconfigured(), ""));
        LightSettingsClientNetworking.setUpdateSender((pos, lightType, settings) ->
                sendLegacyRequest(LightSettingsPacketCodec.UPDATE, pos, lightType, settings, ""));
        LightSettingsClientNetworking.setRangeVisibleSender((pos, lightType, visible) ->
                sendLegacyRequest(LightSettingsPacketCodec.RANGE_VISIBLE, pos, lightType, LightSettings.unconfigured(), visible));
        LightSettingsClientNetworking.setAddAccessSender((pos, lightType, playerName) ->
                sendLegacyRequest(LightSettingsPacketCodec.ADD_ACCESS, pos, lightType, LightSettings.unconfigured(), playerName));
        LightSettingsClientNetworking.setRemoveAccessSender((pos, lightType, playerUuid) ->
                sendLegacyRequest(LightSettingsPacketCodec.REMOVE_ACCESS, pos, lightType, LightSettings.unconfigured(), playerUuid));
        *///?}
    }

    private static void handleRequest(ServerPlayerEntity player, int action, BlockPos pos, LightType lightType, LightSettings settings, String accessValue,
            LightSettingsNetworkHandler.SnapshotSender sender)
    {
        if (action == LightSettingsPacketCodec.UPDATE) {
            LightSettingsNetworkHandler.handleUpdate(player, pos, lightType, settings, sender);
        } else if (action == LightSettingsPacketCodec.ADD_ACCESS) {
            LightSettingsNetworkHandler.handleAddAccess(player, pos, lightType, accessValue, sender);
        } else if (action == LightSettingsPacketCodec.REMOVE_ACCESS) {
            LightSettingsNetworkHandler.handleRemoveAccess(player, pos, lightType, accessValue, sender);
        } else if (action == LightSettingsPacketCodec.RANGE_VISIBLE) {
            LightSettingsNetworkHandler.handleRangeVisible(player, pos, lightType, Boolean.parseBoolean(accessValue), sender);
        } else if (action == LightSettingsPacketCodec.VISIBLE_RANGES_QUERY) {
            LightSettingsNetworkHandler.handleVisibleRangesQuery(player, sender);
        } else {
            LightSettingsNetworkHandler.handleQuery(player, pos, lightType, sender);
        }
    }

    private static void sendSnapshotToPlayer(ServerPlayerEntity player, BlockPos pos, LightType lightType, LightSettingsView snapshot)
    {
        //? if >=1.20.6 {
        ServerPlayNetworking.send(player, new SnapshotPayload(pos, lightType, snapshot));
        //?} else if <1.20.6 {
        /*sendLegacySnapshot(player, pos, lightType, snapshot);
        *///?}
    }

    //? if >=1.19 && <1.20.6 {
    /*private static void sendLegacyRequest(int action, BlockPos pos, LightType lightType, LightSettings settings, String accessValue)
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(action);
        buf.writeBlockPos(pos);
        LightSettingsPacketCodec.writeType(buf, lightType);
        if (action == LightSettingsPacketCodec.UPDATE) {
            LightSettingsPacketCodec.writeSettings(buf, settings);
        } else if (action == LightSettingsPacketCodec.ADD_ACCESS || action == LightSettingsPacketCodec.REMOVE_ACCESS
                || action == LightSettingsPacketCodec.RANGE_VISIBLE) {
            buf.writeString(accessValue == null ? "" : accessValue);
        }
        ClientPlayNetworking.send(REQUEST_ID, buf);
    }

    private static void sendLegacySnapshot(ServerPlayerEntity player, BlockPos pos, LightType lightType, LightSettingsView snapshot)
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        LightSettingsPacketCodec.writeType(buf, lightType);
        LightSettingsPacketCodec.writeSnapshot(buf, snapshot);
        ServerPlayNetworking.send(player, SNAPSHOT_ID, buf);
    }
    *///?}

    //? if <1.19 {
    /*private static void sendLegacyRequest(int action, BlockPos pos, LightType lightType, LightSettings settings, String accessValue)
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(action);
        buf.writeBlockPos(pos);
        LightSettingsPacketCodec.writeType(buf, lightType);
        if (action == LightSettingsPacketCodec.UPDATE) {
            LightSettingsPacketCodec.writeSettings(buf, settings);
        } else if (action == LightSettingsPacketCodec.ADD_ACCESS || action == LightSettingsPacketCodec.REMOVE_ACCESS
                || action == LightSettingsPacketCodec.RANGE_VISIBLE) {
            buf.writeString(accessValue == null ? "" : accessValue);
        }
        ClientSidePacketRegistry.INSTANCE.sendToServer(REQUEST_ID, buf);
    }

    private static void sendLegacySnapshot(ServerPlayerEntity player, BlockPos pos, LightType lightType, LightSettingsView snapshot)
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        LightSettingsPacketCodec.writeType(buf, lightType);
        LightSettingsPacketCodec.writeSnapshot(buf, snapshot);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, SNAPSHOT_ID, buf);
    }
    *///?}

    //? if >=1.20.6 {

    public static final class RequestPayload implements CustomPayload
    {
        static final Id<RequestPayload> ID = new Id<>(REQUEST_ID);
        static final PacketCodec<RegistryByteBuf, RequestPayload> CODEC = PacketCodec.ofStatic(RequestPayload::write, RequestPayload::read);

        private final int action;
        private final BlockPos pos;
        private final LightType lightType;
        private final LightSettings settings;
        private final String accessValue;

        RequestPayload(int action, BlockPos pos, LightType lightType, LightSettings settings, String accessValue)
        {
            this.action = action;
            this.pos = pos;
            this.lightType = lightType;
            this.settings = settings;
            this.accessValue = accessValue == null ? "" : accessValue;
        }

        @Override
        public Id<? extends CustomPayload> getId()
        {
            return ID;
        }

        private static RequestPayload read(PacketByteBuf buf)
        {
            int action = buf.readInt();
            BlockPos pos = buf.readBlockPos();
            LightType lightType = LightSettingsPacketCodec.readType(buf);
            LightSettings settings = action == LightSettingsPacketCodec.UPDATE ? LightSettingsPacketCodec.readSettings(buf) : LightSettings.unconfigured();
            String accessValue = action == LightSettingsPacketCodec.ADD_ACCESS || action == LightSettingsPacketCodec.REMOVE_ACCESS
                    || action == LightSettingsPacketCodec.RANGE_VISIBLE ? buf.readString() : "";
            return new RequestPayload(action, pos, lightType, settings, accessValue);
        }

        private static void write(PacketByteBuf buf, RequestPayload payload)
        {
            buf.writeInt(payload.action);
            buf.writeBlockPos(payload.pos);
            LightSettingsPacketCodec.writeType(buf, payload.lightType);
            if (payload.action == LightSettingsPacketCodec.UPDATE) {
                LightSettingsPacketCodec.writeSettings(buf, payload.settings);
            } else if (payload.action == LightSettingsPacketCodec.ADD_ACCESS || payload.action == LightSettingsPacketCodec.REMOVE_ACCESS
                    || payload.action == LightSettingsPacketCodec.RANGE_VISIBLE) {
                buf.writeString(payload.accessValue);
            }
        }
    }

    public static final class SnapshotPayload implements CustomPayload
    {
        static final Id<SnapshotPayload> ID = new Id<>(SNAPSHOT_ID);
        static final PacketCodec<RegistryByteBuf, SnapshotPayload> CODEC = PacketCodec.ofStatic(SnapshotPayload::write, SnapshotPayload::read);

        private final BlockPos pos;
        private final LightType lightType;
        private final LightSettingsView snapshot;

        SnapshotPayload(BlockPos pos, LightType lightType, LightSettingsView snapshot)
        {
            this.pos = pos;
            this.lightType = lightType;
            this.snapshot = snapshot;
        }

        @Override
        public Id<? extends CustomPayload> getId()
        {
            return ID;
        }

        private static SnapshotPayload read(PacketByteBuf buf)
        {
            BlockPos pos = buf.readBlockPos();
            LightType lightType = LightSettingsPacketCodec.readType(buf);
            return new SnapshotPayload(pos, lightType, LightSettingsPacketCodec.readSnapshot(buf));
        }

        private static void write(PacketByteBuf buf, SnapshotPayload payload)
        {
            buf.writeBlockPos(payload.pos);
            LightSettingsPacketCodec.writeType(buf, payload.lightType);
            LightSettingsPacketCodec.writeSnapshot(buf, payload.snapshot);
        }
    }
    //?}
}
