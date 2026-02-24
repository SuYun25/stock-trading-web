package stock.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import stock.dao.AccountDAO;
import stock.dao.HoldingDAO;
import stock.dao.StockDAO;
import stock.dto.*;
import stock.service.StockService;

@WebServlet("/trade")
public class TradeController extends HttpServlet {

    private final AccountDAO accountDAO = new AccountDAO();
    private final StockDAO stockDAO = new StockDAO();
    private final HoldingDAO holdingDAO = new HoldingDAO();
    private final StockService stockService = new StockService();

    // ---------------- 공통 ----------------
    private User loginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session == null) ? null : (User) session.getAttribute("loginUser");
    }

    private int intParam(HttpServletRequest request, String name, int def) {
        try {
            String v = request.getParameter(name);
            if (v == null) return def;
            return Integer.parseInt(v.trim());
        } catch (Exception e) {
            return def;
        }
    }

    private String strParam(HttpServletRequest request, String name, String def) {
        String v = request.getParameter(name);
        if (v == null) return def;
        v = v.trim();
        return v.isEmpty() ? def : v;
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String jsp)
            throws ServletException, IOException {
        request.getRequestDispatcher(jsp).forward(request, response);
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    private void flash(HttpServletRequest request, String msg) {
        request.getSession().setAttribute("msg", msg);
    }

    // ---------------- GET ----------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        User user = loginUser(request);
        if (user == null) {
            redirect(request, response, "/user?action=loginForm");
            return;
        }

        String action = strParam(request, "action", "menu");

        try {
            switch (action) {

                // 1) 주식 매매 메인 메뉴(계좌선택/잔고)
                case "menu": {
                    ArrayList<Account> accounts = accountDAO.getAccounts(user.getUserId());
                    request.setAttribute("accounts", accounts);

                    int accountId = intParam(request, "accountId", -1);
                    if (accountId > 0) {
                        Account acc = accountDAO.getAccount(accountId, user.getUserId());
                        request.setAttribute("selectedAccount", acc);
                    }

                    forward(request, response, "/views/trade/menu.jsp");
                    break;
                }

                // 2) 전체 종목 / 검색
                case "stocks": {
                    stockDAO.updateRandomPrices(); // 페이지 들어올 때마다 자동 변동(원하면 주석)

                    String keyword = strParam(request, "keyword", "");
                    ArrayList<Stock> stocks;

                    if (keyword.isEmpty()) stocks = stockDAO.getAllStocks();
                    else stocks = stockDAO.searchByName(keyword);

                    request.setAttribute("keyword", keyword);
                    request.setAttribute("stocks", stocks);

                    // 계좌 선택 유지용
                    int accountId = intParam(request, "accountId", -1);
                    request.setAttribute("accountId", accountId);

                    forward(request, response, "/views/trade/stocks.jsp");
                    break;
                }

                // 3) 보유 종목 조회(잔고 + 종목명/수량/평단/평가손익)
                case "holdings": {
                    int accountId = intParam(request, "accountId", -1);
                    Account acc = accountDAO.getAccount(accountId, user.getUserId());
                    if (acc == null) {
                        flash(request, "잘못된 계좌이거나 본인 계좌가 아닙니다.");
                        redirect(request, response, "/trade?action=menu");
                        return;
                    }

                    ArrayList<Holding> holdings = holdingDAO.getHoldingsByAccount(accountId);
                    ArrayList<HoldingView> views = new ArrayList<>();

                    for (Holding h : holdings) {
                        Stock st = stockDAO.getStockById(h.getStockId());
                        if (st == null) continue;

                        int eval = st.getCurrentPrice() * h.getQuantity();
                        int cost = h.getAvgPrice() * h.getQuantity();
                        int profit = eval - cost;

                        views.add(new HoldingView(
                                accountId,
                                st.getStockId(),
                                st.getStockName(),
                                h.getQuantity(),
                                h.getAvgPrice(),
                                st.getCurrentPrice(),
                                eval, cost, profit
                        ));
                    }

                    request.setAttribute("account", acc);
                    request.setAttribute("holdingViews", views);

                    forward(request, response, "/views/trade/holdings.jsp");
                    break;
                }

             // 4) 매수 폼
                case "buyForm": {
                    int accountId = intParam(request, "accountId", -1);
                    String stockName = strParam(request, "stockName", "");

                    Account acc = accountDAO.getAccount(accountId, user.getUserId());
                    Stock stock = stockDAO.getStockByName(stockName);

                    int balance = acc.getBalance();
                    int price = stock.getCurrentPrice();
                    int maxQty = balance / price;   

                    request.setAttribute("accountId", accountId);
                    request.setAttribute("stockName", stockName);
                    request.setAttribute("price", price);
                    request.setAttribute("balance", balance);
                    request.setAttribute("maxQty", maxQty);

                    forward(request, response, "/views/trade/buy.jsp");
                    break;
                }

                // 5) 매도 폼
                case "sellForm": {
                    int accountId = intParam(request, "accountId", -1);
                    String stockName = strParam(request, "stockName", "");

                    Account acc = accountDAO.getAccount(accountId, user.getUserId());
                    Stock stock = stockDAO.getStockByName(stockName);
                    Holding h = holdingDAO.getHolding(acc.getAccountId(), stock.getStockId());

                    int maxQty = (h == null) ? 0 : h.getQuantity();

                    request.setAttribute("accountId", accountId);
                    request.setAttribute("stockName", stockName);
                    request.setAttribute("price", stock.getCurrentPrice());
                    request.setAttribute("maxQty", maxQty);

                    forward(request, response, "/views/trade/sell.jsp");
                    break;
                }


                default:
                    redirect(request, response, "/trade?action=menu");
            }

        } catch (Exception e) {
            e.printStackTrace();
            flash(request, "서버 오류가 발생했습니다 (trade).");
            redirect(request, response, "/trade?action=menu");
        }
    }

    // ---------------- POST ----------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        User user = loginUser(request);
        if (user == null) {
            redirect(request, response, "/user?action=loginForm");
            return;
        }

        String action = strParam(request, "action", "");

        try {
            if ("buy".equals(action)) doBuy(request, response, user);
            else if ("sell".equals(action)) doSell(request, response, user);
            else redirect(request, response, "/trade?action=menu");

        } catch (Exception e) {
            e.printStackTrace();
            flash(request, "서버 오류가 발생했습니다 (매매 처리).");
            redirect(request, response, "/trade?action=menu");
        }
    }

    private void doBuy(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        int accountId = intParam(request, "accountId", -1);
        int qty = intParam(request, "qty", -1);
        String stockName = strParam(request, "stockName", "");

        if (accountId <= 0) { flash(request, "계좌를 먼저 선택하세요."); redirect(request, response, "/trade?action=menu"); return; }
        if (stockName.isEmpty()) { flash(request, "종목명을 입력하세요."); redirect(request, response, "/trade?action=buyForm&accountId=" + accountId); return; }
        if (qty <= 0) { flash(request, "수량은 숫자 1 이상만 가능합니다."); redirect(request, response, "/trade?action=buyForm&accountId=" + accountId + "&stockName=" + encode(stockName)); return; }

        Account acc = accountDAO.getAccount(accountId, user.getUserId());
        if (acc == null) { flash(request, "본인 계좌가 아닙니다."); redirect(request, response, "/trade?action=menu"); return; }

        Stock stock = stockDAO.getStockByName(stockName);
        if (stock == null) { flash(request, "존재하지 않는 종목입니다."); redirect(request, response, "/trade?action=buyForm&accountId=" + accountId); return; }

        int result = stockService.buyStock(accountId, stock.getStockId(), qty, user.getUserId());

        if (result == 1) flash(request, "매수 완료!");
        else if (result == -3) flash(request, "잔액이 부족합니다.");
        else if (result == -2) flash(request, "계좌 오류입니다.");
        else if (result == -1) flash(request, "종목이 존재하지 않습니다.");
        else flash(request, "매수 실패!");

        redirect(request, response, "/trade?action=holdings&accountId=" + accountId);
    }

    private void doSell(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        int accountId = intParam(request, "accountId", -1);
        int qty = intParam(request, "qty", -1);
        String stockName = strParam(request, "stockName", "");

        if (accountId <= 0) { flash(request, "계좌를 먼저 선택하세요."); redirect(request, response, "/trade?action=menu"); return; }
        if (stockName.isEmpty()) { flash(request, "종목명을 입력하세요."); redirect(request, response, "/trade?action=sellForm&accountId=" + accountId); return; }
        if (qty <= 0) { flash(request, "수량은 숫자 1 이상만 가능합니다."); redirect(request, response, "/trade?action=sellForm&accountId=" + accountId + "&stockName=" + encode(stockName)); return; }

        Account acc = accountDAO.getAccount(accountId, user.getUserId());
        if (acc == null) { flash(request, "본인 계좌가 아닙니다."); redirect(request, response, "/trade?action=menu"); return; }

        Stock stock = stockDAO.getStockByName(stockName);
        if (stock == null) { flash(request, "존재하지 않는 종목입니다."); redirect(request, response, "/trade?action=sellForm&accountId=" + accountId); return; }

        int result = stockService.sellStock(accountId, stock.getStockId(), qty, user.getUserId());

        if (result == 1) flash(request, "매도 완료!");
        else if (result == -4) flash(request, "보유 수량이 부족합니다.");
        else if (result == -3) flash(request, "보유하지 않은 종목입니다.");
        else if (result == -2) flash(request, "계좌 오류입니다.");
        else if (result == -1) flash(request, "종목이 존재하지 않습니다.");
        else flash(request, "매도 실패!");

        redirect(request, response, "/trade?action=holdings&accountId=" + accountId);
    }

    private String encode(String s) {
        return s.replace(" ", "%20");
    }
}
