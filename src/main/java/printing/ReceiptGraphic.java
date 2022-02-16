package printing;

import restaurantmanager.ProductOrder;

import java.awt.*;

public class ReceiptGraphic {
    private ProductOrder[] orderedProducts;
    private String date;
    private String companyName;
    private String companyLocation;
    private String receivedAmount;
    private String totalAmount;
    private String givenAmount;
    private String tickerNumber;

    public ReceiptGraphic(ProductOrder[] orderedProducts, String date, String companyName,
                          String companyLocation, String receivedAmount, String totalAmount, String givenAmount, String tickerNumber) {
        this.orderedProducts = orderedProducts;
        this.date = date;
        this.companyName = companyName;
        this.companyLocation = companyLocation;
        this.receivedAmount = receivedAmount;
        this.totalAmount = totalAmount;
        this.givenAmount = givenAmount;
        this.tickerNumber = tickerNumber;
    }

    public void setReceiptGraphic(Graphics2D g2d) {

        int y = 5;
        int yShift = 15;
        int headerRectHeight = 20;

        int alea = 2;

        y += yShift;
        g2d.setFont(new Font("Monospaced", Font.BOLD, 10));
        g2d.drawString("       " + getCompanyName(), alea, y);

        y += yShift;
        g2d.drawString("           " + getCompanyLocation(), alea, y);

        y += yShift;
        g2d.setFont(new Font("Monospaced", Font.BOLD, 9));
        g2d.drawString("     Tél: +261 34 91 244 70", alea, y);

        y += headerRectHeight;
        g2d.drawString("     --------------------------", alea, y);

        y += yShift;
        g2d.drawString("  " + getDate() + "       " + getTickerNumber(), alea, y);

        y += yShift;
        g2d.drawString("     --------------------------", alea, y);

        for(int iP = 0; iP < getOrderedProducts().length; iP++) {

            y += yShift;

            g2d.drawString("" + getOrderedProducts()[iP].getProductName(), alea, y);

            y += yShift;
            g2d.drawString(""  + getOrderedProducts()[iP].getQuantity()
                    + " x " + getOrderedProducts()[iP].getPrice(), alea, y);
            g2d.drawString(getOrderedProducts()[iP].getTotalAmount(), alea + 120, y);

        }

        y += yShift;
        g2d.drawString("     --------------------------", alea, y);

        y += yShift;
        g2d.drawString("       TOTAL TTC:      "+ getTotalAmount(), alea, y);
/*
        y += yShift;
        g2d.drawString("       ESPECES:        " +  getReceivedAmount(), alea, y);

        y += yShift;
        g2d.drawString("       RENDU:          " + getGivenAmount(), alea, y);
*/
        y += yShift;
        g2d.drawString("     --------------------------", alea, y);

        y += yShift;
        g2d.drawString("       Merci de votre visite", alea, y);

        y += yShift;
        g2d.drawString("          à très bientôt", alea, y);

        y += yShift;
        g2d.drawString("          www.enxaneta.cat", alea, y);
    }

    public ProductOrder[] getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(ProductOrder[] orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public String getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getGivenAmount() {
        return givenAmount;
    }

    public void setGivenAmount(String givenAmount) {
        this.givenAmount = givenAmount;
    }

    public String getTickerNumber() {
        return tickerNumber;
    }

    public void setTickerNumber(String tickerNumber) {
        this.tickerNumber = tickerNumber;
    }
}