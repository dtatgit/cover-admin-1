<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>项目详情</title>
    <meta name="decorator" content="ani"/>
    <link href="${ctxStatic}/common/fonts/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet"
          type="text/css"/>
    <!-- Bootstrap -->
    <%--<link href="https://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">--%>
    <link href="${ctxStatic}/plugin/bootstrap/bootstrap.min.css" rel="stylesheet">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <%--<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>--%>
    <script src="${ctxStatic}/plugin/jquery/jquery.min.js"></script>
    <script src="${ctxStatic}/plugin/bootstrap/bootstrap.min.js"></script>
    <script src="${ctxStatic}/plugin/imagesPlug/jquery.magnify.js"></script>
    <link href="${ctxStatic}/plugin/imagesPlug/jquery.magnify.css" rel="stylesheet">
    <script src="${ctxStatic}/plugin/jquery-validation\1.14.0/jquery.validate.js"></script>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-primary">
                <div class="panel-body">
                        <table class="table table-bordered">
                            <tbody>
                            <tr>
                                <td class="width-15 active"><label class="pull-right">项目编号:</label></td>
                                <td class="width-35">
                                        ${projectInfo.projectNo}
                                </td>
                                <td class="width-15 active"><label class="pull-right">项目名称：</label></td>
                                <td class="width-35">
                                        ${projectInfo.projectName}
                                </td>
                            </tr>
                            <tr>
                                <td class="width-15 active"><label class="pull-right">项目状态：</label></td>
                                <td class="width-35">
                                    ${projectInfo.status}
                                </td>
                                <td class="width-15 active"><label class="pull-right">备注：</label></td>
                                <td class="width-35">
                                        ${projectInfo.remarks}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>