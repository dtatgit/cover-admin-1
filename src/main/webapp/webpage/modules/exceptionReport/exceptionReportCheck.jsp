<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <meta name="decorator" content="ani"/>
    <link href="${ctxStatic}/common/fonts/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <script src="http://webapi.amap.com/maps?v=1.4.6&key=06de357afd269944d97de0abcde0f4e0"></script>
    <!-- Bootstrap -->
    <link href="https://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="${ctxStatic}/plugin/imagesPlug/jquery.magnify.js"></script>
    <link href="${ctxStatic}/plugin/imagesPlug/jquery.magnify.css" rel="stylesheet">
    <script src="${ctxStatic}/plugin/jquery-validation\1.14.0/jquery.validate.js"></script>
    <title>异常上报管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var coverAppUrl = '${coverAppUrl}';
        $(document).ready(function () {
            $("#inputForm").validate({
                submitHandler: function (form) {
                    jp.loading();
                    form.submit();
                },
                messages: {
                    checkStatus: {required: "不能为空！"},
                    adress: {required: "站点地址不能为空."}
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
            /*$('input[type=radio][name=checkStatus]').change(function() {
                alert(this.value);
            });*/
        });
    </script>
    <style type="text/css">
        .images-view {
            margin: 0;
            padding: 0;
        }

        .images-view li {
            float: left;
            margin-right: 10px;
            width: 200px;
            height: 200px;
            position: relative;
            border: 1px solid #e5e5e5;
        }

        .images-view li a img {
            position: absolute;
            top: 50%;
            left: 0;
            transform: translateY(-50%);
            width: 100%;
        }

        .table-radio span {
            margin-right: 15px;
        }

        .table-radio span label {
            margin-left: 3px;
        }
    </style>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <a class="panelButton" href="${ctx}/cb/report/exceptionReport"><i class="ti-angle-left"></i> 返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="exceptionReport"
                               action="${ctx}/cb/report/exceptionReport/checkReport" method="post"
                               class="form-horizontal">
                        <form:hidden path="id"/>
                        <table class="table table-bordered">
                            <tbody>
                            <tr>
                                <td class="width-15 active"><label class="pull-right">异常上报人：</label></td>
                                <td class="width-35">
                                        ${exceptionReport.createByName}
                                </td>
                                <td class="width-15 active"><label class="pull-right">异常上报时间：</label></td>
                                <td class="width-35">
                                    <fmt:formatDate value="${exceptionReport.createDate}"
                                                    pattern="yyyy-MM-dd hh:mm:ss"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-15 active"><label class="pull-right">异常区域：</label></td>
                                <td class="width-35">
                                        ${exceptionReport.address}
                                </td>
                                <td class="width-15 active"><label class="pull-right">异常描述：</label></td>
                                <td class="width-35">
                                        ${exceptionReport.remarks}
                                </td>
                            </tr>
                            <tr>
                                <td class="width-15 active"><label class="pull-right">上报照片：</label></td>
                                <td class="width-35" colspan="3">
                                    <c:if test="${exceptionReport.imageList !=null}">
                                        <ul class="images-view">
                                            <c:forEach items="${exceptionReport.imageList}" var="item">
                                                <li>
                                                    <a data-magnify="gallery" href="${coverAppUrl}/sys/file/download/${item}">
                                                        <img src="${coverAppUrl}/sys/file/download/${item}" alt="">
                                                    </a>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </c:if>
                                </td>
                            </tr>
                           <%-- <tr>
                                <td class="width-15 active"><label class="pull-right">上报照片：</label></td>
                                <td class="width-35" colspan="3">
                                    <c:forEach items="${exceptionReport.imageList}" var="image">
                                        <a data-magnify="gallery" href="${coverAppUrl}/sys/file/download/${image}">
                                            <img src="${coverAppUrl}/sys/file/download/${image}" alt="" style="width:260px;">
                                        </a>
                                    </c:forEach>
                                </td>
                            </tr>--%>
                            <tr>
                                <td class="width-15 active"><label class="pull-right">异常区域：</label></td>
                                <td class="width-35 table-radio" colspan="3">
                                    <form:radiobuttons class="i-checks required" path="checkStatus"
                                                       items="${fns:getDictList('pass_or_not')}" id="checkStatus"
                                                       itemLabel="label" itemValue="value" htmlEscape="false"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-15 active"><label class="pull-right">不通过原因：</label></td>
                                <td class="width-35" colspan="3">
                                    <form:textarea path="passNotReason" htmlEscape="false" rows="4"
                                                   class="form-control "/>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <sys:message content="${message}"/>
                        <%--			<div class="form-group">--%>
                        <%--				<label class="label-item single-overflow pull-left" title="审核结果：">审核结果：</label>--%>
                        <%--				<div class="col-xs-12">--%>
                        <%--					<form:radiobuttons class="i-checks required" path="checkStatus" items="${fns:getDictList('pass_or_not')}" id="checkStatus" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
                        <%--				</div>--%>
                        <%--			</div>--%>
                        <%--				<div class="form-group">--%>
                        <%--					<label class="col-sm-2 control-label">不通过原因：</label>--%>
                        <%--					<div class="col-sm-10">--%>
                        <%--						<form:textarea path="passNotReason" htmlEscape="false" rows="4"    class="form-control "/>--%>
                        <%--					</div>--%>
                        <%--				</div>--%>
                        <%--<div class="form-group">
                            <label class="col-sm-2 control-label">生成工单类型：</label>
                            <div class="col-sm-10">
                                <form:select path="workType" class="form-control ">
                                    &lt;%&ndash;	<form:option value="" label=""/>&ndash;%&gt;
                                    <form:options items="${fns:getDictList('work_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>
                        </div>--%>
                        <c:if test="${fns:hasPermission('report:exceptionReport:check') || isCheck}">
                            <div class="col-lg-3"></div>
                            <div class="col-lg-6">
                                <div class="form-group text-center">
                                    <div>
                                        <button class="btn btn-primary btn-block btn-lg btn-parsley"
                                                data-loading-text="正在提交...">提 交
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>