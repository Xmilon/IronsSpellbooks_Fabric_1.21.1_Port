package io.redspace.ironsspellbooks.registries;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.redspace.ironsspellbooks.command.*;
import io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableMenu;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.RegisterCommandsEvent;


@EventBusSubscriber()
public class CommandRegistry {
    public static void bootstrapFabric() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registerCommands(dispatcher, registryAccess));
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        registerCommands(event.getDispatcher(), event.getBuildContext());
    }

    @SuppressWarnings("unchecked")
    private static void registerCommands(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext) {
        if (commandDispatcher == null) {
            return;
        }

        CreateScrollCommand.register(commandDispatcher);
        CreateSpellBookCommand.register(commandDispatcher);
        if (commandBuildContext != null) {
            CreateImbuedSwordCommand.register(commandDispatcher, commandBuildContext);
        }
        CreateDebugWizardCommand.register(commandDispatcher);
        CastCommand.register(commandDispatcher);
        ManaCommand.register(commandDispatcher);
        GenerateModList.register(commandDispatcher);
        LearnCommand.register(commandDispatcher);
        ClearCooldownCommand.register(commandDispatcher);
        ClearRecastsCommand.register(commandDispatcher);
        IronsSpellbooksCommand.register(commandDispatcher);

        if (!FMLLoader.isProduction()) {
            ClearSpellSelectionCommand.register(commandDispatcher);
            GenerateSiteData.register(commandDispatcher);
            commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder)LiteralArgumentBuilder.literal("it")).executes(source->((CommandSourceStack)source.getSource()).getPlayer().openMenu(new SimpleMenuProvider(
                    (i, inventory, player) ->
                            new InscriptionTableMenu(i, inventory, ContainerLevelAccess.NULL), Component.translatable("block.irons_spellbooks.inscription_table")
            )).orElse(0)));
        }
    }
}
