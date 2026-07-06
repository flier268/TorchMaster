package net.xalcon.torchmaster.client;

final class TorchmasterRangeRenderBackend
{
    private TorchmasterRangeRenderBackend()
    {
    }

    static TorchmasterRangeRenderBackendDescriptor active()
    {
        return new TorchmasterRangeRenderBackendDescriptor(activeLineLayer(), activeFlushTarget(), activeLegacySessionState(), lineWidth());
    }

    static TorchmasterRangeRenderBackendDescriptor.LineLayerChoice activeLineLayer()
    {
        //? if >=1.21.11 {
        /*return TorchmasterRangeRenderBackendDescriptor.LineLayerChoice.CUSTOM_PIPELINE;
        *///?} else {
        return TorchmasterRangeRenderBackendDescriptor.LineLayerChoice.VANILLA_LINES;
        //?}
    }

    static TorchmasterRangeRenderBackendDescriptor.FlushTarget activeFlushTarget()
    {
        //? if >=1.21.11 {
        /*return TorchmasterRangeRenderBackendDescriptor.FlushTarget.CUSTOM_PIPELINE;
        *///?} else {
        return TorchmasterRangeRenderBackendDescriptor.FlushTarget.VANILLA_LINES;
        //?}
    }

    static TorchmasterRangeRenderBackendDescriptor.CameraOffset cameraOffset(double x, double y, double z)
    {
        return active().cameraOffset(x, y, z);
    }

    static TorchmasterRangeRenderBackendDescriptor.LegacySessionState legacySessionState()
    {
        return active().legacySessionState();
    }

    static float lineWidth()
    {
        return TorchmasterLineBoxRenderer.LINE_WIDTH;
    }

    private static TorchmasterRangeRenderBackendDescriptor.LegacySessionState activeLegacySessionState()
    {
        return new TorchmasterRangeRenderBackendDescriptor.LegacySessionState(true, true, lineWidth());
    }
}
