package net.xalcon.torchmaster.minecraft.light.feralflare;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
//? if >=1.16.5
import net.minecraft.block.ShapeContext;
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
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.blocks.InvisibleLightBlock;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.domain.FeralFlareLightPlanner;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftAdapterViews;

import java.util.List;

public final class FeralFlareLanternLifecycle {
    private FeralFlareLanternLifecycle() {
    }

    public static TickOutcome tick(
            World level,
            BlockPos origin,
            List<BlockPos> childLights,
            int ticks,
            int checkIndex,
            boolean useLineOfSight,
            ITorchmasterConfig config
    ) {
        FeralFlareLightPlanner.TickDecision decision = FeralFlareLightPlanner.beginTick(
                isClientSide(level),
                ticks,
                checkIndex,
                config.getFeralFlareTickRate(),
                childLights.size(),
                config.getFeralFlareLanternLightCountHardcap());
        if (!decision.runCycle()) {
            return new TickOutcome(decision.ticks(), decision.checkIndex(), false);
        }

        boolean dirty = false;
        BlockPos targetPos = randomTarget(level, origin, config.getFeralFlareRadius());
        targetPos = clampToSurface(level, targetPos);
        targetPos = clampToWorldHeight(level, targetPos);

        if (!canSetBlock(level, targetPos)) {
            return new TickOutcome(0, checkIndex, false);
        }

        if (canPlaceLight(level, targetPos, childLights.size(), config)
                && (!useLineOfSight || hasLineOfSight(level, origin, targetPos))
                && placeLight(level, targetPos)) {
            childLights.add(targetPos);
            dirty = true;
        }

        if (!childLights.isEmpty()) {
            checkIndex = FeralFlareLightPlanner.nextCheckIndex(checkIndex, childLights.size());
            BlockPos childLight = childLights.get(checkIndex);
            BlockState block = level.getBlockState(childLight);
            if (shouldRemoveTrackedChild(childLight, block.getBlock() instanceof InvisibleLightBlock)) {
                childLights.remove(checkIndex);
            }
        }

        return new TickOutcome(0, checkIndex, dirty);
    }

    public static void removeChildLights(World level, List<BlockPos> childLights) {
        for (BlockPos childLight : childLights) {
            if (level.getBlockState(childLight).getBlock() == TorchmasterContent.blockInvisibleLight.get()) {
                level.removeBlock(childLight, false);
            }
        }
        childLights.clear();
    }

    static boolean shouldRemoveTrackedChild(BlockPos childLight, boolean blockStillExists) {
        return FeralFlareLightPlanner.shouldRemoveChildLight(
                childLight == null ? null : MinecraftAdapterViews.blockPos(childLight),
                blockStillExists);
    }

    private static BlockPos randomTarget(World level, BlockPos origin, int radius) {
        int diameter = radius * 2;
        int x = (radius - level.random.nextInt(diameter)) + origin.getX();
        int y = (radius - level.random.nextInt(diameter)) + origin.getY();
        int z = (radius - level.random.nextInt(diameter)) + origin.getZ();
        return new BlockPos(x, y, z);
    }

    private static BlockPos clampToSurface(World level, BlockPos targetPos) {
        //? if >=1.15 {
        int surfaceHeight = level.getTopY(Heightmap.Type.WORLD_SURFACE, targetPos.getX(), targetPos.getZ());
        //?} else {
        /*int surfaceHeight = level.getTop(Heightmap.Type.WORLD_SURFACE, targetPos.getX(), targetPos.getZ());
        *///?}
        if (targetPos.getY() > surfaceHeight + 4) {
            //? if >=1.17 {
            return targetPos.withY(surfaceHeight).up(4);
            //?} else {
            /*return new BlockPos(targetPos.getX(), surfaceHeight + 4, targetPos.getZ());
            *///?}
        }
        return targetPos;
    }

    private static BlockPos clampToWorldHeight(World level, BlockPos targetPos) {
        //? if >=1.16.5 {
        int worldHeightCap = level.getHeight();
        //?} else {
        /*int worldHeightCap = level.getEffectiveHeight();
        *///?}
        if (targetPos.getY() > worldHeightCap) {
            return new BlockPos(targetPos.getX(), worldHeightCap - 1, targetPos.getZ());
        }
        return targetPos;
    }

    private static boolean canSetBlock(World level, BlockPos targetPos) {
        //? if >=1.21.11 {
        /*return level.isInBuildLimit(targetPos);
        *///?} else {
        return level.canSetBlock(targetPos);
        //?}
    }

    private static boolean canPlaceLight(World level, BlockPos targetPos, int childLightCount, ITorchmasterConfig config) {
        return level.isAir(targetPos) && FeralFlareLightPlanner.shouldPlaceLight(
                childLightCount,
                config.getFeralFlareLanternLightCountHardcap(),
                level.getLightLevel(LightType.BLOCK, targetPos),
                config.getFeralFlareMinLightLevel());
    }

    private static boolean placeLight(World level, BlockPos targetPos) {
        //? if >=1.17 {
        return level.setBlockState(targetPos, TorchmasterContent.blockInvisibleLight.get().getDefaultState(), Block.NOTIFY_ALL);
        //?} else {
        /*return level.setBlockState(targetPos, TorchmasterContent.blockInvisibleLight.get().getDefaultState(), 3);
        *///?}
    }

    private static boolean hasLineOfSight(World level, BlockPos origin, BlockPos targetPos) {
        Vec3d start = new Vec3d(targetPos.getX(), targetPos.getY(), targetPos.getZ()).add(0.5, 0.5, 0.5);
        Vec3d end = new Vec3d(origin.getX(), origin.getY(), origin.getZ()).add(0.5, 0.5, 0.5);
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
        BlockHitResult rtResult = level.raycast(rtxCtx);
        //?} else {
        /*BlockHitResult rtResult = level.rayTrace(rtxCtx);
        *///?}

        if (rtResult.getType() == BlockHitResult.Type.BLOCK) {
            BlockPos hitPos = rtResult.getBlockPos();
            return hitPos.getX() == origin.getX() && hitPos.getY() == origin.getY() && hitPos.getZ() == origin.getZ();
        }
        return true;
    }

    private static boolean isClientSide(World level) {
        //? if fabric && forge && >=1.21.9 {
        /*return level.isClientSide();
        *///?} else {
        //? if >=1.21.11
        //return level.isClient()
        //? if <1.21.11
        return level.isClient
        ;
        //?}
    }

    public static final class TickOutcome {
        private final int ticks;
        private final int checkIndex;
        private final boolean dirty;

        public TickOutcome(int ticks, int checkIndex, boolean dirty) {
            this.ticks = ticks;
            this.checkIndex = checkIndex;
            this.dirty = dirty;
        }

        public int ticks() {
            return ticks;
        }

        public int checkIndex() {
            return checkIndex;
        }

        public boolean dirty() {
            return dirty;
        }
    }
}
