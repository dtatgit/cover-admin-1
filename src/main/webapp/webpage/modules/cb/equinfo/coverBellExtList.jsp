<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>井卫设备信息管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="coverBellExtList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">井卫设备信息列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="coverBell" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="井盖编号：">井盖编号：</label>
				<form:input path="coverNo" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="井卫编号：">井卫编号：</label>
				<form:input path="bellNo" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="井卫型号：">井卫型号：</label>
				<form:input path="bellModel" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="设备类型：">设备类型：</label>
				<form:select path="bellType"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('bellType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="固件版本号：">固件版本号：</label>
				<form:input path="version" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="设备IMEI号：">设备IMEI号：</label>
				<form:input path="imei" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="设备SIM卡号：">设备SIM卡号：</label>
				<form:input path="sim" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="生命周期：">生命周期：</label>
				<form:select path="bellStatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('bell_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="工作状态：">工作状态：</label>
				<form:select path="workStatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('bell_work_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="设防状态：">设防状态：</label>
				<form:select path="defenseStatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('defense_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
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
			<shiro:hasPermission name="cb:equinfo:coverBell:add">
				<a id="add" class="btn btn-primary" onclick="add()"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="cb:equinfo:coverBell:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
		<shiro:hasPermission name="cv:equinfo:cover:work">
			<button id="work" class="btn btn-danger" disabled onclick="createWorkPage()">
				<i class="glyphicon glyphicon-edit"></i> 生成工单
			</button>
		</shiro:hasPermission>
<%--			<shiro:hasPermission name="cv:equinfo:cover:view">
				<button id="edit" class="btn btn-success" disabled onclick="coverInfo()">
					<i class="glyphicon glyphicon-edit"></i> 井盖信息
				</button>
			</shiro:hasPermission>--%>
			<shiro:hasPermission name="cb:equinfo:coverBell:alarmlist">
				<button id="alarm" class="btn btn-success" disabled onclick="alarmInfo()">
					<i class="glyphicon glyphicon-edit"></i> 报警记录
				</button>
			</shiro:hasPermission>
		<shiro:hasPermission name="cb:equinfo:coverBell:operationList">
			<button id="operation" class="btn btn-success" disabled onclick="operationInfo()">
				<i class="glyphicon glyphicon-edit"></i> 操作记录
			</button>
		</shiro:hasPermission>

		<shiro:hasPermission name="cb:equinfo:coverBell:bellStateList">
			<button id="bellState" class="btn btn-success" disabled onclick="bellStateInfo()">
				<i class="glyphicon glyphicon-edit"></i> 状态历史数据
			</button>
		</shiro:hasPermission>

			<shiro:hasPermission name="cb:equinfo:coverBell:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>

		<shiro:hasPermission name="cb:equinfo:coverBell:defense">
			<button id="fortify" class="btn btn-danger" disabled onclick="fortify()">
				<i class="glyphicon glyphicon-edit"></i> 设防
			</button>
		</shiro:hasPermission>

		<shiro:hasPermission name="cb:equinfo:coverBell:defense">
			<button id="revoke" class="btn btn-danger" disabled onclick="revoke()">
				<i class="glyphicon glyphicon-edit"></i> 撤防
			</button>
		</shiro:hasPermission>

		<shiro:hasPermission name="cb:equinfo:coverBell:scrap">
			<button id="scrap" class="btn btn-danger" disabled onclick="scrap()">
				<i class="glyphicon glyphicon-edit"></i> 报废
			</button>
		</shiro:hasPermission>

		<shiro:hasPermission name="cb:equinfo:coverBell:untying">
			<button id="untying" class="btn btn-danger" disabled onclick="untying()">
				<i class="glyphicon glyphicon-edit"></i> 解绑
			</button>
		</shiro:hasPermission>

		<shiro:hasPermission name="cb:equinfo:coverBell:toSetParam">
			<button id="setParam" class="btn btn-success" disabled onclick="toSetParam()">
				<i class="glyphicon glyphicon-edit"></i> 设置参数
			</button>
		</shiro:hasPermission>
			<shiro:hasPermission name="cb:equinfo:coverBell:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/cb/equinfo/coverBell/import" method="post" enctype="multipart/form-data"
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
	<table id="coverBellTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="cb:equinfo:coverBell:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="cb:equinfo:coverBell:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>