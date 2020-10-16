<%@ page contentType="text/html;charset=UTF-8" %>
<script>
    let tableData = null;
    $(document).ready(function () {
        getTable();

        $("#search").click("click", function () {// 绑定查询按扭
            $('#alarmInfoStatisticsTable').bootstrapTable('refresh');
        });

        $("#reset").click("click", function () {// 绑定查询按扭
            $("#searchForm input").val("");
            $("#searchForm select").val("");
            $("#searchForm .select-item").html("");
            $('#alarmInfoStatisticsTable').bootstrapTable('refresh');
        });

        $('#beginDate').datetimepicker({
            format: "YYYY-MM-DD HH:mm:ss"
        });
        $('#endDate').datetimepicker({
            format: "YYYY-MM-DD HH:mm:ss"
        });
    })

    function showList() {//工单操作记录
        // if(id == undefined){
        //     id = getIdSelections();
        // }
        // <shiro:hasPermission name="cb:work:coverWork:view">
        jp.openDialogView('列表', "http://www.baidu.com", '1000px', '75%', $('#coverWorkTable'));
        // </shiro:hasPermission>
    }

    // 格式化数据
    function formatterRow(rows) {
        rows.forEach(function (row) {
            row.total = row.untreated + row.processed + row.processing;
        });
        return rows;
    }

    // 列表加载
    function getTable() {
        $('#alarmInfoStatisticsTable').bootstrapTable({
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
            undefinedText: "-",
            //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
            url: "${ctx}/cv/statis/alarmInfoStatistics/statisticData",
            showFooter: true,
            queryParams: function (params) {
                let searchParam = $("#searchForm").serializeJSON();
                console.log(searchParam);
                return searchParam;
            },
            columns: [{
                field: 'alarmType',
                title: '报警类型',
                width: '20%',
                cellStyle: {
                    css: {
                        "overflow": "hidden",
                        "text-overflow": "ellipsis",
                        "white-space": "nowrap"
                    }
                },
                formatter: function (value, row, index) {
                    return "<span title='"+ value +"'>" + value + "</span>";
                },
                footerFormatter: function (value) {
                    return "合计";
                }
            }, {
                field: 'untreated',
                title: '未处理',
                width: '20%',
                cellStyle: {
                    css: {
                        "overflow": "hidden",
                        "text-overflow": "ellipsis",
                        "white-space": "nowrap"
                    }
                },
                formatter: function (value, row, index) {
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + value + "</a>";
                    return "<span title='"+ value +"'>" + value + "</span>";
                },
                footerFormatter: function (value) {
                    let count = 0;
                    for (let i in value) {
                        if (value[i].untreated != null) {
                            count += value[i].untreated;
                        }
                    }
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                    return "<span title='"+ count +"'>" + count + "</span>";
                }
            }, {
                field: 'processed',
                title: '已处理',
                width: '20%',
                cellStyle: {
                    css: {
                        "overflow": "hidden",
                        "text-overflow": "ellipsis",
                        "white-space": "nowrap"
                    }
                },
                formatter: function (value, row, index) {
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + value + "</a>";
                    return "<span title='"+ value +"'>" + value + "</span>";
                },
                footerFormatter: function (value) {
                    let count = 0;
                    for (let i in value) {
                        if (value[i].processed != null) {
                            count += value[i].processed;
                        }
                    }
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                    return "<span title='"+ count +"'>" + count + "</span>";
                }
            }, {
                field: 'processing',
                title: '处理中',
                width: '20%',
                cellStyle: {
                    css: {
                        "overflow": "hidden",
                        "text-overflow": "ellipsis",
                        "white-space": "nowrap"
                    }
                },
                formatter: function (value, row, index) {
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + value + "</a>";
                    return "<span title='"+ value +"'>" + value + "</span>";
                },
                footerFormatter: function (value) {
                    let count = 0;
                    for (let i in value) {
                        if (value[i].processing != null) {
                            count += value[i].processing;
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
                formatter: function (value, row, index) {
                    let total = row.processed + row.processing + row.untreated;
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + total + "</a>";
                    return "<div>" + total + "</div>";
                },
                footerFormatter: function (value) {
                    console.log(value);
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
            onPostBody: function () {
                //合并页脚
                mergeFooter();
            },
            onLoadSuccess: function (data) {
                tableData = formatterRow(data.data);
                getCharts(tableData);
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
        for (let i = 0; i < footer_td.length; i++) {
            footer_td.eq(i).css('width', "20%").show();
            footer_td.eq(i).find(".fht-cell").css("width", "auto");
        }
        footer_table.css('width', bootstrap_table.width());
    }

    // 图表加载
    function getCharts(tableData) {
        let dom = document.getElementById("container");
        let myChart = echarts.init(dom);
        let option = null;
        let legendData = [];
        let chartsData = [];
        tableData.forEach(function (row) {
            legendData.push(row.alarmType);
            chartsData.push({
                value: row.total,
                name: row.alarmType
            })
        });
        option = {
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
                orient: 'vertical',
                left: 10,
                top: 10,
                data: legendData
            },
            series: [{
                name: '报警分类',
                type: 'pie',
                selectedMode: 'single',
                radius: [0, '30%'],

                label: {
                    position: 'inner'
                },
                labelLine: {
                    show: false
                }
            },
                {
                    name: '报警分类',
                    type: 'pie',
                    radius: ['40%', '55%'],
                    label: {
                        formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
                        backgroundColor: '#eee',
                        borderColor: '#aaa',
                        borderWidth: 1,
                        borderRadius: 4,
                        rich: {
                            a: {
                                color: '#999',
                                lineHeight: 22,
                                align: 'center'
                            },
                            hr: {
                                borderColor: '#aaa',
                                width: '100%',
                                borderWidth: 0.5,
                                height: 0
                            },
                            b: {
                                fontSize: 16,
                                lineHeight: 33
                            },
                            per: {
                                color: '#eee',
                                backgroundColor: '#334455',
                                padding: [2, 4],
                                borderRadius: 2
                            }
                        }
                    },
                    data: chartsData
                }
            ]
        };
        if (option && typeof option === "object") {
            myChart.setOption(option, true);
            setTimeout(function () {
                window.onresize = function () {
                    myChart.resize();
                    mergeFooter();
                }
            }, 200);
        }
    }
</script>
