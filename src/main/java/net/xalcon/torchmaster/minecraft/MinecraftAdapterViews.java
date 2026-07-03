package net.xalcon.torchmaster.minecraft;

import net.minecraft.core.BlockPos;
//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.xalcon.torchmaster.EntityFilterList;
import net.xalcon.torchmaster.adapter.BlockPosView;
import net.xalcon.torchmaster.adapter.EntityTypeKey;
import net.xalcon.torchmaster.adapter.Vec3View;
import net.xalcon.torchmaster.adapter.WorldView;
import net.xalcon.torchmaster.core.EntityFilter;

public final class MinecraftAdapterViews {
    private MinecraftAdapterViews() {
    }

    public static BlockPosView blockPos(BlockPos pos) {
        return new BlockPosView(pos.getX(), pos.getY(), pos.getZ());
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
        //? if >=1.21.11 {
        /*for (Identifier id : source.getEntities()) {
        *///?} else {
        for (ResourceLocation id : source.getEntities()) {
        //?}
            filter.register(entityTypeKey(id));
        }
        return filter;
    }
}
