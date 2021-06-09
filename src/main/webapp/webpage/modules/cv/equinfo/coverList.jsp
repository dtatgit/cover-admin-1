<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>井盖基础信息管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="coverList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">井盖基础信息列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>

	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" action="${ctx}/cv/equinfo/cover/export" modelAttribute="cover" class="form form-horizontal well clearfix">
<%--			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="状态：">状态：</label>
				<form:select path="coverStatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cover_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>--%>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="井盖编号：">井盖编号：</label>
				<form:input path="no" htmlEscape="false" maxlength="20"  class=" form-control"/>
			</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="井卫编号：">井卫编号：</label>
					<form:input path="tagNo" htmlEscape="false" maxlength="20"  class=" form-control"/>
				</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="在线状态：">在线状态：</label>
				<form:select path="onlineStatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('bell_work_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
            <div class="col-xs-12 col-sm-6 col-md-4">
                    <label class="label-item single-overflow pull-left" title="监测状态：">监测状态：</label>
                    <form:select path="monitoringStatus"  class="form-control m-b">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('monitoring_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-4">
                    <label class="label-item single-overflow pull-left" title="工作状态：">工作状态：</label>
                    <form:select path="workStatus"  class="form-control m-b">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('defense_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
             </div>

			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="窨井用途：">窨井用途：</label>
				<form:select path="purpose"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cover_purpose')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="地理位置：">地理位置：</label>
				<form:select path="situation"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cover_situation')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>

			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="创建人：">创建人：</label>
				<sys:userselect id="createBy" name="createBy.id" value="${cover.createBy.id}" labelName="createBy.name" labelValue="${cover.createBy.name}"
							    cssClass="form-control required"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="创建时间：">&nbsp;创建时间：</label>
				<div class="col-xs-12">
					   <div class="col-xs-12 col-sm-5">
							  <div class='input-group date' id='beginCreateDate' style="left: -10px;" >
								   <input type='text'  name="beginCreateDate" class="form-control"  />
								   <span class="input-group-addon">
									   <span class="glyphicon glyphicon-calendar"></span>
								   </span>
							 </div>
						</div>
						<div class="col-xs-12 col-sm-1">
								~
						</div>
						<div class="col-xs-12 col-sm-5">
							<div class='input-group date' id='endCreateDate' style="left: -10px;" >
								   <input type='text'  name="endCreateDate" class="form-control" />
								   <span class="input-group-addon">
									   <span class="glyphicon glyphicon-calendar"></span>
								   </span>
							</div>
						</div>
				</div>
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
		<shiro:hasPermission name="cb:equinfo:cover:defense">
			<button id="fortify" class="btn btn-danger" disabled onclick="fortify()">
				<i class="glyphicon glyphicon-edit"></i> 设防
			</button>
		</shiro:hasPermission>

		<shiro:hasPermission name="cb:equinfo:cover:defense">
			<button id="revoke" class="btn btn-danger" disabled onclick="revoke()">
				<i class="glyphicon glyphicon-edit"></i> 撤防
			</button>
		</shiro:hasPermission>

		<shiro:hasPermission name="cv:equinfo:cover:work">
			<button id="work" class="btn btn-danger" disabled onclick="createWorkPage()">
				<i class="glyphicon glyphicon-edit"></i> 派发工单
			</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="cb:equinfo:cover:untying">
			<button id="untying" class="btn btn-danger" disabled onclick="untying()">
				<i class="glyphicon glyphicon-edit"></i> 解绑井卫
			</button>
		</shiro:hasPermission>
		<%--<shiro:hasPermission name="cv:equinfo:cover:bell">--%>
			<%--<button id="bell" class="btn btn-danger" disabled onclick="bellInfo()">--%>
				<%--<i class="glyphicon glyphicon-edit"></i> 井卫信息--%>
			<%--</button>--%>
		<%--</shiro:hasPermission>--%>
		<%--<shiro:hasPermission name="cv:equinfo:cover:alarm">--%>
			<%--<button id="alarm" class="btn btn-danger" disabled onclick="alarmInfo()">--%>
				<%--<i class="glyphicon glyphicon-edit"></i> 报警信息--%>
			<%--</button>--%>
		<%--</shiro:hasPermission>--%>

			<%--<shiro:hasPermission name="cv:equinfo:cover:del">--%>
				<%--<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">--%>
	            	<%--<i class="glyphicon glyphicon-remove"></i> 删除--%>
	        	<%--</button>--%>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:hasPermission name="cv:equinfo:cover:import">--%>
				<%--<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>--%>
				<%--<div id="importBox" class="hide">--%>
						<%--<form id="importForm" action="${ctx}/cv/equinfo/cover/import" method="post" enctype="multipart/form-data"--%>
							 <%--style="padding-left:20px;text-align:center;" ><br/>--%>
							<%--<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　--%>


						<%--</form>--%>
				<%--</div>--%>
			<%--</shiro:hasPermission>--%>
		<%--<shiro:hasPermission name="cv:equinfo:cover:export">--%>
			<%--<button id="export" class="btn btn-info"  onclick="exportAll()">--%>
				<%--<i class="glyphicon glyphicon-export"></i> 导出--%>
			<%--</button>--%>
		<%--</shiro:hasPermission>--%>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>

	<!-- 表格 -->
	<table id="coverTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="cv:equinfo:cover:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="cv:equinfo:cover:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>
	</div>
	</div>
	</div>
</body>
</html>
