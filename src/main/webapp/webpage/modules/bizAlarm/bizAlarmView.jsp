<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>业务报警详情</title>
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
			/*validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					jp.post("${ctx}/oilSmokeData/oilSmokeData/save",$('#inputForm').serialize(),function(data){
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
			});*/

		});
	</script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="cover" class="form-horizontal">
	<form:hidden path="id"/>
	<sys:message content="${message}"/>
	<table class="table table-bordered">
		<tbody>
		<tr>
			<td class="width-15 active"><label class="pull-right">直径：</label></td>
			<td class="width-35">
				<form:input path="sizeDiameter" htmlEscape="false"    class="form-control " readonly="true"/>
			</td>
			<td class="width-15 active"><label class="pull-right">半径：</label></td>
			<td class="width-35">
				<form:input path="sizeRadius" htmlEscape="false"    class="form-control " readonly="true"/>
			</td>
		</tr>
		<tr>
			<td class="width-15 active"><label class="pull-right">长度：</label></td>
			<td class="width-35">
				<form:input path="sizeLength" htmlEscape="false"    class="form-control " readonly="true"/>
			</td>
			<td class="width-15 active"><label class="pull-right">宽度：</label></td>
			<td class="width-35">
				<form:input path="sizeWidth" htmlEscape="false"    class="form-control " readonly="true"/>
			</td>
		</tr>
		</tbody>
	</table>
</form:form>

<form:form id="inputForm" modelAttribute="coverBell" class="form-horizontal">
	<form:hidden path="id"/>
	<sys:message content="${message}"/>
	<table class="table table-bordered">
		<tbody>
		<tr>
			<td class="width-15 active"><label class="pull-right">井卫编号：</label></td>
			<td class="width-35">
				<form:input path="coverNo" htmlEscape="false"    class="form-control " readonly="true"/>
			</td>
			<td class="width-15 active"><label class="pull-right">警铃类型：</label></td>
			<td class="width-35">
				<form:input path="bellType" htmlEscape="false"    class="form-control " readonly="true"/>
			</td>
		</tr>
		</tbody>
	</table>
</form:form>

<form:form id="inputForm" modelAttribute="bizAlarm" class="form-horizontal">
	<form:hidden path="id"/>
	<sys:message content="${message}"/>
	<table class="table table-bordered">
		<tbody>
		<tr>
			<td class="width-15 active"><label class="pull-right">报警编号：</label></td>
			<td class="width-35">
				<form:input path="alarmNo" htmlEscape="false"    class="form-control " readonly="true"/>
			</td>
			<td class="width-15 active"><label class="pull-right">报警类型：</label></td>
			<td class="width-35">
				<form:input path="alarmType" htmlEscape="false"    class="form-control " readonly="true"/>
			</td>
		</tr>
		</tbody>
	</table>
</form:form>
</body>
</html>