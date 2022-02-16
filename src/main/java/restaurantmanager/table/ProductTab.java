package restaurantmanager.table;

import errorHandling.ErrorHandling;
import outils.FctGen;

public class ProductTab {
    /*productID char(5) primary key,
    categoryID char(2) references accounting.productCategories(categoryID),
    productName varchar(200) NOT NULL,
    price*/
    private String productID;
    private String categoryID;
    private String productName;
    private float price;
    private String productImage;



    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public void setPrice(String price) throws Exception {
        FctGen fctGen = new FctGen();
        if(fctGen.isNumber(price) == false) {
            throw new Exception(ErrorHandling.numberException());
        } else {
            setPrice(Float.valueOf(price));
        }
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
