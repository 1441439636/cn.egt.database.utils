<%--
  Created by IntelliJ IDEA.
  User: 14414
  Date: 2017/4/19
  Time: 14:52
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>错误404</title>
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
<body>
<div style="text-align: center;height: 300px;"><h1 style="line-height: 80%;">错误404</h1></div>
<br/><br/>

<c:if test="${msg=='addDBSuccess'}">
    <div align="center">
        <h3 align="center">配置添加成功</h3>
    </div>
</c:if>
<c:if test="${msg=='adminHomeUI'}">
    <div align="center">
        <h3 align="center"> 帐号或密码错误，点击返回管理员登录界面</h3>
        <a href="${pageContext.request.contextPath}/admin/url/adminDatabaseadminLoginUI.jsp">点击登录</a>
    </div>
</c:if>
<c:if test="${msg=='addRole0'}">
    <div align="center">
        <h3 align="center"> 角色名不能为空，请仔细填写查看！</h3>
        <a href="${pageContext.request.contextPath}/adminRole?method=roleUI">点击返回</a>
    </div>
</c:if>
<c:if test="${msg=='addRole1'}">
    <div align="center">
        <h3 align="center"> 角色名已存在，请仔细查看！</h3>
        <a href="${pageContext.request.contextPath}/adminRole?method=roleUI">点击返回</a>
    </div>
</c:if>
<c:if test="${msg=='addRole2'}">
    <div align="center">
        <h3 align="center"> 角色名添加失败，请重新添加！</h3>
        <a href="${pageContext.request.contextPath}/adminRole?method=roleUI">点击返回</a>
    </div>
</c:if>

<c:if test="${msg=='updateRole0'}">
    <div align="center">
        <h3 align="center"> 角色名不能为空，请返回重新修改！</h3>
        <a href="${pageContext.request.contextPath}/adminRole?method=updateRole">点击返回</a>
    </div>
</c:if>
<c:if test="${msg=='updateRole1'}">
    <div align="center">
        <h3 align="center"> 获取不到角色，请返回重新修改！</h3>
        <a href="${pageContext.request.contextPath}/adminRole?method=updateRole">点击返回</a>
    </div>
</c:if>
<c:if test="${msg=='updateRole2'}">
    <div align="center">
        <h3 align="center"> 角色修改失败，请返回重新修改！</h3>
        <a href="${pageContext.request.contextPath}/adminRole?method=updateRole">点击返回</a>
    </div>
</c:if>
<c:if test="${msg=='deleteRole1'}">
    <div align="center">
        <h3 align="center"> 角色删除失败，请返回重新删除！</h3>
        <a href="${pageContext.request.contextPath}/adminRole?method=updateRole">点击返回</a>
    </div>
</c:if>
<%--addAcount--%>
<c:if test="${msg=='addAcount1'}">
    <div align="center">
        <h3 align="center"> 账户已存在，请认真填写！</h3>
        <a href="${pageContext.request.contextPath}/adminAccount?method=accountUI">点击返回</a>
    </div>
</c:if>
<c:if test="${msg=='addAcount2'}">
    <div align="center">
        <h3 align="center"> 账户添加失败，请认真填写！</h3>
        <a href="${pageContext.request.contextPath}/adminAccount?method=accountUI">点击返回</a>
    </div>
</c:if>

<c:if test="${msg=='login0'}">
    <div align="center">
        <h3 align="center">验证码输入错误，请重新登录，或请管理员确认！</h3>
        <a href="${pageContext.request.contextPath}/user?method=loginUI">点击返回登录</a>
    </div>
</c:if>
<c:if test="${msg=='login1'}">
    <div align="center">
        <h3 align="center">账户不存在，请重新输入，或请管理员确认！</h3>
        <a href="${pageContext.request.contextPath}/user?method=login">点击返回登录</a>
    </div>
</c:if><c:if test="${msg=='addSet1'}">
    <div align="center">
        <h3 align="center">该账户的配置已存在，请重新输入配置名！</h3>
        <a href="${pageContext.request.contextPath}/user?method=login">点击返回登录</a>
    </div>
</c:if>
<c:if test="${msg=='deleteSet1'}">
    <div align="center">
        <h3 align="center">该账户的配置不存在，请返回仔细查看！</h3>
        <a href="${pageContext.request.contextPath}/user?method=login">点击返回登录</a>
    </div>
</c:if>
<c:if test="${msg=='resultUI0'}">
    <div align="center">
        <h3 align="center">未选择查询条件，请返回仔细选择！</h3>
        <a href="${pageContext.request.contextPath}/user?method=login">点击返回登录</a>
    </div>
</c:if>
</body>
</html>
