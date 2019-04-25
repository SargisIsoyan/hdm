package am.iunetworks.ecr.client.V04;

import am.iunetworks.ecr.client.V04.exception.ECRException;
import am.iunetworks.ecr.client.V04.pojo.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

public interface ECRClient {
    void connect() throws IOException;

    GetOperatorsAndDepsResponse getOperatorsAndDeps() throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException;

    void printFiscalReport(int reportType, Date startDate, Date endDate, Integer deptId, Integer cashierId, Integer transactionTypeId) throws Exception;

    OperatorLoginResponse operatorLogin(int cashierId, String cashierPin) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException;

    PrintReceiptResponse printPrePaymentReceipt(int printMode, double paidAmount, double paidAmountCard, boolean useExtPOS) throws Exception;

    PrintReceiptResponse printReceipt(int printMode, List<Item> items, double paidAmount, double partialAmount, double prePaymentAmount) throws Exception;

    PrintReceiptResponse printReceipt(int printMode, List<Item> items, double paidAmount, double partialAmount, double prePaymentAmount, double paidAmountCard, boolean useExtPOS) throws Exception;

    GetReceiptResponse getReceipt(String receiptId, String crn) throws Exception;

    void printLastReceipt() throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, ECRException;

    PrintReceiptResponse printReturnReceipt(PrintReturnReceiptRequest request, int cashierId, int deptId) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException;

    void setupHeaderAndFooter(List<HeaderFooter> headers, List<HeaderFooter> footers) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException;

    void setupHeaderLogo(byte[] bitmapBytes) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException;

    boolean operatorLogout() throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException;

    PrintTemplateResponse printTemplate() throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException;

    void disconnect() throws IOException;
}
