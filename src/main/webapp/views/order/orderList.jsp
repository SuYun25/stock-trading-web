<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="stock.dto.OrderView" %>   
<%@ page import="stock.dto.Account" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>거래 내역</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body{
    background:#f1f3f5;
    font-family:"Pretendard","Apple SD Gothic Neo",sans-serif;
}

.container{
    max-width:1000px;
}

h3{
    font-weight:600;
    color:#212529;
}

.table{
    background:#fff;
    border:1px solid #dee2e6;
}

.table thead{
    background:#f8f9fa;
}

th, td{
    vertical-align:middle;
}

/* ✅ 매수 / 매도 색상 */
.BUY{
    color:#c92a2a !important;   /* 매수 = 빨강 */
    font-weight:600;
}
.SELL{
    color:#1864ab !important;   /* 매도 = 파랑 */
    font-weight:600;
}

/* 버튼 통일 */
.btn-primary{
    background:#343a40;
    border:none;
}
.btn-primary:hover{
    background:#212529;
}

.btn-outline-primary{
    border:none;
    color:#495057;
}
.btn-outline-primary:hover{
    background:#e9ecef;
    color:#212529;
}
</style>
</head>

<body>

<div class="container py-5">

    <h3 class="mb-4">거래 내역</h3>

    <div class="mb-3">
        <!-- 홈 이동은 반드시 컨트롤러 경유 -->
        <a href="<%=request.getContextPath()%>/user?action=home"
           class="btn btn-sm btn-outline-primary">
            홈
        </a>
    </div>

    <form method="post"
          action="<%= request.getContextPath() %>/orderList"
          class="mb-4 d-flex align-items-center gap-2">

        <span class="text-muted small">계좌 선택</span>

        <select name="accountId" class="form-select w-auto">
        <%
            ArrayList<Account> accountList =
                (ArrayList<Account>) request.getAttribute("accountList");
            Integer selectedId = (Integer) request.getAttribute("accountId");

            for (Account acc : accountList) {
        %>
            <option value="<%= acc.getAccountId() %>"
                <%= acc.getAccountId() == selectedId ? "selected" : "" %>>
                <%= acc.getAccountId() %>
            </option>
        <%
            }
        %>
        </select>

        <button type="submit" class="btn btn-primary btn-sm">
            조회
        </button>
    </form>

    <table class="table table-hover align-middle">
        <thead>
            <tr>
                <th>번호</th>
                <th>종목명</th>
                <th>구분</th>
                <th class="text-end">수량</th>
                <th class="text-end">체결가</th>
                <th>거래일</th>
            </tr>
        </thead>
        <tbody>
        <%
            ArrayList<OrderView> list =
                (ArrayList<OrderView>) request.getAttribute("orderList");

            int no = (list != null) ? list.size() : 0;

            if (list != null && !list.isEmpty()) {
                for (OrderView o : list) {
        %>
            <tr>
                <td><%= no-- %></td>
                <td><%= o.getStockName() %></td>
                <!-- BUY / SELL 클래스 그대로 활용 -->
                <td class="<%= o.getOrderType() %>">
                    <%= o.getOrderType() %>
                </td>
                <td class="text-end"><%= o.getQuantity() %></td>
                <td class="text-end"><%= o.getPriceAtTrade() %></td>
                <td><%= o.getTradeDate() %></td>
            </tr>
        <%
                }
            } else {
        %>
            <tr>
                <td colspan="6" class="text-center text-muted py-4">
                    거래 내역이 없습니다.
                </td>
            </tr>
        <%
            }
        %>
        </tbody>
    </table>

</div>

</body>
</html>
