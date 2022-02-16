package restaurantmanager;

public class OrderFrontAv extends OrderFront {
    ProductOrder[] productOrders;
    Place place;


    public OrderFrontAv() {}

    public OrderFrontAv(OrderFront orderFront) {
        super(orderFront.getOrderID(), orderFront.getOrderDate(), orderFront.getTotalAmount(), orderFront.getInvoiceID(), orderFront.getTicketNumber(), orderFront.getPlaceId());

    }

    public OrderFrontAv(String orderID, String orderDate, float totalAmount, String invoiceID, String ticketNumber) {
        super(orderID, orderDate, totalAmount, invoiceID, ticketNumber);
    }

    public ProductOrder[] getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(ProductOrder[] productOrders) {
        this.productOrders = productOrders;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

}
