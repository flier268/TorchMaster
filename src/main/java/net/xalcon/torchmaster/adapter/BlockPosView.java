package net.xalcon.torchmaster.adapter;

public final class BlockPosView {
    private final int x;
    private final int y;
    private final int z;

    public BlockPosView(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }
}
