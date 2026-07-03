package net.xalcon.torchmaster;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.xalcon.torchmaster.commands.CommandTorchmaster;

public class TorchmasterFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Torchmaster.init();

        ServerWorldEvents.LOAD.register((server, world) ->
        {
            Torchmaster.onWorldLoaded();
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CommandTorchmaster.register(dispatcher));
    }


}
