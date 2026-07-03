package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.common.logic.entityblocking.megatorch.MegatorchEntityBlockingLight;

import java.util.function.Function;

public class MegaTorchBlock extends EntityBlockingLightBlock
{
    public static final BooleanProperty DIAMOND_BASE = BooleanProperty.create("diamond_base");

    public MegaTorchBlock(Properties properties, Function<BlockPos, String> keyFactory, float flameOffsetY, VoxelShape shape)
    {
        super(properties, keyFactory, MegatorchEntityBlockingLight::new, flameOffsetY, shape);
        this.setDefaultState(this.stateContainer.getBaseState().with(DIAMOND_BASE, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(DIAMOND_BASE);
    }

    @Override
    protected IEntityBlockingLight createLight(BlockState state, BlockPos pos)
    {
        return new MegatorchEntityBlockingLight(pos, state.get(DIAMOND_BASE));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if(hand != Hand.MAIN_HAND)
            return ActionResultType.PASS;

        boolean hasDiamondBase = state.get(DIAMOND_BASE);
        ItemStack heldStack = player.getHeldItem(hand);

        if(!hasDiamondBase && heldStack.getItem() == Blocks.DIAMOND_BLOCK.asItem())
        {
            if(!world.isRemote)
            {
                if(!player.abilities.isCreativeMode)
                    heldStack.shrink(1);

                BlockState newState = state.with(DIAMOND_BASE, true);
                world.setBlockState(pos, newState, 3);
                this.updateLight(world, newState, pos);
            }
            return ActionResultType.SUCCESS;
        }

        if(hasDiamondBase && player.isSneaking() && heldStack.isEmpty())
        {
            if(!world.isRemote)
            {
                BlockState newState = state.with(DIAMOND_BASE, false);
                world.setBlockState(pos, newState, 3);
                this.updateLight(world, newState, pos);

                ItemStack diamondBlock = new ItemStack(Blocks.DIAMOND_BLOCK);
                if(!player.addItemStackToInventory(diamondBlock))
                    player.dropItem(diamondBlock, false);
            }
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }
}
