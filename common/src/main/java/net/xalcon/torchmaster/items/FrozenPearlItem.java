package net.xalcon.torchmaster.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.Torchmaster;

import javax.annotation.Nullable;
import java.util.List;

public class FrozenPearlItem extends Item
{
    public FrozenPearlItem(Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(level.isClientSide())
            return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);

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
                    if(block == ModRegistry.blockInvisibleLight.get())
                    {
                        level.removeBlock(checkPos, false);
                        if(itemStack.isEmpty())
                            return new InteractionResultHolder<>(InteractionResult.SUCCESS, ItemStack.EMPTY);
                    }
                }
            }
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable(this.getDescriptionId(stack) + ".tooltip"));
    }
}
