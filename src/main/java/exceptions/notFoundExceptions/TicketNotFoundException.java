package exceptions.notFoundExceptions;

public class TicketNotFoundException extends NotFoundException {
    public TicketNotFoundException(Long id) {
        super("Person with the following id was not found in the database: %s".formatted(id), id);
    }
}
