<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<HTML>

<HEAD>
    <meta http-equiv="Content-Language" content="zh-cn">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <LINK href="${pageContext.request.contextPath}/css/Style1.css" type="text/css" rel="stylesheet">
</HEAD>
<script type="text/javascript">
    function refresh() {
        var tableName = $('#tableName').val();
        location.href = "${pageContext.request.contextPath}/adminColumn?method=refreshUI&tableName=" + tableName;
    }

</script>
<body>
<!--  -->
<form id="form1" name="Form1" action="${pageContext.request.contextPath}/adminColumn?method=updateColumn"
      method="post"
>
    <input type="hidden" name="method" value="updateColumn">
    &nbsp;
    <table cellSpacing="1" cellPadding="5" width="100%" align="center" bgColor="#eeeeee"
           style="border: 1px solid #8ba7e3" border="0">
        <tr>
            <td class="ta_01" align="right" bgColor="#afd1f3"
                height="35">
                <h5>选择查询对象： </h5></td>
            <td class="ta_01" align="left" bgColor="#afd1f3" colSpan="1"
                height="35">
                <select class="form-control" id="tableName" name="tableName" onchange="refresh()"
                        style="  height: 27px; width: auto;padding-top: 3px;">
                    <c:forEach var="view" items="${viewList}" varStatus="varStatus">
                        <c:choose>
                            <c:when test="${view== tableName}">
                                <option selected="selected">${tableName}</option>
                            </c:when>
                            <c:otherwise>
                                <option>${view}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </td>
            <td class="ta_01" align="center" bgColor="#afd1f3"
                height="35" colSpan="2">
                <label>翻译：</label><input type="text" class="bg" name="tableAdornName" value="${tableAdornName}">
            </td>
            <td class="ta_01" align="left" bgColor="#afd1f3"
                height="35">

            </td>
        </tr>
        <c:forEach var="columnAdorn" items="${columnAdornList}" varStatus="varStatus">
                <tr>
                <td width="15%" align="right" bgColor="#f5fafe" class="ta_01" style="padding-right: 10px;">
                    <c:choose>
                        <c:when test="${columnAdorn.get(0) =='Y'}">
                            <input type="checkbox" checked="checked" name="flag${varStatus.count}">
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="flag${varStatus.count}">
                        </c:otherwise>
                    </c:choose>
                </td>
                <td width="20%" class="ta_01" bgColor="#ffffff">
                    <input type="text" class="bg" name="columnName" readonly value="${columnAdorn.get(3)}"/>
                </td>
                <td width="40%" align="center" bgColor="#f5fafe" class="ta_01" colSpan="2">
                    <label>中文名：</label><input type="text" class="bg" name="columnAdornName"
                                              value="${columnAdorn.get(1)}">
                </td>
                <td class="ta_01" bgColor="#ffffff">
                    <label>排序：</label>
                    <input type="text" class="bg" style="width: 20px;text-align: center;"
                           name="columnNo" value="${columnAdorn.get(2)}">
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td class="ta_01" align="right" bgColor="#afd1f3"
                height="35">
            <td class="ta_01" align="left" bgColor="#afd1f3" colSpan="2"
                height="35">
                <input type="submit" class="btn btn-default" style="float: right;margin-right: 20px;" value="修改">
            </td>
            <td class="ta_01" align="left" bgColor="#afd1f3"
                height="35" colSpan="2">
                <input type="reset" class="btn btn-default" style="float: left;margin-left: 20px;" value="重置">
            </td>
        </tr>
    </table>
</form>
</body>
</HTML>