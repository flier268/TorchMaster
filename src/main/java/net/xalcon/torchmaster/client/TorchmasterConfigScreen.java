package net.xalcon.torchmaster.client;

//? if >=1.20
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
//? if >=1.21.11
//import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
//? if >=1.16 && <1.20
//import net.minecraft.client.util.math.MatrixStack;
import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.config.TorchmasterTomlConfig;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class TorchmasterConfigScreen extends TorchmasterScreenCompat
{
    private static final int BUTTON_HEIGHT = 20;
    private final Screen parent;
    private final List<Entry> entries = new ArrayList<>();
    private int scrollOffset;
    private CompatText status = emptyText();
    private int statusColor = 0xFFA0A0A0;

    public TorchmasterConfigScreen(Screen parent)
    {
        super(text("screen.torchmaster.config.title"));
        this.parent = parent;
    }

    public static void open()
    {
        open(new TorchmasterConfigScreen(currentScreen()));
    }

    @Override
    protected void init()
    {
        entries.clear();
        ITorchmasterConfig config = TorchmasterRuntime.getConfig();

        TorchmasterConfigScreenLayout layout = layout();
        int fieldX = layout.fieldX();
        int fieldWidth = layout.fieldWidth();
        int rowHeight = layout.rowHeight();
        List<TorchmasterConfigEntries.EntryDefinition> definitions = TorchmasterConfigEntries.fromConfig(config);
        for (int index = 0; index < definitions.size(); index++) {
            TorchmasterConfigEntries.EntryDefinition definition = definitions.get(index);
            int rowY = 48 + rowHeight * index;
            switch (definition.type()) {
                case INTEGER:
                    addIntEntry(definition, fieldX, fieldWidth, rowY);
                    break;
                case BOOLEAN:
                    addBooleanEntry(definition, fieldX, rowY);
                    break;
                case LIST:
                    addListEntry(definition, layout.listFieldX(), layout.listFieldWidth(), rowY);
                    break;
                default:
                    throw new IllegalStateException("Unsupported config entry type " + definition.type());
            }
        }

        int bottomButtonWidth = layout.bottomButtonWidth();
        int buttonGap = 4;
        int totalButtonWidth = bottomButtonWidth * 3 + buttonGap * 2;
        int buttonX = layout.panelLeft() + (layout.panelWidth() - totalButtonWidth) / 2;
        addCompatWidget(button(buttonX, height - 28, bottomButtonWidth, BUTTON_HEIGHT, text("screen.torchmaster.config.save"), button -> save()));
        addCompatWidget(button(buttonX + bottomButtonWidth + buttonGap, height - 28, bottomButtonWidth, BUTTON_HEIGHT, text("screen.torchmaster.config.reset"), button -> reset()));
        addCompatWidget(button(buttonX + (bottomButtonWidth + buttonGap) * 2, height - 28, bottomButtonWidth, BUTTON_HEIGHT, text("gui.done"), button -> closeScreen()));

        updateEntryPositions();
    }

    private void addIntEntry(TorchmasterConfigEntries.EntryDefinition definition, int x, int width, int y)
    {
        TextFieldWidget editBox = textField(x, layout().widgetY(y), width, BUTTON_HEIGHT, definition.translationKey());
        editBox.setTextPredicate(text -> text.isEmpty() || text.matches("-?\\d*"));
        editBox.setMaxLength(10);
        editBox.setText(Integer.toString(definition.intValue()));
        addCompatWidget(editBox);
        entries.add(new IntEntry(definition, y, editBox));
    }

    private void addListEntry(TorchmasterConfigEntries.EntryDefinition definition, int x, int width, int y)
    {
        TextFieldWidget editBox = textField(x, layout().widgetY(y), width, BUTTON_HEIGHT, definition.translationKey());
        editBox.setMaxLength(1024);
        editBox.setText(String.join(", ", definition.listValue()));
        addCompatWidget(editBox);
        entries.add(new ListEntry(definition, y, editBox));
    }

    private void addBooleanEntry(TorchmasterConfigEntries.EntryDefinition definition, int x, int y)
    {
        BooleanEntry entry = new BooleanEntry(definition, y, definition.booleanValue());
        TorchmasterConfigScreenLayout layout = layout();
        ButtonWidget button = button(layout.booleanButtonX(x), layout.widgetY(y), layout.booleanButtonWidth(), BUTTON_HEIGHT, booleanLabel(entry.value), ignored -> {
            entry.toggle();
            ignored.setMessage(booleanLabel(entry.value).asWidget());
        });
        entry.button = button;
        addCompatWidget(button);
        entries.add(entry);
    }

    private void save()
    {
        ITorchmasterConfig loadedConfig = TorchmasterRuntime.getConfig();
        if (!(loadedConfig instanceof TorchmasterTomlConfig)) {
            setStatus("screen.torchmaster.config.unsupported", 0xFFFF5555);
            return;
        }

        TorchmasterConfigEntries.Collector collector = TorchmasterConfigEntries.collector();
        for (Entry entry : entries) {
            TorchmasterConfigEntries.ReadResult result = entry.read(collector);
            if (!result.isSuccess()) {
                setStatus(result.errorKey(), 0xFFFF5555);
                return;
            }
        }

        collector.toDraft().saveTo((TorchmasterTomlConfig)loadedConfig);
        TorchmasterRuntime.onWorldLoaded();
        setStatus("screen.torchmaster.config.saved", 0xFF55FF55);
    }

    private void reset()
    {
        scrollOffset = 0;
        clearCompatWidgets();
        init();
        setStatus("screen.torchmaster.config.reverted", 0xFFFFFF55);
    }

    private void setStatus(String translationKey, int color)
    {
        status = text(translationKey);
        statusColor = color;
    }

    private void closeScreen()
    {
        //? if >=1.18
        close();
        //? if >=1.17.1 && <1.18 {
        /*returnTo(parent);
        *///?}
        //? if <1.17.1 {
        /*onClose();
        *///?}
    }

    //? if >=1.18 {
    @Override
    public void close()
    {
        returnTo(parent);
    }
    //?}
    //? if >=1.17.1 && <1.18 {
    /*@Override
    public void onClose()
    {
        returnTo(parent);
    }
    *///?}
    //? if >=1.16 && <1.17.1 {
    /*@Override
    public void onClose()
    {
        returnTo(parent);
    }
    *///?}
    //? if <1.16 {
    /*@Override
    public void onClose()
    {
        returnTo(parent);
    }
    *///?}

    //? if >=1.21.11 {
    /*@Override
    public boolean keyPressed(KeyInput event)
    {
        if (event.key() == GLFW.GLFW_KEY_ENTER || event.key() == GLFW.GLFW_KEY_KP_ENTER) {
            save();
            return true;
        }
        return super.keyPressed(event);
    }
    *///?} else if fabric && forge && >=1.21.9 {
    /*@Override
    public boolean keyPressed(KeyEvent event)
    {
        if (event.key() == GLFW.GLFW_KEY_ENTER || event.key() == GLFW.GLFW_KEY_KP_ENTER) {
            save();
            return true;
        }
        return super.keyPressed(event);
    }
    *///?} else {
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            save();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    //?}

    //? if >=1.20.6 {
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        return scroll(scrollY);
    }
    //?} else {
    /*@Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        return scroll(delta);
    }
    *///?}

    private boolean scroll(double delta)
    {
        TorchmasterConfigScreenLayout layout = layout();
        int maxScroll = Math.max(0, entries.size() * layout.rowHeight() - (height - 94));
        scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - (int)(delta * layout.rowHeight())));
        updateEntryPositions();
        return true;
    }

    private void updateEntryPositions()
    {
        TorchmasterConfigScreenLayout layout = layout();
        int fieldX = layout.fieldX();
        int top = 44;
        int bottom = height - 38;
        for (Entry entry : entries) {
            int y = entry.baseY - scrollOffset;
            entry.setPosition(fieldX, y);
            entry.setVisible(y >= top - layout.rowHeight() && y <= bottom - BUTTON_HEIGHT);
        }
    }

    private TorchmasterConfigScreenLayout layout()
    {
        return new TorchmasterConfigScreenLayout(width, height);
    }

    //? if >=1.20 {
    @Override
    public void render(DrawContext graphics, int mouseX, int mouseY, float partialTick)
    {
        TorchmasterConfigScreenLayout layout = layout();
        int left = layout.panelLeft();
        int right = layout.panelRight();
        int top = layout.panelTop();
        int bottom = layout.panelBottom();

        super.render(graphics, mouseX, mouseY, partialTick);

        drawPanelFrame(graphics, left, top, right, bottom);
        graphics.drawCenteredTextWithShadow(textRenderer, title, width / 2, 14, TorchmasterPanelRenderer.TITLE_COLOR);

        for (Entry entry : entries) {
            if (entry.visible) {
                graphics.drawText(textRenderer, text(entry.translationKey()).asWidget(), left + 12, layout.compact() ? entry.y : entry.y + 6, TorchmasterPanelRenderer.LABEL_COLOR, false);
            }
        }
        graphics.drawCenteredTextWithShadow(textRenderer, status.asWidget(), width / 2, height - 48, statusColor);
    }
    //?} else if >=1.19.4 {
    /*@Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        TorchmasterConfigScreenLayout layout = layout();
        int left = layout.panelLeft();
        int right = layout.panelRight();
        int top = layout.panelTop();
        int bottom = layout.panelBottom();

        super.render(poseStack, mouseX, mouseY, partialTick);

        drawPanelFrame(poseStack, left, top, right, bottom);
        drawCenteredTextWithShadow(poseStack, textRenderer, title, width / 2, 14, TorchmasterPanelRenderer.TITLE_COLOR);

        for (Entry entry : entries) {
            if (entry.visible) {
                drawTextWithShadow(poseStack, textRenderer, text(entry.translationKey()).asWidget(), left + 12, layout.compact() ? entry.y : entry.y + 6, TorchmasterPanelRenderer.LABEL_COLOR);
            }
        }
        drawCenteredTextWithShadow(poseStack, textRenderer, status.asWidget(), width / 2, height - 48, statusColor);
    }
    *///?} else if >=1.16 {
    /*@Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        TorchmasterConfigScreenLayout layout = layout();
        int left = layout.panelLeft();
        int right = layout.panelRight();
        int top = layout.panelTop();
        int bottom = layout.panelBottom();

        super.render(poseStack, mouseX, mouseY, partialTick);

        drawPanelFrame(poseStack, left, top, right, bottom);
        drawCenteredText(poseStack, textRenderer, title, width / 2, 14, TorchmasterPanelRenderer.TITLE_COLOR);

        for (Entry entry : entries) {
            if (entry.visible) {
                drawTextWithShadow(poseStack, textRenderer, text(entry.translationKey()).asWidget(), left + 12, layout.compact() ? entry.y : entry.y + 6, TorchmasterPanelRenderer.LABEL_COLOR);
            }
        }
        drawCenteredText(poseStack, textRenderer, status.asWidget(), width / 2, height - 48, statusColor);
    }
    *///?} else {
    /*@Override
    public void render(int mouseX, int mouseY, float partialTick)
    {
        TorchmasterConfigScreenLayout layout = layout();
        int left = layout.panelLeft();
        int right = layout.panelRight();
        int top = layout.panelTop();
        int bottom = layout.panelBottom();

        renderBackground();
        fillPanel(TorchmasterPanelRenderer.background(left, top, right, bottom));
        super.render(mouseX, mouseY, partialTick);

        drawPanelFrame(left, top, right, bottom);
        drawCenteredString(font, text("screen.torchmaster.config.title").asWidget(), width / 2, 14, TorchmasterPanelRenderer.TITLE_COLOR);

        for (Entry entry : entries) {
            if (entry.visible) {
                drawString(font, text(entry.translationKey()).asWidget(), left + 12, layout.compact() ? entry.y : entry.y + 6, TorchmasterPanelRenderer.LABEL_COLOR);
            }
        }
        drawCenteredString(font, status.asWidget(), width / 2, height - 48, statusColor);
    }
    *///?}

    //? if >=1.21 {
    @Override
    public void renderBackground(DrawContext graphics, int mouseX, int mouseY, float partialTick)
    {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        TorchmasterConfigScreenLayout layout = layout();
        fillPanel(graphics, TorchmasterPanelRenderer.background(layout.panelLeft(), layout.panelTop(), layout.panelRight(), layout.panelBottom()));
    }

    private void drawPanelFrame(DrawContext graphics, int left, int top, int right, int bottom)
    {
        for (TorchmasterPanelRenderer.Fill fill : TorchmasterPanelRenderer.frame(left, top, right, bottom)) {
            fillPanel(graphics, fill);
        }
    }

    private static void fillPanel(DrawContext graphics, TorchmasterPanelRenderer.Fill fill)
    {
        graphics.fill(fill.left, fill.top, fill.right, fill.bottom, fill.color);
    }
    //?} else if >=1.20.6 {
    /*@Override
    public void renderBackground(DrawContext graphics, int mouseX, int mouseY, float partialTick)
    {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        TorchmasterConfigScreenLayout layout = layout();
        fillPanel(graphics, TorchmasterPanelRenderer.background(layout.panelLeft(), layout.panelTop(), layout.panelRight(), layout.panelBottom()));
    }

    private void drawPanelFrame(DrawContext graphics, int left, int top, int right, int bottom)
    {
        for (TorchmasterPanelRenderer.Fill fill : TorchmasterPanelRenderer.frame(left, top, right, bottom)) {
            fillPanel(graphics, fill);
        }
    }

    private static void fillPanel(DrawContext graphics, TorchmasterPanelRenderer.Fill fill)
    {
        graphics.fill(fill.left, fill.top, fill.right, fill.bottom, fill.color);
    }
    *///?} else if >=1.20 {
    /*@Override
    public void renderBackground(DrawContext graphics)
    {
        super.renderBackground(graphics);
        TorchmasterConfigScreenLayout layout = layout();
        fillPanel(graphics, TorchmasterPanelRenderer.background(layout.panelLeft(), layout.panelTop(), layout.panelRight(), layout.panelBottom()));
    }

    private void drawPanelFrame(DrawContext graphics, int left, int top, int right, int bottom)
    {
        for (TorchmasterPanelRenderer.Fill fill : TorchmasterPanelRenderer.frame(left, top, right, bottom)) {
            fillPanel(graphics, fill);
        }
    }

    private static void fillPanel(DrawContext graphics, TorchmasterPanelRenderer.Fill fill)
    {
        graphics.fill(fill.left, fill.top, fill.right, fill.bottom, fill.color);
    }
    *///?} else if >=1.16 {
    /*@Override
    public void renderBackground(MatrixStack poseStack)
    {
        super.renderBackground(poseStack);
        TorchmasterConfigScreenLayout layout = layout();
        fillPanel(poseStack, TorchmasterPanelRenderer.background(layout.panelLeft(), layout.panelTop(), layout.panelRight(), layout.panelBottom()));
    }

    private void drawPanelFrame(MatrixStack poseStack, int left, int top, int right, int bottom)
    {
        for (TorchmasterPanelRenderer.Fill fill : TorchmasterPanelRenderer.frame(left, top, right, bottom)) {
            fillPanel(poseStack, fill);
        }
    }

    private static void fillPanel(MatrixStack poseStack, TorchmasterPanelRenderer.Fill fill)
    {
        fill(poseStack, fill.left, fill.top, fill.right, fill.bottom, fill.color);
    }
    *///?} else {
    /*private void drawPanelFrame(int left, int top, int right, int bottom)
    {
        for (TorchmasterPanelRenderer.Fill fill : TorchmasterPanelRenderer.frame(left, top, right, bottom)) {
            fillPanel(fill);
        }
    }

    private static void fillPanel(TorchmasterPanelRenderer.Fill fill)
    {
        fill(fill.left, fill.top, fill.right, fill.bottom, fill.color);
    }
    *///?}

    private abstract class Entry
    {
        protected final TorchmasterConfigEntries.EntryDefinition definition;
        protected final int baseY;
        protected int y;
        protected boolean visible;

        Entry(TorchmasterConfigEntries.EntryDefinition definition, int baseY)
        {
            this.definition = definition;
            this.baseY = baseY;
        }

        String translationKey()
        {
            return definition.translationKey();
        }

        abstract void setPosition(int fieldX, int y);

        abstract void setVisible(boolean visible);

        abstract TorchmasterConfigEntries.ReadResult read(TorchmasterConfigEntries.Collector collector);
    }

    private class IntEntry extends Entry
    {
        private final TextFieldWidget editBox;

        IntEntry(TorchmasterConfigEntries.EntryDefinition definition, int baseY, TextFieldWidget editBox)
        {
            super(definition, baseY);
            this.editBox = editBox;
        }

        @Override
        void setPosition(int fieldX, int y)
        {
            this.y = y;
            setWidgetX(editBox, fieldX);
            setWidgetY(editBox, layout().widgetY(y));
        }

        @Override
        void setVisible(boolean visible)
        {
            this.visible = visible;
            editBox.visible = visible;
            editBox.active = visible;
        }

        @Override
        TorchmasterConfigEntries.ReadResult read(TorchmasterConfigEntries.Collector collector)
        {
            return collector.addInt(editBox.getText());
        }
    }

    private class BooleanEntry extends Entry
    {
        private boolean value;
        private ButtonWidget button;

        BooleanEntry(TorchmasterConfigEntries.EntryDefinition definition, int baseY, boolean value)
        {
            super(definition, baseY);
            this.value = value;
        }

        void toggle()
        {
            value = !value;
        }

        @Override
        void setPosition(int fieldX, int y)
        {
            TorchmasterConfigScreenLayout layout = layout();
            this.y = y;
            setWidgetX(button, layout.booleanButtonX(fieldX));
            setWidgetY(button, layout.widgetY(y));
        }

        @Override
        void setVisible(boolean visible)
        {
            this.visible = visible;
            button.visible = visible;
            button.active = visible;
        }

        @Override
        TorchmasterConfigEntries.ReadResult read(TorchmasterConfigEntries.Collector collector)
        {
            return collector.addBoolean(value);
        }
    }

    private class ListEntry extends Entry
    {
        private final TextFieldWidget editBox;

        ListEntry(TorchmasterConfigEntries.EntryDefinition definition, int baseY, TextFieldWidget editBox)
        {
            super(definition, baseY);
            this.editBox = editBox;
        }

        @Override
        void setPosition(int fieldX, int y)
        {
            TorchmasterConfigScreenLayout layout = layout();
            this.y = y;
            setWidgetX(editBox, layout.listFieldX());
            setWidgetY(editBox, layout.widgetY(y));
        }

        @Override
        void setVisible(boolean visible)
        {
            this.visible = visible;
            editBox.visible = visible;
            editBox.active = visible;
        }

        @Override
        TorchmasterConfigEntries.ReadResult read(TorchmasterConfigEntries.Collector collector)
        {
            return collector.addList(editBox.getText());
        }
    }

    private static void setWidgetX(TextFieldWidget widget, int x)
    {
        //? if >=1.19.4 {
        widget.setX(x);
        //?} else {
        /*widget.x = x;
        *///?}
    }

    private static void setWidgetY(TextFieldWidget widget, int y)
    {
        //? if >=1.19.4 {
        widget.setY(y);
        //?} else {
        /*widget.y = y;
        *///?}
    }

    private static void setWidgetX(ButtonWidget widget, int x)
    {
        //? if >=1.19.4 {
        widget.setX(x);
        //?} else {
        /*widget.x = x;
        *///?}
    }

    private static void setWidgetY(ButtonWidget widget, int y)
    {
        //? if >=1.19.4 {
        widget.setY(y);
        //?} else {
        /*widget.y = y;
        *///?}
    }
}
