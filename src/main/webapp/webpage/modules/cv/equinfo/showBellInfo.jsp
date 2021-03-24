<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>井卫信息</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/treeview.jsp" %>
    <%@ include file="/webpage/include/bootstraptable.jsp"%>

    <script type="text/javascript">
        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            if(validateForm.form()){
                $table = table;
                $topIndex = index;
                jp.close($topIndex);//关闭dialog
                /*jp.loading();
                $("#inputForm").submit();
                return true;*/
                return false;
            }

            return false;
        }

        $(document).ready(function() {
            validateForm = $("#inputForm").validate({
                submitHandler: function(form){
                    /*    jp.post("${ctx}/cg/baseinfo/cgAccount/save",$('#inputForm').serialize(),function(data){
                        if(data.success){
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog

                        }else{
                            jp.error(data.msg);
                        }
                    })*/
                },
                errorContainer: "#messageBox",
                errorPlacement: function(error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });




            $('#coverBellTable').bootstrapTable({
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
                showExport: false,
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
                url: "${ctx}/cb/equinfo/coverBell/data",
                //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
                //queryParamsType:'',
                ////查询参数,每次调用是会带上这个参数，可自定义
                queryParams : function(params) {
                    var searchParam = $("#searchForm").serializeJSON();
                    searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
                    searchParam.pageSize = params.limit === undefined? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
                    var coverId = $("#id").val();
                    searchParam.coverId = coverId;
                    return searchParam;
                },
                //分页方式：client客户端分页，server服务端分页（*）
                sidePagination: "server",
                contextMenuTrigger:"right",//pc端 按右键弹出菜单
                contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
                contextMenu: '#context-menu',
                onContextMenuItem: function(row, $el){

                },

                onClickRow: function(row, $el){
                },
                columns: [{
                    field: 'bellNo',
                    title: '井卫编号',
                    sortable: true
                    ,formatter:function(value, row , index){
                        return "<a href='javascript:operationInfo(\""+row.id+"\")'>"+value+"</a>";
                    }

                }
                    ,{
                        field: 'bellModel',
                        title: '井卫型号',
                        sortable: true

                    }			,{
                        field: 'bellType',
                        title: '设备类型',
                        sortable: true,
                        formatter:function(value, row , index){
                            return jp.getDictLabel(${fns:toJson(fns:getDictList('bellType'))}, value, "-");
                        }

                    }			,{
                        field: 'version',
                        title: '固件版本号',
                        sortable: true

                    }
                    ,{
                        field: 'imei',
                        title: '设备IMEI号',
                        sortable: true

                    }
                    ,{
                        field: 'sim',
                        title: '设备SIM卡号',
                        sortable: true

                    }
                    ,{
                        field: 'bellStatus',
                        title: '生命周期',
                        sortable: true,
                        formatter:function(value, row , index){
                            return jp.getDictLabel(${fns:toJson(fns:getDictList('bell_status'))}, value, "-");
                        }

                    }
                    ,{
                        field: 'workStatus',
                        title: '工作状态',
                        sortable: true,
                        formatter:function(value, row , index){
                            return jp.getDictLabel(${fns:toJson(fns:getDictList('bell_work_status'))}, value, "-");
                        }

                    }
                    ,{
                        field: 'defenseStatus',
                        title: '设防状态',
                        sortable: true,
                        formatter:function(value, row , index){
                            return jp.getDictLabel(${fns:toJson(fns:getDictList('defense_status'))}, value, "-");
                        }

                    }
                    ,{
                        field: 'createDate',
                        title: '创建时间',
                        sortable: true

                    }

                ]

            });

        });

        function operationInfo(id){//井铃操作记录
            if(id == undefined){
                id = getIdSelections();
            }
            <shiro:hasPermission name="cv:equinfo:cover:bell">
            jp.openDialogView('查看操作记录', "${ctx}/cb/equinfo/coverBell/operationList?id=" + id,'800px', '500px', $('#coverBellTable'));
            </shiro:hasPermission>
        }
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="cover" class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>

        <tr>
            <td class="width-15 active"><label class="pull-right">井盖编号：</label></td>
            <td class="width-35">
                    ${cover.no}
            </td>
            <td class="width-15 active"><label class="pull-right">井盖状态：</label></td>
            <td class="width-35">
                    ${fns:getDictLabel (cover.coverStatus, "cover_status", "--")}
            </td>

        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">省份：</label></td>
            <td class="width-35">
                    ${cover.province}
            </td>
            <td class="width-15 active"><label class="pull-right">市：</label></td>
            <td class="width-35">
                    ${cover.city}
            </td>

        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">区：</label></td>
            <td class="width-35">
                    ${cover.district}
            </td>
            <td class="width-15 active"><label class="pull-right">街道（办事处）：</label></td>
            <td class="width-35">
                    ${cover.township}
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">路（街巷）：</label></td>
            <td class="width-35">
                    ${cover.street}
            </td>
            <td class="width-15 active"><label class="pull-right">门牌号：</label></td>
            <td class="width-35">
                    ${cover.streetNumber}
            </td>
        </tr>

        <tr>
            <td class="width-15 active"><label class="pull-right">详细地址：</label></td>
            <td class="width-35">
                    ${cover.addressDetail}
            </td>
            <td class="width-15 active"><label class="pull-right">井盖类型：</label></td>
            <td class="width-35">
                    ${cover.coverType}
            </td>
        </tr>

        <tr>
            <td class="width-15 active"><label class="pull-right">管网用途：</label></td>
            <td class="width-35">
                    ${cover.purpose}
            </td>
            <td class="width-15 active"><label class="pull-right">井位地理场合：</label></td>
            <td class="width-35">
                    ${cover.situation}
            </td>
        </tr>

        <tr>
            <td class="width-15 active"><label class="pull-right">井盖材质：</label></td>
            <td class="width-35">
                    ${cover.material}
            </td>
            <td class="width-15 active"><label class="pull-right">采集时间：</label></td>
            <td class="width-35">
                <fmt:formatDate value="${cover.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
        </tr>

        </tbody>
    </table>
    <c:if test="${cover.id !=null && cover.id !=''}">
        <table id="coverBellTable"   data-toolbar="#toolbar"></table>
    </c:if>
</form:form>
</body>
</html>