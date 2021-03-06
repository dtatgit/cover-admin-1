<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>井卫报警信息管理</title>
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
					jp.post("${ctx}/cb/alarm/coverBellAlarm/save",$('#inputForm').serialize(),function(data){
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
			
	        $('#alarmDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="coverBellAlarm" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
<%--					<td class="width-15 active"><label class="pull-right">井铃ID：</label></td>
					<td class="width-35">
						<form:input path="coverBellId" htmlEscape="false"    class="form-control "/>
					</td>--%>
					<td class="width-15 active"><label class="pull-right">井卫编号：</label></td>
					<td class="width-35">
					<%--	<form:input path="bellNo" htmlEscape="false"    class="form-control "/>--%>
							${coverBellAlarm.bellNo}
					</td>
					<td class="width-15 active"><label class="pull-right">井盖编号：</label></td>
					<td class="width-35">
					<%--	<form:input path="coverNo" htmlEscape="false"    class="form-control "/>--%>
							${coverBellAlarm.coverNo}
					</td>
				</tr>
	<%--			<tr>
					<td class="width-15 active"><label class="pull-right">井盖ID：</label></td>
					<td class="width-35">
						<form:input path="coverId" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">井盖编号：</label></td>
					<td class="width-35">
						<form:input path="coverNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>--%>

				<tr>
					<td class="width-15 active"><label class="pull-right">报警编号：</label></td>
					<td class="width-35">
				<%--		<form:input path="alarmNum" htmlEscape="false"    class="form-control "/>--%>
							${coverBellAlarm.alarmNum}
					</td>
					<td class="width-15 active"><label class="pull-right">报警类型：</label></td>
					<td class="width-35">
							${fns:getDictLabel (coverBellAlarm.alarmType, "alarm_type", "--")}
						<%--<form:select path="alarmType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('alarm_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>--%>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">当前值：</label></td>
					<td class="width-35">
							${coverBellAlarm.currentValue}
					<%--	<form:input path="currentValue" htmlEscape="false"    class="form-control "/>--%>
					</td>
					<td class="width-15 active"><label class="pull-right">报警时间：</label></td>
					<td class="width-35">
						<fmt:formatDate value="${coverBellAlarm.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
						<%--<p class="input-group">
							<div class='input-group form_datetime' id='alarmDate'>
			                    <input type='text'  name="alarmDate" class="form-control"  value="<fmt:formatDate value="${coverBellAlarm.alarmDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
			            </p>--%>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
							${coverBellAlarm.remarks}
						<%--<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>--%>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>