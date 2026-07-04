package net.xalcon.torchmaster;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.xalcon.torchmaster.commands.CommandTorchmaster;

public abstract class AbstractTorchmasterForge
{
    protected AbstractTorchmasterForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.addListener(AbstractTorchmasterForge::onRegisterCommands);

        TorchmasterRuntime.init();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // In forge, rendertype is configured via the model.json.
    }

    private static void onRegisterCommands(RegisterCommandsEvent event)
    {
        CommandTorchmaster.register(event.getDispatcher());
    }
}
