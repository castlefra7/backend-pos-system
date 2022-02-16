package restaurantmanager.graph;

public final class BigStatistic {

    private int totalOrders;
    private float totalTurnover;
    private int totalProductsSold;
    private LineData[] lineData;
    private Label[] label;
    private LineData[] pieData;
    private LineData[] horizontalData;

    public BigStatistic(int totalOrders, float totalTurnover, int totalProductsSold, LineData[] lineData, Label[] label, LineData[] pieData,
            LineData[] horizontalData) {
        setTotalOrders(totalOrders);
        setTotalTurnover(totalTurnover);
        setTotalProductsSold(totalProductsSold);
        setLabel(label);
        setLineData(lineData);
        setPieData(pieData);
        setHorizontalData(horizontalData);
    }

    public LineData[] getHorizontalData() {
        return horizontalData;
    }

    public void setHorizontalData(LineData[] horizontalData) {
        this.horizontalData = horizontalData;
    }

    public LineData[] getPieData() {
        return pieData;
    }

    public void setPieData(LineData[] pieData) {
        this.pieData = pieData;
    }

    public Label[] getLabel() {
        return label;
    }

    public void setLabel(Label[] label) {
        this.label = label;
    }

    public LineData[] getLineData() {
        return lineData;
    }

    public void setLineData(LineData[] lineData) {
        this.lineData = lineData;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public float getTotalTurnover() {
        return totalTurnover;
    }

    public void setTotalTurnover(float totalTurnover) {
        this.totalTurnover = totalTurnover;
    }

    public int getTotalProductsSold() {
        return totalProductsSold;
    }

    public void setTotalProductsSold(int totalProductsSold) {
        this.totalProductsSold = totalProductsSold;
    }
}
