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

    private ECRClient ecrSampleClient;


    @PostMapping("/connect")
    @ResponseBody
    public Response<Object> connect(@RequestParam("ip") String ip, @RequestParam("port") int port, @RequestParam("password") String password) throws IOException, NoSuchAlgorithmException {
        ecrSampleClient = new ECRSampleClient(ip, port, password);
        ecrSampleClient.connect();
        return new Response<>(200, null);
    }

    @GetMapping("/getOperatorsAndDeps")
    @ResponseBody
    public GetOperatorsAndDepsResponse getOperatorsAndDeps() throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException {
        return ecrSampleClient.getOperatorsAndDeps();
    }

    @PostMapping("/printFiscalReport")
    @ResponseBody
    public Response<Object> printFiscalReport(@RequestParam("reportType") int reportType,
                                              @RequestParam("startDate") Date startDate,
                                              @RequestParam("endDate") Date endDate,
                                              @RequestParam("deptId") Integer deptId,
                                              @RequestParam("cashierId") Integer cashierId,
                                              @RequestParam("transactionTypeId") Integer transactionTypeId) throws Exception {
        ecrSampleClient.printFiscalReport(reportType, startDate, endDate, deptId, cashierId, transactionTypeId);
        return new Response<>(200, null);
    }

    @PostMapping("/operatorLogin")
    @ResponseBody
    public OperatorLoginResponse operatorLogin(@RequestParam("cashierId") int cashierId,
                                               @RequestParam("cashierPin") String cashierPin) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException {
        return ecrSampleClient.operatorLogin(cashierId, cashierPin);
    }

    @PostMapping("/printPrePaymentReceipt")
    @ResponseBody
    public PrintReceiptResponse printPrePaymentReceipt(@RequestParam("printMode") int printMode,
                                                       @RequestParam("paidAmount") double paidAmount,
                                                       @RequestParam("paidAmountCard") double paidAmountCard,
                                                       @RequestParam("useExtPOS") boolean useExtPOS) throws Exception {
        return ecrSampleClient.printPrePaymentReceipt(printMode, paidAmount, paidAmountCard, useExtPOS);
    }

    @PostMapping("/printReceipt")
    @ResponseBody
    public PrintReceiptResponse printReceipt(@RequestParam("printMode") int printMode,
                                             @RequestParam("items") List<Item> items,
                                             @RequestParam("paidAmount") double paidAmount,
                                             @RequestParam("partialAmount") double partialAmount,
                                             @RequestParam("prePaymentAmount") double prePaymentAmount) throws Exception {
        return ecrSampleClient.printReceipt(printMode, items, paidAmount, partialAmount, prePaymentAmount);
    }

    @PostMapping("/printReceiptCard")
    @ResponseBody
    public PrintReceiptResponse printReceipt(
            @RequestParam("printMode") int printMode,
            @RequestParam("items") List<Item> items,
            @RequestParam("paidAmount") double paidAmount,
            @RequestParam("partialAmount") double partialAmount,
            @RequestParam("prePaymentAmount") double prePaymentAmount,
            @RequestParam("paidAmountCard") double paidAmountCard,
            @RequestParam("useExtPOS") boolean useExtPOS) throws Exception {
        return ecrSampleClient.printReceipt(printMode, items, paidAmount, partialAmount, prePaymentAmount, paidAmountCard, useExtPOS);
    }

    @PostMapping("/getReceipt")
    @ResponseBody
    public GetReceiptResponse getReceipt(
            @RequestParam("receiptId") String receiptId,
            @RequestParam("crn") String crn
    ) throws Exception {
        return ecrSampleClient.getReceipt(receiptId, crn);
    }

    @PostMapping("/printLastReceipt")
    @ResponseBody
    public Response<Object> printLastReceipt() throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, ECRException {
        ecrSampleClient.printLastReceipt();
        return new Response<>(200, null);
    }

    @PostMapping("/printReturnReceipt")
    @ResponseBody
    public PrintReceiptResponse printReturnReceipt(PrintReturnReceiptRequest request, int cashierId, int deptId) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException {
        return ecrSampleClient.printReturnReceipt(request, cashierId, deptId);
    }

    @PostMapping("/setupHeaderAndFooter")
    @ResponseBody
    public Response<Object> setupHeaderAndFooter(List<HeaderFooter> headers, List<HeaderFooter> footers) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        ecrSampleClient.setupHeaderAndFooter(headers, footers);
        return new Response<>(200, null);
    }

    @PostMapping("/setupHeaderLogo")
    @ResponseBody
    public Response<Object> setupHeaderLogo(byte[] bitmapBytes) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        ecrSampleClient.setupHeaderLogo(bitmapBytes);
        return new Response<>(200, null);
    }

    @PostMapping("/operatorLogout")
    @ResponseBody
    public boolean operatorLogout() throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException {
        return ecrSampleClient.operatorLogout();
    }

    @PostMapping("/printTemplate")
    @ResponseBody
    public PrintTemplateResponse printTemplate() throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return ecrSampleClient.printTemplate();
    }

    @PostMapping("/disconnect")
    @ResponseBody
    public Response<Object> disconnect() throws IOException {
        ecrSampleClient.disconnect();
        return new Response<>(200, null);
    }
}
