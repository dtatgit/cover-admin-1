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
			<form:form id="searchForm" modelAttribute="cover" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="状态：">状态：</label>
				<form:select path="coverStatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cover_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="编号：">编号：</label>
				<form:input path="no" htmlEscape="false" maxlength="20"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="井盖类型：">井盖类型：</label>
				<form:select path="coverType"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cover_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="市：">市：</label>
				<form:input path="city" htmlEscape="false" maxlength="40"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="区：">区：</label>
				<form:input path="district" htmlEscape="false" maxlength="40"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="街道（办事处）：">街道（办事处）：</label>
				<form:input path="township" htmlEscape="false" maxlength="40"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="地址：路（街巷）：">地址：路（街巷）：</label>
				<form:input path="street" htmlEscape="false" maxlength="80"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="井位用途：">井位用途：</label>
				<form:select path="purpose"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cover_purpose')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="井位地理场合：">井位地理场合：</label>
				<form:select path="situation"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cover_situation')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="制造商：">制造商：</label>
				<form:input path="manufacturer" htmlEscape="false" maxlength="200"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="尺寸规格：">尺寸规格</label>
				<form:select path="sizeSpec"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cover_size_spec')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="井盖规格：">井盖规格：</label>
				<form:select path="sizeRule"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cover_size_rule')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="井盖材质：">井盖材质：</label>
				<form:select path="material"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cover_material')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="权属单位：">权属单位：</label>
				<form:select path="ownerDepart"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cover_owner_depart')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>

				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="井盖损坏形式：">井盖损坏形式：</label>
					<form:select path="damageType"  class="form-control m-b">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('cover_damage')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>

			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="是否损毁：">是否损毁：</label>
				<form:select path="isDamaged"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('boolean')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="高度差：">高度差：</label>
				 <div class="col-xs-12">
					 <div class="col-xs-12 col-sm-5">
						 <div class='input-group' id='beginAltitudeIntercept' style="left: -10px;" >
							 <input type='text'  name="beginAltitudeIntercept" class="form-control"  />

						 </div>
					 </div>
					 <div class="col-xs-12 col-sm-1">
						 ~
					 </div>
					 <div class="col-xs-12 col-sm-5">
						 <div class='input-group' id='endAltitudeIntercept' style="left: -10px;" >
							 <input type='text'  name="endAltitudeIntercept" class="form-control" />
						 </div>
					 </div>
				 </div>
				<%-- <form:input path="altitudeIntercept" htmlEscape="false"    class="form-control "/>--%>
			<%--	<form:select path="altitudeIntercept"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cover_altitude_intercept')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>--%>
			</div>

				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="是否生成工单：">是否生成工单：</label>
					<form:select path="isGwo"  class="form-control m-b">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('boolean')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>

			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="创建人：">创建人：</label>
				<sys:userselect id="createBy" name="createBy.id" value="${cover.createBy.id}" labelName="createBy.name" labelValue="${cover.createBy.name}"
							    cssClass="form-control required"/>
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
			<shiro:hasPermission name="cv:equinfo:cover:add">
				<a id="add" class="btn btn-primary" onclick="add()"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="cv:equinfo:cover:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 信息修复
	        	</button>
			</shiro:hasPermission>

		<shiro:hasPermission name="cv:equinfo:cover:work">
			<button id="work" class="btn btn-danger" disabled onclick="createWorkPage()">
				<i class="glyphicon glyphicon-edit"></i> 生成安装工单
			</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="cv:equinfo:cover:bell">
			<button id="bell" class="btn btn-danger" disabled onclick="bellInfo()">
				<i class="glyphicon glyphicon-edit"></i> 井卫信息
			</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="cv:equinfo:cover:alarm">
			<button id="alarm" class="btn btn-danger" disabled onclick="alarmInfo()">
				<i class="glyphicon glyphicon-edit"></i> 报警信息
			</button>
		</shiro:hasPermission>

			<shiro:hasPermission name="cv:equinfo:cover:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="cv:equinfo:cover:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
				<div id="importBox" class="hide">
						<form id="importForm" action="${ctx}/cv/equinfo/cover/import" method="post" enctype="multipart/form-data"
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