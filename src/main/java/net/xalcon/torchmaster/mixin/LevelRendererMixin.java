package net.xalcon.torchmaster.mixin;

//? if fabric && >=1.21.9 {
/*import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.xalcon.torchmaster.client.TorchmasterLightRangeRenderer;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin
{
    @Inject(
            method = "renderLevel(Lcom/mojang/blaze3d/resource/GraphicsResourceAllocator;Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/Camera;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Z)V",
            at = @At("TAIL")
    )
    private void torchmaster_renderLevel_tail(GraphicsResourceAllocator allocator, DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, Matrix4f frustumMatrix, Matrix4f projectionMatrix, Matrix4f modelViewMatrix, GpuBufferSlice fogBuffer, Vector4f fogColor, boolean renderSky, CallbackInfo ci)
    {
        renderRange(camera);
    }

    private static void renderRange(Camera camera)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        TorchmasterLightRangeRenderer.render(minecraft.level, camera, new PoseStack());
    }
}
*///?} else if fabric && >=1.21.8 {
/*import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.xalcon.torchmaster.client.TorchmasterLightRangeRenderer;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin
{
    @Inject(
            method = "renderLevel(Lcom/mojang/blaze3d/resource/GraphicsResourceAllocator;Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/Camera;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Z)V",
            at = @At("TAIL")
    )
    private void torchmaster_renderLevel_tail(GraphicsResourceAllocator allocator, DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, Matrix4f frustumMatrix, Matrix4f projectionMatrix, GpuBufferSlice fogBuffer, Vector4f fogColor, boolean renderSky, CallbackInfo ci)
    {
        renderRange(camera);
    }

    private static void renderRange(Camera camera)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        TorchmasterLightRangeRenderer.render(minecraft.level, camera, new PoseStack());
    }
}
*///?} else if fabric {
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin
{
}
//?}
