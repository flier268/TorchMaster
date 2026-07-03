package net.xalcon.torchmaster.compat;

//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}
import net.minecraft.world.entity.EntityType;

class EntityInfoWrapper
{
    //? if >=1.21.11 {
    /*private final Identifier entityName;
    *///?} else {
    private final ResourceLocation entityName;
    //?}
    private final EntityType<?> entityType;

    //? if >=1.21.11 {
    /*EntityInfoWrapper(Identifier entityName, EntityType<?> entityType)
    *///?} else {
    EntityInfoWrapper(ResourceLocation entityName, EntityType<?> entityType)
    //?}
    {
        this.entityName = entityName;
        this.entityType = entityType;
    }

    //? if >=1.21.11 {
    /*Identifier getEntityName()
    *///?} else {
    ResourceLocation getEntityName()
    //?}
    {
        return entityName;
    }

    EntityType<?> getEntityType()
    {
        return entityType;
    }
}
