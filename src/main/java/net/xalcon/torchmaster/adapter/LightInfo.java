package net.xalcon.torchmaster.adapter;

public final class LightInfo {
    private final String name;
    private final BlockPosView position;

    public LightInfo(String name, BlockPosView position) {
        this.name = name;
        this.position = position;
    }

    public String name() {
        return name;
    }

    public BlockPosView position() {
        return position;
    }
}
