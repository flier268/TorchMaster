package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.EntityTypeKey;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class EntityFilter {
    private final Set<EntityTypeKey> blockedEntities = new HashSet<>();

    public void register(EntityTypeKey entityType) {
        blockedEntities.add(entityType);
    }

    public void remove(EntityTypeKey entityType) {
        blockedEntities.remove(entityType);
    }

    public boolean contains(EntityTypeKey entityType) {
        return blockedEntities.contains(entityType);
    }

    public Set<EntityTypeKey> entries() {
        return Collections.unmodifiableSet(blockedEntities);
    }

    public void clear() {
        blockedEntities.clear();
    }
}
