package net.xalcon.torchmaster.client;

import net.minecraft.client.MinecraftClient;
//? if >=1.15
import net.minecraft.client.render.VertexConsumerProvider;
//? if >=1.15
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.xalcon.torchmaster.TorchmasterClientBridge;

public final class TorchmasterClientLifecycle
{
    private static String lastSyncedWorldKey;
    private static int lastSyncedChunkX = Integer.MIN_VALUE;
    private static int lastSyncedChunkZ = Integer.MIN_VALUE;

    private TorchmasterClientLifecycle()
    {
    }

    public static void installLightScreenOpener()
    {
        TorchmasterClientBridge.setLightScreenOpener(TorchmasterLightScreen::open);
    }

    public static void onEndClientTick()
    {
        syncVisibleRangesWhenScopeChanges();
        TorchmasterLightRangeDisplay.tick();
    }

    private static void syncVisibleRangesWhenScopeChanges()
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.world == null || minecraft.player == null) {
            resetVisibleRangeSyncState();
            return;
        }
        String worldKey = currentWorldKey(minecraft);
        BlockPos playerPos = minecraft.player.getBlockPos();
        int chunkX = playerPos.getX() >> 4;
        int chunkZ = playerPos.getZ() >> 4;
        boolean changedWorld = !worldKey.equals(lastSyncedWorldKey);
        if (!changedWorld && chunkX == lastSyncedChunkX && chunkZ == lastSyncedChunkZ) {
            return;
        }
        lastSyncedWorldKey = worldKey;
        lastSyncedChunkX = chunkX;
        lastSyncedChunkZ = chunkZ;
        if (changedWorld) {
            TorchmasterLightRangeDisplay.clear();
        }
        LightSettingsClientNetworking.queryVisibleRanges();
    }

    private static void resetVisibleRangeSyncState()
    {
        lastSyncedWorldKey = null;
        lastSyncedChunkX = Integer.MIN_VALUE;
        lastSyncedChunkZ = Integer.MIN_VALUE;
    }

    private static String currentWorldKey(MinecraftClient minecraft)
    {
        //? if >=1.16.5 {
        return minecraft.world.getRegistryKey().getValue().toString();
        //?} else {
        /*return "legacy";
        *///?}
    }

    //? if >=1.15 {
    public static void renderCurrentRange(MatrixStack poseStack)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.world == null) {
            return;
        }
        TorchmasterLightRangeRenderer.render(minecraft.world, minecraft.gameRenderer.getCamera(), poseStack);
    }

    public static void renderCurrentRange(MatrixStack poseStack, VertexConsumerProvider bufferSource)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.world == null) {
            return;
        }
        TorchmasterLightRangeRenderer.render(minecraft.world, minecraft.gameRenderer.getCamera(), poseStack, bufferSource);
    }
    //?} else {
    /*public static void renderCurrentRange()
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.world == null) {
            return;
        }
        TorchmasterLightRangeRenderer.render(minecraft.world, minecraft.gameRenderer.getCamera());
    }
    *///?}
}
