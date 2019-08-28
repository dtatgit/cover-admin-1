<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>井卫报警记录</title>
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



            $('#followTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });

            $('#coverBellAlarmTable').bootstrapTable({
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
                url: "${ctx}/cb/alarm/coverBellAlarm/data",
                //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
                //queryParamsType:'',
                ////查询参数,每次调用是会带上这个参数，可自定义
                queryParams : function(params) {
                    var searchParam = $("#searchForm").serializeJSON();
                    searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
                    searchParam.pageSize = params.limit === undefined? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
                    var coverBellId = $("#id").val();
                    searchParam.coverBellId = coverBellId;
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
                    field: 'alarmNum',
                    title: '报警编号',
                    sortable: true

                }
                    ,{
                    field: 'bellNo',
                    title: '井卫编号',
                    sortable: true

                }
                    ,{
                        field: 'coverNo',
                        title: '井盖编号',
                        sortable: true

                    }
                    ,{
                        field: 'alarmType',
                        title: '报警类型',
                        sortable: true,
                        formatter:function(value, row , index){
                            return jp.getDictLabel(${fns:toJson(fns:getDictList('alarm_type'))}, value, "-");
                        }

                    }
                    ,{
                        field: 'currentValue',
                        title: '当前值',
                        sortable: true

                    }
                    ,{
                        field: 'alarmDate',
                        title: '报警时间',
                        sortable: true

                    }

                ]

            });

        });
	</script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="coverBell" class="form-horizontal">
	<form:hidden path="id"/>
	<sys:message content="${message}"/>
	<table class="table table-bordered">
		<tbody>

		<tr>
			<td class="width-15 active"><label class="pull-right">井盖编号：</label></td>
			<td class="width-35">
					${coverBell.coverNo}
			</td>
			<td class="width-15 active"><label class="pull-right">井卫编号：</label></td>
			<td class="width-35">
					${coverBell.bellNo}
			</td>

		</tr>
		<tr>
			<td class="width-15 active"><label class="pull-right">井卫型号：</label></td>
			<td class="width-35">

					${coverBell.bellModel}
			</td>
			<td class="width-15 active"><label class="pull-right">设备IMEI号：</label></td>
			<td class="width-35">

					${coverBell.imei}
			</td>

		</tr>
		<tr>
			<td class="width-15 active"><label class="pull-right">设备SIM卡号：</label></td>
			<td class="width-35">
					${coverBell.sim}
			</td>
			<td class="width-15 active"><label class="pull-right">生命周期：</label></td>
			<td class="width-35">
					${ fns:getDictLabel (coverBell.bellStatus, "bell_status", "")}
			</td>
		</tr>
		<tr>
			<td class="width-15 active"><label class="pull-right">注册时间：</label></td>
			<td class="width-35">
				<fmt:formatDate value="${coverBell.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</td>
			<td class="width-15 active"><label class="pull-right">更新时间：</label></td>
			<td class="width-35">
				<fmt:formatDate value="${coverBell.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</td>
		</tr>

		<tr>
			<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
			<td class="width-35">
					<%--<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>--%>
					${cgAccount.remarks}
			</td>
		</tr>
		</tbody>
	</table>
	<c:if test="${coverBell.id !=null && coverBell.id !=''}">
		<table id="coverBellAlarmTable"   data-toolbar="#toolbar"></table>
	</c:if>
</form:form>
</body>
</html>