package net.xalcon.torchmaster;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

@Environment(EnvType.CLIENT)
public class TorchmasterFabricClient implements ClientModInitializer {
    public void onInitializeClient()
    {
        BlockRenderLayerMap.putBlock(ModRegistry.blockDreadLamp.get(), ChunkSectionLayer.CUTOUT);
    }
}