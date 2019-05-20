<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>任务处理明细管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="coverTaskProcessList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">任务处理明细列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="coverTaskProcess" class="form form-horizontal well clearfix">
	<%--		 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="所属任务：">所属任务：</label>
				<sys:gridselect url="${ctx}/cv/task/coverTaskInfo/data" id="coverTaskInfo" name="coverTaskInfo.id" value="${coverTaskProcess.coverTaskInfo.id}" labelName="coverTaskInfo.taskNo" labelValue="${coverTaskProcess.coverTaskInfo.taskNo}"
					title="选择所属任务" cssClass="form-control required" fieldLabels="任务编号|任务名称|任务数量" fieldKeys="taskNo|taskName|taskNum" searchLabels="任务编号|任务名称" searchKeys="taskNo|taskName" ></sys:gridselect>
			</div>--%>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="任务状态：">任务状态：</label>
				<form:select path="taskStatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('task_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="井盖信息：">井盖信息：</label>
				<sys:gridselect url="${ctx}/cv/equinfo/cover/data" id="cover" name="cover.id" value="${coverTaskProcess.cover.id}" labelName="cover.no" labelValue="${coverTaskProcess.cover.no}"
					title="选择井盖信息" cssClass="form-control required" fieldLabels="编号|地址" fieldKeys="no|addressDetail" searchLabels="编号" searchKeys="no" ></sys:gridselect>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="审核人：">审核人：</label>
				<sys:userselect id="auditUser" name="auditUser.id" value="${coverTaskProcess.auditUser.id}" labelName="auditUser.name" labelValue="${coverTaskProcess.auditUser.name}"
							    cssClass="form-control "/>
			</div>
<%--			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="审核状态：">审核状态：</label>
				<form:select path="auditStatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('audit_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>--%>
<%--			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="申请事项：">申请事项：</label>
				<form:select path="applyItem"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('apply_item')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>--%>
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

		<!---获取查询井盖信息到审核信息中start--->
		<div id="obtainDiv" style="display: none;">
			<form:form id="obtainForm" modelAttribute="coverTaskProcess" class="form form-horizontal well clearfix">
				<hidden id="hiddenFlag" value="0"></hidden>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="所属任务：">所属任务：</label>
					<sys:gridselect url="${ctx}/cv/task/coverTaskInfo/data" id="coverTaskInfo" name="coverTaskInfo.id" value="${coverTaskProcess.coverTaskInfo.id}" labelName="coverTaskInfo.taskNo" labelValue="${coverTaskProcess.coverTaskInfo.taskNo}"
									title="选择所属任务" cssClass="form-control required" fieldLabels="任务编号|任务名称|任务数量" fieldKeys="taskNo|taskName|taskNum" searchLabels="任务编号|任务名称" searchKeys="taskNo|taskName" ></sys:gridselect>
				</div>

				<div class="col-xs-12 col-sm-6 col-md-4">
					<div style="margin-top:26px">
						<a  onclick="obtainCover()" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 获取任务</a>
					</div>
				</div>
			</form:form>
		</div>
		<!---获取查询井盖信息到审核信息中end--->

	<!-- 工具栏 -->
	<div id="toolbar">
			<shiro:hasPermission name="cv:task:coverTaskProcess:add">
				<a id="add" class="btn btn-primary" onclick="add()"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="cv:task:coverTaskProcess:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="cv:task:coverTaskProcess:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="cv:task:coverTaskProcess:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/cv/task/coverTaskProcess/import" method="post" enctype="multipart/form-data"
							 style="padding-left:20px;text-align:center;" ><br/>
							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　
							
							
						</form>
				</div>
			</shiro:hasPermission>
		<shiro:hasPermission name="cv:task:coverTaskProcess:obtainCover">
			<a id="add" class="btn btn-primary" onclick="obtainCoverPage()" ><i class="glyphicon glyphicon-plus"></i> 获取井盖</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="cv:task:coverTaskProcess:audit">
			<button id="audit" class="btn btn-success" disabled onclick="auditPage()">
				<i class="glyphicon glyphicon-edit"></i> 审核
			</button>
		</shiro:hasPermission>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="coverTaskProcessTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="cv:task:coverTaskProcess:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="cv:task:coverTaskProcess:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>