package net.xalcon.torchmaster.minecraft.adapter;

import net.minecraft.network.chat.Component;
//? if <1.19 {
/*import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
*///?}

public final class MinecraftText {
    private MinecraftText() {
    }

    public static Component translatable(String key) {
        return translatable(key, new Object[0]);
    }

    public static Component translatable(String key, Object... args) {
        //? if >=1.19 {
        return Component.translatable(key, args);
        //?} else {
        /*return new TranslatableComponent(key, args);
        *///?}
    }

    public static Component literal(String value) {
        //? if >=1.19 {
        return Component.literal(value);
        //?} else {
        /*return new TextComponent(value);
        *///?}
    }
}
