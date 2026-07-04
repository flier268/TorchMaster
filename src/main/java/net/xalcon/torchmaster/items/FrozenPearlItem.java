package net.xalcon.torchmaster.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
//? if <1.21.2
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
//? if >=1.21.5 {
/*import net.minecraft.world.item.component.TooltipDisplay;
*///?}
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftText;

import javax.annotation.Nullable;
import java.util.List;
//? if >=1.21.5 {
/*import java.util.function.Consumer;
*///?}

public class FrozenPearlItem extends Item
{
    public FrozenPearlItem(Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    //? if >=1.21.2 {
    /*public InteractionResult use(Level level, Player player, InteractionHand hand) {
    *///?} else {
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    //?}
        ItemStack itemStack = player.getItemInHand(hand);
        if(level.isClientSide())
            //? if >=1.21.2 {
            /*return InteractionResult.PASS;
            *///?} else {
            return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
            //?}

        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos(0, 0, 0);
        BlockPos pos = player.blockPosition();
        level.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.7f, 0.6f);
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
                            //? if >=1.21.2 {
                            /*return InteractionResult.SUCCESS;
                            *///?} else {
                            return new InteractionResultHolder<>(InteractionResult.SUCCESS, ItemStack.EMPTY);
                            //?}
                    }
                }
            }
        }
        //? if >=1.21.2 {
        /*return InteractionResult.SUCCESS;
        *///?} else {
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
        //?}
    }

    //? if >=1.21.5 {
    /*@Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltip, flag);
        tooltip.accept(MinecraftText.translatable(this.getDescriptionId() + ".tooltip"));
    }
*///?} elif >=1.21 {
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(MinecraftText.translatable(
                //? if >=1.21.2 {
                /*this.getDescriptionId()
                *///?} else {
                this.getDescriptionId(stack)
                //?}
                + ".tooltip"));
    }
//?} else {
    /*@Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(MinecraftText.translatable(this.getDescriptionId(stack) + ".tooltip"));
    }
    *///?}
}
