package net.xalcon.torchmaster.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
//? if >=1.19.3
import net.minecraft.registry.Registries;
//? if <1.19 {
/*import net.minecraft.text.TranslatableText;
*///?}
//? if <1.19.3
//import net.minecraft.util.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
//? if >=1.19
import net.minecraft.text.Text;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.minecraft.storage.MinecraftLightStoreAccess;
import net.xalcon.torchmaster.port.LightInfo;
import net.xalcon.torchmaster.port.EntityTypeKey;

public class CommandTorchmaster
{
    public enum SubCommands
    {
        DUMP_TORCHES("torchdump")
            {
                @Override
                public int execute(CommandContext<ServerCommandSource> ctx)
                {
                    ServerCommandSource source = ctx.getSource();
                    //? if >=1.17.1
                    MinecraftServer server = source.getServer();
                    //? if <1.17.1
                    //MinecraftServer server = source.getMinecraftServer();
                    TorchmasterRuntime.LOG.info("#################################");
                    TorchmasterRuntime.LOG.info("# Torchmaster Torch Dump Start  #");
                    TorchmasterRuntime.LOG.info("#################################");
                    for(ServerWorld level: server.getWorlds())
                    {
                        MinecraftLightStoreAccess.get(level).ifPresent(container ->
                        {
                            TorchmasterRuntime.LOG.info("Torches in dimension {}:",
                                    //? if fabric && forge && >=1.21.11 {
                                    /*level.dimension().identifier()
                                    *///?} else {
                                    //? if >=1.17
                                    level.getRegistryKey().getValue()
                                    //? if <1.17
                                    //"overworld"
                                    //?}
                            );
                            for(LightInfo torch: container.getEntries())
                                TorchmasterRuntime.LOG.info("  {} @ {}/{}/{}", torch.name(), torch.position().x(), torch.position().y(), torch.position().z());
                        });
                    }
                    TorchmasterRuntime.LOG.info("#################################");
                    TorchmasterRuntime.LOG.info("# Torchmaster Torch Dump End    #");
                    TorchmasterRuntime.LOG.info("#################################");

                    //? if >=1.20 {
                    source.sendFeedback(() -> Text.translatable(Constants.MOD_ID + ".command.torch_dump.completed"), false);
//?} elif >=1.19 {
                    /*source.sendFeedback(Text.translatable(Constants.MOD_ID + ".command.torch_dump.completed"), false);
*///?} else {
                    /*source.sendFeedback(new TranslatableText(Constants.MOD_ID + ".command.torch_dump.completed"), false);
                    *///?}
                    return 0;
                }
            },
        DUMP_ENTITIES("entitydump")
            {
                @Override
                public int execute(CommandContext<ServerCommandSource> ctx)
                {
                    ServerCommandSource source = ctx.getSource();
                    TorchmasterRuntime.LOG.info("#################################");
                    TorchmasterRuntime.LOG.info("# Torchmaster Entity Dump Start #");
                    TorchmasterRuntime.LOG.info("#################################");
                    TorchmasterRuntime.LOG.info("List of registered entities:");
                    //? if >=1.19.3 {
                    Registries.ENTITY_TYPE.stream().map(Registries.ENTITY_TYPE::getId).forEach(loc ->
//?} else {
                    /*Registry.ENTITY_TYPE.stream().map(Registry.ENTITY_TYPE::getId).forEach(loc ->
                    *///?}
                            TorchmasterRuntime.LOG.info("  {}", loc));

                    TorchmasterRuntime.LOG.info("Dread Lamp Registry Content:");
                    for(EntityTypeKey loc: TorchmasterRuntime.DreadLampFilterRegistry.getEntities())
                        TorchmasterRuntime.LOG.info("  {}", loc);

                    TorchmasterRuntime.LOG.info("Mega Torch Registry Content:");
                    for(EntityTypeKey loc: TorchmasterRuntime.MegaTorchFilterRegistry.getEntities())
                        TorchmasterRuntime.LOG.info("  {}", loc);
                    TorchmasterRuntime.LOG.info("#################################");
                    TorchmasterRuntime.LOG.info("# Torchmaster Entity Dump End   #");
                    TorchmasterRuntime.LOG.info("#################################");
                    //? if >=1.20 {
                    source.sendFeedback(() -> Text.translatable(Constants.MOD_ID + ".command.entity_dump.completed"), false);
//?} elif >=1.19 {
                    /*source.sendFeedback(Text.translatable(Constants.MOD_ID + ".command.entity_dump.completed"), false);
*///?} else {
                    /*source.sendFeedback(new TranslatableText(Constants.MOD_ID + ".command.entity_dump.completed"), false);
                    *///?}
                    return 0;
                }
            };

        private final String translationKey;

        SubCommands(String translationKey)
        {
            this.translationKey = translationKey;
        }

        public abstract int execute(CommandContext<ServerCommandSource> ctx);

        public String getTranslationKey()
        {
            return translationKey;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        LiteralArgumentBuilder<ServerCommandSource> command = CommandManager.literal("torchmaster");
        for (SubCommands subCommand : SubCommands.values())
        {
            command.then(CommandManager.literal(subCommand.getTranslationKey()).executes(subCommand::execute));
        }

        dispatcher.register(
            //? if >=1.21.11 {
            /*(LiteralArgumentBuilder) command.requires(CommandManager.requirePermissionLevel(CommandManager.ADMINS_CHECK))
            *///?} else {
            (LiteralArgumentBuilder) ((LiteralArgumentBuilder) command.requires((cmdSrc) -> cmdSrc.hasPermissionLevel(2)))
                .executes((ctx) -> 0)
            //?}
        );
    }
}
