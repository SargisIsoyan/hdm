package am.iunetworks.ecr.client.V04.pojo;

/**
 * Created by mainserver
 */
public class ReturnItem {

    private long rpid; //ReceiptProductId

    private double quantity; //Quantity to be returned

    public ReturnItem(long rpid, double quantity) {
        this.rpid = rpid;
        this.quantity = quantity;
    }

    public long getRpid() {
        return rpid;
    }

    public double getQuantity() {
        return quantity;
    }
}
