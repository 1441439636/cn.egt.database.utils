
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>ECharts实例</title>
</head>
<body>
<!--Step:1 Prepare a dom for ECharts which (must) has size (width & hight)-->
<!--Step:1 为ECharts准备一个具备大小（宽高）的Dom-->
<div id="mainBar" style="height:500px;border:1px solid #ccc;padding:10px;"></div>

<!--Step:2 Import echarts.js-->
<!--Step:2 引入echarts.js-->
<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>

</body>
</html>