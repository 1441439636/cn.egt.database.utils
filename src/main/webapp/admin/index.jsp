<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <title>管理中心</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link href="${pageContext.request.contextPath }/css/login.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">
        body {
            color: white;
        }
    </style>
    <script type="text/javascript">
        function userBTN() {
            location.href = '${pageContext.request.contextPath}/user?method=loginUI';
        }
    </script>
</head>
<body>
<div class="login">
    <div class="message">后台登录</div>
    <div id="darkbannerwrap"></div>
    <form method="post" action="${pageContext.request.contextPath }/adminLogin?method=adminHomeUI" target="_parent"
          name='theForm'
          onsubmit="return validate()">
        <input name="action" value="login" type="hidden">
        <input name="username" placeholder="用户名" required="" type="text">
        <hr class="hr15">
        <input name="password" placeholder="密码" required="" type="password">
        <hr class="hr15">
        <input value="登录" style="width:100%;" type="submit">
        <hr class="hr20">
    </form>
    <a href="${pageContext.request.contextPath}/login.jsp"  style="float: right;"><img src="images/back.png" width="25px" height="25px">返回</a>
</div>
<script language="JavaScript">
    <!--
    document.forms['theForm'].elements['username'].focus();

    /**
     * 检查表单输入的内容
     */
    function validate() {
        var validator = new Validator('theForm');
        validator.required('username', user_name_empty);
        //validator.required('password', password_empty);
        if (document.forms['theForm'].elements['captcha']) {
            validator.required('captcha', captcha_empty);
        }
        return validator.passed();
    }

    //-->
</script>
<script type="text/javascript">
    function back() {
        window.location.href = "${pageContext.request.contextPath}/login.jsp";
    }
</script>
</body>
</html>