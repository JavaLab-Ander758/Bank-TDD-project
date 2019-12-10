package bank;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ErrorTest {

    private static BufferedWriter errorWriter;

    @BeforeAll
    static void init(){
        // Using @Mock is insufficient, as it does not mock further dependencies, whereas mock() can
        errorWriter = mock(BufferedWriter.class, RETURNS_DEEP_STUBS);
    }

    @Test
    void error_TestGetErrorMessage_WithException() {
        String err = Error.getErrorMessage(new Exception("SomeFault"));
        assertThat(err.contains("Error"), is(true));
        assertThat(err.contains("SomeFault"), is(true));
    }

    @Test
    void error_TestGetErrorMessage_WithReason() {
        String err = Error.getErrorMessage("SomeOtherFault");
        assertThat(err.contains("Error"), is(true));
        assertThat(err.contains("SomeOtherFault"), is(true));
    }

    @Test
    void error_TestWriteErrorToFile() throws IOException {
        assertThat(Error.writeErrorToFile(errorWriter, "SomeReason"), is(true));
        verify(errorWriter).write("SomeReason");
    }

    /*// Difficult to test, and only prints to the default error-stream anyway(usually console)...
    @Test
    void printFatalErrorMessage() {
        Error.printFatalErrorMessage(new Exception("SomeFatalException"));
    }
    /**/

    // writeError calls writeErrorToFile, supplying the result of getErrorMessage
      // depending on whether it was supplied with a generic reason or an exception.
      // This test would depend on Error.writeErrorToFile() and Error.getErrorMessage,
      // and would never return the "exact" same message since it uses time-stamping.
      // Thus, we see no need to actually test this, since anything that would make this
      // test fail have already been tested for in previous tests...
    @Test
    void error_TestWriteError_WithException() throws IOException {
        assertThat(Error.writeError(errorWriter, new Exception("SomeException")), is(true));
    }
    /**/

    // writeError calls writeErrorToFile, supplying the result of getErrorMessage
      // depending on whether it was supplied with a generic reason or an exception.
      // This test would depend on Error.writeErrorToFile() and Error.getErrorMessage,
      // and would never return the "exact" same message since it uses time-stamping.
      // Thus, we see no need to actually test this, since anything that would make this
      // test fail have already been tested for in previous tests...
    @Test
    void error_TestWriteError_WithReason() {
        assertThat(Error.writeError(errorWriter, "SomeReason"), is(true));
    }
    /**/

    // Do we reeaaaaally need to test this?
    @Test
    void error_TestGetTime() {
        assertNotNull(Error.getTime());
    }
     /**/
}