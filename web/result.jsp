<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head style="background-image: url(images/background.png);">
    <meta charset="utf-8">
    <title>通用数据查询系统</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- jQuery -->
    <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
    <!-- Loading Bootstrap -->
    <link rel="stylesheet" type="text/css" href="http://apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.css">
    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <!-- Loading Uikt -->
    <link href="https://cdn.bootcss.com/uikit/3.0.0-beta.25/css/uikit.min.css" rel="stylesheet">
    <!-- Loading Flat UI -->
    <link href="css/flat-ui.css" rel="stylesheet">
    <!-- Loading Base -->
    <link rel="stylesheet" type="text/css" href="css/base.css">
</head>

<body background="images/background.png">
<!-- Static navbar -->
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
            </button>
            <a class="navbar-brand" href="api.html">通用数据查询系统</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a><img src="images/user.png" height="15px" width="15px">${user.username}</a></li>
                <li><a href="${pageContext.request.contextPath}/user?method=logout"><img src="images/loginout.png"
                                                                                         height="15px"
                                                                                         width="15px">登出</a></li>
            </ul>
        </div>
    </div>
</div>
<!-- card
    ================================================== -->
<div align="center">
    <div class="uk-section uk-section-default uk-section-small uk-padding-remove-top"
         style="background-image: url(images/background.png)">
        <div class="uk-container">
            <div class="uk-child-width-1-1@m uk-grid-small uk-grid-match " uk-grid>
                <!--数据-->
                <form id="form1" name="Form1"
                      action="${pageContext.request.contextPath}/user?method=resultUI" method="post">
                    <div>
                        <div class="uk-card uk-card-default uk-card-hover" style="background: #EEE">
                            <div class="uk-card-body">
                                <table class="table table-hover" style="text-align: center;">
                                    <thead>
                                    <!-- 表格标签 -->
                                    <tr>
                                        <c:forEach var="title" items="${sessionScope.resultList.get(0)}"
                                                   varStatus="resultValStatus">
                                            <c:forEach var="result" items="${title}" varStatus="resultStatus">
                                                <th style="text-align: center;">${result}</th>
                                            </c:forEach>
                                        </c:forEach>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <%--一行数据--%>
                                    <c:forEach var="resultVal" items="${sessionScope.resultList}"
                                               varStatus="resultValStatus" begin="1">
                                        <tr>
                                            <c:forEach var="result" items="${resultVal}" varStatus="resultStatus">
                                                <%--一列数据--%>
                                                <td>${result}</td>
                                            </c:forEach>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <hr style="color: white;">
                                <div style="margin-top: 10px;">
                                    <div>
                                        <button style="margin-top: 15px" class="btn btn-inverse" onclick="OutPut()">输出
                                        </button>
                                    </div>
                                    <div style="float: left;margin-top: 15px;">
                                        <a onclick="Go_Back()"> <img src="images/return.gif" height="50px" width="50px">返回</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
                <!--数据结束-->
            </div>
        </div>
        <!-- /card -->
    </div>

</div>
<!-- Uikit -->
<script src="https://cdn.bootcss.com/uikit/3.0.0-beta.25/js/uikit.min.js"></script>
<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>


</body>
<script type="text/javascript" src="js/jquery-3.2.1/jquery-3.2.1.js"></script>
<script type="text/javascript">
    function OutPut() {
        var element = document.getElementById("form1");

        element.action = "${pageContext.request.contextPath}/download";
        element.submit();
    }
    function Go_Back() {
        window.history.go(-1);
    }
</script>

</html>