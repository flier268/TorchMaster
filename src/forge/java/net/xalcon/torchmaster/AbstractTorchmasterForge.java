package net.xalcon.torchmaster;

import net.minecraftforge.common.MinecraftForge;
//? if >=1.16 {
import net.minecraftforge.event.RegisterCommandsEvent;
//?} else {
/*import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
*///?}
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

    //? if >=1.16 {
    private static void onRegisterCommands(RegisterCommandsEvent event)
    {
        CommandTorchmaster.register(event.getDispatcher());
    }
//?} else {
    /*private static void onRegisterCommands(FMLServerStartingEvent event)
    {
        CommandTorchmaster.register(event.getCommandDispatcher());
    }
    *///?}
}
