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
            min-height:340px;
            box-sizing: border-box;
        }
        .right-worklist-box{
            background: #fff;
        }
        .right-worklist-box .item{
            padding: 10px 30px;
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
                        <div id="alarmNumber" style="height: 280px;width: 100%"></div>
                    </div>
                </div>

            </div>
            <div class="col-md-4 col-lg-4">
                <div class="home-charts-middle">
                    <div class="home-panel-heading panel-heading">
                        <h2>工单完成情况</h2>
                    </div>
                    <div class="chart-container">
                        <div id="workOrder" style="height: 280px;width: 100%"></div>
                    </div>
                </div>

            </div>
            <div class="col-md-2 col-lg-2">
                <div class="worklist-container" >
                    <div class="right-worklist-box" >
                        <a class="item">
                            <img src="${ctxStatic}/common/images/gongdan.png" alt="" />
                            <dl>
                                <dt>${assignNum}<span>单</span> </dt>
                                <dd>今日派单数</dd>
                            </dl>
                        </a>

                        <a class="item">
                            <img src="${ctxStatic}/common/images/gongdan.png" alt="" />
                            <dl>
                                <dt>${completeNum} <span>单</span> </dt>
                                <dd>已完成数</dd>
                            </dl>
                        </a>

                        <a class="item">
                            <img src="${ctxStatic}/common/images/gongdan.png" alt="" />
                            <dl>
                                <dt>${processingNum}<span>单</span> </dt>
                                <dd>待完成数</dd>
                            </dl>
                        </a>

                        <a class="item">
                            <img src="${ctxStatic}/common/images/gongdan.png" alt="" />
                            <dl>
                                <dt>${overtimeNum}<span>单</span> </dt>
                                <dd>超时工单数</dd>
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
                        <h2>报警类型占比</h2>
                    </div>
                    <div class="chart-container">
                        <div id="texture" style="height: 280px;">
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
                        <div id="occasion" style="height:280px;width: 100%"></div>
                    </div>
                </div>

            </div>
            <div class="col-md-3 col-lg-3">
                <div class="home-charts-middle">
                    <div class="home-panel-heading panel-heading">
                        <h2>损坏形式</h2>
                    </div>
                    <div class="chart-container">
                        <div id="damage" style="height:280px"></div>
                    </div>
                </div>
            </div>
            <div class="col-md-3 col-lg-3">
                <div class="home-charts-middle">
                    <div class="home-panel-heading panel-heading">
                        <h2>井盖用途</h2>
                    </div>
                    <div class="chart-container">
                        <div id="purpose" style="height:280px"></div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
<script src="vendor/ckeditor/ckeditor.js" type="text/javascript"></script>
<script src="js/vendor.js"></script>
<script src="${ctxStatic}/plugin/echarts3/echarts.min.js"></script>

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
            console.log('hello');
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
        const alarmDate=['01-01','01-02','01-03','01-04','01-05','01-06','01-07','01-08','01-09','01-10']
        const alarmData=[10,20,30,30,30,30,30,30,59,30]
        var  alarmOptions = {
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            //控制4周margin
            grid:{
                // x:50,
                // x2:50,
                // y:40,
                // y2:20
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
                    name: '报警数量',
                    interval: 5,
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
        let alarmNumberChart = echarts.init(document.getElementById('alarmNumber')); //应用dark主题
        alarmNumberChart.setOption(alarmOptions);
            // 工單
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
                    name: '报警数量',
                    interval: 5,
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
                    data: alarmData
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
                    data: alarmData
                }
            ]
        };
        let workerOrderChart = echarts.init(document.getElementById('workOrder')); //应用dark主题
        workerOrderChart.setOption(workerOrderOptions);

        const materialList=['铸铁','混凝土','复合材料']
        const materialData=[
            {name:"铸铁",value:90},
            {name:"混凝土",value:5},
            {name:"复合材料",value:5}
            ]
        // 井盖材质
        var option3 = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}  {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                x: 'left',
                data:materialList
            },
            series: [
                {
                    name:'报警类型',
                    type:'pie',
                    radius: ['50%', '70%'],
                    avoidLabelOverlap: true,
                    label: {
                        normal: {
                            show: true,
                            position: 'center'
                        },
                        emphasis: {
                            show: true,
                            textStyle: {
                                fontSize: '30',
                                fontWeight: 'bold'
                            }
                        }
                    },
                    labelLine: {
                        normal: {
                            show: true
                        }
                    },
                    data:materialData
                }
            ]
        };
        let textureChart = echarts.init(document.getElementById('texture')); //应用dark主题
        textureChart.setOption(option3);

        // 井盖地理场合图表
        const occasionYAxis=['机动车辆', '非机动车辆', '人行道','侧分带','中分带','红线外绿化']
        const occasionData=[1,2,3,4,5,6]
        const occasionOptions={
            tooltip: {
                trigger: 'item',
                formatter: '{b} : {c}元 '
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
        let occasionChart = echarts.init(document.getElementById('occasion')); //应用dark主题
        occasionChart.setOption(occasionOptions);

        // 损坏形式
        const damageData=[
            {name:"井盖缺失",value:50},
            {name:"井盖破损",value:50}
        ]
        const damageOptions={
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            series: [
                {
                    name: '损坏形式',
                    type: 'pie',
                    roseType: 'radius',
                    radius: [15, 60],
                    center: ['50%', '45%'],
                    data: damageData,
                    animationEasing: 'cubicInOut',
                    animationDuration: 2600
                }
            ]
        }
        let damageChart = echarts.init(document.getElementById('damage')); //应用dark主题
        damageChart.setOption(damageOptions);

        // 井盖用途
        const purposeYAxis=['污水', '雨水', '人行道','侧分带','中分带','红线外绿化']
        const purposeData=[1,2,3,4,5,6]
        const purposeOptions={
            tooltip: {
                trigger: 'item',
                formatter: '{b}:{c} '
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
                name: '井盖用途',
                type: 'bar',
                data: occasionData
            }]
        }
        let purposeChart = echarts.init(document.getElementById('purpose'));
        purposeChart.setOption(purposeOptions);
    });
</script>

</body>
</html>

