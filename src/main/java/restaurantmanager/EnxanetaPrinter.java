package restaurantmanager;

import printing.PrinterDocument;
import printing.ReceiptGraphic;

public class EnxanetaPrinter {
    public boolean printReceipt(ProductOrder[] productOrders, String date, String totalAmount,
                             String amountReceived, String givenAmount, String ticketNumber, int numberOfPrints) throws Exception {

        String companyName = "ENXANETA MADAGASCAR";
        String companyLoc = "Majunga Be";

        ReceiptGraphic receiptGraphic = new ReceiptGraphic(productOrders,
                date, companyName, companyLoc, amountReceived, totalAmount, givenAmount, ticketNumber );
        PrinterDocument printerDocument = new PrinterDocument(numberOfPrints, "POS-80", receiptGraphic);
        return printerDocument.printReceipt();
    }
}
