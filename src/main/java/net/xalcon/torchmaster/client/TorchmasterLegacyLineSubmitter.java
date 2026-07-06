package net.xalcon.torchmaster.client;

//? if <1.15 {
/*import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
*///?}

final class TorchmasterLegacyLineSubmitter
{
    private TorchmasterLegacyLineSubmitter()
    {
    }

    //? if <1.15 {
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
}
