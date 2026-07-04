package net.xalcon.torchmaster.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
//? if >=1.19.3 {
import net.minecraft.core.registries.BuiltInRegistries;
//?} else {
/*import net.minecraft.core.Registry;
*///?}
import net.minecraft.network.chat.Component;
//? if <1.19 {
/*import net.minecraft.network.chat.TranslatableComponent;
*///?}
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.port.LightInfo;
import net.xalcon.torchmaster.port.EntityTypeKey;

public class CommandTorchmaster
{
    public enum SubCommands
    {
        DUMP_TORCHES("torchdump")
            {
                @Override
                public int execute(CommandContext<CommandSourceStack> ctx)
                {
                    CommandSourceStack source = ctx.getSource();
                    MinecraftServer server = source.getServer();
                    TorchmasterRuntime.LOG.info("#################################");
                    TorchmasterRuntime.LOG.info("# Torchmaster Torch Dump Start  #");
                    TorchmasterRuntime.LOG.info("#################################");
                    for(ServerLevel level: server.getAllLevels())
                    {
                        TorchmasterRuntime.getRegistryForLevel(level).ifPresent(container ->
                        {
                            TorchmasterRuntime.LOG.info("Torches in dimension {}:",
                                    //? if >=1.21.11 {
                                    /*level.dimension().identifier()
                                    *///?} else {
                                    level.dimension().location()
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
                    source.sendSuccess(() -> Component.translatable(Constants.MOD_ID + ".command.torch_dump.completed"), false);
//?} elif >=1.19 {
                    /*source.sendSuccess(Component.translatable(Constants.MOD_ID + ".command.torch_dump.completed"), false);
*///?} else {
                    /*source.sendSuccess(new TranslatableComponent(Constants.MOD_ID + ".command.torch_dump.completed"), false);
                    *///?}
                    return 0;
                }
            },
        DUMP_ENTITIES("entitydump")
            {
                @Override
                public int execute(CommandContext<CommandSourceStack> ctx)
                {
                    CommandSourceStack source = ctx.getSource();
                    TorchmasterRuntime.LOG.info("#################################");
                    TorchmasterRuntime.LOG.info("# Torchmaster Entity Dump Start #");
                    TorchmasterRuntime.LOG.info("#################################");
                    TorchmasterRuntime.LOG.info("List of registered entities:");
                    //? if >=1.19.3 {
                    BuiltInRegistries.ENTITY_TYPE.stream().map(BuiltInRegistries.ENTITY_TYPE::getKey).forEach(loc ->
//?} else {
                    /*Registry.ENTITY_TYPE.stream().map(Registry.ENTITY_TYPE::getKey).forEach(loc ->
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
                    source.sendSuccess(() -> Component.translatable(Constants.MOD_ID + ".command.entity_dump.completed"), false);
//?} elif >=1.19 {
                    /*source.sendSuccess(Component.translatable(Constants.MOD_ID + ".command.entity_dump.completed"), false);
*///?} else {
                    /*source.sendSuccess(new TranslatableComponent(Constants.MOD_ID + ".command.entity_dump.completed"), false);
                    *///?}
                    return 0;
                }
            };

        private final String translationKey;

        SubCommands(String translationKey)
        {
            this.translationKey = translationKey;
        }

        public abstract int execute(CommandContext<CommandSourceStack> ctx);

        public String getTranslationKey()
        {
            return translationKey;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("torchmaster");
        for (SubCommands subCommand : SubCommands.values())
        {
            command.then(Commands.literal(subCommand.getTranslationKey()).executes(subCommand::execute));
        }

        dispatcher.register(
            //? if >=1.21.11 {
            /*(LiteralArgumentBuilder) command.requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
            *///?} else {
            (LiteralArgumentBuilder) ((LiteralArgumentBuilder) command.requires((cmdSrc) -> cmdSrc.hasPermission(2)))
                .executes((ctx) -> 0)
            //?}
        );
    }
}
