package printing;

import java.awt.*;
import java.awt.print.*;
import javax.print.*;

public final class PrinterDocument implements Printable {

    private int numberOfPrints;
    private String printerName;
    private float margin = 2.0f;
    private ReceiptGraphic receiptGraphic;

    /*public static void main(String[] args) throws Exception {
        ProductOrder[] productOrders = new ProductOrder[2];
        productOrders[0] = new ProductOrder("crêpe", 15000, "1", "15 000,00 Ar");
        productOrders[1] = new ProductOrder("hamburger", 1500, "3", "45 000,00 Ar");
        String todayDate = "22/02/2020 19:26";
        String companyName = "ENXANETA MADAGASCAR";
        String companyLoc = "Majunga Be";
        String receivedAmount = "100 000,00 Ar";
        String totaAmount = "70 000,00 Ar";
        String givenAmount = "30 000,00 Ar";
        String ticketNumber = "001";

        ReceiptGraphic receiptGraphic = new ReceiptGraphic(productOrders,
                todayDate, companyName, companyLoc, receivedAmount, totaAmount, givenAmount, ticketNumber );
        PrinterDocument printerDocument = new PrinterDocument(1, "POS-80 (copy 2)", receiptGraphic);
        System.out.println(printerDocument.printReceipt());


        ProductOrderQuant[] productOrderQuants = new ProductOrderQuant[2];
        productOrderQuants[0] = new ProductOrderQuant("1", "crêpe", "15000", "1", "1");
        productOrderQuants[1] = new ProductOrderQuant("1", "hamburger", "15000", "1", "1");



    }

     */
    public PrinterDocument(int numberOfPrints, String printerName, ReceiptGraphic receiptGraphic) {
        setNumberOfPrints(numberOfPrints);
        setPrinterName(printerName);
        setReceiptGraphic(receiptGraphic);
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {

        if (page > getNumberOfPrints()) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        getReceiptGraphic().setReceiptGraphic(g2d);

        return PAGE_EXISTS;
    }

    public boolean printReceipt() throws Exception {

        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printer : printServices) {
            if (printer.getName().compareTo(getPrinterName()) == 0) {
                PrinterJob printerJob = PrinterJob.getPrinterJob();
                printerJob.setPrintService(printer);

                PageFormat pageFormat = printerJob.defaultPage();
                Paper paper = new Paper();
                paper.setImageableArea(getMargin(), getMargin(), paper.getWidth() - getMargin() * 2,
                        paper.getHeight() - getMargin() * 2);
                pageFormat.setPaper(paper);

                printerJob.setPrintable(this, pageFormat);
                printerJob.print();
                return true;
            }

        }

        return false;

    }

    public float getMargin() {
        return margin;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    public int getNumberOfPrints() {
        return numberOfPrints;
    }

    public void setNumberOfPrints(int numberOfPrints) {
        if (numberOfPrints == 0) {
            this.numberOfPrints = 0;
        } else {
            numberOfPrints--;
            this.numberOfPrints = numberOfPrints;
        }
    }

    public ReceiptGraphic getReceiptGraphic() {
        return receiptGraphic;
    }

    public void setReceiptGraphic(ReceiptGraphic receiptGraphic) {
        this.receiptGraphic = receiptGraphic;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }
}
