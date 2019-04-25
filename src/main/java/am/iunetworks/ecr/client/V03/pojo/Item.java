package am.iunetworks.ecr.client.V03.pojo;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: vmomjyan
 * Item for Receipt
 */
public class Item {
    int dep;
    BigDecimal qty;
    //Discount percent
    BigDecimal dp;
    BigDecimal price;
    BigDecimal totalPrice;
    String productCode;

    String productName;

    //measurement unit
    String unit;

    public int getDep() {
        return dep;
    }

    public void setDep(int dep) {
        this.dep = dep;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getDp() {
        return dp;
    }

    public void setDp(BigDecimal dp) {
        this.dp = dp;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
