package net.xalcon.torchmaster.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;

public abstract class TorchmasterScreenCompat extends Screen
{
    protected TorchmasterScreenCompat(CompatText title)
    {
        super(title.asTitle());
    }

    public static void open(Screen screen)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        setScreen(minecraft, screen);
    }

    protected static Screen currentScreen()
    {
        return MinecraftClient.getInstance().currentScreen;
    }

    protected static void setScreen(MinecraftClient minecraft, Screen screen)
    {
        //? if >=1.17.1 {
        minecraft.setScreen(screen);
        //?} else {
        /*minecraft.openScreen(screen);
        *///?}
    }

    protected void openChildScreen(Screen screen)
    {
        //? if >=1.17.1 {
        client.setScreen(screen);
        //?} else if >=1.16 {
        /*client.openScreen(screen);
        *///?} else {
        /*minecraft.openScreen(screen);
        *///?}
    }

    protected void returnTo(Screen parent)
    {
        //? if >=1.17.1 {
        client.setScreen(parent);
        //?} else if >=1.16 {
        /*client.openScreen(parent);
        *///?} else {
        /*minecraft.openScreen(parent);
        *///?}
    }

    protected ButtonWidget addCompatWidget(ButtonWidget widget)
    {
        //? if >=1.17 {
        return addDrawableChild(widget);
        //?} else {
        /*return addButton(widget);
        *///?}
    }

    protected TextFieldWidget addCompatWidget(TextFieldWidget widget)
    {
        //? if >=1.17 {
        return addDrawableChild(widget);
        //?} else {
        /*return addButton(widget);
        *///?}
    }

    protected void clearCompatWidgets()
    {
        //? if >=1.17 {
        clearChildren();
        //?} else {
        /*buttons.clear();
        children.clear();
        *///?}
    }

    protected static ButtonWidget button(int x, int y, int width, int height, CompatText label, ButtonWidget.PressAction onPress)
    {
        //? if >=1.19.4 {
        return ButtonWidget.builder(label.asWidget(), onPress).dimensions(x, y, width, height).build();
        //?} else {
        /*return new ButtonWidget(x, y, width, height, label.asWidget(), onPress);
        *///?}
    }

    protected TextFieldWidget textField(int x, int y, int width, int height, String translationKey)
    {
        //? if >=1.16 {
        return new TextFieldWidget(textRenderer, x, y, width, height, text(translationKey).asWidget());
        //?} else {
        /*return new TextFieldWidget(font, x, y, width, height, text(translationKey).asWidget());
        *///?}
    }

    static void setWidgetX(TextFieldWidget widget, int x)
    {
        //? if >=1.19.4 {
        widget.setX(x);
        //?} else {
        /*widget.x = x;
        *///?}
    }

    static void setWidgetY(TextFieldWidget widget, int y)
    {
        //? if >=1.19.4 {
        widget.setY(y);
        //?} else {
        /*widget.y = y;
        *///?}
    }

    static void setWidgetX(ButtonWidget widget, int x)
    {
        //? if >=1.19.4 {
        widget.setX(x);
        //?} else {
        /*widget.x = x;
        *///?}
    }

    static void setWidgetY(ButtonWidget widget, int y)
    {
        //? if >=1.19.4 {
        widget.setY(y);
        //?} else {
        /*widget.y = y;
        *///?}
    }

    static void setWidgetVisible(TextFieldWidget widget, boolean visible)
    {
        widget.visible = visible;
        widget.active = visible;
    }

    static void setWidgetVisible(ButtonWidget widget, boolean visible)
    {
        widget.visible = visible;
        widget.active = visible;
    }

    protected static CompatText booleanLabel(boolean value)
    {
        return text(value ? "options.on" : "options.off");
    }

    protected static CompatText text(String translationKey)
    {
        return CompatText.translatable(translationKey);
    }

    protected static CompatText text(String translationKey, Object value)
    {
        return CompatText.translatable(translationKey, value);
    }

    protected static CompatText literal(String value)
    {
        return CompatText.literal(value);
    }

    protected static CompatText emptyText()
    {
        return CompatText.empty();
    }
}
