package restaurantmanager.table;

public class PaymentTab {
    String pointOfSaleID;
    String orderID;
    float amountReceived;
    float totalAmount;
    float amountDue;


    public PaymentTab(String pointOfSaleID, String orderID, float amountReceived, float totalAmount, float amountDue) {
        setPointOfSaleID(pointOfSaleID);
        setOrderID(orderID);
        setAmountReceived(amountReceived);
        setTotalAmount(totalAmount);
        setAmountDue(amountDue);
    }

    public String getPointOfSaleID() {
        return pointOfSaleID;
    }

    public void setPointOfSaleID(String pointOfSaleID) {
        this.pointOfSaleID = pointOfSaleID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public float getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(float amountReceived) {
        this.amountReceived = amountReceived;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public float getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(float amountDue) {
        this.amountDue = amountDue;
    }
}
