package net.xalcon.torchmaster.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
//? if >=1.16.5
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
//? if >=1.16.5 {
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIntArray;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
*///?}
//? if >=1.20.6
import net.minecraft.registry.RegistryWrapper;
//? if >=1.21.11
//import net.minecraft.storage.ReadView;
//? if >=1.21.11
//import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
//? if >=1.16.5
import net.minecraft.world.RaycastContext;
//? if <1.16.5
//import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.domain.FeralFlareLightPlanner;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftAdapterViews;
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
        super(TorchmasterContent.tileFeralFlareLantern.get(), pos, state);
	//?} else {
        /*super(TorchmasterContent.tileFeralFlareLantern.get());
        this.setPos(pos);
        *///?}
    }

    //? if fabric && forge && >=1.21.5 {
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

        int x = (radius - this.world.random.nextInt(diameter)) + this.pos.getX();
        int y = (radius - this.world.random.nextInt(diameter)) + this.pos.getY();
        int z = (radius - this.world.random.nextInt(diameter)) + this.pos.getZ();

        // limit height - upper bounds
        BlockPos targetPos = new BlockPos(x, y, z);
        //? if >=1.15 {
        int surfaceHeight = this.world.getTopY(Heightmap.Type.WORLD_SURFACE, targetPos.getX(), targetPos.getZ());
        //?} else {
        /*int surfaceHeight = this.world.getTop(Heightmap.Type.WORLD_SURFACE, targetPos.getX(), targetPos.getZ());
        *///?}
        if (targetPos.getY() > surfaceHeight + 4)
        {
            //? if >=1.17 {
            targetPos = targetPos.withY(surfaceHeight).up(4);
//?} else {
            /*targetPos = new BlockPos(targetPos.getX(), surfaceHeight + 4, targetPos.getZ());
            *///?}
        }

        // dont try to place blocks outside of the world height
        //? if >=1.16.5 {
        int worldHeightCap = world.getHeight();
        //?} else {
        /*int worldHeightCap = world.getEffectiveHeight();
        *///?}
        if(targetPos.getY() > worldHeightCap)
            targetPos = new BlockPos(targetPos.getX(), worldHeightCap - 1, targetPos.getZ());

        //? if >=1.21.11 {
        /*if(!this.world.isInBuildLimit(targetPos)) return;
        *///?} else {
        if(!this.world.canSetBlock(targetPos)) return;
        //?}

        if (this.world.isAir(targetPos) && FeralFlareLightPlanner.shouldPlaceLight(
                this.childLights.size(),
                config.getFeralFlareLanternLightCountHardcap(),
                this.world.getLightLevel(LightType.BLOCK, targetPos),
                config.getFeralFlareMinLightLevel()))
        {
            if(this.useLineOfSight)
            {
                Vec3d start = new Vec3d(targetPos.getX(), targetPos.getY(), targetPos.getZ()).add(0.5, 0.5, 0.5);
                Vec3d end = new Vec3d(this.pos.getX(), this.pos.getY(), this.pos.getZ()).add(0.5, 0.5, 0.5);
                //? if >=1.16.5 {
                RaycastContext rtxCtx = new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY,
                        //? if >=1.20.6
                        ShapeContext.absent()
                        //? if <1.20.6
                        //null
                );
//?} else {
                /*RayTraceContext rtxCtx = new RayTraceContext(start, end, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.ANY, null);
                *///?}
                //? if >=1.16.5 {
                BlockHitResult rtResult = world.raycast(rtxCtx);
                //?} else {
                /*BlockHitResult rtResult = world.rayTrace(rtxCtx);
                *///?}

                if(rtResult.getType() == BlockHitResult.Type.BLOCK)
                {
                    BlockPos hitPos = rtResult.getBlockPos();
                    if(!(hitPos.getX() == this.pos.getX() && hitPos.getY() == this.pos.getY() && hitPos.getZ() == this.pos.getZ()))
                        return;
                }
            }

            //? if >=1.17 {
            if(this.world.setBlockState(targetPos, TorchmasterContent.blockInvisibleLight.get().getDefaultState(), Block.NOTIFY_ALL))
//?} else {
            /*if(this.world.setBlockState(targetPos, TorchmasterContent.blockInvisibleLight.get().getDefaultState(), 3))
            *///?}
            {
                this.childLights.add(targetPos);
                this.markDirty();
            }
        }

        if(!this.childLights.isEmpty())
        {
            this.checkIndex = (this.checkIndex + 1) % this.childLights.size();
            BlockPos pos = this.childLights.get(this.checkIndex);
            BlockState block = world.getBlockState(pos);
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

    public static <T extends BlockEntity> void dispatchTickBlockEntity(World level, BlockPos pos, BlockState state, T blockEntity)
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

    //? if >=1.21.11 {
    /*@Override
    protected void writeData(WriteView output)
    {
        super.writeData(output);
        int[] childLightsEncoded = new int[this.childLights.size()];
        for(int i = 0; i < this.childLights.size(); i++)
            childLightsEncoded[i] = encodePosition(this.pos, this.childLights.get(i));

        output.putIntArray("lights", childLightsEncoded);
        output.putInt("ticks", this.ticks);
        output.putBoolean("useLoS", this.useLineOfSight);
    }
*///?} elif fabric && forge && >=1.21.6 {
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
    *///?} elif fabric && forge && >=1.21.5 {
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
    *///?} elif >=1.20.6 {
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup pRegistries)
    {
        super.writeNbt(nbt, pRegistries);
        List<Integer> childLightsEncoded = new ArrayList<>(this.childLights.size());
        for(BlockPos child : this.childLights)
            childLightsEncoded.add(encodePosition(this.pos, child));

        nbt.put("lights", new NbtIntArray(childLightsEncoded));
        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("useLoS", this.useLineOfSight);
    }
//?} elif >=1.18 {
    /*@Override
    protected void writeNbt(NbtCompound nbt)
    {
        super.writeNbt(nbt);
        List<Integer> childLightsEncoded = new ArrayList<>(this.childLights.size());
        for(BlockPos child : this.childLights)
            childLightsEncoded.add(encodePosition(this.pos, child));

        nbt.put("lights", new NbtIntArray(childLightsEncoded));
        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("useLoS", this.useLineOfSight);
    }
    *///?} elif >=1.16.5 {
    /*@Override
    public NbtCompound writeNbt(NbtCompound nbt)
    {
        super.writeNbt(nbt);
        List<Integer> childLightsEncoded = new ArrayList<>(this.childLights.size());
        for(BlockPos child : this.childLights)
            childLightsEncoded.add(encodePosition(this.pos, child));

        nbt.put("lights", new NbtIntArray(childLightsEncoded));
        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("useLoS", this.useLineOfSight);
        return nbt;
    }
    *///?} else {
    /*@Override
    public CompoundTag toTag(CompoundTag nbt)
    {
        super.toTag(nbt);
        List<Integer> childLightsEncoded = new ArrayList<>(this.childLights.size());
        for(BlockPos child : this.childLights)
            childLightsEncoded.add(encodePosition(this.pos, child));

        nbt.put("lights", new IntArrayTag(childLightsEncoded));
        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("useLoS", this.useLineOfSight);
        return nbt;
    }
    *///?}

    //? if >=1.21.11 {
    /*@Override
    protected void readData(ReadView input)
    {
        this.childLights.clear();
        input.getOptionalIntArray("lights").ifPresent(lightsEncoded -> {
            BlockPos origin = this.pos;
            for(int encodedLight : lightsEncoded)
                this.childLights.add(decodePosition(origin, encodedLight));
        });
        this.ticks = input.getInt("ticks", 0);
        this.useLineOfSight = input.getBoolean("useLoS", false);
        super.readData(input);
    }
*///?} elif fabric && forge && >=1.21.6 {
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
    *///?} elif fabric && forge && >=1.21.5 {
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
    *///?} elif >=1.20.6 {
    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup pRegistries)
    {
        this.childLights.clear();
        if(nbt.getType("lights") == NbtElement.INT_ARRAY_TYPE)
        {
            BlockPos origin = this.pos;
            int[] lightsEncoded = ((NbtIntArray) nbt.get("lights")).getIntArray();
            for(int encodedLight : lightsEncoded)
                this.childLights.add(decodePosition(origin, encodedLight));
        }
        this.ticks = nbt.getInt("ticks");
        this.useLineOfSight = nbt.getBoolean("useLoS");
        super.readNbt(nbt, pRegistries);
    }
//?} elif >=1.17 {
    /*@Override
    public void readNbt(NbtCompound nbt)
    {
        this.childLights.clear();
        if(nbt.getType("lights") == 11)
        {
            BlockPos origin = this.pos;
            int[] lightsEncoded = ((NbtIntArray) nbt.get("lights")).getIntArray();
            for(int encodedLight : lightsEncoded)
                this.childLights.add(decodePosition(origin, encodedLight));
        }
        this.ticks = nbt.getInt("ticks");
        this.useLineOfSight = nbt.getBoolean("useLoS");
        super.readNbt(nbt);
    }
    *///?} elif >=1.16.5 {
    /*@Override
    public void fromTag(BlockState state, NbtCompound nbt)
    {
        this.childLights.clear();
        if(nbt.getType("lights") == 11)
        {
            BlockPos origin = this.pos;
            int[] lightsEncoded = ((NbtIntArray) nbt.get("lights")).getIntArray();
            for(int encodedLight : lightsEncoded)
                this.childLights.add(decodePosition(origin, encodedLight));
        }
        this.ticks = nbt.getInt("ticks");
        this.useLineOfSight = nbt.getBoolean("useLoS");
        super.fromTag(state, nbt);
    }
    *///?} else {
    /*@Override
    public void fromTag(CompoundTag nbt)
    {
        this.childLights.clear();
        if(nbt.getType("lights") == 11)
        {
            BlockPos origin = this.pos;
            int[] lightsEncoded = ((IntArrayTag) nbt.get("lights")).getIntArray();
            for(int encodedLight : lightsEncoded)
                this.childLights.add(decodePosition(origin, encodedLight));
        }
        this.ticks = nbt.getInt("ticks");
        this.useLineOfSight = nbt.getBoolean("useLoS");
        super.fromTag(nbt);
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
        TorchmasterRuntime.LOG.info("Current: {}, New: {}", useLineOfSight, state);
        this.useLineOfSight = state;
        this.markDirty();
        BlockState blockState = this.world.getBlockState(this.pos);
        this.world.updateListeners(this.pos, blockState, blockState, 3);
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
            if (this.world.getBlockState(pos).getBlock() == TorchmasterContent.blockInvisibleLight.get())
            {
                this.world.removeBlock(pos, false);
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
        //? if fabric && forge && >=1.21.9 {
        /*return this.level.isClientSide();
        *///?} else {
        //? if >=1.21.11
        //return this.world.isClient()
        //? if <1.21.11
        return this.world.isClient
        ;
        //?}
    }
}
