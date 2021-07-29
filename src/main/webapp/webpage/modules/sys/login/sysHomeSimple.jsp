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

        .charts-module > .title {
            height: 32px;
            line-height: 32px;
            font-size: 20px;
            font-weight: bold;
            margin: 15px 10px 0 0;
            padding: 0 0 0 10px;
            border-left: 4px solid #3a8fea;
            color: #333333;
        }

        .charts-module > .content {
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

        /*图表*/
        .charts-title {
            font-weight: bold;
            font-size: 16px;
            height: 34px;
            line-height: 34px;
            padding-bottom: 10px;
            color: #333333;
        }

        .charts-content-1 {
            width: 100%;
            height: 244px;
        }

        .charts-content-2 {
            float: left;
            width: 42%;
        }

        .charts-content-2 > .charts-content {
            width: 100%;
            height: 266px;
        }

        .statistics-module {
            text-align: center;
            padding: 0 10px;
            box-sizing: border-box;
        }

        .statistics-module .number {
            font-size: 36px;
            height: 40px;
            margin: 15px 0 0 0;
        }

        .statistics-module .title {
            font-size: 14px;
        }

        .statistics-module.fc-1 {
            color: #2089c9;
        }

        .statistics-module.fc-2 {
            color: #ff6667;
        }

        .statistics-module.fc-3 {
            color: #22b573;
        }

        /*页面头部*/
        .home-title {
            display: flex;
            justify-content: space-between;
            height: 40px;
            line-height: 40px;
            padding: 0 10px;
            margin-bottom: 10px;
            background-color: #3ca2e0;
            color: #ffffff;
        }

        .home-title > .left {
            margin: 0;
            padding: 0;
            line-height: 40px;
            font-size: 16px;
            font-weight: normal;
        }

        .home-title > .right {
            overflow: hidden;
        }

        .home-title > .right .content {
            float: right;
            font-size: 16px;
            line-height: 40px;
        }

        .home-title > .right .btn {
            float: right;
            height: 30px;
            line-height: 30px;
            border: none;
            font-size: 14px;
            padding: 0 10px;
            margin: 5px 0 0 10px;
            background-color: #00609a;
            color: #ffffff;
        }

        .home-title > .right .btn:hover {
            background-color: #187cb9;
        }

        .home-title > .right .btn:focus {
            outline: none;
        }

        .home-title > .right .btn:active {
            background-color: #00609a;
        }
    </style>
</head>
<body class="">
<div id="body-container">
    <%--    <div class="row margin-b number-module">--%>
    <%--        <div class="col-md-4 col-lg-4">--%>
    <%--            <div class="number-module-one">--%>
    <%--                <h3 class="number fc-1">${generalSurveyNum}/${auditedNum}</h3>--%>
    <%--                <div class="title">已普查/已审核</div>--%>
    <%--            </div>--%>
    <%--        </div>--%>
    <%--        <div class="col-md-4 col-lg-4">--%>
    <%--            <div class="number-module-one is-bc">--%>
    <%--                <h3 class="number fc-2">${alarmNum}</h3>--%>
    <%--                <div class="title">报警总数</div>--%>
    <%--            </div>--%>
    <%--        </div>--%>
    <%--        <div class="col-md-4 col-lg-4">--%>
    <%--            <div class="number-module-one">--%>
    <%--                <h3 class="number fc-3">${workOrderNum}</h3>--%>
    <%--                <div class="title">工单总数</div>--%>
    <%--            </div>--%>
    <%--        </div>--%>
    <%--    </div>--%>
    <div class="home-title">
        <h3 class="left">业务简报</h3>
        <div class="right">
            <button class="btn" onclick="btnRefresh();">
                <i class="glyphicon glyphicon-refresh"></i>
                <span>刷新</span>
            </button>
            <div class="content">刷新时间：${currentDateTime}</div>
        </div>
    </div>
    <div class="row margin-b">
        <div class="col-md-5 col-lg-5">
            <div class="charts-module">
                <h3 class="title">窨井统计</h3>
                <div class="common-box">
                    <div class="statistics-module fc-1 w-33">
                        <h3 class="number">${covercount1}</h3>
                        <div class="title">已普查</div>
                    </div>
                    <div class="statistics-module fc-1 w-33">
                        <h3 class="number">${covercount2}</h3>
                        <div class="title">未审核</div>
                    </div>
                    <div class="statistics-module fc-1 w-33">
                        <h3 class="number">${covercount3}</h3>
                        <div class="title">损毁井盖</div>
                    </div>
                </div>
                <div class="content">
                    <div class="charts-title">窨井用途</div>
                    <div id="coverChart" class="charts-content-1"></div>
                </div>
            </div>
        </div>
        <div class="col-md-4 col-lg-4">
            <div class="charts-module">
                <h3 class="title">报警统计</h3>
                <div class="statistics-module fc-2">
                    <h3 class="number">${alarmCountTotal}</h3>
                    <div class="title">累计报警</div>
                </div>
                <div class="content">
                    <div class="charts-title">报警类型</div>
                    <div id="alarmChart" class="charts-content-1"></div>
                </div>
            </div>
        </div>
        <div class="col-md-3 col-lg-3">
            <div class="charts-module">
                <h3 class="title">实时报警</h3>
                <div class="content" style="height:382px">
                    <ul class="alarms-message-list" id="ulAlarmList">
                        <%--                        <li title="【打开报警】32030220123109470005井盖--中山北路127号">--%>
                        <%--                            【水位报警】32030220123109470001井盖--中山北路127号--%>
                        <%--                        </li>--%>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12 col-lg-12">
            <div class="charts-module">
                <h3 class="title">工单统计</h3>
                <div class="content">
                    <div class="charts-content-2">
                        <div class="charts-title">工单状态</div>
                        <div id="workStatusChart" class="charts-content"></div>
                    </div>
                    <div class="charts-content-2">
                        <div class="charts-title">工单类型</div>
                        <div id="workTypeChart" class="charts-content"></div>
                    </div>
                    <div class="statistics-module fc-3" style="width: 16%; float: left; margin-top: 5%">
                        <h3 class="number">${coverWorkCountTotal}</h3>
                        <div class="title">累计工单</div>
                    </div>
                    <div class="common-clear"></div>
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
                    lineStyle: {
                        color: ['#eeeeee'],
                        width: 1,
                        type: 'solid'
                    }
                },
                axisLine: {
                    lineStyle: {
                        borderColor: '#999999'
                    }
                }
            },
            yAxis: {
                type: 'category',
                data: ['已普查', '已审核'],
                axisLine: {
                    lineStyle: {
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
        // let censusChart = echarts.init(document.getElementById('censusChart'));
        // censusChart.setOption(censusOption);

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
                    lineStyle: {
                        color: ['#eeeeee'],
                        width: 1,
                        type: 'solid'
                    }
                }
            },
            yAxis: {
                type: 'value',
                axisLine: {
                    lineStyle: {
                        borderColor: '#999999'
                    }
                },
                splitLine: {
                    show: true,
                    lineStyle: {
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
        // let alarmChart = echarts.init(document.getElementById('alarmChart'));
        // alarmChart.setOption(alarmOption);


        // ------------------------------1.井盖统计begin------------------------------
        let coverOption = {
            color: chartsColors,
            title: {
                text: '',
                subtext: '',
                left: 'left'
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
                    name: '窨井用途',
                    type: 'pie',
                    radius: ['40%', '72%'],
                    data:
                        [
                            // {name: '进行中',value: 1080},
                            // {name: '待审核',value: 735},
                            // {value: 580, name: '已完成'},
                            // {value: 484, name: '已超期'},
                            // {value: 300, name: '已取消'}
                        ]
                    ,
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
        let coverChart = echarts.init(document.getElementById('coverChart'));
        coverChart.setOption(coverOption);

        jp.get("${ctx}/cv/equinfo/cover/coverCount", function (data) {
            if (data.success) {
                //格式和饼图需要的数据格式 是一样的，所以直接赋值
                coverChart.setOption({
                    series: [{
                        data: data.data
                    }]
                });
            } else {
                jp.error(data.msg);
            }
        })
        // ------------------------------1.井盖统计end------------------------------


        // ------------------------------2.报警统计begin------------------------------
        let alarmOption = {
            color: chartsColors,
            title: {
                text: '',
                subtext: '',
                left: 'left'
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
                    name: '报警类型',
                    type: 'pie',
                    radius: ['40%', '72%'],
                    data:
                        [
                            // {name: '进行中',value: 1080},
                            // {name: '待审核',value: 735},
                            // {value: 580, name: '已完成'},
                            // {value: 484, name: '已超期'},
                            // {value: 300, name: '已取消'}
                        ]
                    ,
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
        let alarmChart = echarts.init(document.getElementById('alarmChart'));
        alarmChart.setOption(alarmOption);

        jp.get("${ctx}/cv/equinfo/cover/alarmCount", function (data) {
            if (data.success) {
                //格式和饼图需要的数据格式 是一样的，所以直接赋值
                alarmChart.setOption({
                    series: [{
                        data: data.data
                    }]
                });
            } else {
                jp.error(data.msg);
            }
        })
        // ------------------------------2.报警统计end------------------------------


        // ------------------------------3.工单状态(基于生命周期字段)统计begin------------------------------
        let workStatusOption = {
            color: chartsColors,
            title: {
                text: '',
                subtext: '',
                left: 'left'
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
                    name: '工单状态',
                    type: 'pie',
                    radius: ['40%', '72%'],
                    data:
                        [
                            // {name: '进行中',value: 1080},
                            // {name: '待审核',value: 735},
                            // {value: 580, name: '已完成'},
                            // {value: 484, name: '已超期'},
                            // {value: 300, name: '已取消'}
                        ]
                    ,
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
        let workStatusChart = echarts.init(document.getElementById('workStatusChart'));
        workStatusChart.setOption(workStatusOption);

        jp.get("${ctx}/cv/equinfo/cover/workStatusCount", function (data) {
            if (data.success) {
                //格式和饼图需要的数据格式 是一样的，所以直接赋值
                workStatusChart.setOption({
                    series: [{
                        data: data.data
                    }]
                });
            } else {
                jp.error(data.msg);
            }
        })
        // ------------------------------3.工单状态(基于生命周期字段)统计end------------------------------


        // ------------------------------4.工单类型统计begin------------------------------
        let workTypeOption = {
            color: chartsColors,
            title: {
                text: '',
                subtext: '',
                left: 'left'
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
                    name: '工单状态',
                    type: 'pie',
                    radius: ['40%', '72%'],
                    data:
                        [
                            // {name: '进行中',value: 1080},
                            // {name: '待审核',value: 735},
                            // {value: 580, name: '已完成'},
                            // {value: 484, name: '已超期'},
                            // {value: 300, name: '已取消'}
                        ]
                    ,
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
        let workTypeChart = echarts.init(document.getElementById('workTypeChart'));
        workTypeChart.setOption(workTypeOption);

        jp.get("${ctx}/cv/equinfo/cover/workTypeCount", function (data) {
            if (data.success) {
                //格式和饼图需要的数据格式 是一样的，所以直接赋值
                workTypeChart.setOption({
                    series: [{
                        data: data.data
                    }]
                });
            } else {
                jp.error(data.msg);
            }
        })
        // ------------------------------4.工单类型统计end------------------------------

        alarmList();
    });

    function btnRefresh() {
        location.reload();
    }

    function alarmList() {
        jp.get("${ctx}/cv/equinfo/cover/alarmList", function (data) {
            if (data.success) {
                let arr = data.data;
                console.log("arr",arr);
                for (let i = 0; i < 7; i++) {
                    let itemTemp = arr[i];
                    if(!itemTemp){
                        continue;
                    }
                    if(!itemTemp.coverNo){
                        continue;
                    }
                    let type = itemTemp.alarmType;
                    let t = "未知";
                    switch (type) {
                        case "waterLevel":
                            t = "水位报警";
                            break;
                        case "votage":
                            t = "电压报警";
                            break;
                        case "temperature":
                            t = "温度报警";
                            break;
                        case "open":
                            t = "井盖开合";
                            break;
                        case "vibrate":
                            t = "井盖震动";
                            break;
                        case "broken":
                            t = "井盖破损";
                            break;
                        case "offline":
                            t = "离线报警";
                            break;
                        case "pullOff":
                            t = "脱落报警";
                            break;
                    }

                    let html = "<li title=\"【" + t + "】" + itemTemp.coverNo + "井盖--" + itemTemp.address + "\">【" + t + "】" + itemTemp.coverNo + "井盖--" + itemTemp.address + "</li>";

                    $("#ulAlarmList").append(html);
                }

            } else {
                jp.error(data.msg);
            }
        })
    }
</script>

</body>
</html>
