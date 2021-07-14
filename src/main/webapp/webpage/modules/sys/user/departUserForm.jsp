<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>账号管理</title>
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
					jp.post("${ctx}/sys/departUser/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="user" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>

				<tr>
					<td class="active"><label class="pull-right"><font color="red">*</font>所在机构:</label></td>
					<td><sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
										allowClear="true" title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" notAllowSelectParent="true"/></td>
					<td class="width-15 active"><label class="pull-right">账号：</label></td>
					<td class="width-35">
						<c:if test="${user.id!=null}">
							${user.loginName}
						</c:if>
						<c:if test="${user.id==null}">
							<form:input path="loginName" htmlEscape="false"    class="form-control "/>
						</c:if>

					</td>
				</tr>
				<tr>
					<td class="active"><label class="pull-right"><c:if test="${empty user.id}"><font color="red">*</font></c:if>密码:</label></td>
					<td><input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="form-control ${empty user.id?'required':''}"/>
						<c:if test="${not empty user.id}"><span class="help-inline">若不修改密码，请留空。</span></c:if></td>
					<td class="active"><label class="pull-right"><c:if test="${empty user.id}"><font color="red">*</font></c:if>确认密码:</label></td>
					<td><input id="confirmNewPassword" name="confirmNewPassword" type="password"  class="form-control ${empty user.id?'required':''}" value="" maxlength="50" minlength="3" equalTo="#newPassword"/></td>
				</tr>

				<tr>
					<td class="active"><label class="pull-right"><font color="red">*</font>姓名:</label></td>
					<td><form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/></td>
					<td class="width-15 active"><label class="pull-right">手机号：</label></td>
					<td class="width-35">
						<form:input path="mobile" htmlEscape="false"    class="form-control  isMobile"/>
					</td>
					<%--<td class="active"><label class="pull-right">是否允许登录:</label></td>--%>
					<%--<td><form:select path="loginFlag"  class="form-control">--%>
						<%--<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
					<%--</form:select></td>--%>
				</tr>
				<tr>
					<td class="active"><label class="pull-right"><font color="red">*</font>岗位类型:</label></td>
					<td>
						<form:checkboxes path="roleIdList" items="${allRoles}" itemLabel="name" itemValue="id" htmlEscape="false" cssClass="i-checks required"/>
						<label id="roleIdList-error" class="error" for="roleIdList"></label>
					</td>
					<td class="active"><label class="pull-right">备注:</label></td>
					<td><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/></td>
				</tr>

				<c:if test="${not empty user.id}">
					<tr>
						<td class=""><label class="pull-right">创建时间:</label></td>
						<td><span class="lbl"><fmt:formatDate value="${user.createDate}" type="both" dateStyle="full"/></span></td>
						<td class=""><label class="pull-right">最后登陆:</label></td>
						<td><span class="lbl">IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/></span></td>
					</tr>
				</c:if>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>