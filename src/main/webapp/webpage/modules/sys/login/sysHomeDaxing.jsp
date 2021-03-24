<%--
  Created by IntelliJ IDEA.
  User: L
  Date: 2021/02/07
  Time: 14:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%--<%
    //页面每隔30秒自动刷新一遍
    response.setHeader("refresh","30");
%>--%>
<html>
<head>
    <title>首页</title>
    <meta name="decorator" content="ani"/>

    <style>
        #body-container {
            margin-left: 0px !important;
            /*padding: 10px;*/
            margin-top: 0px !important;
            overflow-x: hidden!important;
            transition: all 0.2s ease-in-out !important;
            height: 100% !important;
            /*border: 1px solid #ff0000;*/
        }
        .stat.hvr-wobble-horizontal:last-child {
            margin-bottom: 10px;
        }
        .worklist-container{
            background-color: #ffffff;
            box-sizing: border-box;
        }
        .right-worklist-box{
            background: #fff;
            height:390px;
        }
        .right-worklist-box .item{
            padding: 16px 30px;
            display: flex;
            justify-content: start;
            border: 1px solid rgba(0,0,0,0.1);
            align-items: center;
        }
        .right-worklist-box .item img{
            width: 40px;
            height: 40px;
            max-width: 100%;
            overflow: hidden;
            margin-right: 20px;
        }
        .right-worklist-box .item dl{
            margin: 0;
            padding: 0;
            /*width: 60%;*/
            text-align: center;
            color: #6d6d6d;
        }
        .right-worklist-box .item dl dt{
            font-size: 30px;
            padding: 0;
            margin: 0;
            font-weight: normal;
        }
        .right-worklist-box .item dl dt span{
            font-size: 14px;
        }
        .right-worklist-box .item dd{
            margin-inline-start:0;
            font-size: 14px;
            margin: 0px;
            padding: 0;
        }
        #updateDate{
            position: absolute;
            top:10px;
            right:10px;
        }

    </style>
</head>
<body class="">
<div id="body-container" class="wrapper wrapper-content">
    <div class="conter-wrapper home-container">
        <div class="row home-row">
            <div class="col-md-6 col-lg-6">
                <div class="home-charts-middle">
                    <div class="home-panel-heading panel-heading">
                        <h2>报警数量统计</h2>
                    </div>
                    <div class="chart-container">
                        <div id="alarmNumber" style="height: 330px;width: 100%"></div>
                    </div>
                </div>

            </div>
            <div class="col-md-4 col-lg-4">
                <div class="home-charts-middle">
                    <div class="home-panel-heading panel-heading">
                        <h2>工单完成情况</h2>
                        <span id="updateDate"></span>
                    </div>
                    <div class="chart-container">
                        <div id="workOrder" style="height: 330px;width: 100%"></div>
                    </div>
                </div>

            </div>
            <div class="col-md-2 col-lg-2">
                <div class="worklist-container" >
                    <div class="right-worklist-box" >
                        <a class="item">
                            <img src="${ctxStatic}/common/images/gongdan.png" alt="" />
                            <dl>
                                <dt>${workTotalNum}<span>单</span> </dt>
                                <dd>工单总数</dd>
                            </dl>
                        </a>

                        <a class="item">
                            <img src="${ctxStatic}/common/images/gongdan.png" alt="" />
                            <dl>
                                <dt>${addWorkToday} <span>单</span> </dt>
                                <dd>今日新增工单数</dd>
                            </dl>
                        </a>

                        <a class="item">
                            <img src="${ctxStatic}/common/images/gongdan.png" alt="" />
                            <dl>
                                <dt>${completeWorkToday}<span>单</span> </dt>
                                <dd>今日完成工单</dd>
                            </dl>
                        </a>

                        <a class="item">
                            <img src="${ctxStatic}/common/images/gongdan.png" alt="" />
                            <dl>
                                <dt>${proWorkToday}<span>单</span> </dt>
                                <dd>待完成工单</dd>
                            </dl>
                        </a>

                    </div>
                </div>
            </div>
        </div>
        <div class="row home-row">
            <div class="col-md-3 col-lg-3">
                <div class="home-charts-middle">
                    <div class="home-panel-heading panel-heading">
                        <h2>井盖材质</h2>
                    </div>
                    <div class="chart-container">
                        <div id="texture" style="height: 330px;">
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3 col-lg-3">
                <div class="home-charts-middle">
                    <div class="home-panel-heading panel-heading">
                        <h2>井盖地理场合</h2>
                    </div>
                    <div class="chart-container">
                        <div id="occasion" style="height:330px;width: 100%"></div>
                    </div>
                </div>

            </div>
            <div class="col-md-3 col-lg-3">
                <div class="home-charts-middle" style="overflow: initial">
                    <div class="home-panel-heading panel-heading">
                        <h2>井盖病害</h2>
                    </div>
                    <div class="chart-container">
                        <div id="damage" style="height:330px;"></div>
                    </div>
                </div>
            </div>
            <div class="col-md-3 col-lg-3">
                <div class="home-charts-middle">
                    <div class="home-panel-heading panel-heading">
                        <h2>行业井盖数量</h2>
                    </div>
                    <div class="chart-container">
                        <div id="purpose" style="height:330px"></div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
<script src="vendor/ckeditor/ckeditor.js" type="text/javascript"></script>
<script src="js/vendor.js"></script>
<script src="${ctxStatic}/plugin/echarts4/echarts.min.js"></script>
<script src="${ctxStatic}/plugin/echarts4/macarons.js"></script>
<script>
    $(function(){
        $('#calendar2').fullCalendar({
            eventClick: function(calEvent, jsEvent, view) {
                alert('Event: ' + calEvent.title);
                alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
                alert('View: ' + view.name);
            }
        });

        $('#rtlswitch').click(function() {
            $('body').toggleClass('rtl');
            var hasClass = $('body').hasClass('rtl');
            $.get('/api/set-rtl?rtl='+ (hasClass ? 'rtl': ''));

        });
        $('.theme-picker').click(function() {
            changeTheme($(this).attr('data-theme'));
        });
        $('#showMenu').click(function() {
            $('body').toggleClass('push-right');
        });

    });
    function changeTheme(the) {
        $("#current-theme").remove();
        $('<link>')
            .appendTo('head')
            .attr('id','current-theme')
            .attr({type : 'text/css', rel : 'stylesheet'})
            .attr('href', '/css/app-'+the+'.css');
    }
</script>

<script>
    $(function(){

        //报警数量统计
        const alarmDate=[]
        const alarmData=[]
        <c:forEach items="${alarmDataList}" var="item" varStatus="status" >
        alarmDate.push("${item.alarmTime}".substr(5))
        alarmData.push("${item.alarmNum}");
        </c:forEach>
        var  alarmOptions = {
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            //控制4周margin
            grid:{
                top: '10%',
                left: '2%',
                right: '8%',
                bottom: '3%',
                containLabel: true
            },
            legend: {
                data: ['报警数量']
            },
            //x坐标轴
            xAxis: [
                {
                    name: '报警日期',
                    type: 'category',
                    data: alarmDate,
                    axisPointer: {
                        type: 'shadow'
                    },
                    //设置轴线的属性
                    axisLine:{
                        lineStyle:{
                            color:'#ddd',
                        }
                    },
                    axisLabel: {
                        show: true,
                        textStyle: {
                            color: '#333'
                        }
                    }
                }
            ],
            //y坐标轴
            yAxis: [
                {
                    type: 'value',
                    name: '数量',
                    axisLabel: {
                        formatter: '{value}'
                    },
                    //设置轴线的属性
                    axisLine:{
                        lineStyle:{
                            color:'#ddd',
                        }
                    },
                    //颜色
                    axisLabel: {
                        show: true,
                        textStyle: {
                            color: '#333'
                        }
                    },
                    //分割线
                    splitLine: {
                        show: true,
                        lineStyle:{
                            type:'dashed',
                            color:'#eee',
                        }
                    }

                }
            ],
            series: [
                {
                    name: '报警数量',
                    type: 'bar',
                    barWidth: 45, //柱图宽度
                    itemStyle:{
                        normal:{
                            color:'#69d2e7'
                        }
                    },
                    data: alarmData
                }
            ]
        };
        let alarmNumberChart = echarts.init(document.getElementById('alarmNumber'),'macarons'); //应用dark主题
        alarmNumberChart.setOption(alarmOptions);



        // 工单完成情况
        const finishedList=[]
        const unfinishedList=[]
        const workDate=[]
        let mydate=[]
        <c:forEach items="${workDataList}" var="item" varStatus="status" >
        workDate.push("${item.statisTime}".substr(5))
        finishedList.push("${item.addNum}");
        unfinishedList.push("${item.proNum}");
        mydate.push("${item.updateDate}")
        </c:forEach>
        const updateDate= document.getElementById('updateDate');
        for(let i=0;i<mydate.length;i++){
            if(mydate[i]!== null){
                updateDate.innerText='更新时间:'+ mydate[i]
                break
            }
        }

        let   workerOrderOptions = {
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            //控制4周margin
            grid:{
                top: '10%',
                left: '2%',
                right: '8%',
                bottom: '3%',
                containLabel: true
            },
            legend: {
                data: ['新增', '未处理']
            },
            //x坐标轴
            xAxis: [
                {
                    name: '日期',
                    type: 'category',
                    data: workDate,
                    axisPointer: {
                        type: 'shadow'
                    },
                    //设置轴线的属性
                    axisLine:{
                        lineStyle:{
                            color:'#ddd',
                        }
                    },
                    axisLabel: {
                        show: true,
                        interval:0,rotate:40,
                        textStyle: {
                            color: '#333'
                        }
                    }
                }
            ],
            //y坐标轴
            yAxis: [
                {
                    type: 'value',
                    name: '数量',
                    axisLabel: {
                        formatter: '{value}'
                    },
                    //设置轴线的属性
                    axisLine:{
                        lineStyle:{
                            color:'#ddd',
                        }
                    },
                    //颜色
                    axisLabel: {
                        show: true,
                        textStyle: {
                            color: '#333'
                        }
                    },




                    //分割线
                    splitLine: {
                        show: true,
                        lineStyle:{
                            type:'dashed',
                            color:'#eee',
                        }
                    }

                }
            ],
            series: [
                {
                    name: '新增',
                    type: 'bar',
                    barWidth: 10, //柱图宽度
                    itemStyle:{
                        normal:{
                            color:'#69d2e7'
                        }
                    },
                    data: finishedList
                },
                {
                    name: '未处理',
                    type: 'bar',
                    barWidth: 10, //柱图宽度
                    itemStyle:{
                        normal:{
                            color:'red'
                        }
                    },
                    data: unfinishedList
                }
            ]
        };
        let workerOrderChart = echarts.init(document.getElementById('workOrder'),'macarons'); //应用dark主题
        workerOrderChart.setOption(workerOrderOptions);

        // 井盖材质
        const materialList=[]
        const materialData=[]
        <c:forEach items="${materialList}" var="item" varStatus="status" >
        materialList.push("${item.material}")
        materialData.push({
            name:"${item.material}",
            value:"${item.coverTotalNum}"
        });
        </c:forEach>

        var option3 = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}  {c} ({d}%)"
            },
            legend: {
                orient: 'horizontal',
                bottom: "0%",
                data:materialList
            },
            series: [
                {
                    name:'井盖材质',
                    type:'pie',
                    radius: ['50%', '70%'],
                    avoidLabelOverlap: true,
                    labelLine: {
                        normal: {
                            show: true
                        }
                    },
                    data:materialData
                }
            ]
        };
        let textureChart = echarts.init(document.getElementById('texture'),'macarons');
        textureChart.setOption(option3);

        // 井盖地理场合图表
        const occasionYAxis=[]
        const occasionData=[]
        <c:forEach items="${situationList}" var="item" varStatus="status" >
        occasionYAxis.push("${item.situation}")
        occasionData.push("${item.coverTotalNum}");
        </c:forEach>
        const occasionOptions={
            tooltip: {
                trigger: 'item',
                formatter: '{b} : {c} '
            },
            legend: {},
            grid:{
                left: '20%',
            },
            xAxis: {
            },
            yAxis: {
                data: occasionYAxis
            },
            series: [{
                name: '井盖地理场合',
                type: 'bar',
                data: occasionData
            }]
        }
        let occasionChart = echarts.init(document.getElementById('occasion'),'macarons'); //应用'macarons'主题
        occasionChart.setOption(occasionOptions);

        // 井盖病害
        const damageYAxis=[]
        const damageData=[]
        <c:forEach items="${damageList}" var="item" varStatus="status" >
        damageYAxis.push("${item.damageName}")
        damageData.push("${item.coverTotalNum}");
        </c:forEach>
        const damageOptions={
            tooltip: {
                trigger: 'item',
                formatter: '{b} : {c} '
            },
            legend: {},
            grid:{
                left: '20%',
            },
            xAxis: {
            },
            yAxis: {
                data: damageYAxis
            },
            series: [{
                name: '井盖病害',
                type: 'bar',
                data: damageData
            }]
        }
        let damageChart = echarts.init(document.getElementById('damage'),'macarons'); //应用dark主题
        damageChart.setOption(damageOptions);

        // 行业井盖数量
        const purposeYAxis=[]
        const purposeData=[]
        <c:forEach items="${purposeList}" var="item" varStatus="status" >
        purposeYAxis.push("${item.purpose}")
        purposeData.push("${item.coverTotalNum}")
        </c:forEach>


        const purposeOptions={
            tooltip: {
                trigger: 'item',
                formatter: '{b}:{c} '
            },
            legend: {},
            grid:{
                left: '25%',
            },
            xAxis: {
                type: 'value'
            },
            yAxis: {
                type: 'category',
                data: purposeYAxis,
            },
            series: [{
                name: '行业井盖数量',
                type: 'bar',
                data: purposeData
            }]
        }
        let purposeChart = echarts.init(document.getElementById('purpose'),'macarons');
        purposeChart.setOption(purposeOptions);

        // 监听缩放
        setTimeout(function () {
            window.onresize = function () {
                alarmNumberChart.resize();
                workerOrderChart.resize();
                textureChart.resize();
                occasionChart.resize();
                damageChart.resize();
                purposeChart.resize();
            }
        }, 200);
    });
</script>

</body>
</html>

