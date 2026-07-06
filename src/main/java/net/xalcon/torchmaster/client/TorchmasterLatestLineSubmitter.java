package net.xalcon.torchmaster.client;

//? if >=1.15
import net.minecraft.client.render.VertexConsumer;
//? if >=1.15
import net.minecraft.client.util.math.MatrixStack;

final class TorchmasterLatestLineSubmitter
{
    private TorchmasterLatestLineSubmitter()
    {
    }

    //? if >=1.21.11 {
    /*static void submitBox(MatrixStack poseStack, VertexConsumer lineBuffer, TorchmasterRangeBoxes.Box box, TorchmasterLineBoxRenderer.Style style)
    {
        for (TorchmasterLineBoxRenderer.Line line : TorchmasterLineBoxRenderer.lines(box)) {
            submitLine(poseStack, lineBuffer, line, style);
        }
    }

    private static void submitLine(MatrixStack poseStack, VertexConsumer lineBuffer, TorchmasterLineBoxRenderer.Line line, TorchmasterLineBoxRenderer.Style style)
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
