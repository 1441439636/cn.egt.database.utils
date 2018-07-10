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

    <script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
    <!-- Loading Bootstrap -->
    <link rel="stylesheet" type="text/css" href="http://apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.css">
    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <!-- Loading Uikt -->
    <link href="https://cdn.bootcss.com/uikit/3.0.0-beta.25/css/uikit.min.css" rel="stylesheet">
    <!-- Loading Flat UI -->
    <link href="css/flat-ui.css" rel="stylesheet">

    <!-- Loading Base -->
    <link rel="stylesheet" type="text/css" href="css/base.css">

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
            width: 100px;
            height: 30px;
        }

        hr.hr15 {
            height: 15px;
            border: none;
            margin: 0px;
            padding: 0px;
            width: 100%;
        }

        .text1 {
            background: #EEE;
            border-top: 0px;
            border-left: 0px;
            border-bottom: 1px solid gray;
        }

        .textDiv {
            float: left;
            padding-left: 150px;
        }

        .text2 {
            background: #EEE;
            border: none;
            width: 130px;
            padding-left: 8px;
        }

        select option {
            font-size: 16px;
            font-family: 黑体;
        }

        /*清除ie的默认选择框样式清除，隐藏下拉箭头*/
        /*select::-ms-expand { display: none; }*/
    </style>
</head>

<body background="images/background.png">
<!-- Static navbar -->
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
            </button>
            <a class="navbar-brand" href="#">通用数据查询系统</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a><img src="images/user.png" height="15px" width="15px"> ${user.username}</a></li>
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
                <form id="form1" name="Form1" action="${pageContext.request.contextPath}/user?method=resultUI"
                      method="post">
                    <div>
                        <div class="uk-card uk-card-default uk-card-hover" style="background: #EEE">
                            <div class="uk-card-body">
                                <%--表格--%>
                                <table class="table table-hover" id="table" style="text-align: center;">
                                    <c:forEach var="result" items="${sessionScope.resultList.get(0)}"
                                               varStatus="resultStatus">
                                        <input type="hidden" name="resultName" value="${result}"/>
                                    </c:forEach>
                                    <thead>
                                    <!-- 表格的标题行 -->
                                    <tr>
                                        <%--输出表格的标题行--%>
                                        <c:forEach var="title" items="${sessionScope.resultList.get(0)}"
                                                   varStatus="resultValStatus">
                                            <c:forEach var="result" items="${title}" varStatus="resultStatus">
                                                <th style="text-align: center;"  >${result}</th>
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
                                                <input name="${sessionScope.resultList.get(0).get(resultStatus.index)}"
                                                       value="${result}" hidden/>
                                            </c:forEach>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <hr style="color: white;">
                                <%--选择--%>
                                <div style="margin-top: 10px;">
                                    <div>
                                        <label>选择统计项：</label>
                                        <select id="statisticalItem" name="statisticalItem">
                                            <option>请选择！</option>
                                            <c:forEach var="result" items="${sessionScope.resultList.get(0)}"
                                                       varStatus="resultStatus">
                                                <c:if test="${sessionScope.adornColumnType.get(resultStatus.index)=='int'}">
                                                    <option>${result}</option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                        <label style="margin-left: 10px;">选择规则：</label>
                                        <select id="statisticalRule" name="statisticalRule">
                                            <option>请选择！</option>
                                            <c:forEach var="result" items="${sessionScope.resultList.get(0)}"
                                                       varStatus="resultStatus">
                                                <option>${result}</option>
                                            </c:forEach>
                                        </select>
                                        <label style="margin-left: 10px;">统计方式：</label>
                                        <select id="statisticalPattern" name="statisticalPattern">
                                            <option>全部汇总</option>
                                            <option>求和</option>
                                            <option>平均值</option>
                                            <option>最大值</option>
                                            <option>最小值</option>
                                        </select>
                                    </div>
                                    <div>
                                        <button style="margin-top: 15px" class="btn btn-inverse" onclick="">查询</button>
                                        <button type="button" style="margin-top: 15px;margin-left: auto;"
                                                class="btn btn-inverse btn-default" onclick="OutPut()">输出
                                        </button>
                                        <button type="button" style="margin-top: 15px;margin-left: auto;"
                                                class="btn btn-inverse btn-default" onclick="echarts()">数据统计
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
    <div id="mainBar" style=" border:1px solid #ccc;padding:10px;" hidden="hidden">

        <div class="textDiv">
            <label> 标题：</label> <input type="text" class="text1" align="center" id="mainTitle"
                                       placeholder="请输入标题"
                                       value="">
        </div>
        <div>
            <label>副标题：</label> <input type="text" class="text1" align="center" id="secondTitle"
                                       placeholder="请输入范围"
                                       value="">
            <hr class="hr15">
        </div>
        <div class="textDiv">
            <label>选择统计数据(x轴)：</label>
            <select id="xAxisData" name="xAxisData">
                <option>请选择！</option>
                <c:forEach var="result" items="${sessionScope.resultList.get(0)}"
                           varStatus="resultStatus">
                    <option>${result}</option>
                </c:forEach>
            </select>
            <hr class="hr15">
        </div>
        <div>
            <label>选择统计数据(y轴)：</label>
            <select id="yAxisData" name="yAxisData">
                <option>请选择！</option>
                <c:forEach var="result" items="${sessionScope.resultList.get(0)}"
                           varStatus="resultStatus">
                    <c:if test="${sessionScope.adornColumnType.get(resultStatus.index)=='int'}">
                        <option>${result}</option>
                    </c:if>
                </c:forEach>
            </select>
            <hr class="hr15">
            <button type="button" style="margin-top: 15px;margin: auto 0;"
                    class="btn btn-inverse btn-default" onclick="echartsFun()">输出图表
            </button>
        </div>
    </div>

    <div id="echarts" style="height:500px;border:1px solid #ccc;padding:10px;" hidden="hidden">

    </div>
</div>
<!-- /container -->
<!-- footer
    ================================================== -->
<!-- /footer -->
<!-- javascript
    ================================================== -->
<!-- /script -->
<!-- Flat-UI -->
<%--<script src="js/flat-ui.min.js"></script>--%>
<!-- Uikit -->
<script src="https://cdn.bootcss.com/uikit/3.0.0-beta.25/js/uikit.min.js"></script>
<!-- Self-->
<%--<script type="text/javascript" src="js/search.js"></script>--%>
<%--<script type="text/javascript" src="js/movie.js"></script>--%>
<%--<script src="php/movies.php"></script>--%>
<%--<script src="php/news.php"></script>--%>
<%--<script src="php/yixi.php"></script>--%>
<br><br><br><br><br><br><br><br><br><br><br><
</body>
<script type="text/javascript">
    function OutPut() {
        var element = document.getElementById("form1");
        element.action = "${pageContext.request.contextPath}/download";
        element.submit();

    }
    function Go_Back() {
        window.history.go(-1);
    }
    function echarts() {
        $("#mainBar").removeAttr("hidden");
    }
    function echartsFun() {

        var yData=   $("#yAxisData  option:selected").val();
        var xData  =$("#xAxisData  option:selected").val();
        var yList= $("* [name='" +  yData.trim() + "']");
        var xList= $("* [name='" + xData.trim() + "']");
        $("#echarts").removeAttr("hidden");


        // Step:3 conifg ECharts's path, link to echarts.js from current page.
        // Step:3 为模块加载器配置echarts的路径，从当前页面链接到echarts.js，定义所需图表路径
        require.config({
            paths: {
                echarts: 'http://echarts.baidu.com/build/dist'
            }
        });

        // Step:4 require echarts and use it in the callback.
        // Step:4 动态加载echarts然后在回调函数中开始使用，注意保持按需加载结构定义图表路径
        require(
            [
                //这里的'echarts'相当于'./js'
                'echarts',
                'echarts/chart/bar',
                'echarts/chart/line',
            ],
            //创建ECharts图表方法
            function echartsFun(ec) {
                //--- 折柱 ---
                //基于准备好的dom,初始化echart图表
                var myChart = ec.init(document.getElementById('echarts'));
                //定义图表option
                var option = {
                    //标题，每个图表最多仅有一个标题控件，每个标题控件可设主副标题
                    title: {
                        //主标题文本，'\n'指定换行
                        text: $("#mainTitle").val(),
                        //主标题文本超链接
                        link: '',
                        //副标题文本，'\n'指定换行
                        subtext: $("#secondTitle").val(),
                        //副标题文本超链接
                        sublink: '',
                        //水平安放位置，默认为左侧，可选为：'center' | 'left' | 'right' | {number}（x坐标，单位px）
                        x: 'left',
                        //垂直安放位置，默认为全图顶端，可选为：'top' | 'bottom' | 'center' | {number}（y坐标，单位px）
                        y: 'top'
                    },
                    //提示框，鼠标悬浮交互时的信息提示
                    tooltip: {
                        //触发类型，默认（'item'）数据触发，可选为：'item' | 'axis'
                        trigger: 'axis'
                    },
                    //图例，每个图表最多仅有一个图例
                    legend: {
                        //显示策略，可选为：true（显示） | false（隐藏），默认值为true
                        show: true,
                        //水平安放位置，默认为全图居中，可选为：'center' | 'left' | 'right' | {number}（x坐标，单位px）
                        x: 'center',
                        //垂直安放位置，默认为全图顶端，可选为：'top' | 'bottom' | 'center' | {number}（y坐标，单位px）
                        y: 'top',
                        //legend的data: 用于设置图例，data内的字符串数组需要与sereis数组内每一个series的name值对应

                        data: [yData]
                    },
                    //工具箱，每个图表最多仅有一个工具箱
                    toolbox: {
                        //显示策略，可选为：true（显示） | false（隐藏），默认值为false
                        show: true,
                        //启用功能，目前支持feature，工具箱自定义功能回调处理
                        feature: {
                            //辅助线标志
                            mark: {show: true},
                            //dataZoom，框选区域缩放，自动与存在的dataZoom控件同步，分别是启用，缩放后退
                            dataZoom: {
                                show: true,
                                title: {
                                    dataZoom: '区域缩放',
                                    dataZoomReset: '区域缩放后退'
                                }
                            },
                            //数据视图，打开数据视图，可设置更多属性,readOnly 默认数据视图为只读(即值为true)，可指定readOnly为false打开编辑功能
                            dataView: {show: true, readOnly: true},
                            //magicType，动态类型切换，支持直角系下的折线图、柱状图、堆积、平铺转换
                            magicType: {show: true, type: ['line', 'bar']},
                            //restore，还原，复位原始图表
                            restore: {show: true},
                            //saveAsImage，保存图片（IE8-不支持）,图片类型默认为'png'
                            saveAsImage: {show: true}
                        }
                    },
                    //是否启用拖拽重计算特性，默认关闭(即值为false)
                    calculable: true,
                    //直角坐标系中横轴数组，数组中每一项代表一条横轴坐标轴，仅有一条时可省略数值
                    //横轴通常为类目型，但条形图时则横轴为数值型，散点图时则横纵均为数值型
                    xAxis: [
                        {
                            //显示策略，可选为：true（显示） | false（隐藏），默认值为true
                            show: true,
                            //坐标轴类型，横轴默认为类目型'category'
                            type: 'category',
                            //类目型坐标轴文本标签数组，指定label内容。 数组项通常为文本，'\n'指定换行
                            data: xList.toArray()
                        }
                    ],
                    //直角坐标系中纵轴数组，数组中每一项代表一条纵轴坐标轴，仅有一条时可省略数值
                    //纵轴通常为数值型，但条形图时则纵轴为类目型
                    yAxis: [
                        {
                            //显示策略，可选为：true（显示） | false（隐藏），默认值为true
                            show: true,
                            //坐标轴类型，纵轴默认为数值型'value'
                            type: 'value',
                            //分隔区域，默认不显示
                            splitArea: {show: true}
                        }
                    ],

                    //sereis的数据: 用于设置图表数据之用。series是一个对象嵌套的结构；对象内包含对象
                    series: [
                        {
                            //系列名称，如果启用legend，该值将被legend.data索引相关
                            name: yData,
                            //图表类型，必要参数！如为空或不支持类型，则该系列数据不被显示。
                            type: 'bar',
                            //系列中的数据内容数组，折线图以及柱状图时数组长度等于所使用类目轴文本标签数组axis.data的长度，并且他们间是一一对应的。数组项通常为数值
                            data:  yList.toArray()/*,
                            //系列中的数据标注内容
                            markPoint: {
                                data: [
                                    {type: 'max', name: '最大值'},
                                    {type: 'min', name: '最小值'}
                                ]
                            },*/
                            //系列中的数据标线内容
                         /*   markLine: {
                                data: [
                                    {type: 'average', name: '平均值'}
                                ]
                            }*/
                        }
                    ]
                };
                //为echarts对象加载数据
                myChart.setOption(option);
            }
        );
    }
</script>
</html>