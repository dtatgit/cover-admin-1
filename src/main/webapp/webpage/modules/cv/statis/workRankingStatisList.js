<%@ page contentType="text/html;charset=UTF-8" %>
    <script>
    $(document).ready(function() {
        tableLoad() // 初始化表格


        function tableLoad(){
            $('#officeOwnerStatisTable').bootstrapTable({
                //请求方法
                method: 'get',
                //类型json
                dataType: "json",
                //显示刷新按钮
                showRefresh: true,
                //显示切换手机试图按钮
                showToggle: true,
                //显示 内容列下拉框
                showColumns: true,
                //显示到处按钮
                showExport: true,
                //显示切换分页按钮
                showPaginationSwitch: true,
                //最低显示2行
                minimumCountColumns: 2,
                //是否显示行间隔色
                striped: true,
                //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                cache: false,
                //是否显示分页（*）
                pagination: true,
                //排序方式
                sortOrder: "asc",
                //初始化加载第一页，默认第一页
                pageNumber:1,
                //每页的记录行数（*）
                pageSize: 10,
                //可供选择的每页的行数（*）
                pageList: [10, 25, 50, 100],
                //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
                url: "${ctx}/cv/statis/workRanking/statisData",
                //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
                //queryParamsType:'',
                ////查询参数,每次调用是会带上这个参数，可自定义
                queryParams : function(params) {
                    var searchParam = $("#searchForm").serializeJSON();
                    searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
                    searchParam.pageSize = params.limit === undefined? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
                    return searchParam;
                },
                //分页方式：client客户端分页，server服务端分页（*）
                sidePagination: "server",
                contextMenuTrigger:"right",//pc端 按右键弹出菜单
                contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
                contextMenu: '#context-menu',
                onContextMenuItem: function(row, $el){
                    if($el.data("item") == "edit"){
                        edit(row.id);
                    } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该维护部门权属单位统计记录吗？', function(){
                            jp.loading();
                            jp.get("${ctx}/cv/statis/officeOwnerStatis/delete?id="+row.id, function(data){
                                if(data.success){
                                    $('#officeOwnerStatisTable').bootstrapTable('refresh');
                                    jp.success(data.msg);
                                }else{
                                    jp.error(data.msg);
                                }
                            })

                        });

                    }
                },
                onClickRow: function(row, $el){
                },
                columns: [{
                    checkbox: true

                }
                    ,{
                        field: 'officeName',
                        title: '部门',
                        sortable: true

                    }
                    ,{
                        field: 'ownerDepart',
                        title: '权属单位',
                        sortable: true
                    }

                    ,{
                        field: 'proWorkNum',
                        title: '未完成工单总数（累计）',
                        sortable: true

                    }
                    ,{
                        field: 'workNumTotal',
                        title: '工单总数（累计）',
                        sortable: true

                    }
                    ,{
                        field: 'completeWorkNumTotal',
                        title: '已完成工单总数（累计）',
                        sortable: true

                    }
                    ,{
                        field: 'completionRate',
                        title: '完成率',
                        sortable: true,
                        formatter: function (value, row, index) {
                            let newvalue=parseFloat(value).toFixed(1)
                            return "<span title='"+ newvalue +"'>" + newvalue + "</span>";
                        },

                    }
                ],
                onLoadSuccess: function (data) {
                    let searchParam = $("#searchForm").serializeJSON();
                    let statisMethods=searchParam.statisMethods
                    let hideType=statisMethods==='ownerDepart'?'officeName':"ownerDepart"
                    let showType=statisMethods==='ownerDepart'?'ownerDepart':"officeName"
                    console.log('hideType',hideType)
                    $('#officeOwnerStatisTable').bootstrapTable('hideColumn',hideType)
                    $('#officeOwnerStatisTable').bootstrapTable('showColumn',showType)
                    getCharts(data.rows);
                }

            });
        }

        if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


            $('#officeOwnerStatisTable').bootstrapTable("toggleView");
        }

        $('#officeOwnerStatisTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
            'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#officeOwnerStatisTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#officeOwnerStatisTable').bootstrapTable('getSelections').length!=1);
        });

        $("#btnImport").click(function(){
            jp.open({
                type: 1,
                area: [500, 300],
                title:"导入数据",
                content:$("#importBox").html() ,
                btn: ['下载模板','确定', '关闭'],
                btn1: function(index, layero){
                    window.location='${ctx}/cv/statis/officeOwnerStatis/import/template';
                },
                btn2: function(index, layero){
                    var inputForm =top.$("#importForm");
                    var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe
                    inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
                    inputForm.onsubmit = function(){
                        jp.loading('  正在导入，请稍等...');
                    }
                    inputForm.submit();
                    jp.close(index);
                },

                btn3: function(index){
                    jp.close(index);
                }
            });
        });

        $("#search").click("click", function() {// 绑定查询按扭
            $('#officeOwnerStatisTable').bootstrapTable('refresh');
        });

        $("#reset").click("click", function() {// 绑定重置按扭
            $("#searchForm  input").val("");
            $("#searchForm  select").val("ownerDepart");
            $("#searchForm  .select-item").html("");
            $('#officeOwnerStatisTable').bootstrapTable('refresh');
        });
        $('#beginDate').datetimepicker({
            format: "YYYY-MM-DD"
        });


    });

function getIdSelections() {
    return $.map($("#officeOwnerStatisTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll(){

    jp.confirm('确认要删除该维护部门权属单位统计记录吗？', function(){
        jp.loading();
        jp.get("${ctx}/cv/statis/officeOwnerStatis/deleteAll?ids=" + getIdSelections(), function(data){
            if(data.success){
                $('#officeOwnerStatisTable').bootstrapTable('refresh');
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })

    })
}
function add(){
    jp.openDialog('新增维护部门权属单位统计', "${ctx}/cv/statis/officeOwnerStatis/form",'800px', '500px', $('#officeOwnerStatisTable'));
}
function edit(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getIdSelections();
    }
<shiro:hasPermission name="cv:statis:officeOwnerStatis:edit">
        jp.openDialog('编辑维护部门权属单位统计', "${ctx}/cv/statis/officeOwnerStatis/form?id=" + id,'800px', '500px', $('#officeOwnerStatisTable'));
</shiro:hasPermission>
    <shiro:lacksPermission name="cv:statis:officeOwnerStatis:edit">
        jp.openDialogView('查看维护部门权属单位统计', "${ctx}/cv/statis/officeOwnerStatis/form?id=" + id,'800px', '500px', $('#officeOwnerStatisTable'));
</shiro:lacksPermission>
}

// 图表加载
function getCharts(tableData) {
    let searchParam = $("#searchForm").serializeJSON();
    let orgType=searchParam.statisMethods==='ownerDepart'?'ownerDepart':"officeName"
    let dom = document.getElementById("container");
    let myChart = echarts.init(dom);
    let option = null;
    let legendData = [];
    let chartsData = [];
    const dataCopy=tableData.slice(0,10) //取排名前10条
    dataCopy.forEach(function (row) {
        legendData.push(row[orgType]);
        let newvalue=parseFloat(row.completionRate).toFixed(1)
        chartsData.push(newvalue)
    });
    option = {
        tooltip : {
            trigger: 'axis',
            formatter: '{b} : {c} ',
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
            data: ['完成率']
        },
        //x坐标轴
        xAxis: [
            {
                name: '完成率',
                type: 'category',
                data: legendData,
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
                name: '完成率',
                axisLabel: {
                    formatter: '{value}%'
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
                name: '完成率',
                type: 'bar',
                barWidth: 45, //柱图宽度
                itemStyle:{
                    normal:{
                        color:'#69d2e7'
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