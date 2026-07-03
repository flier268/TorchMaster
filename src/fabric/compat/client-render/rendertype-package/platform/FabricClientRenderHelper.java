package net.xalcon.torchmaster.platform;

import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.xalcon.torchmaster.ModRegistry;

public final class FabricClientRenderHelper {
    private FabricClientRenderHelper() {
    }

    public static void configureBlockRenderLayers() {
        BlockRenderLayerMap.putBlock(ModRegistry.blockDreadLamp.get(), ChunkSectionLayer.CUTOUT);
    }
}
