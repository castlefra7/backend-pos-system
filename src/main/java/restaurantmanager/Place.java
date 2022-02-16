package restaurantmanager;

public class Place {
    /*placeID smallint primary key,
    placeName varchar(5),
    placeTypeID smallint references accounting.placeType(placeTypeID),
    companyAffiliateID*/

    private int placeID;
    private String placeName;
    private int placeTypeID;
    private int companyAffiliateID;


    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }

    public void setPlaceID(String placeID) {
        setPlaceID(Integer.parseInt(placeID));
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public int getPlaceTypeID() {
        return placeTypeID;
    }

    public void setPlaceTypeID(int placeTypeID) {
        this.placeTypeID = placeTypeID;
    }

    public void setPlaceTypeID(String placeTypeID) {
        setPlaceTypeID(Integer.parseInt(placeTypeID));
    }

    public int getCompanyAffiliateID() {
        return companyAffiliateID;
    }

    public void setCompanyAffiliateID(int companyAffiliateID) {
        this.companyAffiliateID = companyAffiliateID;
    }

    public void setCompanyAffiliateID(String companyAffiliateID) {
        setCompanyAffiliateID(Integer.parseInt(companyAffiliateID));
    }
}
