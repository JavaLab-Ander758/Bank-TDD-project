package bank;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Bank {
    private static TransferWriter transferWriter;
    private static TransferReader transferReader;
    private static BufferedWriter bwErrorWriter;

    private static Map<Integer, Account> accounts = new HashMap<>();

    static final String TRANSFER_FILE_NAME = "src/Transfer File.csv";
    static final String ERROR_FILE_NAME = "src/Error Log File.txt";
    private static final int NUMBER_OF_INPUTS_FOR_USER = 6;

    public Bank() {
        try {
            BufferedWriter bwTransferWriter = new BufferedWriter(new FileWriter(Bank.TRANSFER_FILE_NAME, true));
            BufferedReader brTransferReader = new BufferedReader(new FileReader(Bank.TRANSFER_FILE_NAME));
            bwErrorWriter = new BufferedWriter(new FileWriter(Bank.ERROR_FILE_NAME, true));

            transferWriter = new TransferWriter(bwTransferWriter, bwErrorWriter);
            transferReader = new TransferReader(brTransferReader, bwErrorWriter);
        } catch(IOException ex) {
            Error.printFatalErrorMessage(ex);
            System.exit(-1);
        }
    }

    /**
     * Creates a new account and puts it in the HashMap 'accounts'
     * @param accountHolder String for owner of this account
     * @param amount Initial balance for the new account
     * @return boolean stating success of Account creation
     */
    public Boolean createAccount(String accountHolder, double amount) {
        if (checkValidAccountHolderName(accountHolder) && amount >= 0) {
            Account account = new Account(generateAccountNumber(), accountHolder, amount);
            accounts.put(account.getAccountNumber(), account);
            return true;
        } else
            return false;
    }

    /**
     * Generate Transfer object and write it to transfer file
     * @param sourceAccountNumber Account who sends
     * @param destinationAccountNumber Account who receives
     * @param amount Amount to send
     * @return boolean stating if transfer was successful
     */
    public boolean newTransfer(int sourceAccountNumber, int destinationAccountNumber, double amount) {
        if (amount == 0 || amount < 0)
            return false;
        if (sourceAccountNumber == destinationAccountNumber) {
            Error.writeError(bwErrorWriter,"Users are not allowed to make transfers to their own account numbers");
            return false;
        }
        if (checkIfAccountNumberNotExist(sourceAccountNumber) || checkIfAccountNumberNotExist(destinationAccountNumber)) {
            // Write to ERROR_FILE_NAME which accountNumber was non-existent
            Error.writeError(bwErrorWriter, getNonExistentAccountNumbersAsString(sourceAccountNumber, destinationAccountNumber));
            return false;
        }

        Account accountFrom = getAccountByAccountNumber(sourceAccountNumber);
        Account accountTo = getAccountByAccountNumber(destinationAccountNumber);

        assert accountFrom != null;
        if (accountFrom.getAccountBalance() >= amount) {
            Transfer transfer = new Transfer(accountFrom, accountTo, amount);
            // Write transfer to TRANSFER_FILE_NAME and return true for success
            return transferWriter.writeTransfer(transfer);
        } else
            Error.writeError(bwErrorWriter,String.format("Account Number #%s has insufficient funds. Please create new Transfer with lower amount!", accountFrom.getAccountNumber()));
        return false;
    }

    /**
     * Reads all transfers from TRANSFER_FILE_NAME and executes all transfers
     * @return Boolean stating success or not
     */
    public boolean executePendingTransfers() {
        // Fetch all Transfer objects from ERROR_FILE_NAME and put it in a List
        List<Transfer> transfersFromTransferFile = transferReader.readTransfers();
        if (transfersFromTransferFile.isEmpty()) {
            Error.writeError(bwErrorWriter,"No transfers were found in the database! Please add Transfers before continuing!");
            return false;
        }

        // Traverse all Transfers in List
        for (int i = 0; i < transfersFromTransferFile.size(); i++) {
            Transfer transfer       = transfersFromTransferFile.get(i);
            double transferAmount   = transfer.getAmount();
            Account accountFrom     = transfersFromTransferFile.get(i).getAccountFrom();
            Account accountTo       = transfersFromTransferFile.get(i).getAccountTo();

            double accountFromInitialBalance    = transfersFromTransferFile.get(i).getAccountFrom().getAccountBalance();
            double accountToInitialBalance      = transfersFromTransferFile.get(i).getAccountTo().getAccountBalance();

            // Execute transfer
            if (accountFrom.getAccountBalance() >= transferAmount) {
                accounts.get(accountFrom.getAccountNumber()).setAccountBalance(accountFromInitialBalance - transferAmount);
                accounts.get(accountTo.getAccountNumber()).setAccountBalance(accountToInitialBalance + transferAmount);
            } else
                // Write to ERROR_FILE_NAME for insufficiency in funds
                Error.writeError(bwErrorWriter,String.format("Transfer #%s was not executed!! accountNumber %s did not have sufficient funds", i + 1, accountFrom.getAccountNumber()));
            if (i == transfersFromTransferFile.size() - 1) {
                // Transfers executed, truncate file
                truncateTransferFile();
                return true;
            }
        }
        return false;
    }

    /**
     * Return specific Account Object
     * @param accountNumber Find Account by specific id
     * @return Found Account or NULL for no result
     */
    public static Account getAccountByAccountNumber(int accountNumber) {
        if (accounts.containsKey(accountNumber))
            return accounts.get(accountNumber);
        return null;
    }


    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        Bank bank = new Bank();
        if (new File(TRANSFER_FILE_NAME).length() != 0)
            bank.truncateTransferFile();
        System.out.println("\t\t\t\tUiT Bank INC");
        System.out.println("\tCopyright 1998-2019 UiT Incorporation");
        System.out.println("\t\t\t\t -Server 1-\n");
        System.out.println("Terminal manufactured by Andy");

        while (true) {
            try {
                System.out.print(getDefaultMenu());
                int userChoice = input.nextInt();
                printUserInputFields(bank, userChoice);
            } catch (InputMismatchException ex) {
                System.out.println("Invalid input, try again!\n\n");
                input.nextLine();
            }
        }
    }

    /**
     * Prints user choices and fetches input, used to simplify main method
     * @param bank Bank used in the session
     * @param userChoice Users current input to be printed out
     */
    private static void printUserInputFields(Bank bank, int userChoice) {
        Scanner input = new Scanner(System.in);
        switch (userChoice) {
            case 1:
                try {
                    System.out.print("\nName for user (2 or 3 words): ");
                    String accountHolder = input.nextLine();
                    System.out.print("Start balance for user (balance>0): ");
                    double accountBalance = input.nextDouble();
                    if (bank.createAccount(accountHolder, accountBalance))
                        System.out.printf("\t-> Success! Account with name \"%s\" created with an initial balance of %s\n\n", accountHolder, accountBalance);
                    else
                        System.out.print("\t-> Account not created, check your inputs!\n\n");
                } catch (InputMismatchException ex) {
                    System.out.println("Invalid input, Account creation aborted!!\n\n");
                }
                break;
            case 2:
                try {
                    System.out.print("\nAccount number to transfer from: ");
                    int sourceAccountNumber = input.nextInt();
                    System.out.print("Account number to transfer to: ");
                    int destinationAccountNumber = input.nextInt();
                    System.out.print("Amount to transfer: ");
                    double amount = input.nextDouble();
                    if (bank.newTransfer(sourceAccountNumber, destinationAccountNumber, amount))
                        System.out.printf("\t -> Success! A pending transfer of %.2f from #%s to #%s was created.\n\n", amount, sourceAccountNumber, destinationAccountNumber);
                    else
                        System.out.print("\t-> Transfer not made! Please check the Error log for further information!\n\n");
                } catch (InputMismatchException ex) {
                    System.out.println("Invalid input, transfer aborted!!\n\n");
                }
                break;
            case 3:
                System.out.printf("\n|%6s|%6s|%6s|\n", "Account holder", "Number", "Balance");
                for (Account account : bank.getAllAccounts())
                    System.out.printf("|%6s|%6s|%6s|\n", account.getAccountHolder(), account.getAccountNumber(), account.getAccountBalance());
                System.out.printf("%n");
                break;
            case 4:
                if (bank.checkIfTransferFileNotEmpty()) {
                    System.out.println("No");
                    if (bank.executePendingTransfers())
                        System.out.print("All pending transfers executed...\n\n");
                    else
                        System.out.print("Could not execute pending transfers, check Error log!\n\n");
                }
                break;
            case 5:
                if (bank.checkIfTransferFileNotEmpty()) {
                    if (bank.executePendingTransfers()) {
                        System.out.print("All pending transfers executed, exiting program with code 0...");
                        System.exit(0);
                    } else
                        System.out.print("Could not execute pending transfer, check Error log!\n\n");
                }
                break;
            case 6:
                System.out.print("Exiting program without executing Transfers in file...");
                System.exit(0);
                break;
            default:
                System.out.printf("%s is an invalid input, please check your inputs!\n\n", userChoice);
        }
    }

    private static String getDefaultMenu() {
        return "________________________________________________________\n" +
                "Which Bank Entry would you like to access?\n" +
                "1) - Create new account\n" +
                "2) - Transfer between two account numbers\n" +
                "3) - Print all Accounts\n" +
                "4) - Execute all pending transfers\n" +
                "5) - Execute all pending transfers and exit program\n" +
                "6) - Exit program without executing transfers\n" +
                String.format(" --> Enter [%s] -> [%s]: ", 1, Bank.NUMBER_OF_INPUTS_FOR_USER);
    }





    //////////////////////////
    // Helper methods below //
    //////////////////////////

    /**
     * Check if account name is valid
     *      String not empty
     *      String not null
     *      Only 2 or 3 words
     * @param accountHolder String to check requirements
     * @return Boolean stating whether string meets requirements
     */
    static boolean checkValidAccountHolderName(String accountHolder) {
        if (accountHolder == null || accountHolder.isEmpty() || !accountHolder.matches("^[\\p{L} .'-]+$"))
            return false;

        return accountHolder.trim().split(("\\s+")).length > 1 && accountHolder.trim().split(("\\s+")).length < 4;
    }

    /**
     * Generate random unique 9-digit int which starts with 4 and is an even number
     * @return Randomly generated number
     */
    private static int generateAccountNumber() {
        int randomAccountNumber = 400000000 + new Random().nextInt(49999999) * 2;
        if (!accounts.containsKey(randomAccountNumber))
            return randomAccountNumber;
        else
            return generateAccountNumber();
    }

    /**
     * Returns true if given accountNumber does not exist in the accounts HashMap
     * @param accountNumber Account.accountNumber to search for
     * @return boolean stating whether Account.accountNumber exists or not
     */
    private static boolean checkIfAccountNumberNotExist(int accountNumber) {
        return !accounts.containsKey(accountNumber);
    }

    /**
     * Returns which account number(s) is non-existing as a fancy String
     * @param sourceAccountNumber accountNumber which sends
     * @param destinationAccountNumber accountNumber which receives
     * @return The generated String stating which Account.accountNumber !exist
     */
    private String getNonExistentAccountNumbersAsString(int sourceAccountNumber, int destinationAccountNumber) {
        StringBuilder output = new StringBuilder("Account number");
        if (checkIfAccountNumberNotExist(sourceAccountNumber) && checkIfAccountNumberNotExist(destinationAccountNumber))
            output.append(String.format("s #%s and #%s", sourceAccountNumber, destinationAccountNumber));
        else if (checkIfAccountNumberNotExist(sourceAccountNumber))
            output.append(String.format(" #%s", sourceAccountNumber));
        else if (checkIfAccountNumberNotExist(destinationAccountNumber))
            output.append(String.format(" #%s", destinationAccountNumber));
        output.append(" does not exist => Transfer not executed!");
        return output.toString();
    }

    /**
     * Return all Accounts in Bank as list of Accounts
     * @return All Accounts in an array or
     */
    private List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    /**
     * Truncates TRANSFER_FILE_NAME file. To be used when Transfer objects are read
     * or if file is not empty at startup of application
     */
    private void truncateTransferFile() {
        Path path = Paths.get(TRANSFER_FILE_NAME);
        try (FileChannel fc = FileChannel.open(path, StandardOpenOption.WRITE)) {
            fc.truncate(0);
        } catch (IOException e) {
            Error.printFatalErrorMessage(e);
        }
    }

    /**
     * Checks if Transfer file is empty
     * @return Returns True if Transfer file is empty
     */
    private boolean checkIfTransferFileNotEmpty() {
        if (new File(TRANSFER_FILE_NAME).length() == 0) {
            System.out.printf("File \"%s\" was empty, please create transfers before executing!\n\n", TRANSFER_FILE_NAME);
            return false;
        } else
            return true;
    }
}