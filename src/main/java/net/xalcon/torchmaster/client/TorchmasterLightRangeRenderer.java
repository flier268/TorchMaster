package net.xalcon.torchmaster.client;

//? if <1.15 {
/*import com.mojang.blaze3d.platform.GlStateManager;
*///?}
import net.minecraft.client.MinecraftClient;
//? if >=1.21.11
//import net.minecraft.client.gl.RenderPipelines;
//? if <1.15
/*import net.minecraft.client.render.BufferBuilder;*/
import net.minecraft.client.render.Camera;
//? if >=1.15
import net.minecraft.client.render.RenderLayer;
//? if >=1.21.11
//import net.minecraft.client.render.RenderSetup;
//? if >=1.15
import net.minecraft.client.render.VertexConsumer;
//? if >=1.15
import net.minecraft.client.render.VertexConsumerProvider;
//? if <1.15
/*import net.minecraft.client.render.Tessellator;*/
//? if <1.15
/*import net.minecraft.client.render.VertexFormats;*/
import net.minecraft.client.render.WorldRenderer;
//? if >=1.15
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
//? if <1.15
/*import net.minecraft.util.math.Vec3d;*/
import net.minecraft.world.World;

public final class TorchmasterLightRangeRenderer
{
    //? if >=1.21.11
    //private static final RenderLayer LINE_LAYER = RenderLayer.of("torchmaster_lines", RenderSetup.builder(RenderPipelines.LINES).build());

    private TorchmasterLightRangeRenderer()
    {
    }

    //? if >=1.15 {
    public static void render(World level, Camera camera, MatrixStack poseStack)
    {
        VertexConsumerProvider.Immediate bufferSource = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        renderAndFlush(level, camera, poseStack, bufferSource);
    }
    //?} else {
    /*public static void render(World level, Camera camera)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player == null || minecraft.player.isSneaking()) {
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.lineWidth(TorchmasterLineBoxRenderer.LINE_WIDTH);
        buffer.begin(1, VertexFormats.POSITION_COLOR);
        for (TorchmasterLightRangeDisplay.RangeSnapshot snapshot : TorchmasterLightRangeDisplay.snapshots(level)) {
            renderRangeBox(snapshot.pos, snapshot.radius, camera, buffer);
            for (BlockPos pos : snapshot.randomAirBlocks) {
                TorchmasterRangeBoxes.Box box = TorchmasterRangeBoxes.sampleBox(pos);
                renderLineBox(camera, buffer, box, TorchmasterLineBoxRenderer.SAMPLE_STYLE);
            }
        }
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture();
    }
    *///?}

    //? if >=1.15 {
    public static void renderAndFlush(World level, Camera camera, MatrixStack poseStack, VertexConsumerProvider.Immediate bufferSource)
    {
        render(level, camera, poseStack, bufferSource);
        flushLines(bufferSource);
    }

    public static void render(World level, Camera camera, MatrixStack poseStack, VertexConsumerProvider bufferSource)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player == null || minecraft.player.isSneaking()) {
            return;
        }

        //? if >=1.21.11 {
        /*VertexConsumer lineBuffer = bufferSource.getBuffer(LINE_LAYER);
        *///?} else {
        VertexConsumer lineBuffer = bufferSource.getBuffer(RenderLayer.getLines());
        //?}

        poseStack.push();
        //? if >=1.21.11 {
        /*poseStack.translate(-camera.getCameraPos().x, -camera.getCameraPos().y, -camera.getCameraPos().z);
        *///?} else {
        poseStack.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);
        //?}
        for (TorchmasterLightRangeDisplay.RangeSnapshot snapshot : TorchmasterLightRangeDisplay.snapshots(level)) {
            renderBox(TorchmasterRangeBoxes.rangeBox(snapshot.pos, snapshot.radius), poseStack, lineBuffer, TorchmasterLineBoxRenderer.RANGE_STYLE);
            for (BlockPos pos : snapshot.randomAirBlocks) {
                TorchmasterRangeBoxes.Box box = TorchmasterRangeBoxes.sampleBox(pos);
                renderBox(box, poseStack, lineBuffer, TorchmasterLineBoxRenderer.SAMPLE_STYLE);
            }
        }
        poseStack.pop();
    }

    private static void flushLines(VertexConsumerProvider.Immediate bufferSource)
    {
        //? if >=1.21.11 {
        /*bufferSource.draw(LINE_LAYER);
        *///?} else {
        bufferSource.draw(RenderLayer.getLines());
        //?}
    }

    private static void renderBox(TorchmasterRangeBoxes.Box box, MatrixStack poseStack, VertexConsumer lineBuffer, TorchmasterLineBoxRenderer.Style style)
    {
        //? if >=1.21.11 {
        /*renderLineBox(poseStack, lineBuffer, box, style);
        *///?} else {
        WorldRenderer.drawBox(poseStack, lineBuffer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, style.red, style.green, style.blue, style.alpha);
        //?}
    }
    //?} else {
    /*private static void renderRangeBox(BlockPos center, int radius, Camera camera, BufferBuilder buffer)
    {
        TorchmasterRangeBoxes.Box box = TorchmasterRangeBoxes.rangeBox(center, radius);
        renderLineBox(camera, buffer, box, TorchmasterLineBoxRenderer.RANGE_STYLE);
    }

    private static void renderLineBox(Camera camera, BufferBuilder buffer, TorchmasterRangeBoxes.Box box, TorchmasterLineBoxRenderer.Style style)
    {
        Vec3d cameraPos = camera.getPos();
        for (TorchmasterLineBoxRenderer.Line line : TorchmasterLineBoxRenderer.lines(box)) {
            line(buffer,
                    line.startX - cameraPos.x, line.startY - cameraPos.y, line.startZ - cameraPos.z,
                    line.endX - cameraPos.x, line.endY - cameraPos.y, line.endZ - cameraPos.z,
                    style);
        }
    }

    private static void line(BufferBuilder buffer, double startX, double startY, double startZ, double endX, double endY, double endZ, TorchmasterLineBoxRenderer.Style style)
    {
        buffer.vertex(startX, startY, startZ).color(style.red, style.green, style.blue, style.alpha).next();
        buffer.vertex(endX, endY, endZ).color(style.red, style.green, style.blue, style.alpha).next();
    }
    *///?}

    //? if >=1.21.11 {
    /*private static void renderLineBox(MatrixStack poseStack, VertexConsumer lineBuffer, TorchmasterRangeBoxes.Box box, TorchmasterLineBoxRenderer.Style style)
    {
        for (TorchmasterLineBoxRenderer.Line line : TorchmasterLineBoxRenderer.lines(box)) {
            line(poseStack, lineBuffer, line, style);
        }
    }

    private static void line(MatrixStack poseStack, VertexConsumer lineBuffer, TorchmasterLineBoxRenderer.Line line, TorchmasterLineBoxRenderer.Style style)
    {
        MatrixStack.Entry entry = poseStack.peek();
        float normalX = (float)(line.endX - line.startX);
        float normalY = (float)(line.endY - line.startY);
        float normalZ = (float)(line.endZ - line.startZ);
        lineBuffer.vertex(entry, (float)line.startX, (float)line.startY, (float)line.startZ)
                .color(style.red, style.green, style.blue, style.alpha)
                .normal(entry, normalX, normalY, normalZ)
                .lineWidth(TorchmasterLineBoxRenderer.LINE_WIDTH);
        lineBuffer.vertex(entry, (float)line.endX, (float)line.endY, (float)line.endZ)
                .color(style.red, style.green, style.blue, style.alpha)
                .normal(entry, normalX, normalY, normalZ)
                .lineWidth(TorchmasterLineBoxRenderer.LINE_WIDTH);
    }
    *///?} elif fabric && forge && >=1.21.9 {
    /*private static void renderLineBox(PoseStack poseStack, VertexConsumer lineBuffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        ShapeRenderer.renderLineBox(poseStack.last(), lineBuffer, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
    }
    *///?} elif fabric && forge && >=1.21.2 {
    /*private static void renderLineBox(PoseStack poseStack, VertexConsumer lineBuffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        ShapeRenderer.renderLineBox(poseStack, lineBuffer, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
    }
    *///?}
}
