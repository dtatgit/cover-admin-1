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
    <link href="${ctxStatic}/plugin/bootstrap/bootstrap.min.css" rel="stylesheet">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="${ctxStatic}/plugin/jquery/jquery.min.js"></script>
    <script src="${ctxStatic}/plugin/bootstrap/bootstrap.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="${ctxStatic}/plugin/imagesPlug/jquery.magnify.js"></script>
    <link href="${ctxStatic}/plugin/imagesPlug/jquery.magnify.css" rel="stylesheet">
    <script src="${ctxStatic}/plugin/jquery-validation\1.14.0/jquery.validate.js"></script>

    <%--     引入datetimepicker样式--%>
    <link href="${ctxStatic}/plugin/bootstrap-datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet">
    <%--     引入datetimepicker脚本--%>
    <script src="${ctxStatic}/plugin/bootstrap-datetimepicker/moment-with-locales.min.js"></script>
    <script src="${ctxStatic}/plugin/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js"></script>

    <script type="text/javascript" src="${ctxStatic}/common/js/utils.js"></script>
    <script src="${ctxStatic}/plugin/echarts4/echarts.min.js"></script>
    <script src="${ctxStatic}/plugin/echarts4/macarons.js"></script>
    <%@include file="/webpage/include/treeview.jsp" %>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <link href="${ctxStatic}/common/css/main.css" rel="stylesheet">

    <script>
        $(document).ready(function () {
            $('#startTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss",
                locale: moment.locale('zh-CN')
            });
            $('#endTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss",
                locale: moment.locale('zh-CN')
            });
            $('#startTimeTemp').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss",
                locale: moment.locale('zh-CN')
            });
            $('#endTimeTemp').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss",
                locale: moment.locale('zh-CN')
            });
        })

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

        .input-wrapper {
            display: flex;
            justify-content: flex-end;
            align-items: center;
        }


    </style>
    <script type="text/javascript">
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
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog

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

            $('#coverBellStateTable').bootstrapTable({
                //请求方法
                method: 'get',
                //类型json
                dataType: "json",
                //显示刷新按钮
                showRefresh: true,
                //显示切换手机试图按钮
                showToggle: true,
                //显示 内容列下拉框
                showColumns: true,
                //显示到处按钮
                showExport: false,
                //显示切换分页按钮
                showPaginationSwitch: true,
                //最低显示2行
                minimumCountColumns: 2,
                //是否显示行间隔色
                striped: true,
                //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                cache: false,
                //是否显示分页（*）
                pagination: true,
                //排序方式
                sortOrder: "asc",
                //初始化加载第一页，默认第一页
                pageNumber: 1,
                //每页的记录行数（*）
                pageSize: 10,
                //可供选择的每页的行数（*）
                pageList: [10, 25, 50, 100],
                //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
                url: "${ctx}/cb/equinfo/coverBellState/dataThird",
                //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
                //queryParamsType:'',
                ////查询参数,每次调用是会带上这个参数，可自定义
                queryParams: function (params) {
                    var searchParam = {};//$("#searchForm").serializeJSON();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    var devId = $("#bellNoId").val();
                    searchParam.devNo = devId;
                    return searchParam;
                },
                //分页方式：client客户端分页，server服务端分页（*）
                sidePagination: "server",
                contextMenuTrigger: "right",//pc端 按右键弹出菜单
                contextMenuTriggerMobile: "press",//手机端 弹出菜单，click：单击， press：长按。
                contextMenu: '#context-menu',
                onContextMenuItem: function (row, $el) {

                },

                onClickRow: function (row, $el) {
                },
                columns: [{
                    field: 'batteryVoltage',
                    title: '电压值(V)',
                    sortable: true

                }
                    , {
                        field: 'waterLevel',
                        title: '水位值(M)',
                        sortable: true

                    }
                    , {
                        field: 'temperature',
                        title: '温度值(℃)',
                        sortable: true

                    }
                    , {
                        field: 'rssi',
                        title: '信号值',
                        sortable: true

                    }
                    , {
                        field: 'angle',
                        title: '角度(度)',
                        sortable: true

                    }
                    , {
                        field: 'createDate',
                        title: '上报时间',
                        sortable: true

                    }

                ]

            });


            /*$('#auditTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });*/
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
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">井卫设备信息列表</h3>
        </div>
        <body class="bg-white">

        <form:form id="inputForm" modelAttribute="coverBell" class="form-horizontal">
            <form:hidden path="id"/>
            <input type="hidden" id="longId" value="${coverBell.cover.longitude}"/>
            <input type="hidden" id="latId" value="${coverBell.cover.latitude}"/>
            <input type="hidden" id="bellNoId" value="${coverBell.bellNo}">
            <%--<input type="hidden" id="showFlag" value="${cover.isDamaged}"/>--%>
            <sys:message content="${message}"/>
            <div class="details-header">
                <span>井卫管理</span>
                <span class="division">/</span>
                <span>井卫详情</span>
            </div>
            <div class="details-content">
                <div class="details-view">
                    <h1 class="view-title">井卫IMEI码：${coverBell.imei}</h1>
                    <div class="view-form">
                        <ul>
                            <li><label>井卫编号：</label><span>${coverBell.bellNo}</span></li>
                            <li><label>井卫型号：</label><span>${coverBell.bellModel}</span></li>
                            <li><label>固件版本号：</label><span>${coverBell.version}</span></li>
                            <li><label>IMEI：</label><span>${coverBell.imei}</span></li>
                            <li><label>SIM：</label><span>${coverBell.sim}</span></li>
                            <li>
                                <label>设备类型：</label><span>${fns:getDictLabel (coverBell.bellType, " bellType", "--")}</span>
                            </li>
                            <li>
                                <label>工作状态：</label><span>${fns:getDictLabel (coverBell.workStatus, "bell_work_status", "--")}</span>
                            </li>
                            <li>
                                <label>生命周期：</label><span>${fns:getDictLabel (coverBell.bellStatus, "bell_status", "--")}</span>
                            </li>
                            <li>
                                <label>设防状态：</label><span>${fns:getDictLabel (coverBell.defenseStatus, "defense_status", "--")}</span>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="details-view">
                    <h1 class="view-title">井卫工作参数</h1>
                    <div class="view-form">
                        <ul>
                            <c:if test="${empty coverBell.paramList}">
                                <li><label>设备编号不存在</label></li>
                            </c:if>
                            <c:forEach items="${coverBell.paramList}" var="item">
                                <li><label>${item.name}：</label><span>${item.value}</span></li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="details-footer">
                <a id="add" class="common-btn common-btn-primary" onclick="">返回</a>
            </div>
            <%-- <div class="examinebox">
                 <h1 class="title2">通讯记录</h1>
                 <c:if test="${coverBell.id !=null && coverBell.id !=''}">
                     <table id="coverBellStateTable"   data-toolbar="#toolbar"></table>
                 </c:if>
             </div>--%>
        </form:form>
        <script>
        </script>
        </body>
    </div>
</div>
</html>
