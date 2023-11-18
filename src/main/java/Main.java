import factories.ApplicationFactory;

public class Main {
    public static void main(String[] args) {
        ApplicationFactory factory = null; //TODO
        Application application = new Application(factory);
        application.run();
    }
}
