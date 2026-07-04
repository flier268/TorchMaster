package net.xalcon.torchmaster.port;

public interface EntityView {
    EntityTypeKey typeKey();

    WorldView world();

    Vec3View position();
}
