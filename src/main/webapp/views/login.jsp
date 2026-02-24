<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body {
    background:#f1f3f5;
    height:100vh;
    font-family:"Pretendard","Apple SD Gothic Neo",sans-serif;
}

.login-wrap {
    width:380px;
    border:1px solid #dee2e6;
    border-radius:6px;
    box-shadow:none;
}

h3 {
    font-weight:600;
    color:#212529;
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

<div class="container d-flex justify-content-center align-items-center h-100">

    <div class="login-wrap bg-white p-4">

        <h3 class="text-center mb-4">Stock Trading</h3>

        <% String error = (String)request.getAttribute("error"); %>
        <% if(error != null){ %>
            <div class="alert alert-danger text-center">
                <%= error %>
            </div>
        <% } %>

        <form action="<%= request.getContextPath() %>/user" method="post">
            <input type="hidden" name="action" value="login">

            <div class="mb-3">
                <label class="form-label">아이디</label>
                <input type="text" name="username" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">비밀번호</label>
                <input type="password" name="password" class="form-control" required>
            </div>

            <button type="submit" class="btn btn-primary w-100">
                로그인
            </button>
        </form>

        <div class="text-center mt-3">
            <a href="<%= request.getContextPath() %>/user?action=signupForm"
               class="link">
                회원가입
            </a>
        </div>

    </div>

</div>

</body>
</html>
