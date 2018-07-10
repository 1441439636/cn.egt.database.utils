<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Language" content="zh-cn">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <LINK href="${pageContext.request.contextPath}/css/Style1.css" type="text/css" rel="stylesheet">
    <style type="text/css">
        a {
            list-style: none;
        }
    </style>
</head>
<body <c:if test="${msg=='addDBSuccess'}"> onload="setTimeout('delay()',3000)" </c:if>>
<c:if test="${msg=='addDBSuccess'}">
    <div>
        <h3 align="center">配置添加成功</h3>
        <h4 align="center">3秒后返回......</h4>
    </div>
</c:if>
<c:if test="${msg=='addDBFail'}">
    <div>
        <h3 align="center">配置添加失败</h3>
        <h4 align="center">3秒后返回......</h4>
    </div>
</c:if>
<c:if test="${msg=='adminHomeUI'}">
    <h3 align="center"> 帐号或密码错误，点击返回管理员登录界面</h3>
    <a href="${pageContext.request.contextPath}/admin/url/adminDatabaseadminLoginUI.jsp">点击登录</a>
</c:if>
<c:if test="${msg=='addRole0'}">
    <h3 align="center"> 角色名不能为空，请仔细填写查看！</h3>
    <a href="${pageContext.request.contextPath}/adminRole?method=roleUI">点击返回</a>
</c:if><c:if test="${msg=='addRole1'}">
    <h3 align="center"> 角色名已存在，请仔细查看！</h3>
    <a href="${pageContext.request.contextPath}/adminRole?method=roleUI">点击返回</a>
</c:if>
<c:if test="${msg=='addRole2'}">
    <h3 align="center"> 角色名添加失败，请重新添加！</h3>
    <a href="${pageContext.request.contextPath}/adminRole?method=roleUI">点击返回</a>
</c:if>

<c:if test="${msg=='updateRole0'}">
    <h3 align="center"> 角色名不能为空，请返回重新修改！</h3>
    <a href="${pageContext.request.contextPath}/adminRole?method=updateRole">点击返回</a>
</c:if><c:if test="${msg=='updateRole1'}">
    <h3 align="center"> 获取不到角色，请返回重新修改！</h3>
    <a href="${pageContext.request.contextPath}/adminRole?method=updateRole">点击返回</a>
</c:if><c:if test="${msg=='updateRole2'}">
    <h3 align="center"> 角色修改失败，请返回重新修改！</h3>
    <a href="${pageContext.request.contextPath}/adminRole?method=updateRole">点击返回</a>
</c:if>

<c:if test="${msg=='deleteRole1'}">
    <h3 align="center"> 角色删除失败，请返回重新删除！</h3>
    <a href="${pageContext.request.contextPath}/adminRole?method=updateRole">点击返回</a>
</c:if>
<script type="text/javascript">
    function delay() {
        window.location = "${pageContext.request.contextPath}/regist?method=registUI";

    }
</script>
</body>
</html>
