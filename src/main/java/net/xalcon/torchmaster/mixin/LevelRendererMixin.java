package net.xalcon.torchmaster.mixin;

//? if fabric && 1.21.9 {
/*import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.xalcon.torchmaster.client.TorchmasterLightRangeRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.Vec3;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin
{
    @Inject(
            method = "method_62214",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/debug/DebugRenderer;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/culling/Frustum;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;DDDZ)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void torchmaster_renderBeforeDebug(GpuBufferSlice fogBuffer, LevelRenderState levelRenderState, ProfilerFiller profiler, Matrix4f modelViewMatrix, ResourceHandle<?> resourceHandle, ResourceHandle<?> resourceHandle2, boolean renderSky, Frustum frustum, ResourceHandle<?> resourceHandle3, ResourceHandle<?> resourceHandle4, CallbackInfo ci, Vec3 cameraPos, double cameraX, double cameraY, double cameraZ, ChunkSectionsToRender chunkSectionsToRender, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, MultiBufferSource.BufferSource crumblingBufferSource)
    {
        renderRange(poseStack, bufferSource);
    }

    private static void renderRange(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        TorchmasterLightRangeRenderer.renderAndFlush(minecraft.level, minecraft.gameRenderer.getMainCamera(), poseStack, bufferSource);
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
