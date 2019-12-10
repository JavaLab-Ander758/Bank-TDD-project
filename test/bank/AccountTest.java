package bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;


import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private Account account;

    @BeforeEach
    void beforeEach() {
        account = new Account(484739470, "John Appleseed", 2000.0);
    }

    @Nested
    class Withdraw {
        @Nested
        class InvalidInputTestsReturningFalse {
            @Test
            void returnsFalseIfInput_amount_Negative() {
                assertFalse(account.withdraw(-0.1));
            }

            @Test
            void returnsFalseIfInput_amount_Zero() {
                assertFalse(account.withdraw(0));
            }

            @Test
            void returnsFalseIfInput_amount_MoreThanBalance() {
                assertFalse(account.withdraw(2000.1));
            }
        }

        @Nested
        class ValidInputTestsReturningTrue {
            @Test
            void returnsTrueIfInput_amount_BelowBalance() {
                assertTrue(account.withdraw(account.getAccountBalance() - 1));
            }

            @Test
            void returnsTrueIfInput_amount_ExactlyLikeBalance() {
                assertTrue(account.withdraw(account.getAccountBalance()));
            }
        }

        @Test
        void balanceDecreasesForSuccessfulWithdraw() {
            Double initialBalance = account.getAccountBalance();
            Double withdrawAmount = 1000.0;
            account.withdraw(withdrawAmount);

            assertEquals(initialBalance - withdrawAmount, account.getAccountBalance());
        }
    }

    @Nested
    class Deposit {
        @Nested
        class InvalidInputTestsReturningFalse {
            @Test
            void returnsFalseIfInput_amount_Negative() {
                assertFalse(account.deposit(-0.1));
            }

            @Test
            void returnsFalseIfInput_amount_Zero() {
                assertFalse(account.deposit(0));
            }
        }

        @Nested
        class validInputTestsReturningTrue {
            @Test
            void returnsTrueIfInput_amount_Positive() {
                assertTrue(account.deposit(0.1));
            }
        }

        @Test
        void deposit_BalanceIncreasesForSuccessfulDeposit() {
            Double initialBalance = account.getAccountBalance();
            Double depositAmount = 1000.0;
            account.deposit(depositAmount);

            assertEquals(initialBalance + depositAmount, account.getAccountBalance());
        }
    }

    @Nested
    class AccountData{
        @Test
        void account_testGetAccountNumber(){
            assertEquals(484739470, account.getAccountNumber());
        }

        @Test
        void account_testGetAccountHolder(){
            assertEquals("John Appleseed", account.getAccountHolder());
        }

        @Test
        void account_testGetAccountBalance(){
            assertEquals(2000.0, account.getAccountBalance());
        }

        @Test
        void account_testSetAccountBalance(){
            account.setAccountBalance(2500.0);
            assertEquals(2500.0, account.getAccountBalance());
        }
    }
}
