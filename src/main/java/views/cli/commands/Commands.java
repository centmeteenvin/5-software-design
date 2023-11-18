package views.cli.commands;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum Commands {
    HELP(CommandHelp.commandString, CommandHelp.class);

    Commands(String commandLineString, Class<? extends Command> commandClass) {
        this.commandString = commandLineString;
        this.commandClass = commandClass;
    }

    /**
     * Parses the commands based on the first argument
     * @param args the arguments split on the space
     * @return returns the specified command, empty if not present.
     */
    @SneakyThrows
    public static Optional<Command> parse(String[] args) {
        String commandString = args[0];
        Optional<Commands> result = Arrays.stream(Commands.values()).filter(commands -> Objects.equals(commands.commandString, commandString)).findFirst();
        if (result.isEmpty()) return Optional.empty();
        Class<? extends Command> commandClass = result.get().commandClass;
        Constructor<? extends Command> constructor = commandClass.getConstructor(String[].class);
        return Optional.of(constructor.newInstance((Object) args));
    }

    private final String commandString;
    private final Class<? extends Command> commandClass;
}
