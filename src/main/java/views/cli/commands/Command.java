package views.cli.commands;

public abstract class Command {
    public static final String commandString = "";

    protected final String[] args;

    /**
     * @param args the arguments that are given with the command. [0] equals {@link #commandString}.
     */
    public Command(String[] args) {
        this.args = args;
    }

    public abstract void execute();
}
