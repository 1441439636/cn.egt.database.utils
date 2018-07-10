<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head style="background-image: url(images/background.png);">
    <meta charset="utf-8">
    <title>通用数据查询系统</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- jQuery -->
    <script src="${pageContext.request.contextPath}/js/jquery-3.2.1/jquery-3.2.1.min.js"></script>
    <!-- Loading Bootstrap -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
    <!-- Loading Uikt -->
    <link href="https://cdn.bootcss.com/uikit/3.0.0-beta.25/css/uikit.min.css" rel="stylesheet">
    <!-- Loading Flat UI -->
    <link href="${pageContext.request.contextPath}/css/flat-ui.css" rel="stylesheet">
    <!-- Loading Base -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
    <%--<link href="${pageContext.request.contextPath }/css/register.css" rel="stylesheet" type="text/css"/>--%>
</head>

<body background="images/background.png">
<!-- Static navbar -->
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
            </button>
            <a class="navbar-brand">通用数据查询系统</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a><img src="images/user.png" height="15px" width="15px"> ${user.username}</a></li>
                <li><a href="${pageContext.request.contextPath}/user?method=logout"><img src="images/loginout.png"
                                                                                         height="15px"
                                                                                         width="15px"> 登出</a></li>
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
                <form id="form1" name="Form1" action="${pageContext.request.contextPath}/user?method=selectUI"
                      method="post">
                    <div>
                        <div class="uk-card uk-card-default uk-card-hover" style="background: #EEE">
                            <div class="uk-card-body">
                                <div style="text-align: left;">
                                    <input type="button" style="margin-top: 15px" class="btn btn-inverse"
                                           onclick="createSetBTN()" value="新建配置">
                                    <input type="button" style="margin-top: 15px" class="btn btn-inverse"
                                           onclick="deleteSetBTN()" value="删除配置">
                                </div>
                                <div style="text-align: right; padding-bottom: 10px">
                                    <b>选择查询表：</b>
                                    <select id="adornTable" name="adornTable">
                                        <c:forEach var="adornTable" items="${adornTableList}" varStatus="varStatus">
                                            <c:choose>
                                                <c:when test="${adornTable== adornTableVal}">
                                                    <option selected="selected">${adornTable}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option>${adornTable}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                    <b>选择配置</b>
                                    <select id="setName" name="setName">
                                        <c:forEach var="setName" items="${setNameList}" varStatus="varStatus">
                                            <c:choose>
                                                <c:when test="${setName==setNameVal}">
                                                    <option selected>${setName}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option>${setName}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="row" style="margin-top:10px">
                                    <div class="col-md-4">
                                        <c:forEach var="adornColumn" items="${adornColumnList}" varStatus="varStatus">
                                            <input type="hidden" name="columnType${varStatus.index}"
                                                   value="${adornColumnTypeList.get(varStatus.index)}"/>
                                            <c:choose>
                                                <c:when test="${tableFlagList.get(varStatus.index)[0]=='Y'}">
                                                    <input type="checkbox" class="checkbox1"
                                                           style="background: #EEE;margin-left: 90px;"
                                                           name="flag${varStatus.index}"
                                                           checked>
                                                    <input type="text" value="${adornColumn}" class="text2"
                                                           name="adornColumn"
                                                           readonly="true">
                                                    <hr class="hr15">
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="checkbox" class="checkbox1"
                                                           style="background: #EEE;margin-left: 90px;"
                                                           name="flag${varStatus.index}">
                                                    <input type="text" value="${adornColumn}" class="text2"
                                                           name="adornColumn"
                                                           readonly="true">
                                                    <hr class="hr15">
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <input type="button" style="margin-top: 10px" id="check" class="btn btn-inverse"
                                               value="全选">
                                        <input type="button" style="margin-top: 10px" id="uncheck"
                                               class="btn btn-inverse" value="全不选">
                                    </div>
                                    <div class="col-md-4">
                                        <c:forEach var="adornColumn" items="${adornColumnList}" varStatus="varStatus">
                                            <input type="text" align="center" name="con1" class="text1"
                                                   placeholder="请输入范围"
                                                   value="${tableFlagList.get(varStatus.index)[1]}">
                                            <hr class="hr15">
                                        </c:forEach>

                                    </div>
                                    <div class="col-md-4">
                                        <c:forEach var="adornColumn" items="${adornColumnList}" varStatus="varStatus">
                                            <input type="text" class="text1" align="center" name="con2"
                                                   placeholder="请输入范围"
                                                   value="${tableFlagList.get(varStatus.index)[2]}">
                                            <hr class="hr15">
                                        </c:forEach>
                                    </div>
                                </div>
                                <div>
                                    <input type="submit" style="margin-top: 15px" class="btn btn-inverse" value="查询">
                                </div>
                                <div style="float: left;margin-top: 15px;">
                                    <a onclick="Go_Back()"> <img src="images/return.gif" height="50px"
                                                                 width="50px">返回</a>
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
<script src="https://cdn.bootcss.com/uikit/3.0.0-beta.25/js/uikit.min.js"></script>
<!-- Self-->
<style>
    select {
        /*Chrome和Firefox里面的边框是不一样的，所以复写了一下*/
        border: solid 1px #000;

        /*很关键：将默认的select选择框样式清除*/
        appearance: none;
        -moz-appearance: none;
        -webkit-appearance: none;

        /*在选择框的最右侧中间显示小箭头图片*/
        background: url("http://ourjs.github.io/static/2015/arrow.png") no-repeat scroll right center transparent;

        /*为下拉小箭头留出一点位置，避免被文字覆盖*/
        padding-right: 14px;
        font-size: 17px;
        font-family: 黑体;
        width: 110px;
        height: 30px;
    }

    select option {
        font-size: 16px;
        font-family: 黑体;
    }

    hr.hr15 {
        height: 15px;
        border: none;
        margin: 0px;
        padding: 0px;
        width: 100%;
    }

    input.text1 {
        background: #EEE;
        border-top: 0px;
        border-left: 0px;
        border-bottom: 1px solid gray;
    }

    input.text2 {
        background: #EEE;
        border: none;
        width: 130px;
        padding-left: 8px;
    }
</style>
<script type="text/javascript">
    $("#uncheck").click(function () {//给全选按钮加上点击事件
        var ck = $(".checkbox1").prop("checked", false);  //让class名为qx的选项的选中状态和全选按钮的选中状态一致。
    })
    $("#check").click(function () {//给全选按钮加上点击事件
        var ck = $(".checkbox1").prop("checked", true);  //让class名为qx的选项的选中状态和全选按钮的选中状态一致。
    })

    function createSetBTN() {
        var setName = prompt('请出入配置名！', '');
        var flag = true;
        $("option").each(function (index, item) {
            if (setName == item.value)
                flag = false;
        });
        if (!flag) {
            alert("配置名已存在，新建配置错误，新建配置关闭！");
        }
        if (setName == '' || setName == null) {
            alert("配置名不能为空，新建配置错误，新建配置关闭！");
            flag = false;
        }
        if (flag) {
            $("#form1").attr("action", "${pageContext.request.contextPath}/user?method=addSet&setName=" + setName);
            $("#form1").submit();
        }
    }

    function Go_Back() {
        window.history.go(-1);
    }

    function deleteSetBTN() {
        $("#form1").attr("action", "${pageContext.request.contextPath}/user?method=deleteSet&setName=" + $("#setName").val());
        $("#form1").submit();
    }

    $("#setName").change(function () {
        var setName = $("#setName").val();
        if (setName != "请选择配置！") {
            $("#form1").attr("action", "${pageContext.request.contextPath}/user?method=selectSet&setName=" + setName);
            $("#form1").submit();
        }
    });
    $("#adornTable").change(function () {
        $("#form1").attr("action", "${pageContext.request.contextPath}/user?method=selectTable&adornTable=" + $("#adornTable").val());
        $("#form1").submit();
    });

</script>
<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
</body>

</html>