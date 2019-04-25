package am.iunetworks;

import am.iunetworks.ecr.client.V04.ECRClient;
import am.iunetworks.ecr.client.V04.ECRSampleClient;
import am.iunetworks.ecr.client.V04.exception.ECRException;
import am.iunetworks.ecr.client.V04.pojo.*;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@RestController("/hdm")
public class HDMResource {

    ECRClient ecrSampleClient;


    @PostMapping("/connect")
    public void connect( @RequestParam("ip") String ip, @RequestParam("port") int port, @RequestParam("password") String password) throws IOException, NoSuchAlgorithmException {
        ecrSampleClient = new ECRSampleClient(ip, port, password);
        ecrSampleClient.connect();
    }

    @GetMapping("/getOperatorsAndDeps")
    @ResponseBody
    public GetOperatorsAndDepsResponse getOperatorsAndDeps() throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException {
        return ecrSampleClient.getOperatorsAndDeps();
    }

    @PostMapping("/printFiscalReport")
    public void printFiscalReport(@RequestParam("reportType") int reportType,
                                  @RequestParam("startDate") Date startDate,
                                  @RequestParam("endDate") Date endDate,
                                  @RequestParam("deptId") Integer deptId,
                                  @RequestParam("cashierId") Integer cashierId,
                                  @RequestParam("transactionTypeId") Integer transactionTypeId) throws Exception {
        ecrSampleClient.printFiscalReport(reportType, startDate, endDate, deptId, cashierId, transactionTypeId);
    }

    @PostMapping("/operatorLogin")
    public OperatorLoginResponse operatorLogin(@RequestParam("cashierId") int cashierId,
                                               @RequestParam("cashierPin") String cashierPin) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException {
        return ecrSampleClient.operatorLogin(cashierId, cashierPin);
    }

    @PostMapping("/printPrePaymentReceipt")
    public PrintReceiptResponse printPrePaymentReceipt(@RequestParam("printMode") int printMode,
                                                       @RequestParam("paidAmount") double paidAmount,
                                                       @RequestParam("paidAmountCard") double paidAmountCard,
                                                       @RequestParam("useExtPOS") boolean useExtPOS) throws Exception {
        return ecrSampleClient.printPrePaymentReceipt(printMode, paidAmount, paidAmount, useExtPOS);
    }

    @PostMapping("/printReceipt")
    public PrintReceiptResponse printReceipt(@RequestParam("printMode") int printMode,
                                             @RequestParam("items") List<Item> items,
                                             @RequestParam("paidAmount") double paidAmount,
                                             @RequestParam("partialAmount") double partialAmount,
                                             @RequestParam("prePaymentAmount") double prePaymentAmount) throws Exception {
        return ecrSampleClient.printReceipt(printMode, items, paidAmount, partialAmount, prePaymentAmount);
    }

    public PrintReceiptResponse printReceipt(int printMode, List<Item> items, double paidAmount, double partialAmount, double prePaymentAmount, double paidAmountCard, boolean useExtPOS) throws Exception {
        return null;
    }


    public GetReceiptResponse getReceipt(String receiptId, String crn) throws Exception {
        return null;
    }


    public void printLastReceipt() throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, ECRException {

    }


    public PrintReceiptResponse printReturnReceipt(PrintReturnReceiptRequest request, int cashierId, int deptId) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException {
        return null;
    }


    public void setupHeaderAndFooter(List<HeaderFooter> headers, List<HeaderFooter> footers) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

    }


    public void setupHeaderLogo(byte[] bitmapBytes) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

    }

    public boolean operatorLogout() throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException {
        return false;
    }

    public PrintTemplateResponse printTemplate() throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return null;
    }

    public void disconnect() throws IOException {

    }
}
