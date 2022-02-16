package restaurantmanager.table;

public class InvoiceDetailTab {
    String invoiceID;
    String productID;
    int quantity;
    float amount;

    public InvoiceDetailTab(String invoiceID, String productID, int quantity, float amount) {
        setInvoiceID(invoiceID);
        setProductID(productID);
        setQuantity(quantity);
        setAmount(amount);
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
