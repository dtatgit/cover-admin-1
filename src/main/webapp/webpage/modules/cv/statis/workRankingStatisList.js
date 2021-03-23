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
    if(rows.length){
        rows.forEach(function (row) {
            row.total = parseInt(row.untreated) + parseInt(row.processed) + parseInt(row.processing);
            console.log(row);
        });
        return rows;
    }

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
        pagination: true,
        //初始化加载第一页，默认第一页
        pageNumber:1,
        //每页的记录行数（*）
        pageSize: 10,
        //可供选择的每页的行数（*）
        pageList: [10, 25, 50, 100],
        //排序方式
        sortOrder: "asc",
        undefinedText: "-",
        //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
        url: "${ctx}/cv/statis/workRanking/dataOffice",
        showFooter: true,
        queryParams: function (params) {
            let searchParam = $("#searchForm").serializeJSON();
            console.log(searchParam);
            return searchParam;
        },
        columns: [{
            field: 'officeName',
            title: '部门',
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

        }, {
            field: 'ownerDepart',
            title: '权属单位',
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

        }, {
            field: 'workNumTotal',
            title: '工单总数（累计）',
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

        }, {
            field: 'proWorkNum',
            title: '未完成工单总数（累计）',
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
        }, {
            field: 'completeWorkNumTotal',
            title: '已完成工单总数（累计）',
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
        },
            // {
            //     field: 'completeWorkNumTotal',
            //     title: '完成率',
            //     width: '20%',
            //     cellStyle: {
            //         css: {
            //             "overflow": "hidden",
            //             "text-overflow": "ellipsis",
            //             "white-space": "nowrap"
            //         }
            //     },
            //     formatter: function (value, row, index) {
            //         return "<span title='"+ row.completeWorkNumTotal +"'>" + row.completeWorkNumTotal + "</span>";
            //     },
            // }
        ],
        onPostBody: function () {
            //合并页脚
            mergeFooter();
        },
        onLoadSuccess: function (data) {
            const responseData=data.data
            if(responseData){
              // let  tableData = formatterRow(responseData);
                // getCharts(tableData);
            }

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
