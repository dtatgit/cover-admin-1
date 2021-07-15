<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>工单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="/webpage/include/treeview.jsp" %>
    <%@include file="coverWorkList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">工单管理</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>

            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="coverWork"
                                   class="form form-horizontal well clearfix">
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="工单编号：">工单编号：</label>
                                <form:input path="workNum" htmlEscape="false" maxlength="64" class=" form-control"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="井盖编号：">井盖编号：</label>
                                <form:input path="coverNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="工单类型：">工单类型：</label>
                                <form:select path="workType" class="form-control m-b">
                                    <form:option value="" label=""/>
                                    <form:options items="${fns:getDictList('work_type')}" itemLabel="label"
                                                  itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="时间：">时间：</label>
								<div class="col-xs-12">
									<div class="col-xs-12 col-sm-5">
										<div class='input-group date' id='beginDate' style="left: -10px;">
											<input type='text' name="beginDate" class="form-control"/>
											<span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
										</div>
									</div>
									<div class="col-xs-12 col-sm-1">
										~
									</div>
									<div class="col-xs-12 col-sm-5">
										<div class='input-group date' id='endDate' style="left: -10px;">
											<input type='text' name="endDate" class="form-control"/>
											<span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
										</div>
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="施工人员：">处理人：</label>
								<sys:userselect id="constructionUser" name="constructionUser.id"
												value="${coverWork.constructionUser.id}"
												labelName="constructionUser.name"
												labelValue="${coverWork.constructionUser.name}"
												cssClass="form-control "/>
							</div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="工单状态：">工单状态：</label>
                                <form:input path="workStatus" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    <%--<form:select path="workStatus"  class="form-control m-b">
                                        <form:option value="" label=""/>
                                        <form:options items="${fns:getDictList('work_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                    </form:select>--%>
                            </div>
                           <%-- <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="联系电话：">联系电话：</label>
                                <form:input path="phone" htmlEscape="false" maxlength="64" class=" form-control"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="施工部门：">施工部门：</label>
                                <sys:treeselect id="constructionDepart" name="constructionDepart.id"
                                                value="${coverWork.constructionDepart.id}"
                                                labelName="constructionDepart.name"
                                                labelValue="${coverWork.constructionDepart.name}"
                                                title="部门" url="/sys/office/treeData?type=2" cssClass="form-control"
                                                allowClear="true" notAllowSelectParent="true"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="紧急程度：">紧急程度：</label>
                                <form:select path="workLevel" class="form-control m-b">
                                    <form:option value="" label=""/>
                                    <form:options items="${fns:getDictList('work_level')}" itemLabel="label"
                                                  itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="工单批次：">工单批次：</label>
                                <form:input path="batch" htmlEscape="false" maxlength="64" class=" form-control"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="流程信息：">流程信息：</label>
                                <sys:gridselect url="${ctx}/flow/base/flowProc/data" id="flowId" name="flowId.id"
                                                value="${coverWork.flowId.id}" labelName="flowId.flowNo"
                                                labelValue="${coverWork.flowId.flowNo}"
                                                title="选择流程信息" cssClass="form-control required"
                                                fieldLabels="流程编号|流程名称|版本" fieldKeys="flowNo|flowName|version"
                                                searchLabels="流程编号|流程名称|版本"
                                                searchKeys="flowNo|flowName|version"></sys:gridselect>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="父类工单：">父类工单：</label>
                                <sys:gridselect url="${ctx}/cb/work/coverWork/data" id="parentWorkId"
                                                name="parentWorkId.id" value="${coverWork.parentWorkId.id}"
                                                labelName="parentWorkId.work_num"
                                                labelValue="${coverWork.parentWorkId.work_num}"
                                                title="选择父类工单" cssClass="form-control required"
                                                fieldLabels="井盖编号|工单编号|联系电话" fieldKeys="coverNo|workNum|phone"
                                                searchLabels="井盖编号|工单编号|联系电话"
                                                searchKeys="coverNo|workNum|phone"></sys:gridselect>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="生命周期：">生命周期：</label>
                                <form:select path="lifeCycle" class="form-control m-b">
                                    <form:option value="" label=""/>
                                    <form:options items="${fns:getDictList('lifecycle')}" itemLabel="label"
                                                  itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>--%>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <div style="margin-top:26px">
                                    <a id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
                                            class="fa fa-search"></i> 查询</a>
                                    <a id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
                                            class="fa fa-refresh"></i> 重置</a>
                                </div>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>

            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="cb:work:coverWork:assign">
                    <button id="assign" class="btn btn-danger" disabled onclick="workAssign()">
                        <i class="glyphicon glyphicon-edit"></i> 批量指派
                    </button>
                </shiro:hasPermission>
               <%-- <shiro:hasPermission name="cb:work:coverWork:add">
                    <a id="add" class="btn btn-primary" onclick="add()"><i class="glyphicon glyphicon-plus"></i>
                        新建工单</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="cb:work:coverWork:edit">
                    <button id="edit" class="btn btn-success" disabled onclick="edit()">
                        <i class="glyphicon glyphicon-edit"></i> 修改工单
                    </button>
                </shiro:hasPermission>
                <shiro:hasPermission name="cb:work:coverWork:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
                        <i class="glyphicon glyphicon-remove"></i> 删除
                    </button>
                </shiro:hasPermission>

                <shiro:hasPermission name="cb:work:coverWork:workOperationList">
                    <button id="workOperation" class="btn btn-success" disabled onclick="workOperation()">
                        <i class="glyphicon glyphicon-edit"></i> 工单操作记录
                    </button>
                </shiro:hasPermission>

                <shiro:hasPermission name="cb:work:coverWork:audit">
                    <button id="audit" class="btn btn-success" disabled onclick="auditPage()">
                        <i class="glyphicon glyphicon-edit"></i> 审核
                    </button>
                </shiro:hasPermission>

				<shiro:hasPermission name="cb:work:coverWork:discard">
					<button id="discard" class="btn btn-success" disabled onclick="discard()">
						<i class="glyphicon glyphicon-edit"></i> 作废
					</button>
				</shiro:hasPermission>

                <shiro:hasPermission name="cb:work:coverWork:import">
                    <button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
                    <div id="importBox" class="hide">
                        <form id="importForm" action="${ctx}/cb/work/coverWork/import" method="post"
                              enctype="multipart/form-data"
                              style="padding-left:20px;text-align:center;"><br/>
                            <input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　


                        </form>
                    </div>
                </shiro:hasPermission>--%>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2"
                   href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>

            <!-- 表格 -->
            <table id="coverWorkTable" data-toolbar="#toolbar"></table>

            <!-- context menu -->
            <ul id="context-menu" class="dropdown-menu">
                <shiro:hasPermission name="cb:work:coverWork:edit">
                    <li data-item="edit"><a>编辑</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="cb:work:coverWork:del">
                    <li data-item="delete"><a>删除</a></li>
                </shiro:hasPermission>
                <li data-item="action1"><a>取消</a></li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>