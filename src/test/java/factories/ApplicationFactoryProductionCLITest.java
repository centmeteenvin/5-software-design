package factories;

import org.junit.jupiter.api.Test;
import views.cli.ViewCommandLine;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationFactoryProductionCLITest extends ApplicationFactoryProductionTest {

    @Test
    void createView() {
        ApplicationFactory factory = getFactory();
        assertTrue(factory.createView(null, null, null, null, null, null) instanceof ViewCommandLine);
    }

    @Override
    protected ApplicationFactory getFactory() {
        return new ApplicationFactoryProductionCLI();
    }
}