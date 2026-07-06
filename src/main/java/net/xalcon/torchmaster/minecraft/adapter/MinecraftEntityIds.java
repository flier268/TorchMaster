package net.xalcon.torchmaster.minecraft.adapter;

//? if >=1.19.3 {
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
//?} else {
/*import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
*///?}
import net.xalcon.torchmaster.port.EntityTypeKey;

public final class MinecraftEntityIds {
    private MinecraftEntityIds() {
    }

    public static EntityTypeKey parseEntityTypeKey(String id) {
        //? if >=1.21 {
        Identifier parsed = Identifier.of(id);
//?} else {
        /*Identifier parsed = new Identifier(id);
        *///?}
        return MinecraftAdapterViews.entityTypeKey(parsed);
    }

    public static boolean entityExists(EntityTypeKey key) {
        //? if >=1.21 {
        Identifier id = Identifier.of(key.namespace(), key.path());
        return Registries.ENTITY_TYPE.containsId(id);
//?} elif >=1.19.3 {
        /*Identifier id = Identifier.of(key.namespace(), key.path());
        return Registries.ENTITY_TYPE.containsId(id);
        *///?} elif >=1.17 {
        /*Identifier id = new Identifier(key.namespace(), key.path());
        return Registry.ENTITY_TYPE.containsId(id);
        *///?} else {
        /*Identifier id = new Identifier(key.namespace(), key.path());
        return Registry.ENTITY_TYPE.containsId(id);
        *///?}
    }
}
