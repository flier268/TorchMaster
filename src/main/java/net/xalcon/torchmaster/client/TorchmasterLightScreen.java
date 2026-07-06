package net.xalcon.torchmaster.client;

import net.minecraft.client.MinecraftClient;
//? if >=1.20
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
//? if <1.16
/*import net.minecraft.client.resource.language.I18n;*/
//? if >=1.16 && <1.20
//import net.minecraft.client.util.math.MatrixStack;
//? if >=1.19.4
import net.minecraft.registry.RegistryKey;
//? if >=1.16.5 && <1.19.4
//import net.minecraft.util.registry.RegistryKey;
//? if >=1.16
import net.minecraft.text.Text;
//? if <1.16
/*import net.minecraft.text.TranslatableText;*/
//? if >=1.16 && <1.19 {
/*import net.minecraft.text.LiteralText;
*///?}
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.blocks.LightType;
//? if >=1.16
import net.xalcon.torchmaster.minecraft.adapter.MinecraftText;

public class TorchmasterLightScreen extends Screen
{
    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 118;
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;
    private static final int SETTINGS_BUTTON_SIZE = 20;

    private final Screen parent;
    private final BlockPos pos;
    //? if >=1.16.5
    private final RegistryKey<World> dimension;
    //? if <1.16.5
    //private final Object dimension;
    private final TorchmasterLightScreenModel model;
    private ButtonWidget visibilityButton;

    private TorchmasterLightScreen(Screen parent, BlockPos pos,
            //? if >=1.16.5
            RegistryKey<World> dimension
            //? if <1.16.5
            //Object dimension
            , LightType lightType)
    {
        //? if >=1.16 {
        super(text(TorchmasterLightScreenModel.TITLE_KEY));
        //?} else {
        /*super(new TranslatableText(TorchmasterLightScreenModel.TITLE_KEY));
        *///?}
        this.parent = parent;
        this.pos = pos.toImmutable();
        this.dimension = dimension;
        this.model = new TorchmasterLightScreenModel(lightType, TorchmasterRuntime.getConfig());
    }

    public static void open(BlockPos pos,
            //? if >=1.16.5
            RegistryKey<World> dimension
            //? if <1.16.5
            //Object dimension
            , LightType lightType)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        //? if >=1.17.1 {
        minecraft.setScreen(new TorchmasterLightScreen(minecraft.currentScreen, pos, dimension, lightType));
        //?} else {
        /*minecraft.openScreen(new TorchmasterLightScreen(minecraft.currentScreen, pos, dimension, lightType));
        *///?}
    }

    @Override
    protected void init()
    {
        TorchmasterLightScreenLayout layout = layout();
        addCompatWidget(button(layout.panelRight() - SETTINGS_BUTTON_SIZE - 8, layout.panelTop() + 8, SETTINGS_BUTTON_SIZE, SETTINGS_BUTTON_SIZE, literal("\u2699"),
                ignored -> openConfigScreen()));
        visibilityButton = button(layout.centerX() - BUTTON_WIDTH / 2, layout.panelTop() + 54, BUTTON_WIDTH, BUTTON_HEIGHT, visibilityLabel(),
                ignored -> toggleVisibility());
        addCompatWidget(visibilityButton);
        addCompatWidget(button(layout.centerX() - 50, layout.panelTop() + 84, 100, BUTTON_HEIGHT, text("gui.done"), ignored -> closeScreen()));
    }

    private void toggleVisibility()
    {
        TorchmasterLightRangeDisplay.toggle(dimension, pos, model.lightType(), model.radius());
        visibilityButton.setMessage(visibilityLabel());
    }

    private void openConfigScreen()
    {
        //? if >=1.17.1 {
        client.setScreen(new TorchmasterConfigScreen(this));
        //?} else if >=1.16 {
        /*client.openScreen(new TorchmasterConfigScreen(this));
        *///?} else {
        /*minecraft.openScreen(new TorchmasterConfigScreen(this));
        *///?}
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

    //? if >=1.16
    private Text visibilityLabel()
    //? if <1.16
    //private String visibilityLabel()
    {
        return text(model.visibilityButtonKey(TorchmasterLightRangeDisplay.isVisible(dimension, pos)));
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

    //? if >=1.20 {
    @Override
    public void render(DrawContext graphics, int mouseX, int mouseY, float partialTick)
    {
        drawPanelBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        drawPanelFrame(graphics);
        drawPanelLabels(graphics);
    }
    //?} else if >=1.16 {
    /*@Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        drawPanelBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        drawPanelFrame(poseStack);
        drawPanelLabels(poseStack);
    }
    *///?} else {
    /*@Override
    public void render(int mouseX, int mouseY, float partialTick)
    {
        drawPanelBackground();
        super.render(mouseX, mouseY, partialTick);
        drawPanelFrame();
        drawPanelLabels();
    }
    *///?}

    private TorchmasterLightScreenLayout layout()
    {
        return new TorchmasterLightScreenLayout(width, height, PANEL_WIDTH, PANEL_HEIGHT);
    }

    private int panelLeft()
    {
        return layout().panelLeft();
    }

    private int panelTop()
    {
        return layout().panelTop();
    }

    private int panelRight()
    {
        return layout().panelRight();
    }

    private int panelBottom()
    {
        return layout().panelBottom();
    }

    //? if >=1.20 {
    private void drawPanelBackground(DrawContext graphics, int mouseX, int mouseY, float partialTick)
    {
        //? if >=1.21 {
        renderInGameBackground(graphics);
        //?} else if >=1.20.6 {
        /*
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        *///?} else {
        /*
        super.renderBackground(graphics);
        *///?}
        graphics.fill(panelLeft(), panelTop(), panelRight(), panelBottom(), 0xAA101010);
    }
    //?} else if >=1.16 {
    /*private void drawPanelBackground(MatrixStack poseStack)
    {
        super.renderBackground(poseStack);
        fill(poseStack, panelLeft(), panelTop(), panelRight(), panelBottom(), 0xAA101010);
    }

    private void drawPanelFrame(MatrixStack poseStack)
    {
        fill(poseStack, panelLeft(), panelTop(), panelRight(), panelTop() + 1, 0xFF404040);
        fill(poseStack, panelLeft(), panelTop(), panelLeft() + 1, panelBottom(), 0xFF404040);
        fill(poseStack, panelLeft(), panelBottom() - 1, panelRight(), panelBottom(), 0xFF202020);
        fill(poseStack, panelRight() - 1, panelTop(), panelRight(), panelBottom(), 0xFF202020);
    }

    private void drawPanelLabels(MatrixStack poseStack)
    {
        //? if >=1.19.4 {
        drawCenteredTextWithShadow(poseStack, textRenderer, title, width / 2, panelTop() + 10, 0xFFFFFFFF);
        drawCenteredTextWithShadow(poseStack, textRenderer, blockName(), width / 2, panelTop() + 28, 0xFFE0E0E0);
        drawCenteredTextWithShadow(poseStack, textRenderer, text(TorchmasterLightScreenModel.RANGE_KEY, model.radius()), width / 2, panelTop() + 42, 0xFFA0FFA0);
        //?} else {
        /^drawCenteredText(poseStack, textRenderer, title, width / 2, panelTop() + 10, 0xFFFFFFFF);
        drawCenteredText(poseStack, textRenderer, blockName(), width / 2, panelTop() + 28, 0xFFE0E0E0);
        drawCenteredText(poseStack, textRenderer, text(TorchmasterLightScreenModel.RANGE_KEY, model.radius()), width / 2, panelTop() + 42, 0xFFA0FFA0);
        ^///?}
    }
    *///?} else {
    /*private void drawPanelBackground()
    {
        renderBackground();
        fill(panelLeft(), panelTop(), panelRight(), panelBottom(), 0xAA101010);
    }

    private void drawPanelFrame()
    {
        fill(panelLeft(), panelTop(), panelRight(), panelTop() + 1, 0xFF404040);
        fill(panelLeft(), panelTop(), panelLeft() + 1, panelBottom(), 0xFF404040);
        fill(panelLeft(), panelBottom() - 1, panelRight(), panelBottom(), 0xFF202020);
        fill(panelRight() - 1, panelTop(), panelRight(), panelBottom(), 0xFF202020);
    }

    private void drawPanelLabels()
    {
        drawCenteredString(font, text(TorchmasterLightScreenModel.TITLE_KEY), width / 2, panelTop() + 10, 0xFFFFFFFF);
        drawCenteredString(font, blockName(), width / 2, panelTop() + 28, 0xFFE0E0E0);
        drawCenteredString(font, text(TorchmasterLightScreenModel.RANGE_KEY, model.radius()), width / 2, panelTop() + 42, 0xFFA0FFA0);
    }
    *///?}

    //? if >=1.20 {
    private void drawPanelFrame(DrawContext graphics)
    {
        graphics.fill(panelLeft(), panelTop(), panelRight(), panelTop() + 1, 0xFF404040);
        graphics.fill(panelLeft(), panelTop(), panelLeft() + 1, panelBottom(), 0xFF404040);
        graphics.fill(panelLeft(), panelBottom() - 1, panelRight(), panelBottom(), 0xFF202020);
        graphics.fill(panelRight() - 1, panelTop(), panelRight(), panelBottom(), 0xFF202020);
    }

    private void drawPanelLabels(DrawContext graphics)
    {
        graphics.drawCenteredTextWithShadow(textRenderer, title, width / 2, panelTop() + 10, 0xFFFFFFFF);
        graphics.drawCenteredTextWithShadow(textRenderer, blockName(), width / 2, panelTop() + 28, 0xFFE0E0E0);
        graphics.drawCenteredTextWithShadow(textRenderer, text(TorchmasterLightScreenModel.RANGE_KEY, model.radius()), width / 2, panelTop() + 42, 0xFFA0FFA0);
    }
    //?}

    //? if >=1.16
    private Text blockName()
    //? if <1.16
    //private String blockName()
    {
        return text(model.blockKey());
    }

    private ButtonWidget addCompatWidget(ButtonWidget widget)
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

    //? if >=1.16
    private static Text text(String translationKey, Object value)
    //? if <1.16
    //private static String text(String translationKey, Object value)
    {
        //? if >=1.16 {
        return MinecraftText.translatable(translationKey, value);
        //?} else {
        /*return I18n.translate(translationKey, value);
        *///?}
    }

    //? if >=1.16
    private static Text literal(String value)
    //? if <1.16
    //private static String literal(String value)
    {
        //? if >=1.16 {
        return MinecraftText.literal(value);
        //?} else {
        /*return value;
        *///?}
    }
}
