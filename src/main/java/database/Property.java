package database;

public enum Property {
    CREATE("Model Creation"), UPDATE("Model Modification"), DELETE("Model Deletion");

    public final String name;

    Property(String name) {
        this.name = name;
    }
}
