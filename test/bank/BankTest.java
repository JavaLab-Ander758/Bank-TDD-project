package bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    private Bank bank;

    @BeforeEach
    void beforeEach() {
        bank = new Bank();
    }

    @Nested
    class createAccount {
        @Nested
        class InvalidInputTestsReturningFalse {
            @Test
            void returnsFalseIfInput_accountHolder_Null() {
                assertFalse(bank.createAccount(null, 5));
            }
            @Test
            void returnsFalseIfInput_accountHolder_Empty() {
                assertFalse(bank.createAccount("", 5));
            }
            @Test
            void returnsFalseIfInput_accountHolder_OneWord() {
                assertFalse(bank.createAccount("John", 5));
            }
            @Test
            void returnsFalseIfInput_accountHolder_FourWords() {
                assertFalse(bank.createAccount("John Appleseed Appleseed Appleseed", 5));
            }
            @ParameterizedTest
            @CsvFileSource(resources = "IllegalCharacters.csv", numLinesToSkip = 2, delimiter = '\n')
            void returnsFalseIfInput_accountHolder_ContainsIllegalCharacters(String accountHolder) {
                assertFalse(bank.createAccount(accountHolder, 5));
            }
            @Test
            void returnsFalseIfInput_amount_Negative() {
                assertFalse(bank.createAccount("John Appleseed", -1));
            }
        }

        @Nested
        class ValidInputTestsReturningTrue {
            @Test
            void returnsTrueIfInput_accountHolder_TwoWords() {
                assertTrue(bank.createAccount("John Appleseed", 5));
            }
            @Test
            void returnsTrueIfInput_accountHolder_ThreeWords() {
                assertTrue(bank.createAccount("John Appleseed Appleseed", 5));
            }
            @Test
            void returnsTrueIfInput_accountHolder_WordWithHyphen() {
                assertTrue(bank.createAccount("John-Carter Appleseed", 5));
            }
            @Test
            void returnsTrueIfInput_AccountHolder_WordWithApostrophe() {
                assertTrue(bank.createAccount("Jimmy Kellogg's", 5));
            }
            @Test
            void returnsTrueIfInput_amount_Zero() {
                assertTrue(bank.createAccount("John Appleseed", 0));
            }
            @Test
            void returnsTrueIfInput_amount_Positive() {
                assertTrue(bank.createAccount("John Appleseed", 10));
            }
        }
    }


    @Test
    void getAccountByID_ReturnsNullIfAccountNumberNotExistInHashMap() {
        assertNull(Bank.getAccountByAccountNumber(212345678));
    }
}