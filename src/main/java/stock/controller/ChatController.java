package stock.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import stock.dao.AccountDAO;
import stock.dao.HoldingDAO;
import stock.dto.Account;
import stock.dto.HoldingView;
import stock.dto.User;

@WebServlet("/chat")
public class ChatController extends HttpServlet {

    private final AccountDAO accountDAO = new AccountDAO();

    private User loginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("loginUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
            return null;
        }
        return user;
    }

    @SuppressWarnings("unchecked")
    private List<String[]> getHistory(HttpSession session) {
        Object o = session.getAttribute("chatHistory");
        if (o != null) return (List<String[]>) o;

        List<String[]> history = new ArrayList<>();
        history.add(new String[]{
            "bot",
            "안녕하세요! 🤖 Stock Trading 도우미입니다.\n\n"
          + "✔ 잔고 / 계좌 조회\n"
          + "✔ 계좌 개설\n"
          + "✔ 보유 종목 조회\n"
          + "✔ 거래내역 안내\n\n"
          + "예) 잔고, 계좌 개설 (숫자 8자리 이하), 보유 종목, 도움말"
        });
        session.setAttribute("chatHistory", history);
        return history;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        User user = loginUser(request, response);
        if (user == null) return;

        List<String[]> history = getHistory(request.getSession());
        request.setAttribute("history", history);
        request.getRequestDispatcher("/views/chat/chat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        User user = loginUser(request, response);
        if (user == null) return;

        HttpSession session = request.getSession();
        List<String[]> history = getHistory(session);

        String q = request.getParameter("q");
        if (q == null || q.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/chat");
            return;
        }

        q = q.trim();
        history.add(new String[]{"user", q});

        String answer;
        try {
            answer = generateAnswer(q, user);
        } catch (Exception e) {
            e.printStackTrace();
            answer = "⚠ 처리 중 오류가 발생했어요. 관리자에게 문의해 주세요.";
        }

        history.add(new String[]{"bot", answer});
        response.sendRedirect(request.getContextPath() + "/chat");
    }

    private String generateAnswer(String q, User user) throws Exception {
        String s = q.toLowerCase();

        // 도움말
        if (containsAny(s, "도움", "help", "가능", "뭐")) {
            return """
            📌 사용 가능한 기능

            • 잔고 / 계좌 조회
            • 계좌 개설 (계좌 개설 (숫자 8자리 이하))
            • 보유 종목 조회
            • 거래내역 안내
            • 계좌번호 형식
                   

            예)
            - 잔고
            - 계좌 개설 1001
            - 보유 종목
            - 거래내역
            """;
        }

        // 계좌번호 형식
        if (containsAny(s, "형식", "규칙")) {
            return """
            ✔ 계좌번호 규칙

            • 숫자만 가능
            • 최대 8자리
            • 하이픈(-), 문자 불가

            예) 1001 / 202401 / 77777777
            """;
        }

        // 계좌 개설
        if (s.startsWith("계좌 개설")) {
            String[] parts = s.split(" ");
            if (parts.length < 3) {
                return "👉 이렇게 입력해 주세요:\n계좌 개설 (숫자 8자리 이하) ex) 계좌 개설 123456";
            }

            int accountId;
            try {
                accountId = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                return "⚠ 계좌번호는 숫자만 입력해 주세요.";
            }

            if (String.valueOf(accountId).length() > 8) {
                return "⚠ 계좌번호는 8자리 이하만 가능합니다.";
            }

            int result = accountDAO.createAccount(user.getUserId(), accountId);
            if (result == -10) {
                return "❌ 이미 존재하는 계좌번호입니다.";
            }

            return "✅ 계좌 개설 완료!\n계좌번호: " + accountId;
        }

        // 잔고 / 계좌 조회
        if (containsAny(s, "잔고", "계좌", "조회")) {
            ArrayList<Account> list = accountDAO.getAccounts(user.getUserId());
            if (list.isEmpty()) {
                return "📭 계좌가 없습니다.\n👉 `계좌 개설 ex)123456` 처럼 입력해 주세요.";
            }

            StringBuilder sb = new StringBuilder("💼 내 계좌 현황\n");
            for (Account a : list) {
                sb.append("• ")
                  .append(a.getAccountId())
                  .append(" : ")
                  .append(a.getBalance())
                  .append(" 원\n");
            }
            return sb.toString();
        }

        // 보유 종목 조회
        if (containsAny(s, "보유", "보유종목", "내 주식")) {
            ArrayList<Account> accounts = accountDAO.getAccounts(user.getUserId());
            if (accounts.isEmpty()) {
                return "📭 계좌가 없습니다.";
            }

            Account acc = accounts.get(0);
            HoldingDAO holdingDAO = new HoldingDAO();
            List<HoldingView> list = holdingDAO.getHoldings(acc.getAccountId());

            if (list == null || list.isEmpty()) {
                return "📉 보유 중인 주식이 없습니다.";
            }

            int totalProfit = 0;
            StringBuilder sb = new StringBuilder("📊 보유 종목 요약\n");

            for (HoldingView h : list) {
                totalProfit += h.getProfit();
                sb.append("• ")
                  .append(h.getStockName())
                  .append(" ")
                  .append(h.getQuantity()).append("주 ")
                  .append("(")
                  .append(h.getProfit() > 0 ? "+" : "")
                  .append(h.getProfit())
                  .append(")\n");
            }

            sb.append("\n💰 총 평가손익: ")
              .append(totalProfit > 0 ? "+" : "")
              .append(totalProfit);

            return sb.toString();
        }

        // 거래내역
        if (containsAny(s, "거래", "주문", "체결")) {
            return "📄 거래 내역은 **[거래 내역] 메뉴**에서 확인할 수 있어요.";
        }

        return "🤔 이해하지 못했어요.\n`도움말`을 입력해 보세요!";
    }

    private boolean containsAny(String s, String... keys) {
        for (String k : keys) {
            if (s.contains(k.toLowerCase())) return true;
        }
        return false;
    }
}
