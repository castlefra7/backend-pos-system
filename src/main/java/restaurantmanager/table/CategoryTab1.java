package restaurantmanager.table;

public class CategoryTab1 extends  CategoryTab {
    char isActive;

    public CategoryTab1(String categoryID, String categoryName, String categoryTaste, String categoryImage) {
        setCategoryID(categoryID);
        setCategoryName(categoryName);
        setCategoryTaste(categoryTaste);
        setCategoryImage(categoryImage);
        setIsActive('y');
    }

    public char getIsActive() {
        return isActive;
    }

    public void setIsActive(char isActive) {
        this.isActive = isActive;
    }
}
