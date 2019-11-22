<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工单操作记录管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="flowWorkOptList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">工单操作记录列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="flowWorkOpt" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="工单：">工单：</label>
				<sys:gridselect url="${ctx}/cb/work/coverWork/data" id="billId" name="billId.id" value="${flowWorkOpt.billId.id}" labelName="billId.work_num" labelValue="${flowWorkOpt.billId.work_num}"
					title="选择工单" cssClass="form-control required" fieldLabels="井盖编号|工单编号|联系电话" fieldKeys="coverNo|workNum|phone" searchLabels="井盖编号|工单编号|联系电话" searchKeys="coverNo|workNum|phone" ></sys:gridselect>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="流程信息：">流程信息：</label>
				<sys:gridselect url="${ctx}/flow/base/flowProc/data" id="flowId" name="flowId.id" value="${flowWorkOpt.flowId.id}" labelName="flowId.flowNo" labelValue="${flowWorkOpt.flowId.flowNo}"
					title="选择流程信息" cssClass="form-control required" fieldLabels="流程编号|流程名称|版本" fieldKeys="flowNo|flowName|version" searchLabels="流程编号|流程名称|版本" searchKeys="flowNo|flowName|version" ></sys:gridselect>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="操作信息：">操作信息：</label>
				<sys:gridselect url="${ctx}/flow/opt/flowOpt/data" id="optId" name="optId.id" value="${flowWorkOpt.optId.id}" labelName="optId.optName" labelValue="${flowWorkOpt.optId.optName}"
					title="选择操作信息" cssClass="form-control required" fieldLabels="操作名称|操作代码" fieldKeys="optName|optCode" searchLabels="操作名称|操作代码" searchKeys="optName|optCode" ></sys:gridselect>
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
			<shiro:hasPermission name="flow:opt:flowWorkOpt:add">
				<a id="add" class="btn btn-primary" onclick="add()"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="flow:opt:flowWorkOpt:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="flow:opt:flowWorkOpt:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="flow:opt:flowWorkOpt:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/flow/opt/flowWorkOpt/import" method="post" enctype="multipart/form-data"
							 style="padding-left:20px;text-align:center;" ><br/>
							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　
							
							
						</form>
				</div>
			</shiro:hasPermission>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="flowWorkOptTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="flow:opt:flowWorkOpt:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="flow:opt:flowWorkOpt:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>