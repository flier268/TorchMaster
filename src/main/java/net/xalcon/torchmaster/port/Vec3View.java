package net.xalcon.torchmaster.port;

public final class Vec3View {
    private final double x;
    private final double y;
    private final double z;

    public Vec3View(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }
}
