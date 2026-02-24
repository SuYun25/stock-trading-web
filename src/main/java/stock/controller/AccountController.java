package stock.controller;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import stock.dao.AccountDAO;
import stock.dto.User;

@WebServlet("/account")
public class AccountController extends HttpServlet {

    private final AccountDAO accountDAO = new AccountDAO();

    private User getLoginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("loginUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
            return null;
        }
        return user;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        User user = getLoginUser(request, response);
        if (user == null) return;

        String action = request.getParameter("action");
        if (action == null) action = "menu";

        try {
            switch (action) {

                case "menu":
                    request.getRequestDispatcher("/views/account/menu.jsp")
                           .forward(request, response);
                    break;

                case "list":
                    request.setAttribute("accounts",
                            accountDAO.getAccounts(user.getUserId()));
                    request.getRequestDispatcher("/views/account/list.jsp")
                           .forward(request, response);
                    break;

                case "createForm":
                    request.getRequestDispatcher("/views/account/create.jsp")
                           .forward(request, response);
                    break;

                case "depositForm":
                    request.setAttribute("accounts",
                            accountDAO.getAccounts(user.getUserId()));
                    request.getRequestDispatcher("/views/account/deposit.jsp")
                           .forward(request, response);
                    break;

                case "transferForm":
                    request.setAttribute("accounts",
                            accountDAO.getAccounts(user.getUserId()));
                    request.getRequestDispatcher("/views/account/transfer.jsp")
                           .forward(request, response);
                    break;

                case "delete": {
                    int accountId = Integer.parseInt(request.getParameter("accountId"));

                    try {
                        accountDAO.deleteAccount(accountId, user.getUserId());
                        response.sendRedirect(request.getContextPath()
                                + "/account?action=list");
                    }
                    catch (SQLIntegrityConstraintViolationException e) {
                        // ✅ 보유 주식이 있어서 삭제 불가
                        request.setAttribute("error",
                                "보유 중인 주식이 있는 계좌는 삭제할 수 없습니다. "
                              + "모든 종목을 매도한 후 다시 시도해주세요.");
                        request.setAttribute("accounts",
                                accountDAO.getAccounts(user.getUserId()));
                        request.getRequestDispatcher("/views/account/list.jsp")
                               .forward(request, response);
                    }
                    break;
                }

                default:
                    response.sendRedirect(request.getContextPath()
                            + "/account?action=menu");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "계좌 처리 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/views/account/menu.jsp")
                   .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        User user = getLoginUser(request, response);
        if (user == null) return;

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                int accountId = Integer.parseInt(request.getParameter("accountId"));

                if (accountDAO.existsAccountId(accountId)) {
                    request.setAttribute("error", "이미 존재하는 계좌번호입니다.");
                    request.getRequestDispatcher("/views/account/create.jsp")
                           .forward(request, response);
                    return;
                }

                int result = accountDAO.createAccount(user.getUserId(), accountId);
                if (result > 0) {
                    response.sendRedirect(request.getContextPath()
                            + "/account?action=list");
                } else {
                    request.setAttribute("error", "계좌 개설 실패");
                    request.getRequestDispatcher("/views/account/create.jsp")
                           .forward(request, response);
                }
            }

            else if ("deposit".equals(action)) {
                int accountId = Integer.parseInt(request.getParameter("accountId"));
                int amount = Integer.parseInt(request.getParameter("amount"));

                int result = accountDAO.deposit(accountId, amount, user.getUserId());
                if (result > 0) {
                    response.sendRedirect(request.getContextPath()
                            + "/account?action=list");
                } else {
                    request.setAttribute("error", "충전 실패(계좌 확인)");
                    request.setAttribute("accounts",
                            accountDAO.getAccounts(user.getUserId()));
                    request.getRequestDispatcher("/views/account/deposit.jsp")         
                           .forward(request, response);
                }
            }

            else if ("transfer".equals(action)) {
                int fromAcc = Integer.parseInt(request.getParameter("fromAcc"));
                int toAcc = Integer.parseInt(request.getParameter("toAcc"));
                int amount = Integer.parseInt(request.getParameter("amount"));

                int result = accountDAO.transfer(fromAcc, toAcc, amount, user.getUserId());

                if (result == 1) {
                    response.sendRedirect(request.getContextPath()
                            + "/account?action=list");
                } else {
                    String msg = switch (result) {
                        case -1 -> "출금 계좌가 본인 계좌가 아니거나 존재하지 않습니다.";
                        case -2 -> "잔액이 부족합니다.";
                        case -3 -> "입금할 계좌가 존재하지 않습니다.";
                        default -> "이체 실패";
                    };
                    request.setAttribute("error", msg);
                    request.setAttribute("accounts",
                            accountDAO.getAccounts(user.getUserId()));
                    request.getRequestDispatcher("/views/account/transfer.jsp")
                           .forward(request, response);
                }
            }

            else {
                response.sendRedirect(request.getContextPath()
                        + "/account?action=menu");
            }

        } catch (NumberFormatException nfe) {
            request.setAttribute("error", "숫자 입력이 올바르지 않습니다.");
            request.getRequestDispatcher("/views/account/menu.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "서버 오류가 발생했습니다.");
            request.getRequestDispatcher("/views/account/menu.jsp")
                   .forward(request, response);
        }
    }
}
