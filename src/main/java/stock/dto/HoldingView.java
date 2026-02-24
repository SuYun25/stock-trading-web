package stock.dto;

public class HoldingView {
    private int accountId;
    private int stockId;
    private String stockName;

    private int quantity;
    private int avgPrice;
    private int currentPrice;

    private int evalAmount;   // 현재가 * 수량
    private int costAmount;   // 평단 * 수량
    private int profit;       // 평가손익

    public HoldingView() {}

    public HoldingView(int accountId, int stockId, String stockName,
                       int quantity, int avgPrice, int currentPrice,
                       int evalAmount, int costAmount, int profit) {
        this.accountId = accountId;
        this.stockId = stockId;
        this.stockName = stockName;
        this.quantity = quantity;
        this.avgPrice = avgPrice;
        this.currentPrice = currentPrice;
        this.evalAmount = evalAmount;
        this.costAmount = costAmount;
        this.profit = profit;
    }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public int getStockId() { return stockId; }
    public void setStockId(int stockId) { this.stockId = stockId; }

    public String getStockName() { return stockName; }
    public void setStockName(String stockName) { this.stockName = stockName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getAvgPrice() { return avgPrice; }
    public void setAvgPrice(int avgPrice) { this.avgPrice = avgPrice; }

    public int getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(int currentPrice) { this.currentPrice = currentPrice; }

    public int getEvalAmount() { return evalAmount; }
    public void setEvalAmount(int evalAmount) { this.evalAmount = evalAmount; }

    public int getCostAmount() { return costAmount; }
    public void setCostAmount(int costAmount) { this.costAmount = costAmount; }

    public int getProfit() { return profit; }
    public void setProfit(int profit) { this.profit = profit; }
}
