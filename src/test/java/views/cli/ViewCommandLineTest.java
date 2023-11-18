package views.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import views.View;
import views.cli.io.Input;
import views.cli.io.Output;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ViewCommandLineTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testExit() {
        Input input = mock(Input.class);
        Output output = mock(Output.class);
        View view = new ViewCommandLine(null, null, null, null,
                null, null, input, output);

        doNothing().when(output).print(any());
        doReturn("exit").when(input).input(any());
        assertTimeout(Duration.ofSeconds(2), view::run);
        verify(input, times(1)).input(any());
    }
}