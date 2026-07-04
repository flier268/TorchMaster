package net.xalcon.torchmaster.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
//? if >=1.21.5 {
/*import net.minecraft.world.item.component.TooltipDisplay;
*///?}
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftText;

import javax.annotation.Nullable;
import java.util.List;
//? if >=1.21.5 {
/*import java.util.function.Consumer;
*///?}

public class TMItemBlock extends BlockItem
{
    public TMItemBlock(Block blockIn, Properties builder)
    {
        super(blockIn, builder);
    }

    //? if >=1.21.5 {
    /*@Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, TooltipDisplay pTooltipDisplay, Consumer<Component> pTooltipComponents, TooltipFlag pTooltipFlag)
    {
        super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.accept(MinecraftText.translatable(this.getDescriptionId() + ".tooltip"));
    }
*///?} elif >=1.21 {
    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag)
    {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(MinecraftText.translatable(
                //? if >=1.21.2 {
                /*this.getDescriptionId()
                *///?} else {
                this.getDescriptionId(pStack)
                //?}
                + ".tooltip"));
    }
//?} else {
    /*@Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag)
    {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(MinecraftText.translatable(this.getDescriptionId(pStack) + ".tooltip"));
    }
    *///?}
}
