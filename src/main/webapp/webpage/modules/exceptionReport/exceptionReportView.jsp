<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>异常上报管理</title>
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
    <script type="text/javascript">
        var coverAppUrl = '${coverAppUrl}';
        $(document).ready(function () {

        });
    </script>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-primary">
               <%-- <div class="panel-heading">
                    <h3 class="panel-title">
                        <a class="panelButton" href="${ctx}/cb/report/exceptionReport"><i class="ti-angle-left"></i> 返回</a>
                    </h3>
                </div>--%>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="exceptionReport"
                               action="${ctx}/cb/report/exceptionReport/checkReport" method="post"
                               class="form-horizontal">
                        <form:hidden path="id"/>
                        <table class="table table-bordered">
                            <tbody>
                            <tr>
                                <td class="width-15 active"><label class="pull-right">工单编号：</label></td>
                                <td class="width-35">
                                        ${exceptionReport.coverWorkNo}
                                </td>
                                <td class="width-15 active"><label class="pull-right">上报人：</label></td>
                                <td class="width-35">
                                        ${exceptionReport.createByName}
                                </td>
                            </tr>
                            <tr>
                                <td class="width-15 active"><label class="pull-right">上报时间：</label></td>
                                <td class="width-35">
                                    <fmt:formatDate value="${exceptionReport.createDate}"
                                                    pattern="yyyy-MM-dd hh:mm:ss"/>
                                </td>
                                <td class="width-15 active"><label class="pull-right">区域：</label></td>
                                <td class="width-35">
                                        ${exceptionReport.address}
                                </td>
                            </tr>
                            <tr>
                                <td class="width-15 active"><label class="pull-right">上报照片：</label></td>
                                <td class="width-35" colspan="3">
                                    <c:if test="${exceptionReport.imageList !=null}">
                                        <ul class="images-view">
                                            <c:forEach items="${exceptionReport.imageList}" var="item">
                                                <li>
                                                    <a data-magnify="gallery"
                                                       href="${coverAppUrl}/sys/file/download/${item}">
                                                        <img src="${coverAppUrl}/sys/file/download/${item}" alt="">
                                                    </a>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </c:if>
                                        <%--						&lt;%&ndash;<c:if test="${exceptionReport.imageList !=null}">--%>
                                        <%--							<c:forEach items="${exceptionReport.imageList}" var="item">--%>
                                        <%--								<img src="${coverAppUrl}/sys/file/download/${item}" style="width:300px;"/>--%>
                                        <%--							</c:forEach>--%>
                                        <%--						</c:if>&ndash;%&gt;--%>
                                        <%--							<div class="container imgsbox">--%>
                                        <%--								<div class="image-set">--%>
                                        <%--									<c:forEach items="${exceptionReport.imageList}" var="image">--%>
                                        <%--										<a data-magnify="gallery" href="${coverAppUrl}/sys/file/download/${image}">--%>
                                        <%--											<img  src="${coverAppUrl}/sys/file/download/${image}" alt="">--%>
                                        <%--										</a>--%>
                                        <%--									</c:forEach>--%>
                                        <%--								</div>--%>
                                        <%--							</div>--%>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-15 active"><label class="pull-right">异常描述：</label></td>
                                <td class="width-35" colspan="3">
                                        ${exceptionReport.remarks}
                                </td>
                            </tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">审核结果：</label></td>
								<td class="width-35">
									<c:if test="${exceptionReport.checkStatus eq '0'}">待审核</c:if>
									<c:if test="${exceptionReport.checkStatus eq '1'}">已通过</c:if>
									<c:if test="${exceptionReport.checkStatus eq '2'}">未通过</c:if>
								</td>
								<td class="width-15 active"><label class="pull-right">审核人：</label></td>
								<td class="width-35">
										${exceptionReport.checkByName}
								</td>
							</tr>
                            <tr>
                                <td class="width-15 active"><label class="pull-right">不通过原因：</label></td>
                                <td class="width-35" colspan="3">
                                        ${exceptionReport.passNotReason}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <sys:message content="${message}"/>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>