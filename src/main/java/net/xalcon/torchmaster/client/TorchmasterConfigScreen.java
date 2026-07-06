package net.xalcon.torchmaster.client;

//? if >=1.20
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
//? if >=1.21.11
//import net.minecraft.client.input.KeyInput;
//? if >=1.16 && <1.20
//import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class TorchmasterConfigScreen extends TorchmasterScreenCompat
{
    private static final int BUTTON_HEIGHT = 20;
    private final Screen parent;
    private final TorchmasterConfigScreenController controller = new TorchmasterConfigScreenController();

    public TorchmasterConfigScreen(Screen parent)
    {
        super(text("screen.torchmaster.config.globalTitle"));
        this.parent = parent;
    }

    public static void open()
    {
        open(new TorchmasterConfigScreen(currentScreen()));
    }

    @Override
    protected void init()
    {
        TorchmasterConfigScreenLayout layout = layout();
        boolean readOnly = remoteServer();
        controller.initialize(TorchmasterConfigScreenController.runtime().config(), layout, height, BUTTON_HEIGHT, widgetFactory(), readOnly);

        for (TorchmasterConfigScreenActions.ButtonDescriptor action : controller.bottomButtons(layout, height, BUTTON_HEIGHT)) {
            ButtonWidget button = button(action.x, action.y, action.width, action.height, text(action.translationKey), ignored -> handleAction(action.action));
            if (readOnly && action.action != TorchmasterConfigScreenActions.Action.DONE) {
                TorchmasterConfigWidgetAdapter.active(button, false);
            }
            addCompatWidget(button);
        }
    }

    private void handleAction(TorchmasterConfigScreenActions.Action action)
    {
        TorchmasterConfigScreenController.ActionOutcome outcome = controller.runAction(action, TorchmasterConfigScreenController.runtime());
        if (outcome == TorchmasterConfigScreenController.ActionOutcome.REBUILD_WIDGETS) {
            clearCompatWidgets();
            init();
        } else if (outcome == TorchmasterConfigScreenController.ActionOutcome.CLOSE_SCREEN) {
            closeScreen();
        }
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
            handleAction(TorchmasterConfigScreenActions.Action.SAVE);
            return true;
        }
        return super.keyPressed(event);
    }
    *///?} else if fabric && forge && >=1.21.9 {
    /*@Override
    public boolean keyPressed(KeyEvent event)
    {
        if (event.key() == GLFW.GLFW_KEY_ENTER || event.key() == GLFW.GLFW_KEY_KP_ENTER) {
            handleAction(TorchmasterConfigScreenActions.Action.SAVE);
            return true;
        }
        return super.keyPressed(event);
    }
    *///?} else {
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            handleAction(TorchmasterConfigScreenActions.Action.SAVE);
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
        return controller.scroll(delta, layout(), height, BUTTON_HEIGHT);
    }

    private TorchmasterConfigScreenLayout layout()
    {
        return new TorchmasterConfigScreenLayout(width, height);
    }

    private boolean remoteServer()
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        return minecraft.world != null && minecraft.getServer() == null;
    }

    //? if >=1.20 {
    @Override
    public void render(DrawContext graphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(graphics, mouseX, mouseY, partialTick);
        TorchmasterScreenRenderAdapter.renderPlan(graphics, textRenderer, renderPlan());
    }
    //?} else if >=1.19.4 {
    /*@Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        super.render(poseStack, mouseX, mouseY, partialTick);
        TorchmasterScreenRenderAdapter.renderPlan(poseStack, textRenderer, renderPlan());
    }
    *///?} else if >=1.16 {
    /*@Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        super.render(poseStack, mouseX, mouseY, partialTick);
        TorchmasterScreenRenderAdapter.renderPlan(poseStack, textRenderer, renderPlan());
    }
    *///?} else {
    /*@Override
    public void render(int mouseX, int mouseY, float partialTick)
    {
        TorchmasterConfigScreenLayout layout = layout();

        renderBackground();
        TorchmasterScreenRenderAdapter.background(renderPlan());
        super.render(mouseX, mouseY, partialTick);
        TorchmasterScreenRenderAdapter.renderPlan(font, renderPlan());
    }
    *///?}

    //? if >=1.21 {
    @Override
    public void renderBackground(DrawContext graphics, int mouseX, int mouseY, float partialTick)
    {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        TorchmasterScreenRenderAdapter.background(graphics, renderPlan());
    }
    //?} else if >=1.20.6 {
    /*@Override
    public void renderBackground(DrawContext graphics, int mouseX, int mouseY, float partialTick)
    {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        TorchmasterScreenRenderAdapter.background(graphics, renderPlan());
    }
    *///?} else if >=1.20 {
    /*@Override
    public void renderBackground(DrawContext graphics)
    {
        super.renderBackground(graphics);
        TorchmasterScreenRenderAdapter.background(graphics, renderPlan());
    }
    *///?} else if >=1.16 {
    /*@Override
    public void renderBackground(MatrixStack poseStack)
    {
        super.renderBackground(poseStack);
        TorchmasterScreenRenderAdapter.background(poseStack, renderPlan());
    }
    *///?} else {
    /*
    *///?}

    private TorchmasterScreenRenderPlan renderPlan()
    {
        return controller.renderPlan(layout());
    }

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
