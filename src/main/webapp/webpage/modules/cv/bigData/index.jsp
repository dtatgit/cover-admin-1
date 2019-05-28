<%@ page contentType="text/html;charset=UTF-8" %>
<%--<%@ include file="/webpage/include/taglib.jsp"%>--%>
<%--
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<c:set var="ctx" value="${pageContext.request.contextPath}${fns:getAdminPath()}"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <title>井盖勘察可视化数据大屏</title>
    <link href="${ctxStatic}/bigData/bootstrap/bootstrap.min.css" rel="stylesheet">
    <link href="${ctxStatic}/bigData/css/main.css" rel="stylesheet">
    <!--echarts-->
    <script type="text/javascript" src="${ctxStatic}/bigData/js/echarts.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/bigData/js/dark.js"></script>
    <!--滚动插件-->
    <script type="text/javascript" src="${ctxStatic}/bigData/js/jquery1.42.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/bigData/js/jquery.SuperSlide.2.1.1.js"></script>

</head>

<body>
<article id="main">
    <section class="col-sm-3 main-left">
        <div class="box" style="display: none;">
            <h2><span>区域统计</span></h2>
            <div class="list">
                <div class="list-header col-lg-12">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">区域名称</div>
                    <div class="col-lg-4">数量</div>
                </div>
                <ul class="list-box col-lg-12">
                    <li>
                        <div class="col-lg-1 sort"><span>1</span></div>
                        <div class="col-lg-7 name">鼓楼区</div>
                        <div class="col-lg-4 num">0</div>
                    </li>
                    <li>
                        <div class="col-lg-1 sort"><span>2</span></div>
                        <div class="col-lg-7 name">云龙区</div>
                        <div class="col-lg-4 num">0</div>
                    </li>
                    <li>
                        <div class="col-lg-1 sort"><span>3</span></div>
                        <div class="col-lg-7 name">泉山区</div>
                        <div class="col-lg-4 num">0</div>
                    </li>
                    <li>
                        <div class="col-lg-1 sort"><span>4</span></div>
                        <div class="col-lg-7 name">铜山区</div>
                        <div class="col-lg-4 num">0</div>
                    </li>
                </ul>
            </div>
            <div class="hornborder-group">
                <span class="hornborder horn_left_top"></span>
                <span class="hornborder horn_right_top"></span>
                <span class="hornborder horn_left_bottom"></span>
                <span class="hornborder horn_right_bottom"></span>
            </div>
        </div>
        <div class="box multipleLine">
            <h2><span>产权TOP20</span></h2>
            <div class="list">
                <div class="list-header col-lg-12">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">单位名称</div>
                    <div class="col-lg-4">数量</div>
                </div>
                <div class="ulWrap">
                    <ul class="list-box list-box-first col-lg-12">
                        <c:forEach items="${ownerList}" var="statisVO" varStatus="status">
                            <c:if test="${status.index  <= 9 }">
                                <li>
                                    <div class="col-lg-1 sort"><span>${ status.index + 1}</span></div>
                                    <div class="col-lg-7 name">${statisVO.purpose}</div>
                                    <div class="col-lg-4 num">${statisVO.coverTotalNum}</div>
                                </li>
                          </c:if>
                        </c:forEach>

                    </ul>
                    <ul class="list-box col-lg-12">
                         <c:forEach items="${ownerList}" var="statisVO" varStatus="status">
                           <c:if test="${status.index  >= 10&& status.index  <= 19}">
                                <li>
                                <div class="col-lg-1 sort"><span>${ status.index + 1}</span></div>
                                <div class="col-lg-7 name">${statisVO.purpose}</div>
                                <div class="col-lg-4 num">${statisVO.coverTotalNum}</div>
                                </li>
                          </c:if>
                        </c:forEach>

                    </ul>

                </div>
                <script type="text/javascript">
                    $(".multipleLine").slide({
                        titCell: ".hd ul",
                        mainCell: ".ulWrap",
                        effect: "top",
                        autoPage: true,
                        autoPlay: true
                    });
                </script>
            </div>
            <div class="hornborder-group">
                <span class="hornborder horn_left_top"></span>
                <span class="hornborder horn_right_top"></span>
                <span class="hornborder horn_left_bottom"></span>
                <span class="hornborder horn_right_bottom"></span>
            </div>
        </div>
        <div class="box">
            <h2><span>一周工期统计</span></h2>
            <div id="work"></div>
            <script type="text/javascript">
                //指定图标的配置和数据
                var option = {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: { // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    //控制4周margin
                    grid: {
                        x: 50,
                        x2: 50,
                        y: 30,
                        y2: 30
                    },
                    legend: {
                        data: ['数量', '日期']
                    },
                    //x坐标轴
                    xAxis: [{
                        name: '日期',
                        type: 'category',
                        data: [${indexStatisVO.dateArr[6]}, ${indexStatisVO.dateArr[5]}, ${indexStatisVO.dateArr[4]}, ${indexStatisVO.dateArr[3]}, ${indexStatisVO.dateArr[2]}, ${indexStatisVO.dateArr[1]},${indexStatisVO.dateArr[0]}],
                        axisPointer: {
                            type: 'shadow'
                        }
                    }],
                    //y坐标轴
                    yAxis: [{
                        type: 'value',
                        name: '数量',
                        min: 0,
                        max: 25000,
                        interval: 2500,
                        axisLabel: {
                            formatter: '{value}'
                        }
                    }],
                    series: [{
                        name: '用户数量',
                        type: 'line',
                        symbolSize: 5, //拐点圆的大小
                        symbol: 'circle',
                        itemStyle: {
                            normal : {
                                color:'#fff',  //拐点圆的颜色
                                lineStyle:{
                                    color:'#0697e7'  //线的颜色
                                }
                            }
                        },
                        data: [${indexStatisVO.numArr[6]}, ${indexStatisVO.numArr[5]}, ${indexStatisVO.numArr[4]}, ${indexStatisVO.numArr[3]}, ${indexStatisVO.numArr[2]}, ${indexStatisVO.numArr[1]},${indexStatisVO.numArr[0]}]
                    }]
                };

                var myChart = echarts.init(document.getElementById('work'), 'dark'); //应用dark主题
                myChart.setOption(option);
            </script>
            <div class="hornborder-group">
                <span class="hornborder horn_left_top"></span>
                <span class="hornborder horn_right_top"></span>
                <span class="hornborder horn_left_bottom"></span>
                <span class="hornborder horn_right_bottom"></span>
            </div>
        </div>
        <div class="box">
            <h2><span>损坏占比</span></h2>
            <ul class="damage-list" style="height: 220px">
                <c:forEach items="${damageList}" var="damage">
                    <li><label>${damage.damageName}</label>
                        <div class="Histogram blue"><span class="numWidth" style="width: ${damage.damagePerNum}%;"></span><span class="num">${damage.damagePerNum}%</span></div>
                    </li>
                </c:forEach>

                <style type="text/css">
                    .damage-list li:first-child .numWidth{
                        background: #ffef01!important;
                    }
                    .damage-list li:nth-child(2) .numWidth{
                        background: #d0dc00!important;
                    }
                    .damage-list li:nth-child(3) .numWidth{
                        background: #90c320!important;
                    }
                    .damage-list li:nth-child(4) .numWidth{
                        background: #23ac3a!important;
                    }
                    .damage-list li:nth-child(5) .numWidth{
                        background: #009946!important;
                    }
                    .damage-list li:nth-child(6) .numWidth {
                        background: #009c6d!important;
                    }
                    .damage-list li:nth-child(7) .numWidth {
                        background: #029f98!important;
                    }
                    .damage-list li:nth-child(8) .numWidth {
                        background: #019fc2!important;
                    }
                    .damage-list li:nth-child(9) .numWidth {
                        background: #00a1e9!important;
                    }
                    .damage-list li:nth-child(10) .numWidth {
                        background: #0186d3!important;
                    }
                    .damage-list li:nth-child(11) .numWidth {
                        background: #01499e!important;
                    }
                    .damage-list li:nth-child(12) .numWidth {
                        background: #1d208b!important;
                    }
                    .damage-list li:nth-child(13) .numWidth {
                        background: #611786!important;
                    }
                    .damage-list li:nth-child(14) .numWidth {
                        background: #950684!important;
                    }
                    .damage-list li:nth-child(15) .numWidth {
                        background: #bf0081!important;
                    }
                    .damage-list li:nth-child(16) .numWidth {
                        background: #e6007f!important;
                    }
                    .damage-list li:nth-child(17) .numWidth {
                        background: #e6006a!important;
                    }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            </li>--%>
                </style>
               <%-- <li><label>完好</label>
                    <div class="Histogram blue"><span class="numWidth" style="width: 80%;background: #ffef01"></span><span class="num">${indexStatisVO.perDamageGoodNum}%</span></div>
                </li>
                <li><label>井盖缺失</label>
                    <div class="Histogram green"><span class="numWidth" style="width: 2%;background: #d0dc00"></span><span class="num">${indexStatisVO.perDamageDefectNum}%</span></div>
                </li>
                <li><label>井盖破坏</label>
                    <div class="Histogram red"><span class="numWidth" style="width: 5%;background: #90c320"></span><span class="num">${indexStatisVO.perDamageDestroyNum}%</span></div>
                </li>
                <li><label>井盖倾斜</label>
                    <div class="Histogram red"><span class="numWidth" style="width: 0.37%;background: #23ac3a"></span><span class="num">0.37%</span></div>
                </li>
                <li><label>井周沉降</label>
                    <div class="Histogram oranger"><span class="numWidth" style="width: 16%;background: #009946"></span><span class="num">${indexStatisVO.perDamageRiftNum}%</span></div>
                </li>
                <li><label>沥青路面龟裂</label>
                    <div class="Histogram oranger"><span class="numWidth" style="width: 0.19%;background: #009c6d"></span><span class="num">0.19%</span></div>
                </li>
                <li><label>混凝土路面碎石剥落</label>
                    <div class="Histogram oranger"><span class="numWidth" style="width: 0.16%;background: #029f98"></span><span class="num">0.16%</span></div>
                </li>
                <li><label>混凝土路面裂缝</label>
                    <div class="Histogram oranger"><span class="numWidth" style="width: 0.27%;background: #019fc2"></span><span class="num">0.27%</span></div>
                </li>
                <li><label>混凝土路面坑槽</label>
                    <div class="Histogram oranger"><span class="numWidth" style="width: 2.1%;background: #00a1e9"></span><span class="num">2.1%</span></div>
                </li>
                <li><label>混凝土路面断裂</label>
                    <div class="Histogram oranger"><span class="numWidth" style="width: 0.07%;background: #0186d3"></span><span class="num">0.07%</span></div>
                </li>
                <li><label>砌砖断裂</label>
                    <div class="Histogram oranger"><span class="numWidth" style="width: 0.13%;background: #01499e"></span><span class="num">0.13%</span></div>
                </li>
                <li><label>砌砖缺失、脱落</label>
                    <div class="Histogram oranger"><span class="numWidth" style="width: 0.14%;background: #1d208b"></span><span class="num">0.14%</span></div>
                </li>
                <li><label>建筑物覆盖</label>
                    <div class="Histogram oranger"><span class="numWidth" style="width: 4.46%;background: #611786"></span><span class="num">4.46%</span></div>
                </li>
                <li><label>填埋废弃</label>
                    <div class="Histogram oranger"><span class="numWidth" style="width: 0%;background: #950684"></span><span class="num">0%</span></div>
                </li>
                <li><label>井筒本身破坏</label>
                    <div class="Histogram violet"><span class="numWidth" style="width: 0.07%;background: #bf0081"></span><span class="num">0.07%</span></div>
                </li>
                <li><label>井座破坏</label>
                    <div class="Histogram violet"><span class="numWidth" style="width: 0.81%;background: #e6007f"></span><span class="num">0.81%</span></div>
                </li>
                <li><label>其他</label>
                    <div class="Histogram gray"><span class="numWidth" style="width: 2%;background: #e6006a"></span><span class="num">${indexStatisVO.perDamageOtherNum}%</span></div>
                </li>--%>
            </ul>

            <div class="hornborder-group">
                <span class="hornborder horn_left_top"></span>
                <span class="hornborder horn_right_top"></span>
                <span class="hornborder horn_left_bottom"></span>
                <span class="hornborder horn_right_bottom"></span>
            </div>
        </div>
    </section>
    <section class="col-sm-9 main-right">
        <div class="box box-maps col-lg-12" style="padding: 0px">
            <div class="total">
                <div class="total-num">
                    <h3>${indexStatisVO.coverTodayNum}</h3>
                    <span>今日勘察</span>
                </div>
                <div class="total-num">
                    <h3>${indexStatisVO.coverTotalNum}</h3>
                    <span>已勘察</span>
                </div>
<%--                <div class="total-num">
                    <h3>${indexStatisVO.coverNoDepartNum}</h3>
                    <span>无组别</span>
                </div>--%>
            </div>
            <div class="map col-lg-12" style="padding: 0px;overflow: hidden;">
              <iframe id="map-iframe" src="${pageContext.request.contextPath}/webpage/modules/cv/bigData/map.jsp" scrolling="no"></iframe>
            </div>
            <div class="hornborder-group">
                <span class="hornborder horn_left_top"></span>
                <span class="hornborder horn_right_top"></span>
                <span class="hornborder horn_left_bottom"></span>
                <span class="hornborder horn_right_bottom"></span>
            </div>
        </div>
        <div class="box-col col-lg-12">
            <div class="box col-lg-12">
                <h2><span>用途统计</span></h2>
                <div id="purpose"></div>
                <script type="text/javascript">
                    //指定图标的配置和数据
                    var option = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: { // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        //控制4周margin
                        grid: {
                            x: 50,
                            x2: 50,
                            y: 30,
                            y2: 30
                        },
                        legend: {
                            data: ['数量', '用途']
                        },
                        //x坐标轴
                        xAxis: [{
                            name: '用途',
                            type: 'category',
                            data: [${purposeNames}],
                            axisPointer: {
                                type: 'shadow'
                            }
                        }],
                        //y坐标轴
                        yAxis: [{
                            type: 'value',
                            name: '数量',
                            min: 0,
                            max: 50000,
                            interval: 5000,
                            axisLabel: {
                                formatter: '{value}'
                            }
                        }],
                        series: [{
                            name: '数量',
                            type: 'bar',
                            barWidth: 30, //柱图宽度
                            data: [${purposeNums}]
                        }]
                    };

                    var myChart = echarts.init(document.getElementById('purpose'), 'dark'); //应用dark主题
                    myChart.setOption(option);
                </script>
                <div class="hornborder-group">
                    <span class="hornborder horn_left_top"></span>
                    <span class="hornborder horn_right_top"></span>
                    <span class="hornborder horn_left_bottom"></span>
                    <span class="hornborder horn_right_bottom"></span>
                </div>
            </div>
        </div>
    </section>
</article>
</body>

</html>