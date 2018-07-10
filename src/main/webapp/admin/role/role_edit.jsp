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
    function compileBTN() {
        $("input[name='role_name']").each(function (index, item) {
            item.removeAttribute("readonly");
        });
        $("input[name='alterBTN']").each(function (index, item) {
//            item.removeAttribute("disabled");
            item.type = "button";
        });
        $("input[name='deleteBTN']").each(function (index, item) {
//            item.removeAttribute("disabled");
            item.type = "button";
        });


    }
    function achieveBTN() {
        $("input[name='role_name']").each(function (index, item) {
            item.setAttribute("readonly", true);
        });
        $("input[name='alterBTN']").each(function (index, item) {
//            item.setAttribute("disabled","disabled");
            item.type = "hidden";
        });
        $("input[name='deleteBTN']").each(function (index, item) {
            //            item.setAttribute("disabled","disabled");
            item.type = "hidden";
        });
    }
    function addRoleBTN() {
        $("input[name='add_role_name']").each(function (index, item) {
            //            item.setAttribute("disabled","disabled");
            item.type = "text";
            item.focus();
        });
        $("input[name='addBTN']").each(function (index, item) {
            //            item.setAttribute("disabled","disabled");
            item.type = "submit";
        });
    }
    function updateRoleBTN(v) {
        var result = $("input[id='" + v + "']").val();
        $("form[name='Form1']").each(function (index, item) {
            item.setAttribute("action", "${pageContext.request.contextPath}/adminRole?method=updateRole&update_role_name=" + result + "&update_role_id=" + v);
        });
        $("form[name='Form1']").submit();
    }
    function deleteRoleBTN(v) {
        var result = $("input[id='" + v + "']").val();
        $("form[name='Form1']").each(function (index, item) {
            item.setAttribute("action", "${pageContext.request.contextPath}/adminRole?method=deleteRole&delete_role_name=" + result+ "&update_role_id=" + v);
        });
        $("form[name='Form1']").submit();
    }
</script>
<body>
<!--  -->
<form id="Form1" name="Form1" action="${pageContext.request.contextPath}/adminRole?method=addRole"
      method="post"
>
    <input type="hidden" name="method" value="updateColumn">
    &nbsp;
    <table cellSpacing="1" cellPadding="4" width="520px" align="center" bgColor="#eeeeee"
           style="border: 1px solid #8ba7e3" border="0">
        <tr>
            <td width="300px" class="ta_01" align="center" bgColor="#afd1f3" colSpan="2"
                height="35">
                <h5>角色名</h5>
            </td>
            <td width="100px" class="ta_01" align="center" bgColor="#afd1f3" height="35">
                <input type="button" class="btn btn-default" style="height: 30px;" value="编辑" onclick="compileBTN()">
            </td>
            <td width="100px" class="ta_01" align="center" bgColor="#afd1f3"
                height="35">
                <input type="button" class="btn btn-default" style="height: 30px;" value="完成" onclick="achieveBTN()">
            </td>
        </tr>
        <c:forEach var="role" items="${roleList}" varStatus="varStatus">
            <tr>
                <td bgColor="#f5fafe" align="center" class="ta_01" style="padding-right: 10px;" colspan="2">
                        <%--<input type="hidden" value="${role[0]}" name="role_id">--%>
                    <input type="text" class="form-control" value="${role[1]}" name="role_name" id="${role[0]}"
                           style="width: 100px;"
                           readonly>
                </td>
                <td align="center" bgColor="#ffffff" class="ta_01">
                    <input type="hidden" class="btn" name="alterBTN"
                           style="width:50px;text-align: center; height:28px;line-height: 24px;padding:0px 2px;"
                           value="修改" onclick="updateRoleBTN( ${role[0]})">
                </td>
                <td class="ta_01" align="center" bgColor="#ffffff">
                    <input type="hidden" class="btn" name="deleteBTN"
                           style="width:50px;text-align: center; height:28px;line-height: 24px;padding:0px 2px;"
                           value="删除" onclick="deleteRoleBTN(${role[0]})">
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td class="ta_01" align="right" bgColor="#afd1f3" height="35">
                <input type="button" class="btn btn-default" onclick="addRoleBTN()"
                       style="margin-right: 20px;height: 30px;"
                       value="添加角色">
            </td>
            <td class="ta_01" align="center" bgColor="#afd1f3" height="35">
                <input type="hidden" class="form-control" name="add_role_name"
                       style=" margin-left: 10px; width: 100px;" placeholder="角色名">
            </td>
            <td class="ta_01" align="left" bgColor="#afd1f3" height="35" colspan="2">
                <input type="hidden" class="btn btn-default" name="addBTN"
                       style=" text-align: center; height:28px;line-height: 24px;padding:0px 2px;"
                       value="完成添加">
            </td>
        </tr>
    </table>
</form>
</body>
</HTML>