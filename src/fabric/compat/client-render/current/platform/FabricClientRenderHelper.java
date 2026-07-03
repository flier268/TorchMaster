package net.xalcon.torchmaster.platform;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.xalcon.torchmaster.ModRegistry;

public final class FabricClientRenderHelper {
    private FabricClientRenderHelper() {
    }

    public static void configureBlockRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModRegistry.blockDreadLamp.get(), RenderType.cutout());
    }
}
