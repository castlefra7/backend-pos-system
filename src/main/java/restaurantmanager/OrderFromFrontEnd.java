package restaurantmanager;

public class OrderFromFrontEnd {
    ProductOrderQuant[] orders;
    String amountReceived;
    String userID;
    String customerID;
    String pointOfSaleID;
    boolean isPayment;
    String placeID;

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getPointOfSaleID() {
        return pointOfSaleID;
    }

    public void setPointOfSaleID(String pointOfSaleID) {
        this.pointOfSaleID = pointOfSaleID;
    }

    public void setIsPayment(boolean val) {
        this.isPayment = val;
    }

    public boolean getIsPayment() {
        return this.isPayment;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public ProductOrderQuant[] getOrders() {
        return orders;
    }

    public void setOrders(ProductOrderQuant[] orders) {
        this.orders = orders;
    }

    public String getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(String amountReceived) {
        this.amountReceived = amountReceived;
    }
}
