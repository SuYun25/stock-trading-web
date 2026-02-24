<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그아웃</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body style="background:#f5f7fb;">

<div class="container py-5 text-center">

    <h3 class="mb-3">로그아웃 되었습니다</h3>

    <a href="<%=request.getContextPath()%>/user?action=loginForm"
       class="btn btn-primary">로그인 화면으로</a>
    

</div>

</body>
</html>
