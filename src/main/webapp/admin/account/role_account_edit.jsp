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
    function selectAllBTN() {
        $("input[type='checkbox']").each(function (index, item) {
            item.setAttribute("checked", "checked");
        });
    }
    function deleteAllBTN() {
        $("input[type='checkbox']").each(function (index, item) {
            item.removeAttribute("checked");
        });
    }

    function role_accountSLT() {
        var roleName = $("#roleName").val();
        $("form[name='Form1']").each(function (index, item) {
            item.setAttribute("action", "${pageContext.request.contextPath}/adminAccount?method=selectRoleAcount&roleNameVal=" + roleName);
//            alert("action="+item.getAttribute("action"));
        });
        $("form[name='Form1']").submit();
    }

</script>
<body>
<form id="userAction_save_do" name="Form1"
      action="${pageContext.request.contextPath}/adminAccount?method=updateRoleAccount"
      method="post"
>
    <input type="hidden" name="method" value="updateColumn">
    &nbsp;
    <table cellSpacing="1" cellPadding="2" width="620px" align="center" bgColor="#eeeeee"
           style="border: 1px solid #8ba7e3" border="0">
        <tr>
            <td width="150px" class="ta_01" align="right" bgColor="#afd1f3"
                height="35">
                <h5>选择角色：</h5>
            </td>
            <td width="150px" class="ta_01" align="left" bgColor="#afd1f3" height="35">
                <select class="form-control" id="roleName" name="roleName"
                        style="  height: 27px; width: auto;padding-top: 1.5px;" onchange="role_accountSLT()">
                    <c:forEach var="roleVal" items="${roleList}" varStatus="varStatus">
                        <c:choose>
                            <c:when test="${roleVal[1]==role_name}">
                                <option selected="selected">${roleVal[1]}</option>
                            </c:when>
                            <c:otherwise>
                                <option>${roleVal[1]}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td class="ta_01" align="right" bgColor="#f5fafe" height="35"
                style="padding-right: 20px;">
                <input type="button" class="btn btn-default" style="height: 30px;" value="全选" onclick="selectAllBTN()">
            </td>
            <td class="ta_01" align="left" bgColor="#ffffff" height="35"
                style="padding-left: 20px;">
                <input type="button" class="btn btn-default" style="height: 30px;" value="反选" onclick="deleteAllBTN()">
            </td>
        </tr>
        <c:forEach var="accountVal" items="${accountList}" varStatus="varStatus">
            <tr>
                <td align="right" bgColor="#f5fafe" class="ta_01" style="padding-right: 10px;">
                    <c:choose>
                        <c:when test="${accountVal[2]=='true'}">
                            <input type="checkbox" name="accountId${accountVal[0]}" checked
                                   id="${accountVal[0]}"
                                   style="width: 100px;"
                            >
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="accountId${accountVal[0]}"
                                   id="${accountVal[0]}"
                                   style="width: 100px;"
                            >
                        </c:otherwise>
                    </c:choose>
                </td>
                <td align="left" bgColor="#ffffff" class="ta_01" style="padding-left: 10px;">
                    <h4>${accountVal[1]}</h4>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td class="ta_01" align="center" bgColor="#afd1f3" height="35" colspan="2">
                <input type="submit" class="btn btn-default"
                       style=" text-align: center; height:28px;width:60px;line-height: 24px;padding:0px 2px;"
                       value="完成">
            </td>
        </tr>
    </table>
</form>
</body>
</HTML>