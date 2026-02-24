<%@ page contentType="text/html; charset=UTF-8" %>
<%
    int accountId = (int) request.getAttribute("accountId");
    String stockName = (String) request.getAttribute("stockName");
    int price = (int) request.getAttribute("price");
    int maxQty = (int) request.getAttribute("maxQty");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>주식 매도</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<style>

body{
    background:#f1f3f5;
    font-family:"Pretendard","Apple SD Gothic Neo",sans-serif;
}
.container{ max-width:600px; }
h3{ font-weight:600; color:#212529; }
.card{
    border:1px solid #dee2e6;
    border-radius:6px;
}
.form-label{ font-size:0.85rem; color:#495057; }
.btn-danger{
    background:#495057;
    border:none;
}
.btn-danger:hover{ background:#343a40; }
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
<h3 class="mb-4">주식 매도</h3>

<form method="post" action="<%=request.getContextPath()%>/trade" class="card p-4">
    <input type="hidden" name="action" value="sell">
    <input type="hidden" name="accountId" value="<%=accountId%>">
    <input type="hidden" name="stockName" value="<%=stockName%>">

    <div class="mb-3">
        <label class="form-label">종목명</label>
        <input type="text" class="form-control" value="<%=stockName%>" readonly>
    </div>

    <div class="mb-1 text-muted small">
        현재가: <b><%=price%></b> 원  
        <br>👉 최대 <b><%=maxQty%></b> 주 매도 가능
    </div>

    <div class="mb-3 mt-2">
        <label class="form-label">수량</label>
        <input type="number" name="qty" id="qty"
               class="form-control"
               min="1" max="<%=maxQty%>">
    </div>

    <div class="mb-3 text-muted small">
        총 금액: <b id="total">0</b> 원
    </div>

    <button type="submit" class="btn btn-danger w-100" id="sellBtn">
        매도
    </button>
</form>

<a href="<%=request.getContextPath()%>/trade?action=stocks&accountId=<%=accountId%>"
   class="btn btn-secondary btn-sm mt-3">
    ← 뒤로가기
</a>
</div>

<script>
const price = <%=price%>;
const maxQty = <%=maxQty%>;
const qtyInput = document.getElementById("qty");
const totalEl = document.getElementById("total");
const btn = document.getElementById("sellBtn");

qtyInput.addEventListener("input", () => {
    const q = parseInt(qtyInput.value || 0);
    totalEl.innerText = (q * price).toLocaleString();
    btn.disabled = (q <= 0 || q > maxQty);
});
</script>

</body>
</html>
