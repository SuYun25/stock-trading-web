<%@ page contentType="text/html; charset=UTF-8" %>
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
<title>Home</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">

<style>
    body {
        background-color: #f4f6f9;
    }
    .brand-title {
        font-weight: 600;
        letter-spacing: -0.5px;
    }
    .menu-card {
        transition: all 0.15s ease-in-out;
        border: 1px solid #e5e7eb;
        border-radius: 14px;
        background: #ffffff;
    }
    .menu-card:hover {
        background-color: #f8fafc;
        transform: translateY(-2px);
    }
    .sub-btn {
        font-size: 0.85rem;
    }
</style>

</head>

<body>

<div class="container py-5" style="max-width: 900px;">

    <!-- 상단 -->
    <div class="mb-5">
        <h4 class="text-secondary mb-1">Stock Trading System</h4>
        <h2 class="brand-title">
            <%= user.getUsername() %> 님, 안녕하세요
        </h2>
    </div>

    <!-- 메인 메뉴 -->
    <div class="row g-4">

        <div class="col-md-4">
            <a href="<%=request.getContextPath()%>/account?action=menu"
               class="text-decoration-none text-dark">
                <div class="p-4 menu-card h-100">
                    <h6 class="text-muted mb-2">ACCOUNT</h6>
                    <h5 class="fw-semibold mb-1">계좌 관리</h5>
                    <p class="text-muted small mb-0">계좌 개설 · 조회 · 이체</p>
                </div>
            </a>
        </div>

        <div class="col-md-4">
            <a href="<%=request.getContextPath()%>/trade?action=menu"
               class="text-decoration-none text-dark">
                <div class="p-4 menu-card h-100">
                    <h6 class="text-muted mb-2">TRADING</h6>
                    <h5 class="fw-semibold mb-1">주식 매매</h5>
                    <p class="text-muted small mb-0">매수 · 매도 · 보유 종목</p>
                </div>
            </a>
        </div>

        <div class="col-md-4">
            <a href="<%=request.getContextPath()%>/orderList"
               class="text-decoration-none text-dark">
                <div class="p-4 menu-card h-100">
                    <h6 class="text-muted mb-2">HISTORY</h6>
                    <h5 class="fw-semibold mb-1">거래 내역</h5>
                    <p class="text-muted small mb-0">주문 · 체결 기록 조회</p>
                </div>
            </a>
            
        </div>

    </div>

    <!-- 하단 -->
    <div class="d-flex justify-content-between align-items-center mt-5">
        <span class="text-muted small">
            © 2025 Stock Trading Project
        </span>

        <!-- 🔽 오른쪽 버튼 영역 -->
        <div class="d-flex flex-column align-items-end gap-2">

            <!-- 챗봇 (서브 기능) -->
          <a href="<%=request.getContextPath()%>/chat"
   class="btn btn-outline-secondary btn-sm sub-btn">
    <i class="fa-solid fa-robot me-1"></i> 도우미 챗봇
</a>


            <!-- 로그아웃 -->
            <a href="<%=request.getContextPath()%>/user?action=logout"
               class="btn btn-outline-secondary btn-sm">
                로그아웃
            </a>

        </div>
    </div>

</div>

</body>
</html>
