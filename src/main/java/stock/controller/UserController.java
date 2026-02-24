package stock.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import stock.dao.UserDAO;
import stock.dto.User;

@WebServlet("/user")
public class UserController extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (action == null) action = "loginForm";

        switch (action) {

            case "loginForm":
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                break;

            case "signupForm":
                request.getRequestDispatcher("/views/signup.jsp").forward(request, response);
                break;

            case "home":
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("loginUser") == null) {
                    response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
                    return;
                }
                request.getRequestDispatcher("/views/home.jsp").forward(request, response);
                break;

            case "logout":
                HttpSession s = request.getSession(false);
                if (s != null) s.invalidate();
                response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("login".equals(action)) {
                login(request, response);
            } else if ("signup".equals(action)) {
                signup(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "서버 오류가 발생했습니다.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDAO.login(username, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", user);
            response.sendRedirect(request.getContextPath() + "/user?action=home");
        } else {
            request.setAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    private void signup(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (userDAO.exists(username)) {
            request.setAttribute("error", "이미 존재하는 아이디입니다.");
            request.getRequestDispatcher("/views/signup.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        userDAO.signup(user);

        // 회원가입 성공 → 로그인 화면
        response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
    }
}
