<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>部门流程配置管理</title>
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
					jp.post("${ctx}/flow/base/flowDepart/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="flowDepart" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">流程编号：</label></td>
					<td class="width-35">
						<form:input path="flowNo" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">工单类型：</label></td>
					<td class="width-35">
						<form:select path="billType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('work_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">部门：</label></td>
					<td class="width-35">
						<sys:treeselect id="orgId" name="orgId.id" value="${flowDepart.orgId.id}" labelName="orgId.name" labelValue="${flowDepart.orgId.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">所属项目：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/project/projectInfo/data" id="project" name="projectInfo.id" value="${flowProc.projectId}" labelName="projectInfo.projectName" labelValue="${flowProc.projectName}"
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