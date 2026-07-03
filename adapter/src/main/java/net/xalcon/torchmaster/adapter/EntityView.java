package net.xalcon.torchmaster.adapter;

public interface EntityView {
    EntityTypeKey typeKey();

    WorldView world();

    Vec3View position();
}
