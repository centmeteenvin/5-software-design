package exceptions.notFoundExceptions;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(Long id) {
        super("Person with the following id was not found in the database: %s".formatted(id), id);
    }
}
