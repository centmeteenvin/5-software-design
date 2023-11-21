import factories.ApplicationFactory;
import factories.ApplicationFactoryProductionCLI;
import factories.ApplicationFactoryProductionGUI;

public class Main {
    public static void main(String[] args) {
        ApplicationFactory factory = new ApplicationFactoryProductionGUI();
        Application application = new Application(factory);
        application.run();
    }
}
