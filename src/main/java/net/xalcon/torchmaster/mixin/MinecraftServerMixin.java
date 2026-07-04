package net.xalcon.torchmaster.mixin;

import net.minecraft.server.MinecraftServer;
import net.xalcon.torchmaster.events.SpawnEventBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin
{
    @Inject(at = @At("TAIL"), method = "tickServer(Ljava/util/function/BooleanSupplier;)V")
    private void torchmaster_tickServer_tail(BooleanSupplier haveTime, CallbackInfo info)
    {
        MinecraftServer server = ((MinecraftServer)(Object)this);
        SpawnEventBridge.onServerLevelTickEnd(server, haveTime);
    }
}
