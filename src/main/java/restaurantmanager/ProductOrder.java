package restaurantmanager;

import restaurantmanager.table.ProductTab;

public class ProductOrder extends ProductTab {
    private String quantity;
    private String totalAmount;

    public ProductOrder() {

    }

    public ProductOrder(String productName, float price, String quantity, String totalAmount) {
        setProductName(productName);
        setPrice(price);
        setQuantity(quantity);
        setTotalAmount(totalAmount);
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
