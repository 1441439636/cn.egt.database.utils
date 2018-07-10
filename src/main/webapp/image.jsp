<%@ page import="java.util.Random" %><%--
  Created by IntelliJ IDEA.
  User: WYX
  Date: 2017/9/19 0019
  Time: 18:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    Random random = new Random();
    int x = random.nextInt(100);
    request.setAttribute("x", x);
%>
<div id="image">
    <div style="margin-left: 95%">
        <label onclick="q()">关闭</label>
    </div>
    <div>
        <img src="${pageContext.request.contextPath}/view?method=GetView&x=${requestScope.x}">
    </div>
</div>

<script type="text/javascript">
    function q() {

        document.getElementById("image").style.display = "none";

    }
</script>
</body>
</html>
