package net.xalcon.torchmaster.client;

//? if >=1.20
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
//? if >=1.21.11
//import net.minecraft.client.gui.Click;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
//? if >=1.21.11
//import net.minecraft.client.input.KeyInput;
//? if >=1.16 && <1.20
//import net.minecraft.client.util.math.MatrixStack;
import net.xalcon.torchmaster.port.LightAccessEntry;
import net.xalcon.torchmaster.port.LightSettingsView;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class TorchmasterLightAccessScreen extends TorchmasterScreenCompat
{
    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 286;
    private static final int BUTTON_HEIGHT = 20;
    private static final int ACCESS_FIELD_WIDTH = 176;
    private static final int REMOVE_BUTTON_WIDTH = 58;
    private static final int MAX_AUTOCOMPLETE_ROWS = 4;

    private final TorchmasterLightScreen parent;
    private TextFieldWidget accessNameField;
    private ButtonWidget addAccessButton;
    private ButtonWidget[] removeAccessButtons = new ButtonWidget[0];
    private int scrollOffset;

    TorchmasterLightAccessScreen(TorchmasterLightScreen parent)
    {
        super(text("screen.torchmaster.light.accessSettings"));
        this.parent = parent;
    }

    @Override
    protected void init()
    {
        TorchmasterLightAccessScreenLayout layout = layout();
        accessNameField = textField(layout.panelLeft() + 18, layout.accessFieldY(), ACCESS_FIELD_WIDTH, BUTTON_HEIGHT,
                "screen.torchmaster.light.accessPlayer");
        addCompatWidget(accessNameField);
        addAccessButton = button(layout.panelLeft() + 204, layout.accessFieldY(), 78, BUTTON_HEIGHT,
                text("screen.torchmaster.light.accessAdd"), ignored -> addAccess());
        addCompatWidget(addAccessButton);
        removeAccessButtons = new ButtonWidget[layout.visibleRows()];
        for (int i = 0; i < removeAccessButtons.length; i++) {
            final int row = i;
            removeAccessButtons[i] = button(layout.panelLeft() + 224, layout.listY() + i * layout.rowHeight(), REMOVE_BUTTON_WIDTH, 16,
                    text("screen.torchmaster.light.accessRemove"), ignored -> removeAccess(row));
            addCompatWidget(removeAccessButtons[i]);
        }
        addCompatWidget(button(layout.panelLeft() + 198, layout.footerY(), 84, BUTTON_HEIGHT, text("gui.done"), ignored -> closeScreen()));
        syncFromParent();
    }

    void syncFromParent()
    {
        clampScroll();
        updateControls();
    }

    private void addAccess()
    {
        if (accessNameField == null) {
            return;
        }
        parent.addAccess(accessNameField.getText());
        accessNameField.setText("");
        syncFromParent();
    }

    private void removeAccess(int row)
    {
        parent.removeAccess(scrollOffset + row);
        syncFromParent();
    }

    private void updateControls()
    {
        if (accessNameField == null) {
            return;
        }
        LightSettingsView settings = parent.settings();
        boolean accessManageable = settings.accessManageable();
        accessNameField.active = accessManageable;
        addAccessButton.active = accessManageable;
        LightAccessEntry[] accessEntries = settings.accessEntries();
        for (int i = 0; i < removeAccessButtons.length; i++) {
            boolean visible = scrollOffset + i < accessEntries.length;
            removeAccessButtons[i].visible = visible;
            removeAccessButtons[i].active = visible && accessManageable;
        }
    }

    private boolean scroll(double delta)
    {
        int max = maxScroll();
        if (max <= 0) {
            return false;
        }
        int previous = scrollOffset;
        scrollOffset = Math.max(0, Math.min(max, scrollOffset - (int)Math.signum(delta)));
        if (previous == scrollOffset) {
            return false;
        }
        updateControls();
        return true;
    }

    private void clampScroll()
    {
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll()));
    }

    private int maxScroll()
    {
        return Math.max(0, parent.settings().accessEntries().length - layout().visibleRows());
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
    public boolean keyPressed(KeyInput event)
    {
        if (event.key() == GLFW.GLFW_KEY_TAB && autocompleteAccessName()) {
            return true;
        }
        return super.keyPressed(event);
    }
    *///?} else if fabric && forge && >=1.21.9 {
    /*@Override
    public boolean keyPressed(KeyEvent event)
    {
        if (event.key() == GLFW.GLFW_KEY_TAB && autocompleteAccessName()) {
            return true;
        }
        return super.keyPressed(event);
    }
    *///?} else {
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (keyCode == GLFW.GLFW_KEY_TAB && autocompleteAccessName()) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    //?}

    //? if >=1.21.11 {
    /*@Override
    public boolean mouseClicked(Click click, boolean doubleClick)
    {
        if (selectAutocomplete(click.x(), click.y())) {
            return true;
        }
        return super.mouseClicked(click, doubleClick);
    }
    *///?} else {
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (selectAutocomplete(mouseX, mouseY)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    //?}

    //? if >=1.18 {
    @Override
    public void close()
    {
        parent.clearAccessScreen(this);
        returnTo(parent);
    }
    //?}
    //? if >=1.17.1 && <1.18 {
    /*@Override
    public void onClose()
    {
        parent.clearAccessScreen(this);
        returnTo(parent);
    }
    *///?}
    //? if >=1.16 && <1.17.1 {
    /*@Override
    public void onClose()
    {
        parent.clearAccessScreen(this);
        returnTo(parent);
    }
    *///?}
    //? if <1.16 {
    /*@Override
    public void onClose()
    {
        parent.clearAccessScreen(this);
        returnTo(parent);
    }
    *///?}

    private void closeScreen()
    {
        parent.clearAccessScreen(this);
        //? if >=1.18
        close();
        //? if >=1.17.1 && <1.18 {
        /*returnTo(parent);
        *///?}
        //? if <1.17.1 {
        /*onClose();
        *///?}
    }

    //? if >=1.20 {
    @Override
    public void render(DrawContext graphics, int mouseX, int mouseY, float partialTick)
    {
        drawPanelBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        TorchmasterScreenRenderAdapter.renderPlan(graphics, textRenderer, renderPlan());
        renderAutocomplete(graphics);
    }
    //?} else if >=1.16 {
    /*@Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        drawPanelBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        TorchmasterScreenRenderAdapter.renderPlan(poseStack, textRenderer, renderPlan());
        renderAutocomplete(poseStack);
    }
    *///?} else {
    /*@Override
    public void render(int mouseX, int mouseY, float partialTick)
    {
        drawPanelBackground();
        super.render(mouseX, mouseY, partialTick);
        TorchmasterScreenRenderAdapter.renderPlan(font, renderPlan());
        renderAutocomplete();
    }
    *///?}

    private TorchmasterScreenRenderPlan renderPlan()
    {
        TorchmasterLightAccessScreenLayout layout = layout();
        List<TorchmasterScreenRenderPlan.LeftLabel> labels = new ArrayList<>();
        labels.add(TorchmasterScreenRenderPlan.left(CompatText.translatable("screen.torchmaster.light.accessTitle"), layout.panelLeft() + 18,
                layout.titleY(), TorchmasterPanelRenderer.LABEL_COLOR));
        labels.add(TorchmasterScreenRenderPlan.left(CompatText.translatable(parent.settings().accessManageable()
                        ? "screen.torchmaster.light.accessManageable"
                        : "screen.torchmaster.light.accessReadOnly"),
                layout.panelLeft() + 18, layout.statusY(), parent.settings().accessManageable() ? TorchmasterPanelRenderer.RANGE_COLOR : TorchmasterPanelRenderer.UNIT_COLOR));
        addAccessRows(layout, labels);
        return new TorchmasterScreenRenderPlan(
                TorchmasterPanelRenderer.background(layout.panelLeft(), layout.panelTop(), layout.panelRight(), layout.panelBottom()),
                layout.panelLeft(),
                layout.panelTop(),
                layout.panelRight(),
                layout.panelBottom(),
                new TorchmasterScreenRenderPlan.CenteredLabel[] {
                        TorchmasterScreenRenderPlan.centered(CompatText.translatable("screen.torchmaster.light.accessSettings"), layout.centerX(),
                                layout.headerY(), TorchmasterPanelRenderer.TITLE_COLOR, true)
                },
                labels.toArray(new TorchmasterScreenRenderPlan.LeftLabel[0]),
                scrollbarFills(layout));
    }

    private void addAccessRows(TorchmasterLightAccessScreenLayout layout, List<TorchmasterScreenRenderPlan.LeftLabel> labels)
    {
        LightAccessEntry[] accessEntries = parent.settings().accessEntries();
        if (accessEntries.length == 0) {
            labels.add(TorchmasterScreenRenderPlan.left(CompatText.translatable("screen.torchmaster.light.accessEmpty"), layout.panelLeft() + 18,
                    layout.listY(), TorchmasterPanelRenderer.UNIT_COLOR));
            return;
        }
        int rows = Math.min(layout.visibleRows(), accessEntries.length - scrollOffset);
        for (int i = 0; i < rows; i++) {
            labels.add(TorchmasterScreenRenderPlan.left(CompatText.literal(accessEntries[scrollOffset + i].name()), layout.panelLeft() + 18,
                    layout.listY() + i * layout.rowHeight() + 4, TorchmasterPanelRenderer.LABEL_COLOR));
        }
    }

    private TorchmasterPanelRenderer.Fill[] scrollbarFills(TorchmasterLightAccessScreenLayout layout)
    {
        int max = maxScroll();
        if (max <= 0) {
            return new TorchmasterPanelRenderer.Fill[0];
        }
        int trackLeft = layout.panelRight() - 10;
        int trackTop = layout.listY();
        int trackBottom = layout.listBottom();
        int trackHeight = Math.max(1, trackBottom - trackTop);
        int entryCount = parent.settings().accessEntries().length;
        int thumbHeight = Math.max(12, trackHeight * layout.visibleRows() / Math.max(layout.visibleRows(), entryCount));
        int thumbTop = trackTop + (trackHeight - thumbHeight) * scrollOffset / max;
        return new TorchmasterPanelRenderer.Fill[] {
                TorchmasterPanelRenderer.fill(trackLeft, trackTop, trackLeft + 3, trackBottom, TorchmasterPanelRenderer.FRAME_DARK_COLOR),
                TorchmasterPanelRenderer.fill(trackLeft, thumbTop, trackLeft + 3, thumbTop + thumbHeight, TorchmasterPanelRenderer.RANGE_COLOR)
        };
    }

    private TorchmasterLightAccessScreenLayout layout()
    {
        return new TorchmasterLightAccessScreenLayout(width, height, PANEL_WIDTH, PANEL_HEIGHT);
    }

    private boolean autocompleteAccessName()
    {
        if (accessNameField == null || !accessNameField.isFocused()) {
            return false;
        }
        for (String playerName : autocompleteCandidates()) {
            if (!playerName.isEmpty()) {
                accessNameField.setText(playerName);
                return true;
            }
        }
        return false;
    }

    private List<String> autocompleteCandidates()
    {
        if (accessNameField == null || !accessNameField.isFocused() || !parent.settings().accessManageable()) {
            return Collections.emptyList();
        }
        String current = accessNameField.getText().trim().toLowerCase();
        List<String> names = new ArrayList<>();
        for (String playerName : onlinePlayerNames()) {
            if ((current.isEmpty() || playerName.toLowerCase().startsWith(current)) && !isAlreadyAllowed(playerName)) {
                names.add(playerName);
            }
            if (names.size() >= MAX_AUTOCOMPLETE_ROWS) {
                break;
            }
        }
        return names;
    }

    private boolean isAlreadyAllowed(String playerName)
    {
        for (LightAccessEntry entry : parent.settings().accessEntries()) {
            if (entry.name().equalsIgnoreCase(playerName)) {
                return true;
            }
        }
        return false;
    }

    private List<String> onlinePlayerNames()
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.getNetworkHandler() == null) {
            return Collections.emptyList();
        }
        List<String> names = new ArrayList<>();
        for (PlayerListEntry entry : minecraft.getNetworkHandler().getPlayerList()) {
            //? if >=1.21.11 {
            /*names.add(entry.getProfile().name());
            *///?} else {
            names.add(entry.getProfile().getName());
            //?}
        }
        Collections.sort(names);
        return names;
    }

    private boolean selectAutocomplete(double mouseX, double mouseY)
    {
        List<String> candidates = autocompleteCandidates();
        if (candidates.isEmpty()) {
            return false;
        }
        TorchmasterLightAccessScreenLayout layout = layout();
        int left = layout.panelLeft() + 18;
        int top = layout.accessFieldY() + BUTTON_HEIGHT + 1;
        int right = left + ACCESS_FIELD_WIDTH;
        for (int i = 0; i < candidates.size(); i++) {
            int rowTop = top + i * 16;
            if (mouseX >= left && mouseX <= right && mouseY >= rowTop && mouseY <= rowTop + 16) {
                accessNameField.setText(candidates.get(i));
                return true;
            }
        }
        return false;
    }

    //? if >=1.20 {
    private void renderAutocomplete(DrawContext graphics)
    {
        renderAutocomplete((fill) -> TorchmasterScreenRenderAdapter.fill(graphics, fill),
                (text, x, y, color) -> TorchmasterScreenRenderAdapter.label(graphics, textRenderer, text, x, y, color));
    }
    //?} else if >=1.16 {
    /*private void renderAutocomplete(MatrixStack poseStack)
    {
        renderAutocomplete((fill) -> TorchmasterScreenRenderAdapter.fill(poseStack, fill),
                (text, x, y, color) -> TorchmasterScreenRenderAdapter.label(poseStack, textRenderer, text, x, y, color));
    }
    *///?} else {
    /*private void renderAutocomplete()
    {
        renderAutocomplete(TorchmasterScreenRenderAdapter::fill,
                (text, x, y, color) -> TorchmasterScreenRenderAdapter.label(font, text, x, y, color));
    }
    *///?}

    private void renderAutocomplete(FillRenderer fillRenderer, LabelRenderer labelRenderer)
    {
        List<String> candidates = autocompleteCandidates();
        if (candidates.isEmpty()) {
            return;
        }
        TorchmasterLightAccessScreenLayout layout = layout();
        int left = layout.panelLeft() + 18;
        int top = layout.accessFieldY() + BUTTON_HEIGHT + 1;
        int right = left + ACCESS_FIELD_WIDTH;
        int bottom = top + candidates.size() * 16;
        fillRenderer.fill(TorchmasterPanelRenderer.fill(left - 1, top - 1, right + 1, bottom + 1, TorchmasterPanelRenderer.FRAME_DARK_COLOR));
        fillRenderer.fill(TorchmasterPanelRenderer.fill(left, top, right, bottom, TorchmasterPanelRenderer.BACKGROUND_COLOR));
        for (int i = 0; i < candidates.size(); i++) {
            labelRenderer.label(CompatText.literal(candidates.get(i)), left + 4, top + 4 + i * 16, TorchmasterPanelRenderer.LABEL_COLOR);
        }
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

    private interface FillRenderer
    {
        void fill(TorchmasterPanelRenderer.Fill fill);
    }

    private interface LabelRenderer
    {
        void label(CompatText text, int x, int y, int color);
    }

    private static final class TorchmasterLightAccessScreenLayout
    {
        private final int width;
        private final int height;
        private final int panelWidth;
        private final int panelHeight;

        private TorchmasterLightAccessScreenLayout(int width, int height, int panelWidth, int panelHeight)
        {
            this.width = width;
            this.height = height;
            this.panelWidth = Math.min(panelWidth, Math.max(160, width - 16));
            this.panelHeight = Math.min(panelHeight, Math.max(220, height - 8));
        }

        private int centerX()
        {
            return width / 2;
        }

        private int panelLeft()
        {
            return (width - panelWidth) / 2;
        }

        private int panelTop()
        {
            return Math.max(4, (height - panelHeight) / 2);
        }

        private int panelRight()
        {
            return panelLeft() + panelWidth;
        }

        private int panelBottom()
        {
            return panelTop() + panelHeight;
        }

        private int headerY()
        {
            return panelTop() + 10;
        }

        private int titleY()
        {
            return panelTop() + 34;
        }

        private int statusY()
        {
            return panelTop() + 48;
        }

        private int accessFieldY()
        {
            return panelTop() + 70;
        }

        private int listY()
        {
            return panelTop() + 100;
        }

        private int listBottom()
        {
            return footerY() - 8;
        }

        private int rowHeight()
        {
            return 18;
        }

        private int visibleRows()
        {
            return Math.max(4, (listBottom() - listY()) / rowHeight());
        }

        private int footerY()
        {
            return panelBottom() - 28;
        }
    }
}
