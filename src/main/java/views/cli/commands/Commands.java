package views.cli.commands;

import lombok.SneakyThrows;
import views.cli.ViewCommandLine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum Commands {
    CATEGORIES(CommandCategories.commandString, CommandCategories.class),
    CATEGORY(CommandCategory.commandString, CommandCategory.class),
    HELP(CommandHelp.commandString, CommandHelp.class),
    PERSON(CommandPerson.commandString, CommandPerson.class),
    PERSONS(CommandPersons.commandString, CommandPersons.class),
    TICKETS(CommandTickets.commandString, CommandTickets.class),
    ;

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
    public static Optional<Command> parse(String[] args, ViewCommandLine view) {
        String commandString = args[0];
        Optional<Commands> result = Arrays.stream(Commands.values()).filter(commands -> Objects.equals(commands.commandString, commandString)).findFirst();
        if (result.isEmpty()) return Optional.empty();
        Class<? extends Command> commandClass = result.get().commandClass;
        Constructor<? extends Command> constructor = commandClass.getConstructor(String[].class, ViewCommandLine.class);
        return Optional.of(constructor.newInstance((Object) args, view));
    }

    public static Command[] getAllCommands() {
        return Arrays.stream(Commands.values()).map(command -> {
            try {
                return command.commandClass.getConstructor().newInstance();
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).toArray(Command[]::new);
    }

    final String commandString;
    final Class<? extends Command> commandClass;
}
