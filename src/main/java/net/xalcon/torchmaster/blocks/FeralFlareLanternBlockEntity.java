package net.xalcon.torchmaster.blocks;

import net.minecraft.block.BlockState;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.minecraft.light.feralflare.FeralFlareLanternLifecycle;
import net.xalcon.torchmaster.minecraft.light.feralflare.FeralFlareLightPositions;
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
        FeralFlareLanternLifecycle.TickOutcome outcome = FeralFlareLanternLifecycle.tick(
                this.world,
                this.pos,
                this.childLights,
                this.ticks,
                this.checkIndex,
                this.useLineOfSight,
                config);
        this.ticks = outcome.ticks();
        this.checkIndex = outcome.checkIndex();
        if (outcome.dirty()) {
            this.markDirty();
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
        int[] childLightsEncoded = FeralFlareLightPositions.encode(this.pos, this.childLights);

        output.putIntArray("lights", childLightsEncoded);
        output.putInt("ticks", this.ticks);
        output.putBoolean("useLoS", this.useLineOfSight);
    }
*///?} elif fabric && forge && >=1.21.6 {
    /*@Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        int[] childLightsEncoded = FeralFlareLightPositions.encode(this.worldPosition, this.childLights);

        output.putIntArray("lights", childLightsEncoded);
        output.putInt("ticks", this.ticks);
        output.putBoolean("useLoS", this.useLineOfSight);
    }
    *///?} elif fabric && forge && >=1.21.5 {
    /*@Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
    {
        super.saveAdditional(nbt, pRegistries);
        int[] childLightsEncoded = FeralFlareLightPositions.encode(this.worldPosition, this.childLights);

        nbt.putIntArray("lights", childLightsEncoded);
        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("useLoS", this.useLineOfSight);
    }
    *///?} elif >=1.20.6 {
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup pRegistries)
    {
        super.writeNbt(nbt, pRegistries);
        int[] childLightsEncoded = FeralFlareLightPositions.encode(this.pos, this.childLights);

        nbt.put("lights", new NbtIntArray(childLightsEncoded));
        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("useLoS", this.useLineOfSight);
    }
//?} elif >=1.18 {
    /*@Override
    protected void writeNbt(NbtCompound nbt)
    {
        super.writeNbt(nbt);
        int[] childLightsEncoded = FeralFlareLightPositions.encode(this.pos, this.childLights);

        nbt.put("lights", new NbtIntArray(childLightsEncoded));
        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("useLoS", this.useLineOfSight);
    }
    *///?} elif >=1.16.5 {
    /*@Override
    public NbtCompound writeNbt(NbtCompound nbt)
    {
        super.writeNbt(nbt);
        int[] childLightsEncoded = FeralFlareLightPositions.encode(this.pos, this.childLights);

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
        int[] childLightsEncoded = FeralFlareLightPositions.encode(this.pos, this.childLights);

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
        input.getOptionalIntArray("lights").ifPresent(lightsEncoded ->
                this.childLights.addAll(FeralFlareLightPositions.decode(this.pos, lightsEncoded)));
        this.ticks = input.getInt("ticks", 0);
        this.useLineOfSight = input.getBoolean("useLoS", false);
        super.readData(input);
    }
*///?} elif fabric && forge && >=1.21.6 {
    /*@Override
    protected void loadAdditional(ValueInput input)
    {
        this.childLights.clear();
        input.getIntArray("lights").ifPresent(lightsEncoded ->
                this.childLights.addAll(FeralFlareLightPositions.decode(this.worldPosition, lightsEncoded)));
        this.ticks = input.getIntOr("ticks", 0);
        this.useLineOfSight = input.getBooleanOr("useLoS", false);
        super.loadAdditional(input);
    }
    *///?} elif fabric && forge && >=1.21.5 {
    /*@Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
    {
        this.childLights.clear();
        nbt.getIntArray("lights").ifPresent(lightsEncoded ->
                this.childLights.addAll(FeralFlareLightPositions.decode(this.worldPosition, lightsEncoded)));
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
            int[] lightsEncoded = ((NbtIntArray) nbt.get("lights")).getIntArray();
            this.childLights.addAll(FeralFlareLightPositions.decode(this.pos, lightsEncoded));
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
            int[] lightsEncoded = ((NbtIntArray) nbt.get("lights")).getIntArray();
            this.childLights.addAll(FeralFlareLightPositions.decode(this.pos, lightsEncoded));
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
            int[] lightsEncoded = ((NbtIntArray) nbt.get("lights")).getIntArray();
            this.childLights.addAll(FeralFlareLightPositions.decode(this.pos, lightsEncoded));
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
            int[] lightsEncoded = ((IntArrayTag) nbt.get("lights")).getIntArray();
            this.childLights.addAll(FeralFlareLightPositions.decode(this.pos, lightsEncoded));
        }
        this.ticks = nbt.getInt("ticks");
        this.useLineOfSight = nbt.getBoolean("useLoS");
        super.fromTag(nbt);
    }
    *///?}

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
        FeralFlareLanternLifecycle.removeChildLights(this.world, this.childLights);
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
