package net.xalcon.torchmaster.client;

import java.util.ArrayList;
import java.util.List;

final class TorchmasterSwitchControl
{
    static final int WIDTH = 36;
    static final int HEIGHT = 14;
    private static final int THUMB_SIZE = 10;
    private static final int THUMB_INSET = 2;
    private static final int ON_COLOR = 0xFF2D9CDB;
    private static final int OFF_COLOR = 0xFF606060;
    private static final int ON_DISABLED_COLOR = 0xFF39708E;
    private static final int OFF_DISABLED_COLOR = 0xFF454545;
    private static final int THUMB_COLOR = 0xFFFFFFFF;
    private static final int THUMB_DISABLED_COLOR = 0xFFB0B0B0;

    private TorchmasterSwitchControl()
    {
    }

    static TorchmasterPanelRenderer.Fill[] fills(int x, int y, boolean on, boolean active)
    {
        List<TorchmasterPanelRenderer.Fill> fills = new ArrayList<>();
        int trackColor = trackColor(on, active);
        int thumbColor = active ? THUMB_COLOR : THUMB_DISABLED_COLOR;
        fills.add(TorchmasterPanelRenderer.fill(x + 4, y, x + WIDTH - 4, y + HEIGHT, trackColor));
        fills.add(TorchmasterPanelRenderer.fill(x, y + 4, x + WIDTH, y + HEIGHT - 4, trackColor));
        int thumbLeft = on ? x + WIDTH - THUMB_SIZE - THUMB_INSET : x + THUMB_INSET;
        int thumbTop = y + (HEIGHT - THUMB_SIZE) / 2;
        fills.add(TorchmasterPanelRenderer.fill(thumbLeft + 2, thumbTop, thumbLeft + THUMB_SIZE - 2, thumbTop + THUMB_SIZE, thumbColor));
        fills.add(TorchmasterPanelRenderer.fill(thumbLeft, thumbTop + 2, thumbLeft + THUMB_SIZE, thumbTop + THUMB_SIZE - 2, thumbColor));
        return fills.toArray(new TorchmasterPanelRenderer.Fill[0]);
    }

    static boolean contains(int x, int y, double mouseX, double mouseY)
    {
        return mouseX >= x && mouseX < x + WIDTH && mouseY >= y && mouseY < y + HEIGHT;
    }

    private static int trackColor(boolean on, boolean active)
    {
        if (active) {
            return on ? ON_COLOR : OFF_COLOR;
        }
        return on ? ON_DISABLED_COLOR : OFF_DISABLED_COLOR;
    }
}
