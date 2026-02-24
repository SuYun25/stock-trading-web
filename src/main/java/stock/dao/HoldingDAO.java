package stock.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import stock.dto.Holding;
import stock.dto.HoldingView;
import stock.util.DBUtil;

public class HoldingDAO {

	// 특정 계좌의 전체 보유 주식 조회
	public ArrayList<Holding> getHoldingsByAccount(int accId) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "SELECT * FROM HOLDINGS WHERE account_id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, accId);

		ResultSet rs = ps.executeQuery();
		ArrayList<Holding> list = new ArrayList<>();

		while (rs.next()) {
			list.add(new Holding(rs.getInt("holding_id"), rs.getInt("account_id"), rs.getInt("stock_id"),
					rs.getInt("quantity"), rs.getInt("avg_price")));
		}

		conn.close();
		return list;
	}

	// 특정 종목 하나 조회
	public Holding getHolding(int accId, int stockId) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "SELECT * FROM HOLDINGS WHERE account_id = ? AND stock_id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, accId);
		ps.setInt(2, stockId);

		ResultSet rs = ps.executeQuery();
		Holding h = null;

		if (rs.next()) {
			h = new Holding(rs.getInt("holding_id"), rs.getInt("account_id"), rs.getInt("stock_id"),
					rs.getInt("quantity"), rs.getInt("avg_price"));
		}

		conn.close();
		return h;
	}

	// 신규 보유 추가
	public void insertHolding(int accId, int stockId, int qty, int price) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "INSERT INTO HOLDINGS VALUES (seq_holding.NEXTVAL, ?, ?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, accId);
		ps.setInt(2, stockId);
		ps.setInt(3, qty);
		ps.setInt(4, price);

		ps.executeUpdate();
		conn.close();
	}

	// 기존 보유 업데이트
	public void updateHolding(int holdingId, int newQty, int newAvg) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "UPDATE HOLDINGS SET quantity = ?, avg_price = ? WHERE holding_id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, newQty);
		ps.setInt(2, newAvg);
		ps.setInt(3, holdingId);

		ps.executeUpdate();
		conn.close();
	}

	// 수량 0이면 삭제
	public void deleteHolding(int holdingId) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "DELETE FROM HOLDINGS WHERE holding_id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, holdingId);

		ps.executeUpdate();
		conn.close();
	}

	// ⭐ 보유 종목 View용 (종목명 + 현재가 + 평가손익)
	public List<HoldingView> getHoldings(int accountId) throws Exception {

		List<HoldingView> list = new ArrayList<>();
		Connection conn = DBUtil.getConnection();

		String sql = """
				    SELECT
				        h.account_id,
				        h.stock_id,
				        s.stock_name,
				        h.quantity,
				        h.avg_price,
				        s.current_price,
				        (h.quantity * s.current_price) AS eval_amount,
				        (h.quantity * h.avg_price) AS cost_amount,
				        (h.quantity * s.current_price) - (h.quantity * h.avg_price) AS profit
				    FROM holdings h
				    JOIN stocks s ON h.stock_id = s.stock_id
				    WHERE h.account_id = ?
				""";

		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, accountId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			list.add(new HoldingView(rs.getInt("account_id"), rs.getInt("stock_id"), rs.getString("stock_name"),
					rs.getInt("quantity"), rs.getInt("avg_price"), rs.getInt("current_price"), rs.getInt("eval_amount"),
					rs.getInt("cost_amount"), rs.getInt("profit")));
		}

		conn.close();
		return list;
	}

}
