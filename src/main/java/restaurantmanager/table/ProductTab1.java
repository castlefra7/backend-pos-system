package restaurantmanager.table;

public class ProductTab1 extends ProductTab {
    char isActive;

    public ProductTab1(String productID, String categoryID, String productName, String productImage, float price) {
        setProductID(productID);
        setCategoryID(categoryID);
        setProductName(productName);
        setProductImage(productImage);
        setPrice(price);
        setIsActive('y');
    }

    public char getIsActive() {
        return isActive;
    }

    public void setIsActive(char isActive) {
        this.isActive = isActive;
    }

}
