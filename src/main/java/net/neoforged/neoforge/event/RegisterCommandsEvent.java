package net.neoforged.neoforge.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

public class RegisterCommandsEvent extends net.neoforged.bus.api.Event {
    public CommandDispatcher<CommandSourceStack> getDispatcher() {
        return new CommandDispatcher<>();
    }

    public CommandBuildContext getBuildContext() {
        return null;
    }
}
