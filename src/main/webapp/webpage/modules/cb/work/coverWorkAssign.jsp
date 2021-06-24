<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工单信息管理</title>
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
					jp.post("${ctx}/cb/work/coverWork/workAssign",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="coverWork" class="form-horizontal">
		<form:hidden path="ids"/>
		<%--<form:hidden path="workNums"/>--%>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<%--<tr>
					<td class="width-15 active"><label class="pull-right">工单编号：</label></td>
					<td class="width-35" colspan="3">
						<form:input path="workNums" htmlEscape="false"  readonly="true"   class="form-control "/>
					</td>
				</tr>
--%>
				<tr>
					<td class="width-15 active"><label class="pull-right">施工人员：</label></td>
					<td class="width-35" colspan="3">
						<sys:userselect id="constructionUser" name="constructionUser.id" value="${coverWork.constructionUser.id}" labelName="constructionUser.name" labelValue="${coverWork.constructionUser.name}"
							    cssClass="form-control required"/>
					</td>
					<%--<td class="width-15 active"><label class="pull-right">紧急程度：</label></td>
					<td class="width-35">
						<form:select path="workLevel" class="form-control ">
			&lt;%&ndash;				<form:option value="" label=""/>&ndash;%&gt;
							<form:options items="${fns:getDictList('work_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>--%>
				</tr>
				<%--<tr>
					<td class="width-15 active"><label class="pull-right">联系电话：</label></td>
					<td class="width-35">
						<form:input path="phone" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>


				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">施工内容：</label></td>
					<td class="width-35">
						<form:textarea path="constructionContent" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>--%>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>