package net.xalcon.torchmaster.minecraft.adapter;

//? if >=1.19.3 {
import net.minecraft.core.registries.BuiltInRegistries;
//?} else {
/*import net.minecraft.core.Registry;
*///?}
//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}
import net.xalcon.torchmaster.port.EntityTypeKey;

public final class MinecraftEntityIds {
    private MinecraftEntityIds() {
    }

    public static EntityTypeKey parseEntityTypeKey(String id) {
        //? if >=1.21.11 {
        /*Identifier parsed = Identifier.parse(id);
        *///?} elif >=1.21 {
        ResourceLocation parsed = ResourceLocation.parse(id);
        //?} else {
        /*ResourceLocation parsed = new ResourceLocation(id);
        *///?}
        return MinecraftAdapterViews.entityTypeKey(parsed);
    }

    public static boolean entityExists(EntityTypeKey key) {
        //? if >=1.21.11 {
        /*Identifier id = Identifier.fromNamespaceAndPath(key.namespace(), key.path());
        return BuiltInRegistries.ENTITY_TYPE.containsKey(id);
        *///?} elif >=1.21 {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(key.namespace(), key.path());
        return BuiltInRegistries.ENTITY_TYPE.containsKey(id);
        //?} elif >=1.19.3 {
        /*ResourceLocation id = new ResourceLocation(key.namespace(), key.path());
        return BuiltInRegistries.ENTITY_TYPE.containsKey(id);
        *///?} elif >=1.17 {
        /*ResourceLocation id = new ResourceLocation(key.namespace(), key.path());
        return Registry.ENTITY_TYPE.containsKey(id);
        *///?} else {
        /*ResourceLocation id = new ResourceLocation(key.namespace(), key.path());
        return Registry.ENTITY_TYPE.containsKey(id);
        *///?}
    }
}
