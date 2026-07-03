package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xalcon.torchmaster.common.ModCaps;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;

import java.util.Random;
import java.util.function.Function;

public class EntityBlockingLightBlock extends Block
{
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);

    protected Function<BlockPos, String> keyFactory;
    protected Function<BlockPos, IEntityBlockingLight> lightFactory;
    private float flameOffsetY;
    private final VoxelShape shape;

    public EntityBlockingLightBlock(Properties properties, Function<BlockPos, String> keyFactory, Function<BlockPos, IEntityBlockingLight> lightFactory, float flameOffsetY, VoxelShape shape)
    {
        super(properties);
        this.keyFactory = keyFactory;
        this.lightFactory = lightFactory;
        this.flameOffsetY = flameOffsetY;
        this.shape = shape;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return this.shape;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        double d0 = (double)pos.getX() + 0.5f;
        double d1 = (double)pos.getY() + this.flameOffsetY;
        double d2 = (double)pos.getZ() + 0.5f;
        worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0f, 0.0f, 0.0f);
        worldIn.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moving)
    {
        super.onBlockAdded(state, world, pos, oldState, moving);

        world.getCapability(ModCaps.TEB_REGISTRY)
        .ifPresent(reg ->
        {
            //String lightKey = pos.getX() + "_" + pos.getY() + "_" + pos.getZ();
            reg.registerLight(this.getLightKey(pos), this.createLight(state, pos));
        });
    }

    protected String getLightKey(BlockPos pos)
    {
        return this.keyFactory.apply(pos);
    }

    protected IEntityBlockingLight createLight(BlockState state, BlockPos pos)
    {
        return this.lightFactory.apply(pos);
    }

    protected void updateLight(World world, BlockState state, BlockPos pos)
    {
        world.getCapability(ModCaps.TEB_REGISTRY)
            .ifPresent(reg -> reg.registerLight(this.getLightKey(pos), this.createLight(state, pos)));
    }

    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }


    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moving)
    {
        if(state.getBlock() != newState.getBlock())
        {
            world.getCapability(ModCaps.TEB_REGISTRY)
            .ifPresent(reg ->
            {
                //String lightKey = pos.getX() + "_" + pos.getY() + "_" + pos.getZ();
                reg.unregisterLight(this.getLightKey(pos));
            });
        }
        super.onReplaced(state, world, pos, newState, moving);
    }
}
