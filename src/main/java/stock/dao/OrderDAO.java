package stock.dao;

import java.sql.*;
import java.util.ArrayList;

import stock.dto.OrderView;
import stock.util.DBUtil;

public class OrderDAO {

    // 주문 기록 저장 (BUY / SELL)
    public void insertOrder(int accountId, int stockId, int qty, int price, String type) throws Exception {

        String sql = """
            INSERT INTO ORDERS
            (order_id, account_id, stock_id, order_type, quantity, price_at_trade, trade_date)
            VALUES (seq_order.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE)
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ps.setInt(2, stockId);
            ps.setString(3, type);
            ps.setInt(4, qty);
            ps.setInt(5, price);

            ps.executeUpdate();
        }
    }

    // 계좌별 거래내역 조회 (모든 종목 표시됨)
    public ArrayList<OrderView> getOrders(int accountId) throws Exception {

        ArrayList<OrderView> list = new ArrayList<>();

        String sql = """
            SELECT
                o.order_id,
                o.account_id,
                s.stock_name,
                o.order_type,
                o.quantity,
                o.price_at_trade,
                TO_CHAR(o.trade_date, 'YYYY-MM-DD') AS trade_date
            FROM orders o
            JOIN stocks s ON o.stock_id = s.stock_id
            WHERE o.account_id = ?
            ORDER BY o.order_id DESC
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new OrderView(
                    rs.getInt("order_id"),
                    rs.getInt("account_id"),
                    rs.getString("stock_name"),
                    rs.getString("order_type"),
                    rs.getInt("quantity"),
                    rs.getInt("price_at_trade"),
                    rs.getString("trade_date")
                ));
            }
        }
        return list;
    }
}
