package net.xalcon.torchmaster.client;

//? if <1.15
/*import net.minecraft.client.render.BufferBuilder;*/
import net.minecraft.client.render.Camera;
//? if >=1.15
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
//? if >=1.15
import net.minecraft.client.util.math.MatrixStack;
//? if <1.15
/*import net.minecraft.util.math.Vec3d;*/

final class TorchmasterRangeLineSubmitter
{
    private TorchmasterRangeLineSubmitter()
    {
    }

    //? if >=1.15 {
    static void submitBox(MatrixStack poseStack, VertexConsumer lineBuffer, TorchmasterRangeBoxes.Box box, TorchmasterLineBoxRenderer.Style style)
    {
        //? if >=1.21.11 {
        /*for (TorchmasterLineBoxRenderer.Line line : TorchmasterLineBoxRenderer.lines(box)) {
            submitLine(poseStack, lineBuffer, line, style);
        }
        *///?} else {
        WorldRenderer.drawBox(poseStack, lineBuffer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, style.red, style.green, style.blue, style.alpha);
        //?}
    }
    //?} else {
    /*static void submitBox(Camera camera, BufferBuilder buffer, TorchmasterRangeBoxes.Box box, TorchmasterLineBoxRenderer.Style style)
    {
        Vec3d cameraPos = camera.getPos();
        for (TorchmasterLineBoxRenderer.Line line : TorchmasterLineBoxRenderer.lines(box)) {
            submitLine(buffer,
                    line.startX - cameraPos.x, line.startY - cameraPos.y, line.startZ - cameraPos.z,
                    line.endX - cameraPos.x, line.endY - cameraPos.y, line.endZ - cameraPos.z,
                    style);
        }
    }

    private static void submitLine(BufferBuilder buffer, double startX, double startY, double startZ, double endX, double endY, double endZ, TorchmasterLineBoxRenderer.Style style)
    {
        buffer.vertex(startX, startY, startZ).color(style.red, style.green, style.blue, style.alpha).next();
        buffer.vertex(endX, endY, endZ).color(style.red, style.green, style.blue, style.alpha).next();
    }
    *///?}

    //? if >=1.21.11 {
    /*private static void submitLine(MatrixStack poseStack, VertexConsumer lineBuffer, TorchmasterLineBoxRenderer.Line line, TorchmasterLineBoxRenderer.Style style)
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
    *///?}
}
