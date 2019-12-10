package bank;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransferReader {
    private BufferedReader transferReader;
    private BufferedWriter errorWriter;


    /**
     * Constructs a TransferWriter with supplied BufferedWriter Objects
     * @param transferReader BufferedWriter-object used to write transfers to file
     * @param errorWriter BufferedTransfer-object used to write to error-log
     */
    public TransferReader(BufferedReader transferReader, BufferedWriter errorWriter){
        this.transferReader = transferReader;
        this.errorWriter = errorWriter;
    }

    /**
     * Read all Transfer Objects from the transfer file
     * @return All transfers in the file as an ArrayList containing transfers
     */
    public List<Transfer> readTransfers() {
        List<Transfer> pendingTransfers = new ArrayList<>();

        // With some help from: https://www.tutorialkart.com/java/read-contents-file-line-line-using-bufferedreader-java/
        String line;
        try {
            // Read all lines in TRANSFER_FILE_NAME
            while ((line = transferReader.readLine()) != null) {
                try {
                    // Fetch all Accounts
                    int accountFrom = Integer.parseInt(line.split(",")[0]);
                    if(!isAccountNumberValid(accountFrom))
                        throw new Exception(String.format("Error parsing transaction %s: Invalid Account number from", line));

                    int accountTo = Integer.parseInt(line.split(",")[1]);
                    if(!isAccountNumberValid(accountTo))
                        throw new Exception(String.format("Error parsing transaction %s: Invalid Account number to", line));

                    double amount = Double.parseDouble(line.split(",")[2]);

                    //String date = line.split(",")[3];
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    calendar.setTime(simpleDateFormat.parse(line.split(",")[3]));

                    // Create new Transfer object with its 3rd. constructor and add it to ArrayList 'pendingTransfers'
                    pendingTransfers.add(new Transfer(Bank.getAccountByAccountNumber(accountFrom), Bank.getAccountByAccountNumber(accountTo), amount, calendar));
                } catch(Exception ex){
                    Error.writeError(errorWriter, ex);
                }
            }
        } catch (IOException ex) {
            Error.writeError(errorWriter, ex);
        }
        return pendingTransfers;
    }

    /**
     * Checks if Account numbre is valid
     * @param accountNo Account number to check
     * @return Returns true if Account number is valid
     */
    boolean isAccountNumberValid(int accountNo) {
        return ((int)(Math.log10(accountNo)+1) == 9 && Integer.parseInt(Integer.toString(accountNo).substring(0, 1)) == 4 && accountNo % 2 == 0);
    }
}