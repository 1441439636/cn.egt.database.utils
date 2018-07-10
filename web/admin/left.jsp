<%@ page language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>菜单</title>
    <link href="${pageContext.request.contextPath}/css/left.css" rel="stylesheet" type="text/css"/>
    <link rel="StyleSheet" href="${pageContext.request.contextPath}/css/dtree.css" type="text/css"/>
</head>
<body>
<table width="100" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="12"></td>
    </tr>
</table>
<table width="100%" border="0">
    <tr>
        <td>
            <div class="dtree">

                <a href="javascript: d.openAll();">展开所有</a> | <a href="javascript: d.closeAll();">关闭所有</a>
                <script type="text/javascript" src="${pageContext.request.contextPath}/js/dtree.js"></script>
                <script type="text/javascript">
                    d = new dTree('d');
                    d.add('01', -1, '系统菜单');
                    d.add('0101', '01', '查询设置', '', '', 'mainFrame');
                    d.add('010101', '0101', '字段翻译', '${pageContext.request.contextPath}/adminColumn?method=columnUI', '', 'mainFrame');
                    d.add('0102', '01', '角色', '', '', 'mainFrame');
                    d.add('010201', '0102', '角色', '${pageContext.request.contextPath}/adminRole?method=roleUI', '', 'mainFrame');
                    d.add('010202', '0102', '角色权限', '${pageContext.request.contextPath}/adminRole?method=rolePermissionUI', '', 'mainFrame');
                    d.add('0103', '01', '帐号', '', '', 'mainFrame');
                    d.add('010301', '0103', '帐号', '${pageContext.request.contextPath}/adminAccount?method=accountUI', '', 'mainFrame');
                    d.add('010302', '0103', '帐号角色', '${pageContext.request.contextPath}/adminAccount?method=roleAccountUI', '', 'mainFrame');
                    document.write(d);
                </script>
            </div>
        </td>
    </tr>
</table>
</body>
</html>
