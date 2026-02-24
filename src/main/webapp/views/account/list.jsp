<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, stock.dto.Account" %>
<%
    stock.dto.User user = (stock.dto.User) session.getAttribute("loginUser");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
        return;
    }

    List<Account> accounts = (List<Account>) request.getAttribute("accounts");
    if (accounts == null) accounts = new ArrayList<>();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내 계좌 목록</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body {
    background:#f1f3f5;
    font-family:"Pretendard","Apple SD Gothic Neo",sans-serif;
}

.container {
    max-width:900px;
}

h3 {
    font-weight:600;
    color:#212529;
}

.table {
    background:#fff;
    border:1px solid #dee2e6;
}

.table thead {
    background:#f8f9fa;
}

.table th {
    font-size:0.85rem;
    color:#495057;
}

.table td {
    font-size:0.9rem;
    vertical-align:middle;
}

.btn-outline-danger {
    border:none;
    color:#c92a2a;
}
.btn-outline-danger:hover {
    background:#ffe3e3;
    color:#a51111;
}

.btn-secondary {
    background:none;
    border:none;
    color:#495057;
}
.btn-secondary:hover {
    background:#e9ecef;
    color:#212529;
}

.btn-outline-primary {
    border:none;
    color:#495057;
}
.btn-outline-primary:hover {
    background:#e9ecef;
    color:#212529;
}

.alert-danger {
    background:#fff5f5;
    color:#c92a2a;
    border:1px solid #ffe3e3;
}
</style>
</head>

<body>

<div class="container py-5">

    <h3 class="mb-4">내 계좌 목록</h3>

    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger">
            <%= request.getAttribute("error") %>
        </div>
    <% } %>

    <div class="mb-3">
        <a href="<%=request.getContextPath()%>/user?action=home"
           class="btn btn-outline-primary btn-sm">
            홈
        </a>
    </div>

    <table class="table table-hover">
        <thead>
            <tr>
                <th>계좌번호</th>
                <th>잔고</th>
                <th>개설일</th>
                <th>삭제</th>
            </tr>
        </thead>
        <tbody>
        <% for (Account a : accounts) { %>
            <tr>
                <td><%= a.getAccountId() %></td>
                <td><%= a.getBalance() %></td>
                <td><%= a.getCreateDate() %></td>
                <td>
                    <a href="<%=request.getContextPath()%>/account?action=delete&accountId=<%=a.getAccountId()%>"
                       class="btn btn-sm btn-outline-danger"
                       onclick="return confirm('정말 삭제할까요?');">
                        삭제
                    </a>
                </td>
            </tr>
        <% } %>
        </tbody>
    </table>

    <a href="<%=request.getContextPath()%>/account?action=menu"
       class="btn btn-secondary btn-sm mt-3">
        ← 뒤로
    </a>

</div>

</body>
</html>
