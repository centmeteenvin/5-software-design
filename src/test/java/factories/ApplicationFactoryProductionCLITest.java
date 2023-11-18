package factories;

import org.junit.jupiter.api.Test;
import views.ViewCommandLine;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationFactoryProductionCLITest extends ApplicationFactoryProductionTest {

    @Test
    void createView() {
        ApplicationFactory factory = new ApplicationFactoryProductionCLI();
        assertTrue(factory.createView(null, null, null, null, null, null) instanceof ViewCommandLine);
    }
}