<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, stock.dto.*" %>
<%
    User user = (User) session.getAttribute("loginUser");
    if (user == null) {
        response.sendRedirect(request.getContextPath()+"/user?action=loginForm");
        return;
    }

    List<Account> accounts = (List<Account>) request.getAttribute("accounts");
    Account selected = (Account) request.getAttribute("selectedAccount");
    String msg = (String) session.getAttribute("msg");
    if (msg != null) session.removeAttribute("msg");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>주식 매매</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body {
    background:#f1f3f5;
    font-family:"Pretendard","Apple SD Gothic Neo",sans-serif;
}

.container {
    max-width:900px;
}

.section-title {
    font-weight:600;
    color:#212529;
}

.section-sub {
    font-size:0.85rem;
    color:#6c757d;
}

.box {
    background:#ffffff;
    border:1px solid #dee2e6;
    border-radius:6px;
}

.form-label {
    font-size:0.8rem;
    color:#6c757d;
}

.form-select {
    font-size:0.9rem;
}

.btn-primary {
    background:#343a40;
    border:none;
}
.btn-primary:hover {
    background:#212529;
}

.btn-outline-primary,
.btn-outline-secondary {
    border:none;
    color:#495057;
}

.btn-outline-primary:hover,
.btn-outline-secondary:hover {
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

    <!-- 타이틀 -->
    <div class="mb-4">
        <h3 class="section-title mb-1">주식 매매</h3>
        <p class="section-sub mb-0">
            계좌를 선택한 후 거래를 진행하세요
        </p>
    </div>

    <!-- 메시지 -->
    <% if (msg != null) { %>
        <div class="alert alert-danger mb-4">
            <%= msg %>
        </div>
    <% } %>

    <!-- 계좌 선택 -->
    <div class="box p-4 mb-4">
        <form method="get"
              action="<%=request.getContextPath()%>/trade"
              class="row g-3 align-items-end">

            <input type="hidden" name="action" value="menu">

            <div class="col-md-4">
                <label class="form-label">계좌 선택</label>
                <select name="accountId" class="form-select">
                    <option value="">-- 선택 --</option>
                    <% for(Account a : accounts){ %>
                        <option value="<%=a.getAccountId()%>"
                            <%= (selected!=null && a.getAccountId()==selected.getAccountId())?"selected":"" %>>
                            <%=a.getAccountId()%>
                        </option>
                    <% } %>
                </select>
            </div>

            <div class="col-md-2">
                <button type="submit"
                        class="btn btn-primary w-100">
                    조회
                </button>
            </div>
        </form>
    </div>

    <% if (selected != null) { %>

        <!-- 잔고 -->
        <div class="box p-3 mb-4">
            <div class="text-muted small mb-1">
                현재 잔고
            </div>
            <div class="fw-semibold">
                <%= selected.getBalance() %> 원
            </div>
        </div>

        <!-- 메뉴 -->
        <div class="d-flex gap-2 mb-4">
            <a href="<%=request.getContextPath()%>/trade?action=stocks&accountId=<%=selected.getAccountId()%>"
               class="btn btn-outline-primary btn-sm">
                전체 종목 조회
            </a>

            <a href="<%=request.getContextPath()%>/trade?action=holdings&accountId=<%=selected.getAccountId()%>"
               class="btn btn-outline-secondary btn-sm">
                보유 종목 조회
            </a>
        </div>

    <% } %>

    <hr>

    <!-- 하단 -->
    <a href="<%=request.getContextPath()%>/user?action=home"
       class="btn btn-outline-secondary btn-sm">
        홈으로
    </a>

</div>

</body>
</html>
