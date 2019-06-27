<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>井铃设备信息管理</title>
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
					jp.post("${ctx}/cb/equinfo/coverBell/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="coverBell" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">所属井盖：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/cv/equinfo/cover/data" id="cover" name="cover.id" value="${coverBell.cover.id}" labelName="cover.no" labelValue="${coverBell.cover.no}"
										title="选择所属井盖" cssClass="form-control required" fieldLabels="井盖编号|详细地址" fieldKeys="no|addressDetail" searchLabels="井盖编号" searchKeys="no" ></sys:gridselect>
					</td>
				<%--	<td class="width-35">
						<form:input path="coverId" htmlEscape="false"    class="form-control "/>
					</td>--%>
					<td class="width-15 active"><label class="pull-right">井铃编号：</label></td>
					<td class="width-35">
						<form:input path="bellNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">井铃型号：</label></td>
					<td class="width-35">
						<form:input path="bellModel" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">设备IMEI号：</label></td>
					<td class="width-35">
						<form:input path="imei" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">设备SIM卡号：</label></td>
					<td class="width-35">
						<form:input path="sim" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">生命周期：</label></td>
					<td class="width-35">
						<form:select path="bellStatus" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('bell_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">工作状态：</label></td>
					<td class="width-35">
						<form:select path="workStatus" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('bell_work_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">设防状态：</label></td>
					<td class="width-35">
						<form:select path="defenseStatus" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('defense_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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