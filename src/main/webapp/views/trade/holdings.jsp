<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, stock.dto.*" %>
<%
    Account acc = (Account) request.getAttribute("account");
    List<HoldingView> list = (List<HoldingView>) request.getAttribute("holdingViews");

    int totalProfit = 0;
    if (list != null) {
        for (HoldingView h : list) totalProfit += h.getProfit();
    }

    String totalCls, totalSign="";
    if (totalProfit > 0) {
        totalCls="profit-plus"; totalSign="+";
    } else if (totalProfit < 0) {
        totalCls="profit-minus"; totalSign="-";
        totalProfit=Math.abs(totalProfit);
    } else totalCls="profit-zero";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>보유 종목 조회</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body{
    background:#f1f3f5;
    font-family:"Pretendard","Apple SD Gothic Neo",sans-serif;
}
.container{ max-width:900px; }
h3{ font-weight:600; color:#212529; }
.card{
    border:1px solid #dee2e6;
    border-radius:6px;
}
.table{
    background:#fff;
    border:1px solid #dee2e6;
}
.table thead{ background:#f8f9fa; }

/* Bootstrap 색 덮어쓰기 방지 (중요) */
.profit-plus{
    color:#c92a2a !important;   /* + 빨강 */
    font-weight:600;
}
.profit-minus{
    color:#1864ab !important;   /* - 파랑 */
    font-weight:600;
}
.profit-zero{
    color:#6c757d !important;
    font-weight:600;
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

<h3 class="mb-4">보유 종목 조회</h3>

<div class="card p-3 mb-3">
    <div class="row text-center">
        <div class="col-md-6">
            <div class="text-muted small">계좌번호</div>
            <div class="fw-semibold"><%=acc.getAccountId()%></div>
        </div>
        <div class="col-md-6">
            <div class="text-muted small">잔고</div>
            <div class="fw-semibold"><%=acc.getBalance()%> 원</div>
        </div>
    </div>
</div>

<div class="card p-4 mb-4 text-center">
    <div class="text-muted small mb-1">총 평가손익</div>
    <div class="fs-4 <%=totalCls%>">
        <%=totalSign%><%=totalProfit%> 원
    </div>
</div>

<table class="table table-hover align-middle">
<thead>
<tr>
    <th>종목명</th>
    <th class="text-end">보유 수량</th>
    <th class="text-end">평단가</th>
    <th class="text-end">현재가</th>
    <th class="text-end">평가손익</th>
</tr>
</thead>
<tbody>

<% if(list==null || list.isEmpty()){ %>
<tr>
    <td colspan="5" class="text-center text-muted py-4">
        보유 종목이 없습니다.
    </td>
</tr>
<% } %>

<% for(HoldingView h:list){
    int p=h.getProfit(); String c, s="";
    if(p>0){ c="profit-plus"; s="+"; }
    else if(p<0){ c="profit-minus"; s="-"; p=Math.abs(p); }
    else c="profit-zero";
%>
<tr>
    <td><%=h.getStockName()%></td>
    <td class="text-end"><%=h.getQuantity()%></td>
    <td class="text-end"><%=h.getAvgPrice()%></td>
    <td class="text-end"><%=h.getCurrentPrice()%></td>
    <td class="text-end <%=c%>"><%=s%><%=p%></td>
</tr>
<% } %>

</tbody>
</table>

<a href="<%=request.getContextPath()%>/trade?action=menu&accountId=<%=acc.getAccountId()%>"
   class="btn btn-secondary btn-sm mt-3">
    ← 뒤로가기
</a>

</div>
</body>
</html>
