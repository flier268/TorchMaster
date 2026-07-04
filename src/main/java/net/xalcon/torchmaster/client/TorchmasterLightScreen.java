//? if >=1.16 {
package net.xalcon.torchmaster.client;

import net.minecraft.client.Minecraft;
//? if >=1.20 {
import net.minecraft.client.gui.GuiGraphics;
//?} else {
/*import com.mojang.blaze3d.vertex.PoseStack;
*///?}
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
//? if <1.19 {
/*import net.minecraft.network.chat.TextComponent;
*///?}
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.minecraft.MinecraftText;

public class TorchmasterLightScreen extends Screen
{
    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 118;
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;
    private static final int SETTINGS_BUTTON_SIZE = 20;

    private final Screen parent;
    private final BlockPos pos;
    private final ResourceKey<Level> dimension;
    private final LightType lightType;
    private final int radius;
    private Button visibilityButton;

    private TorchmasterLightScreen(Screen parent, BlockPos pos, ResourceKey<Level> dimension, LightType lightType)
    {
        super(text("screen.torchmaster.light.title"));
        this.parent = parent;
        this.pos = pos.immutable();
        this.dimension = dimension;
        this.lightType = lightType;
        this.radius = radiusFor(lightType, Torchmaster.getConfig());
    }

    public static void open(BlockPos pos, ResourceKey<Level> dimension, LightType lightType)
    {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.setScreen(new TorchmasterLightScreen(minecraft.screen, pos, dimension, lightType));
    }

    @Override
    protected void init()
    {
        int centerX = width / 2;
        int top = Math.max(20, (height - PANEL_HEIGHT) / 2);
        int right = (width + PANEL_WIDTH) / 2;
        addCompatWidget(button(right - SETTINGS_BUTTON_SIZE - 8, top + 8, SETTINGS_BUTTON_SIZE, SETTINGS_BUTTON_SIZE, literal("⚙"),
                ignored -> minecraft.setScreen(new TorchmasterConfigScreen(this))));
        visibilityButton = button(centerX - BUTTON_WIDTH / 2, top + 54, BUTTON_WIDTH, BUTTON_HEIGHT, visibilityLabel(),
                ignored -> toggleVisibility());
        addCompatWidget(visibilityButton);
        addCompatWidget(button(centerX - 50, top + 84, 100, BUTTON_HEIGHT, text("gui.done"), ignored -> onClose()));
    }

    private void toggleVisibility()
    {
        TorchmasterLightRangeDisplay.toggle(dimension, pos, lightType, radius);
        visibilityButton.setMessage(visibilityLabel());
    }

    private Component visibilityLabel()
    {
        return text(TorchmasterLightRangeDisplay.isVisible(dimension, pos)
                ? "screen.torchmaster.light.hideRange"
                : "screen.torchmaster.light.showRange");
    }

    @Override
    public void onClose()
    {
        minecraft.setScreen(parent);
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
        int left = (width - PANEL_WIDTH) / 2;
        int top = Math.max(20, (height - PANEL_HEIGHT) / 2);
        int right = left + PANEL_WIDTH;
        int bottom = top + PANEL_HEIGHT;

        graphics.fill(left, top, right, bottom, 0xAA101010);
        graphics.fill(left, top, right, top + 1, 0xFF404040);
        graphics.fill(left, bottom - 1, right, bottom, 0xFF404040);
        graphics.drawCenteredString(font, title, width / 2, top + 10, 0xFFFFFFFF);
        graphics.drawCenteredString(font, blockName(), width / 2, top + 28, 0xFFE0E0E0);
        graphics.drawCenteredString(font, text("screen.torchmaster.light.range", radius), width / 2, top + 42, 0xFFA0FFA0);

        super.render(graphics, mouseX, mouseY, partialTick);
    }
    //?} else {
    /*@Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        renderBackground(poseStack);
        int left = (width - PANEL_WIDTH) / 2;
        int top = Math.max(20, (height - PANEL_HEIGHT) / 2);
        int right = left + PANEL_WIDTH;
        int bottom = top + PANEL_HEIGHT;

        fill(poseStack, left, top, right, bottom, 0xAA101010);
        fill(poseStack, left, top, right, top + 1, 0xFF404040);
        fill(poseStack, left, bottom - 1, right, bottom, 0xFF404040);
        drawCenteredString(poseStack, font, title, width / 2, top + 10, 0xFFFFFFFF);
        drawCenteredString(poseStack, font, blockName(), width / 2, top + 28, 0xFFE0E0E0);
        drawCenteredString(poseStack, font, text("screen.torchmaster.light.range", radius), width / 2, top + 42, 0xFFA0FFA0);

        super.render(poseStack, mouseX, mouseY, partialTick);
    }
    *///?}

    private Component blockName()
    {
        return text(lightType == LightType.MegaTorch ? "block.torchmaster.megatorch" : "block.torchmaster.dreadlamp");
    }

    private static int radiusFor(LightType lightType, ITorchmasterConfig config)
    {
        return lightType == LightType.MegaTorch ? config.getMegaTorchRadius() : config.getDreadLampRadius();
    }

    private Button addCompatWidget(Button widget)
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

    private static Component text(String translationKey)
    {
        return MinecraftText.translatable(translationKey);
    }

    private static Component text(String translationKey, Object value)
    {
        return MinecraftText.translatable(translationKey, value);
    }

    private static Component literal(String value)
    {
        return MinecraftText.literal(value);
    }

    @SuppressWarnings("unused")
    private static Component emptyText()
    {
        //? if >=1.19 {
        return Component.empty();
        //?} else {
        /*return new TextComponent("");
        *///?}
    }
}
//?}
