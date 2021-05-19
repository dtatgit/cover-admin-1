<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>工单响应排名</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="/webpage/include/echarts4.jsp" %>
    <%@ include file="workRankingStatisList.js" %>
    <style type="text/css">
        .fixed-table-footer .table {
            table-layout: fixed
        }

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

        .form-horizontal {
            margin: 0;
        }

        .form-horizontal .form-group {
            margin-left: 0;
            margin-right: 0;
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
            <h3 class="panel-title">工单响应排名</h3>
        </div>
        <div class="panel-body">
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="officeOwnerStatis"
                                   class="form form-horizontal well clearfix">
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="井盖类型：">井盖类型：</label>
                                <form:select path="statisMethods"  class="form-control m-b">
                                    <form:option value="ownerDepart" label="权属单位"/>
                                    <form:option value="office" label="部门"/>
                                </form:select>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <div class="form-group">
                                    <label class="label-item single-overflow pull-left"
                                           title="统计时间：">统计时间：</label>
                                    <div class="col-xs-12">
                                        <div class="col-xs-12 col-sm-5">
                                            <div class='input-group date' id='beginDate' style="left: -10px;">
                                                <input type='text' name="statisTime" class="form-control"/>
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

            <!-- 工具栏 -->
            <div id="toolbar">
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2"
                   href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- /工具栏 -->

            <div class="row common-m-t">
                <div class="col-sm-6 list-main">
                    <!-- 图表 -->
                    <div id="container" class="charts-container"></div>
                    <!-- /图表 -->
                </div>
                <div class="col-sm-6 list-main">
                    <!-- 列表 -->
                    <table id="officeOwnerStatisTable" class="table text-nowrap" style="table-layout:fixed"></table>
                    <!-- /列表 -->
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>