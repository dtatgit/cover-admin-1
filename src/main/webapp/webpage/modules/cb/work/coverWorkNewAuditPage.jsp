<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>工单审核信息管理</title>
    <meta name="decorator" content="ani"/>
    <link href="${ctxStatic}/common/fonts/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet"
          type="text/css"/>
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


    <link href="${ctxStatic}/plugin/jquery-upload/css/jquery.upload.css" type="text/css" rel="stylesheet" />

    <script>
        $('[data-magnify]').magnify({
            headToolbar: [
                'minimize',
                'maximize',
                'close'
            ],
            footToolbar: [
                //'prev',
                //'next',
                'zoomIn',
                'zoomOut',
                //'fullscreen',
                //'actualSize',
                'rotateLeft',
                'rotateRight'
            ],
            modalWidth: 400,
            modalHeight: 400
        });

    </script>
    <style>
        .nav-tabs {
            clear: both;
            overflow: hidden;
            margin: 0;
            padding: 0;
            border: 0;
            position: relative;
            top: 1px;
        }

        .nav-tabs li a {
            margin-right: 0px;
            line-height: 1.42857143;
            border: 1px solid #ddd;
            border-radius: 0;
            background-color: #fff;
            padding: 5px 10px;
            border-right: none;
            color: #555;
            display: block
        }

        .nav-tabs li:last-child a {
            border-right: 1px solid #ddd;
        }

        .nav-tabs > li.active > a, .nav-tabs > li.active > a:hover, .nav-tabs > li.active > a:focus {
            color: #3ca2e0;
            border-top: 2px solid #3ca2e0;
        }


    </style>
    <script type="text/javascript">

        var proPath = "${ctx}";

        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            if (validateForm.form()) {
                $table = table;
                $topIndex = index;
                jp.loading();
                $("#inputForm").submit();
                return true;
            }

            return false;
        }
        function resList() {
            window.location.href="${ctx}/cb/work/coverWork/list";
        }

        function auditFunc(val) {
            $('#operationStatus').val(val);
            $('#inputForm').submit();
        }
        $(document).ready(function () {

            $(".nav-tabs").on("click", "a", function () {
                $(".nav-tabs li").removeClass("active");
                $(this).parent().addClass("active");
                var title = $(this).attr("title");
                $(".panel").hide();
                $("#" + title).show();
            });

            validateForm = $("#inputForm").validate({
                submitHandler: function (form) {
                    jp.post("${ctx}/cb/work/coverWork/saveAudit", $('#inputForm').serialize(), function (data) {
                        if (data.success) {
                            //$table.bootstrapTable('refresh');
                            resList();
                            /*jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog*/

                        } else {
                            jp.error(data.msg);
                        }
                    })
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

            $('#auditTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
        });

    </script>

    <script type="text/javascript">
        $(document).ready(function () {
            var flag = $("#showFlag").val();
            if (flag == "Y") {

                $("#damagedId").removeAttr("hidden");
            } else {

                $("#damagedId").attr("hidden", 'hidden');

            }
        });
    </script>

</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="coverWork" class="common-p-all-small">
    <form:hidden path="id"/>
    <form:hidden id="operationStatus"  path="operationStatus"/>
    <form:hidden path="constructionDepart.id"/>
    <input type="hidden" id="longId" value="${cover.longitude}"/>
    <input type="hidden" id="latId" value="${cover.latitude}"/>
    <%--<input type="hidden" id="showFlag" value="${cover.isDamaged}"/>--%>
    <sys:message content="${message}"/>
    <div class="details-header">
        <span>工单管理</span>
        <span class="division">/</span>
        <span>工单详情</span>
    </div>
    <div class="details-content">
        <div class="details-view is-border">
            <h1 class="view-title">工单信息</h1>
            <div class="view-form">
                <ul>
                    <li><label>工单编号：</label><span>${coverWork.workNum}</span></li>
                    <li><label>工单类型：</label><span>${fns:getDictLabel (coverWork.workType, "work_type", "--")}</span>
                    </li>
                        <%--<li><label>工单状态:</label><span>${fns:getDictLabel (coverWork.workStatus, "work_status", "--")}</span></li>--%>
                    <li><label>紧急程度：</label><span>${fns:getDictLabel (coverWork.workLevel, "work_level", "--")}</span>
                    </li>
                    <li><label>工单状态： </label><span>${coverWork.workStatus}</span></li>
                    <li><label>井盖编号：</label><span>${cover.no}</span></li>
                    <li><label>扩展编号：</label><span>${cover.tagNo}</span></li>
                    <li><label>创建时间：</label><span><fmt:formatDate value="${coverWork.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span></li>
                    <li><label>最迟完成时间：</label><span><fmt:formatDate value="${coverWork.latestCompleteDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span></li>
                    <li><label>工单详细说明：</label><span>${coverWork.remarks}</span></li>
                </ul>
            </div>
        </div>

        <div class="details-view is-border">
            <%--<h1 class="view-title">工单信息</h1>--%>
            <div class="view-form">
                <ul>
                    <li><label>创建人账号：</label><span>${coverWork.createBy.loginName}</span></li>
                    <li><label>创建人姓名：</label><span>${coverWork.createBy.name}</span>
                    </li>
                    <li><label>创建人电话：</label><span>${coverWork.createBy.mobile}</span>
                    </li>
                </ul>
            </div>
        </div>

        <div class="details-view is-border">
                <%--<h1 class="view-title">工单信息</h1>--%>
            <div class="view-form">
                <ul>
                    <li><label>处理人账号：</label><span>${coverWork.constructionUser.loginName}</span></li>
                    <li><label>处理人姓名：</label><span>${coverWork.constructionUser.name}</span>
                    </li>
                    <li><label>处理人电话：</label><span>${coverWork.constructionUser.mobile}</span>
                    </li>
                </ul>
            </div>
        </div>

        <div class="details-view is-border">
            <div id="czjl" class="panel panel-primary" style="display: block;">
                <table class="table table-ullist">
                    <tr><td class="width-10 active">流程信息</td><td class="width-10 active">操作信息</td><%--<td class="width-10 active">原状态</td><td class="width-10 active">结果状态</td>--%><td class="width-10 active">操作人</td><td class="width-10 active">操作部门</td><td class="width-10 active">操作时间</td><%--<td class="width-10 active">目标部门</td>--%>
                        <td class="width-10 active">图片</td><td class="width-10 active">备注</td></tr>
                    <c:forEach items="${flowOptList}" var="operation">
                        <tr>
                            <td>${operation.flowId.flowNo}</td>
                            <td>${operation.optId.optName}</td>
                           <%-- <td>${operation.originState}</td>
                            <td>${operation.resultState}</td>--%>

                            <td>${operation.createBy.name}</td>
                            <td>${operation.optOrg.name}</td>
                            <td><fmt:formatDate value="${operation.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                           <%-- <td>${operation.targetOrg.name}</td>--%>
                            <td>
                                <c:forEach items="${operation.imagesList}" var="item">
                                    <a data-magnify="gallery" data-caption="" href="${item}">
                                        <img  src="${item}" alt="" height="70px">
                                    </a>

                                </c:forEach>
                            </td>
                            <td>${operation.optRemarks}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>

        <div class="details-view is-border">
                <%--<h1 class="view-title">工单信息</h1>--%>
            <%--<div class="view-form">
                <ul>
                    <li><label>审核意见：</label><span>${coverWork.constructionUser.loginName}</span></li>
                    <li><label>处理人姓名：</label><span></span>
                    </li>
                    <li><label>处理人电话：</label><span>${coverWork.constructionUser.mobile}</span>
                    </li>
                </ul>
            </div>--%>
                    <div class="mui-input-row">
                        <label class="t">审核意见：</label>
                        <form:textarea path="operationResult" htmlEscape="false"  rows="3"    class="form-control "/>
                    </div>
                    <div class="upload-box clear">
                        <p class="upload-tip">最多上传3张图片(包含已上传的)，每个图片不能超过1M，向后追加图片，不允许放大</p>
                        <div class="image-box"></div>
                    </div>
        </div>

    <div class="details-footer">
        <a id="approved" class="common-btn common-btn-success" onclick="auditFunc('success')">审核通过</a>
        <a id="reject" class="common-btn common-btn-danger" onclick="auditFunc('fail')">驳回</a>
        <a id="add" class="common-btn common-btn-primary" onclick="resList()">返回</a>
    </div>
</form:form>

<script src="${ctxStatic}/plugin/jquery-upload/js/jquery.upload.js"></script>

<script>

    $(".image-box").ajaxImageUpload({
        fileInput : 'file',
        postUrl : '${ctx}/api/file/uploadimg', //上传的服务器地址
        width : 180,
        height : 180,
        imageUrl: [],
        postData : { category:'work-images' },
        maxNum: 3, //允许上传图片数量
        allowZoom : false, //允许放大
        maxSize : 1, //允许上传图片的最大尺寸，单位M
        appendMethod : 'after',
        before : function () {
            //alert('上传前回调函数2');
        },
        success : function(json){
            //alert('上传成功回调函数2'+json.data);
        },
        complete : function () {
            //alert('全部上传成功2');
        },
        error : function (e) {
            alert(e.msg + '(' + e.code + ')');
        }
    });

</script>
</body>
<script>
</script>
</html>