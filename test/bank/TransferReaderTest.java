package bank;

import java.io.*;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferReaderTest {

    @Test
    void TransferReader_ReadPendingTransfers() throws IOException {
        // not using @Mock and @ExtendWith(MockitoExtension.class) shaves about 100ms of running-time...
        BufferedReader bwTransferReader = mock(BufferedReader.class, RETURNS_DEEP_STUBS);
        when(bwTransferReader.readLine())
                //yyyy-MM-dd HH:mm:ss
                .thenReturn("459283764,465323782,100.00,2019-12-24 13:37:30")
                .thenReturn("491037200,465478946,100.50,2019-12-24 13:37:31")
                .thenReturn("410947322,453674328,1000,2019-12-24 13:37:32")
                .thenReturn("4592a83763,465323785,100.00,2019-12-24 13:37:33")
                .thenReturn("459283763,46532b3785,100.00,2019-12-24 13:37:34")
                .thenReturn("459283763,465323785,1f00.00,2019-12-24 13:37:35")
                .thenReturn("459283763,465323785,100.00,2019-12-24-13:37:36")
                .thenReturn("459283763,465323785,100.00,2019-12-24 63:37:37")
                .thenReturn("4592837633,465323785,100.00,2019-12-24 13:37:37")
                .thenReturn("459283763,465323782,100.00,2019-12-24 13:37:37")
                .thenReturn("45928373,465323782,100.00,2019-12-24 13:37:37")
                .thenReturn(null);

        BufferedWriter bwErrorWriter = new BufferedWriter(new FileWriter(Bank.ERROR_FILE_NAME, true));
        TransferReader transferReader = new TransferReader(bwTransferReader, bwErrorWriter);

        List<Transfer> transfers = transferReader.readTransfers();

        // Mocking an account-object in a transfer-object in the transfer-list would require a lot
        // of code if we are to use the current implementation...
        // A consequence of FDD, and maybe coding before planning, to be sure...
        assertNotNull(transfers);
        assertEquals(100.00, transfers.get(0).getAmount());
        assertEquals(100.50, transfers.get(1).getAmount());
        assertEquals(1000.00, transfers.get(2).getAmount());
        assertEquals(1577191050000L, transfers.get(0).getDate().getTime().getTime());
        assertEquals(1577191051000L, transfers.get(1).getDate().getTime().getTime());
        assertEquals(1577191052000L, transfers.get(2).getDate().getTime().getTime());
    }

    @Nested
    class TestsForAccountNumberFormats
    {
        @ParameterizedTest
        @ValueSource(ints = {323879562, 471094, 423879563, 423879561})
        void isAccountNumber_ReturnsFalseForInvalidAccountNumbers(int accountNumber) {
            TransferReader transferReader = new TransferReader(mock(BufferedReader.class), mock(BufferedWriter.class));
            assertFalse(transferReader.isAccountNumberValid(accountNumber));
        }

        @ParameterizedTest
        @ValueSource(ints = {471091284, 400000000, 412345676})
        void isAccountNumber_ReturnsTrueForValidAccountNumber(int accountNumber) {
            TransferReader transferReader = new TransferReader(mock(BufferedReader.class), mock(BufferedWriter.class));
            assertTrue(transferReader.isAccountNumberValid(accountNumber));
        }
    }
}