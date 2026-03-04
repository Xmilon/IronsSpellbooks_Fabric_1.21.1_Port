package net.neoforged.neoforge.server.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

public class EnumArgument<T extends Enum<T>> implements ArgumentType<T> {
    private static final DynamicCommandExceptionType INVALID_VALUE =
            new DynamicCommandExceptionType(value -> Component.literal("Invalid enum value: " + value));

    private final Class<T> enumClass;

    private EnumArgument(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    public static <T extends Enum<T>> EnumArgument<T> enumArgument(Class<T> enumClass) {
        return new EnumArgument<>(enumClass);
    }

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        String token = reader.readUnquotedString();
        for (T constant : enumClass.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(token)) {
                return constant;
            }
        }
        throw INVALID_VALUE.create(token);
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(e -> e.name().toLowerCase(Locale.ROOT))
                .collect(Collectors.toList());
    }

    public static <T extends Enum<T>> T getEnum(CommandContext<?> context, String name, Class<T> enumClass) {
        return context.getArgument(name, enumClass);
    }
}
