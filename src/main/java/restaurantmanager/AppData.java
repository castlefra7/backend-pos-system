package restaurantmanager;

import java.sql.Timestamp;
import java.util.Arrays;

import connexion.ConnGen;
import outils.FctGen;
import restaurantmanager.graph.BigStatistic;
import restaurantmanager.graph.Label;
import restaurantmanager.graph.LineData;
import restaurantmanager.table.CategoryTab;
import restaurantmanager.table.CategoryTab1;
import restaurantmanager.table.InvoiceDetailTab;
import restaurantmanager.table.InvoiceTab;
import restaurantmanager.table.OrderTab;
import restaurantmanager.table.PaymentTab;
import restaurantmanager.table.ProductTab;
import restaurantmanager.table.ProductTab1;
import restaurantmanager.table.StockTab;
import restaurantmanager.table.StockTabAv;

public final class AppData {

    ConnGen connGen;

    public AppData() throws Exception {
        setConnGen(new ConnGen());
    }

    public AppData(ConnGen connGen) {
        setConnGen(connGen);
    }


    /* BEGIN STAT */
    public BigStatistic getStatistics(ConnGen connGen, int month, int year) throws Exception { // MONTH IS [1-12]

        String totalOrders = "select count(*) as count from accounting.totalOrders where month = " + (month) + " and year = " + year;
        String totalTurnover = "select sum(amount) as sum from accounting.productOrdersWithDate where month = " + (month) + " and year = " + year;
        String totalProductsSold = "select sum(quantity) as quantity from accounting.productOrdersWithDate where month = " + (month) + " and year = " + year;
        FctGen fctGen = new FctGen();
        String temp1 = fctGen.findOne(connGen, totalOrders, "count");
        if (temp1.isEmpty()) {
            temp1 = "0";
        }
        int resTotal = Integer.parseInt(temp1);
        String temp2 = fctGen.findOne(connGen, totalTurnover, "sum");
        if (temp2.isEmpty()) {
            temp2 = "0";
        }
        float resTurn = Float.parseFloat(temp2);
        String temp3 = fctGen.findOne(connGen, totalProductsSold, "quantity");
        if (temp3.isEmpty()) {
            temp3 = "0";
        }
        int resTotalProducts = Integer.parseInt(temp3);

        setUPCalendar(connGen, month, year);

        BigStatistic bigStatistic = new BigStatistic(resTotal, resTurn, resTotalProducts, getLineDataSalesPerDay(connGen, month, year), getLabelsSalesPerDay(connGen, month,
                 year), getCategoriesDistribution(connGen, month, year), getTop5SoldProducts(connGen, month, year));

        return bigStatistic;
    }

    public LineData[] getTop5SoldProducts(ConnGen connGen, int month, int year) throws Exception {
        String req = "select accounting.top5SoldProducts.*, accounting.products.productName as info from "
                + "accounting.top5SoldProducts join accounting.products on accounting.top5SoldProducts.index = "
                + "accounting.products.productID order by accounting.top5SoldProducts.data DESC ";

        Object[] all = getAll(connGen, new LineData(), req);
        return Arrays.copyOf(all, all.length, LineData[].class);
    }

    public LineData[] getCategoriesDistribution(ConnGen connGen, int month, int year) throws Exception {
        String req = "select accounting.categorySalesDistribution.*, accounting.productCategories.categoryName as info  from "
                + "accounting.categorySalesDistribution join  accounting.productCategories on accounting.productCategories.categoryID = "
                + "accounting.categorySalesDistribution.index order by accounting.categorySalesDistribution.data DESC";

        Object[] all = getAll(connGen, new LineData(), req);
        return Arrays.copyOf(all, all.length, LineData[].class);
    }

    public Label[] getLabelsSalesPerDay(ConnGen connGen, int month, int year) throws Exception {
        String req = "select * from  accounting.fullMonthDays_1";

        Object[] all = getAll(connGen, new Label(), req);
        return Arrays.copyOf(all, all.length, Label[].class);
    }

    public LineData[] getLineDataSalesPerDay(ConnGen connGen, int month, int year) throws Exception {

        String req3 = "create or replace view accounting.salesMonthDays as\n"
                + "select sum(amount) as data, extract(day from orderdate) as index from accounting.dailySalesMonthYear "
                + "group by extract(day from orderdate)";
        connGen.insertResult(req3);

        String req4 = "select accounting.fullMonthDays.day as index, coalesce(accounting.salesMonthDays.data, 0) as data, '' as info from "
                + "accounting.fullMonthDays left join accounting.salesMonthDays on accounting.salesMonthDays.index = "
                + "accounting.fullMonthDays.day";

        Object[] all = getAll(connGen, new LineData(), req4);
        return Arrays.copyOf(all, all.length, LineData[].class);
    }

    public void setUPCalendar(ConnGen connGen, int month, int year) throws Exception {
        String req = "create or replace view accounting.dailySalesMonthYear as select * from accounting.dailySales where "
                + " extract(month from orderdate) =  " + month + " and extract(year from orderdate) = " + year;
        connGen.insertResult(req);
        String req1 = "create or replace view accounting.fullYearDays as   \n"
                + "    select generate_series( (date '" + year + "-01-01')::timestamp,\n"
                + "                            (date '" + year + "-12-31')::timestamp,\n"
                + "                            interval '1 day'\n"
                + "                            ) as date";
        connGen.insertResult(req1);
        String req2 = "create or replace view accounting.fullMonthDays as\n"
                + "    select extract(day from date) as day from accounting.fullYearDays where extract(month from date) = " + month;
        connGen.insertResult(req2);
        String req3 = "create or replace view accounting.fullMonthDays_1 as\n"
                + "    select extract(day from date) as data from accounting.fullYearDays where extract(month from date) = " + month;
        connGen.insertResult(req3);
        String req4 = "create or replace view accounting.totalOrdersSpecifiDate as\n"
                + "    select * from accounting.totalOrders where month = " + month + " and year = " + year;
        connGen.insertResult(req4);
    }

    /* END STAT */
    public void updateStock(ConnGen connGen, String stockID, float initialQuantity, float realQuantityLeft) throws Exception {
        String req = "UPDATE accounting.stocks set isUpdated='y', realQuantityLeft=" + realQuantityLeft
                + " " //, initialQuantity=" + initialQuantity
                + " where stockID='" + stockID + "'";
        connGen.insertResult(req);
    }

    public StockTabAv[] getAllStocks() throws Exception {
        String req = "select accounting.stocks.*, accounting.products.productName from accounting.stocks join accounting.products on accounting.products.productID = accounting.stocks.productID  order by accounting.stocks.stockDate DESC, cast(accounting.stocks.stockID as integer) DESC";
        Object[] all = getAll(new StockTabAv(), req);
        return Arrays.copyOf(all, all.length, StockTabAv[].class);
    }

    public void insertStock(StockTab stockTab) throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        stockTab.setEditDate(timestamp.toString());
        stockTab.setIsUpdated('n');

        FctGen fctGen = new FctGen();
        fctGen.insertBdd(stockTab, "accounting.stocks", "accounting.stockIDSeq", "stockID");
    }

    public Place getPlace(ConnGen connGen, String placeID) throws Exception {
        String req = "select * from accounting.places where placeID = " + placeID;
        return (Place) get(connGen, new Place(), req);
    }

    public Place[] getAllPlaces(ConnGen connGen) throws Exception {
        String req = "select * from accounting.places order by placeName";
        Object[] all = getAll(connGen, new Place(), req);
        return Arrays.copyOf(all, all.length, Place[].class);
    }

    public void deleteCategory(ConnGen connGen, String categoryID) throws Exception {
        String req = "update accounting.productCategories set isActive='n' where categoryID = '" + categoryID + "'";
        connGen.insertResult(req);
    }

    public void deleteProduct(ConnGen connGen, String productID) throws Exception {
        String req = "update accounting.products set isActive='n' where productID ='" + productID + "'";
        connGen.insertResult(req);
    }

    public OrderFront getOrderFront(ConnGen connGen, String orderID, String invoiceID) throws Exception {
        String req = " select * from accounting.allUnpaidOrders where orderID = '" + orderID + "' and invoiceID ='"
                + invoiceID + "'";

        Object object = get(connGen, new OrderFront(), req);
        if (object == null) {
            return null;
        }
        return (OrderFront) object;
    }

    public String getTicketNumber(ConnGen connGen, String invoiceID) throws Exception {
        String req = "select ticketnumber from accounting.invoices where invoiceID = '" + invoiceID + "'";
        FctGen fctGen = new FctGen();
        return fctGen.findOne(connGen, req, "ticketnumber");
    }

    public OrderFrontAv[] getUnpaidOrdersAvFront(ConnGen connGen, String placeID) throws Exception {
        OrderFront[] orderFronts = getUnpaidOrdersFront(connGen, placeID);
        return returnOrderFrontAv(orderFronts);
    }

    public OrderFrontAv[] getUnpaidOrdersAvFront(ConnGen connGen) throws Exception {
        OrderFront[] orderFronts = getUnpaidOrdersFront(connGen);
        return returnOrderFrontAv(orderFronts);
    }

    public OrderFrontAv[] returnOrderFrontAv(OrderFront[] orderFronts) throws Exception {
        OrderFrontAv[] orderFrontAvs = new OrderFrontAv[orderFronts.length];
        for (int iO = 0; iO < orderFronts.length; iO++) {
            orderFrontAvs[iO] = new OrderFrontAv(orderFronts[iO]);
            orderFrontAvs[iO].setProductOrders(getProductsOrder(connGen, orderFrontAvs[iO].getInvoiceID()));
            orderFrontAvs[iO].setPlace(getPlace(connGen, orderFronts[iO].getPlaceId()));
        }

        return orderFrontAvs;
    }

    public ProductOrder[] getProductsOrder(ConnGen connGen, String invoiceID) throws Exception {
        String req = "select productID,categoryID,productName,price,productImage,quantity,amount as totalAmount from accounting.allOrderedProducts where invoiceID='"
                + invoiceID + "'";
        Object[] all = getAll(connGen, new ProductOrder(), req);
        return Arrays.copyOf(all, all.length, ProductOrder[].class);
    }

    public OrderFront[] getUnpaidOrdersFront(ConnGen connGen, String placeID) throws Exception {
        String req = "select * from accounting.allUnpaidOrders where placeID = " + placeID + " order by orderDate DESC";
        Object[] all = getAll(connGen, new OrderFront(), req);
        return Arrays.copyOf(all, all.length, OrderFront[].class);
    }

    public OrderFront[] getUnpaidOrdersFront(ConnGen connGen) throws Exception {
        String req = "select * from accounting.allUnpaidOrders order by orderDate DESC";
        Object[] all = getAll(connGen, new OrderFront(), req);
        return Arrays.copyOf(all, all.length, OrderFront[].class);
    }

    public int deleteInvoiceDetails(ConnGen connGen, String invoiceID) throws Exception {
        String req = "DELETE FROM accounting.invoiceDetails where invoiceID = '" + invoiceID + "'";
        return connGen.insertResult(req);
    }

    public int updateProduct(ConnGen connGen, ProductTab productTab) throws Exception {
        String req = "UPDATE accounting.products SET categoryID = '" + productTab.getCategoryID() + "', productName = '"
                + productTab.getProductName() + "', price = " + productTab.getPrice() + ", productImage = '"
                + productTab.getProductImage() + "' where productID = '" + productTab.getProductID() + "'";
        int result = connGen.insertResult(req);
        return result;
    }

    public String getCurrentInvoiceID(ConnGen connGen) throws Exception {
        String req = "select currVal('accounting.invoiceIDSeq') as nextValue";
        FctGen fctGen = new FctGen();
        return fctGen.findOne(connGen, req, "nextValue");
    }

    public String getCurrentOrderID(ConnGen connGen) throws Exception {
        String req = "select currVal('accounting.ordersIDSeq') as nextValue";
        FctGen fctGen = new FctGen();
        return fctGen.findOne(connGen, req, "nextValue");
    }

    public void insertInvoiceDetail(ConnGen connGen, InvoiceDetailTab invoiceDetailTab) throws Exception {
        FctGen fctGen = new FctGen();
        fctGen.insertBdd(connGen, invoiceDetailTab, "accounting.invoiceDetails", "", "");
    }

    public void insertInvoice(ConnGen connGen, InvoiceTab invoiceTab) throws Exception {
        FctGen fctGen = new FctGen();
        fctGen.insertBdd(connGen, invoiceTab, "accounting.invoices", "accounting.invoiceIDSeq", "invoiceID");
    }

    public void insertPayment(ConnGen connGen, PaymentTab paymentTab) throws Exception {
        FctGen fctGen = new FctGen();
        fctGen.insertBdd(connGen, paymentTab, "accounting.payments", "", "");
    }

    public void insertOrder(ConnGen connGen, OrderTab orderTab) throws Exception {
        FctGen fctGen = new FctGen();
        fctGen.insertBdd(connGen, orderTab, "accounting.orders", "accounting.ordersIDSeq", "orderID");
    }

    public void insertCategory(CategoryTab categoryTab) throws Exception {
        FctGen fctGen = new FctGen();
        CategoryTab1 categoryTab1 = new CategoryTab1(categoryTab.getCategoryID(), categoryTab.getCategoryName(), categoryTab.getCategoryTaste(), categoryTab.getCategoryImage());
        fctGen.insertBdd(getConnGen(), categoryTab1, "accounting.productCategories",
                "accounting.categoryIDSeq", "categoryID");
        getConnGen().getConn().commit();
    }

    public void insertProduct(ProductTab product) throws Exception {
        FctGen fctGen = new FctGen();
        ProductTab1 productTab1 = new ProductTab1(product.getProductID(), product.getCategoryID(), product.getProductName(),
                product.getProductImage(), product.getPrice());
        fctGen.insertBdd(productTab1, "accounting.products",
                "accounting.productsIDSeq", "productID");
    }

    public String getTodayNextTickerNumber() throws Exception {
        String req = "select nextVal('accounting.ticketNumberSeq') as nextValue";
        FctGen fctGen = new FctGen();
        return fctGen.findOne(req, "nextValue");
    }

    public CategoryTab[] getAllCategories() throws Exception {
        String req = "select * from accounting.productCategories where isActive='y' order by categoryID ASC";
        Object[] all = getAll(new CategoryTab(), req);
        return Arrays.copyOf(all, all.length, CategoryTab[].class);
    }

    public ProductTab[] getAllProducts(String categoryID) throws Exception {
        String req = "select * from accounting.products where isActive='y' and accounting.products.categoryID= '" + categoryID + "' order by productID DESC";
        Object[] all = getAll(new ProductTab(), req);
        return Arrays.copyOf(all, all.length, ProductTab[].class);
    }

    public ProductTab[] getAllProducts() throws Exception {
        String req = "select * from accounting.products where isActive='y' order by productID DESC";
        Object[] all = getAll(new ProductTab(), req);
        return Arrays.copyOf(all, all.length, ProductTab[].class);
    }

    Object[] getAll(Object base, String req) throws Exception {
        ConnGen conn = null;
        try {
            conn = getConnGen();
            return getAll(conn, base, req);
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (conn != null) {
                if (conn.getConn() != null) {
                    conn.getConn().close();
                }
            }
        }

    }

    Object[] getAll(ConnGen connGen, Object base, String req) throws Exception {
        FctGen gen = new FctGen();
        Class[] cls = null;
        Object[] val = gen.findAll(connGen, base.getClass().getConstructor(cls).newInstance(), req);
        if (val == null) {
            return new Object[0];
        }
        return val;
    }

    Object get(ConnGen connex, Object base, String req) throws Exception {
        FctGen gen = new FctGen();
        Class[] cls = null;
        Object[] val = gen.findAll(connex, base.getClass().getConstructor(cls).newInstance(), req);
        if (val == null) {
            return null;
        }
        return val[0];
    }

    public ConnGen getConnGen() {
        return connGen;
    }

    public void setConnGen(ConnGen connGen) {
        this.connGen = connGen;
    }
}
