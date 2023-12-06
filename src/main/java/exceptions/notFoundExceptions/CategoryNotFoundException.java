package exceptions.notFoundExceptions;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(Long id) {
        super("Category with the following id was not found in the database: %s".formatted(id), id);
    }
}
