<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>测试管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="coverTestList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">获取井盖信息</h3>
		</div>
		<div class="panel-body">


			<div >
				<div >
					<div >
						<form:form id="searchForm" modelAttribute="coverAudit" class="form form-horizontal well clearfix">
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="测试1：">测试1：</label>
								<sys:treeselect id="test1" name="test1" value="${coverAudit.auditResult}" labelName="" labelValue="${coverAudit.auditResult}"
												title="区域" url="/sys/area/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
							</div>

							<div class="col-xs-12 col-sm-6 col-md-4">
								<div style="margin-top:26px">
									<a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
									<a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>

	</div>
</div>
</body>
</html>