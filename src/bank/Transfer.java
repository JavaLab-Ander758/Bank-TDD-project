package bank;

import java.util.Calendar;

public class Transfer {
    private Account accountFrom;
    private Account accountTo;
    private double amount;
    private Calendar date;

    public Transfer(Account accountFrom, Account accountTo, double amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.date = Calendar.getInstance();
    }

    public Transfer(Account accountFrom, Account accountTo, double amount, Calendar date) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.date = date;
    }

    Account getAccountFrom() {
        return accountFrom;
    }

    Account getAccountTo() {
        return accountTo;
    }

    double getAmount() {
        return amount;
    }

    Calendar getDate() {
        return date;
    }
}
