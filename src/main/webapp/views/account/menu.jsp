<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    stock.dto.User user = (stock.dto.User) session.getAttribute("loginUser");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>계좌 관리</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body {
    background:#f1f3f5;
    font-family:"Pretendard","Apple SD Gothic Neo",sans-serif;
}

.container {
    max-width:600px;
}

.card {
    border:1px solid #dee2e6;
    border-radius:6px;
    box-shadow:none;
}

h3 {
    font-weight:600;
    color:#212529;
}

.list-group-item {
    padding:14px 18px;
    font-size:0.95rem;
    border-color:#dee2e6;
    color:#212529;
}

.list-group-item:hover {
    background:#f8f9fa;
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

    <div class="card p-4">

        <h3 class="mb-4 text-center">계좌 관리</h3>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger text-center">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <div class="list-group mb-4">

            <a href="<%=request.getContextPath()%>/account?action=list"
               class="list-group-item list-group-item-action">
               내 계좌 조회
            </a>

            <a href="<%=request.getContextPath()%>/account?action=createForm"
               class="list-group-item list-group-item-action">
               계좌 개설
            </a>

            <a href="<%=request.getContextPath()%>/account?action=depositForm"
               class="list-group-item list-group-item-action">
               잔액 충전
            </a>

            <a href="<%=request.getContextPath()%>/account?action=transferForm"
               class="list-group-item list-group-item-action">
               계좌 이체
            </a>

        </div>

        <div class="text-center">
            <a href="<%=request.getContextPath()%>/user?action=home"
               class="btn btn-outline-primary btn-sm px-4">
                홈으로
            </a>
        </div>

    </div>

</div>

</body>
</html>
