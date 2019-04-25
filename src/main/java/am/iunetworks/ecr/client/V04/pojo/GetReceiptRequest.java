package am.iunetworks.ecr.client.V04.pojo;

/**
 * Created by mainserver
 */
public class GetReceiptRequest extends SessionRequest{

    private String receiptId;
    private String crn;

    public GetReceiptRequest(String receiptId, String crn) {
        this.receiptId = receiptId;
        this.crn = crn;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public String getCrn() {
        return crn;
    }
}
