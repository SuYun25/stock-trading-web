<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%
    List<String[]> history = (List<String[]>) request.getAttribute("history");
    if (history == null) history = new ArrayList<>();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Stock Trading Assistant</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body {
    background:#eef1f4;
    font-family: "Pretendard", "Apple SD Gothic Neo", sans-serif;
}

.app-wrapper {
    max-width: 1000px;
    margin: 40px auto;
    background: #ffffff;
    border-radius: 10px;
    box-shadow: 0 10px 30px rgba(0,0,0,.08);
}

/* 상단 바 */
.header {
    padding: 16px 24px;
    border-bottom: 1px solid #e5e7eb;
    display:flex;
    justify-content:space-between;
    align-items:center;
}
.header h5 {
    margin:0;
    font-weight:600;
}
.header a {
    text-decoration:none;
    color:#374151;
    font-size:0.9rem;
}

/* 로그 영역 */
.log-area {
    height: 460px;
    padding: 20px 24px;
    overflow-y: auto;
    background:#fafafa;
    font-size:0.9rem;
}

.log-line {
    margin-bottom: 14px;
}
.log-user {
    color:#111827;
    font-weight:600;
}
.log-bot {
    color:#374151;
    padding-left:12px;
    border-left:3px solid #d1d5db;
    white-space:pre-line;
}

/* 입력 */
.input-area {
    border-top:1px solid #e5e7eb;
    padding:16px 24px;
    background:#fff;
}
.input-area input {
    border-radius:6px;
}

/* 빠른 명령 */
.quick {
    padding: 12px 24px 18px;
    background:#fff;
}
.quick button {
    font-size:0.8rem;
}
</style>
</head>

<body>

<div class="app-wrapper">

    <!-- HEADER -->
    <div class="header">
        <h5>Stock Trading Assistant</h5>
        <a href="<%=request.getContextPath()%>/user?action=home">Home</a>
    </div>

    <!-- LOG -->
    <div class="log-area" id="logArea">
        <% for(String[] m : history){ %>
            <div class="log-line">
                <% if("user".equals(m[0])){ %>
                    <div class="log-user">▶ <%= m[1] %></div>
                <% } else { %>
                    <div class="log-bot"><%= m[1] %></div>
                <% } %>
            </div>
        <% } %>
    </div>

    <!-- INPUT -->
    <div class="input-area">
        <form method="post" action="<%=request.getContextPath()%>/chat" class="d-flex gap-2">
            <input type="text" name="q" class="form-control"
                   placeholder="예: 도움말 / 잔고 / 계좌 개설  / 보유 종목 / 형식 "
                   autofocus required>
            <button class="btn btn-dark px-4">Enter</button>
        </form>
    </div>

    <!-- QUICK COMMAND -->
    <div class="quick d-flex gap-2 flex-wrap">
        <form method="post" action="<%=request.getContextPath()%>/chat">
            <input type="hidden" name="q" value="도움말">
            <button class="btn btn-outline-secondary btn-sm">도움말</button>
        </form>
        <form method="post" action="<%=request.getContextPath()%>/chat">
            <input type="hidden" name="q" value="잔고">
            <button class="btn btn-outline-secondary btn-sm">잔고</button>
        </form>
        <form method="post" action="<%=request.getContextPath()%>/chat">
            <input type="hidden" name="q" value="보유 종목">
            <button class="btn btn-outline-secondary btn-sm">보유 종목</button>
        </form>
        <form method="post" action="<%=request.getContextPath()%>/chat">
            <input type="hidden" name="q" value="거래내역">
            <button class="btn btn-outline-secondary btn-sm">거래내역</button>
        </form>
    </div>

</div>

<script>
  const area = document.getElementById("logArea");
  area.scrollTop = area.scrollHeight;
</script>

</body>
</html>
