package restaurantmanager;

public class OrderFront {
    String orderID;
    String orderDate;
    float totalAmount;
    String invoiceID;
    String ticketNumber;
    String placeId;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public OrderFront() {
    }

    public OrderFront(String orderID, String orderDate, float totalAmount, String invoiceID, String ticketNumber) {
        setOrderID(orderID);
        setOrderDate(orderDate);
        setTotalAmount(totalAmount);
        setInvoiceID(invoiceID);
        setTicketNumber(ticketNumber);
    }

    public OrderFront(String orderID, String orderDate, float totalAmount, String invoiceID, String ticketNumber, String placeId) {
        setOrderID(orderID);
        setOrderDate(orderDate);
        setTotalAmount(totalAmount);
        setInvoiceID(invoiceID);
        setTicketNumber(ticketNumber);
        setPlaceId(placeId);
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        setTotalAmount(Float.parseFloat(totalAmount));
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }
}
