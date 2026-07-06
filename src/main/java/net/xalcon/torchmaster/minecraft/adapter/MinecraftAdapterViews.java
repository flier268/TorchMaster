package net.xalcon.torchmaster.minecraft.adapter;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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

    public static Vec3View vec3(Vec3d vec) {
        return new Vec3View(vec.getX(), vec.getY(), vec.getZ());
    }

    public static WorldView world(World level) {
        return new WorldView() {
            @Override
            public String dimensionKey() {
                //? if fabric && forge && >=1.21.11 {
                /*return level.dimension().identifier().toString();
                *///?} else {
                //? if >=1.17
                return level.getRegistryKey().getValue().toString();
                //? if <1.17
                //return "overworld";
                //?}
            }
        };
    }

    //? if fabric && forge && >=1.21.11 {
    /*public static EntityTypeKey entityTypeKey(Identifier id) {
    *///?} else {
    public static EntityTypeKey entityTypeKey(Identifier id) {
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
