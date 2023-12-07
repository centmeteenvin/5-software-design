import factories.ApplicationFactory;
import factories.ApplicationFactoryProductionCLI;
import factories.ApplicationFactoryProductionGUI;

public class Main {
    public static void main(String[] args) {

        ApplicationFactory factory;
        if (args.length == 0) {
            factory = new ApplicationFactoryProductionGUI();
        } else {
            switch (args[0]) {
                case "cli" -> factory = new ApplicationFactoryProductionCLI();
                case "gui" -> factory = new ApplicationFactoryProductionGUI();
                default -> throw new IllegalArgumentException("Unexpected arguments where given, valid options are: [cli, gui]");
            }
        }
        Application application = new Application(factory);
        application.run();
    }
}
