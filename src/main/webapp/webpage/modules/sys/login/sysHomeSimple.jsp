<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<%--<%
    //页面每隔30秒自动刷新一遍
    response.setHeader("refresh","30");
%>--%>
<html>
<head>
    <title>首页</title>
    <meta name="decorator" content="ani"/>
    <style type="text/css">
        #body-container {
            padding: 10px;
            box-sizing: border-box;
            margin-left: 0px !important;
            margin-top: 0px !important;
            overflow-x: hidden !important;
            transition: all 0.2s ease-in-out !important;
            height: 100% !important;
        }

        .margin-b {
            margin-bottom: 10px;
        }

        .number-module {
            margin-left: 0;
            margin-right: 0;
            border: 1px solid #eeeeee;
            background-color: #ffffff;
        }

        .number-module .col-md-4,
        .number-module .col-lg-4 {
            padding-left: 0;
            padding-right: 0;
        }

        .number-module-one {
            text-align: center;
            padding: 10px;
            box-sizing: border-box;
            margin: 10px 0;
        }

        .number-module-one.is-bc {
            border-left: 1px solid #eeeeee;
            border-right: 1px solid #eeeeee;
        }

        .number-module-one .number {
            margin: 0;
            padding: 0;
            font-size: 32px;
        }

        .number-module-one .number.fc-1 {
            color: #3a8fea;
        }

        .number-module-one .number.fc-2 {
            color: #eb623b;
        }

        .number-module-one .number.fc-3 {
            color: #20c0ba;
        }

        .number-module .title {
            font-size: 14px;
            margin-top: 5px;
            color: #333333;
        }

        .charts-module {
            box-sizing: border-box;
            border: 1px solid #eeeeee;
            background-color: #ffffff;
        }

        .charts-module .title {
            height: 22px;
            line-height: 22px;
            font-size: 15px;
            font-weight: bold;
            margin: 15px 10px 0 0;
            padding: 0 0 0 10px;
            border-left: 4px solid #3a8fea;
            color: #333333;
        }

        .charts-module .content {
            padding: 15px;
            box-sizing: border-box;
        }

        .alarms-message-list {
            margin: 0;
            padding: 0;
        }

        .alarms-message-list li {
            height: 40px;
            line-height: 40px;
            padding: 0 8px 0 6px;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden;
            word-break: break-all;
            border-radius: 4px;
            border-left: 4px solid #eb623b;
            background-color: #fff4f2;
            color: #333333;
        }

        .alarms-message-list li:not(:last-child) {
            margin-bottom: 12px;
        }

        .charts-content-1 {
            width: 100%;
            height: 250px;
            background-color: #eeeeee;
        }

        .charts-content-2 {
            width: 100%;
            height: 310px;
            background-color: #eeeeee;
        }
    </style>
</head>
<body class="">
<div id="body-container">
    <div class="row margin-b number-module">
        <div class="col-md-4 col-lg-4">
            <div class="number-module-one">
                <h3 class="number fc-1">${generalSurveyNum}/${auditedNum}</h3>
                <div class="title">已普查/已审核</div>
            </div>
        </div>
        <div class="col-md-4 col-lg-4">
            <div class="number-module-one is-bc">
                <h3 class="number fc-2">${alarmNum}</h3>
                <div class="title">报警总数</div>
            </div>
        </div>
        <div class="col-md-4 col-lg-4">
            <div class="number-module-one">
                <h3 class="number fc-3">${workOrderNum}</h3>
                <div class="title">工单总数</div>
            </div>
        </div>
    </div>

    <div class="row margin-b">
        <div class="col-md-4 col-lg-4">
            <div class="charts-module">
                <h3 class="title">普查统计</h3>
                <div class="content">
                    <div id="censusChart" class="charts-content-1"></div>
                </div>
            </div>
        </div>
        <div class="col-md-4 col-lg-4">
            <div class="charts-module">
                <h3 class="title">报警统计</h3>
                <div class="content">
                    <div id="alarmsChart" class="charts-content-1"></div>
                </div>
            </div>
        </div>
        <div class="col-md-4 col-lg-4">
            <div class="charts-module">
                <h3 class="title">实时报警</h3>
                <div class="content">
                    <ul class="alarms-message-list">
                        <li title="【打开报警】32030220123109470005井盖--中山东路">
                            【打开报警】32030220123109470005井盖--中山东路
                        </li>
                        <li title="【打开报警】32030220123109470005井盖--中山北路127号">
                            【离线报警】32030220123109470004井盖--中山北路127号
                        </li>
                        <li title="【打开报警】32030220123109470005井盖--中山东路">
                            【打开报警】32030220123109470003井盖--中山东路
                        </li>
                        <li title="【打开报警】32030220123109470005井盖--中山北路127号">
                            【震动报警】32030220123109470002井盖--中山北路127号
                        </li>
                        <li title="【打开报警】32030220123109470005井盖--中山北路127号">
                            【水位报警】32030220123109470001井盖--中山北路127号
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-4 col-lg-4">
            <div class="charts-module">
                <h3 class="title">报警工单统计</h3>
                <div class="content">
                    <div id="alarmsWorkOrderChart" class="charts-content-2"></div>
                </div>
            </div>
        </div>
        <div class="col-md-4 col-lg-4">
            <div class="charts-module">
                <h3 class="title">安装工单统计</h3>
                <div class="content">
                    <div id="installWorkOrderChart" class="charts-content-2"></div>
                </div>
            </div>
        </div>
        <div class="col-md-4 col-lg-4">
            <div class="charts-module">
                <h3 class="title">维护工单统计</h3>
                <div class="content">
                    <div id="maintainWorkOrderChart" class="charts-content-2"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="vendor/ckeditor/ckeditor.js" type="text/javascript"></script>
<script src="js/vendor.js"></script>
<script src="${ctxStatic}/plugin/echarts3/echarts.min.js"></script>
<script>
    $(function () {
        $('#calendar2').fullCalendar({
            eventClick: function (calEvent, jsEvent, view) {
                alert('Event: ' + calEvent.title);
                alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
                alert('View: ' + view.name);
            }
        });

        $('#rtlswitch').click(function () {
            console.log('hello');
            $('body').toggleClass('rtl');

            var hasClass = $('body').hasClass('rtl');

            $.get('/api/set-rtl?rtl=' + (hasClass ? 'rtl' : ''));

        });
        $('.theme-picker').click(function () {
            changeTheme($(this).attr('data-theme'));
        });
        $('#showMenu').click(function () {
            $('body').toggleClass('push-right');
        });

    });

    function changeTheme(the) {
        $("#current-theme").remove();
        $('<link>')
            .appendTo('head')
            .attr('id', 'current-theme')
            .attr({type: 'text/css', rel: 'stylesheet'})
            .attr('href', '/css/app-' + the + '.css');
    }
</script>
<script>
    $(function () {
        let chartsColors = ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc'];
        // 普查统计
        let censusOption = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                top: 0,
                bottom: 20,
                left: 50,
                right: 20
            },
            xAxis: {
                type: 'value',
                splitLine: {
                    show: true,
                    lineStyle:{
                        color: ['#eeeeee'],
                        width: 1,
                        type: 'solid'
                    }
                },
                axisLine:{
                    lineStyle:{
                        borderColor: '#999999'
                    }
                }
            },
            yAxis: {
                type: 'category',
                data: ['已普查', '已审核'],
                axisLine:{
                    lineStyle:{
                        borderColor: '#999999'
                    }
                }
            },
            series: [{
                type: 'bar',
                barWidth: 40,
                data: [{
                    value: 166,
                    itemStyle: {
                        normal: {
                            color: '#5470c6'
                        }
                    },
                    label: {
                        show: true,
                        position: 'inside'
                    }
                }, {
                    value: 50,
                    itemStyle: {
                        normal: {
                            color: '#91cc75'
                        }
                    }
                }]
            }]
        };
        let censusChart = echarts.init(document.getElementById('censusChart'));
        censusChart.setOption(censusOption);

        // 报警统计
        let alarmsOption = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                top: 10,
                bottom: 50,
                left: 50,
                right: 20
            },
            xAxis: {
                type: 'category',
                data: ['水位报警', '电压报警', '温度报警', '井盖开合', '井盖震动', '井盖破损', '离线报警', '脱落报警'],
                axisLabel: {
                    interval: 0,
                    rotate: 45,
                    lineStyle:{
                        color: ['#eeeeee'],
                        width: 1,
                        type: 'solid'
                    }
                }
            },
            yAxis: {
                type: 'value',
                axisLine:{
                    lineStyle:{
                        borderColor: '#999999'
                    }
                },
                splitLine: {
                    show: true,
                    lineStyle:{
                        color: ['#eeeeee'],
                        width: 1,
                        type: 'solid'
                    }
                }
            },
            series: [{
                type: 'bar',
                barWidth: 30,
                data: [{
                    value: 36
                }, {
                    value: 120
                }, {
                    value: 126
                }, {
                    value: 80
                }, {
                    value: 106
                }, {
                    value: 120
                }, {
                    value: 206
                }, {
                    value: 100
                }],
                itemStyle: {
                    normal: {
                        color: function (params) {
                            return chartsColors[params.dataIndex];
                        }
                    }
                }
            }]
        };
        let alarmsChart = echarts.init(document.getElementById('alarmsChart'));
        alarmsChart.setOption(alarmsOption);

        // 报警工单统计
        let alarmsWorkOrderOption = {
            color: chartsColors,
            title: {
                text: '',
                subtext: '',
                left: 'center'
            },
            tooltip: {
                trigger: 'item'
            },
            legend: {
                orient: 'vertical',
                left: 'left',
            },
            series: [
                {
                    name: '处理状态',
                    type: 'pie',
                    radius: '72%',
                    data: [
                        {value: 1048, name: '进行中'},
                        {value: 735, name: '待审核'},
                        {value: 580, name: '已完成'},
                        {value: 484, name: '已超期'},
                        {value: 300, name: '已取消'}
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        let alarmsWorkOrderChart = echarts.init(document.getElementById('alarmsWorkOrderChart'));
        alarmsWorkOrderChart.setOption(alarmsWorkOrderOption);

        // 安装工单统计
        let installWorkOrderOption = {
            color: chartsColors,
            title: {
                text: '',
                subtext: '',
                left: 'center'
            },
            tooltip: {
                trigger: 'item'
            },
            legend: {
                orient: 'vertical',
                left: 'left',
            },
            series: [
                {
                    name: '处理状态',
                    type: 'pie',
                    radius: '72%',
                    data: [
                        {value: 484, name: '进行中'},
                        {value: 735, name: '待审核'},
                        {value: 580, name: '已完成'},
                        {value: 1048, name: '已超期'},
                        {value: 300, name: '已取消'}
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        let installWorkOrderChart = echarts.init(document.getElementById('installWorkOrderChart'));
        installWorkOrderChart.setOption(installWorkOrderOption);

        // 维护工单统计
        let maintainWorkOrderOption = {
            color: chartsColors,
            title: {
                text: '',
                subtext: '',
                left: 'center'
            },
            tooltip: {
                trigger: 'item'
            },
            legend: {
                orient: 'vertical',
                left: 'left',
            },
            series: [
                {
                    name: '处理状态',
                    type: 'pie',
                    radius: '72%',
                    data: [
                        {value: 1048, name: '进行中'},
                        {value: 300, name: '待审核'},
                        {value: 580, name: '已完成'},
                        {value: 484, name: '已超期'},
                        {value: 735, name: '已取消'}
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        let maintainWorkOrderChart = echarts.init(document.getElementById('maintainWorkOrderChart'));
        maintainWorkOrderChart.setOption(maintainWorkOrderOption);
    });
</script>

</body>
</html>