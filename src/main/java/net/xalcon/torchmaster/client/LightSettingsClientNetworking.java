package net.xalcon.torchmaster.client;

import net.minecraft.util.math.BlockPos;
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.domain.LightSettings;

public final class LightSettingsClientNetworking
{
    @FunctionalInterface
    public interface Sender
    {
        void send(BlockPos pos, LightType lightType, LightSettings settings);
    }

    @FunctionalInterface
    public interface AccessSender
    {
        void send(BlockPos pos, LightType lightType, String player);
    }

    private static Sender querySender = (pos, lightType, settings) -> {};
    private static Runnable visibleRangesQuerySender = () -> {};
    private static Sender updateSender = (pos, lightType, settings) -> {};
    private static AccessSender rangeVisibleSender = (pos, lightType, visible) -> {};
    private static AccessSender addAccessSender = (pos, lightType, player) -> {};
    private static AccessSender removeAccessSender = (pos, lightType, player) -> {};

    private LightSettingsClientNetworking()
    {
    }

    public static void setQuerySender(Sender sender)
    {
        querySender = sender == null ? (pos, lightType, settings) -> {} : sender;
    }

    public static void setVisibleRangesQuerySender(Runnable sender)
    {
        visibleRangesQuerySender = sender == null ? () -> {} : sender;
    }

    public static void setUpdateSender(Sender sender)
    {
        updateSender = sender == null ? (pos, lightType, settings) -> {} : sender;
    }

    public static void setRangeVisibleSender(AccessSender sender)
    {
        rangeVisibleSender = sender == null ? (pos, lightType, visible) -> {} : sender;
    }

    public static void setAddAccessSender(AccessSender sender)
    {
        addAccessSender = sender == null ? (pos, lightType, player) -> {} : sender;
    }

    public static void setRemoveAccessSender(AccessSender sender)
    {
        removeAccessSender = sender == null ? (pos, lightType, player) -> {} : sender;
    }

    public static void query(BlockPos pos, LightType lightType)
    {
        querySender.send(pos, lightType, LightSettings.unconfigured());
    }

    public static void queryVisibleRanges()
    {
        visibleRangesQuerySender.run();
    }

    public static void update(BlockPos pos, LightType lightType, LightSettings settings)
    {
        updateSender.send(pos, lightType, settings);
    }

    public static void rangeVisible(BlockPos pos, LightType lightType, boolean visible)
    {
        rangeVisibleSender.send(pos, lightType, Boolean.toString(visible));
    }

    public static void addAccess(BlockPos pos, LightType lightType, String playerName)
    {
        addAccessSender.send(pos, lightType, playerName);
    }

    public static void removeAccess(BlockPos pos, LightType lightType, String playerUuid)
    {
        removeAccessSender.send(pos, lightType, playerUuid);
    }
}
