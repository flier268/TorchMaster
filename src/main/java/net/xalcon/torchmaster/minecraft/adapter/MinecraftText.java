package net.xalcon.torchmaster.minecraft.adapter;

//? if >=1.19
import net.minecraft.text.Text;
//? if <1.19 {
/*import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
*///?}

public final class MinecraftText {
    private MinecraftText() {
    }

    public static Text translatable(String key) {
        return translatable(key, new Object[0]);
    }

    public static Text translatable(String key, Object... args) {
        //? if >=1.19 {
        return Text.translatable(key, args);
        //?} else {
        /*return new TranslatableText(key, args);
        *///?}
    }

    public static Text literal(String value) {
        //? if >=1.19 {
        return Text.literal(value);
        //?} else {
        /*return new LiteralText(value);
        *///?}
    }
}
