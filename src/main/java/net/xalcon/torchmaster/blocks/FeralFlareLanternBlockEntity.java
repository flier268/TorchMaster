package net.xalcon.torchmaster.blocks;

import net.minecraft.core.BlockPos;
//? if >=1.21
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
//? if >=1.21.6 {
/*import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
*///?}
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
//? if >=1.21
import net.minecraft.world.phys.shapes.CollisionContext;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.core.FeralFlareLightPlanner;
import net.xalcon.torchmaster.minecraft.MinecraftAdapterViews;
import net.xalcon.torchmaster.platform.Services;

import java.util.ArrayList;
import java.util.List;

public class FeralFlareLanternBlockEntity extends BlockEntity
{
    private int ticks;
    private boolean useLineOfSight;
    private List<BlockPos> childLights = new ArrayList<>();

    private int checkIndex;

    public FeralFlareLanternBlockEntity(BlockPos pos, BlockState state)
    {
        //? if >=1.17 {
        super(ModRegistry.tileFeralFlareLantern.get(), pos, state);
	//?} else {
        /*super(ModRegistry.tileFeralFlareLantern.get());
        this.setPosition(pos);
        *///?}
    }

    //? if >=1.21.5 {
    /*@Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);

        this.removeChildLights();
    }
    *///?}

    // @Override
    public void tick()
    {
        ITorchmasterConfig config = Services.PLATFORM.getConfig();
        if(!FeralFlareLightPlanner.shouldTick(isClientSide(), ++this.ticks, config.getFeralFlareTickRate())) return;
        if(this.childLights.size() > config.getFeralFlareLanternLightCountHardcap()) return;
        ticks = 0;

        int radius = config.getFeralFlareRadius();
        int diameter = radius * 2;

        int x = (radius - this.level.random.nextInt(diameter)) + this.worldPosition.getX();
        int y = (radius - this.level.random.nextInt(diameter)) + this.worldPosition.getY();
        int z = (radius - this.level.random.nextInt(diameter)) + this.worldPosition.getZ();

        // limit height - upper bounds
        BlockPos targetPos = new BlockPos(x, y, z);
        int surfaceHeight = this.level.getHeight(Heightmap.Types.WORLD_SURFACE, targetPos.getX(), targetPos.getZ());
        if (targetPos.getY() > surfaceHeight + 4)
        {
            //? if >=1.17 {
            targetPos = targetPos.atY(surfaceHeight).above(4);
//?} else {
            /*targetPos = new BlockPos(targetPos.getX(), surfaceHeight + 4, targetPos.getZ());
            *///?}
        }

        // dont try to place blocks outside of the world height
        int worldHeightCap = level.getHeight();
        if(targetPos.getY() > worldHeightCap)
            targetPos = new BlockPos(targetPos.getX(), worldHeightCap - 1, targetPos.getZ());

        if(!this.level.isLoaded(targetPos)) return;

        if (this.level.isEmptyBlock(targetPos) && FeralFlareLightPlanner.shouldPlaceLight(
                this.childLights.size(),
                config.getFeralFlareLanternLightCountHardcap(),
                this.level.getBrightness(LightLayer.BLOCK, targetPos),
                config.getFeralFlareMinLightLevel()))
        {
            if(this.useLineOfSight)
            {
                Vec3 start = new Vec3(targetPos.getX(), targetPos.getY(), targetPos.getZ()).add(0.5, 0.5, 0.5);
                Vec3 end = new Vec3(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ()).add(0.5, 0.5, 0.5);
                //? if >=1.21 {
                ClipContext rtxCtx = new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, CollisionContext.empty());
//?} else {
                /*ClipContext rtxCtx = new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, null);
                *///?}
                BlockHitResult rtResult = level.clip(rtxCtx);

                if(rtResult.getType() == BlockHitResult.Type.BLOCK)
                {
                    BlockPos hitPos = rtResult.getBlockPos();
                    if(!(hitPos.getX() == this.worldPosition.getX() && hitPos.getY() == this.worldPosition.getY() && hitPos.getZ() == this.worldPosition.getZ()))
                        return;
                }
            }

            //? if >=1.17 {
            if(this.level.setBlock(targetPos, ModRegistry.blockInvisibleLight.get().defaultBlockState(), Block.UPDATE_ALL))
//?} else {
            /*if(this.level.setBlock(targetPos, ModRegistry.blockInvisibleLight.get().defaultBlockState(), 3))
            *///?}
            {
                this.childLights.add(targetPos);
                this.setChanged();
            }
        }

        if(!this.childLights.isEmpty())
        {
            this.checkIndex = (this.checkIndex + 1) % this.childLights.size();
            BlockPos pos = this.childLights.get(this.checkIndex);
            BlockState block = level.getBlockState(pos);
            if(FeralFlareLightPlanner.shouldRemoveChildLight(
                    MinecraftAdapterViews.blockPos(pos),
                    block.getBlock() instanceof InvisibleLightBlock))
            {
                // Pos in light list no longer points to an invisible light.
                // it may have gotten removed by some means (replaced by block, removed, etc)
                this.childLights.remove(checkIndex);
            }
        }
    }

    public static <T extends BlockEntity> void dispatchTickBlockEntity(Level level, BlockPos pos, BlockState state, T blockEntity)
    {
        if(blockEntity instanceof FeralFlareLanternBlockEntity)
            ((FeralFlareLanternBlockEntity)blockEntity).tick();
    }

    //@Nullable
    //@Override
    //public Packet<ClientGamePacketListener> getUpdatePacket() {
    //    return ClientboundBlockEntityDataPacket.create(this);
    //}

    //@Override
    //public CompoundTag getUpdateTag() {
    //    var tag = super.getUpdateTag();
    //    tag.putBoolean("useLoS", useLineOfSight);
    //    return tag;
    //}

    //@Override
    //public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    //    super.onDataPacket(net, pkt);
    //}

    //@Override
    //public void handleUpdateTag(CompoundTag tag) {
    //    super.handleUpdateTag(tag);
    //}

    //? if >=1.21.6 {
    /*@Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        int[] childLightsEncoded = new int[this.childLights.size()];
        for(int i = 0; i < this.childLights.size(); i++)
            childLightsEncoded[i] = encodePosition(this.worldPosition, this.childLights.get(i));

        output.putIntArray("lights", childLightsEncoded);
        output.putInt("ticks", this.ticks);
        output.putBoolean("useLoS", this.useLineOfSight);
    }
    *///?} elif >=1.21.5 {
    /*@Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
    {
        super.saveAdditional(nbt, pRegistries);
        int[] childLightsEncoded = new int[this.childLights.size()];
        for(int i = 0; i < this.childLights.size(); i++)
            childLightsEncoded[i] = encodePosition(this.worldPosition, this.childLights.get(i));

        nbt.putIntArray("lights", childLightsEncoded);
        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("useLoS", this.useLineOfSight);
    }
    *///?} elif >=1.21 {
    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
    {
        super.saveAdditional(nbt, pRegistries);
        List<Integer> childLightsEncoded = new ArrayList<>(this.childLights.size());
        for(BlockPos child : this.childLights)
            childLightsEncoded.add(encodePosition(this.worldPosition, child));

        nbt.put("lights", new IntArrayTag(childLightsEncoded));
        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("useLoS", this.useLineOfSight);
    }
//?} elif >=1.18 {
    /*@Override
    protected void saveAdditional(CompoundTag nbt)
    {
        super.saveAdditional(nbt);
        List<Integer> childLightsEncoded = new ArrayList<>(this.childLights.size());
        for(BlockPos child : this.childLights)
            childLightsEncoded.add(encodePosition(this.worldPosition, child));

        nbt.put("lights", new IntArrayTag(childLightsEncoded));
        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("useLoS", this.useLineOfSight);
    }
    *///?} else {
    /*@Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        List<Integer> childLightsEncoded = new ArrayList<>(this.childLights.size());
        for(BlockPos child : this.childLights)
            childLightsEncoded.add(encodePosition(this.worldPosition, child));

        nbt.put("lights", new IntArrayTag(childLightsEncoded));
        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("useLoS", this.useLineOfSight);
        return nbt;
    }
    *///?}

    //? if >=1.21.6 {
    /*@Override
    protected void loadAdditional(ValueInput input)
    {
        this.childLights.clear();
        input.getIntArray("lights").ifPresent(lightsEncoded -> {
            BlockPos origin = this.worldPosition;
            for(int encodedLight : lightsEncoded)
                this.childLights.add(decodePosition(origin, encodedLight));
        });
        this.ticks = input.getIntOr("ticks", 0);
        this.useLineOfSight = input.getBooleanOr("useLoS", false);
        super.loadAdditional(input);
    }
    *///?} elif >=1.21.5 {
    /*@Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
    {
        this.childLights.clear();
        nbt.getIntArray("lights").ifPresent(lightsEncoded -> {
            BlockPos origin = this.worldPosition;
            for(int encodedLight : lightsEncoded)
                this.childLights.add(decodePosition(origin, encodedLight));
        });
        this.ticks = nbt.getIntOr("ticks", 0);
        this.useLineOfSight = nbt.getBooleanOr("useLoS", false);
        super.loadAdditional(nbt, pRegistries);
    }
    *///?} elif >=1.21 {
    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
    {
        this.childLights.clear();
        if(nbt.getTagType("lights") == Tag.TAG_INT_ARRAY)
        {
            BlockPos origin = this.worldPosition;
            int[] lightsEncoded = ((IntArrayTag) nbt.get("lights")).getAsIntArray();
            for(int encodedLight : lightsEncoded)
                this.childLights.add(decodePosition(origin, encodedLight));
        }
        this.ticks = nbt.getInt("ticks");
        this.useLineOfSight = nbt.getBoolean("useLoS");
        super.loadAdditional(nbt, pRegistries);
    }
//?} elif >=1.17 {
    /*@Override
    public void load(CompoundTag nbt)
    {
        this.childLights.clear();
        if(nbt.getTagType("lights") == Tag.TAG_INT_ARRAY)
        {
            BlockPos origin = this.worldPosition;
            int[] lightsEncoded = ((IntArrayTag) nbt.get("lights")).getAsIntArray();
            for(int encodedLight : lightsEncoded)
                this.childLights.add(decodePosition(origin, encodedLight));
        }
        this.ticks = nbt.getInt("ticks");
        this.useLineOfSight = nbt.getBoolean("useLoS");
        super.load(nbt);
    }
    *///?} else {
    /*@Override
    public void load(BlockState state, CompoundTag nbt)
    {
        this.childLights.clear();
        if(nbt.getTagType("lights") == 11)
        {
            BlockPos origin = new BlockPos(nbt.getInt("x"), nbt.getInt("y"),nbt.getInt("z"));
            int[] lightsEncoded = ((IntArrayTag) nbt.get("lights")).getAsIntArray();
            for(int encodedLight : lightsEncoded)
                this.childLights.add(decodePosition(origin, encodedLight));
        }
        this.ticks = nbt.getInt("ticks");
        this.useLineOfSight = nbt.getBoolean("useLoS");
        super.load(state, nbt);
    }
    *///?}

    // @Override
    // public CompoundTag save(CompoundTag nbt)
    // {
    //     List<Integer> childLightsEncoded = new ArrayList<>(this.childLights.size());
    //     for(BlockPos child : this.childLights)
    //         childLightsEncoded.add(encodePosition(this.worldPosition, child));
    //     nbt.put("lights", new IntArrayTag(childLightsEncoded));
    //     nbt.putInt("ticks", this.ticks);
    //     nbt.putBoolean("useLoS", this.useLineOfSight);
    //     return super.save(nbt);
    // }

    public void setUseLineOfSight(boolean state)
    {
        Torchmaster.LOG.info("Current: {}, New: {}", useLineOfSight, state);
        this.useLineOfSight = state;
        this.setChanged();
        BlockState blockState = this.level.getBlockState(this.worldPosition);
        this.level.sendBlockUpdated(this.worldPosition, blockState, blockState, 3);
    }

    public boolean shouldUseLineOfSight()
    {
        return this.useLineOfSight;
    }

    public void removeChildLights()
    {
        if(
                isClientSide()
        ) return;
        for(BlockPos pos : this.childLights)
        {
            if (this.level.getBlockState(pos).getBlock() == ModRegistry.blockInvisibleLight.get())
            {
                this.level.removeBlock(pos, false);
            }
        }
        this.childLights.clear();
    }

    private static int encodePosition(BlockPos origin, BlockPos target)
    {
        return FeralFlareLightPlanner.encodeRelativePosition(
                MinecraftAdapterViews.blockPos(origin),
                MinecraftAdapterViews.blockPos(target));
    }

    private static BlockPos decodePosition(BlockPos origin, int pos)
    {
        return MinecraftAdapterViews.blockPos(FeralFlareLightPlanner.decodeRelativePosition(MinecraftAdapterViews.blockPos(origin), pos));
    }

    private boolean isClientSide()
    {
        //? if >=1.21.9 {
        /*return this.level.isClientSide();
        *///?} else {
        return this.level.isClientSide;
        //?}
    }
}
