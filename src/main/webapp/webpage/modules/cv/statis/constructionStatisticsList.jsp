<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>施工信息统计</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="/webpage/include/echarts4.jsp" %>
    <%@ include file="constructionStatisticsList.js" %>
    <style type="text/css">

        .fixed-table-footer .table:not(.table-condensed),
        .fixed-table-footer .table:not(.table-condensed) > tbody > tr > th,
        .fixed-table-footer .table:not(.table-condensed) > tfoot > tr > th,
        .fixed-table-footer .table:not(.table-condensed) > thead > tr > td,
        .fixed-table-footer .table:not(.table-condensed) > tbody > tr > td,
        .fixed-table-footer .table:not(.table-condensed) > tfoot > tr > td {
            padding: 0 !important;
        }

        .list-main {
            height: 450px;
        }

        .common-m-t {
            margin-top: 15px;
        }

        .form-horizontal {
            margin: 0;
        }

        .well {
            min-height: 20px;
            padding: 20px;
            margin-bottom: 15px;
            background-color: #f5f5f5;
            border: 1px solid #e3e3e3;
            border-radius: 0;
        }
    </style>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">施工信息统计</h3>
        </div>
        <div class="panel-body">

            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="" class="form form-horizontal well clearfix">
<%--                            <div class="col-xs-12 col-sm-6 col-md-4">--%>
<%--                                <label class="label-item single-overflow pull-left" title="工单类型：">工单类型：</label>--%>
<%--                                <form:select path="workType" class="form-control m-b">--%>
<%--                                    <form:option value="" label=""/>--%>
<%--                                    <form:options items="${fns:getDictList('work_type')}" itemLabel="label"--%>
<%--                                                  itemValue="value" htmlEscape="false"/>--%>
<%--                                </form:select>--%>
<%--                            </div>--%>
<%--                            <div class="col-xs-12 col-sm-6 col-md-4">--%>
<%--                                <label class="label-item single-overflow pull-left" title="工单状态：">工单状态：</label>--%>
<%--                                <form:select path="workStatus" class="form-control m-b">--%>
<%--                                    <form:option value="" label=""/>--%>
<%--                                    <form:options items="${fns:getDictList('work_status')}" itemLabel="label"--%>
<%--                                                  itemValue="value" htmlEscape="false"/>--%>
<%--                                </form:select>--%>
<%--                            </div>--%>
<%--                            <div class="col-xs-12 col-sm-6 col-md-4">--%>
<%--                                <label class="label-item single-overflow pull-left" title="超时工单：">超时工单：</label>--%>
<%--                                <form:select path="overtime" class="form-control m-b">--%>
<%--                                    <form:option value="" label=""/>--%>
<%--                                    <form:options items="${fns:getDictList('work_status')}" itemLabel="label"--%>
<%--                                                  itemValue="value" htmlEscape="false"/>--%>
<%--                                </form:select>--%>
<%--                            </div>--%>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <div class="form-group">
                                    <label class="label-item single-overflow pull-left"
                                           title="工单生成时间：">工单生成时间：</label>
                                    <div class="col-xs-12">
                                        <div class="col-xs-12 col-sm-5">
                                            <div class='input-group date' id='beginDateCreateTime' style="left: -10px;">
                                                <input type='text' name="beginDateCreateTime" class="form-control"/>
                                                <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
                                            </div>
                                        </div>
                                        <div class="col-xs-12 col-sm-1">
                                            ~
                                        </div>
                                        <div class="col-xs-12 col-sm-5">
                                            <div class='input-group date' id='endDateCreateTime' style="left: -10px;">
                                                <input type='text' name="endDateCreateTime" class="form-control"/>
                                                <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
<%--                            <div class="col-xs-12 col-sm-6 col-md-4">--%>
<%--                                <label class="label-item single-overflow pull-left" title="权属单位：">权属单位：</label>--%>
<%--                                <form:select path="ownerDepart" class="form-control m-b">--%>
<%--                                    <form:option value="" label=""/>--%>
<%--                                    <form:options items="${fns:getDictList('work_status')}" itemLabel="label"--%>
<%--                                                  itemValue="value" htmlEscape="false"/>--%>
<%--                                </form:select>--%>
<%--                            </div>--%>
<%--                            <div class="col-xs-12 col-sm-6 col-md-4">--%>
<%--                                <label class="label-item single-overflow pull-left" title="处理单位：">处理单位：</label>--%>
<%--                                <form:select path="processingDepart" class="form-control m-b">--%>
<%--                                    <form:option value="" label=""/>--%>
<%--                                    <form:options items="${fns:getDictList('work_status')}" itemLabel="label"--%>
<%--                                                  itemValue="value" htmlEscape="false"/>--%>
<%--                                </form:select>--%>
<%--                            </div>--%>
<%--                            <div class="col-xs-12 col-sm-6 col-md-4">--%>
<%--                                <label class="label-item single-overflow pull-left" title="处理人：">处理人：</label>--%>
<%--                                <form:select path="user" class="form-control m-b">--%>
<%--                                    <form:option value="" label=""/>--%>
<%--                                    <form:options items="${fns:getDictList('work_status')}" itemLabel="label"--%>
<%--                                                  itemValue="value" htmlEscape="false"/>--%>
<%--                                </form:select>--%>
<%--                            </div>--%>
<%--                            <div class="col-xs-12 col-sm-6 col-md-4">--%>
<%--                                <label class="label-item single-overflow pull-left" title="街道：">街道：</label>--%>
<%--                                <form:input path="street" class="form-control"/>--%>
<%--                            </div>--%>
<%--                            <div class="col-xs-12 col-sm-6 col-md-4">--%>
<%--                                <label class="label-item single-overflow pull-left" title="道路：">道路：</label>--%>
<%--                                <form:input path="road" class="form-control"/>--%>
<%--                            </div>--%>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <div class="form-group">
                                    <label class="label-item single-overflow pull-left"
                                           title="处理时间：">处理时间：</label>
                                    <div class="col-xs-12">
                                        <div class="col-xs-12 col-sm-5">
                                            <div class='input-group date' id='beginDateProcessing' style="left: -10px;">
                                                <input type='text' name="beginStatisticsDate" class="form-control"/>
                                                <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
                                            </div>
                                        </div>
                                        <div class="col-xs-12 col-sm-1">
                                            ~
                                        </div>
                                        <div class="col-xs-12 col-sm-5">
                                            <div class='input-group date' id='endDateProcessing' style="left: -10px;">
                                                <input type='text' name="endStatisticsDate" class="form-control"/>
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
            <!-- /搜索 -->

            <!-- 工具栏 -->
            <div id="toolbar">
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2"
                   href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- /工具栏 -->

            <div class="row common-m-t">
                <div class="col-sm-12 list-main">
                    <table id="constructionStatisticsTable"></table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
