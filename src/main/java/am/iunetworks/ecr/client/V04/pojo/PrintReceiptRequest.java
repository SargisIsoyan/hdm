package am.iunetworks.ecr.client.V04.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vmomjyan
 * PrintReceiptRequest holds needed information that is needed to save and print the Receipt
 */
public class PrintReceiptRequest extends SessionRequest {

    //Mode for printing simple Receipt
    public static final int PRINT_MODE_SIMPLE = 1;
    //Mode for printing Receipt with Products
    public static final int PRINT_MODE_PRODUCT = 2;

    //Mode for printing Receipt with Products
    public static final int PRINT_MODE_PREPAYMENT = 3;

    //Amount in cash that was paid by customer
    double paidAmount;
    //Amount in non cash that was paid by customer
    double paidAmountCard;
    //Amount paid by third party
    double partialAmount;
    //Amount paid earlier by the customer
    double prePaymentAmount;
    //Holds the print mode
    int mode;
    boolean useExtPOS;
    //List of Items to be included in Receipt
    // If PRINT_MODE_SIMPLE is set, there should be only one Item in the list
    List<Item> items;

    //Initialize a new PrintReceiptRequest with a corresponding receipt mode
    public PrintReceiptRequest(int mode) {
        this.mode = mode;
        items = new ArrayList<Item>();
    }

    public double getPartialAmount() {
        return partialAmount;
    }

    public void setPartialAmount(double partialAmount) {
        this.partialAmount = partialAmount;
    }

    public double getPrePaymentAmount() {
        return prePaymentAmount;
    }

    public void setPrePaymentAmount(double prePaymentAmount) {
        this.prePaymentAmount = prePaymentAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public int getMode() {
        return mode;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) throws Exception {
/*        if (mode == PRINT_MODE_SIMPLE && items.size() > 0){
            throw new Exception("Simple mode can only have one item!");
        }*/
        this.items.add(item);
    }

    public boolean isUseExtPOS() {
        return useExtPOS;
    }

    public void setUseExtPOS(boolean useExtPOS) {
        this.useExtPOS = useExtPOS;
    }

    public double getPaidAmountCard() {
        return paidAmountCard;
    }

    public void setPaidAmountCard(double paidAmountCard) {
        this.paidAmountCard = paidAmountCard;
    }
}
