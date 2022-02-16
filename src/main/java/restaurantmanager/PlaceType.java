package restaurantmanager;

public class PlaceType {
    int placeTypeID;
    String placeTypeDesc;

    public int getPlaceTypeID() {
        return placeTypeID;
    }

    public void setPlaceTypeID(int placeTypeID) {
        this.placeTypeID = placeTypeID;
    }

    public void setPlaceTypeID(String placeTypeID) {
        setPlaceTypeID(Integer.parseInt(placeTypeID));
    }

    public String getPlaceTypeDesc() {
        return placeTypeDesc;
    }

    public void setPlaceTypeDesc(String placeTypeDesc) {
        this.placeTypeDesc = placeTypeDesc;
    }
}
