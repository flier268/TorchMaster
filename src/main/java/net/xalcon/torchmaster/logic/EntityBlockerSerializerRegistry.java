package net.xalcon.torchmaster.logic;

import net.minecraft.nbt.CompoundTag;
//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}
import net.minecraft.world.level.saveddata.SavedData;
import net.xalcon.torchmaster.Torchmaster;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EntityBlockerSerializerRegistry
{
    //? if >=1.21.11 {
    /*private static final Map<Identifier, EntityBlockerSerializer<?>> serializers = new HashMap<>();
    *///?} else {
    private static final Map<ResourceLocation, EntityBlockerSerializer<?>> serializers = new HashMap<>();
    //?}

    //? if >=1.21.11 {
    /*public static void RegisterSerializer(Identifier type, EntityBlockerSerializer<?> serializer)
    *///?} else {
    public static void RegisterSerializer(ResourceLocation type, EntityBlockerSerializer<?> serializer)
    //?}
    {
        if(serializers.containsKey(type))
        {
            Torchmaster.LOG.error("Serializer with type '{}' was already registered", type);
            return;
        }

        serializers.put(type, serializer);
    }

    @Nullable
    public static EntityBlocker Deserialize(CompoundTag tag)
    {
        //? if >=1.21.5 {
        /*String typeStr = tag.getStringOr("_type", "");
        *///?} else {
        String typeStr = tag.getString("_type");
        //?}
        if(typeStr.isEmpty())
        {
            Torchmaster.LOG.error("Unable to deserialize EntityBlocker, empty type");
            return null;
        }

        //? if >=1.21.11 {
        /*Identifier type = Identifier.tryParse(typeStr);
        *///?} else {
        ResourceLocation type = ResourceLocation.tryParse(typeStr);
        //?}
        if(type == null)
        {
            Torchmaster.LOG.error("Unable to deserialize EntityBlocker, can't parse type '{}'", typeStr);
            return null;
        }

        EntityBlockerSerializer<?> serializer = serializers.get(type);
        if(serializer == null)
        {
            Torchmaster.LOG.error("Unable to deserialize EntityBlocker, type '{}' not found in registry", typeStr);
            return null;
        }

        return serializer.DeserializeFrom(tag);
    }

    @Nullable
    public static CompoundTag Serialize(EntityBlocker blocker)
    {
        CompoundTag tag = new CompoundTag();
        //? if >=1.21.11 {
        /*Identifier type = blocker.getType();
        *///?} else {
        ResourceLocation type = blocker.getType();
        //?}
        EntityBlockerSerializer<?> serializer = serializers.get(type);
        if(serializer == null)
        {
            Torchmaster.LOG.error("Serializer for type '{}' not found, unable to save data", type);
            return null;
        }

        serializer.SerializeInto(tag, blocker);
        tag.putString("_type", type.toString());
        return tag;
    }
}
