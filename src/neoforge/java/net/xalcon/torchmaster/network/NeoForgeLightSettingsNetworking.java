package net.xalcon.torchmaster.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
//? if >=1.21.11
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
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

public final class NeoForgeLightSettingsNetworking
{
    private NeoForgeLightSettingsNetworking()
    {
    }

    public static void register(RegisterPayloadHandlersEvent event)
    {
        LightPreviewSyncService.setSender(NeoForgeLightSettingsNetworking::sendSnapshotToPlayer);
        BlockingLightLifecycle.setRemovalListener((level, pos, lightType) -> LightPreviewSyncService.syncWorld(level));
        PayloadRegistrar registrar = event.registrar("1").optional();
        registrar.playToServer(RequestPayload.ID, RequestPayload.CODEC, NeoForgeLightSettingsNetworking::handleRequest);
        registrar.playToClient(SnapshotPayload.ID, SnapshotPayload.CODEC, NeoForgeLightSettingsNetworking::handleSnapshot);
    }

    public static void registerClient()
    {
        LightSettingsClientNetworking.setQuerySender((pos, lightType, ignored) ->
                sendToServer(new RequestPayload(LightSettingsPacketCodec.QUERY, pos, lightType, LightSettings.unconfigured(), "")));
        LightSettingsClientNetworking.setVisibleRangesQuerySender(() ->
                sendToServer(new RequestPayload(LightSettingsPacketCodec.VISIBLE_RANGES_QUERY, new BlockPos(0, 0, 0), LightType.MegaTorch, LightSettings.unconfigured(), "")));
        LightSettingsClientNetworking.setUpdateSender((pos, lightType, settings) ->
                sendToServer(new RequestPayload(LightSettingsPacketCodec.UPDATE, pos, lightType, settings, "")));
        LightSettingsClientNetworking.setRangeVisibleSender((pos, lightType, visible) ->
                sendToServer(new RequestPayload(LightSettingsPacketCodec.RANGE_VISIBLE, pos, lightType, LightSettings.unconfigured(), visible)));
        LightSettingsClientNetworking.setAddAccessSender((pos, lightType, playerName) ->
                sendToServer(new RequestPayload(LightSettingsPacketCodec.ADD_ACCESS, pos, lightType, LightSettings.unconfigured(), playerName)));
        LightSettingsClientNetworking.setRemoveAccessSender((pos, lightType, playerUuid) ->
                sendToServer(new RequestPayload(LightSettingsPacketCodec.REMOVE_ACCESS, pos, lightType, LightSettings.unconfigured(), playerUuid)));
    }

    private static void sendToServer(RequestPayload payload)
    {
        //? if >=1.21.11 {
        ClientPacketDistributor.sendToServer(payload);
        //?} else if <1.21.11 {
        /*PacketDistributor.sendToServer(payload);
        *///?}
    }

    private static void handleRequest(RequestPayload payload, IPayloadContext context)
    {
        if (payload.action == LightSettingsPacketCodec.UPDATE) {
            LightSettingsNetworkHandler.handleUpdate((net.minecraft.server.network.ServerPlayerEntity) context.player(), payload.pos, payload.lightType, payload.settings,
                    NeoForgeLightSettingsNetworking::sendSnapshotToPlayer);
        } else if (payload.action == LightSettingsPacketCodec.ADD_ACCESS) {
            LightSettingsNetworkHandler.handleAddAccess((net.minecraft.server.network.ServerPlayerEntity) context.player(), payload.pos, payload.lightType, payload.accessValue,
                    NeoForgeLightSettingsNetworking::sendSnapshotToPlayer);
        } else if (payload.action == LightSettingsPacketCodec.REMOVE_ACCESS) {
            LightSettingsNetworkHandler.handleRemoveAccess((net.minecraft.server.network.ServerPlayerEntity) context.player(), payload.pos, payload.lightType, payload.accessValue,
                    NeoForgeLightSettingsNetworking::sendSnapshotToPlayer);
        } else if (payload.action == LightSettingsPacketCodec.RANGE_VISIBLE) {
            LightSettingsNetworkHandler.handleRangeVisible((net.minecraft.server.network.ServerPlayerEntity) context.player(), payload.pos, payload.lightType,
                    Boolean.parseBoolean(payload.accessValue), NeoForgeLightSettingsNetworking::sendSnapshotToPlayer);
        } else if (payload.action == LightSettingsPacketCodec.VISIBLE_RANGES_QUERY) {
            LightSettingsNetworkHandler.handleVisibleRangesQuery((net.minecraft.server.network.ServerPlayerEntity) context.player(),
                    NeoForgeLightSettingsNetworking::sendSnapshotToPlayer);
        } else {
            LightSettingsNetworkHandler.handleQuery((net.minecraft.server.network.ServerPlayerEntity) context.player(), payload.pos, payload.lightType,
                    NeoForgeLightSettingsNetworking::sendSnapshotToPlayer);
        }
    }

    private static void sendSnapshotToPlayer(ServerPlayerEntity player, BlockPos pos, LightType lightType, LightSettingsView snapshot)
    {
        PacketDistributor.sendToPlayer(player, new SnapshotPayload(pos, lightType, snapshot));
    }

    private static void handleSnapshot(SnapshotPayload payload, IPayloadContext context)
    {
        TorchmasterLightScreen.receiveSnapshot(payload.pos, payload.lightType, payload.snapshot);
    }

    public static final class RequestPayload implements CustomPayload
    {
        static final Id<RequestPayload> ID = new Id<>(Identifier.of(Constants.MOD_ID, "light_settings_request"));
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
        static final Id<SnapshotPayload> ID = new Id<>(Identifier.of(Constants.MOD_ID, "light_settings_snapshot"));
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
}
