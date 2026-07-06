package net.xalcon.torchmaster.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.ZombieSiegeManager;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.SpawnEventBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * This Mixin provides the onVillageSiege event implementation
 * It hooks the invocation of findRandomSpawnPos() from inside tryToSetupSiege()
 * if findRandomSpawnPos() returns a non-null result, a siege is about to happen
 *
 * This code is untested - how the fuck to we force a village siege?!
 */
@Mixin(ZombieSiegeManager.class)
public abstract class VillageSiegeMixin
{
    @Invoker("getSpawnVector")
    protected abstract Vec3d torchmaster_callFindRandomSpawnPos(ServerWorld level, BlockPos pos);

    @Redirect(
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/village/ZombieSiegeManager;getSpawnVector(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/Vec3d;"
            )
    )
    private Vec3d torchmaster_tryToSetupSiege_findRandomSpawnPos(ZombieSiegeManager siege, ServerWorld level, BlockPos pos)
    {
        Vec3d result = torchmaster_callFindRandomSpawnPos(level, pos);
        if(result != null)
        {
            EventResultContainer container = new EventResultContainer(EventResult.DEFAULT);
            SpawnEventBridge.onVillageSiege(level, result, container);
            if(container.getResult() == EventResult.DENY)
                return null;
        }
        return  result;
    }
}
