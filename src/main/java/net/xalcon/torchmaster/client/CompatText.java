package net.xalcon.torchmaster.client;

//? if <1.16
/*import net.minecraft.client.resource.language.I18n;*/
import net.minecraft.text.Text;
//? if <1.16
/*import net.minecraft.text.TranslatableText;*/
//? if <1.19 {
/*import net.minecraft.text.LiteralText;
*///?}
//? if >=1.16
import net.xalcon.torchmaster.minecraft.adapter.MinecraftText;

final class CompatText
{
    private final String translationKey;
    private final Object[] args;
    private final String literalValue;

    private CompatText(String translationKey, Object[] args, String literalValue)
    {
        this.translationKey = translationKey;
        this.args = args;
        this.literalValue = literalValue;
    }

    static CompatText translatable(String translationKey, Object... args)
    {
        return new CompatText(translationKey, args, null);
    }

    static CompatText literal(String value)
    {
        return new CompatText(null, new Object[0], value);
    }

    static CompatText empty()
    {
        return literal("");
    }

    String translationKey()
    {
        return translationKey;
    }

    Object[] args()
    {
        return args;
    }

    String literalValue()
    {
        return literalValue;
    }

    Text asTitle()
    {
        if (literalValue != null) {
            //? if >=1.19 {
            return literalValue.isEmpty() ? Text.empty() : MinecraftText.literal(literalValue);
            //?} else if >=1.16 {
            /*return new LiteralText(literalValue);
            *///?} else {
            /*return new LiteralText(literalValue);
            *///?}
        }
        //? if >=1.16 {
        return MinecraftText.translatable(translationKey, args);
        //?} else {
        /*return new TranslatableText(translationKey, args);
        *///?}
    }

    //? if >=1.16
    Text asWidget()
    //? if <1.16
    //String asWidget()
    {
        if (literalValue != null) {
            //? if >=1.19 {
            return literalValue.isEmpty() ? Text.empty() : MinecraftText.literal(literalValue);
            //?} else if >=1.16 {
            /*return new LiteralText(literalValue);
            *///?} else {
            /*return literalValue;
            *///?}
        }
        //? if >=1.16 {
        return MinecraftText.translatable(translationKey, args);
        //?} else {
        /*return I18n.translate(translationKey, args);
        *///?}
    }
}
