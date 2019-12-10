package bank;

public class Account {
  private Integer accountNumber;
  private String accountHolder;
  private Double accountBalance;

  public Account(Integer accountNumber, String accountHolder, Double accountBalance) {
    this.accountNumber = accountNumber;
    this.accountHolder = accountHolder;
    this.accountBalance = accountBalance;
  }


  /**
   * Withdraws a given amount from the Account if sufficient funds
   * @param amount Amount to withdraw as double
   * @return Boolean stating success of withdrawal
   */
  public boolean withdraw(double amount) {
    if (amount <= 0)
      return false;

    if (amount <= accountBalance) {
      accountBalance -= amount;
      return true;
    } else
      return false;
  }

  /**
   * Deposits a given amount from the Account
   * @param amount Amount to deposit as double
   * @return Boolean stating success of deposit
   */
  public boolean deposit(double amount) {
    if (amount > 0) {
      accountBalance += amount;
      return true;
    } else
      return false;
  }



  public Integer getAccountNumber() {
    return accountNumber;
  }

  public String getAccountHolder() {
    return accountHolder;
  }

  public Double getAccountBalance() {
    return accountBalance;
  }

  public void setAccountBalance(Double accountBalance) {
    this.accountBalance = accountBalance;
  }
}
