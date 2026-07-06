package net.xalcon.torchmaster.client;

//? if >=1.20
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
//? if >=1.21.11
//import net.minecraft.client.input.KeyInput;
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
    private final List<TorchmasterConfigWidgetRows.Row> entries = new ArrayList<>();
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
        entries.addAll(TorchmasterConfigWidgetRows.create(TorchmasterConfigEntries.fromConfig(config), layout, BUTTON_HEIGHT, widgetFactory()));

        int bottomButtonWidth = layout.bottomButtonWidth();
        int buttonGap = 4;
        int totalButtonWidth = bottomButtonWidth * 3 + buttonGap * 2;
        int buttonX = layout.panelLeft() + (layout.panelWidth() - totalButtonWidth) / 2;
        addCompatWidget(button(buttonX, height - 28, bottomButtonWidth, BUTTON_HEIGHT, text("screen.torchmaster.config.save"), button -> save()));
        addCompatWidget(button(buttonX + bottomButtonWidth + buttonGap, height - 28, bottomButtonWidth, BUTTON_HEIGHT, text("screen.torchmaster.config.reset"), button -> reset()));
        addCompatWidget(button(buttonX + (bottomButtonWidth + buttonGap) * 2, height - 28, bottomButtonWidth, BUTTON_HEIGHT, text("gui.done"), button -> closeScreen()));

        updateEntryPositions();
    }

    private void save()
    {
        ITorchmasterConfig loadedConfig = TorchmasterRuntime.getConfig();
        if (!(loadedConfig instanceof TorchmasterTomlConfig)) {
            setStatus("screen.torchmaster.config.unsupported", 0xFFFF5555);
            return;
        }

        TorchmasterConfigEntries.Collector collector = TorchmasterConfigEntries.collector();
        for (TorchmasterConfigWidgetRows.Row entry : entries) {
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
        scrollOffset = TorchmasterConfigWidgetRows.scrollOffset(scrollOffset, delta, entries.size(), layout, height);
        updateEntryPositions();
        return true;
    }

    private void updateEntryPositions()
    {
        TorchmasterConfigWidgetRows.updatePositions(entries, layout(), scrollOffset, height, BUTTON_HEIGHT);
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

        TorchmasterScreenRenderAdapter.frame(graphics, left, top, right, bottom);
        TorchmasterScreenRenderAdapter.centered(graphics, textRenderer, text("screen.torchmaster.config.title"), width / 2, 14, TorchmasterPanelRenderer.TITLE_COLOR);

        for (TorchmasterConfigWidgetRows.Row entry : entries) {
            if (entry.visible()) {
                TorchmasterScreenRenderAdapter.label(graphics, textRenderer, text(entry.translationKey()), left + 12, layout.compact() ? entry.y() : entry.y() + 6, TorchmasterPanelRenderer.LABEL_COLOR);
            }
        }
        TorchmasterScreenRenderAdapter.centered(graphics, textRenderer, status, width / 2, height - 48, statusColor);
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

        TorchmasterScreenRenderAdapter.frame(poseStack, left, top, right, bottom);
        TorchmasterScreenRenderAdapter.centered(poseStack, textRenderer, text("screen.torchmaster.config.title"), width / 2, 14, TorchmasterPanelRenderer.TITLE_COLOR, true);

        for (TorchmasterConfigWidgetRows.Row entry : entries) {
            if (entry.visible()) {
                TorchmasterScreenRenderAdapter.label(poseStack, textRenderer, text(entry.translationKey()), left + 12, layout.compact() ? entry.y() : entry.y() + 6, TorchmasterPanelRenderer.LABEL_COLOR);
            }
        }
        TorchmasterScreenRenderAdapter.centered(poseStack, textRenderer, status, width / 2, height - 48, statusColor, true);
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

        TorchmasterScreenRenderAdapter.frame(poseStack, left, top, right, bottom);
        TorchmasterScreenRenderAdapter.centered(poseStack, textRenderer, text("screen.torchmaster.config.title"), width / 2, 14, TorchmasterPanelRenderer.TITLE_COLOR, false);

        for (TorchmasterConfigWidgetRows.Row entry : entries) {
            if (entry.visible()) {
                TorchmasterScreenRenderAdapter.label(poseStack, textRenderer, text(entry.translationKey()), left + 12, layout.compact() ? entry.y() : entry.y() + 6, TorchmasterPanelRenderer.LABEL_COLOR);
            }
        }
        TorchmasterScreenRenderAdapter.centered(poseStack, textRenderer, status, width / 2, height - 48, statusColor, false);
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
        TorchmasterScreenRenderAdapter.fill(TorchmasterPanelRenderer.background(left, top, right, bottom));
        super.render(mouseX, mouseY, partialTick);

        TorchmasterScreenRenderAdapter.frame(left, top, right, bottom);
        TorchmasterScreenRenderAdapter.centered(font, text("screen.torchmaster.config.title"), width / 2, 14, TorchmasterPanelRenderer.TITLE_COLOR);

        for (TorchmasterConfigWidgetRows.Row entry : entries) {
            if (entry.visible()) {
                TorchmasterScreenRenderAdapter.label(font, text(entry.translationKey()), left + 12, layout.compact() ? entry.y() : entry.y() + 6, TorchmasterPanelRenderer.LABEL_COLOR);
            }
        }
        TorchmasterScreenRenderAdapter.centered(font, status, width / 2, height - 48, statusColor);
    }
    *///?}

    //? if >=1.21 {
    @Override
    public void renderBackground(DrawContext graphics, int mouseX, int mouseY, float partialTick)
    {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        TorchmasterConfigScreenLayout layout = layout();
        TorchmasterScreenRenderAdapter.fill(graphics, TorchmasterPanelRenderer.background(layout.panelLeft(), layout.panelTop(), layout.panelRight(), layout.panelBottom()));
    }
    //?} else if >=1.20.6 {
    /*@Override
    public void renderBackground(DrawContext graphics, int mouseX, int mouseY, float partialTick)
    {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        TorchmasterConfigScreenLayout layout = layout();
        TorchmasterScreenRenderAdapter.fill(graphics, TorchmasterPanelRenderer.background(layout.panelLeft(), layout.panelTop(), layout.panelRight(), layout.panelBottom()));
    }
    *///?} else if >=1.20 {
    /*@Override
    public void renderBackground(DrawContext graphics)
    {
        super.renderBackground(graphics);
        TorchmasterConfigScreenLayout layout = layout();
        TorchmasterScreenRenderAdapter.fill(graphics, TorchmasterPanelRenderer.background(layout.panelLeft(), layout.panelTop(), layout.panelRight(), layout.panelBottom()));
    }
    *///?} else if >=1.16 {
    /*@Override
    public void renderBackground(MatrixStack poseStack)
    {
        super.renderBackground(poseStack);
        TorchmasterConfigScreenLayout layout = layout();
        TorchmasterScreenRenderAdapter.fill(poseStack, TorchmasterPanelRenderer.background(layout.panelLeft(), layout.panelTop(), layout.panelRight(), layout.panelBottom()));
    }
    *///?} else {
    /*
    *///?}

    private TorchmasterConfigWidgetRows.WidgetFactory widgetFactory()
    {
        return new TorchmasterConfigWidgetRows.WidgetFactory()
        {
            @Override
            public net.minecraft.client.gui.widget.TextFieldWidget textField(int x, int y, int width, int height, String translationKey)
            {
                return TorchmasterConfigScreen.this.textField(x, y, width, height, translationKey);
            }

            @Override
            public net.minecraft.client.gui.widget.ButtonWidget button(int x, int y, int width, int height, CompatText label, net.minecraft.client.gui.widget.ButtonWidget.PressAction onPress)
            {
                return TorchmasterConfigScreen.button(x, y, width, height, label, onPress);
            }

            @Override
            public net.minecraft.client.gui.widget.TextFieldWidget add(net.minecraft.client.gui.widget.TextFieldWidget widget)
            {
                return addCompatWidget(widget);
            }

            @Override
            public net.minecraft.client.gui.widget.ButtonWidget add(net.minecraft.client.gui.widget.ButtonWidget widget)
            {
                return addCompatWidget(widget);
            }

            @Override
            public CompatText booleanLabel(boolean value)
            {
                return TorchmasterConfigScreen.booleanLabel(value);
            }
        };
    }
}
