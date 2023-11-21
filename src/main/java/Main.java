import factories.ApplicationFactory;
import factories.ApplicationFactoryProductionCLI;

public class Main {
    public static void main(String[] args) {
        ApplicationFactory factory = new ApplicationFactoryProductionCLI();
        Application application = new Application(factory);
        application.run();
    }
}
