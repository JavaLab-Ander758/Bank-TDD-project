package bank;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TransferWriter {
    private BufferedWriter transferWriter;
    private BufferedWriter errorWriter;

    /**
     * Constructs a TransferWriter with supplied BufferedWriter Objects
     * @param transferWriter BufferedWriter-object used to write transfers to file
     * @param errorWriter BufferedTransfer-object used to write to error-log
     */
    public TransferWriter(BufferedWriter transferWriter, BufferedWriter errorWriter){
        this.transferWriter = transferWriter;
        this.errorWriter = errorWriter;
    }

    /**
     * Writes a Transfer Object to the transfer file
     * Each line in file contains Transfer Objects Instance Variables with ',' as delimiter:
     *                 accountFrom,accountTo,amount,date
     *                 e.g.: 423879562,471091284,142.45,2019-10-17 12:34:12
     * @param transfer Transfer Object which gets written to a File
     * @return boolean stating if transfer was successfully written
     */
    public boolean writeTransfer(Transfer transfer){
        // Generate String containing Transfer Object
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transfer.getDate().getTime());
        String transferString = String.format("%s,%s,%.2f,%s\n", transfer.getAccountFrom().getAccountNumber(), transfer.getAccountTo().getAccountNumber(), transfer.getAmount(), time);

        // Write transfer to file
        try {
            // Write transferString to file
            transferWriter.write(transferString);
            transferWriter.flush();

            // Return true if Transfer was successfully written to file
            return true;
        } catch (IOException ex) {
            Error.writeError(errorWriter, ex);
        }
        return false;
    }
}
