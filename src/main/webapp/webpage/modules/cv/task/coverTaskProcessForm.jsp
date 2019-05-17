<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>任务处理明细管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
		var validateForm;
		var $table; // 父页面table表格id
		var $topIndex;//弹出窗口的 index
		function doSubmit(table, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $table = table;
			  $topIndex = index;
			  jp.loading();
			  $("#inputForm").submit();
			  return true;
		  }

		  return false;
		}

		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					jp.post("${ctx}/cv/task/coverTaskProcess/save",$('#inputForm').serialize(),function(data){
						if(data.success){
	                    	$table.bootstrapTable('refresh');
	                    	jp.success(data.msg);
	                    	jp.close($topIndex);//关闭dialog

	                    }else{
            	  			jp.error(data.msg);
	                    }
					})
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
			
		});
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="coverTaskProcess" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">所属任务：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/cv/task/coverTaskInfo/data" id="coverTaskInfo" name="coverTaskInfo.id" value="${coverTaskProcess.coverTaskInfo.id}" labelName="coverTaskInfo.taskNo" labelValue="${coverTaskProcess.coverTaskInfo.taskNo}"
							 title="选择所属任务" cssClass="form-control required" fieldLabels="任务编号|任务名称|任务数量" fieldKeys="taskNo|taskName|taskNum" searchLabels="任务编号|任务名称" searchKeys="taskNo|taskName" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">任务状态：</label></td>
					<td class="width-35">
						<form:select path="taskStatus" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('task_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">井盖信息：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/cv/equinfo/cover/data" id="cover" name="cover.id" value="${coverTaskProcess.cover.id}" labelName="cover.no" labelValue="${coverTaskProcess.cover.no}"
							 title="选择井盖信息" cssClass="form-control required" fieldLabels="编号|地址" fieldKeys="no|addressDetail" searchLabels="编号" searchKeys="no" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">审核人：</label></td>
					<td class="width-35">
						<sys:userselect id="auditUser" name="auditUser.id" value="${coverTaskProcess.auditUser.id}" labelName="auditUser.name" labelValue="${coverTaskProcess.auditUser.name}"
							    cssClass="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">审核状态：</label></td>
					<td class="width-35">
						<form:select path="auditStatus" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('audit_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">审核结果：</label></td>
					<td class="width-35">
						<form:input path="auditResult" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">申请事项：</label></td>
					<td class="width-35">
						<form:select path="applyItem" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('apply_item')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>