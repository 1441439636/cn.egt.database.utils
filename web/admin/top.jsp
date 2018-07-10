<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Language" content="zh-cn">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style type="text/css">
        BODY {
            MARGIN: 0px;
            BACKGROUND-COLOR: #ffffff
        }

        BODY {
            FONT-SIZE: 12px;
            COLOR: #000000
        }

        TD {
            FONT-SIZE: 12px;
            COLOR: #000000
        }

        TH {
            FONT-SIZE: 12px;
            COLOR: #000000
        }
    </style>
    <link href="${pageContext.request.contextPath}/css/Style1.css" rel="stylesheet" type="text/css">
</HEAD>
<body>
<c:if test="empty ${admin}">
    <jsp:forward page="index.jsp"/>
</c:if>
<table width="100%" height="70%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td>
            <img src="${pageContext.request.contextPath}/images/top_100.jpg" style="width: 100%;height: 90%">
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="22px">
    <tr>
        <td height="30" valign="bottom" background="${pageContext.request.contextPath}/images/mis_01.jpg">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" height="22px">
                <tr>
                    <td width="85%" align="left" height="22">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <font color="#000000">
                            <script language="JavaScript">
                                <!--
                                tmpDate = new Date();
                                year = tmpDate.getFullYear();
                                date = tmpDate.getDate();
                                month = tmpDate.getMonth() + 1;
                                document.write(year);
                                document.write("年");
                                document.write(month);
                                document.write("月");
                                document.write(date);
                                document.write("日 ");

                                myArray = new Array(6);
                                myArray[0] = "星期日"
                                myArray[1] = "星期一"
                                myArray[2] = "星期二"
                                myArray[3] = "星期三"
                                myArray[4] = "星期四"
                                myArray[5] = "星期五"
                                myArray[6] = "星期六"
                                weekday = tmpDate.getDay();
                                if (weekday == 0 | weekday == 6) {
                                    document.write(myArray[weekday])
                                }
                                else {
                                    document.write(myArray[weekday])
                                }
                                ;
                                // -->
                            </script>
                        </font>
                    </td>
                    <td width="15%" height="22px">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="22px">
                            <tr>
                                <td width="100px" height="22px" style="padding-left: 20px;">
                                    用户名：admin
                                </td>
                                <td style="padding-left:10px;">
                                    <input type="button" width="100%" value="退出" onclick="exit1()">
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td align="right" width="5%">
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<script type="text/javascript">
    function exit1() {

        window.parent.frames.location.href = "${pageContext.request.contextPath}/user?method=logout";
    }
</script>
</body>

</HTML>
