package net.xalcon.torchmaster.compat;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

class EntityInfoWrapper
{
    private final Identifier entityName;
    private final EntityType<?> entityType;

    EntityInfoWrapper(Identifier entityName, EntityType<?> entityType)
    {
        this.entityName = entityName;
        this.entityType = entityType;
    }

    Identifier getEntityName()
    {
        return entityName;
    }

    EntityType<?> getEntityType()
    {
        return entityType;
    }
}
