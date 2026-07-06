package net.xalcon.torchmaster.client;

//? if >=1.20
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
//? if >=1.16 && <1.20
//import net.minecraft.client.util.math.MatrixStack;
//? if >=1.16 && <1.20
//import net.minecraft.text.Text;

final class TorchmasterScreenRenderAdapter
{
    private TorchmasterScreenRenderAdapter()
    {
    }

    //? if >=1.20 {
    static void fill(DrawContext graphics, TorchmasterPanelRenderer.Fill fill)
    {
        graphics.fill(fill.left, fill.top, fill.right, fill.bottom, fill.color);
    }

    static void frame(DrawContext graphics, int left, int top, int right, int bottom)
    {
        for (TorchmasterPanelRenderer.Fill fill : TorchmasterPanelRenderer.frame(left, top, right, bottom)) {
            fill(graphics, fill);
        }
    }

    static void centered(DrawContext graphics, TextRenderer textRenderer, CompatText text, int x, int y, int color)
    {
        graphics.drawCenteredTextWithShadow(textRenderer, text.asWidget(), x, y, color);
    }

    static void label(DrawContext graphics, TextRenderer textRenderer, CompatText text, int x, int y, int color)
    {
        graphics.drawText(textRenderer, text.asWidget(), x, y, color, false);
    }

    static void background(DrawContext graphics, TorchmasterScreenRenderPlan plan)
    {
        fill(graphics, plan.background);
    }

    static void renderPlan(DrawContext graphics, TextRenderer textRenderer, TorchmasterScreenRenderPlan plan)
    {
        frame(graphics, plan.frameLeft, plan.frameTop, plan.frameRight, plan.frameBottom);
        for (TorchmasterScreenRenderPlan.CenteredLabel label : plan.centeredLabels()) {
            centered(graphics, textRenderer, label.text, label.x, label.y, label.color);
        }
        for (TorchmasterScreenRenderPlan.LeftLabel label : plan.leftLabels()) {
            label(graphics, textRenderer, label.text, label.x, label.y, label.color);
        }
    }
    //?} else if >=1.16 {
    /*static void fill(MatrixStack poseStack, TorchmasterPanelRenderer.Fill fill)
    {
        net.minecraft.client.gui.DrawableHelper.fill(poseStack, fill.left, fill.top, fill.right, fill.bottom, fill.color);
    }

    static void frame(MatrixStack poseStack, int left, int top, int right, int bottom)
    {
        for (TorchmasterPanelRenderer.Fill fill : TorchmasterPanelRenderer.frame(left, top, right, bottom)) {
            fill(poseStack, fill);
        }
    }

    static void centered(MatrixStack poseStack, TextRenderer textRenderer, CompatText text, int x, int y, int color, boolean shadow)
    {
        Text widgetText = text.asWidget();
        int drawX = x - textRenderer.getWidth(widgetText) / 2;
        if (shadow) {
            textRenderer.drawWithShadow(poseStack, widgetText, drawX, y, color);
        } else {
            textRenderer.draw(poseStack, widgetText, drawX, y, color);
        }
    }

    static void label(MatrixStack poseStack, TextRenderer textRenderer, CompatText text, int x, int y, int color)
    {
        textRenderer.drawWithShadow(poseStack, text.asWidget(), x, y, color);
    }

    static void background(MatrixStack poseStack, TorchmasterScreenRenderPlan plan)
    {
        fill(poseStack, plan.background);
    }

    static void renderPlan(MatrixStack poseStack, TextRenderer textRenderer, TorchmasterScreenRenderPlan plan)
    {
        frame(poseStack, plan.frameLeft, plan.frameTop, plan.frameRight, plan.frameBottom);
        for (TorchmasterScreenRenderPlan.CenteredLabel label : plan.centeredLabels()) {
            centered(poseStack, textRenderer, label.text, label.x, label.y, label.color, label.shadow);
        }
        for (TorchmasterScreenRenderPlan.LeftLabel label : plan.leftLabels()) {
            label(poseStack, textRenderer, label.text, label.x, label.y, label.color);
        }
    }
    *///?} else {
    /*static void fill(TorchmasterPanelRenderer.Fill fill)
    {
        net.minecraft.client.gui.DrawableHelper.fill(fill.left, fill.top, fill.right, fill.bottom, fill.color);
    }

    static void frame(int left, int top, int right, int bottom)
    {
        for (TorchmasterPanelRenderer.Fill fill : TorchmasterPanelRenderer.frame(left, top, right, bottom)) {
            fill(fill);
        }
    }

    static void centered(TextRenderer textRenderer, CompatText text, int x, int y, int color)
    {
        String widgetText = text.asWidget();
        textRenderer.drawWithShadow(widgetText, x - textRenderer.getStringWidth(widgetText) / 2, y, color);
    }

    static void label(TextRenderer textRenderer, CompatText text, int x, int y, int color)
    {
        textRenderer.drawWithShadow(text.asWidget(), x, y, color);
    }

    static void background(TorchmasterScreenRenderPlan plan)
    {
        fill(plan.background);
    }

    static void renderPlan(TextRenderer textRenderer, TorchmasterScreenRenderPlan plan)
    {
        frame(plan.frameLeft, plan.frameTop, plan.frameRight, plan.frameBottom);
        for (TorchmasterScreenRenderPlan.CenteredLabel label : plan.centeredLabels()) {
            centered(textRenderer, label.text, label.x, label.y, label.color);
        }
        for (TorchmasterScreenRenderPlan.LeftLabel label : plan.leftLabels()) {
            label(textRenderer, label.text, label.x, label.y, label.color);
        }
    }
    *///?}
}
