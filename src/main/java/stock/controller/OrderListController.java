package stock.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import stock.dao.AccountDAO;
import stock.dao.OrderDAO;
import stock.dto.Account;
import stock.dto.OrderView;   
import stock.dto.User;

@WebServlet("/orderList")
public class OrderListController extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();
    private AccountDAO accountDAO = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
            return;
        }

        try {
            int userId = loginUser.getUserId();

            // 내 계좌 목록
            ArrayList<Account> accountList = accountDAO.getAccounts(userId);

            // 선택 계좌
            Integer accountId = (Integer) session.getAttribute("selectedAccountId");
            if (accountId == null && !accountList.isEmpty()) {
                accountId = accountList.get(0).getAccountId();
                session.setAttribute("selectedAccountId", accountId);
            }

            // Order → OrderView
            ArrayList<OrderView> orderList =
                    (accountId == null) ? new ArrayList<>() : orderDAO.getOrders(accountId);

            request.setAttribute("accountList", accountList);
            request.setAttribute("accountId", accountId);
            request.setAttribute("orderList", orderList);

            request.getRequestDispatcher("/views/order/orderList.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // 계좌 선택
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int accountId = Integer.parseInt(request.getParameter("accountId"));
        request.getSession().setAttribute("selectedAccountId", accountId);
        response.sendRedirect(request.getContextPath() + "/orderList");

    }
}
