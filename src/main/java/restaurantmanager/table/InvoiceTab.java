package restaurantmanager.table;

public class InvoiceTab {
    String invoiceID;
    String invoiceNumber;
    String ticketNumber;
    String orderID;

    public InvoiceTab(String invoiceNumber, String ticketNumber, String orderID) {
        setInvoiceNumber(invoiceNumber);
        setTicketNumber(ticketNumber);
        setOrderID(orderID);
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
