<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  stock.dto.User user = (stock.dto.User) session.getAttribute("loginUser");
  if (user == null) {
      response.sendRedirect(request.getContextPath()+"/user?action=loginForm");
      return;
  }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>계좌 개설</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
/* 전체 톤 */
body {
    background:#f1f3f5 !important;
    font-family: "Pretendard","Apple SD Gothic Neo",sans-serif;
}

/* 제목 */
h4 {
    font-weight:600;
    color:#212529;
}

/* 카드 */
form.bg-white {
    border-radius:6px !important;
    border:1px solid #dee2e6 !important;
}

/* 입력 */
.form-control {
    font-size:0.9rem;
}
.form-control::placeholder {
    color:#adb5bd;
}

/* 설명 텍스트 */
.form-text {
    font-size:0.75rem;
    color:#868e96;
}

/* 핵심: 파란 버튼 제거 */
.btn-primary {
    background:#343a40 !important;
    border:none !important;
    font-size:0.9rem;
    padding:10px;
}
.btn-primary:hover {
    background:#212529 !important;
}

/* 뒤로가기 */
.btn-outline-secondary {
    border:none;
    color:#495057;
}
.btn-outline-secondary:hover {
    background:#e9ecef;
    color:#212529;
}

/* 에러 메시지 */
.alert-danger {
    background:#fff5f5;
    color:#c92a2a;
    border:1px solid #ffe3e3;
}
</style>

</head>

<body>

<div class="container py-5" style="max-width:520px;">

    <h4 class="mb-4 text-center">계좌 개설</h4>

    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger text-center">
            <%= request.getAttribute("error") %>
        </div>
    <% } %>

    <form action="<%=request.getContextPath()%>/account"
          method="post"
          class="bg-white p-4 border rounded">

        <input type="hidden" name="action" value="create">

        <div class="mb-3">
            <label class="form-label text-muted small">계좌번호</label>
            <input type="text"
                   name="accountId"
                   class="form-control"
                   placeholder="8자리 이하 숫자 (- 제외)"
                   required>
            <div class="form-text">
                숫자만 입력 가능합니다.
            </div>
        </div>

        <button type="submit" class="btn btn-primary w-100">
            계좌 개설
        </button>
    </form>

    <div class="text-center mt-3">
        <a href="<%=request.getContextPath()%>/account?action=menu"
           class="btn btn-outline-secondary btn-sm">
            ← 뒤로
        </a>
    </div>

</div>

</body>
</html>
