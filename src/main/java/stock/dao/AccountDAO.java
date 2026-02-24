package stock.dao;

import java.sql.*;
import java.util.ArrayList;
import stock.dto.Account;
import stock.util.DBUtil;

public class AccountDAO {

    // 계좌번호 중복 존재 여부
    public boolean existsAccountId(int accountId) throws Exception {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT 1 FROM ACCOUNTS WHERE account_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, accountId);
        ResultSet rs = ps.executeQuery();
        boolean exists = rs.next();
        conn.close();
        return exists;
    }

    // 내 계좌인지 여부
    public boolean isMyAccount(int accountId, int userId) throws Exception {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT 1 FROM ACCOUNTS WHERE account_id = ? AND user_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, accountId);
        ps.setInt(2, userId);
        ResultSet rs = ps.executeQuery();
        boolean ok = rs.next();
        conn.close();
        return ok;
    }

    // 계좌 개설 (중복이면 -10 리턴)
    public int createAccount(int userId, int accountId) throws Exception {
        // 중복 체크
        if (existsAccountId(accountId)) return -10;

        Connection conn = DBUtil.getConnection();
        String sql = "INSERT INTO ACCOUNTS (account_id, user_id, balance, create_date) VALUES (?, ?, 0, SYSDATE)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, accountId);
        ps.setInt(2, userId);

        int result = 0;
        try {
            result = ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            // 혹시 모를 동시성 중복 (PK 충돌)
            result = -10;
        } finally {
            conn.close();
        }
        return result;
    }

    // 유저 계좌 전체 조회
    public ArrayList<Account> getAccounts(int userId) throws Exception {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT account_id, user_id, balance, TO_CHAR(create_date,'YYYY-MM-DD') AS create_date " +
                     "FROM ACCOUNTS WHERE user_id = ? ORDER BY account_id";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();
        ArrayList<Account> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new Account(
                    rs.getInt("account_id"),
                    rs.getInt("user_id"),
                    rs.getInt("balance"),
                    rs.getString("create_date")
            ));
        }
        conn.close();
        return list;
    }

    // 특정 계좌 1개 조회 (내 계좌 확인 포함)
    public Account getAccount(int accountId, int userId) throws Exception {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT account_id, user_id, balance, TO_CHAR(create_date,'YYYY-MM-DD') AS create_date " +
                     "FROM ACCOUNTS WHERE account_id = ? AND user_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, accountId);
        ps.setInt(2, userId);

        ResultSet rs = ps.executeQuery();
        Account acc = null;

        if (rs.next()) {
            acc = new Account(
                    rs.getInt("account_id"),
                    rs.getInt("user_id"),
                    rs.getInt("balance"),
                    rs.getString("create_date")
            );
        }
        conn.close();
        return acc;
    }

    // 잔고 업데이트
    public int updateBalance(int accountId, int newBalance) throws Exception {
        Connection conn = DBUtil.getConnection();
        String sql = "UPDATE ACCOUNTS SET balance = ? WHERE account_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, newBalance);
        ps.setInt(2, accountId);
        int result = ps.executeUpdate();
        conn.close();
        return result;
    }

    // 계좌 삭제 (내 계좌만)
    public int deleteAccount(int accountId, int userId) throws Exception {
        Connection conn = DBUtil.getConnection();
        String sql = "DELETE FROM ACCOUNTS WHERE account_id = ? AND user_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, accountId);
        ps.setInt(2, userId);
        int result = ps.executeUpdate();
        conn.close();
        return result;
    }

    // 충전 (0 이하 금액 방지: -5)
    public int deposit(int accountId, int amount, int userId) throws Exception {
        if (amount <= 0) return -5;

        Connection conn = DBUtil.getConnection();
        String sql = "UPDATE ACCOUNTS SET balance = balance + ? WHERE account_id = ? AND user_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, amount);
        ps.setInt(2, accountId);
        ps.setInt(3, userId);

        int result = ps.executeUpdate();
        conn.close();
        return result; // 1이면 성공, 0이면 내 계좌 아님/없음
    }

    // 이체 (트랜잭션)
    //  1: 성공
    // -1: 출금계좌가 내 계좌가 아님/없음
    // -2: 잔액부족
    // -3: 입금계좌 없음
    // -5: 금액 오류
    public int transfer(int fromAcc, int toAcc, int amount, int userId) throws Exception {
        if (amount <= 0) return -5;
        if (fromAcc == toAcc) return -5;

        Connection conn = DBUtil.getConnection();
        conn.setAutoCommit(false);

        try {
            // 1) 출금 계좌 확인 + 잔액 조회 (내 계좌만)
            String sql1 = "SELECT balance FROM ACCOUNTS WHERE account_id = ? AND user_id = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, fromAcc);
            ps1.setInt(2, userId);
            ResultSet rs1 = ps1.executeQuery();

            if (!rs1.next()) {
                conn.rollback();
                return -1;
            }

            int balance = rs1.getInt("balance");
            if (balance < amount) {
                conn.rollback();
                return -2;
            }

            // 2) 입금 계좌 존재 확인
            String sql2 = "SELECT 1 FROM ACCOUNTS WHERE account_id = ?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, toAcc);
            ResultSet rs2 = ps2.executeQuery();

            if (!rs2.next()) {
                conn.rollback();
                return -3;
            }

            // 3) 출금
            String sql3 = "UPDATE ACCOUNTS SET balance = balance - ? WHERE account_id = ?";
            PreparedStatement ps3 = conn.prepareStatement(sql3);
            ps3.setInt(1, amount);
            ps3.setInt(2, fromAcc);
            ps3.executeUpdate();

            // 4) 입금
            String sql4 = "UPDATE ACCOUNTS SET balance = balance + ? WHERE account_id = ?";
            PreparedStatement ps4 = conn.prepareStatement(sql4);
            ps4.setInt(1, amount);
            ps4.setInt(2, toAcc);
            ps4.executeUpdate();

            conn.commit();
            return 1;

        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
    public int getAccountIdByUserId(int userId) throws Exception {
        String sql = "SELECT account_id FROM ACCOUNTS WHERE user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("account_id");
            }
        }
        return 0;
    }


}
