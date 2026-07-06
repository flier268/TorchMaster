package net.xalcon.torchmaster.client;

final class TorchmasterRangeRenderBackendDescriptor
{
    private final LineLayerChoice lineLayer;
    private final FlushTarget flushTarget;
    private final LegacySessionState legacySessionState;
    private final float lineWidth;

    TorchmasterRangeRenderBackendDescriptor(LineLayerChoice lineLayer, FlushTarget flushTarget, LegacySessionState legacySessionState, float lineWidth)
    {
        this.lineLayer = lineLayer;
        this.flushTarget = flushTarget;
        this.legacySessionState = legacySessionState;
        this.lineWidth = lineWidth;
    }

    LineLayerChoice lineLayer()
    {
        return lineLayer;
    }

    FlushTarget flushTarget()
    {
        return flushTarget;
    }

    LegacySessionState legacySessionState()
    {
        return legacySessionState;
    }

    float lineWidth()
    {
        return lineWidth;
    }

    CameraOffset cameraOffset(double x, double y, double z)
    {
        return new CameraOffset(-x, -y, -z);
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

    static final class CameraOffset
    {
        final double x;
        final double y;
        final double z;

        CameraOffset(double x, double y, double z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    static final class LegacySessionState
    {
        final boolean disableTexture;
        final boolean enableBlend;
        final float lineWidth;

        LegacySessionState(boolean disableTexture, boolean enableBlend, float lineWidth)
        {
            this.disableTexture = disableTexture;
            this.enableBlend = enableBlend;
            this.lineWidth = lineWidth;
        }
    }
}
