//? if >=1.16 {
package net.xalcon.torchmaster.client;

import net.minecraft.client.Minecraft;
//? if >=1.20 {
import net.minecraft.client.gui.GuiGraphics;
//?} else {
/*import com.mojang.blaze3d.vertex.PoseStack;
*///?}
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
//? if >=1.21.9 {
/*import net.minecraft.client.input.KeyEvent;
*///?}
import net.minecraft.network.chat.Component;
//? if <1.19 {
/*import net.minecraft.network.chat.TextComponent;
*///?}
import net.xalcon.torchmaster.EntityFilterList;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.config.TorchmasterTomlConfig;
import net.xalcon.torchmaster.minecraft.MinecraftText;
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
    private Component status = emptyText();
    private int statusColor = 0xFFA0A0A0;

    public TorchmasterConfigScreen(Screen parent)
    {
        super(text("screen.torchmaster.config.title"));
        this.parent = parent;
    }

    public static void open()
    {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.setScreen(new TorchmasterConfigScreen(minecraft.screen));
    }

    @Override
    protected void init()
    {
        entries.clear();
        ITorchmasterConfig config = Torchmaster.getConfig();

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
        addCompatWidget(button(buttonX + (bottomButtonWidth + buttonGap) * 2, height - 28, bottomButtonWidth, BUTTON_HEIGHT, text("gui.done"), button -> onClose()));

        updateEntryPositions();
    }

    private void addIntEntry(String translationKey, int value, int x, int width, int y)
    {
        EditBox editBox = new EditBox(font, x, widgetY(y), width, BUTTON_HEIGHT, text(translationKey));
        editBox.setFilter(text -> text.isEmpty() || text.matches("-?\\d*"));
        editBox.setMaxLength(10);
        editBox.setValue(Integer.toString(value));
        addCompatWidget(editBox);
        entries.add(new IntEntry(translationKey, y, editBox));
    }

    private void addListEntry(String translationKey, List<String> values, int x, int width, int y)
    {
        EditBox editBox = new EditBox(font, x, widgetY(y), width, BUTTON_HEIGHT, text(translationKey));
        editBox.setMaxLength(1024);
        editBox.setValue(String.join(", ", values));
        addCompatWidget(editBox);
        entries.add(new ListEntry(translationKey, y, editBox));
    }

    private void addBooleanEntry(String translationKey, boolean value, int x, int y)
    {
        BooleanEntry entry = new BooleanEntry(translationKey, y, value);
        Button button = button(booleanButtonX(x), widgetY(y), booleanButtonWidth(), BUTTON_HEIGHT, booleanLabel(value), ignored -> {
            entry.toggle();
            ignored.setMessage(booleanLabel(entry.value));
        });
        entry.button = button;
        addCompatWidget(button);
        entries.add(entry);
    }

    private Button addCompatWidget(Button widget)
    {
        //? if >=1.17 {
        return addRenderableWidget(widget);
        //?} else {
        /*return addButton(widget);
        *///?}
    }

    private EditBox addCompatWidget(EditBox widget)
    {
        //? if >=1.17 {
        return addRenderableWidget(widget);
        //?} else {
        /*return addButton(widget);
        *///?}
    }

    private static Button button(int x, int y, int width, int height, Component label, Button.OnPress onPress)
    {
        //? if >=1.19.4 {
        return Button.builder(label, onPress).bounds(x, y, width, height).build();
        //?} else {
        /*return new Button(x, y, width, height, label, onPress);
        *///?}
    }

    private static Component booleanLabel(boolean value)
    {
        return text(value ? "options.on" : "options.off");
    }

    private static Component text(String translationKey)
    {
        return MinecraftText.translatable(translationKey);
    }

    private static Component emptyText()
    {
        //? if >=1.19 {
        return Component.empty();
        //?} else {
        /*return new TextComponent("");
        *///?}
    }

    private void save()
    {
        ITorchmasterConfig loadedConfig = Torchmaster.getConfig();
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
        Torchmaster.onWorldLoaded();
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
        clearWidgets();
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

    @Override
    public void onClose()
    {
        minecraft.setScreen(parent);
    }

    //? if >=1.21.9 {
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

    //? if >=1.21 {
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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        //? if >=1.21 {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        //?} else {
        /*renderBackground(graphics);
        *///?}
        int left = panelLeft();
        int right = left + panelWidth();
        graphics.fill(left, 32, right, height - 34, 0xAA101010);
        graphics.fill(left, 32, right, 33, 0xFF404040);
        graphics.fill(left, height - 35, right, height - 34, 0xFF404040);
        graphics.drawCenteredString(font, title, width / 2, 14, 0xFFFFFFFF);

        for (Entry entry : entries) {
            if (entry.visible) {
                graphics.drawString(font, text(entry.translationKey), left + 12, compactLayout() ? entry.y : entry.y + 6, 0xFFE0E0E0, false);
            }
        }
        graphics.drawCenteredString(font, status, width / 2, height - 48, statusColor);

        super.render(graphics, mouseX, mouseY, partialTick);
    }
    //?} else {
    /*@Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        renderBackground(poseStack);
        int left = panelLeft();
        int right = left + panelWidth();
        fill(poseStack, left, 32, right, height - 34, 0xAA101010);
        fill(poseStack, left, 32, right, 33, 0xFF404040);
        fill(poseStack, left, height - 35, right, height - 34, 0xFF404040);
        drawCenteredString(poseStack, font, title, width / 2, 14, 0xFFFFFFFF);

        for (Entry entry : entries) {
            if (entry.visible) {
                drawString(poseStack, font, text(entry.translationKey), left + 12, compactLayout() ? entry.y : entry.y + 6, 0xFFE0E0E0);
            }
        }
        drawCenteredString(poseStack, font, status, width / 2, height - 48, statusColor);

        super.render(poseStack, mouseX, mouseY, partialTick);
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
        private final EditBox editBox;

        IntEntry(String translationKey, int baseY, EditBox editBox)
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
                ints.add(Integer.parseInt(editBox.getValue().trim()));
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
        private Button button;

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
        private final EditBox editBox;

        ListEntry(String translationKey, int baseY, EditBox editBox)
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
            List<String> parsed = parseList(editBox.getValue());
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

    private static void setWidgetX(EditBox widget, int x)
    {
        //? if >=1.19.4 {
        widget.setX(x);
        //?} else {
        /*widget.x = x;
        *///?}
    }

    private static void setWidgetY(EditBox widget, int y)
    {
        //? if >=1.19.4 {
        widget.setY(y);
        //?} else {
        /*widget.y = y;
        *///?}
    }

    private static void setWidgetX(Button widget, int x)
    {
        //? if >=1.19.4 {
        widget.setX(x);
        //?} else {
        /*widget.x = x;
        *///?}
    }

    private static void setWidgetY(Button widget, int y)
    {
        //? if >=1.19.4 {
        widget.setY(y);
        //?} else {
        /*widget.y = y;
        *///?}
    }
}
//?}
