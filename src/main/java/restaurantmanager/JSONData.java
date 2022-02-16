package restaurantmanager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import connexion.ConnGen;
import restaurantmanager.graph.BigStatistic;
import restaurantmanager.table.CategoryTab;
import restaurantmanager.table.ProductTab;
import restaurantmanager.table.StockTab;
import restaurantmanager.table.StockTabAv;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class JSONData {

    public static void main(String[] args) throws Exception {
        JSONData jsonData = new JSONData();
    }

    public String getSucess(String text) {
        Gson gson = new Gson();
        ServerResponseJSON serverResponseJSON = new ServerResponseJSON();
        serverResponseJSON.setSuccess(true);
        serverResponseJSON.setText(text);
        return gson.toJson(serverResponseJSON);
    }

    public String getError(String text) {
        Gson gson = new Gson();
        ServerResponseJSON serverResponseJSON = new ServerResponseJSON();
        serverResponseJSON.setSuccess(false);
        serverResponseJSON.setText(text);
        return gson.toJson(serverResponseJSON);
    }

    public String getAllCategoriesJSON() throws  Exception {
        Gson gson = new Gson();
        AppData appData = new AppData();
        CategoryTab[] allCategories=  appData.getAllCategories();
        return gson.toJson(allCategories);
    }

    public String getAllProductsJSON(String categoryID) throws Exception {
        Gson gson = new Gson();
        AppData appData = new AppData();

        ProductTab[] allProductTabs= new ProductTab[0];
        if(categoryID.isEmpty() == true) {
            allProductTabs = appData.getAllProducts();
        } else {
            allProductTabs = appData.getAllProducts(categoryID);
        }
        return gson.toJson(allProductTabs);
    }

    public String getAllUnpaidOrders() throws Exception {
        Gson gson = new Gson();
        AppData appData = new AppData();
        ConnGen connGen = new ConnGen();
        OrderFrontAv[] orderFrontAvs = appData.getUnpaidOrdersAvFront(connGen);
        connGen.close();
        return gson.toJson(orderFrontAvs);
    }

    public String getAllUnpaidOrders(String placeID) throws Exception {
        Gson gson = new Gson();
        AppData appData = new AppData();
        ConnGen connGen = new ConnGen();
        OrderFrontAv[] orderFrontAvs = appData.getUnpaidOrdersAvFront(connGen, placeID);
        connGen.close();
        return gson.toJson(orderFrontAvs);
    }

    public String getAllStocks() throws Exception {
        Gson gson = new Gson();
        AppData appData = new AppData();
        ConnGen connGen = new ConnGen();
        StockTabAv[] stockTabs = appData.getAllStocks();
        connGen.close();
        return gson.toJson(stockTabs);
    }

    public  String getBigStatistic(int month, int year) throws Exception {
        Gson gson = new Gson();
        AppData appData = new AppData();
        ConnGen connGen = new ConnGen();
        BigStatistic bigStatistic = appData.getStatistics(connGen, month, year);
        
        connGen.close();
        return gson.toJson(bigStatistic);
    }

    public String getAllPlaces() throws Exception {
        Gson gson = new Gson();
        AppData appData = new AppData();
        ConnGen connGen = new ConnGen();
        Place[] places = appData.getAllPlaces(connGen);
        connGen.close();
        return gson.toJson(places);
    }


    public OrderFrontAvAv getOrderFrontAvAv(String orderJSON) {
        Gson gson = new Gson();
        Type type = new TypeToken<DecodeOrderFrontAvAv>(){}.getType();

        DecodeOrderFrontAvAv decodeOrderFrontAvAv = gson.fromJson(orderJSON, type);

        OrderFrontAvAv orderFrontAvAv = new OrderFrontAvAv();
        orderFrontAvAv.setPointOfSaleID(decodeOrderFrontAvAv.getPointOfSaleID());
        orderFrontAvAv.setOrderID(decodeOrderFrontAvAv.getOrderID());

        orderFrontAvAv.setAmountreceived(decodeOrderFrontAvAv.getAmountreceived());
        orderFrontAvAv.setInvoiceID(decodeOrderFrontAvAv.getInvoiceID());

        ProductOrder[] productOrders = new ProductOrder[decodeOrderFrontAvAv.getProductOrders().size()];
        orderFrontAvAv.setProductOrders(decodeOrderFrontAvAv.getProductOrders().toArray(productOrders));
        return orderFrontAvAv;
    }

    public OrderFrontAv getOrderFrontAV(String orderFrontJSON) {
        Gson gson = new Gson();
        Type type = new TypeToken<DecodeOrderFrontAv>(){}.getType();
        DecodeOrderFrontAv decodeOrderFrontAv = gson.fromJson(orderFrontJSON, type);
        OrderFrontAv orderFrontAv = new OrderFrontAv(decodeOrderFrontAv.getOrderID(), decodeOrderFrontAv.getOrderDate(),
                decodeOrderFrontAv.getTotalAmount(), decodeOrderFrontAv.getInvoiceID(), decodeOrderFrontAv.getTicketNumber());
        ProductOrder[] productOrders = new ProductOrder[decodeOrderFrontAv.getProductOrders().size()];
        orderFrontAv.setProductOrders(decodeOrderFrontAv.getProductOrders().toArray(productOrders));
        return orderFrontAv;
    }

    public ProductTab[] getAllProducts(String ProductTabsJSON) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ProductTab>>(){}.getType();
        ArrayList<ProductTab> allProductTabs = gson.fromJson(ProductTabsJSON, type);
        ProductTab[] all = new ProductTab[allProductTabs.size()];
        return allProductTabs.toArray(all);
    }

    public ProductOrderQuant[] getAllOrderedProducts(String orderedProducts) throws  Exception {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ProductOrderQuant>>(){}.getType();
        ArrayList<ProductOrderQuant> allProductTabs = gson.fromJson(orderedProducts, type);
        ProductOrderQuant[] all = new ProductOrderQuant[allProductTabs.size()];
        return allProductTabs.toArray(all);
    }

    public OrderFromFrontEnd getOrder(String orders) {
        Gson gson = new Gson();
        Type type = new TypeToken<DecodeOrder>(){}.getType();
        DecodeOrder decodeOrder = gson.fromJson(orders, type);
        ProductOrderQuant[] all = new ProductOrderQuant[decodeOrder.getOrders().size()];

        OrderFromFrontEnd orderFromFrontEnd = new OrderFromFrontEnd();
        orderFromFrontEnd.setOrders(decodeOrder.getOrders().toArray(all));
        orderFromFrontEnd.setAmountReceived(decodeOrder.getAmountReceived());
        orderFromFrontEnd.setCustomerID(decodeOrder.getCustomerID());
        orderFromFrontEnd.setIsPayment(decodeOrder.getIsPayment());
        orderFromFrontEnd.setUserID(decodeOrder.getUserID());
        orderFromFrontEnd.setPointOfSaleID(decodeOrder.getPointOfSaleID());
        orderFromFrontEnd.setPlaceID(decodeOrder.getPlaceID());

        return orderFromFrontEnd;
    }

    public ProductTab getProduct(String product) {
        Gson gson = new Gson();
        return gson.fromJson(product, ProductTab.class);
    }

    public StockTab getStock(String stock) {
        Gson gson = new Gson();
        return gson.fromJson(stock, StockTab.class);
    }

    public CategoryTab getCategory(String category) {
        Gson gson = new Gson();
        return gson.fromJson(category, CategoryTab.class);
    }
}

class DecodeOrder {
    ArrayList<ProductOrderQuant> orders;
    String amountReceived;
    String userID;
    String customerID;
    String pointOfSaleID;
    boolean isPayment;
    String placeID;

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }


    public String getPointOfSaleID() {
        return pointOfSaleID;
    }

    public void setPointOfSaleID(String pointOfSaleID) {
        this.pointOfSaleID = pointOfSaleID;
    }

    public void setIsPayment(boolean val) {
        this.isPayment = val;
    }

    public boolean getIsPayment() {
        return this.isPayment;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public ArrayList<ProductOrderQuant> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<ProductOrderQuant> orders) {
        this.orders = orders;
    }

    public String getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(String amountReceived) {
        this.amountReceived = amountReceived;
    }
}

class DecodeOrderFrontAv extends OrderFront {
    ArrayList<ProductOrder> productOrders;
    Place place;

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public ArrayList<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(ArrayList<ProductOrder> productOrders) {
        this.productOrders = productOrders;
    }
}

class DecodeOrderFrontAvAv extends DecodeOrderFrontAv {
    float amountReceived;
    String pointOfSaleID;

    public String getPointOfSaleID() {
        return pointOfSaleID;
    }

    public void setPointOfSaleID(String pointOfSaleID) {
        this.pointOfSaleID = pointOfSaleID;
    }

    public float getAmountreceived() {
        return amountReceived;
    }

    public void setAmountreceived(float amountreceived) {
        this.amountReceived = amountreceived;
    }
}