package net.xalcon.torchmaster.client;

//? if >=1.20
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
//? if >=1.16 && <1.20
//import net.minecraft.client.util.math.MatrixStack;
//? if >=1.19.4
import net.minecraft.registry.RegistryKey;
//? if >=1.16.5 && <1.19.4
//import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.blocks.LightType;

public class TorchmasterLightScreen extends TorchmasterScreenCompat
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
        super(text(TorchmasterLightScreenModel.TITLE_KEY));
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
        open(new TorchmasterLightScreen(currentScreen(), pos, dimension, lightType));
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
        visibilityButton.setMessage(visibilityLabel().asWidget());
    }

    private void openConfigScreen()
    {
        openChildScreen(new TorchmasterConfigScreen(this));
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

    private CompatText visibilityLabel()
    {
        return text(model.visibilityButtonKey(TorchmasterLightRangeDisplay.isVisible(dimension, pos)));
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
        fillPanel(graphics, TorchmasterPanelRenderer.background(panelLeft(), panelTop(), panelRight(), panelBottom()));
    }
    //?} else if >=1.16 {
    /*private void drawPanelBackground(MatrixStack poseStack)
    {
        super.renderBackground(poseStack);
        fillPanel(poseStack, TorchmasterPanelRenderer.background(panelLeft(), panelTop(), panelRight(), panelBottom()));
    }

    private void drawPanelFrame(MatrixStack poseStack)
    {
        for (TorchmasterPanelRenderer.Fill fill : TorchmasterPanelRenderer.frame(panelLeft(), panelTop(), panelRight(), panelBottom())) {
            fillPanel(poseStack, fill);
        }
    }

    private void drawPanelLabels(MatrixStack poseStack)
    {
        //? if >=1.19.4 {
        drawCenteredTextWithShadow(poseStack, textRenderer, title, width / 2, panelTop() + 10, TorchmasterPanelRenderer.TITLE_COLOR);
        drawCenteredTextWithShadow(poseStack, textRenderer, blockName().asWidget(), width / 2, panelTop() + 28, TorchmasterPanelRenderer.LABEL_COLOR);
        drawCenteredTextWithShadow(poseStack, textRenderer, text(TorchmasterLightScreenModel.RANGE_KEY, model.radius()).asWidget(), width / 2, panelTop() + 42, TorchmasterPanelRenderer.RANGE_COLOR);
        //?} else {
        /^drawCenteredText(poseStack, textRenderer, title, width / 2, panelTop() + 10, TorchmasterPanelRenderer.TITLE_COLOR);
        drawCenteredText(poseStack, textRenderer, blockName().asWidget(), width / 2, panelTop() + 28, TorchmasterPanelRenderer.LABEL_COLOR);
        drawCenteredText(poseStack, textRenderer, text(TorchmasterLightScreenModel.RANGE_KEY, model.radius()).asWidget(), width / 2, panelTop() + 42, TorchmasterPanelRenderer.RANGE_COLOR);
        ^///?}
    }

    private static void fillPanel(MatrixStack poseStack, TorchmasterPanelRenderer.Fill fill)
    {
        fill(poseStack, fill.left, fill.top, fill.right, fill.bottom, fill.color);
    }
    *///?} else {
    /*private void drawPanelBackground()
    {
        renderBackground();
        fillPanel(TorchmasterPanelRenderer.background(panelLeft(), panelTop(), panelRight(), panelBottom()));
    }

    private void drawPanelFrame()
    {
        for (TorchmasterPanelRenderer.Fill fill : TorchmasterPanelRenderer.frame(panelLeft(), panelTop(), panelRight(), panelBottom())) {
            fillPanel(fill);
        }
    }

    private void drawPanelLabels()
    {
        drawCenteredString(font, text(TorchmasterLightScreenModel.TITLE_KEY).asWidget(), width / 2, panelTop() + 10, TorchmasterPanelRenderer.TITLE_COLOR);
        drawCenteredString(font, blockName().asWidget(), width / 2, panelTop() + 28, TorchmasterPanelRenderer.LABEL_COLOR);
        drawCenteredString(font, text(TorchmasterLightScreenModel.RANGE_KEY, model.radius()).asWidget(), width / 2, panelTop() + 42, TorchmasterPanelRenderer.RANGE_COLOR);
    }

    private static void fillPanel(TorchmasterPanelRenderer.Fill fill)
    {
        fill(fill.left, fill.top, fill.right, fill.bottom, fill.color);
    }
    *///?}

    //? if >=1.20 {
    private void drawPanelFrame(DrawContext graphics)
    {
        for (TorchmasterPanelRenderer.Fill fill : TorchmasterPanelRenderer.frame(panelLeft(), panelTop(), panelRight(), panelBottom())) {
            fillPanel(graphics, fill);
        }
    }

    private void drawPanelLabels(DrawContext graphics)
    {
        graphics.drawCenteredTextWithShadow(textRenderer, title, width / 2, panelTop() + 10, TorchmasterPanelRenderer.TITLE_COLOR);
        graphics.drawCenteredTextWithShadow(textRenderer, blockName().asWidget(), width / 2, panelTop() + 28, TorchmasterPanelRenderer.LABEL_COLOR);
        graphics.drawCenteredTextWithShadow(textRenderer, text(TorchmasterLightScreenModel.RANGE_KEY, model.radius()).asWidget(), width / 2, panelTop() + 42, TorchmasterPanelRenderer.RANGE_COLOR);
    }

    private static void fillPanel(DrawContext graphics, TorchmasterPanelRenderer.Fill fill)
    {
        graphics.fill(fill.left, fill.top, fill.right, fill.bottom, fill.color);
    }
    //?}

    private CompatText blockName()
    {
        return text(model.blockKey());
    }

}
