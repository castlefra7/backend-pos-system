package restaurantmanager;

import errorHandling.ErrorHandling;
import outils.FctGen;
import restaurantmanager.table.ProductTab;

public final class ProductOrderQuant extends ProductTab {
    private int quantity;

    public ProductOrderQuant(String id, String productName, String price, String categoryID, String quantity) throws Exception {
        setProductID(id);
        setProductName(productName);
        setPrice(price);
        setCategoryID(categoryID);
        setQuantity(quantity);
    }

    public void setQuantity(String price) throws Exception {
        FctGen fctGen = new FctGen();
        if(fctGen.isNumber(price) == false) {
            throw new Exception(ErrorHandling.numberException());
        } else {
            setQuantity(Integer.valueOf(price));
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
