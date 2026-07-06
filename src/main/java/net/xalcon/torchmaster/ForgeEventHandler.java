package net.xalcon.torchmaster;

//? if forge && >=1.19.4 {
/*import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.minecraft.spawn.MinecraftSpawnEventContainers;
import net.xalcon.torchmaster.minecraft.spawn.MinecraftSpawnEventHooks;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ForgeEventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event)
    {
        EventResultContainer container = MinecraftSpawnEventContainers.defaultContainer();

        MinecraftSpawnEventHooks.onCheckSpawn(
                event.getSpawnType(),
                event.getEntity(),
                new Vec3d(event.getX(), event.getY(), event.getZ()),
                container);

        ForgeSpawnEventResults.applyFinalizeSpawnResult(event, container);
    }
}
*///?} else if forge {
/*import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.minecraft.spawn.MinecraftSpawnEventHooks;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ForgeEventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        EventResultContainer container = ForgeSpawnEventResults.container(event.getResult());

        MinecraftSpawnEventHooks.onCheckSpawn(
                event.getSpawnReason(),
                event.getEntity(),
                new Vec3d(event.getX(), event.getY(), event.getZ()),
                container);

        event.setResult(ForgeSpawnEventResults.toForgeResult(container));
    }
}
*///?}
