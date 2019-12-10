package bank;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class TransferWriterTest {

    private TransferWriter transferWriter;
    private static Transfer mockTransfer;

    @BeforeAll
    static void init(){
        mockTransfer = mock(Transfer.class, RETURNS_DEEP_STUBS);

        when(mockTransfer.getAccountFrom().getAccountNumber()).thenReturn(488888888);
        when(mockTransfer.getAccountTo().getAccountNumber()).thenReturn(466666666);
        when(mockTransfer.getAmount()).thenReturn(1000.00);
        when(mockTransfer.getDate().getTime().getTime()).thenReturn(1574879186095L); //2019-11-27 19:26:26
    }

    @Test
    void writeTransfer_CreatesCSVFile() throws IOException {
        BufferedWriter bwTransferWriter = new BufferedWriter(new FileWriter(Bank.TRANSFER_FILE_NAME, true));
        BufferedWriter bwErrorWriter = new BufferedWriter(new FileWriter(Bank.ERROR_FILE_NAME, true));

        transferWriter = new TransferWriter(bwTransferWriter, bwErrorWriter);
        transferWriter.writeTransfer(mockTransfer);

        assertTrue(new File(Bank.TRANSFER_FILE_NAME).exists());
    }

    @Test
    void writeTransfer_WritesCorrectData() throws IOException {
        BufferedWriter mockTransferWriter = mock(BufferedWriter.class);
        BufferedWriter mockErrorWriter = mock(BufferedWriter.class);

        transferWriter = new TransferWriter(mockTransferWriter, mockErrorWriter);
        transferWriter.writeTransfer(mockTransfer);

        verify(mockTransferWriter).write("488888888,466666666,1000.00,2019-11-27 19:26:26\n");
    }

    @AfterAll
    static void removeFile() {
        File file = new File(Bank.TRANSFER_FILE_NAME);
        file.delete();
    }
}