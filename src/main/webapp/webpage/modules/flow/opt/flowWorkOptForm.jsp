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
					jp.post("${ctx}/flow/opt/flowWorkOpt/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="flowWorkOpt" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">工单：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/cb/work/coverWork/data" id="billId" name="billId.id" value="${flowWorkOpt.billId.id}" labelName="billId.work_num" labelValue="${flowWorkOpt.billId.work_num}"
							 title="选择工单" cssClass="form-control required" fieldLabels="井盖编号|工单编号|联系电话" fieldKeys="coverNo|workNum|phone" searchLabels="井盖编号|工单编号|联系电话" searchKeys="coverNo|workNum|phone" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">工单编号：</label></td>
					<td class="width-35">
						<form:input path="billNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">流程信息：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/flow/base/flowProc/data" id="flowId" name="flowId.id" value="${flowWorkOpt.flowId.id}" labelName="flowId.flowNo" labelValue="${flowWorkOpt.flowId.flowNo}"
							 title="选择流程信息" cssClass="form-control required" fieldLabels="流程编号|流程名称|版本" fieldKeys="flowNo|flowName|version" searchLabels="流程编号|流程名称|版本" searchKeys="flowNo|flowName|version" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">操作信息：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/flow/opt/flowOpt/data" id="optId" name="optId.id" value="${flowWorkOpt.optId.id}" labelName="optId.optName" labelValue="${flowWorkOpt.optId.optName}"
							 title="选择操作信息" cssClass="form-control required" fieldLabels="操作名称|操作代码" fieldKeys="optName|optCode" searchLabels="操作名称|操作代码" searchKeys="optName|optCode" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">操作名称：</label></td>
					<td class="width-35">
						<form:input path="optName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">原状态：</label></td>
					<td class="width-35">
						<form:input path="originState" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">结果状态：</label></td>
					<td class="width-35">
						<form:input path="resultState" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">操作部门：</label></td>
					<td class="width-35">
						<sys:treeselect id="optOrg" name="optOrg.id" value="${flowWorkOpt.optOrg.id}" labelName="optOrg.name" labelValue="${flowWorkOpt.optOrg.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">目标部门：</label></td>
					<td class="width-35">
						<sys:treeselect id="targetOrg" name="targetOrg.id" value="${flowWorkOpt.targetOrg.id}" labelName="targetOrg.name" labelValue="${flowWorkOpt.targetOrg.name}"
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