<%@ page contentType="text/html;charset=UTF-8" %>
<script>
    let tableData = null;
    $(document).ready(function() {
        tableLoad(); //列表加载
        chartsLoad();    //图表加载

        $("#search").click("click", function() {// 绑定查询按扭
            chartsLoad();
            $('#workOrderStatisticsTable').bootstrapTable('refresh');
        });

        $("#reset").click("click", function() {// 绑定查询按扭
            $("#searchForm input").val("");
            $("#searchForm select").val("");
            $("#searchForm .select-item").html("");
            $('#workOrderStatisticsTable').bootstrapTable('refresh');
        });

        $('#beginDate').datetimepicker({
            format: "YYYY-MM-DD HH:mm:ss"
        });
        $('#endDate').datetimepicker({
            format: "YYYY-MM-DD HH:mm:ss"
        });
    });

    function showList(){//工单操作记录
        // if(id == undefined){
        //     id = getIdSelections();
        // }
        // <shiro:hasPermission name="cb:work:coverWork:view">
        jp.openDialogView('列表', "http://www.baidu.com",'1000px', '75%', $('#coverWorkTable'));
        // </shiro:hasPermission>
    }


    function formatterRow(rows) {
        rows.forEach(function(row) {
            row.total = row.normal + row.urgent + row.extra;
            row.data = [row.normal, row.urgent, row.extra];
        });
        return rows;
    }

    // 列表加载
    function tableLoad(){
        $('#workOrderStatisticsTable').bootstrapTable({
            //请求方法
            method: 'get',
            //类型json
            dataType: "json",
            height: 450,
            //是否显示行间隔色
            striped: true,
            //是否显示分页（*）
            pagination: false,
            //排序方式
            sortOrder: "asc",
            //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
            url: "${ctx}/cv/statis/workOrderStatistics/tableData",
            showFooter: true,
            queryParams: function (params) {
                let searchParam = $("#searchForm").serializeJSON();
                return searchParam;
            },
            columns: [{
                field: 'workOrderType',
                title: '报警类型',
                width: '20%',
                cellStyle: {
                    css: {
                        "overflow": "hidden",
                        "text-overflow": "ellipsis",
                        "white-space": "nowrap"
                    }
                },
                footerFormatter: function(value) {
                    return "合计";
                }
            }, {
                field: 'normal',
                title: '未处理',
                width: '20%',
                cellStyle: {
                    css: {
                        "overflow": "hidden",
                        "text-overflow": "ellipsis",
                        "white-space": "nowrap"
                    }
                },
                formatter: function(value, row, index) {
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + value + "</a>";
                    return "<span title='"+ value +"'>" + value + "</span>";
                },
                footerFormatter: function(value) {
                    let count = 0;
                    for (let i in value) {
                        if (value[i].normal != null) {
                            count += value[i].normal;
                        }
                    }
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                    return "<span title='"+ count +"'>" + count + "</span>";
                }
            }, {
                field: 'urgent',
                title: '已处理',
                width: '20%',
                cellStyle: {
                    css: {
                        "overflow": "hidden",
                        "text-overflow": "ellipsis",
                        "white-space": "nowrap"
                    }
                },
                formatter: function(value, row, index) {
                    // return "<a href='javascript:list(\"" + row.id + "\")'>" + value + "</a>";
                    return "<span title='"+ value +"'>" + value + "</span>";
                },
                footerFormatter: function(value) {
                    let count = 0;
                    for (let i in value) {
                        if (value[i].urgent != null) {
                            count += value[i].urgent;
                        }
                    }
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                    return "<span title='"+ count +"'>" + count + "</span>";
                }
            }, {
                field: 'extra',
                title: '处理中',
                width: '20%',
                cellStyle: {
                    css: {
                        "overflow": "hidden",
                        "text-overflow": "ellipsis",
                        "white-space": "nowrap"
                    }
                },
                formatter: function(value, row, index) {
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + value + "</a>";
                    return "<span title='"+ value +"'>" + value + "</span>";
                },
                footerFormatter: function(value) {
                    let count = 0;
                    for (let i in value) {
                        if (value[i].extra != null) {
                            count += value[i].extra;
                        }
                    }
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                    return "<span title='"+ count +"'>" + count + "</span>";
                }
            }, {
                field: 'total',
                title: '合计',
                width: '20%',
                cellStyle: {
                    css: {
                        "overflow": "hidden",
                        "text-overflow": "ellipsis",
                        "white-space": "nowrap"
                    }
                },
                formatter: function(value, row, index) {
                    let total = row.total = row.urgent + row.extra + row.normal;
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + total + "</a>";
                    return "<span title='"+ total +"'>" + total + "</span>";
                },
                footerFormatter: function(value) {
                    let count = 0;
                    for (let i in value) {
                        if (value[i].total != null) {
                            count += value[i].total;
                        }
                    }
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                    return "<span title='"+ count +"'>" + count + "</span>";
                }
            }],
            onPostBody:function () {
                //合并页脚
                mergeFooter();
            }
        });
    }

    //合并页脚
    function mergeFooter() {
        //获取table表中footer 并获取到这一行的所有列
        let footer_tbody = $('.fixed-table-footer table tbody');
        let footer_table = $('.fixed-table-footer table');
        let bootstrap_table = $('.fixed-table-footer');
        let footer_tr = footer_tbody.find('>tr');
        let footer_td = footer_tr.find('>td');
        for(let i=0; i < footer_td.length; i++) {
            footer_td.eq(i).css('width', "20%").show();
            footer_td.eq(i).find(".fht-cell").css("width","auto");
        }
        footer_table.css('width', bootstrap_table.width());
    }

    // 图表加载
    function chartsLoad() {
        let searchParam = $("#searchForm").serializeJSON();
        $.ajax({
            url: "${ctx}/cv/statis/workOrderStatistics/lineData",
            type: 'GET',
            async: false,
            data: searchParam,
            beforeSend: function () {
                console.log("正在进行，请稍候");
            },
            success: function (res) {
                if (res.success) {
                    getCharts(res.data);
                }
            },
            error: function () {
                jp.error('系统正忙，请稍后重试!');
            }
        });
    }

    // 图表显示
    function getCharts(data) {
        let dom = document.getElementById("container");
        let myChart = echarts.init(dom);
        let option = null;
        let legendData = data.workLevels;
        let xAxisData = data.workTypes;
        let seriesData = [];
        data.workLevels.forEach(function(row){
            let workLevelData = [];
            switch (row) {
                case '常规':
                    workLevelData = data.normal;
                    break;
                case '紧急':
                    workLevelData = data.urgent;
                    break;
                case '特急':
                    workLevelData = data.extra;
                    break;
                default:
                    workLevelData = [];
                    break;
            }
            let seriesRow = {
                name: row,
                type: 'line',
                stack: '报警',
                data: workLevelData
            }
            seriesData.push(seriesRow);
        });

        option = {
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['常规', '紧急', '特急']
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: xAxisData
            },
            yAxis: {
                type: 'value'
            },
            series: seriesData
        };
        if (option && typeof option === "object") {
            myChart.setOption(option, true);
            setTimeout(function (){
                window.onresize = function () {
                    myChart.resize();
                    mergeFooter();
                }
            },200);
        }
    }
</script>
