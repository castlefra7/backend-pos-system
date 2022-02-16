package restaurantmanager.table;

public class CategoryTab  {
    private String categoryID;
    private String categoryName;
    private String categoryTaste;
    private String categoryImage;

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryTaste() {
        return categoryTaste;
    }

    public void setCategoryTaste(String categoryTaste) {
        this.categoryTaste = categoryTaste;
    }
}
