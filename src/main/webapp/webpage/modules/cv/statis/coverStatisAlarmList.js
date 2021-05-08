<%@ page contentType = "text/html;charset=UTF-8" %>
    <script>
    $(document).ready(function () {


        $('#constructionStatisticsTable').bootstrapTable({
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
            url: "${ctx}/cv/statis/coverStatis/work/tableData",
            showFooter: true,
            queryParams: function (params) {
                let searchParam = $("#searchForm").serializeJSON();
                return searchParam;
            },
            columns: [{
                field: 'district',
                title: '区域',
                width: '14%',
                footerFormatter: function (value) {
                    return "合计";
                }
            }, {
                field: 'coverNum',
                title: '井盖数',
                width: '14%',
                formatter: function (value, row, index) {
                    console.log(value);
                    return "<span title='"+ parseInt(value) +"'>" + parseInt(value) + "</span>";
                },
                footerFormatter: function (value) {
                    let count = 0;
                    for (let i in value) {
                        if (value[i].coverNum !== null) {
                            count += +value[i].coverNum;
                        }
                    }
                    return "<span title='"+ count +"'>" + count + "</span>";
                }
            }, {
                field: 'installEqu',
                title: ' 已安装设备数',
                width: '14%',
                formatter: function (value, row, index) {
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + value + "</a>";
                    return "<span title='"+ parseInt(value) +"'>" + parseInt(value) + "</span>";
                },
                footerFormatter: function (value) {
                    let count = 0;
                    for (let i in value) {
                        if (value[i].installEqu !== null) {
                            count += +value[i].installEqu;
                        }
                    }
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                    return "<span title='"+ count +"'>" + count + "</span>";
                }
            }, {
                field: 'onlineNum',
                title: '当前在线数',
                width: '14%',
                formatter: function (value, row, index) {
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + value + "</a>";
                    return "<span title='"+ parseInt(value) +"'>" + parseInt(value) + "</span>";
                },
                footerFormatter: function (value) {
                    let count = 0;
                    for (let i in value) {
                        if (value[i].onlineNum !== null) {
                            count += +value[i].onlineNum;
                        }
                    }
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                    return "<span title='"+ count +"'>" + count + "</span>";
                }
            }, {
                field: 'offlineNum',
                title: '当前离线数',
                width: '14%',
                formatter: function (value, row, index) {
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + value + "</a>";
                    return "<span title='"+ parseInt(value) +"'>" + parseInt(value) + "</span>";
                },
                footerFormatter: function (value) {
                    let count = 0;
                    for (let i in value) {
                        if (value[i].offlineNum !== null) {
                            count += +value[i].offlineNum;
                        }
                    }
                    // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                    return "<span title='"+ count +"'>" + count + "</span>";
                }
            }
                , {
                    field: 'coverAlarmNum',
                    title: '报警井盖数',
                    width: '14%',
                    formatter: function (value, row, index) {
                        // return "<a href='javascript:showList(\"" + row.id + "\")'>" + value + "</a>";
                        return "<span title='"+ parseInt(value) +"'>" + parseInt(value) + "</span>";
                    },
                    footerFormatter: function (value) {
                        let count = 0;
                        for (let i in value) {
                            if (value[i].coverAlarmNum !== null) {
                                count += +value[i].coverAlarmNum;
                            }
                        }
                        // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                        return "<span title='"+ count +"'>" + count + "</span>";
                    }
                },
                {
                    field: 'alarmTotalNum',
                    title: '报警总数',
                    width: '14%',
                    formatter: function (value, row, index) {
                        // return "<a href='javascript:showList(\"" + row.id + "\")'>" + value + "</a>";
                        return "<span title='"+ parseInt(value) +"'>" + parseInt(value) + "</span>";
                    },
                    footerFormatter: function (value) {
                        let count = 0;
                        for (let i in value) {
                            if (value[i].alarmTotalNum !== null) {
                                count += +value[i].alarmTotalNum;
                            }
                        }
                        // return "<a href='javascript:showList(\"" + row.id + "\")'>" + count + "</a>";
                        return "<span title='"+ count +"'>" + count + "</span>";
                    }
                }
            ],
            onPostBody: function () {
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
            for (let i = 0; i < footer_td.length; i++) {
                footer_td.eq(i).css('width', "20%").show();
                footer_td.eq(i).find(".fht-cell").css("width", "auto");
            }
            footer_table.css('width', bootstrap_table.width());
        }

        window.onresize = function () {
            setTimeout(function () {
                mergeFooter();
            }, 200);
        }

        $("#search").click("click", function () {// 绑定查询按扭
            $('#constructionStatisticsTable').bootstrapTable('refresh');
        });

        $("#reset").click("click", function () {// 绑定查询按扭
            $("#searchForm input").val("");
            $("#searchForm select").val("");
            $("#searchForm .select-item").html("");
            $('#constructionStatisticsTable').bootstrapTable('refresh');
        });

        $('#statisTimeId').datetimepicker({
            format: "YYYY-MM-DD"
        });
        // $('#endStatisTime').datetimepicker({
        //     format: "YYYY-MM-DD HH:mm:ss"
        // });

    });

function showList() {//工单操作记录
    // if(id == undefined){
    //     id = getIdSelections();
    // }
    // <shiro:hasPermission name="cb:work:coverWork:view">
    jp.openDialogView('列表', "http://www.baidu.com", '1000px', '75%', $('#coverWorkTable'));
    // </shiro:hasPermission>
}
</script>
