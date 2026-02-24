package stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderView {

    private int orderId;
    private int accountId;
    private String stockName;   // ✅ 종목명
    private String orderType;
    private int quantity;
    private int priceAtTrade;
    private String tradeDate;
}
