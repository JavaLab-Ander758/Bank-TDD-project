package bank;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Error {

    /**
     * Prepares an error-message based on received exception.
     * @param exception Exception used to generate an error-message
     * @return String containing prepared error-message
     */
    static String getErrorMessage(Exception exception) {
        // Gather some error-data for current message
        StackTraceElement[] trace = exception.getStackTrace();
        String className = trace[0].getClassName();
        String methodName = trace[0].getMethodName();
        String reason = exception.getMessage();

        //Format and return error-string
        return String.format("Error%n --> Time of occurrence: %s%n --> %s !%n --> Reason: Exception in method %s in %s.java %n --> Thrown exception: %s%n%n",
                getTime(), reason, methodName, className, exception);
    }

    /**
     * Prepares an error-message based on received exception.
     * @param reason String containg a manually crafted reason used to generate an error-message
     * @return String containing prepared error-message
     */
    static String getErrorMessage(String reason){
        // Gather some error-data for current message
        StackTraceElement[] trace = new Exception().getStackTrace();
        String className = trace[1].getClassName();
        String methodName = trace[1].getMethodName();

        // Format and return error-string
        return String.format("Error%n --> Time of occurrence: %s%n --> Reason: %s in method %s in %s.java%n%n",
                getTime(), reason, methodName, className);
    }

    /**
     * Prints a fatal error-message based on received exception.
     * @param exception Exception used to generate fatal error-message
     */
    static void printFatalErrorMessage(Exception exception) {
        // Gather some error-data for fatal error message
        StackTraceElement[] trace = exception.getStackTrace();
        String className = trace[0].getClassName();
        String methodName = trace[0].getMethodName();

        // Format and print fatal error
        System.err.format("FATAL ERROR!%n --> Time of occurrence: %s%n --> Could not write to \"%s\" !%n --> Reason: Exception in method %s in %s.java%n --> Thrown exception: %s%n%n",
                getTime(), Bank.ERROR_FILE_NAME, methodName, className, exception);
    }

    /**
     * Writes an entry to the error-log based on supplied exception.
     * Use this for exception-errors.
     * @param errorWriter BufferedWriter used to write to error-file
     * @param exception Exception used to generate error-entry
     * @return True if successful, false if it failed
     */
    static boolean writeError(BufferedWriter errorWriter, Exception exception) {
        return writeErrorToFile(errorWriter, getErrorMessage(exception));
    }

    /**
     * Writes an entry to the error-log based on supplied message.
     * Use this for custom/non-exception errors.
     * @param errorWriter BufferedWriter used to write to error-file
     * @param reason Reason used to generate error-entry
     * @return True if successful, false if it failed
     */
    static boolean writeError(BufferedWriter errorWriter, String reason){
        return writeErrorToFile(errorWriter, getErrorMessage(reason));
    }

    /**
     * Writes a generated error-message to file.
     * @param errorWriter BufferedWriter used to write to error-file
     * @param error Errormessage to write
     * @return True if successful, false if it failed
     */
    static boolean writeErrorToFile(BufferedWriter errorWriter, String error) {
        try {
            // Write error-message to file
            errorWriter.write(error);
            errorWriter.flush();
        } catch (IOException ex) {
            // If we fail, write directly to standard error-stream
            printFatalErrorMessage(ex);
            return false;
        }
        return true;
    }

    /**
     * Helper-function: gets current time.
     * @return String containg current time in "yyyy-MM-dd HH:mm:ss:SSS"-format
     */
    static String getTime(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Calendar.getInstance().getTime());
    }
}