package views.cli.commands;

import views.cli.ViewCommandLine;
import views.cli.io.Input;
import views.cli.io.Output;

public abstract class Command {
    public static final String commandString = "";

    protected final String[] args;
    protected final ViewCommandLine view;

    /**
     * @param args the arguments that are given with the command. [0] equals {@link #commandString}.
     */
    public Command(String[] args, ViewCommandLine view) {
        this.args = args;
        this.view = view;
    }

    public Command() {
        this.args = new String[]{};
        this.view = null;
    }

    public abstract void execute();
    public abstract String shortDescription();

    public abstract String description();

    public abstract String getCommandString();

    public static String incorrectNumberOfArguments(int expected, int actual) {
        return """
                ! Incorrect number of arguments
                ! Expected %s but received %s
                """.formatted(expected, actual);
    }
}
