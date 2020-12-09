<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>项目管理</title>
	<meta name="decorator" content="ani"/>
	<%@include file="/webpage/include/treeview.jsp" %>
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
		
		$(document).ready(function(){
			$("#name").focus();
			
			validateForm= $("#inputForm").validate({
				/*rules: {
					name: {remote: "${ctx}/sys/role/checkName?oldName=" + encodeURIComponent("${role.name}")},//设置了远程验证，在初始化时必须预先调用一次。
					enname: {remote: "${ctx}/sys/role/checkEnname?oldEnname=" + encodeURIComponent("${role.enname}")}
				},
				messages: {
					name: {remote: "角色名已存在"},
					enname: {remote: "英文名已存在"}
				},*/
				submitHandler: function(form){
					jp.post("${ctx}/project/projectInfo/save",$('#inputForm').serialize(),function(data){
						if(data.success){
							console.log("11111");
	                    	$table.bootstrapTable('refresh');
	                    	jp.success(data.msg);
	                    }else{
            	  			jp.error(data.msg);
	                    }
	                    
	                    jp.close($topIndex);//关闭dialog
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
	<form:form id="inputForm" modelAttribute="projectInfo" autocomplete="off"  method="post" class="form-horizontal" >
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td class="width-15 active"><label class="pull-right">项目编号:</label></td>
                  <td>
				   <form:input path="projectNo" htmlEscape="false"  class="form-control " readonly="true"/>
				 </td>
		         <td  class="width-15 active" class="active"><label class="pull-right"><font color="red">*</font>项目名称：</label>
				 </td>
		         <td class="width-35">
					 <form:input path="projectName" htmlEscape="false"    class="form-control required"/>
				 </td>
			  </tr>

		      <tr>
                  <td  class="width-15 active" class="active"><label class="pull-right">项目状态：</label></td>
                  <td class="width-35">
                      <form:select path="status" class="form-control ">
                          <form:options items="${fns:getDictList('on_off')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                      </form:select>
                  </td>
				 <td class="width-15 active"><label class="pull-right">备注:</label></td>
		         <td class="width-35"><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control "/></td>
		      </tr>
			</tbody>
			</table>
		<%--<sys:message content="${message}"/>
		<c:if test="${fns:hasPermission('project:projectInfo:edit') || isAdd}">
			<div class="col-lg-3"></div>
			<div class="col-lg-6">
				<div class="form-group text-center">
					<div>
						<button class="btn btn-primary btn-block btn-lg btn-parsley"
								data-loading-text="正在提交...">提 交
						</button>
					</div>
				</div>
			</div>
		</c:if>--%>
	</form:form>
</body>
</html>