package am.iunetworks.ecr.client.V04.pojo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vmomjyan
 * Date: 12/19/13
 * Time: 1:41 PM
 */
public class PrintReturnReceiptRequest extends SessionRequest {
//    public int deptId;
//    public double time;
    public String crn;
    public int returnTicketId;
//    public int cashierId;

    public List<ReturnItem> returnItemList;

    public BigDecimal cashAmountForReturn;
    public BigDecimal cardAmountForReturn;

}
