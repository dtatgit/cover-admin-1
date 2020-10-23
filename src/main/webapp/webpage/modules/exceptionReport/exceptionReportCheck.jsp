<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>异常上报管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
		var coverAppUrl = '${coverAppUrl}';
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					jp.loading();
					form.submit();
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
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/cb/report/exceptionReport"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="exceptionReport" action="${ctx}/cb/report/exceptionReport/checkReport" method="post" class="form-horizontal">
		<form:hidden path="id"/>
			<table class="table table-bordered">
				<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">异常上报时间：</label></td>
					<td class="width-35">
							${exceptionReport.createDate}
					</td>
					<td class="width-15 active"><label class="pull-right">异常上报人：</label></td>
					<td class="width-35">
						${exceptionReport.createByName}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">异常地址：</label></td>
					<td class="width-35">
							${exceptionReport.imageIds}
					</td>
					<td class="width-15 active"><label class="pull-right">异常描述：</label></td>
					<td class="width-35">
						${exceptionReport.remarks}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">上报照片：</label></td>
					<td class="width-35">
						<c:if test="exceptionReport.imageList !=null">
							<c:forEach items="exceptionReport.imageList" var="item">
								coverAppUrl +'/sys/file/download/'+ ${item}
							</c:forEach>
						</c:if>
					</td>
					<td class="width-15 active"><label class="pull-right">上报照片：</label></td>
					<td class="width-35">
							${exceptionReport.address}
					</td>
				</tr>
				</tbody>
			</table>
		<sys:message content="${message}"/>
			<div class="form-group">
				<label class="label-item single-overflow pull-left" title="性别：">审核结果：</label>
				<div class="col-xs-12">
					<form:radiobuttons class="i-checks" path="checkStatus" items="${fns:getDictList('pass_or_not')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</div>
			</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">不通过原因：</label>
					<div class="col-sm-10">
						<form:textarea path="passNotReason" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('report:exceptionReport:check') || isCheck}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
		                 </div>
		             </div>
		        </div>
		</c:if>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>