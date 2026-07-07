package net.xalcon.torchmaster.client;

//? if >=1.20
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
//? if >=1.21.11
//import net.minecraft.client.gui.Click;
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
    private static final int PANEL_HEIGHT = 246;
    private static final int BUTTON_HEIGHT = 20;
    private static final int SETTINGS_BUTTON_SIZE = 20;
    private static final int SCROLL_STEP = 18;
    private static final int RANGE_HOLD_INITIAL_DELAY_TICKS = 8;
    private static final int RANGE_HOLD_FAST_DELAY_TICKS = 20;
    private static final int RANGE_HOLD_SLOW_INTERVAL_TICKS = 3;
    private static TorchmasterLightScreen activeScreen;

    private final Screen parent;
    private final BlockPos pos;
    //? if >=1.16.5
    private final RegistryKey<World> dimension;
    //? if <1.16.5
    //private final Object dimension;
    private final TorchmasterLightScreenModel model;
    private final TorchmasterLightScreenController controller;
    private ButtonWidget settingsButton;
    private ButtonWidget resetButton;
    private ButtonWidget doneButton;
    private ButtonWidget accessButton;
    private ButtonWidget rangeWestMinusButton;
    private ButtonWidget rangeWestPlusButton;
    private ButtonWidget rangeEastMinusButton;
    private ButtonWidget rangeEastPlusButton;
    private ButtonWidget rangeDownMinusButton;
    private ButtonWidget rangeDownPlusButton;
    private ButtonWidget rangeUpMinusButton;
    private ButtonWidget rangeUpPlusButton;
    private ButtonWidget rangeNorthMinusButton;
    private ButtonWidget rangeNorthPlusButton;
    private ButtonWidget rangeSouthMinusButton;
    private ButtonWidget rangeSouthPlusButton;
    private TorchmasterLightAccessScreen accessScreen;
    private int scrollOffset;
    private RangeHoldState rangeHoldState;

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
        settingsButton = button(layout.settingsButtonX(), layout.settingsButtonY(), SETTINGS_BUTTON_SIZE, SETTINGS_BUTTON_SIZE, literal("\u2699"),
                ignored -> openConfigScreen());
        addCompatWidget(settingsButton);
        accessButton = button(layout.accessButtonX(), layout.accessButtonY(), layout.accessButtonWidth(), BUTTON_HEIGHT,
                text("screen.torchmaster.light.accessShort"), ignored -> openAccessScreen());
        addCompatWidget(accessButton);
        rangeWestMinusButton = addRangeButton(layout, RangeDirection.WEST, false);
        rangeWestPlusButton = addRangeButton(layout, RangeDirection.WEST, true);
        rangeEastMinusButton = addRangeButton(layout, RangeDirection.EAST, false);
        rangeEastPlusButton = addRangeButton(layout, RangeDirection.EAST, true);
        rangeDownMinusButton = addRangeButton(layout, RangeDirection.DOWN, false);
        rangeDownPlusButton = addRangeButton(layout, RangeDirection.DOWN, true);
        rangeUpMinusButton = addRangeButton(layout, RangeDirection.UP, false);
        rangeUpPlusButton = addRangeButton(layout, RangeDirection.UP, true);
        rangeNorthMinusButton = addRangeButton(layout, RangeDirection.NORTH, false);
        rangeNorthPlusButton = addRangeButton(layout, RangeDirection.NORTH, true);
        rangeSouthMinusButton = addRangeButton(layout, RangeDirection.SOUTH, false);
        rangeSouthPlusButton = addRangeButton(layout, RangeDirection.SOUTH, true);
        resetButton = button(layout.footerButtonX(1), layout.footerY(), layout.footerButtonWidth(), BUTTON_HEIGHT, text("screen.torchmaster.light.reset"), ignored -> resetSettings());
        doneButton = button(layout.footerButtonX(2), layout.footerY(), layout.footerButtonWidth(), BUTTON_HEIGHT, text("gui.done"), ignored -> closeScreen());
        addCompatWidget(resetButton);
        addCompatWidget(doneButton);
        applyLayout();
        updateEditableState();
    }

    private void toggleVisibility()
    {
        controller.toggleVisibility();
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

    private ButtonWidget addRangeButton(TorchmasterLightScreenLayout layout, RangeDirection direction, boolean increment)
    {
        int column = directionSlot(direction) % 2;
        int row = directionSlot(direction) / 2;
        int x = increment ? layout.rangePlusX(column) : layout.rangeMinusX(column);
        ButtonWidget button = button(x, layout.radiusFieldY(row), layout.rangeButtonWidth(), BUTTON_HEIGHT, literal(increment ? "+" : "-"),
                ignored -> changeRange(direction, increment ? 1 : -1));
        return addCompatWidget(button);
    }

    private void toggleEnabled()
    {
        if (!settings().editable()) {
            return;
        }
        applyImmediate(!settings().enabled(), settings().rangeWest(), settings().rangeEast(), settings().rangeDown(), settings().rangeUp(),
                settings().rangeNorth(), settings().rangeSouth());
    }

    private void changeRange(RangeDirection direction, int delta)
    {
        if (!settings().editable()) {
            stopRangeHold();
            return;
        }
        int rangeWest = settings().rangeWest();
        int rangeEast = settings().rangeEast();
        int rangeDown = settings().rangeDown();
        int rangeUp = settings().rangeUp();
        int rangeNorth = settings().rangeNorth();
        int rangeSouth = settings().rangeSouth();
        int next = adjustedRange(currentRange(direction), delta);
        switch (direction) {
            case WEST:
                rangeWest = next;
                break;
            case EAST:
                rangeEast = next;
                break;
            case DOWN:
                rangeDown = next;
                break;
            case UP:
                rangeUp = next;
                break;
            case NORTH:
                rangeNorth = next;
                break;
            case SOUTH:
                rangeSouth = next;
                break;
        }
        applyImmediate(settings().enabled(), rangeWest, rangeEast, rangeDown, rangeUp, rangeNorth, rangeSouth);
    }

    private void beginRangeHold(RangeDirection direction, int delta)
    {
        rangeHoldState = new RangeHoldState(direction, delta);
    }

    private void stopRangeHold()
    {
        rangeHoldState = null;
    }

    private void tickRangeHold()
    {
        if (rangeHoldState == null) {
            return;
        }
        if (!canChangeRange(rangeHoldState.direction, rangeHoldState.delta)) {
            stopRangeHold();
            return;
        }
        rangeHoldState.ticks++;
        int repeatTicks = rangeHoldState.ticks - RANGE_HOLD_INITIAL_DELAY_TICKS;
        if (repeatTicks < 0) {
            return;
        }
        int interval = repeatTicks >= RANGE_HOLD_FAST_DELAY_TICKS ? 1 : RANGE_HOLD_SLOW_INTERVAL_TICKS;
        if (repeatTicks % interval == 0) {
            changeRange(rangeHoldState.direction, rangeHoldState.delta);
        }
    }

    private void resetSettings()
    {
        if (!settings().editable()) {
            return;
        }
        applyImmediate(true, settings().globalMax(), settings().globalMax(), settings().globalMax(), settings().globalMax(),
                settings().globalMax(), settings().globalMax());
    }

    private void applyImmediate(boolean enabled, int rangeWest, int rangeEast, int rangeDown, int rangeUp, int rangeNorth, int rangeSouth)
    {
        controller.setDraft(enabled, rangeWest, rangeEast, rangeDown, rangeUp, rangeNorth, rangeSouth);
        controller.applySettings(LightSettings.configured(enabled, rangeWest, rangeEast, rangeDown, rangeUp, rangeNorth, rangeSouth));
        syncControls();
        controller.refreshVisibleRange();
    }

    private void syncControls()
    {
        updateEditableState();
    }

    private void updateEditableState()
    {
        applyLayout();
        boolean editable = settings().editable();
        resetButton.active = editable;
        accessButton.active = settings().found();
        updateRangeButtonState(RangeDirection.WEST, rangeWestMinusButton, rangeWestPlusButton, editable, true);
        updateRangeButtonState(RangeDirection.EAST, rangeEastMinusButton, rangeEastPlusButton, editable, true);
        updateRangeButtonState(RangeDirection.DOWN, rangeDownMinusButton, rangeDownPlusButton, editable, !settings().chunkAligned());
        updateRangeButtonState(RangeDirection.UP, rangeUpMinusButton, rangeUpPlusButton, editable, !settings().chunkAligned());
        updateRangeButtonState(RangeDirection.NORTH, rangeNorthMinusButton, rangeNorthPlusButton, editable, true);
        updateRangeButtonState(RangeDirection.SOUTH, rangeSouthMinusButton, rangeSouthPlusButton, editable, true);
    }

    private void applyLayout()
    {
        TorchmasterLightScreenLayout layout = layout();
        if (settingsButton != null) {
            setWidgetX(settingsButton, layout.settingsButtonX());
            setWidgetY(settingsButton, layout.settingsButtonY());
        }
        if (accessButton != null) {
            setWidgetX(accessButton, layout.accessButtonX());
            setWidgetY(accessButton, layout.accessButtonY());
        }
        if (rangeWestMinusButton != null) {
            positionRangeButtons(layout, RangeDirection.WEST, rangeWestMinusButton, rangeWestPlusButton);
            positionRangeButtons(layout, RangeDirection.EAST, rangeEastMinusButton, rangeEastPlusButton);
            positionRangeButtons(layout, RangeDirection.DOWN, rangeDownMinusButton, rangeDownPlusButton);
            positionRangeButtons(layout, RangeDirection.UP, rangeUpMinusButton, rangeUpPlusButton);
            positionRangeButtons(layout, RangeDirection.NORTH, rangeNorthMinusButton, rangeNorthPlusButton);
            positionRangeButtons(layout, RangeDirection.SOUTH, rangeSouthMinusButton, rangeSouthPlusButton);
        }
        if (resetButton != null) {
            setWidgetX(resetButton, layout.footerButtonX(1));
            setWidgetY(resetButton, layout.footerY());
        }
        if (doneButton != null) {
            setWidgetX(doneButton, layout.footerButtonX(2));
            setWidgetY(doneButton, layout.footerY());
        }
    }

    private void positionRangeButtons(TorchmasterLightScreenLayout layout, RangeDirection direction, ButtonWidget minusButton,
            ButtonWidget plusButton)
    {
        int slot = directionSlot(direction);
        int column = slot % 2;
        int row = slot / 2;
        TorchmasterConfigWidgetAdapter.position(minusButton, layout.rangeMinusX(column), layout.radiusFieldY(row));
        TorchmasterConfigWidgetAdapter.position(plusButton, layout.rangePlusX(column), layout.radiusFieldY(row));
    }

    private void updateRangeButtonState(RangeDirection direction, ButtonWidget minusButton, ButtonWidget plusButton, boolean editable,
            boolean visible)
    {
        TorchmasterConfigWidgetAdapter.visible(minusButton, visible);
        TorchmasterConfigWidgetAdapter.visible(plusButton, visible);
        int value = currentRange(direction);
        minusButton.active = visible && editable && adjustedRange(value, -1) != value;
        plusButton.active = visible && editable && adjustedRange(value, 1) != value;
    }

    private boolean canChangeRange(RangeDirection direction, int delta)
    {
        return settings().editable() && isRangeDirectionVisible(direction) && adjustedRange(currentRange(direction), delta) != currentRange(direction);
    }

    private boolean isRangeDirectionVisible(RangeDirection direction)
    {
        return !settings().chunkAligned() || direction != RangeDirection.DOWN && direction != RangeDirection.UP;
    }

    private int adjustedRange(int current, int delta)
    {
        int displayValue = displayRadius(current, settings().chunkAligned()) + delta;
        return readRadiusValue(Integer.toString(displayValue), current, settings().globalMax(), settings().chunkAligned());
    }

    private int currentRange(RangeDirection direction)
    {
        switch (direction) {
            case WEST:
                return settings().rangeWest();
            case EAST:
                return settings().rangeEast();
            case DOWN:
                return settings().rangeDown();
            case UP:
                return settings().rangeUp();
            case NORTH:
                return settings().rangeNorth();
            case SOUTH:
                return settings().rangeSouth();
            default:
                return 0;
        }
    }

    private int directionSlot(RangeDirection direction)
    {
        if (settings().chunkAligned()) {
            switch (direction) {
                case NORTH:
                    return 2;
                case SOUTH:
                    return 3;
                default:
                    break;
            }
        }
        switch (direction) {
            case WEST:
                return 0;
            case EAST:
                return 1;
            case DOWN:
                return 2;
            case UP:
                return 3;
            case NORTH:
                return 4;
            case SOUTH:
                return 5;
            default:
                return 0;
        }
    }

    LightType lightType()
    {
        return model.lightType();
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
            if (activeScreen.rangeWestMinusButton != null) {
                activeScreen.syncControls();
                activeScreen.updateEditableState();
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
        clampScroll();
        applyLayout();
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
        return new TorchmasterLightScreenLayout(width, height, PANEL_WIDTH, PANEL_HEIGHT, scrollOffset);
    }

    @Override
    public void tick()
    {
        super.tick();
        tickRangeHold();
    }

    private boolean scroll(double delta)
    {
        TorchmasterLightScreenLayout unscrolledLayout = new TorchmasterLightScreenLayout(width, height, PANEL_WIDTH, PANEL_HEIGHT);
        int max = unscrolledLayout.maxScroll();
        if (max <= 0) {
            return false;
        }
        int previous = scrollOffset;
        scrollOffset = Math.max(0, Math.min(max, scrollOffset - (int)Math.signum(delta) * SCROLL_STEP));
        if (previous == scrollOffset) {
            return false;
        }
        applyLayout();
        return true;
    }

    private void clampScroll()
    {
        int max = new TorchmasterLightScreenLayout(width, height, PANEL_WIDTH, PANEL_HEIGHT).maxScroll();
        scrollOffset = Math.max(0, Math.min(scrollOffset, max));
    }

    //? if >=1.20.6 {
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        return scroll(scrollY) || super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
    //?} else {
    /*@Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        return scroll(delta) || super.mouseScrolled(mouseX, mouseY, delta);
    }
    *///?}

    //? if >=1.21.11 {
    /*@Override
    public boolean mouseClicked(Click click, boolean doubleClick)
    {
        if (handleSwitchClick(click.x(), click.y())) {
            return true;
        }
        RangeButtonAction rangeAction = rangeButtonActionAt(click.x(), click.y());
        boolean handled = super.mouseClicked(click, doubleClick);
        if (handled && rangeAction != null) {
            beginRangeHold(rangeAction.direction, rangeAction.delta);
        }
        return handled;
    }

    @Override
    public boolean mouseReleased(Click click)
    {
        stopRangeHold();
        return super.mouseReleased(click);
    }
    *///?} else {
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (handleSwitchClick(mouseX, mouseY)) {
            return true;
        }
        RangeButtonAction rangeAction = button == 0 ? rangeButtonActionAt(mouseX, mouseY) : null;
        boolean handled = super.mouseClicked(mouseX, mouseY, button);
        if (handled && rangeAction != null) {
            beginRangeHold(rangeAction.direction, rangeAction.delta);
        }
        return handled;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        stopRangeHold();
        return super.mouseReleased(mouseX, mouseY, button);
    }
    //?}

    private boolean handleSwitchClick(double mouseX, double mouseY)
    {
        TorchmasterLightScreenLayout layout = layout();
        if (settings().editable() && TorchmasterSwitchControl.contains(layout.enabledSwitchX(), layout.enabledSwitchY(), mouseX, mouseY)) {
            toggleEnabled();
            return true;
        }
        if (TorchmasterSwitchControl.contains(layout.visibilitySwitchX(), layout.visibilityY(), mouseX, mouseY)) {
            toggleVisibility();
            return true;
        }
        return false;
    }

    private RangeButtonAction rangeButtonActionAt(double mouseX, double mouseY)
    {
        RangeButtonAction action = rangeButtonActionAt(layout(), RangeDirection.WEST, mouseX, mouseY);
        if (action != null) {
            return action;
        }
        action = rangeButtonActionAt(layout(), RangeDirection.EAST, mouseX, mouseY);
        if (action != null) {
            return action;
        }
        action = rangeButtonActionAt(layout(), RangeDirection.DOWN, mouseX, mouseY);
        if (action != null) {
            return action;
        }
        action = rangeButtonActionAt(layout(), RangeDirection.UP, mouseX, mouseY);
        if (action != null) {
            return action;
        }
        action = rangeButtonActionAt(layout(), RangeDirection.NORTH, mouseX, mouseY);
        if (action != null) {
            return action;
        }
        return rangeButtonActionAt(layout(), RangeDirection.SOUTH, mouseX, mouseY);
    }

    private RangeButtonAction rangeButtonActionAt(TorchmasterLightScreenLayout layout, RangeDirection direction, double mouseX, double mouseY)
    {
        if (!isRangeDirectionVisible(direction)) {
            return null;
        }
        int slot = directionSlot(direction);
        int column = slot % 2;
        int row = slot / 2;
        if (contains(layout.rangeMinusX(column), layout.radiusFieldY(row), layout.rangeButtonWidth(), BUTTON_HEIGHT, mouseX, mouseY)
                && canChangeRange(direction, -1)) {
            return new RangeButtonAction(direction, -1);
        }
        if (contains(layout.rangePlusX(column), layout.radiusFieldY(row), layout.rangeButtonWidth(), BUTTON_HEIGHT, mouseX, mouseY)
                && canChangeRange(direction, 1)) {
            return new RangeButtonAction(direction, 1);
        }
        return null;
    }

    private static boolean contains(int x, int y, int width, int height, double mouseX, double mouseY)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
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
        
        /*super.renderBackground(graphics);
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

    private enum RangeDirection
    {
        WEST,
        EAST,
        DOWN,
        UP,
        NORTH,
        SOUTH
    }

    private static final class RangeButtonAction
    {
        private final RangeDirection direction;
        private final int delta;

        private RangeButtonAction(RangeDirection direction, int delta)
        {
            this.direction = direction;
            this.delta = delta;
        }
    }

    private static final class RangeHoldState
    {
        private final RangeDirection direction;
        private final int delta;
        private int ticks;

        private RangeHoldState(RangeDirection direction, int delta)
        {
            this.direction = direction;
            this.delta = delta;
        }
    }
}
