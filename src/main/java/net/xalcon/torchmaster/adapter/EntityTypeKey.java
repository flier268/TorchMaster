package net.xalcon.torchmaster.adapter;

import java.util.Objects;

public final class EntityTypeKey {
    private final String namespace;
    private final String path;

    public EntityTypeKey(String namespace, String path) {
        Objects.requireNonNull(namespace, "namespace");
        Objects.requireNonNull(path, "path");
        this.namespace = namespace;
        this.path = path;
    }

    public static EntityTypeKey parse(String id) {
        int separator = id.indexOf(':');
        if (separator <= 0 || separator == id.length() - 1) {
            throw new IllegalArgumentException("Entity id must be namespace:path: " + id);
        }
        return new EntityTypeKey(id.substring(0, separator), id.substring(separator + 1));
    }

    public String namespace() {
        return namespace;
    }

    public String path() {
        return path;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntityTypeKey)) {
            return false;
        }
        EntityTypeKey other = (EntityTypeKey)obj;
        return namespace.equals(other.namespace) && path.equals(other.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }
}
