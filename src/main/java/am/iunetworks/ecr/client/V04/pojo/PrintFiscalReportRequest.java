package am.iunetworks.ecr.client.V04.pojo;

import java.util.Date;

/**
 * Created by mainserver on 8/26/15
 */
public class PrintFiscalReportRequest extends SessionRequest {

    //Mode for printing X Report
    public static final int PRINT_FISCAL_REPORT_X = 1;

    //Mode for printing Z Report
    public static final int PRINT_FISCAL_REPORT_Z = 2;
    private final int reportType;
    private Integer deptId;
    private Integer cashierId;
    private Integer transactionTypeId;
    private Date startDate;
    private Date endDate;

    public PrintFiscalReportRequest(int reportType, Date startDate, Date endDate, Integer deptId, Integer cashierId, Integer transactionTypeId) {
        this.reportType = reportType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deptId = deptId;
        this.cashierId = cashierId;
        this.transactionTypeId = transactionTypeId;
    }

    public int getReportType() {
        return reportType;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public Integer getCashierId() {
        return cashierId;
    }

    public Integer getTransactionTypeId() {
        return transactionTypeId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
