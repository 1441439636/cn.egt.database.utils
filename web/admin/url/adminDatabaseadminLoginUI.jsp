<%--
  Created by IntelliJ IDEA.
  User: ZLS
  Date: 2017/5/10
  Time: 19:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
</head>
<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function () {
        window.location.href = "${pageContext.request.contextPath}/adminLogin?method=adminLoginUI";
    });
</script>
<body>
</body>
</html>
