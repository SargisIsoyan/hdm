package am.iunetworks.ecr.client.V04.pojo;

import java.util.ArrayList;
import java.util.List;

public class PrintRequets {

    private double paidAmount;
    //Amount in non cash that was paid by customer
    private double paidAmountCard;
    //Amount paid by third party
    private double partialAmount;
    //Amount paid earlier by the customer
    private double prePaymentAmount;
    //Holds the print mode
    private int mode;
    private boolean useExtPOS;

    private List<Item> items;

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

    public void setMode(int mode) {
        this.mode = mode;
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
