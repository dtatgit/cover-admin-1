<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>异常上报管理</title>
    <meta name="decorator" content="ani"/>
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

        .images-view li img {
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
    <script type="text/javascript">
        var coverAppUrl = '${coverAppUrl}';
        $(document).ready(function () {
            $("#inputForm").validate({
                submitHandler: function (form) {
                    jp.loading();
                    form.submit();
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

        });
    </script>
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
                               action="${ctx}/cb/report/exceptionReport/save" method="post" class="form-horizontal">
                        <form:hidden path="id"/>
                        <sys:message content="${message}"/>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">工单编号：</label>
                            <div class="col-sm-10">
                                <form:input path="coverWorkNo" htmlEscape="false" class="form-control "
                                            readonly="true"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">上报人：</label>
                            <div class="col-sm-10">
                                <form:input path="createByName" htmlEscape="false" class="form-control "
                                            readonly="true"/>
                            </div>
                        </div>
                       <%-- <div class="form-group">
                            <label class="col-sm-2 control-label">上报时间：</label>
                            <div class="col-sm-10">
								<form:input path="createDate" htmlEscape="false" class="form-control " readonly="true"/>
                            </div>
                        </div>--%>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">审核状态：</label>
                            <div class="col-sm-10">
                                <form:select path="checkStatus" class="form-control " disabled="true">
                                    <form:option value="" label=""/>
                                    <form:options items="${fns:getDictList('exception_report_status')}"
                                                  itemLabel="label" itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">异常上报照片：</label>
                            <div class="col-sm-10">
                                    <%--<c:if test="${exceptionReport.imageList !=null}">
                                        <c:forEach items="${exceptionReport.imageList}" var="item">
                                            <img src="${coverAppUrl}/sys/file/download/${item}" style="width:300px;"/>
                                        </c:forEach>
                                    </c:if>--%>
                                <c:if test="${exceptionReport.imageList !=null}">
                                    <ul class="images-view">
                                        <c:forEach items="${exceptionReport.imageList}" var="item">
                                            <li>
                                                <img src="${coverAppUrl}/sys/file/download/${item}"/>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">所在区域：</label>
                            <div class="col-sm-10">
                                <form:input path="address" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">备注信息：</label>
                            <div class="col-sm-10">
                                <form:textarea path="remarks" htmlEscape="false" rows="4" class="form-control "/>
                            </div>
                        </div>
						<c:if test="${exceptionReport.checkStatus eq '2'}">
							<div class="form-group">
								<label class="col-sm-2 control-label">未通过原因：</label>
								<div class="col-sm-10">
									<form:textarea path="passNotReason" htmlEscape="false" rows="4" class="form-control "/>
								</div>
							</div>
						</c:if>
                        <c:if test="${fns:hasPermission('report:exceptionReport:edit') || isAdd}">
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