package restaurantmanager;

import connexion.ConnGen;
import restaurantmanager.table.InvoiceDetailTab;
import restaurantmanager.table.InvoiceTab;
import restaurantmanager.table.OrderTab;
import restaurantmanager.table.PaymentTab;

import java.sql.Timestamp;

public class Order {

    public boolean makeOrder(ConnGen connGen, String orderJSON) throws Exception {
        JSONData jsonData = new JSONData();
        OrderFromFrontEnd orderFromFrontEnd = jsonData.getOrder(orderJSON);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String todayDate = timestamp.toString();

        return makeOrder(connGen, orderFromFrontEnd.getOrders(), todayDate, orderFromFrontEnd.getAmountReceived(),
                orderFromFrontEnd.getCustomerID(), orderFromFrontEnd.getUserID(), orderFromFrontEnd.getIsPayment(), orderFromFrontEnd.getPointOfSaleID(),
                orderFromFrontEnd.getPlaceID());
    }

    public boolean makeOrder(ConnGen connGen, ProductOrderQuant[] productOrderQuants, String date, String amountReceived,
            String customerID, String userID, boolean isPayment, String pointOfSaleID, String placeID) throws Exception {

        ProductOrder[] productOrders = new ProductOrder[productOrderQuants.length];
        float totalAmount = 0;

        for (int iP = 0; iP < productOrderQuants.length; iP++) {
            float subTotal = productOrderQuants[iP].getQuantity() * productOrderQuants[iP].getPrice();
            productOrders[iP] = new ProductOrder(productOrderQuants[iP].getProductName(), productOrderQuants[iP].getPrice(),
                     String.valueOf(productOrderQuants[iP].getQuantity()), String.valueOf(subTotal));
            totalAmount += (subTotal);
        }

        float amountReceiv = 0;
        if (amountReceived.isEmpty()) {
            amountReceiv = totalAmount * (-1);
        } else {
            amountReceiv = Float.parseFloat(amountReceived);
        }
        float givenAmount = amountReceiv - totalAmount;

        AppData appData = new AppData(connGen);
        String ticketNumber = appData.getTodayNextTickerNumber();
        // INSERT INTO DATABASE
        OrderTab orderTab = new OrderTab(date, userID, customerID, placeID);
        appData.insertOrder(connGen, orderTab);

        String currOrderID = appData.getCurrentOrderID(connGen);
        InvoiceTab invoiceTab = new InvoiceTab(ticketNumber, ticketNumber, currOrderID);
        appData.insertInvoice(connGen, invoiceTab);
        String currInvoiceID = appData.getCurrentInvoiceID(connGen);
        for (int iP = 0; iP < productOrderQuants.length; iP++) {
            float amount = productOrderQuants[iP].getPrice() * productOrderQuants[iP].getQuantity();
            InvoiceDetailTab invoiceDetailTab = new InvoiceDetailTab(currInvoiceID, productOrderQuants[iP].getProductID(), productOrderQuants[iP].getQuantity(), amount);
            appData.insertInvoiceDetail(connGen, invoiceDetailTab);
        }
        amountReceiv = -1;
        if (isPayment) {

            float amountDue = amountReceiv - totalAmount;
            amountDue = -1;

            PaymentTab paymentTab = new PaymentTab(pointOfSaleID, currOrderID, amountReceiv, totalAmount, amountDue);
            appData.insertPayment(connGen, paymentTab);
        }

        LoseguentPrinter enxanetaPrinter = new LoseguentPrinter();

        if (isPayment) {
            return enxanetaPrinter.printReceipt(productOrders, date, NumberManipulation.currencyFormat(totalAmount),
                    NumberManipulation.currencyFormat(amountReceiv),
                    NumberManipulation.currencyFormat(givenAmount), ticketNumber, 2);
        } else {
            return enxanetaPrinter.printReceipt(productOrders, date, NumberManipulation.currencyFormat(totalAmount),
                    NumberManipulation.currencyFormat(amountReceiv),
                    NumberManipulation.currencyFormat(givenAmount), ticketNumber, 1);
        }
    }

    public boolean payOrder(ConnGen connGen, String orderJSON, boolean alsoPay) throws Exception {
        //TODO: FIND BETTER WAY TO HANDLE THIS FUNCTION, ISN'T IT BETTER TO JUST GET THE ORDERID THEN GET EVERYTHING ELSE FROM DATABASE?

        JSONData jsonData = new JSONData();
        OrderFrontAvAv orderFrontAvAv = jsonData.getOrderFrontAvAv(orderJSON);
        AppData appData = new AppData(connGen);
        OrderFront orderFront = appData.getOrderFront(connGen, orderFrontAvAv.getOrderID(), orderFrontAvAv.getInvoiceID());

        float totalAmount = orderFront.getTotalAmount();
        float amountReceiv = orderFrontAvAv.getAmountreceived();
        float amountDue = amountReceiv - totalAmount;
        amountDue = -1;
        amountReceiv = -1;

        if (alsoPay) {
            PaymentTab paymentTab = new PaymentTab(orderFrontAvAv.getPointOfSaleID(),
                    orderFrontAvAv.getOrderID(), amountReceiv, totalAmount, amountDue);
            appData.insertPayment(connGen, paymentTab);
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String todayDate = timestamp.toString();

        String ticketNumber = appData.getTicketNumber(connGen, orderFrontAvAv.getInvoiceID());

        if (alsoPay == false) {
            LoseguentPrinter enxanetaPrinter = new LoseguentPrinter();
            return enxanetaPrinter.printReceipt(orderFrontAvAv.getProductOrders(), todayDate, NumberManipulation.currencyFormat(totalAmount),
                    NumberManipulation.currencyFormat(amountReceiv),
                    NumberManipulation.currencyFormat(amountDue), ticketNumber, 1);
        } else {
            return true;
        }

    }

    public void updateOrder(ConnGen connGen, String orderFrontJSON) throws Exception {
        AppData appData = new AppData(connGen);
        JSONData jsonData = new JSONData();
        OrderFrontAv orderFrontAv = jsonData.getOrderFrontAV(orderFrontJSON);
        appData.deleteInvoiceDetails(connGen, orderFrontAv.getInvoiceID());

        for (int iP = 0; iP < orderFrontAv.getProductOrders().length; iP++) {

            float totalAmount = orderFrontAv.getProductOrders()[iP].getPrice() * Integer.parseInt(orderFrontAv.getProductOrders()[iP].getQuantity());
            InvoiceDetailTab invoiceDetailTab = new InvoiceDetailTab(orderFrontAv.getInvoiceID(), orderFrontAv
                    .getProductOrders()[iP].getProductID(), Integer.parseInt(orderFrontAv.getProductOrders()[iP].getQuantity()), totalAmount);
            appData.insertInvoiceDetail(connGen, invoiceDetailTab);
        }
    }

}
