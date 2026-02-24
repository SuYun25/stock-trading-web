<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body {
    background:#f1f3f5;
    font-family:"Pretendard","Apple SD Gothic Neo",sans-serif;
}

.container {
    max-width:500px;
}

h3 {
    font-weight:600;
    color:#212529;
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

.form-control {
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

.alert-danger {
    background:#fff5f5;
    color:#c92a2a;
    border:1px solid #ffe3e3;
}

.link {
    font-size:0.85rem;
    color:#495057;
    text-decoration:none;
}
.link:hover {
    text-decoration:underline;
}
</style>
</head>

<body>

<div class="container py-5">

    <h3 class="mb-4 text-center">회원가입</h3>

    <% String error = (String)request.getAttribute("error"); %>
    <% if(error != null){ %>
        <div class="alert alert-danger">
            <%= error %>
        </div>
    <% } %>

    <form action="<%= request.getContextPath() %>/user"
          method="post"
          class="card p-4">
        <input type="hidden" name="action" value="signup">

        <div class="mb-3">
            <label class="form-label">아이디</label>
            <input type="text" name="username" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">비밀번호</label>
            <input type="password" name="password" class="form-control" required>
        </div>

        <button type="submit" class="btn btn-primary w-100">
            회원가입
        </button>
    </form>

    <div class="text-center mt-3">
        <a href="<%= request.getContextPath() %>/user?action=loginForm"
           class="link">
            로그인 화면으로
        </a>
    </div>

</div>

</body>
</html>
