<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, stock.dto.*" %>
<%
    List<Stock> stocks = (List<Stock>) request.getAttribute("stocks");
    int accountId = (int) request.getAttribute("accountId");
    String keyword = (String) request.getAttribute("keyword");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>전체 종목</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body{
    background:#f1f3f5;
    font-family:"Pretendard","Apple SD Gothic Neo",sans-serif;
}
.container{ max-width:900px; }
h3{ font-weight:600; color:#212529; }
.table{
    background:#fff;
    border:1px solid #dee2e6;
}
.table thead{ background:#f8f9fa; }

/* ✅ 매수 = 빨강 */
.btn-buy{
    color:#c92a2a;
    border:1px solid #c92a2a;
    background:#fff;
    font-size:0.85rem;
}
.btn-buy:hover{
    background:#c92a2a;
    color:#fff;
}

/* ✅ 매도 = 파랑 */
.btn-sell{
    color:#1864ab;
    border:1px solid #1864ab;
    background:#fff;
    font-size:0.85rem;
}
.btn-sell:hover{
    background:#1864ab;
    color:#fff;
}

.btn-secondary{
    background:none;
    border:none;
    color:#495057;
}
.btn-secondary:hover{
    background:#e9ecef;
    color:#212529;
}
</style>
</head>

<body>

<div class="container py-5">

<h3 class="mb-4">전체 종목</h3>

<form method="get" action="<%=request.getContextPath()%>/trade" class="mb-3">
    <input type="hidden" name="action" value="stocks">
    <input type="hidden" name="accountId" value="<%=accountId%>">

    <div class="input-group w-50">
        <input type="text" name="keyword"
               class="form-control"
               value="<%=keyword==null?"":keyword%>"
               placeholder="종목명 검색">
        <button type="submit" class="btn btn-outline-secondary">
            검색
        </button>
    </div>
</form>

<table class="table table-hover align-middle">
<thead>
<tr>
    <th>종목명</th>
    <th>현재가</th>
    <th>매수</th>
    <th>매도</th>
</tr>
</thead>
<tbody>

<% for(Stock s:stocks){ %>
<tr>
    <td><%=s.getStockName()%></td>
    <td><%=s.getCurrentPrice()%></td>
    <td>
        <a href="<%=request.getContextPath()%>/trade?action=buyForm&accountId=<%=accountId%>&stockName=<%=s.getStockName()%>"
           class="btn btn-sm btn-buy">
            매수
        </a>
    </td>
    <td>
        <a href="<%=request.getContextPath()%>/trade?action=sellForm&accountId=<%=accountId%>&stockName=<%=s.getStockName()%>"
           class="btn btn-sm btn-sell">
            매도
        </a>
    </td>
</tr>
<% } %>

</tbody>
</table>

<a href="<%=request.getContextPath()%>/trade?action=menu&accountId=<%=accountId%>"
   class="btn btn-secondary btn-sm">
    ← 뒤로가기
</a>

</div>
</body>
</html>
