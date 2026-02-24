package stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private int orderId;        // DB PK (화면에 직접 안 써도 됨)
    private int accountId;
    private String stockName;   // ⭐ 종목명
    private String orderType;   // BUY / SELL
    private int quantity;
    private int priceAtTrade;
    private String tradeDate;
}
