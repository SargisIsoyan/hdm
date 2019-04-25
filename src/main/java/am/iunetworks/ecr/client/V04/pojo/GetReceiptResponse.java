package am.iunetworks.ecr.client.V04.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by mainserver
 */
@JsonIgnoreProperties({"error"})
public class GetReceiptResponse extends CommonResponse {
    //Cashier Id
    public String cid;
    //Created
    public String time;
    //TotalAmount
    public String ta;
    //Cash amount
    public String cash;
    //Card amount
    public String card;
    //PartialPayment amount
    public String ppa;
    //PrePaymentUsage amount
    public String ppu;
    //Lottery number
    public String lot;
    //OperationType(0 = ORDER, 2 = RETURN, 3 = PREPAYMENT)
    public String type;
    //Referenced Receipt Id
    public String ref;
    //Referenced crn
    public String refcrn;
    //Sale type
    public String saleType;
    //Referenced receipt time
    public String rrt;

    //Products, or Departments
    @JsonDeserialize(as = SubTotal[].class)
    @JsonUnwrapped
    public SubTotal[] totals;

    public GetReceiptResponse() {
    }

    public static class SubTotal {
        public String did;  //Department Id
        public String dt;   //Department Tax
        public String dtm;  //Department Tax Mode
        public String t;    //Total
        public String tt;   //Total With Tax
        public String id;   //Product Id
        public String gc;   //Good Code
        public String gn;   //Good Name
        public String qty;  //Quantity
        public String p;    //Price
        public String adg;  //ATG Code
        public String mu;   //Measurement Unit
        public String dsc;  //Discount
        public String adsc; //AdditionalDiscount
        public String dsct; //DiscountType
        public String rpid; //ReceiptProductId

        public SubTotal() {
        }
    }

}
