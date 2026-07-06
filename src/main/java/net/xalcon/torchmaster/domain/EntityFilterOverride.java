package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.EntityTypeKey;

public final class EntityFilterOverride
{
    public enum Action
    {
        ADD,
        REMOVE,
        INVALID_FORMAT,
        INVALID_ENTITY_ID,
        UNKNOWN_ENTITY
    }

    private final Action action;
    private final String rawValue;
    private final String entityId;
    private final EntityTypeKey entityType;

    private EntityFilterOverride(Action action, String rawValue, String entityId, EntityTypeKey entityType)
    {
        this.action = action;
        this.rawValue = rawValue;
        this.entityId = entityId;
        this.entityType = entityType;
    }

    static EntityFilterOverride add(String rawValue, String entityId, EntityTypeKey entityType)
    {
        return new EntityFilterOverride(Action.ADD, rawValue, entityId, entityType);
    }

    static EntityFilterOverride remove(String rawValue, String entityId, EntityTypeKey entityType)
    {
        return new EntityFilterOverride(Action.REMOVE, rawValue, entityId, entityType);
    }

    static EntityFilterOverride invalidFormat(Object rawValue)
    {
        return new EntityFilterOverride(Action.INVALID_FORMAT, String.valueOf(rawValue), null, null);
    }

    static EntityFilterOverride invalidEntityId(String rawValue, String entityId)
    {
        return new EntityFilterOverride(Action.INVALID_ENTITY_ID, rawValue, entityId, null);
    }

    static EntityFilterOverride unknownEntity(String rawValue, String entityId, EntityTypeKey entityType)
    {
        return new EntityFilterOverride(Action.UNKNOWN_ENTITY, rawValue, entityId, entityType);
    }

    public Action action()
    {
        return action;
    }

    public String rawValue()
    {
        return rawValue;
    }

    public String entityId()
    {
        return entityId;
    }

    public EntityTypeKey entityType()
    {
        return entityType;
    }
}
