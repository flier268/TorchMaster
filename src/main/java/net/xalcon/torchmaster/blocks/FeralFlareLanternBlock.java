package net.xalcon.torchmaster.blocks;

//? if >=1.20.6
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
//? if >=1.16.5
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
//? if <1.16.5
//import net.minecraft.entity.EntityContext;
//? if >1.16.5 {
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
//?}
import net.minecraft.item.ItemPlacementContext;
//? if >=1.21.5
//import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
//? if <1.16.5
//import net.minecraft.world.IWorld;
import net.minecraft.world.World;
//? if >=1.16.5
import net.minecraft.world.WorldAccess;
import net.xalcon.torchmaster.TorchmasterContent;

import javax.annotation.Nullable;

public class FeralFlareLanternBlock extends FacingBlock implements BlockEntityProvider
{
    protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    //? if >=1.20.6
    public static final MapCodec<FeralFlareLanternBlock> CODEC = createCodec(FeralFlareLanternBlock::new);

    public FeralFlareLanternBlock(Settings properties)
    {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.SOUTH));
    }

    //? if >=1.20.6 {
    @Override
    protected MapCodec<? extends FacingBlock> getCodec()
    {
        return CODEC;
    }
//?}

    @Override
    //? if >=1.16.5
    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context)
    //? if <1.16.5
    //public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, EntityContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getSide().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    //? if >=1.21.5 {
    /*@Override
    protected void onStateReplaced(BlockState state, ServerWorld level, BlockPos pos, boolean moving)
    {
        if(level.getBlockEntity(pos) instanceof FeralFlareLanternBlockEntity te)
            te.removeChildLights();
        super.onStateReplaced(state, level, pos, moving);
    }
    *///?} else if >=1.17 {
    @Override
    public void onStateReplaced(BlockState state, World level, BlockPos pos, BlockState oldState, boolean moving)
    {
        if(level.getBlockEntity(pos) instanceof FeralFlareLanternBlockEntity te)
            te.removeChildLights();
        super.onStateReplaced(state, level, pos, oldState, moving);
    }
    //?} else if <1.16.5 {
    /*@Override
    public void onBlockRemoved(BlockState state, World level, BlockPos pos, BlockState oldState, boolean moving)
    {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof FeralFlareLanternBlockEntity)
            ((FeralFlareLanternBlockEntity)blockEntity).removeChildLights();
        super.onBlockRemoved(state, level, pos, oldState, moving);
    }
    *///?}
    @Override
    //? if >=1.16.5
    public void onBroken(WorldAccess level, BlockPos pos, BlockState state)
    //? if <1.16.5
    //public void onBroken(IWorld level, BlockPos pos, BlockState state)
    {
        //? if >1.16.5 {
        if(level.getBlockEntity(pos) instanceof FeralFlareLanternBlockEntity te)
            te.removeChildLights();
//?} else {
        /*BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof FeralFlareLanternBlockEntity)
            ((FeralFlareLanternBlockEntity)blockEntity).removeChildLights();
        *///?}

        super.onBroken(level, pos, state);
    }

    //? if >1.16.5 {
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FeralFlareLanternBlockEntity(pos, state);
    }
//?} else {
    /*@Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView blockGetter) {
        return new FeralFlareLanternBlockEntity(BlockPos.ORIGIN, this.getDefaultState());
    }
    *///?}

    //? if >1.16.5 {
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState state, BlockEntityType<T> type) {
        return type == TorchmasterContent.tileFeralFlareLantern.get() ? FeralFlareLanternBlockEntity::dispatchTickBlockEntity : null;
    }
//?}

    //@Override
    //public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    //    if(world.isClientSide) return InteractionResult.SUCCESS;
    //    var tile = world.getBlockEntity(pos);
    //    if(tile instanceof FeralFlareLanternTileEntity lantern)
    //    {
    //        lantern.setUseLineOfSight(!lantern.shouldUseLineOfSight());
    //        if(lantern.shouldUseLineOfSight())
    //            player.displayClientMessage(new TranslatableComponent("tile.feral_flare_lantern.line_of_sight.enabled"), true);
    //        else
    //            player.displayClientMessage(new TranslatableComponent("tile.feral_flare_lantern.line_of_sight.disabled"), true);
    //    }
    //    return InteractionResult.SUCCESS;
    //}
}
