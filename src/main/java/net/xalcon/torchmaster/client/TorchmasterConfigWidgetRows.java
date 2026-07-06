package net.xalcon.torchmaster.client;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;

import java.util.ArrayList;
import java.util.List;

final class TorchmasterConfigWidgetRows
{
    private static final int FIRST_ROW_Y = 48;
    private static final int VIEWPORT_TOP = 44;
    private static final int VIEWPORT_BOTTOM_MARGIN = 38;
    private static final int SCROLL_RESERVED_HEIGHT = 94;

    private TorchmasterConfigWidgetRows()
    {
    }

    static List<Row> create(List<TorchmasterConfigEntries.EntryDefinition> definitions, TorchmasterConfigScreenLayout layout, int buttonHeight, WidgetFactory factory)
    {
        List<Row> rows = new ArrayList<>();
        for (int index = 0; index < definitions.size(); index++) {
            TorchmasterConfigEntries.EntryDefinition definition = definitions.get(index);
            int rowY = FIRST_ROW_Y + layout.rowHeight() * index;
            switch (definition.type()) {
                case INTEGER:
                    rows.add(createIntRow(definition, layout.fieldX(), layout.fieldWidth(), rowY, buttonHeight, layout, factory));
                    break;
                case BOOLEAN:
                    rows.add(createBooleanRow(definition, layout.fieldX(), rowY, buttonHeight, layout, factory));
                    break;
                case LIST:
                    rows.add(createListRow(definition, layout.listFieldX(), layout.listFieldWidth(), rowY, buttonHeight, layout, factory));
                    break;
                default:
                    throw new IllegalStateException("Unsupported config entry type " + definition.type());
            }
        }
        return rows;
    }

    static int maxScroll(int rowCount, TorchmasterConfigScreenLayout layout, int screenHeight)
    {
        return Math.max(0, rowCount * layout.rowHeight() - (screenHeight - SCROLL_RESERVED_HEIGHT));
    }

    static int scrollOffset(int currentOffset, double delta, int rowCount, TorchmasterConfigScreenLayout layout, int screenHeight)
    {
        int nextOffset = currentOffset - (int)(delta * layout.rowHeight());
        return Math.max(0, Math.min(maxScroll(rowCount, layout, screenHeight), nextOffset));
    }

    static void updatePositions(List<Row> rows, TorchmasterConfigScreenLayout layout, int scrollOffset, int screenHeight, int buttonHeight)
    {
        int bottom = screenHeight - VIEWPORT_BOTTOM_MARGIN;
        for (Row row : rows) {
            int y = row.baseY - scrollOffset;
            row.setPosition(layout, layout.fieldX(), y);
            row.setVisible(isVisible(y, layout.rowHeight(), bottom, buttonHeight));
        }
    }

    static boolean isVisible(int y, int rowHeight, int bottom, int buttonHeight)
    {
        return y >= VIEWPORT_TOP - rowHeight && y <= bottom - buttonHeight;
    }

    private static Row createIntRow(TorchmasterConfigEntries.EntryDefinition definition, int x, int width, int y, int buttonHeight,
            TorchmasterConfigScreenLayout layout, WidgetFactory factory)
    {
        TextFieldWidget editBox = factory.textField(x, layout.widgetY(y), width, buttonHeight, definition.translationKey());
        editBox.setTextPredicate(text -> text.isEmpty() || text.matches("-?\\d*"));
        editBox.setMaxLength(10);
        editBox.setText(Integer.toString(definition.intValue()));
        factory.add(editBox);
        return new IntRow(definition, y, editBox);
    }

    private static Row createListRow(TorchmasterConfigEntries.EntryDefinition definition, int x, int width, int y, int buttonHeight,
            TorchmasterConfigScreenLayout layout, WidgetFactory factory)
    {
        TextFieldWidget editBox = factory.textField(x, layout.widgetY(y), width, buttonHeight, definition.translationKey());
        editBox.setMaxLength(1024);
        editBox.setText(String.join(", ", definition.listValue()));
        factory.add(editBox);
        return new ListRow(definition, y, editBox);
    }

    private static Row createBooleanRow(TorchmasterConfigEntries.EntryDefinition definition, int fieldX, int y, int buttonHeight,
            TorchmasterConfigScreenLayout layout, WidgetFactory factory)
    {
        BooleanRow row = new BooleanRow(definition, y, definition.booleanValue());
        ButtonWidget button = factory.button(layout.booleanButtonX(fieldX), layout.widgetY(y), layout.booleanButtonWidth(), buttonHeight,
                factory.booleanLabel(row.value), ignored -> row.toggle(ignored, factory));
        row.setButton(button);
        factory.add(button);
        return row;
    }

    interface WidgetFactory
    {
        TextFieldWidget textField(int x, int y, int width, int height, String translationKey);

        ButtonWidget button(int x, int y, int width, int height, CompatText label, ButtonWidget.PressAction onPress);

        TextFieldWidget add(TextFieldWidget widget);

        ButtonWidget add(ButtonWidget widget);

        CompatText booleanLabel(boolean value);
    }

    abstract static class Row
    {
        protected final TorchmasterConfigEntries.EntryDefinition definition;
        protected final int baseY;
        protected int y;
        protected boolean visible;

        Row(TorchmasterConfigEntries.EntryDefinition definition, int baseY)
        {
            this.definition = definition;
            this.baseY = baseY;
        }

        String translationKey()
        {
            return definition.translationKey();
        }

        int y()
        {
            return y;
        }

        boolean visible()
        {
            return visible;
        }

        abstract void setPosition(TorchmasterConfigScreenLayout layout, int fieldX, int y);

        abstract void setVisible(boolean visible);

        abstract TorchmasterConfigEntries.ReadResult read(TorchmasterConfigEntries.Collector collector);
    }

    private static final class IntRow extends Row
    {
        private final TextFieldWidget editBox;

        private IntRow(TorchmasterConfigEntries.EntryDefinition definition, int baseY, TextFieldWidget editBox)
        {
            super(definition, baseY);
            this.editBox = editBox;
        }

        @Override
        void setPosition(TorchmasterConfigScreenLayout layout, int fieldX, int y)
        {
            this.y = y;
            TorchmasterScreenCompat.setWidgetX(editBox, fieldX);
            TorchmasterScreenCompat.setWidgetY(editBox, layout.widgetY(y));
        }

        @Override
        void setVisible(boolean visible)
        {
            this.visible = visible;
            TorchmasterScreenCompat.setWidgetVisible(editBox, visible);
        }

        @Override
        TorchmasterConfigEntries.ReadResult read(TorchmasterConfigEntries.Collector collector)
        {
            return collector.addInt(editBox.getText());
        }
    }

    private static final class ListRow extends Row
    {
        private final TextFieldWidget editBox;

        private ListRow(TorchmasterConfigEntries.EntryDefinition definition, int baseY, TextFieldWidget editBox)
        {
            super(definition, baseY);
            this.editBox = editBox;
        }

        @Override
        void setPosition(TorchmasterConfigScreenLayout layout, int fieldX, int y)
        {
            this.y = y;
            TorchmasterScreenCompat.setWidgetX(editBox, layout.listFieldX());
            TorchmasterScreenCompat.setWidgetY(editBox, layout.widgetY(y));
        }

        @Override
        void setVisible(boolean visible)
        {
            this.visible = visible;
            TorchmasterScreenCompat.setWidgetVisible(editBox, visible);
        }

        @Override
        TorchmasterConfigEntries.ReadResult read(TorchmasterConfigEntries.Collector collector)
        {
            return collector.addList(editBox.getText());
        }
    }

    private static final class BooleanRow extends Row
    {
        private boolean value;
        private ButtonWidget button;

        private BooleanRow(TorchmasterConfigEntries.EntryDefinition definition, int baseY, boolean value)
        {
            super(definition, baseY);
            this.value = value;
        }

        private void setButton(ButtonWidget button)
        {
            this.button = button;
        }

        private void toggle(ButtonWidget widget, WidgetFactory factory)
        {
            value = !value;
            widget.setMessage(factory.booleanLabel(value).asWidget());
        }

        @Override
        void setPosition(TorchmasterConfigScreenLayout layout, int fieldX, int y)
        {
            this.y = y;
            TorchmasterScreenCompat.setWidgetX(button, layout.booleanButtonX(fieldX));
            TorchmasterScreenCompat.setWidgetY(button, layout.widgetY(y));
        }

        @Override
        void setVisible(boolean visible)
        {
            this.visible = visible;
            TorchmasterScreenCompat.setWidgetVisible(button, visible);
        }

        @Override
        TorchmasterConfigEntries.ReadResult read(TorchmasterConfigEntries.Collector collector)
        {
            return collector.addBoolean(value);
        }
    }
}
