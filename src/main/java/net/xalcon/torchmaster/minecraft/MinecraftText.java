package net.xalcon.torchmaster.minecraft;

import net.minecraft.network.chat.Component;
//? if <1.19 {
/*import net.minecraft.network.chat.TranslatableComponent;
*///?}

public final class MinecraftText {
    private MinecraftText() {
    }

    public static Component translatable(String key) {
        //? if >=1.19 {
        return Component.translatable(key);
        //?} else {
        /*return new TranslatableComponent(key);
        *///?}
    }
}
