package net.xalcon.torchmaster.items;

import net.minecraft.block.Block;
//? if >=1.21.11
//import net.minecraft.component.type.TooltipDisplayComponent;
//? if <1.20.6
//import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
//? if >=1.20.6 && <1.21
/*import net.minecraft.client.item.TooltipType;*/
//? if >=1.21
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
//? if <1.21.11
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
//? if <1.21
//import net.minecraft.world.World;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftText;

import javax.annotation.Nullable;
//? if >=1.21.11
//import java.util.function.Consumer;
import java.util.List;

public class FrozenPearlItem extends Item
{
    public FrozenPearlItem(net.minecraft.item.Item.Settings pProperties)
    {
        super(pProperties);
    }

    @Override
    //? if >=1.21.11 {
    /*public ActionResult use(World level, PlayerEntity player, Hand hand) {
    *///?} else if fabric && forge && >=1.21.2 {
    /*public InteractionResult use(Level level, Player player, InteractionHand hand) {
    *///?} else {
    public TypedActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
    //?}
        ItemStack itemStack = player.getStackInHand(hand);
        if(level.isClient())
            //? if >=1.21.11 {
            /*return ActionResult.PASS;
            *///?} else if fabric && forge && >=1.21.2 {
            /*return InteractionResult.PASS;
            *///?} else {
            return new TypedActionResult<>(ActionResult.PASS, itemStack);
            //?}

        BlockPos.Mutable checkPos = new BlockPos.Mutable(0, 0, 0);
        BlockPos pos = player.getBlockPos();
        level.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.7f, 0.6f);
        for(int x = -15; x <= 15; x++)
        {
            for(int y = -15; y <= 15; y++)
            {
                for(int z = -15; z <= 15; z++)
                {
                    checkPos.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    Block block = level.getBlockState(checkPos).getBlock();
                    if(block == TorchmasterContent.blockInvisibleLight.get())
                    {
                        level.removeBlock(checkPos, false);
                        if(itemStack.isEmpty())
                            //? if >=1.21.11 {
                            /*return ActionResult.SUCCESS;
                            *///?} else if fabric && forge && >=1.21.2 {
                            /*return InteractionResult.SUCCESS;
                            *///?} else {
                            return new TypedActionResult<>(ActionResult.SUCCESS, ItemStack.EMPTY);
                            //?}
                    }
                }
            }
        }
        //? if >=1.21.11 {
        /*return ActionResult.SUCCESS;
        *///?} else if fabric && forge && >=1.21.2 {
        /*return InteractionResult.SUCCESS;
        *///?} else {
        return new TypedActionResult<>(ActionResult.SUCCESS, itemStack);
        //?}
    }

    //? if >=1.21.11 {
    /*@Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent tooltipDisplay, Consumer<Text> tooltip, TooltipType flag) {
        super.appendTooltip(stack, context, tooltipDisplay, tooltip, flag);
        tooltip.accept(MinecraftText.translatable(this.getTranslationKey() + ".tooltip"));
    }
*///?} elif fabric && forge && >=1.21.5 {
    /*@Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltip, flag);
        tooltip.accept(MinecraftText.translatable(this.getDescriptionId() + ".tooltip"));
    }
*///?} elif >=1.21 {
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType flag) {
        super.appendTooltip(stack, context, tooltip, flag);
        tooltip.add(MinecraftText.translatable(
                //? if fabric && forge && >=1.21.2 {
                /*this.getDescriptionId()
                *///?} else {
                this.getTranslationKey(stack)
                //?}
                + ".tooltip"));
    }
//?} elif >=1.20.6 {
    /*@Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType flag) {
        super.appendTooltip(stack, context, tooltip, flag);
        tooltip.add(MinecraftText.translatable(this.getTranslationKey(stack) + ".tooltip"));
    }
    *///?} else {
    /*@Override
    public void appendTooltip(ItemStack stack, @Nullable World level, List<Text> tooltip, TooltipContext flag) {
        super.appendTooltip(stack, level, tooltip, flag);
        tooltip.add(MinecraftText.translatable(this.getTranslationKey(stack) + ".tooltip"));
    }
    *///?}
}
