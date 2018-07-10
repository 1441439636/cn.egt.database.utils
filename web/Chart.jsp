<%--
  Created by IntelliJ IDEA.
  User: WYX
  Date: 2017/10/8 0008
  Time: 21:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

</head>
<body>
<div style="margin: 0px auto; width:100%;display:table">
    <form id="form2" name="Form2" <%--class="uk-margin uk-grid-small uk-child-width-auto uk-grid"--%>
          style="margin: 0px auto; width:100%;display:table">
        <div class="uk-column-1-2">


            <%--<label  style="width: 50%;text-align: right;line-height: 50px;margin-left:200px">--%>
            <div style="display: table;padding-left: 450px;height:40px">
                主键： <select name="main_column" style="width:150px;border:none;height:30px;">
                <c:forEach var="result" items="${sessionScope.resultList.get(0)}" varStatus="resultStatus">
                    <option value="${result}">${result}</option>
                </c:forEach>
            </select>
            </div>
            <%--</label>--%>


            <%--<label  style="width: 50%;text-align:left;line-height: 50px;">--%>
            <div style="display: table;padding-left:30px;height:40px">
                选择图表类型： <select name="chart" style="width:150px;border:none;height:30px;">
                <option value="BarChart">柱状图</option>
                <option value="LineChart">折线图</option>
            </select>
                <%--</label>--%>
            </div>
        </div>
        <br>


        <div style="margin: auto; display:table">

            <c:set var="i" value="0"/>
            <c:set var="have" value="0"/>
            <span style="display: inline">选择要图表化的数据：</span>


            <c:forEach var="result" items="${sessionScope.resultList.get(0)}" varStatus="resultStatus">
                <c:choose>
                    <c:when test="${sessionScope.adornColumnType.get(i)=='int'}">
                        <c:set var="have" value="1"/>
                        <div style="display: inline;margin: 10px;padding: 10px; ">

                            <label>
                                <input type="checkbox" name="choose"
                                       style="width:30px; height: 15px; vertical-align: middle;padding-left: 30px;margin: 0px auto; "
                                       value="${result}"/>
                            </label>${result}
                        </div>
                    </c:when>

                </c:choose>
                <c:set var="i" value="${i+1}"/>
            </c:forEach>
            <c:if test="${have ==0}">
                <span style="display: inline">没有可图表化的数据</span>
            </c:if>

            <br>
            <div style="display: inline;margin: 5px;padding: 3px; ">

                <input class="btn btn-default" type="button" style=" margin:0px auto;display:table;" value="生成图表"
                       onclick="Judge()">
            </div>
        </div>
    </form>
</div>

<style type="text/css">
    * {
        margin: 0;
        padding: 0;
        list-style-type: none;
    }

    a, img {
        border: 0;
    }

    /* KeFuDiv */
    .KeFuDiv {
        position: absolute;
        cursor: pointer;
    }

</style>

<div id="KeFuDiv" class="KeFuDiv" onmousedown="MoveDiv(KeFuDiv,event);">
</div>
<script type="text/javascript" src="http://7xnlea.com2.z0.glb.qiniucdn.com/online.js"></script>
<script type="text/javascript">
    //初始位置
    gID("KeFuDiv").style.top = (document.documentElement.clientHeight) / 5 + "px";
    gID("KeFuDiv").style.left = document.documentElement.clientWidth / 4 + "px";
    //开始滚动

</script>
<script type="text/javascript" src="js/jquery-3.2.1/jquery-3.2.1.js"></script>
<script type="text/javascript">

    function Judge() {
        var have = ${have};
        if (have === 0) {
            window.alert("没有可以图表化的数据！！");
        } else {
            var choose = document.getElementsByName("choose");
            var num = 0;
            for (var i = 0; i < choose.length; i++) {
                if (choose[i].checked) {
                    num = 1;
                }
            }
            if (num === 0) {
                window.alert("请选择要进行图表化的数据！！");
            }
            getOtherMessage();
        }
    }

    function getOtherMessage() {
        $.ajax({
            type: "post",
            url: "${pageContext.request.contextPath}/view?method=CreateViewData",
            data: $("#form2").serialize(),
            success: function (msg) {
                $("#KeFuDiv").html(msg);
            }

        });
    }
</script>
</body>
</html>
