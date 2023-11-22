package exceptions.notFoundExceptions;

public class PersonNotFoundException extends NotFoundException {
    public PersonNotFoundException(Long id) {
        super("Person with the following id was not found in the database: %s".formatted(id), id);
    }
}
