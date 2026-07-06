package net.xalcon.torchmaster.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
//? if <1.20.6
//import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.minecraft.light.megatorch.MegatorchBlockingLight;

public class MegaTorchBlock extends EntityBlockingLightBlock
{
    public static final BooleanProperty DIAMOND_BASE = BooleanProperty.of("diamond_base");

    public MegaTorchBlock(Settings properties, LightType lightType)
    {
        super(properties, lightType);
        this.setDefaultState(this.getDefaultState().with(DIAMOND_BASE, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(DIAMOND_BASE);
    }

    @Override
    protected MinecraftBlockingLight createLight(BlockState state, BlockPos pos)
    {
        return new MegatorchBlockingLight(pos, state.get(DIAMOND_BASE));
    }

    //? if >=1.20.6 {
    @Override
    protected ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, BlockHitResult hitResult)
    {
        ActionResult result = handleDiamondBaseUse(state, level, pos, player, player.getMainHandStack());
        return result == ActionResult.PASS ? super.onUse(state, level, pos, player, hitResult) : result;
    }
    //?} else if >=1.15 {
    /*@Override
    public ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult)
    {
        if (hand != Hand.MAIN_HAND) {
            return ActionResult.PASS;
        }
        ActionResult result = handleDiamondBaseUse(state, level, pos, player, player.getStackInHand(hand));
        return result == ActionResult.PASS ? super.onUse(state, level, pos, player, hand, hitResult) : result;
    }
    *///?} else {
    /*public boolean activate(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult)
    {
        if (hand != Hand.MAIN_HAND) {
            return false;
        }
        ActionResult result = handleDiamondBaseUse(state, level, pos, player, player.getStackInHand(hand));
        return result == ActionResult.PASS ? super.activate(state, level, pos, player, hand, hitResult) : true;
    }
    *///?}

    private ActionResult handleDiamondBaseUse(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack heldStack)
    {
        boolean hasDiamondBase = state.get(DIAMOND_BASE);
        if (!hasDiamondBase && heldStack.getItem() == Blocks.DIAMOND_BLOCK.asItem()) {
            if (!level.isClient()) {
                if (!player.isCreative()) {
                    heldStack.decrement(1);
                }
                setDiamondBase(level, pos, state, true);
            }
            return ActionResult.SUCCESS;
        }

        if (hasDiamondBase && player.isSneaking() && heldStack.isEmpty()) {
            if (!level.isClient()) {
                setDiamondBase(level, pos, state, false);
                giveOrDrop(player, new ItemStack(Blocks.DIAMOND_BLOCK));
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private void setDiamondBase(World level, BlockPos pos, BlockState state, boolean value)
    {
        BlockState newState = state.with(DIAMOND_BASE, value);
        level.setBlockState(pos, newState, 3);
        updateLight(level, newState, pos);
    }

    private static void giveOrDrop(PlayerEntity player, ItemStack stack)
    {
        //? if >=1.17 {
        if (!player.getInventory().insertStack(stack)) {
        //?} else {
        /*if (!player.inventory.insertStack(stack)) {
        *///?}
            player.dropItem(stack, false);
        }
    }
}
