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
        $("input[name='accountName']").each(function (index, item) {
            item.removeAttribute("readonly");
        });
        $("input[name='accountPasswd']").each(function (index, item) {
            item.removeAttribute("readonly");
        });
        $("input[name='alterBTN']").each(function (index, item) {
            item.type = "button";
        });
        $("input[name='deleteBTN']").each(function (index, item) {
            item.type = "button";
        });
    }
    function achieveBTN() {
        $("input[name='accountName']").each(function (index, item) {
            item.setAttribute("readonly", true);
        });
        $("input[name='accountPasswd']").each(function (index, item) {
            item.setAttribute("readonly", true);
        });
        $("input[name='alterBTN']").each(function (index, item) {
            item.type = "hidden";
        });
        $("input[name='deleteBTN']").each(function (index, item) {
            item.type = "hidden";
        });
    }

    function addAccountBTN() {
        $("input[name='addAcountNameVal']").each(function (index, item) {
            item.type = "text";
            item.focus();
        });
        $("input[name='addAcountPasswdVal']").each(function (index, item) {
            item.type = "password";
        });
        $("input[name='addBTN']").each(function (index, item) {
            item.type = "submit";
        });


    }
    function updateAccountBTN(id) {
        var accountNameVal = $("input[id='accountName" + id + "']").val();
        var accountPasswdVal = $("input[id='accountPasswd" + id + "']").val();
        if (accountNameVal == "" || accountNameVal == null) {
            alert("账户不能为空，请认真填写！");
        } else if (accountPasswdVal == "" || accountPasswdVal == null) {
            alert("密码不能为空，请认真填写！");
        } else {
            $("form[name='Form1']").each(function (index, item) {
                item.setAttribute("action", "${pageContext.request.contextPath}/adminAccount?method=updateAccount&accountIdVal=" + id + "&accountNameVal=" + accountNameVal + "&accountPasswdVal=" + accountPasswdVal);
            });
            $("form[name='Form1']").submit();
        }
    }
    function deleteAccountBTN(id) {
        $("form[name='Form1']").each(function (index, item) {
            item.setAttribute("action", "${pageContext.request.contextPath}/adminAccount?method=deleteAccount&accountIdVal=" + id);
        });
        $("form[name='Form1']").submit();
    }
    function formSubmit() {
        var accountNameVal = $("input[id='addAcountNameVal']").val();
        var accountPasswdVal = $("input[id='addAcountPasswdVal']").val();
        if ((accountNameVal == "" || accountNameVal == null) && (accountPasswdVal == "" || accountPasswdVal == null)) {
            return true;
        }
        else if (accountNameVal == "" || accountNameVal == null) {
            alert("账户不能为空，请认真填写！");
            $("input[id='addAcountNameVal]").focus();
            return false;
        } else if (accountPasswdVal == "" || accountPasswdVal == null) {
            alert("密码不能为空，请认真填写！");
            $("input[id='addAcountPasswdVal]").focus();
            return false;
        } else
            return true;
    }
</script>
<body>
<!--  -->
<form id="Form1" name="Form1" action="${pageContext.request.contextPath}/adminAccount?method=addAccount"
      method="post" onsubmit="return formSubmit();"
>
    <input type="hidden" name="method" value="updateColumn">
    &nbsp;
    <table cellSpacing="1" cellPadding="4" width="520px" align="center" bgColor="#eeeeee"
           style="border: 1px solid #8ba7e3" border="0">
        <tr>
            <td width="200px" class="ta_01" align="center" bgColor="#afd1f3"
                height="35">
                <h5>账户名</h5>
            </td>
            <td width="200px" class="ta_01" align="center" bgColor="#afd1f3"
                height="35">
                <h5>密码</h5>
            </td>
            <td width="100px" class="ta_01" align="center" bgColor="#afd1f3" height="35">
                <input type="button" class="btn btn-default" style="height: 30px;" value="编辑" onclick="compileBTN()">
            </td>
            <td width="100px" class="ta_01" align="center" bgColor="#afd1f3"
                height="35">
                <input type="button" class="btn btn-default" style="height: 30px;" value="完成" onclick="achieveBTN()">
            </td>
        </tr>
        <c:forEach var="account" items="${accountList}" varStatus="varStatus">
            <tr>
                <td bgColor="#f5fafe" align="center" class="ta_01" style="padding-right: 10px;">
                        <%--<input type="hidden" value="${role[0]}" name="role_id">--%>
                    <input type="text" class="form-control" value="${account[1]}" name="accountName"
                           id="accountName${account[0]}"
                           style="width: 100px;"
                           readonly>
                </td>
                <td bgColor="#f5fafe" align="center" class="ta_01" style="padding-right: 10px;">
                    <input type="password" class="form-control" value="${account[2]}" name="accountPasswd"
                           id="accountPasswd${account[0]}"
                           style="width: 100px;"
                           readonly>
                </td>
                <td align="center" bgColor="#ffffff" class="ta_01">
                    <input type="hidden" class="btn" name="alterBTN"
                           style="width:50px;text-align: center; height:28px;line-height: 24px;padding:0px 2px;"
                           value="修改" onclick="updateAccountBTN( ${account[0]})">
                </td>
                <td class="ta_01" align="center" bgColor="#ffffff">
                    <input type="hidden" class="btn" name="deleteBTN"
                           style="width:50px;text-align: center; height:28px;line-height: 24px;padding:0px 2px;"
                           value="删除" onclick="deleteAccountBTN(${account[0]})">
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td class="ta_01" align="center" bgColor="#afd1f3" height="35">
                <input type="hidden" class="form-control" id="addAcountNameVal" name="addAcountNameVal"
                       style=" margin-left: 10px; width: 100px;" placeholder="账户名">
            </td>
            <td class="ta_01" align="center" bgColor="#afd1f3" height="35">
                <input type="hidden" class="form-control" id="addAcountPasswdVal" name="addAcountPasswdVal"
                       style=" margin-left: 10px; width: 100px;" placeholder="账户密码">
            </td>
            <td class="ta_01" align="right" bgColor="#afd1f3" height="35">
                <input type="button" class="btn btn-default" onclick="addAccountBTN()"
                       style="margin-right: 20px;height: 30px;"
                       value="添加账户">
            </td>

            <td class="ta_01" align="left" bgColor="#afd1f3" height="35">
                <input type="hidden" class="btn btn-default" name="addBTN"
                       style=" text-align: center; height:28px;line-height: 24px;padding:0px 2px;"
                       value="完成添加">
            </td>
        </tr>
    </table>
</form>
</body>
</HTML>