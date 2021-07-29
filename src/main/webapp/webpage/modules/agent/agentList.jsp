<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>代理商管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="agentList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">代理商管理</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>

	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse in" aria-expanded="true">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="projectInfo" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="账号：">账号：</label>
				<form:input path="loginName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="账号：">代理商简称：</label>
					<form:input path="projectName" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
<%--				<div class="col-xs-12 col-sm-6 col-md-4">--%>
<%--					<label class="label-item single-overflow pull-left" title="部门：">部门：</label>--%>
<%--					<sys:treeselect id="office" name="office.id" value="${projectInfo.office.id}" labelName="office.office.name" labelValue="${projectInfo.office.office.name}"--%>
<%--									title="部门" url="/sys/office/treeData?type=2" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>--%>
<%--				</div>--%>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="账号状态：">账号状态：</label>
				<form:select path="status"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('on_off')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
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

	<!-- 工具栏 -->
	<div id="toolbar">
			<shiro:hasPermission name="project:agent:add">
				<a id="add" class="btn btn-primary" onclick="add()" title=""><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="project:agent:edit">
				<button id="edit" class="btn btn-success" disabled onclick="edit()">
					<i class="glyphicon glyphicon-edit"></i> 修改
				</button>
			</shiro:hasPermission>
		<shiro:hasPermission name="project:agent:enable">
			<button id="enable" class="btn btn-success" disabled onclick="enableAll()">
				<i class="glyphicon glyphicon-edit"></i> 启用
			</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="project:agent:disable">
			<button id="disable" class="btn btn-success" disabled onclick="disableAll()">
				<i class="glyphicon glyphicon-edit"></i> 禁用
			</button>
		</shiro:hasPermission>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>

	<!-- 表格 -->
	<table id="projectInfoTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="project:projectInfo:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="project:projectInfo:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>
	</div>
	</div>
	</div>
</body>
</html>