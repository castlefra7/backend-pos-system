package restaurantmanager.table;

public class OrderTab {
    private String orderID;
    private String orderDate;
    private String userID;
    private String customerID;
    private int placeID;

    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }

    public void setPlaceID(String placeID) {
        setPlaceID(Integer.parseInt(placeID));
    }

    public OrderTab(String orderDate, String userID, String customerID, String placeID) {
        setOrderDate(orderDate);
        setUserID(userID);
        setCustomerID(customerID);
        setPlaceID(placeID);
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
}
