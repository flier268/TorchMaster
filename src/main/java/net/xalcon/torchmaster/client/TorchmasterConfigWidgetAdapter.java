package net.xalcon.torchmaster.client;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;

final class TorchmasterConfigWidgetAdapter
{
    private TorchmasterConfigWidgetAdapter()
    {
    }

    static void configureInteger(TextFieldWidget widget, int value)
    {
        widget.setTextPredicate(TorchmasterConfigWidgetAdapter::isIntegerText);
        widget.setMaxLength(10);
        widget.setText(Integer.toString(value));
    }

    static void configureList(TextFieldWidget widget, java.util.List<String> values)
    {
        widget.setMaxLength(1024);
        widget.setText(listText(values));
    }

    static boolean isIntegerText(String text)
    {
        return text.isEmpty() || text.matches("-?\\d*");
    }

    static String listText(java.util.List<String> values)
    {
        return String.join(", ", values);
    }

    static void position(TextFieldWidget widget, int x, int y)
    {
        TorchmasterScreenCompat.setWidgetX(widget, x);
        TorchmasterScreenCompat.setWidgetY(widget, y);
    }

    static void position(ButtonWidget widget, int x, int y)
    {
        TorchmasterScreenCompat.setWidgetX(widget, x);
        TorchmasterScreenCompat.setWidgetY(widget, y);
    }

    static void visible(TextFieldWidget widget, boolean visible)
    {
        TorchmasterScreenCompat.setWidgetVisible(widget, visible);
    }

    static void visible(ButtonWidget widget, boolean visible)
    {
        TorchmasterScreenCompat.setWidgetVisible(widget, visible);
    }

    static void active(TextFieldWidget widget, boolean active)
    {
        widget.active = active;
    }

    static void active(ButtonWidget widget, boolean active)
    {
        widget.active = active;
    }

    static void updateBooleanLabel(ButtonWidget widget, CompatText label)
    {
        widget.setMessage(label.asWidget());
    }
}
