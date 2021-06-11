<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>井盖基础信息管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp"%>
    <%@include file="/webpage/include/treeview.jsp" %>
    <%@include file="coverWaitAudit.js" %>
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
                        <form:form id="searchForm" action="${ctx}/cv/equinfo/coverWaitAudit/export" modelAttribute="cover" class="form form-horizontal well clearfix">

                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="井盖编号：">井盖编号：</label>
                                <form:input path="no" htmlEscape="false" maxlength="20"  class=" form-control"/>
                            </div>

                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="窨井用途：">窨井用途：</label>
                                <form:select path="purpose"  class="form-control m-b">
                                    <form:option value="" label=""/>
                                    <form:options items="${fns:getDictList('cover_purpose')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>



                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="采集人：">采集人：</label>
                                <sys:userselect id="createBy" name="createBy.id" value="${cover.createBy.id}" labelName="createBy.name" labelValue="${cover.createBy.name}"
                                                cssClass="form-control required"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="创建时间：">&nbsp;采集时间：</label>
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

                <shiro:hasPermission name="cv:equinfo:coverAudit:batchPass">
                    <button id="batchPass" class="btn btn-danger" disabled onclick="batchPass()">
                        <i class="glyphicon glyphicon-edit"></i> 批量通过
                    </button>
                </shiro:hasPermission>
                <shiro:hasPermission name="cv:equinfo:coverAudit:batchReject">
                    <button id="batchReject" class="btn btn-danger" disabled onclick="batchReject()">
                        <i class="glyphicon glyphicon-edit"></i> 批量驳回
                    </button>
                </shiro:hasPermission>


                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>

            <!-- 表格 -->
            <table id="coverAuditTable"   data-toolbar="#toolbar"></table>

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
