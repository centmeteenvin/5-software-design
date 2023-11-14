import factories.AbstractApplicationFactory;

public class Main {
    public static void main(String[] args) {
        AbstractApplicationFactory factory = null; //TODO
        Application application = new Application(factory);
        application.run();
    }
}
