package am.iunetworks.ecr.client.V04.pojo;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: vmomjyan
 * Item for Receipt
 */
public class Item {

    int dep;
    BigDecimal qty;
    BigDecimal price;
    String productCode;
    BigDecimal discount;
    BigDecimal discountType;
    BigDecimal additionalDiscount;
    BigDecimal additionalDiscountType;
    String productName;

    public BigDecimal getDiscountType() {
        return discountType;
    }

    public void setDiscountType(BigDecimal discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getAdditionalDiscountType() {
        return additionalDiscountType;
    }

    public void setAdditionalDiscountType(BigDecimal additionalDiscountType) {
        this.additionalDiscountType = additionalDiscountType;
    }

    String adgCode;
    String unit;

    public String getAdgCode() {
        return adgCode;
    }

    public void setAdgCode(String adgCode) {
        this.adgCode = adgCode;
    }

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getAdditionalDiscount() {
        return additionalDiscount;
    }

    public void setAdditionalDiscount(BigDecimal additionalDiscount) {
        this.additionalDiscount = additionalDiscount;
    }

    public enum ProductDiscountType {
        PERCENT(1),
        PRICE(2),
        TOTAL(4);

        private int value;

        ProductDiscountType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum ProductAdditionalDiscountType {
        PERCENT(8),
        PRICE(16);

        private int value;

        ProductAdditionalDiscountType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
