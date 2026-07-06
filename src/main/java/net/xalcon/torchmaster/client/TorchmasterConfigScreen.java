package net.xalcon.torchmaster.client;

import net.minecraft.client.MinecraftClient;
//? if >=1.20
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
//? if >=1.21.11
//import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
//? if >=1.16 && <1.20
//import net.minecraft.client.util.math.MatrixStack;
//? if <1.16
/*import net.minecraft.client.resource.language.I18n;*/
import net.minecraft.text.Text;
//? if <1.16
/*import net.minecraft.text.TranslatableText;*/
//? if <1.19 {
/*import net.minecraft.text.LiteralText;
*///?}
import net.xalcon.torchmaster.EntityFilterList;
import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.config.TorchmasterTomlConfig;
//? if >=1.16
import net.xalcon.torchmaster.minecraft.adapter.MinecraftText;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TorchmasterConfigScreen extends Screen
{
    private static final int MAX_PANEL_WIDTH = 470;
    private static final int SIDE_MARGIN = 12;
    private static final int WIDE_ROW_HEIGHT = 32;
    private static final int COMPACT_ROW_HEIGHT = 44;
    private static final int COMPACT_BREAKPOINT = 420;
    private static final int WIDE_FIELD_WIDTH = 150;
    private static final int WIDE_LIST_FIELD_WIDTH = 240;
    private static final int BUTTON_WIDTH = 96;
    private static final int BUTTON_HEIGHT = 20;
    private final Screen parent;
    private final List<Entry> entries = new ArrayList<>();
    private int scrollOffset;
    //? if >=1.16
    private Text status = emptyText();
    //? if <1.16
    //private String status = "";
    private int statusColor = 0xFFA0A0A0;

    public TorchmasterConfigScreen(Screen parent)
    {
        //? if >=1.16 {
        super(text("screen.torchmaster.config.title"));
        //?} else {
        /*super(new TranslatableText("screen.torchmaster.config.title"));
        *///?}
        this.parent = parent;
    }

    public static void open()
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        //? if >=1.17.1 {
        minecraft.setScreen(new TorchmasterConfigScreen(minecraft.currentScreen));
        //?} else {
        /*minecraft.openScreen(new TorchmasterConfigScreen(minecraft.currentScreen));
        *///?}
    }

    @Override
    protected void init()
    {
        entries.clear();
        ITorchmasterConfig config = TorchmasterRuntime.getConfig();

        int fieldX = fieldX();
        int fieldWidth = fieldWidth();
        int listFieldX = listFieldX();
        int listFieldWidth = listFieldWidth();
        int rowY = 48;
        int rowHeight = rowHeight();

        addIntEntry("screen.torchmaster.config.feralFlareTickRate", config.getFeralFlareTickRate(), fieldX, fieldWidth, rowY);
        addIntEntry("screen.torchmaster.config.feralFlareLanternLightHardcap", config.getFeralFlareLanternLightCountHardcap(), fieldX, fieldWidth, rowY += rowHeight);
        addIntEntry("screen.torchmaster.config.feralFlareRadius", config.getFeralFlareRadius(), fieldX, fieldWidth, rowY += rowHeight);
        addIntEntry("screen.torchmaster.config.feralFlareMinLightLevel", config.getFeralFlareMinLightLevel(), fieldX, fieldWidth, rowY += rowHeight);
        addIntEntry("screen.torchmaster.config.dreadLampRadius", config.getDreadLampRadius(), fieldX, fieldWidth, rowY += rowHeight);
        addIntEntry("screen.torchmaster.config.megaTorchRadius", config.getMegaTorchRadius(), fieldX, fieldWidth, rowY += rowHeight);
        addBooleanEntry("screen.torchmaster.config.aggressiveSpawnChecks", config.getAggressiveSpawnChecks(), fieldX, rowY += rowHeight);
        addBooleanEntry("screen.torchmaster.config.blockOnlyNaturalSpawns", config.getBlockOnlyNaturalSpawns(), fieldX, rowY += rowHeight);
        addBooleanEntry("screen.torchmaster.config.blockVillageSieges", config.getBlockVillageSieges(), fieldX, rowY += rowHeight);
        addListEntry("screen.torchmaster.config.megaTorchEntityBlockListOverrides", config.getMegaTorchEntityBlockListOverrides(), listFieldX, listFieldWidth, rowY += rowHeight);
        addListEntry("screen.torchmaster.config.dreadLampEntityBlockListOverrides", config.getDreadLampEntityBlockListOverrides(), listFieldX, listFieldWidth, rowY += rowHeight);

        int bottomButtonWidth = bottomButtonWidth();
        int buttonGap = 4;
        int totalButtonWidth = bottomButtonWidth * 3 + buttonGap * 2;
        int buttonX = panelLeft() + (panelWidth() - totalButtonWidth) / 2;
        addCompatWidget(button(buttonX, height - 28, bottomButtonWidth, BUTTON_HEIGHT, text("screen.torchmaster.config.save"), button -> save()));
        addCompatWidget(button(buttonX + bottomButtonWidth + buttonGap, height - 28, bottomButtonWidth, BUTTON_HEIGHT, text("screen.torchmaster.config.reset"), button -> reset()));
        addCompatWidget(button(buttonX + (bottomButtonWidth + buttonGap) * 2, height - 28, bottomButtonWidth, BUTTON_HEIGHT, text("gui.done"), button -> closeScreen()));

        updateEntryPositions();
    }

    private void addIntEntry(String translationKey, int value, int x, int width, int y)
    {
        TextFieldWidget editBox = textField(x, widgetY(y), width, BUTTON_HEIGHT, translationKey);
        editBox.setTextPredicate(text -> text.isEmpty() || text.matches("-?\\d*"));
        editBox.setMaxLength(10);
        editBox.setText(Integer.toString(value));
        addCompatWidget(editBox);
        entries.add(new IntEntry(translationKey, y, editBox));
    }

    private void addListEntry(String translationKey, List<String> values, int x, int width, int y)
    {
        TextFieldWidget editBox = textField(x, widgetY(y), width, BUTTON_HEIGHT, translationKey);
        editBox.setMaxLength(1024);
        editBox.setText(String.join(", ", values));
        addCompatWidget(editBox);
        entries.add(new ListEntry(translationKey, y, editBox));
    }

    private void addBooleanEntry(String translationKey, boolean value, int x, int y)
    {
        BooleanEntry entry = new BooleanEntry(translationKey, y, value);
        ButtonWidget button = button(booleanButtonX(x), widgetY(y), booleanButtonWidth(), BUTTON_HEIGHT, booleanLabel(value), ignored -> {
            entry.toggle();
            ignored.setMessage(booleanLabel(entry.value));
        });
        entry.button = button;
        addCompatWidget(button);
        entries.add(entry);
    }

    private ButtonWidget addCompatWidget(ButtonWidget widget)
    {
        //? if >=1.17 {
        return addDrawableChild(widget);
        //?} else {
        /*return addButton(widget);
        *///?}
    }

    private TextFieldWidget addCompatWidget(TextFieldWidget widget)
    {
        //? if >=1.17 {
        return addDrawableChild(widget);
        //?} else {
        /*return addButton(widget);
        *///?}
    }

    //? if >=1.16
    private static ButtonWidget button(int x, int y, int width, int height, Text label, ButtonWidget.PressAction onPress)
    //? if <1.16
    //private static ButtonWidget button(int x, int y, int width, int height, String label, ButtonWidget.PressAction onPress)
    {
        //? if >=1.19.4 {
        return ButtonWidget.builder(label, onPress).dimensions(x, y, width, height).build();
        //?} else {
        /*return new ButtonWidget(x, y, width, height, label, onPress);
        *///?}
    }

    //? if >=1.16
    private static Text booleanLabel(boolean value)
    //? if <1.16
    //private static String booleanLabel(boolean value)
    {
        return text(value ? "options.on" : "options.off");
    }

    //? if >=1.16
    private static Text text(String translationKey)
    //? if <1.16
    //private static String text(String translationKey)
    {
        //? if >=1.16 {
        return MinecraftText.translatable(translationKey);
        //?} else {
        /*return I18n.translate(translationKey);
        *///?}
    }

    private TextFieldWidget textField(int x, int y, int width, int height, String translationKey)
    {
        //? if >=1.16 {
        return new TextFieldWidget(textRenderer, x, y, width, height, text(translationKey));
        //?} else {
        /*return new TextFieldWidget(font, x, y, width, height, text(translationKey));
        *///?}
    }

    //? if >=1.16
    private static Text emptyText()
    //? if <1.16
    //private static String emptyText()
    {
        //? if <1.16 {
        /*return "";
        *///?}
        //? if >=1.19 {
        return Text.empty();
        //?} else if >=1.16 {
        /*return new LiteralText("");
        *///?}
    }

    private void save()
    {
        ITorchmasterConfig loadedConfig = TorchmasterRuntime.getConfig();
        if (!(loadedConfig instanceof TorchmasterTomlConfig)) {
            setStatus("screen.torchmaster.config.unsupported", 0xFFFF5555);
            return;
        }

        List<Integer> ints = new ArrayList<>();
        List<Boolean> booleans = new ArrayList<>();
        List<List<String>> lists = new ArrayList<>();
        for (Entry entry : entries) {
            if (!entry.read(ints, booleans, lists)) {
                return;
            }
        }

        ((TorchmasterTomlConfig)loadedConfig).save(
                ints.get(0),
                ints.get(1),
                ints.get(2),
                ints.get(3),
                ints.get(4),
                ints.get(5),
                booleans.get(0),
                booleans.get(1),
                booleans.get(2),
                lists.get(0),
                lists.get(1));
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

    private void clearCompatWidgets()
    {
        //? if >=1.17 {
        clearChildren();
        //?} else {
        /*buttons.clear();
        children.clear();
        *///?}
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
        /*client.setScreen(parent);
        *///?}
        //? if <1.17.1 {
        /*onClose();
        *///?}
    }

    //? if >=1.18 {
    @Override
    public void close()
    {
        client.setScreen(parent);
    }
    //?}
    //? if >=1.17.1 && <1.18 {
    /*@Override
    public void onClose()
    {
        client.setScreen(parent);
    }
    *///?}
    //? if >=1.16 && <1.17.1 {
    /*@Override
    public void onClose()
    {
        client.openScreen(parent);
    }
    *///?}
    //? if <1.16 {
    /*@Override
    public void onClose()
    {
        minecraft.openScreen(parent);
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
        int maxScroll = Math.max(0, entries.size() * rowHeight() - (height - 94));
        scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - (int)(delta * rowHeight())));
        updateEntryPositions();
        return true;
    }

    private void updateEntryPositions()
    {
        int fieldX = fieldX();
        int top = 44;
        int bottom = height - 38;
        for (Entry entry : entries) {
            int y = entry.baseY - scrollOffset;
            entry.setPosition(fieldX, y);
            entry.setVisible(y >= top - rowHeight() && y <= bottom - BUTTON_HEIGHT);
        }
    }

    private int panelWidth()
    {
        return Math.max(180, Math.min(MAX_PANEL_WIDTH, width - SIDE_MARGIN * 2));
    }

    private int panelLeft()
    {
        return Math.max(SIDE_MARGIN, (width - panelWidth()) / 2);
    }

    private boolean compactLayout()
    {
        return panelWidth() < COMPACT_BREAKPOINT;
    }

    private int rowHeight()
    {
        return compactLayout() ? COMPACT_ROW_HEIGHT : WIDE_ROW_HEIGHT;
    }

    private int fieldWidth()
    {
        if (compactLayout()) {
            return Math.max(80, panelWidth() - 24);
        }
        return Math.min(WIDE_FIELD_WIDTH, Math.max(110, panelWidth() / 3));
    }

    private int listFieldWidth()
    {
        if (compactLayout()) {
            return Math.max(80, panelWidth() - 24);
        }
        return Math.min(WIDE_LIST_FIELD_WIDTH, Math.max(160, panelWidth() / 2));
    }

    private int fieldX()
    {
        if (compactLayout()) {
            return panelLeft() + 12;
        }
        return panelLeft() + panelWidth() - fieldWidth() - 12;
    }

    private int listFieldX()
    {
        if (compactLayout()) {
            return panelLeft() + 12;
        }
        return panelLeft() + panelWidth() - listFieldWidth() - 12;
    }

    private int widgetY(int rowY)
    {
        return compactLayout() ? rowY + 16 : rowY;
    }

    private int booleanButtonWidth()
    {
        return compactLayout() ? fieldWidth() : BUTTON_WIDTH;
    }

    private int booleanButtonX(int fieldX)
    {
        return compactLayout() ? fieldX : fieldX + fieldWidth() - booleanButtonWidth();
    }

    private int bottomButtonWidth()
    {
        return Math.min(100, Math.max(52, (panelWidth() - 32) / 3));
    }

    //? if >=1.20 {
    @Override
    public void render(DrawContext graphics, int mouseX, int mouseY, float partialTick)
    {
        int left = panelLeft();
        int right = left + panelWidth();
        int top = 32;
        int bottom = height - 34;

        super.render(graphics, mouseX, mouseY, partialTick);

        drawPanelFrame(graphics, left, top, right, bottom);
        graphics.drawCenteredTextWithShadow(textRenderer, title, width / 2, 14, 0xFFFFFFFF);

        for (Entry entry : entries) {
            if (entry.visible) {
                graphics.drawText(textRenderer, text(entry.translationKey), left + 12, compactLayout() ? entry.y : entry.y + 6, 0xFFE0E0E0, false);
            }
        }
        graphics.drawCenteredTextWithShadow(textRenderer, status, width / 2, height - 48, statusColor);
    }
    //?} else if >=1.19.4 {
    /*@Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        int left = panelLeft();
        int right = left + panelWidth();
        int top = 32;
        int bottom = height - 34;

        super.render(poseStack, mouseX, mouseY, partialTick);

        drawPanelFrame(poseStack, left, top, right, bottom);
        drawCenteredTextWithShadow(poseStack, textRenderer, title, width / 2, 14, 0xFFFFFFFF);

        for (Entry entry : entries) {
            if (entry.visible) {
                drawTextWithShadow(poseStack, textRenderer, text(entry.translationKey), left + 12, compactLayout() ? entry.y : entry.y + 6, 0xFFE0E0E0);
            }
        }
        drawCenteredTextWithShadow(poseStack, textRenderer, status, width / 2, height - 48, statusColor);
    }
    *///?} else if >=1.16 {
    /*@Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        int left = panelLeft();
        int right = left + panelWidth();
        int top = 32;
        int bottom = height - 34;

        super.render(poseStack, mouseX, mouseY, partialTick);

        drawPanelFrame(poseStack, left, top, right, bottom);
        drawCenteredText(poseStack, textRenderer, title, width / 2, 14, 0xFFFFFFFF);

        for (Entry entry : entries) {
            if (entry.visible) {
                drawTextWithShadow(poseStack, textRenderer, text(entry.translationKey), left + 12, compactLayout() ? entry.y : entry.y + 6, 0xFFE0E0E0);
            }
        }
        drawCenteredText(poseStack, textRenderer, status, width / 2, height - 48, statusColor);
    }
    *///?} else {
    /*@Override
    public void render(int mouseX, int mouseY, float partialTick)
    {
        int left = panelLeft();
        int right = left + panelWidth();
        int top = 32;
        int bottom = height - 34;

        renderBackground();
        fill(left, top, right, bottom, 0xAA101010);
        super.render(mouseX, mouseY, partialTick);

        drawPanelFrame(left, top, right, bottom);
        drawCenteredString(font, text("screen.torchmaster.config.title"), width / 2, 14, 0xFFFFFFFF);

        for (Entry entry : entries) {
            if (entry.visible) {
                drawString(font, text(entry.translationKey), left + 12, compactLayout() ? entry.y : entry.y + 6, 0xFFE0E0E0);
            }
        }
        drawCenteredString(font, status, width / 2, height - 48, statusColor);
    }
    *///?}

    //? if >=1.21 {
    @Override
    public void renderBackground(DrawContext graphics, int mouseX, int mouseY, float partialTick)
    {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.fill(panelLeft(), 32, panelLeft() + panelWidth(), height - 34, 0xAA101010);
    }

    private void drawPanelFrame(DrawContext graphics, int left, int top, int right, int bottom)
    {
        graphics.fill(left, top, right, top + 1, 0xFF404040);
        graphics.fill(left, top, left + 1, bottom, 0xFF404040);
        graphics.fill(left, bottom - 1, right, bottom, 0xFF202020);
        graphics.fill(right - 1, top, right, bottom, 0xFF202020);
    }
    //?} else if >=1.20.6 {
    /*@Override
    public void renderBackground(DrawContext graphics, int mouseX, int mouseY, float partialTick)
    {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.fill(panelLeft(), 32, panelLeft() + panelWidth(), height - 34, 0xAA101010);
    }

    private void drawPanelFrame(DrawContext graphics, int left, int top, int right, int bottom)
    {
        graphics.fill(left, top, right, top + 1, 0xFF404040);
        graphics.fill(left, top, left + 1, bottom, 0xFF404040);
        graphics.fill(left, bottom - 1, right, bottom, 0xFF202020);
        graphics.fill(right - 1, top, right, bottom, 0xFF202020);
    }
    *///?} else if >=1.20 {
    /*@Override
    public void renderBackground(DrawContext graphics)
    {
        super.renderBackground(graphics);
        graphics.fill(panelLeft(), 32, panelLeft() + panelWidth(), height - 34, 0xAA101010);
    }

    private void drawPanelFrame(DrawContext graphics, int left, int top, int right, int bottom)
    {
        graphics.fill(left, top, right, top + 1, 0xFF404040);
        graphics.fill(left, top, left + 1, bottom, 0xFF404040);
        graphics.fill(left, bottom - 1, right, bottom, 0xFF202020);
        graphics.fill(right - 1, top, right, bottom, 0xFF202020);
    }
    *///?} else if >=1.16 {
    /*@Override
    public void renderBackground(MatrixStack poseStack)
    {
        super.renderBackground(poseStack);
        fill(poseStack, panelLeft(), 32, panelLeft() + panelWidth(), height - 34, 0xAA101010);
    }

    private void drawPanelFrame(MatrixStack poseStack, int left, int top, int right, int bottom)
    {
        fill(poseStack, left, top, right, top + 1, 0xFF404040);
        fill(poseStack, left, top, left + 1, bottom, 0xFF404040);
        fill(poseStack, left, bottom - 1, right, bottom, 0xFF202020);
        fill(poseStack, right - 1, top, right, bottom, 0xFF202020);
    }
    *///?} else {
    /*private void drawPanelFrame(int left, int top, int right, int bottom)
    {
        fill(left, top, right, top + 1, 0xFF404040);
        fill(left, top, left + 1, bottom, 0xFF404040);
        fill(left, bottom - 1, right, bottom, 0xFF202020);
        fill(right - 1, top, right, bottom, 0xFF202020);
    }
    *///?}

    private abstract class Entry
    {
        protected final String translationKey;
        protected final int baseY;
        protected int y;
        protected boolean visible;

        Entry(String translationKey, int baseY)
        {
            this.translationKey = translationKey;
            this.baseY = baseY;
        }

        abstract void setPosition(int fieldX, int y);

        abstract void setVisible(boolean visible);

        abstract boolean read(List<Integer> ints, List<Boolean> booleans, List<List<String>> lists);
    }

    private class IntEntry extends Entry
    {
        private final TextFieldWidget editBox;

        IntEntry(String translationKey, int baseY, TextFieldWidget editBox)
        {
            super(translationKey, baseY);
            this.editBox = editBox;
        }

        @Override
        void setPosition(int fieldX, int y)
        {
            this.y = y;
            setWidgetX(editBox, fieldX);
            setWidgetY(editBox, widgetY(y));
        }

        @Override
        void setVisible(boolean visible)
        {
            this.visible = visible;
            editBox.visible = visible;
            editBox.active = visible;
        }

        @Override
        boolean read(List<Integer> ints, List<Boolean> booleans, List<List<String>> lists)
        {
            try {
                ints.add(Integer.parseInt(editBox.getText().trim()));
                return true;
            } catch (NumberFormatException ignored) {
                setStatus("screen.torchmaster.config.invalidNumber", 0xFFFF5555);
                return false;
            }
        }
    }

    private class BooleanEntry extends Entry
    {
        private boolean value;
        private ButtonWidget button;

        BooleanEntry(String translationKey, int baseY, boolean value)
        {
            super(translationKey, baseY);
            this.value = value;
        }

        void toggle()
        {
            value = !value;
        }

        @Override
        void setPosition(int fieldX, int y)
        {
            this.y = y;
            setWidgetX(button, booleanButtonX(fieldX));
            setWidgetY(button, widgetY(y));
        }

        @Override
        void setVisible(boolean visible)
        {
            this.visible = visible;
            button.visible = visible;
            button.active = visible;
        }

        @Override
        boolean read(List<Integer> ints, List<Boolean> booleans, List<List<String>> lists)
        {
            booleans.add(value);
            return true;
        }
    }

    private class ListEntry extends Entry
    {
        private final TextFieldWidget editBox;

        ListEntry(String translationKey, int baseY, TextFieldWidget editBox)
        {
            super(translationKey, baseY);
            this.editBox = editBox;
        }

        @Override
        void setPosition(int fieldX, int y)
        {
            this.y = y;
            setWidgetX(editBox, listFieldX());
            setWidgetY(editBox, widgetY(y));
        }

        @Override
        void setVisible(boolean visible)
        {
            this.visible = visible;
            editBox.visible = visible;
            editBox.active = visible;
        }

        @Override
        boolean read(List<Integer> ints, List<Boolean> booleans, List<List<String>> lists)
        {
            List<String> parsed = parseList(editBox.getText());
            for (String value : parsed) {
                if (!EntityFilterList.IsValidFilterString(value)) {
                    setStatus("screen.torchmaster.config.invalidFilter", 0xFFFF5555);
                    return false;
                }
            }
            lists.add(parsed);
            return true;
        }
    }

    private static List<String> parseList(String text)
    {
        List<String> values = new ArrayList<>();
        if (text.trim().isEmpty()) {
            return values;
        }
        for (String value : Arrays.asList(text.split(","))) {
            String trimmed = value.trim();
            if (!trimmed.isEmpty()) {
                values.add(trimmed);
            }
        }
        return values;
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
