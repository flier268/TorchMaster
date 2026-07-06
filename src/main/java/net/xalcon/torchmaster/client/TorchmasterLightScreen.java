package net.xalcon.torchmaster.client;

//? if >=1.20
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.MinecraftClient;
//? if >=1.16 && <1.20
//import net.minecraft.client.util.math.MatrixStack;
//? if >=1.19.4
import net.minecraft.registry.RegistryKey;
//? if >=1.16.5 && <1.19.4
//import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
//? if <1.16.5
//import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.domain.LightSettings;
import net.xalcon.torchmaster.port.LightAccessEntry;
import net.xalcon.torchmaster.port.LightSettingsView;

public class TorchmasterLightScreen extends TorchmasterScreenCompat
{
    private static final int PANEL_WIDTH = 340;
    private static final int PANEL_HEIGHT = 248;
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;
    private static final int SETTINGS_BUTTON_SIZE = 20;
    private static final int FIELD_WIDTH = 64;
    private static TorchmasterLightScreen activeScreen;

    private final Screen parent;
    private final BlockPos pos;
    //? if >=1.16.5
    private final RegistryKey<World> dimension;
    //? if <1.16.5
    //private final Object dimension;
    private final TorchmasterLightScreenModel model;
    private final TorchmasterLightScreenController controller;
    private ButtonWidget visibilityButton;
    private ButtonWidget enabledButton;
    private ButtonWidget applyButton;
    private ButtonWidget resetButton;
    private TextFieldWidget radiusXField;
    private TextFieldWidget radiusYField;
    private TextFieldWidget radiusZField;
    private ButtonWidget accessButton;
    private TorchmasterLightAccessScreen accessScreen;

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
        this.controller = new TorchmasterLightScreenController(this.pos, dimension, lightType);
        this.controller.queryRemoteIfNeeded();
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
        activeScreen = this;
        TorchmasterLightScreenLayout layout = layout();
        addCompatWidget(button(layout.settingsButtonX(), layout.settingsButtonY(), SETTINGS_BUTTON_SIZE, SETTINGS_BUTTON_SIZE, literal("\u2699"),
                ignored -> openConfigScreen()));
        visibilityButton = button(layout.centerX() - BUTTON_WIDTH / 2, layout.visibilityY(), BUTTON_WIDTH, BUTTON_HEIGHT, visibilityLabel(),
                ignored -> toggleVisibility());
        addCompatWidget(visibilityButton);
        enabledButton = button(layout.enabledButtonX(), layout.enabledY(), layout.enabledButtonWidth(), BUTTON_HEIGHT, enabledLabel(),
                ignored -> toggleEnabled());
        addCompatWidget(enabledButton);
        radiusXField = addRadiusField(layout.radiusFieldX(0), layout.radiusFieldY(), displayRadius(settings().radiusX()));
        radiusYField = addRadiusField(layout.radiusFieldX(1), layout.radiusFieldY(), displayRadius(settings().radiusY()));
        radiusZField = addRadiusField(layout.radiusFieldX(settings().chunkAligned() ? 1 : 2), layout.radiusFieldY(), displayRadius(settings().radiusZ()));
        accessButton = button(layout.accessButtonX(), layout.accessButtonY(), layout.accessButtonWidth(), BUTTON_HEIGHT, text("screen.torchmaster.light.accessSettings"),
                ignored -> openAccessScreen());
        addCompatWidget(accessButton);
        applyButton = button(layout.footerButtonX(0), layout.footerY(), layout.footerButtonWidth(), BUTTON_HEIGHT, text("screen.torchmaster.light.apply"), ignored -> applySettings());
        resetButton = button(layout.footerButtonX(1), layout.footerY(), layout.footerButtonWidth(), BUTTON_HEIGHT, text("screen.torchmaster.light.reset"), ignored -> resetSettings());
        addCompatWidget(applyButton);
        addCompatWidget(resetButton);
        addCompatWidget(button(layout.footerButtonX(2), layout.footerY(), layout.footerButtonWidth(), BUTTON_HEIGHT, text("gui.done"), ignored -> closeScreen()));
        updateEditableState();
    }

    private void toggleVisibility()
    {
        controller.toggleVisibility();
        visibilityButton.setMessage(visibilityLabel().asWidget());
    }

    private void openConfigScreen()
    {
        openChildScreen(new TorchmasterConfigScreen(this));
    }

    private void openAccessScreen()
    {
        accessScreen = new TorchmasterLightAccessScreen(this);
        openChildScreen(accessScreen);
    }

    private TextFieldWidget addRadiusField(int x, int y, int value)
    {
        TextFieldWidget field = textField(x, y, FIELD_WIDTH, BUTTON_HEIGHT, "screen.torchmaster.light.radius");
        TorchmasterConfigWidgetAdapter.configureInteger(field, value);
        return addCompatWidget(field);
    }

    private void toggleEnabled()
    {
        if (!settings().editable()) {
            return;
        }
        controller.setDraft(!settings().enabled(), readRadius(radiusXField, settings().radiusX()),
                readRadius(radiusYField, settings().radiusY()), readRadius(radiusZField, settings().radiusZ()));
        enabledButton.setMessage(enabledLabel().asWidget());
    }

    private void applySettings()
    {
        if (!settings().editable()) {
            return;
        }
        LightSettings requested = LightSettings.configured(settings().enabled(),
                readRadius(radiusXField, settings().radiusX()),
                settings().chunkAligned() ? settings().radiusY() : readRadius(radiusYField, settings().radiusY()),
                readRadius(radiusZField, settings().radiusZ()));
        if (controller.applySettings(requested)) {
            syncFields();
        }
    }

    private void resetSettings()
    {
        if (!settings().editable()) {
            return;
        }
        radiusXField.setText(Integer.toString(displayRadius(settings().globalMax())));
        radiusYField.setText(Integer.toString(displayRadius(settings().globalMax())));
        radiusZField.setText(Integer.toString(displayRadius(settings().globalMax())));
        controller.resetDraft();
        enabledButton.setMessage(enabledLabel().asWidget());
        controller.refreshVisibleRange();
    }

    private void syncFields()
    {
        radiusXField.setText(Integer.toString(displayRadius(settings().radiusX())));
        radiusYField.setText(Integer.toString(displayRadius(settings().radiusY())));
        radiusZField.setText(Integer.toString(displayRadius(settings().radiusZ())));
        enabledButton.setMessage(enabledLabel().asWidget());
        controller.refreshVisibleRange();
    }

    private void updateEditableState()
    {
        boolean editable = settings().editable();
        enabledButton.active = editable;
        applyButton.active = editable;
        resetButton.active = editable;
        radiusXField.active = editable;
        radiusYField.active = editable && !settings().chunkAligned();
        radiusZField.active = editable;
        TorchmasterConfigWidgetAdapter.visible(radiusYField, !settings().chunkAligned());
        TorchmasterConfigWidgetAdapter.position(radiusZField, layout().radiusFieldX(settings().chunkAligned() ? 1 : 2), layout().radiusFieldY());
        accessButton.active = settings().found();
    }

    LightType lightType()
    {
        return model.lightType();
    }

    private int readRadius(TextFieldWidget field, int fallback)
    {
        return readRadiusValue(field.getText(), fallback, settings().globalMax(), settings().chunkAligned());
    }

    private int displayRadius(int blockRadius)
    {
        return displayRadius(blockRadius, settings().chunkAligned());
    }

    static int readRadiusValue(String text, int fallback, int globalMax, boolean chunkAligned)
    {
        try {
            int value = Math.max(0, Integer.parseInt(text.trim()));
            if (chunkAligned) {
                return Math.min(chunksToBlocks(value), globalMax);
            }
            return Math.min(value, globalMax);
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    static int displayRadius(int blockRadius, boolean chunkAligned)
    {
        if (chunkAligned) {
            return chunkRadius(blockRadius);
        }
        return blockRadius;
    }

    private static int chunksToBlocks(int chunks)
    {
        return Math.max(0, chunks) * 16;
    }

    private static int chunkRadius(int radius)
    {
        return (Math.max(0, radius) + 15) >> 4;
    }

    LightSettingsView settings()
    {
        return controller.settings();
    }

    BlockPos pos()
    {
        return pos;
    }

    void clearAccessScreen(TorchmasterLightAccessScreen screen)
    {
        if (accessScreen == screen) {
            accessScreen = null;
        }
    }

    public static void receiveSnapshot(BlockPos pos, LightType lightType, LightSettingsView snapshot)
    {
        MinecraftClient.getInstance().execute(() -> {
            TorchmasterLightRangeDisplay.applyCurrentWorld(pos, lightType, snapshot);
            if (activeScreen == null || !snapshot.appliesToSettings()) {
                return;
            }
            if (!activeScreen.controller.applySnapshot(pos, lightType, snapshot)) {
                return;
            }
            if (activeScreen.radiusXField != null) {
                activeScreen.syncFields();
                activeScreen.updateEditableState();
                activeScreen.visibilityButton.setMessage(activeScreen.visibilityLabel().asWidget());
                activeScreen.syncAccessScreen();
            }
        });
    }

    void addAccess(String playerName)
    {
        if (!settings().accessManageable()) {
            return;
        }
        if (controller.addAccess(playerName)) {
            updateEditableState();
            syncAccessScreen();
        }
    }

    void removeAccess(int index)
    {
        LightAccessEntry[] accessEntries = settings().accessEntries();
        if (!settings().accessManageable() || index < 0 || index >= accessEntries.length) {
            return;
        }
        if (controller.removeAccess(index)) {
            updateEditableState();
            syncAccessScreen();
        }
    }

    private void syncAccessScreen()
    {
        if (accessScreen != null) {
            accessScreen.syncFromParent();
        }
    }

    private void closeScreen()
    {
        if (activeScreen == this) {
            activeScreen = null;
        }
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
        return text(model.visibilityButtonKey(settings().rangeVisible()));
    }

    private CompatText enabledLabel()
    {
        return text(settings().enabled() ? "options.on" : "options.off");
    }

    //? if >=1.18 {
    @Override
    public void close()
    {
        if (activeScreen == this) {
            activeScreen = null;
        }
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
        TorchmasterScreenRenderAdapter.renderPlan(graphics, textRenderer, renderPlan());
    }
    //?} else if >=1.16 {
    /*@Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        drawPanelBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        TorchmasterScreenRenderAdapter.renderPlan(poseStack, textRenderer, renderPlan());
    }
    *///?} else {
    /*@Override
    public void render(int mouseX, int mouseY, float partialTick)
    {
        drawPanelBackground();
        super.render(mouseX, mouseY, partialTick);
        TorchmasterScreenRenderAdapter.renderPlan(font, renderPlan());
    }
    *///?}

    private TorchmasterLightScreenLayout layout()
    {
        return new TorchmasterLightScreenLayout(width, height, PANEL_WIDTH, PANEL_HEIGHT);
    }

    private TorchmasterScreenRenderPlan renderPlan()
    {
        return TorchmasterLightScreenPresenter.plan(layout(), model.blockKey(), model.radius(), pos, settings());
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
        TorchmasterScreenRenderAdapter.background(graphics, renderPlan());
    }
    //?} else if >=1.16 {
    /*private void drawPanelBackground(MatrixStack poseStack)
    {
        super.renderBackground(poseStack);
        TorchmasterScreenRenderAdapter.background(poseStack, renderPlan());
    }
    *///?} else {
    /*private void drawPanelBackground()
    {
        renderBackground();
        TorchmasterScreenRenderAdapter.background(renderPlan());
    }
    *///?}

}
