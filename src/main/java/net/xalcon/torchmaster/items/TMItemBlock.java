package net.xalcon.torchmaster.items;

import net.minecraft.block.Block;
//? if >=1.21.11
//import net.minecraft.component.type.TooltipDisplayComponent;
//? if <1.20.6
//import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
//? if >=1.20.6 && <1.21
/*import net.minecraft.client.item.TooltipType;*/
//? if >=1.21
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
//? if <1.21
//import net.minecraft.world.World;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftText;

import javax.annotation.Nullable;
//? if >=1.21.11
//import java.util.function.Consumer;
import java.util.List;

public class TMItemBlock extends BlockItem
{
    public TMItemBlock(Block blockIn, net.minecraft.item.Item.Settings builder)
    {
        super(blockIn, builder);
    }

    //? if >=1.21.11 {
    /*@Override
    public void appendTooltip(ItemStack pStack, TooltipContext pContext, TooltipDisplayComponent pTooltipDisplay, Consumer<Text> pTooltipComponents, TooltipType pTooltipFlag)
    {
        super.appendTooltip(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.accept(MinecraftText.translatable(this.getTranslationKey() + ".tooltip"));
    }
*///?} elif fabric && forge && >=1.21.5 {
    /*@Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, TooltipDisplay pTooltipDisplay, Consumer<Component> pTooltipComponents, TooltipFlag pTooltipFlag)
    {
        super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.accept(MinecraftText.translatable(this.getDescriptionId() + ".tooltip"));
    }
*///?} elif >=1.21 {
    @Override
    public void appendTooltip(ItemStack pStack, TooltipContext pContext, List<Text> pTooltipComponents, TooltipType pTooltipFlag)
    {
        super.appendTooltip(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(MinecraftText.translatable(
                //? if fabric && forge && >=1.21.2 {
                /*this.getDescriptionId()
                *///?} else {
                this.getTranslationKey(pStack)
                //?}
                + ".tooltip"));
    }
//?} elif >=1.20.6 {
    /*@Override
    public void appendTooltip(ItemStack pStack, Item.TooltipContext pContext, List<Text> pTooltipComponents, TooltipType pTooltipFlag)
    {
        super.appendTooltip(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(MinecraftText.translatable(this.getTranslationKey(pStack) + ".tooltip"));
    }
    *///?} else {
    /*@Override
    public void appendTooltip(ItemStack pStack, @Nullable World pLevel, List<Text> pTooltipComponents, TooltipContext pTooltipFlag)
    {
        super.appendTooltip(pStack, pLevel, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(MinecraftText.translatable(this.getTranslationKey(pStack) + ".tooltip"));
    }
    *///?}
}
