package net.xalcon.torchmaster.minecraft.adapter;

import net.minecraft.core.BlockPos;
//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.xalcon.torchmaster.EntityFilterList;
import net.xalcon.torchmaster.port.BlockPosView;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.Vec3View;
import net.xalcon.torchmaster.port.WorldView;
import net.xalcon.torchmaster.domain.EntityFilter;

public final class MinecraftAdapterViews {
    private MinecraftAdapterViews() {
    }

    public static BlockPosView blockPos(BlockPos pos) {
        return new BlockPosView(pos.getX(), pos.getY(), pos.getZ());
    }

    public static BlockPos blockPos(BlockPosView pos) {
        return new BlockPos(pos.x(), pos.y(), pos.z());
    }

    public static Vec3View vec3(Vec3 vec) {
        return new Vec3View(vec.x(), vec.y(), vec.z());
    }

    public static WorldView world(Level level) {
        return new WorldView() {
            @Override
            public String dimensionKey() {
                //? if >=1.21.11 {
                /*return level.dimension().identifier().toString();
                *///?} else {
                return level.dimension().location().toString();
                //?}
            }
        };
    }

    //? if >=1.21.11 {
    /*public static EntityTypeKey entityTypeKey(Identifier id) {
    *///?} else {
    public static EntityTypeKey entityTypeKey(ResourceLocation id) {
    //?}
        return new EntityTypeKey(id.getNamespace(), id.getPath());
    }

    public static EntityFilter entityFilter(EntityFilterList source) {
        EntityFilter filter = new EntityFilter();
        for (EntityTypeKey id : source.getEntities()) {
            filter.register(id);
        }
        return filter;
    }
}
