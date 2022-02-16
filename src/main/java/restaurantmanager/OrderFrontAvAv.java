package restaurantmanager;

public class OrderFrontAvAv extends OrderFrontAv {
    float amountreceived;
    String pointOfSaleID;

    public String getPointOfSaleID() {
        return pointOfSaleID;
    }

    public void setPointOfSaleID(String pointOfSaleID) {
        this.pointOfSaleID = pointOfSaleID;
    }

    public float getAmountreceived() {
        return amountreceived;
    }

    public void setAmountreceived(float amountreceived) {
        this.amountreceived = amountreceived;
    }

}
