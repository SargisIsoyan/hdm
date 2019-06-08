package am.iunetworks.ecr.client.V04;

import am.iunetworks.ecr.client.V04.exception.ECRException;
import am.iunetworks.ecr.client.V04.pojo.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by mainserver
 */
@SuppressWarnings("Duplicates")
public class ECRSampleClient implements ECRClient {

    private static final String TRIPLE_DES_ALG = "DESede";
    private static final String CIPHER_ALGORITHM_SPEC = TRIPLE_DES_ALG + "/ECB/PKCS7Padding";

    private static final byte[] PROTOCOL_VERSION = new byte[]{(byte) 0x00, (byte) 0x05};

    private static final byte[] REQUEST_HEADER = new byte[]{
            // REQUEST_HEADER
            (byte) 0xD5, (byte) 0x80, (byte) 0xD4, (byte) 0xB4, (byte) 0xD5, (byte) 0x84,
            // protocol version
            PROTOCOL_VERSION[0], PROTOCOL_VERSION[1]
    };

    private static final short REQUEST_HEADER_OP_LENGTH = 2;
    private static final short REQUEST_HEADER_PAYLOAD_LENGHT = 2;
    private static final short REQUEST_HEADER_LENGTH = (short) (REQUEST_HEADER.length + REQUEST_HEADER_OP_LENGTH + REQUEST_HEADER_PAYLOAD_LENGHT);

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x-", b & 0xff));
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    final byte ZERO = 0;
    final byte OP_LIST_OPS_AND_DEPS = 1;
    final byte OP_OPS_LOGIN = 2;
    final byte OP_OPS_LOGOUT = 3;
    final byte OP_PRINT_RECEIPT = 4;
    final byte OP_PRINT_LAST_RECEIPT = 5;
    final byte OP_PRINT_RETURN_RECEIPT = 6;
    final byte OP_SETUP_HEADERFOOTER = 7;
    final byte OP_SETUP_HEADERLOGO = 8;
    final byte OP_PRINT_FISCAL_REPORT = 9;
    final byte OP_GET_RECEIPT = 10;
    final byte OP_PRINT_CASH_IN_OUT = 11;
    final byte OP_GET_DATE_TIME = 12;
    final byte OP_PRINT_TEMPLATE = 13;

    public int requestSequenceNumber = 0;
    public byte[] sessionKey;
    protected ObjectMapper mapper;
    String ip;
    int port;
    String password;
    byte[] passwordKey;
    private SocketChannel socketChannel;

    /**
     * @param ip       Server IP Address
     * @param port     Server port
     * @param password ECR Password
     * @throws NoSuchAlgorithmException If the "3DES/ECB/PKCS7Padding" security provider is not registered
     */
    public ECRSampleClient(String ip, int port, String password) throws NoSuchAlgorithmException {
        this.ip = ip;
        this.port = port;
        this.password = password;

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        passwordKey = Arrays.copyOf(md.digest(password.getBytes()), 24);

        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * Connect to server
     *
     * @throws IOException
     */
    @Override
    public void connect() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(ip, port));
        System.out.println("Connected!");

    }

    /**
     * Fetch the list of ECR Operators and Departments
     *
     * @return The ECR Operators and Departments list
     * @throws NoSuchPaddingException    3DES Padding is not correct
     * @throws IOException
     * @throws NoSuchAlgorithmException 3DES encryption/decryption algorithm is not present
     * @throws IllegalBlockSizeException 3DES Padding block not correct
     * @throws BadPaddingException       Invalid 3DES Padding block
     * @throws InvalidKeyException      3DES encryption/decryption key is not correct
     */
    @Override
    public GetOperatorsAndDepsResponse getOperatorsAndDeps() throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException {
        GetOperatorsAndDepsRequest request = new GetOperatorsAndDepsRequest();
        request.setPassword(password);
        byte[] requestEncrypted = getCipheredRequest(true, request);

        short len = (short) requestEncrypted.length;

        ByteBuffer bb = ByteBuffer.allocate(REQUEST_HEADER_LENGTH + len);

        bb.put(REQUEST_HEADER).put(OP_LIST_OPS_AND_DEPS).put(ZERO).putShort(len).put(requestEncrypted);

        System.out.println(byteArrayToHex(bb.array()));

        byte[] respEnc;
        try {
            respEnc = sendAndReceive(bb);
        } catch (AssertionError ae) {
            GetOperatorsAndDepsResponse response = new GetOperatorsAndDepsResponse();
            response.responseCode = Integer.parseInt(ae.getMessage());
            return response;
        }
        byte[] resp = readCipheredResponse(true, respEnc);

        String respString = new String(resp, "utf8");
        System.out.println(respString);

        return mapper.readValue(resp, GetOperatorsAndDepsResponse.class);
    }

    /**
     * @param reportType        Fiscal Report Type (1 for X report and 2 for Z report)
     * @param startDate         Start Date of the requested fiscal report
     * @param endDate           Start Date of the requested fiscal report
     * @param deptId            Department Id for filtering the fiscal report (null for all departments)
     * @param cashierId         Cashier Id for filtering the fiscal report (null for all cashiers)
     * @param transactionTypeId Transaction Type Id for filtering the fiscal report (1 for cash, 2 for card transactions, null for all types)
     * @throws Exception
     */
    @Override
    public void printFiscalReport(int reportType, Date startDate, Date endDate, Integer deptId, Integer cashierId, Integer transactionTypeId) throws Exception {

        PrintFiscalReportRequest request = new PrintFiscalReportRequest(reportType, startDate, endDate, deptId, cashierId, transactionTypeId);

        request.setSeq(getRequestSequenceNo());

        byte[] requestEncrypted = getCipheredRequest(false, request);

        short len = (short) requestEncrypted.length;

        ByteBuffer bb = ByteBuffer.allocate(REQUEST_HEADER_LENGTH + len);

        bb.put(REQUEST_HEADER).put(OP_PRINT_FISCAL_REPORT).put(ZERO).putShort(len).put(requestEncrypted);

        sendAndReceive(bb);
    }

    /**
     * Logs in given operator and returns session key
     *
     * @param cashierId  Cashier ID
     * @param cashierPin Cashier PIN
     * @return Operator login result and session key
     * @throws NoSuchAlgorithmException             3DES encryption/decryption algorithm is not present
     * @throws IllegalBlockSizeException             3DES Padding block not correct
     * @throws BadPaddingException                   Invalid 3DES Padding block
     * @throws InvalidKeyException                  3DES encryption/decryption key is not correct
     * @throws NoSuchPaddingException                Invalid Padding size
     * @throws com.fasterxml.jackson.core.JsonProcessingException @request cannot be serialized to JSON
     */
    @Override
    public OperatorLoginResponse operatorLogin(int cashierId, String cashierPin) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException {
        OperatorLoginRequest request = new OperatorLoginRequest();
        request.setPassword(password);
        request.setCashier(cashierId);
        request.setPin(cashierPin);

        byte[] requestEncrypted = getCipheredRequest(true, request);

        short len = (short) requestEncrypted.length;

        ByteBuffer bb = ByteBuffer.allocate(REQUEST_HEADER_LENGTH + len);

        bb.put(REQUEST_HEADER).put(OP_OPS_LOGIN).put(ZERO).putShort(len).put(requestEncrypted);

        byte[] respEnc;
        try {
            respEnc = sendAndReceive(bb);
        } catch (AssertionError ae) {
            OperatorLoginResponse response = new OperatorLoginResponse();
            response.responseCode = Integer.parseInt(ae.getMessage());
            return response;
        }
        byte[] resp = readCipheredResponse(true, respEnc);

        String respString = new String(resp, "utf8");

        System.out.println(respString);

        OperatorLoginResponse response = mapper.readValue(resp, OperatorLoginResponse.class);

        sessionKey = response.getKeyBytes();

        return response;
    }

    /**
     * Prints receipt
     * <p>
     * If PRINT_MODE_SIMPLE is set, there should be only one Item in the list.
     *
     * @param printMode Receipt print mode PrintReceiptRequest.PRINT_MODE_PRODUCT, PrintReceiptRequest.PRINT_MODE_SIMPLE
     * @return Receipt related information or error status
     * @throws NoSuchAlgorithmException             3DES encryption/decryption algorithm is not present
     * @throws IllegalBlockSizeException             3DES Padding block not correct
     * @throws BadPaddingException                   Invalid 3DES Padding block
     * @throws InvalidKeyException                  3DES encryption/decryption key is not correct
     * @throws NoSuchPaddingException                Invalid Padding size
     * @throws com.fasterxml.jackson.core.JsonProcessingException @request cannot be serialized to JSON
     */
    @Override
    public PrintReceiptResponse printPrePaymentReceipt(int printMode, double paidAmount, double paidAmountCard, boolean useExtPOS) throws Exception {
        return printReceipt(printMode, null, paidAmount, 0, 0, paidAmountCard, useExtPOS);
    }

    /**
     * Prints receipt
     * <p>
     * If PRINT_MODE_SIMPLE is set, there should be only one Item in the list.
     *
     * @param printMode Receipt print mode PrintReceiptRequest.PRINT_MODE_PRODUCT, PrintReceiptRequest.PRINT_MODE_SIMPLE
     * @return Receipt related information or error status
     * @throws NoSuchAlgorithmException             3DES encryption/decryption algorithm is not present
     * @throws IllegalBlockSizeException             3DES Padding block not correct
     * @throws BadPaddingException                   Invalid 3DES Padding block
     * @throws InvalidKeyException                  3DES encryption/decryption key is not correct
     * @throws NoSuchPaddingException                Invalid Padding size
     * @throws com.fasterxml.jackson.core.JsonProcessingException @request cannot be serialized to JSON
     */
    @Override
    public PrintReceiptResponse printReceipt(int printMode, List<Item> items, double paidAmount, double partialAmount, double prePaymentAmount) throws Exception {
        return printReceipt(printMode, items, paidAmount, 0, partialAmount, prePaymentAmount, false);
    }

    /**
     * Prints receipt
     * <p>
     * If PRINT_MODE_SIMPLE is set, there should be only one Item in the list.
     *
     * @param printMode Receipt print mode PrintReceiptRequest.PRINT_MODE_PRODUCT, PrintReceiptRequest.PRINT_MODE_SIMPLE
     * @return Receipt related information or error status
     * @throws NoSuchAlgorithmException             3DES encryption/decryption algorithm is not present
     * @throws IllegalBlockSizeException             3DES Padding block not correct
     * @throws BadPaddingException                   Invalid 3DES Padding block
     * @throws InvalidKeyException                  3DES encryption/decryption key is not correct
     * @throws NoSuchPaddingException                Invalid Padding size
     * @throws com.fasterxml.jackson.core.JsonProcessingException @request cannot be serialized to JSON
     */
    @Override
    public PrintReceiptResponse printReceipt(int printMode, List<Item> items, double paidAmount, double partialAmount, double prePaymentAmount, double paidAmountCard, boolean useExtPOS) throws Exception {
        PrintReceiptRequest request = new PrintReceiptRequest(printMode);
        //request.setPassword(password);
        request.setSeq(getRequestSequenceNo());
        request.setPaidAmountCard(paidAmountCard);
        request.setUseExtPOS(useExtPOS);

        if (items != null) {
            for (Item item : items) {
                request.addItem(item);
            }
        }

        request.setPaidAmount(paidAmount);
        request.setPartialAmount(partialAmount);
        request.setPrePaymentAmount(prePaymentAmount);

        byte[] requestEncrypted = getCipheredRequest(false, request);

        short len = (short) requestEncrypted.length;

        ByteBuffer bb = ByteBuffer.allocate(REQUEST_HEADER_LENGTH + len);

        bb.put(REQUEST_HEADER).put(OP_PRINT_RECEIPT).put(ZERO).putShort(len).put(requestEncrypted);

        byte[] respEnc;

        try {
            respEnc = sendAndReceive(bb);
        } catch (AssertionError ae) {
            PrintReceiptResponse response = new PrintReceiptResponse();
            response.responseCode = Integer.parseInt(ae.getMessage());
            return response;
        }

        byte[] resp = readCipheredResponse(false, respEnc);

        String respString = new String(resp, "utf8");
        System.out.println(respString);

        PrintReceiptResponse respObj = mapper.readValue(resp, PrintReceiptResponse.class);
        respObj.responseCode = 200;

        return respObj;
    }

    @Override
    public GetReceiptResponse getReceipt(String receiptId, String crn) throws Exception {
        GetReceiptRequest request = new GetReceiptRequest(receiptId, crn);

        byte[] requestEncrypted = getCipheredRequest(false, request);

        short len = (short) requestEncrypted.length;

        ByteBuffer bb = ByteBuffer.allocate(REQUEST_HEADER_LENGTH + len);

        bb.put(REQUEST_HEADER).put(OP_GET_RECEIPT).put(ZERO).putShort(len).put(requestEncrypted);

        byte[] respEnc;

        try {
            respEnc = sendAndReceive(bb);
        } catch (AssertionError ae) {
            GetReceiptResponse response = new GetReceiptResponse();
            response.responseCode = Integer.parseInt(ae.getMessage());
            return response;
        }

        byte[] resp = readCipheredResponse(false, respEnc);

        String respString = new String(resp, "utf8");
        System.out.println(respString);

        GetReceiptResponse respObj = mapper.readValue(resp, GetReceiptResponse.class);
        respObj.responseCode = 200;

        return respObj;
    }

    /**
     * Print last receipt
     *
     * @throws NoSuchAlgorithmException             3DES encryption/decryption algorithm is not present
     * @throws IllegalBlockSizeException             3DES Padding block not correct
     * @throws BadPaddingException                   Invalid 3DES Padding block
     * @throws InvalidKeyException                  3DES encryption/decryption key is not correct
     * @throws NoSuchPaddingException                Invalid Padding size
     * @throws com.fasterxml.jackson.core.JsonProcessingException @request cannot be serialized to JSON
     */
    @Override
    public void printLastReceipt() throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, ECRException {
        PrintLastReceiptRequest request = new PrintLastReceiptRequest();
        request.setSeq(getRequestSequenceNo());

        byte[] requestEncrypted = getCipheredRequest(false, request);

        short len = (short) requestEncrypted.length;

        ByteBuffer bb = ByteBuffer.allocate(REQUEST_HEADER_LENGTH + len);

        bb.put(REQUEST_HEADER).put(OP_PRINT_LAST_RECEIPT).put(ZERO).putShort(len).put(requestEncrypted);

        sendAndReceive(bb);
    }

    /**
     * Returns fully or partially Receipt money and return Return Receipt information
     *
     * @param request   Receipt to be returned
     * @param cashierId Cashier ID
     * @param deptId    Department ID
     * @throws NoSuchAlgorithmException             3DES encryption/decryption algorithm is not present
     * @throws IllegalBlockSizeException             3DES Padding block not correct
     * @throws BadPaddingException                   Invalid 3DES Padding block
     * @throws InvalidKeyException                  3DES encryption/decryption key is not correct
     * @throws NoSuchPaddingException                Invalid Padding size
     * @throws com.fasterxml.jackson.core.JsonProcessingException @request cannot be serialized to JSON
     */
    @Override
    public PrintReceiptResponse printReturnReceipt(PrintReturnReceiptRequest request, int cashierId, int deptId) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException {

        byte[] requestEncrypted = getCipheredRequest(false, request);

        short len = (short) requestEncrypted.length;

        ByteBuffer bb = ByteBuffer.allocate(REQUEST_HEADER_LENGTH + len);

        bb.put(REQUEST_HEADER).put(OP_PRINT_RETURN_RECEIPT).put(ZERO).putShort(len).put(requestEncrypted);

        byte[] respEnc;

        try {
            respEnc = sendAndReceive(bb);
        } catch (AssertionError ae) {
            PrintReceiptResponse response = new PrintReceiptResponse();
            response.responseCode = Integer.parseInt(ae.getMessage());
            return response;
        }

        if (respEnc != null) {
            byte[] resp = readCipheredResponse(false, respEnc);

            String respString = new String(resp, "utf8");
            System.out.println(respString);

            PrintReceiptResponse respObj = mapper.readValue(resp, PrintReceiptResponse.class);
            respObj.responseCode = 200;
            return respObj;

        }
        return null;

    }

    /**
     * Setup header and Futter for
     *
     * @throws NoSuchAlgorithmException             3DES encryption/decryption algorithm is not present
     * @throws IllegalBlockSizeException             3DES Padding block not correct
     * @throws BadPaddingException                   Invalid 3DES Padding block
     * @throws InvalidKeyException                  3DES encryption/decryption key is not correct
     * @throws NoSuchPaddingException                Invalid Padding size
     * @throws com.fasterxml.jackson.core.JsonProcessingException @request cannot be serialized to JSON
     */
    @Override
    public void setupHeaderAndFooter(List<HeaderFooter> headers, List<HeaderFooter> footers) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SetupHeaderFooterRequest request = new SetupHeaderFooterRequest();
        request.setHeaders(headers);
        request.setFooters(footers);

        byte[] requestEncrypted = getCipheredRequest(false, request);

        short len = (short) requestEncrypted.length;

        ByteBuffer bb = ByteBuffer.allocate(REQUEST_HEADER_LENGTH + len);

        bb.put(REQUEST_HEADER).put(OP_SETUP_HEADERFOOTER).put(ZERO).putShort(len).put(requestEncrypted);

        sendAndReceive(bb);
    }

    /**
     * Setup header and Futter for
     *
     * @throws NoSuchAlgorithmException             3DES encryption/decryption algorithm is not present
     * @throws IllegalBlockSizeException             3DES Padding block not correct
     * @throws BadPaddingException                   Invalid 3DES Padding block
     * @throws InvalidKeyException                  3DES encryption/decryption key is not correct
     * @throws NoSuchPaddingException                Invalid Padding size
     * @throws com.fasterxml.jackson.core.JsonProcessingException @request cannot be serialized to JSON
     */
    @Override
    public void setupHeaderLogo(byte[] bitmapBytes) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SetupHeaderLogoRequest request = new SetupHeaderLogoRequest();
        request.setHeaderLogo(new String(Base64.encode(bitmapBytes)));

        byte[] requestEncrypted = getCipheredRequest(false, request);

        short len = (short) requestEncrypted.length;

        ByteBuffer bb = ByteBuffer.allocate(REQUEST_HEADER_LENGTH + len);

        bb.put(REQUEST_HEADER).put(OP_SETUP_HEADERLOGO).put(ZERO).putShort(len).put(requestEncrypted);

        sendAndReceive(bb);
    }

    /**
     * Logout current operator
     *
     * @throws NoSuchAlgorithmException             3DES encryption/decryption algorithm is not present
     * @throws IllegalBlockSizeException             3DES Padding block not correct
     * @throws BadPaddingException                   Invalid 3DES Padding block
     * @throws InvalidKeyException                  3DES encryption/decryption key is not correct
     * @throws NoSuchPaddingException                Invalid Padding size
     * @throws com.fasterxml.jackson.core.JsonProcessingException @request cannot be serialized to JSON
     */
    @Override
    public boolean operatorLogout() throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ECRException {
        OperatorLogoutRequest request = new OperatorLogoutRequest();
        request.setSeq(getRequestSequenceNo());

        byte[] requestEncrypted = getCipheredRequest(false, request);

        short len = (short) requestEncrypted.length;

        ByteBuffer bb = ByteBuffer.allocate(REQUEST_HEADER_LENGTH + len);

        bb.put(REQUEST_HEADER).put(OP_OPS_LOGOUT).put(ZERO).putShort(len).put(requestEncrypted);

        sendAndReceive(bb);

        sessionKey = null;

        return true;
    }

    /**
     * @throws NoSuchAlgorithmException             3DES encryption/decryption algorithm is not present
     * @throws IllegalBlockSizeException             3DES Padding block not correct
     * @throws BadPaddingException                   Invalid 3DES Padding block
     * @throws InvalidKeyException                  3DES encryption/decryption key is not correct
     * @throws NoSuchPaddingException                Invalid Padding size
     * @throws com.fasterxml.jackson.core.JsonProcessingException @request cannot be serialized to JSON
     */
    @Override
    public PrintTemplateResponse printTemplate() throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        PrintTicketTemplateRequest request = new PrintTicketTemplateRequest();
        request.setSeq(getRequestSequenceNo());

        PrintTemplateResponse response = new PrintTemplateResponse();

        byte[] requestEncrypted = getCipheredRequest(false, request);

        short len = (short) requestEncrypted.length;

        ByteBuffer bb = ByteBuffer.allocate(REQUEST_HEADER_LENGTH + len);

        bb.put(REQUEST_HEADER).put(OP_PRINT_TEMPLATE).put(ZERO).putShort(len).put(requestEncrypted);

        try {
            sendAndReceive(bb);
        } catch (AssertionError ae){
            response.responseCode = Integer.parseInt(ae.getMessage());
            return response;
        }
        response.responseCode = 200;
        return response;

    }

    public GetDateTimeResponse getDateTime() throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        GetDateTimeRequest request = new GetDateTimeRequest();
        request.setSeq(getRequestSequenceNo());

        byte[] requestEncrypted = getCipheredRequest(false, request);

        short len = (short) requestEncrypted.length;

        ByteBuffer bb = ByteBuffer.allocate(REQUEST_HEADER_LENGTH + len);

        bb.put(REQUEST_HEADER).put(OP_GET_DATE_TIME).put(ZERO).putShort(len).put(requestEncrypted);

        byte[] respEnc;

        try {
            respEnc = sendAndReceive(bb);
        } catch (AssertionError ae) {
            GetDateTimeResponse response = new GetDateTimeResponse();
            response.responseCode = Integer.parseInt(ae.getMessage());
            return response;
        }

        if (respEnc != null) {
            byte[] resp = readCipheredResponse(false, respEnc);

            String respString = new String(resp, "utf8");
            System.out.println(respString);

            GetDateTimeResponse respObj = mapper.readValue(resp, GetDateTimeResponse.class);
            respObj.responseCode = 200;
            return respObj;

        }
        return null;
    }

    /**
     * Disconnect the client form server
     *
     * @throws IOException on network error
     */
    @Override
    public void disconnect() throws IOException {
        if (socketChannel != null) {
            socketChannel.shutdownInput();
            socketChannel.shutdownOutput();
            socketChannel.close();
        }
    }


    /**
     * Get next request sequence number
     *
     * @return Incremental sequence number
     */
    private int getRequestSequenceNo() {
        return ++requestSequenceNumber;
    }

    /**
     * Encrypts the request using using password or session key
     *
     * @param usePasswordKey Use password (true) or session (false) key
     * @param request        Object to be encrypted
     * @return Cyphered request bytes encrypted using password or session key
     * @throws NoSuchAlgorithmException             3DES encryption/decryption algorithm is not present
     * @throws IllegalBlockSizeException             3DES Padding block not correct
     * @throws BadPaddingException                   Invalid 3DES Padding block
     * @throws InvalidKeyException                  3DES encryption/decryption key is not correct
     * @throws NoSuchPaddingException                Invalid Padding size
     * @throws com.fasterxml.jackson.core.JsonProcessingException @request cannot be serialized to JSON
     */
    private byte[] getCipheredRequest(boolean usePasswordKey, Object request) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, JsonProcessingException, IllegalBlockSizeException, BadPaddingException {
        byte[] key = usePasswordKey ? passwordKey : sessionKey;

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_SPEC);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, TRIPLE_DES_ALG));

        byte[] req = mapper.writeValueAsBytes(request);
        System.out.println(new String(req));
        return cipher.doFinal(req);
    }

    /**
     * Decrypts the request using using password or session
     *
     * @param usePasswordKey Use password (true) or session (false) key
     * @param response       Object to be decrypted
     * @return decrypted request bytes using password or session key
     * @throws NoSuchAlgorithmException             3DES encryption/decryption algorithm is not present
     * @throws IllegalBlockSizeException             3DES Padding block not correct
     * @throws BadPaddingException                   Invalid 3DES Padding block
     * @throws InvalidKeyException                  3DES encryption/decryption key is not correct
     * @throws NoSuchPaddingException                Invalid Padding size
     * @throws com.fasterxml.jackson.core.JsonProcessingException @request cannot be serialized to JSON
     */
    private byte[] readCipheredResponse(boolean usePasswordKey, byte[] response) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, JsonProcessingException, IllegalBlockSizeException, BadPaddingException {
        byte[] key = usePasswordKey ? passwordKey : sessionKey;

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_SPEC);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, TRIPLE_DES_ALG));

        return cipher.doFinal(response);
    }

    /**
     * Sends the request and received the response
     *
     * @param bb ByteBuffer to be sent
     * @return response byte array
     * @throws IOException Network error
     */
    private byte[] sendAndReceive(ByteBuffer bb) throws IOException, ECRException {

        //Match Endianness
        bb.flip();

        socketChannel.write(bb);

        System.out.println("Request sent!");

        ByteBuffer h = ByteBuffer.allocate(11);

        socketChannel.read(h);
        System.out.println("Response received");

        //Match Endianness
        h.flip();

        System.out.println("Protocol version :" + h.get() + "." + h.get());
        System.out.println("S/W version :" + h.get() + "." + h.get() + "." + h.get());

        //get the response code
        int responseCode = h.getShort() & 0xFFFF;


        System.out.println("Response code :" + responseCode + " -  " + ResponseCodes.get(responseCode));

        if (ResponseCodes.get(responseCode) != ResponseCodes.OK) {
            throw new AssertionError(responseCode);
        }

        //Match Endianness
        int len = h.getShort() & 0xFFFF;

        //skip 2 reserved bytes
        h.get();
        h.get();

        ByteBuffer response = ByteBuffer.allocate(len);

        socketChannel.read(response);

        return response.array();
    }

    enum ResponseCodes {

        OK(200),
        INTERNAL_ERROR(500),

        ERROR_BAD_REQUEST(400),
        ERROR_WRONG_PROTOCOL_VERSION(402),

        ERROR_UNAUTHORIZED_CONNECTION(403),
        ERROR_BAD_OP_CODE(404),

        SIMPLE_RECEIPT_NOT_ALLOWED(410),

        LOGIN_ERROR_WRONG_PASS(101),
        ERROR_WRONG_SESSION(102),
        ERROR_WRONG_REQ_HEADER(103),
        ERROR_WRONG_RSEQ(104),
        ERROR_WRONG_JSON(105),

        LOGIN_ERROR_WRONG_USER(111),
        LOGIN_ERROR_NO_SUCH_USER(112),
        LOGIN_ERROR_INACTIVE_USER(113),

        ERROR_NO_USER(121),

        LAST_RECEIPT_NO_FILE(141),
        LAST_RECEIPT_ANOTHER_CASHIER(142),
        LAST_RECEIPT_PRN_FAIL(143),
        LAST_RECEIPT_PRN_INIT(144),
        LAST_RECEIPT_PRN_NO_PAPER(145),

        PRINT_RECEIPT_NO_DEPT(151),
        PRINT_RECEIPT_PAID_LESS(152),
        PRINT_RECEIPT_LIMIT(153),
        PRINT_RECEIPT_AMOUNT_POS(154),
        PRINT_RECEIPT_SYNC_NEEDED(155),
        PRINT_RECEIPT_SIMPLE_INVALID_ITEM_COUNT(156),
        RECEIPT_RETURN_INVALID_ID(157),
        RECEIPT_RETURN_ALREADY_RETURNED(158),
        RECEIPT_PRICE_QTY_NONZERO(159),
        RECEIPT_PRICE_INVALID_DISCOUNT_PERCENT(160),

        PRINT_RECEIPT_ERROR_EMPTY_CODE(161),
        PRINT_RECEIPT_ERROR_EMPTY_NAME(162),

        //Added in V0.3
        PRINT_RECEIPT_AMOUNT_CASH(163),
        PAYMENT_FAILED(164),
        RECEIPT_ITEM_FULL_PRICE_NONZERO(165),
        RECEIPT_PRICE_QTY_NOT_EQUAL(166),
        RECEIPT_PAYMENT_AMOUNT_MORE_THAN_TOTAL(167),
        RECEIPT_PAYMENT_AMOUNT_REDUNDANT_CASH(168),

        PRINT_FISCAL_REPORT_WRONG_FILTERS(169),
        PRINT_FISCAL_REPORT_INVALID_PERIOD(170),

        RECEIPT_PRICE_INVALID_ITEM_TOTAL_PRICE(171),
        GET_RECEIPT_TYPE_NOT_A_PRODUCT_RECEIPT(172),
        INVALID_DISCOUNT_TYPES(173),
        RETURN_RECEIPT_DOES_NOT_EXIST(174),

        RETURN_RECEIPT_INVALID_CRN(175),
        ERROR_LAST_RECEIPT_DOES_NOT_EXIST(176),
        RETURN_RECEIPT_TYPE_NOT_SUPPORTED(177),
        RECEIPT_RETURN_BAD_REQUESTED_AMOUNT(178),
        ERROR_PARTIAL_PAYMENT_RECEIPT_MUST_BE_RETURNED_FULLY(179),
        RECEIPT_RETURN_MISSING_PRODUCT_LIST(180),
        RECEIPT_RETURN_INVALID_QUANTITY_FOR_ITEM(181),
        ERROR_RETURN_RECEIPT_IS_RETURN_TYPE(182),
        ERROR_INVALID_ATG_CODE(183),
        PREPAYMENT_RETURN_INVALID_REQUEST(184),
        RECEIPT_HAS_NOT_SYNCED_REFERENCED_RECEIPTS(185),
        PREPAYMENT_RECEIPT_INVALID_AMOUNT(186),
        PREPAYMENT_RECEIPT_INVALID_ITEM_LIST(187),
        PRINT_REQUEST_INVALID_AMOUNTS(188),
        PRINT_REQUEST_INVALID_ROUNDING(189),
        PAYMENT_NOT_AVAILABLE(190),
        BAD_AMOUNT_FOR_CASH_IN_OUT(191),
        ATG_CODE_REQUIRED(192),

        UNKNOWN_RESPONSE_CODE(-1);

        int code;

        ResponseCodes(int code) {
            this.code = code;
        }

        public static ResponseCodes get(int code) {
            for (ResponseCodes a : ResponseCodes.values()) {
                if (a.code == code)
                    return a;
            }
            return UNKNOWN_RESPONSE_CODE;
        }

    }

}
