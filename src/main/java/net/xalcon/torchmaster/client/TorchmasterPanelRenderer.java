package net.xalcon.torchmaster.client;

public final class TorchmasterPanelRenderer
{
    public static final int BACKGROUND_COLOR = 0xAA101010;
    public static final int FRAME_LIGHT_COLOR = 0xFF404040;
    public static final int FRAME_DARK_COLOR = 0xFF202020;
    public static final int TITLE_COLOR = 0xFFFFFFFF;
    public static final int LABEL_COLOR = 0xFFE0E0E0;
    public static final int UNIT_COLOR = 0xFFAAAAAA;
    public static final int RANGE_COLOR = 0xFFA0FFA0;

    private TorchmasterPanelRenderer()
    {
    }

    public static Fill background(int left, int top, int right, int bottom)
    {
        return new Fill(left, top, right, bottom, BACKGROUND_COLOR);
    }

    public static Fill[] frame(int left, int top, int right, int bottom)
    {
        return new Fill[] {
                new Fill(left, top, right, top + 1, FRAME_LIGHT_COLOR),
                new Fill(left, top, left + 1, bottom, FRAME_LIGHT_COLOR),
                new Fill(left, bottom - 1, right, bottom, FRAME_DARK_COLOR),
                new Fill(right - 1, top, right, bottom, FRAME_DARK_COLOR)
        };
    }

    public static final class Fill
    {
        public final int left;
        public final int top;
        public final int right;
        public final int bottom;
        public final int color;

        private Fill(int left, int top, int right, int bottom, int color)
        {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            this.color = color;
        }
    }
}
