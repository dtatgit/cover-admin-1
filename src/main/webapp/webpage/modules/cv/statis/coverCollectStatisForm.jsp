<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>窨井盖采集统计管理</title>
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
					jp.post("${ctx}/cv/statis/coverCollectStatis/save",$('#inputForm').serialize(),function(data){
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
			
	        $('#statisDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="coverCollectStatis" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">采集人员：</label></td>
					<td class="width-35">
						<sys:userselect id="collectUser" name="collectUser.id" value="${coverCollectStatis.collectUser.id}" labelName="collectUser.name" labelValue="${coverCollectStatis.collectUser.name}"
							    cssClass="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">采集数量：</label></td>
					<td class="width-35">
						<form:input path="collectNum" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开始时间：</label></td>
					<td class="width-35">
						<form:input path="startDate" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">结束时间：</label></td>
					<td class="width-35">
						<form:input path="endDate" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">统计时间：</label></td>
					<td class="width-35">
						<p class="input-group">
							<div class='input-group form_datetime' id='statisDate'>
			                    <input type='text'  name="statisDate" class="form-control"  value="<fmt:formatDate value="${coverCollectStatis.statisDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
			            </p>
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