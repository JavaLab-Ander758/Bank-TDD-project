package bank;

import org.junit.jupiter.api.*;
import org.mockito.Mock;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferTest {
    private static Transfer transfer;

    @Mock
    private static Account mockAccountFrom;
    @Mock
    private static Account mockAccountTo;
    @Mock
    private static Calendar mockCalendar;

    @Test
    @BeforeAll
    static void init(){
        mockAccountFrom = mock(Account.class);
        mockAccountTo = mock(Account.class);
        mockCalendar = mock(Calendar.class);

        when(mockAccountFrom.getAccountNumber()).thenReturn(419846723);
        when(mockAccountTo.getAccountNumber()).thenReturn(403750477);
        when(mockCalendar.getTime().getTime()).thenReturn(1575380250837L);//Tue Dec 03 2019 14:37:30
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Transfer_TestIfValuesAreCorrect_WithDate{
        @BeforeAll
        void init(){
            transfer = new Transfer(mockAccountFrom, mockAccountTo, 100, mockCalendar);
        }
        @Test
        void GetAccountNumberFrom() {
            assertEquals(419846723, transfer.getAccountFrom().getAccountNumber());
        }
        @Test
        void GetAccountNumberTo() {
            assertEquals(403750477, transfer.getAccountTo().getAccountNumber());
        }
        @Test
        void GetTransferAmmount() {
            assertEquals(100, transfer.getAmount());
        }
        @Test
        void GetTransferDate() {
            assertEquals(1575380250837L, transfer.getDate().getTime().getTime());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Transfer_TestIfValuesAreCorrect {
        @BeforeAll
        void init() {
            transfer = new Transfer(mockAccountFrom, mockAccountTo, 100);
        }
        @Test
        void GetAccountNumberFrom() {
            assertEquals(419846723, transfer.getAccountFrom().getAccountNumber());
        }
        @Test
        void GetAccountNumberTo() {
            assertEquals(403750477, transfer.getAccountTo().getAccountNumber());
        }
        @Test
        void GetTransferAmount() {
            assertEquals(100, transfer.getAmount());
        }
    }
}
