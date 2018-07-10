<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <title>数据库信息注册</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link href="${pageContext.request.contextPath }/css/register.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">
        body {
            color: white;
        }
    </style>
</head>
<body>
<div class="register">
    <div class="message">添加配置</div>
    <div id="darkbannerwrap"></div>
    <form method="post" action="${pageContext.request.contextPath}/adminDatabase?method=addDB" method="post">
        <input name="action" value="login" type="hidden">
        <select name="dbType">
            <c:choose>
                <c:when  test="${db.dbType eq 'Oracle'}">
                <option>Oracle</option>
                <option>SqlServer</option>
                <option>MySql</option>
                </c:when>
                <c:when test="${db.dbType eq 'SqlServer'}">
                    <option>SqlServer</option>
                    <option>Oracle</option>
                    <option>MySql</option>
                </c:when>
                <c:otherwise>
                    <option>MySql</option>
                    <option>Oracle</option>
                    <option>SqlServer</option>
                </c:otherwise>
            </c:choose>

          <%--  <c:if test="${db.dbType eq 'Oracle'}">
                <option>Oracle</option>
                <option>SqlServer</option>
                <option>MySql</option>
            </c:if>
            <c:if test="${db.dbType eq 'SqlServer'}">
                <option>SqlServer</option>
                <option>Oracle</option>
                <option>MySql</option>
            </c:if>
            <c:if test="${db.dbType eq 'MySql'}">
                <option>MySql</option>
                <option>Oracle</option>
                <option>SqlServer</option>
            </c:if>--%>
        </select>
        <hr class="hr15">
        <input name="dbUsername" placeholder="账户" required="" type="text" value="${db.username}">
        <hr class="hr15">
        <input name="dbPasswd" placeholder="口令" required="" type="password" value="${db.passwrod}">
        <hr class="hr15">
        <input name="dbAddress" placeholder="地址" required="" type="text" value="${db.address}">
        <hr class="hr15">
        <input name="database" placeholder="库名" required="" type="text" value="${db.database}">
        <hr class="hr15">
        <input value="提交" style="width:100%;" type="submit">
        <hr class="hr20">
    </form>
    <a href="${pageContext.request.contextPath}/login.jsp" style="float: right;"><img src="images/back.png" width="25px"
                                                                                      height="25px">返回</a>
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
