package restaurantmanager.table;

import temps.DateHeure;

import java.sql.Timestamp;

public class StockTab {

    private String stockID;
    private String stockDate;
    private String productID;
    private float initialQuantity;
    private float quantityLeft;
    private float realQuantityLeft;
    private String editDate;
    private char isUpdated;

    public char getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(char isUpdated) {
        this.isUpdated = isUpdated;
    }


    public void setIsUpdated(String isUpdated) {
        if(isUpdated.length() > 0) {
            this.isUpdated = isUpdated.charAt(0);
        }

    }

    public StockTab() {
    }

    public StockTab(String stockDate, String productID, float initialQuantity) {
        setStockDate(stockDate);
        setProductID(productID);
        setInitialQuantity(initialQuantity);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        setEditDate(timestamp.toString());
        setQuantityLeft(0);
        setRealQuantityLeft(0);
    }


    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public String getEditDate() {
        return editDate;
    }

    public void setEditDate(String editDate) {
        this.editDate = editDate;
    }

    public String getStockDate() {
        return stockDate;
    }

    public void setStockDate(String stockDate) {
        this.stockDate = stockDate;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public float getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(float initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public float getQuantityLeft() {
        return quantityLeft;
    }

    public void setQuantityLeft(float quantityLeft) {
        this.quantityLeft = quantityLeft;
    }

    public float getRealQuantityLeft() {
        return realQuantityLeft;
    }

    public void setRealQuantityLeft(float realQuantityLeft) {
        this.realQuantityLeft = realQuantityLeft;
    }


    public void setQuantityLeft(String quantityLeft) {
        this.quantityLeft = Double.valueOf(quantityLeft).floatValue();
    }

    public void setRealQuantityLeft(String realQuantityLeft) {
        this.realQuantityLeft = Double.valueOf(realQuantityLeft).floatValue();
    }
    public void setInitialQuantity(String initialQuantity) {
        this.initialQuantity = Double.valueOf(initialQuantity).floatValue();
    }
}
