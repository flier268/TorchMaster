package net.xalcon.torchmaster.network;

//? if >=1.16
import net.minecraft.network.PacketByteBuf;
//? if <1.16
//import net.minecraft.util.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
//? if >=1.20.6 {
import net.minecraft.network.NetworkSide;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
//?} else if >=1.18 <1.20.6 {
/*import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
*///?} else if >=1.17 <1.18 {
/*import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
*///?} else if <1.17 {
/*import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
*///?}
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

import java.util.function.Supplier;

public final class ForgeLightSettingsNetworking
{
    private static final SimpleChannel CHANNEL = createChannel();

    private ForgeLightSettingsNetworking()
    {
    }

    public static void register()
    {
        LightPreviewSyncService.setSender(ForgeLightSettingsNetworking::sendSnapshotToPlayer);
        BlockingLightLifecycle.setRemovalListener((level, pos, lightType) -> LightPreviewSyncService.syncWorld(level));
        //? if >=1.20.6 {
        CHANNEL.messageBuilder(RequestMessage.class)
                .encoder(RequestMessage::write)
                .decoder(RequestMessage::read)
                .direction(NetworkSide.SERVERBOUND)
                .consumerMainThread(ForgeLightSettingsNetworking::handleRequest)
                .add();
        CHANNEL.messageBuilder(SnapshotMessage.class)
                .encoder(SnapshotMessage::write)
                .decoder(SnapshotMessage::read)
                .direction(NetworkSide.CLIENTBOUND)
                .consumerMainThread(ForgeLightSettingsNetworking::handleSnapshot)
                .add();
        CHANNEL.build();
        //?} else if <1.20 {
        /*CHANNEL.messageBuilder(RequestMessage.class, 0)
                .encoder(RequestMessage::write)
                .decoder(RequestMessage::read)
                .consumer(ForgeLightSettingsNetworking::handleLegacyRequest)
                .add();
        CHANNEL.messageBuilder(SnapshotMessage.class, 1)
                .encoder(SnapshotMessage::write)
                .decoder(SnapshotMessage::read)
                .consumer(ForgeLightSettingsNetworking::handleLegacySnapshot)
                .add();
        *///?} else if >=1.20 <1.20.6 {
        /*CHANNEL.messageBuilder(RequestMessage.class, 0)
                .encoder(RequestMessage::write)
                .decoder(RequestMessage::read)
                .consumerMainThread(ForgeLightSettingsNetworking::handleLegacyRequest)
                .add();
        CHANNEL.messageBuilder(SnapshotMessage.class, 1)
                .encoder(SnapshotMessage::write)
                .decoder(SnapshotMessage::read)
                .consumerMainThread(ForgeLightSettingsNetworking::handleLegacySnapshot)
                .add();
        *///?}

    }

    public static void registerClient()
    {
        //? if >=1.20.6 {
        LightSettingsClientNetworking.setQuerySender((pos, lightType, ignored) ->
                CHANNEL.send(new RequestMessage(LightSettingsPacketCodec.QUERY, pos, lightType, LightSettings.unconfigured(), ""), PacketDistributor.SERVER.noArg()));
        LightSettingsClientNetworking.setVisibleRangesQuerySender(() ->
                CHANNEL.send(new RequestMessage(LightSettingsPacketCodec.VISIBLE_RANGES_QUERY, new BlockPos(0, 0, 0), LightType.MegaTorch, LightSettings.unconfigured(), ""), PacketDistributor.SERVER.noArg()));
        LightSettingsClientNetworking.setUpdateSender((pos, lightType, settings) ->
                CHANNEL.send(new RequestMessage(LightSettingsPacketCodec.UPDATE, pos, lightType, settings, ""), PacketDistributor.SERVER.noArg()));
        LightSettingsClientNetworking.setRangeVisibleSender((pos, lightType, visible) ->
                CHANNEL.send(new RequestMessage(LightSettingsPacketCodec.RANGE_VISIBLE, pos, lightType, LightSettings.unconfigured(), visible), PacketDistributor.SERVER.noArg()));
        LightSettingsClientNetworking.setAddAccessSender((pos, lightType, playerName) ->
                CHANNEL.send(new RequestMessage(LightSettingsPacketCodec.ADD_ACCESS, pos, lightType, LightSettings.unconfigured(), playerName), PacketDistributor.SERVER.noArg()));
        LightSettingsClientNetworking.setRemoveAccessSender((pos, lightType, playerUuid) ->
                CHANNEL.send(new RequestMessage(LightSettingsPacketCodec.REMOVE_ACCESS, pos, lightType, LightSettings.unconfigured(), playerUuid), PacketDistributor.SERVER.noArg()));
        //?} else if <1.20.6 {
        /*LightSettingsClientNetworking.setQuerySender((pos, lightType, ignored) ->
                CHANNEL.sendToServer(new RequestMessage(LightSettingsPacketCodec.QUERY, pos, lightType, LightSettings.unconfigured(), "")));
        LightSettingsClientNetworking.setVisibleRangesQuerySender(() ->
                CHANNEL.sendToServer(new RequestMessage(LightSettingsPacketCodec.VISIBLE_RANGES_QUERY, new BlockPos(0, 0, 0), LightType.MegaTorch, LightSettings.unconfigured(), "")));
        LightSettingsClientNetworking.setUpdateSender((pos, lightType, settings) ->
                CHANNEL.sendToServer(new RequestMessage(LightSettingsPacketCodec.UPDATE, pos, lightType, settings, "")));
        LightSettingsClientNetworking.setRangeVisibleSender((pos, lightType, visible) ->
                CHANNEL.sendToServer(new RequestMessage(LightSettingsPacketCodec.RANGE_VISIBLE, pos, lightType, LightSettings.unconfigured(), visible)));
        LightSettingsClientNetworking.setAddAccessSender((pos, lightType, playerName) ->
                CHANNEL.sendToServer(new RequestMessage(LightSettingsPacketCodec.ADD_ACCESS, pos, lightType, LightSettings.unconfigured(), playerName)));
        LightSettingsClientNetworking.setRemoveAccessSender((pos, lightType, playerUuid) ->
                CHANNEL.sendToServer(new RequestMessage(LightSettingsPacketCodec.REMOVE_ACCESS, pos, lightType, LightSettings.unconfigured(), playerUuid)));
        *///?}
    }

    private static SimpleChannel createChannel()
    {
        //? if >=1.20.6 {
        return ChannelBuilder
                .named(Identifier.of(Constants.MOD_ID, "light_settings"))
                .networkProtocolVersion(1)
                .optional()
                .simpleChannel();
        //?} else if <1.20.6 {
        /*return NetworkRegistry.newSimpleChannel(identifier("light_settings"), () -> "1", version -> true, version -> true);
        *///?}
    }

    private static Identifier identifier(String path)
    {
        //? if >=1.20.6 {
        return Identifier.of(Constants.MOD_ID, path);
        //?} else if <1.20.6 {
        /*return new Identifier(Constants.MOD_ID, path);
        *///?}
    }

    //? if >=1.20.6 {
    private static void handleRequest(RequestMessage message, CustomPayloadEvent.Context context)
    {
        if (context.getSender() == null) {
            return;
        }
        dispatch(context.getSender(), message);
    }
    //?}

    //? if <1.20.6 {
    /*private static void handleLegacyRequest(RequestMessage message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection() != NetworkDirection.PLAY_TO_SERVER || context.getSender() == null) {
            context.setPacketHandled(true);
            return;
        }
        context.enqueueWork(() -> dispatch(context.getSender(), message));
        context.setPacketHandled(true);
    }

    private static void handleLegacySnapshot(SnapshotMessage message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
            context.setPacketHandled(true);
            return;
        }
        context.enqueueWork(() -> TorchmasterLightScreen.receiveSnapshot(message.pos, message.lightType, message.snapshot));
        context.setPacketHandled(true);
    }
    *///?}

    private static void dispatch(ServerPlayerEntity player, RequestMessage message)
    {
        if (message.action == LightSettingsPacketCodec.UPDATE) {
            LightSettingsNetworkHandler.handleUpdate(player, message.pos, message.lightType, message.settings, ForgeLightSettingsNetworking::sendSnapshotToPlayer);
        } else if (message.action == LightSettingsPacketCodec.ADD_ACCESS) {
            LightSettingsNetworkHandler.handleAddAccess(player, message.pos, message.lightType, message.accessValue, ForgeLightSettingsNetworking::sendSnapshotToPlayer);
        } else if (message.action == LightSettingsPacketCodec.REMOVE_ACCESS) {
            LightSettingsNetworkHandler.handleRemoveAccess(player, message.pos, message.lightType, message.accessValue, ForgeLightSettingsNetworking::sendSnapshotToPlayer);
        } else if (message.action == LightSettingsPacketCodec.RANGE_VISIBLE) {
            LightSettingsNetworkHandler.handleRangeVisible(player, message.pos, message.lightType, Boolean.parseBoolean(message.accessValue),
                    ForgeLightSettingsNetworking::sendSnapshotToPlayer);
        } else if (message.action == LightSettingsPacketCodec.VISIBLE_RANGES_QUERY) {
            LightSettingsNetworkHandler.handleVisibleRangesQuery(player, ForgeLightSettingsNetworking::sendSnapshotToPlayer);
        } else {
            LightSettingsNetworkHandler.handleQuery(player, message.pos, message.lightType, ForgeLightSettingsNetworking::sendSnapshotToPlayer);
        }
    }

    private static void sendSnapshotToPlayer(ServerPlayerEntity player, BlockPos pos, LightType lightType, LightSettingsView snapshot)
    {
        //? if >=1.20.6 {
        CHANNEL.send(new SnapshotMessage(pos, lightType, snapshot), PacketDistributor.PLAYER.with(player));
        //?} else if <1.20.6 {
        /*CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SnapshotMessage(pos, lightType, snapshot));
        *///?}
    }

    //? if >=1.20.6 {
    private static void handleSnapshot(SnapshotMessage message, CustomPayloadEvent.Context context)
    {
        TorchmasterLightScreen.receiveSnapshot(message.pos, message.lightType, message.snapshot);
    }
    //?}

    private static final class RequestMessage
    {
        private final int action;
        private final BlockPos pos;
        private final LightType lightType;
        private final LightSettings settings;
        private final String accessValue;

        private RequestMessage(int action, BlockPos pos, LightType lightType, LightSettings settings, String accessValue)
        {
            this.action = action;
            this.pos = pos;
            this.lightType = lightType;
            this.settings = settings;
            this.accessValue = accessValue == null ? "" : accessValue;
        }

        private static RequestMessage read(PacketByteBuf buf)
        {
            int action = buf.readInt();
            BlockPos pos = buf.readBlockPos();
            LightType lightType = LightSettingsPacketCodec.readType(buf);
            LightSettings settings = action == LightSettingsPacketCodec.UPDATE ? LightSettingsPacketCodec.readSettings(buf) : LightSettings.unconfigured();
            String accessValue = action == LightSettingsPacketCodec.ADD_ACCESS || action == LightSettingsPacketCodec.REMOVE_ACCESS
                    || action == LightSettingsPacketCodec.RANGE_VISIBLE ? buf.readString() : "";
            return new RequestMessage(action, pos, lightType, settings, accessValue);
        }

        private static void write(RequestMessage message, PacketByteBuf buf)
        {
            buf.writeInt(message.action);
            buf.writeBlockPos(message.pos);
            LightSettingsPacketCodec.writeType(buf, message.lightType);
            if (message.action == LightSettingsPacketCodec.UPDATE) {
                LightSettingsPacketCodec.writeSettings(buf, message.settings);
            } else if (message.action == LightSettingsPacketCodec.ADD_ACCESS || message.action == LightSettingsPacketCodec.REMOVE_ACCESS
                    || message.action == LightSettingsPacketCodec.RANGE_VISIBLE) {
                buf.writeString(message.accessValue);
            }
        }
    }

    private static final class SnapshotMessage
    {
        private final BlockPos pos;
        private final LightType lightType;
        private final LightSettingsView snapshot;

        private SnapshotMessage(BlockPos pos, LightType lightType, LightSettingsView snapshot)
        {
            this.pos = pos;
            this.lightType = lightType;
            this.snapshot = snapshot;
        }

        private static SnapshotMessage read(PacketByteBuf buf)
        {
            BlockPos pos = buf.readBlockPos();
            LightType lightType = LightSettingsPacketCodec.readType(buf);
            return new SnapshotMessage(pos, lightType, LightSettingsPacketCodec.readSnapshot(buf));
        }

        private static void write(SnapshotMessage message, PacketByteBuf buf)
        {
            buf.writeBlockPos(message.pos);
            LightSettingsPacketCodec.writeType(buf, message.lightType);
            LightSettingsPacketCodec.writeSnapshot(buf, message.snapshot);
        }
    }
}
