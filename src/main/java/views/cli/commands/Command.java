package views.cli.commands;

import views.cli.io.Input;
import views.cli.io.Output;

public abstract class Command {
    public static final String commandString = "";

    protected final String[] args;
    protected final Input input;
    protected final Output output;

    /**
     * @param args the arguments that are given with the command. [0] equals {@link #commandString}.
     */
    public Command(String[] args, Input input, Output output) {
        this.args = args;
        this.input = input;
        this.output = output;
    }

    public Command() {
        this.args = new String[]{};
        this.input = null;
        this.output = null;
    }

    public abstract void execute();
    public abstract String shortDescription();

    public abstract String description();

    public abstract String getCommandString();
}
