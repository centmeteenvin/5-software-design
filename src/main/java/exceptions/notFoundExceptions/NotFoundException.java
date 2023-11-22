package exceptions.notFoundExceptions;

public abstract class NotFoundException extends Exception {
    public final Long id;

    protected NotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public NotFoundException(Long id) {
        this("Model with the following id was not found in the database: %s".formatted(id), id);
    }
}
