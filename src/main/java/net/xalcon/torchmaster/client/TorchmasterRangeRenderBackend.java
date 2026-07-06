package net.xalcon.torchmaster.client;

final class TorchmasterRangeRenderBackend
{
    private TorchmasterRangeRenderBackend()
    {
    }

    static LineLayerChoice activeLineLayer()
    {
        //? if >=1.21.11 {
        /*return LineLayerChoice.CUSTOM_PIPELINE;
        *///?} else {
        return LineLayerChoice.VANILLA_LINES;
        //?}
    }

    static FlushTarget activeFlushTarget()
    {
        //? if >=1.21.11 {
        /*return FlushTarget.CUSTOM_PIPELINE;
        *///?} else {
        return FlushTarget.VANILLA_LINES;
        //?}
    }

    static TorchmasterRangeRenderTarget.CameraOffset cameraOffset(double x, double y, double z)
    {
        return new TorchmasterRangeRenderTarget.CameraOffset(-x, -y, -z);
    }

    static TorchmasterRangeRenderTarget.LegacySessionState legacySessionState()
    {
        return new TorchmasterRangeRenderTarget.LegacySessionState(true, true, TorchmasterLineBoxRenderer.LINE_WIDTH);
    }

    static float lineWidth()
    {
        return TorchmasterLineBoxRenderer.LINE_WIDTH;
    }

    enum LineLayerChoice
    {
        VANILLA_LINES,
        CUSTOM_PIPELINE
    }

    enum FlushTarget
    {
        VANILLA_LINES,
        CUSTOM_PIPELINE
    }
}
