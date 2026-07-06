package net.xalcon.torchmaster.mixin;

//? if <1.16 {
/*import net.minecraft.client.MinecraftClient;
*///?}
//? if <1.15
/*import net.minecraft.client.render.RenderLayer;*/
//? if <1.16
/*import net.minecraft.client.render.Camera;*/
//? if >=1.15 && <1.16 {
/*
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
*///?}
//? if <1.16 {
/*
import net.xalcon.torchmaster.client.TorchmasterLightRangeRenderer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?}
//? if <1.15 {
/*import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
*///?}
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldRenderer.class)
public abstract class LevelRendererMixin
{
    //? if <1.15 {
    /*@Inject(method = "renderLayer", at = @At("RETURN"))
    private void torchmaster$renderLightRanges(RenderLayer layer, Camera camera, CallbackInfoReturnable<Integer> cir)
    {
        if (layer != RenderLayer.TRANSLUCENT) {
            return;
        }
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.world != null) {
            TorchmasterLightRangeRenderer.render(minecraft.world, camera);
        }
    }
    *///?} else if <1.16 {
    /*@Inject(method = "render", at = @At("TAIL"))
    private void torchmaster$renderLightRanges(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.world != null) {
            TorchmasterLightRangeRenderer.render(minecraft.world, camera, matrices);
        }
    }
    *///?}
}
