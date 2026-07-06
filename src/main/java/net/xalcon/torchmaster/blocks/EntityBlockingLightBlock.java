package net.xalcon.torchmaster.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
//? if >=1.16.5
import net.minecraft.block.ShapeContext;
//? if <1.16.5
//import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
//? if <1.20.6
//import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
//? if >=1.21.5
//import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
//? if <1.20.6
//import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
//? if >=1.19
import net.minecraft.util.math.random.Random;
//? if <1.19
//import java.util.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchmasterClientBridge;
import net.xalcon.torchmaster.minecraft.light.BlockingLightLifecycle;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;

public class EntityBlockingLightBlock extends Block
{
    private final LightType lightType;

    public EntityBlockingLightBlock(Settings properties, LightType lightType)
    {
        super(properties);
        this.lightType = lightType;
    }

    @Override
    //? if >=1.16.5
    public VoxelShape getOutlineShape(BlockState state, BlockView blockGetter, BlockPos pos, ShapeContext ctx) {
    //? if <1.16.5
    //public VoxelShape getOutlineShape(BlockState state, BlockView blockGetter, BlockPos pos, EntityContext ctx) {
        return lightType.shape();
    }

    //? if >=1.20.6 {
    @Override
    protected ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, BlockHitResult hitResult)
    {
        return openLightScreen(level, pos, player);
    }
    //?} else if >=1.15 {
    /*@Override
    public ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult)
    {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isEmpty()) {
            return ActionResult.PASS;
        }
        return openLightScreen(level, pos, player);
    }
    *///?} else {
    /*public boolean activate(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult)
    {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isEmpty()) {
            return false;
        }
        openLightScreen(level, pos, player);
        return true;
    }
    *///?}

    private ActionResult openLightScreen(World level, BlockPos pos, PlayerEntity player)
    {
        //? if >=1.16.5 {
        if (level.isClient()) {
        //?} else {
        /*if (level.isClient) {
        *///?}
            TorchmasterClientBridge.openLightScreen(pos,
                    //? if >=1.16.5
                    level.getRegistryKey()
                    //? if <1.16.5
                    //level.getDimension().getType()
                    , lightType);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    //? if >=1.19 {
    public void randomDisplayTick(BlockState state, World level, BlockPos pos, Random randomSource)
//?} else {
    /*public void randomDisplayTick(BlockState state, World level, BlockPos pos, Random randomSource)
    *///?}
    {
        Vec3d flameOffset = lightType.flameOffset();
        double d0 = (double)pos.getX() + flameOffset.x;
        double d1 = (double)pos.getY() + flameOffset.y;
        double d2 = (double)pos.getZ() + flameOffset.z;
        //? if >=1.21.11 {
        /*level.addParticleClient(ParticleTypes.SMOKE, d0, d1, d2, 0.0f, 0.0f, 0.0f);
        level.addParticleClient(ParticleTypes.FLAME, d0, d1, d2, 0.0f, 0.0f, 0.0f);
        *///?} else {
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0f, 0.0f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0f, 0.0f, 0.0f);
        //?}
    }

    @Override
    public void onBlockAdded(BlockState state, World level, BlockPos pos, BlockState oldState, boolean moving) {
        super.onBlockAdded(state, level, pos, oldState, moving);
        BlockingLightLifecycle.register(level, lightType.key(pos), createLight(state, pos));
    }

    protected MinecraftBlockingLight createLight(BlockState state, BlockPos pos)
    {
        return lightType.createLight(pos);
    }

    protected void updateLight(World level, BlockState state, BlockPos pos)
    {
        BlockingLightLifecycle.register(level, lightType.key(pos), createLight(state, pos));
    }

    //? if >=1.21.11 {
    /*@Override
    protected boolean isTransparent(BlockState state) {
        return true;
    }
    *///?} else if fabric && forge && >=1.21.2 {
    /*@Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return true;
    }
    *///?} else if >=1.19.4 {
    @Override
    public boolean isTransparent(BlockState state, BlockView getter, BlockPos pos) {
        return true;
    }
    //?} else {
    /*@Override
    public boolean isTranslucent(BlockState state, BlockView getter, BlockPos pos) {
        return true;
    }
    *///?}

    //? if >=1.21.5 {
    /*@Override
    protected void onStateReplaced(BlockState state, ServerWorld level, BlockPos pos, boolean moving) {
        BlockingLightLifecycle.unregister(level, pos, lightType);
        super.onStateReplaced(state, level, pos, moving);
    }
    *///?} else if >=1.17 {
    @Override
    public void onStateReplaced(BlockState state, World level, BlockPos pos, BlockState newState, boolean moving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockingLightLifecycle.unregister(level, pos, lightType);
        }
        super.onStateReplaced(state, level, pos, newState, moving);
    }
//?} else if <1.16.5 {
    /*@Override
    public void onBlockRemoved(BlockState state, World level, BlockPos pos, BlockState newState, boolean moving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockingLightLifecycle.unregister(level, pos, lightType);
        }
        super.onBlockRemoved(state, level, pos, newState, moving);
    }
    *///?}
}
