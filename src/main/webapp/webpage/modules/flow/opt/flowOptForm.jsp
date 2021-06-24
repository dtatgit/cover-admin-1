<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工单流程操作定义管理</title>
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
					jp.post("${ctx}/flow/opt/flowOpt/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="flowOpt" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">流程：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/flow/base/flowProc/data" id="flowId" name="flowId.id" value="${flowOpt.flowId.id}" labelName="flowId.flowNo" labelValue="${flowOpt.flowId.flowNo}"
							 title="选择流程" cssClass="form-control required" fieldLabels="流程编号|流程名称" fieldKeys="flowNo|flowName" searchLabels="流程编号|流程名称" searchKeys="flowNo|flowName" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">适用状态：</label></td>
					<td class="width-35">
						<form:input path="fromState" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">顺序：</label></td>
					<td class="width-35">
						<form:input path="optOrder" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">操作类型：</label></td>
					<td class="width-35">
						<form:select path="optType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('flow_opt_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">操作名称：</label></td>
					<td class="width-35">
						<form:input path="optName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">操作代码：</label></td>
					<td class="width-35">
						<form:input path="optCode" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">结果状态：</label></td>
					<td class="width-35">
						<form:input path="result" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">流转目标：</label></td>
					<td class="width-35">
						<form:input path="target" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">执行任务：</label></td>
					<td class="width-35">
						<form:textarea path="execTask" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">所属项目：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/project/projectInfo/data" id="project" name="project.id" value="${flowProc.projectId}" labelName="project.projectName" labelValue="${flowProc.projectName}"
										title="选择项目" cssClass="form-control required" fieldLabels="客户编号|客户简称" fieldKeys="projectNo|projectName" searchLabels="项目编号" searchKeys="projectNo" ></sys:gridselect>

					</td>
		  		</tr>
				<tr>
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