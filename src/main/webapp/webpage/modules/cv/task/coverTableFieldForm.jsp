<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>井盖任务数据权限管理</title>
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
					jp.post("${ctx}/cv/task/coverTableField/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="coverTableField" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">所属任务：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/cv/task/coverTaskInfo/data" id="coverTaskInfo" name="coverTaskInfo.id" value="${coverTableField.coverTaskInfo.id}" labelName="coverTaskInfo.taskNo" labelValue="${coverTableField.coverTaskInfo.taskNo}"
							 title="选择所属任务" cssClass="form-control required" fieldLabels="任务编号|任务名称|任务数量" fieldKeys="taskNo|taskName|taskNum" searchLabels="任务编号|任务名称" searchKeys="taskNo|taskName" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">所属部门：</label></td>
					<td class="width-35">
						<sys:treeselect id="office" name="office.id" value="${coverTableField.office.id}" labelName="office.name" labelValue="${coverTableField.office.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">表名称：</label></td>
					<td class="width-35">
						<form:input path="tableName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">表中文名称：</label></td>
					<td class="width-35">
						<form:input path="tableTitle" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">字段名称：</label></td>
					<td class="width-35">
						<form:input path="fieldName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">字段中文名称：</label></td>
					<td class="width-35">
						<form:input path="fieldTitle" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">是否列表显示：</label></td>
					<td class="width-35">
						<form:select path="isListField" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('boolean')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">是否修改显示：</label></td>
					<td class="width-35">
						<form:select path="isEditField" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('boolean')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>