<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工单操作记录管理</title>
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
					jp.post("${ctx}/cb/work/coverWorkOperation/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="coverWorkOperation" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">工单信息：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/cb/work/coverWork/data" id="coverWork" name="coverWork.id" value="${coverWorkOperation.coverWork.id}" labelName="coverWork.workNum" labelValue="${coverWorkOperation.coverWork.workNum}"
							 title="选择工单信息" cssClass="form-control required" fieldLabels="工单编号|井盖编号|创建时间" fieldKeys="workNum|coverNo|createDate" searchLabels="工单编号" searchKeys="workNum" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">操作类型：</label></td>
					<td class="width-35">
						<form:select path="operationType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('work_operation_Type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">操作状态：</label></td>
					<td class="width-35">
						<form:input path="operationStatus" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">操作结果：</label></td>
					<td class="width-35">
						<form:input path="operationResult" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">操作部门：</label></td>
					<td class="width-35">
						<sys:treeselect id="createDepart" name="createDepart.id" value="${coverWorkOperation.createDepart.id}" labelName="createDepart.name" labelValue="${coverWorkOperation.createDepart.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>