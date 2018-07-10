<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head >
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>会员登录</title>

    <!-- jQuery -->
    <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
    <!-- Loading Bootstrap -->
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">

    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <!-- Loading Uikt -->
    <link href="https://cdn.bootcss.com/uikit/3.0.0-beta.25/css/uikit.min.css" rel="stylesheet">
    <!-- Loading Flat UI -->
    <link href="css/flat-ui.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/login_index.css">

</head>
<body  >
<div id="top"style="margin-top: 100px;"> </span> <span id="njcit" class="h1">南京信息职业技术学院</span></div>
<div class="card" id="login">
    <!--  <div style="padding-top: -10px;">User Login</div> -->
    <div class="card-title" id="title"><span class="h5">通用数据库查询系统</span></div>
    <hr class="hr15">
    <form role="form" class="card-block"  style="margin-top: 30px;" action="${pageContext.request.contextPath}/user?method=login" method="post">
        <div class="input-group card-block">
            <span class="glyphicon glyphicon-user input-group-addon" id="user"></span>
            <input type="text" class="form-control " placeholder="用户名" name="username" aria-describedby="user">
        </div>
        <hr class="hr15">
        <div class="input-group card-block">
            <span class="glyphicon glyphicon-lock input-group-addon" id="password"></span>
            <input type="password" class="form-control " placeholder="密碼" name="password" aria-describedby="password">
        </div>
        <hr class="hr15">
        <div class="input-prepend card-block">
            <button class="btn btn-primary  btn-large btn-block" type="submit">登录</button>
            <button class="btn btn-primary  btn-large btn-block" type="button"  onclick="regist()">注册</button>
        </div>
    </form>
    <div style="margin-top:15px;float: right; ">
        <a href="${pageContext.request.contextPath}/AdminLoginUI"><span >管理员登录</span></a>
    </div>

</div>
<div id="buttom">
    <span>Copyright © 2017</span>
</div>
<script type="text/javascript">
    function regist() {
        window.location.href = "${pageContext.request.contextPath}/regist?method=registUI"
    }
</script>
</body>

</html>