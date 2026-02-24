<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, stock.dto.Account" %>
<%
  stock.dto.User user = (stock.dto.User) session.getAttribute("loginUser");
  if (user == null) {
      response.sendRedirect(request.getContextPath()+"/user?action=loginForm");
      return;
  }

  List<Account> accounts = (List<Account>) request.getAttribute("accounts");
  if (accounts == null) accounts = new ArrayList<>();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>계좌 이체</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body {
    background:#f1f3f5;
    font-family:"Pretendard","Apple SD Gothic Neo",sans-serif;
}

.container {
    max-width:700px;
}

h3 {
    font-weight:600;
    color:#212529;
    margin-bottom:24px;
}

.card {
    border:1px solid #dee2e6;
    border-radius:6px;
    box-shadow:none;
}

.form-label {
    font-size:0.85rem;
    color:#495057;
}

.form-control,
.form-select {
    font-size:0.9rem;
}

.btn-primary {
    background:#343a40;
    border:none;
    padding:10px;
}
.btn-primary:hover {
    background:#212529;
}

.btn-secondary {
    background:none;
    border:none;
    color:#495057;
    padding:4px 8px;
}
.btn-secondary:hover {
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

    <h3>계좌 이체</h3>

    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger">
            <%= request.getAttribute("error") %>
        </div>
    <% } %>

    <form action="<%=request.getContextPath()%>/account" method="post" class="card p-4">
        <input type="hidden" name="action" value="transfer">

        <div class="mb-3">
            <label class="form-label">출금 계좌</label>
            <select name="fromAcc" class="form-select">
                <% for (Account a : accounts) { %>
                    <option value="<%=a.getAccountId()%>">
                        <%=a.getAccountId()%> (잔고: <%=a.getBalance()%>)
                    </option>
                <% } %>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">입금 계좌번호</label>
            <input type="text"
                   name="toAcc"
                   class="form-control"
                   placeholder="상대 계좌번호">
        </div>

        <div class="mb-3">
            <label class="form-label">이체 금액</label>
            <input type="text"
                   name="amount"
                   class="form-control"
                   placeholder="숫자만 입력">
        </div>

        <button type="submit" class="btn btn-primary w-100">
            이체
        </button>
    </form>

    <a href="<%=request.getContextPath()%>/account?action=menu"
       class="btn btn-secondary btn-sm mt-3">
        ← 뒤로
    </a>

</div>

</body>
</html>
