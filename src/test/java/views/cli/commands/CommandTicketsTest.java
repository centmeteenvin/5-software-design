package views.cli.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTicketsTest extends CommandTest {

    @Test
    void execute() {
    }

    @Override
    public Command getCommand() {
        return new CommandTickets();
    }
}