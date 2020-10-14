<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
    let data = [{
        id: '1',
        alarmType: '水位报警',
        untreated: 10,
        processed: 5,
        processing: 3
    }, {
        id: '2',
        alarmType: '电压报警',
        untreated: 10,
        processed: 5,
        processing: 3
    }, {
        id: '3',
        alarmType: '温度报警',
        untreated: 10,
        processed: 5,
        processing: 3
    }, {
        id: '4',
        alarmType: '井盖开合',
        untreated: 15,
        processed: 5,
        processing: 3
    }, {
        id: '5',
        alarmType: '井盖震动',
        untreated: 10,
        processed: 5,
        processing: 3
    }, {
        id: '6',
        alarmType: '井盖破损',
        untreated: 10,
        processed: 5,
        processing: 3
    }, {
        id: '7',
        alarmType: '离线报警',
        untreated: 20,
        processed: 5,
        processing: 3
    }, {
        id: '1',
        alarmType: '人工上报',
        untreated: 12,
        processed: 5,
        processing: 3
    }];

    let tableData = formatterRow(data);

    function formatterRow(rows) {
        rows.forEach(function (row) {
            row.total = row.untreated + row.processed + row.processing;
        });
        return rows;
    }

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
        //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
        // url: "${ctx}/cb/equinfo/coverBellOperation/data",
        data: tableData,
        showFooter: true,
        columns: [{
            field: 'alarmType',
            title: '报警类型',
            width: '20%',
            footerFormatter: function (value) {
                return "合计";
            }
        }, {
            field: 'untreated',
            title: '未处理',
            width: '20%',
            formatter: function (value, row, index) {
                // return "<a href='javascript:showList(\"" + row.id + "\")'>" + value + "</a>";
                return "<a href='javascript:showList()'>" + value + "</a>";
            },
            footerFormatter: function (value) {
                let count = 0;
                for (let i in value) {
                    if (value[i].untreated != null) {
                        count += value[i].untreated;
                    }
                }
                // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                return "<a href='javascript:showList()'>" + count + "</a>";
            }
        }, {
            field: 'processed',
            title: '已处理',
            width: '20%',
            formatter: function (value, row, index) {
                // return "<a href='javascript:showList(\"" + row.id + "\")'>" + value + "</a>";
                return "<a href='javascript:showList()'>" + value + "</a>";
            },
            footerFormatter: function (value) {
                let count = 0;
                for (let i in value) {
                    if (value[i].processed != null) {
                        count += value[i].processed;
                    }
                }
                // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                return "<a href='javascript:showList()'>" + count + "</a>";
            }
        }, {
            field: 'processing',
            title: '处理中',
            width: '20%',
            formatter: function (value, row, index) {
                // return "<a href='javascript:showList(\"" + row.id + "\")'>" + value + "</a>";
                return "<a href='javascript:showList()'>" + value + "</a>";
            },
            footerFormatter: function (value) {
                let count = 0;
                for (let i in value) {
                    if (value[i].processing != null) {
                        count += value[i].processing;
                    }
                }
                // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                return "<a href='javascript:showList()'>" + count + "</a>";
            }
        }, {
            field: 'total',
            title: '合计',
            width: '20%',
            formatter: function (value, row, index) {
                let total = row.processed + row.processing + row.untreated;
                // return "<a href='javascript:showList(\"" + row.id + "\")'>" + total + "</a>";
                return "<a href='javascript:showList()'>" + total + "</a>";
            },
            footerFormatter: function (value) {
                let count = 0;
                for (let i in value) {
                    if (value[i].total != null) {
                        count += value[i].total;
                    }
                }
                // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                return "<a href='javascript:showList()'>" + count + "</a>";
            }
        }],
        onPostBody:function () {
            //合并页脚
            mergeFooter();
        }
    });

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

    $("#search").click("click", function() {// 绑定查询按扭
        $('#alarmInfoStatisticsTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function() {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#alarmInfoStatisticsTable').bootstrapTable('refresh');
    });

    $('#beginDateAlarmTime').datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    });
    $('#endDateAlarmTime').datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    });

    let dom = document.getElementById("container");
    let myChart = echarts.init(dom);
    let option = null;
    let legendData = [];
    let chartsData = [];
    tableData.forEach(function(row) {
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
        setTimeout(function (){
            window.onresize = function () {
                myChart.resize();
                mergeFooter();
            }
        },200);
    }
})
function showList(){//工单操作记录
    // if(id == undefined){
    //     id = getIdSelections();
    // }
    // <shiro:hasPermission name="cb:work:coverWork:view">
    jp.openDialogView('列表', "http://www.baidu.com",'1000px', '75%', $('#coverWorkTable'));
    // </shiro:hasPermission>
}
</script>
