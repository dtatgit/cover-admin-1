<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>井盖基础信息管理</title>
    <meta name="decorator" content="ani"/>

    <script src="http://webapi.amap.com/maps?v=1.4.6&key=06de357afd269944d97de0abcde0f4e0"></script>
    <script src="${ctxStatic}/plugin/echarts4/echarts.min.js"></script>

    <link href="${ctxStatic}/plugin/bootstrap-datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet">

    <script src="${ctxStatic}/plugin/bootstrap-datetimepicker/moment-with-locales.min.js"></script>
    <script src="${ctxStatic}/plugin/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js"></script>

    <link href="${ctxStatic}/common/iconFonts/iconfont.css" rel="stylesheet" type="text/css"/>


    <script src="${ctxStatic}/plugin/imagesPlug/jquery.magnify.js"></script>
    <link href="${ctxStatic}/plugin/imagesPlug/jquery.magnify.css" rel="stylesheet">


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


    <script type="text/javascript">

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

    <script type="text/javascript">
        $(function () {
            $('.startTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss",
                locale: moment.locale('zh-CN')
            });
            $('.endTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss",
                locale: moment.locale('zh-CN')
            });
        });

        // 水位
        function getQueryStr(devNo, startTime, endTime) {
            return '?devNo=' + devNo + '&startDateTime=' + startTime + '&endDateTime=' + endTime
        }

        function clearText(devNo) {
            $("#startTime_" + devNo).val("");
            $("#endTime_" + devNo).val("");
        }

        function searchT(devNo) {

            let startTime = $("#startTime_" + devNo).val();
            let endTime = $("#endTime_" + devNo).val();
            jp.get("${ctx}/cb/equinfo/coverBell/queryDistanceData" + getQueryStr(devNo, startTime, endTime), function (data) {
                let waterSeries = [], waterXAxis = []
                if (data.success) {
                    const temp = data.data
                    if (temp.length > 0) {
                        temp.forEach(function (item) {
                            waterXAxis.push(item.dtime)
                            waterSeries.push(item.distance)
                        })
                    }
                    jp.success(data.msg);
                } else {
                    jp.error(data.msg);
                }

                let waterChart = echarts.init(document.getElementById('water_' + devNo), 'macarons');
                setWaterChart(waterXAxis, waterSeries, waterChart)
            })
        }


        function setWaterChart(waterXAxis, waterSeries, waterChart) {
            const waterOptions = {
                tooltip: {
                    trigger: 'item',
                    formatter: '{b}:{c} '
                },
                xAxis: {
                    type: 'category',
                    data: waterXAxis
                },
                yAxis: {
                    type: 'value'
                },
                dataZoom: [{
                    start: 0,
                    type: "inside"
                }, {
                    start: 0
                }],
                series: [{
                    data: waterSeries,
                    type: 'line',
                    areaStyle: {
                        color: 'rgba(46,199,211,0.7)'
                    }
                }]
            };
            waterChart.setOption(waterOptions);
        }
    </script>
</head>
<body class="bg-white">
<div class="wrapper wrapper-content">
    <div class="details-header">
        <span>井盖基础信息</span>
        <span class="division">/</span>
        <span>井盖详情</span>
    </div>
    <div>
        <div class="details-view is-one">
            <h1 class="view-title">普查信息</h1>
            <div class="details-view-left">
                <div class="view-form row-2">
                    <ul>
                        <li><label>井盖编号：</label><span>${cover.no}</span></li>
                        <li><label>扩展编号：</label><span>${cover.extNum}</span></li>
                        <li><label>地区：</label><span>${cover.province}${cover.city}${cover.district}</span></li>
                        <li><label>详细地址： </label><span>${cover.addressDetail}</span></li>
                        <li><label>环境位置：</label><span>${cover.situation}</span></li>
                        <li><label>窨井用途：</label><span>${cover.purpose}</span></li>
                        <li><label>权属机构：</label><span>${cover.ownerDepart}</span></li>
                        <li><label>管理机构：</label><span>${cover.superviseDepart}</span></li>
                        <li><label>井盖材质：</label><span>${cover.material}</span></li>
                        <li>
                            <label>井盖规格：</label><span>${fns:getDictLabel (cover.sizeRule, "cover_size_rule", "--")}</span>
                        </li>
                        <li><label>损毁情况：</label><span>${fns:getDictLabel (cover.isDamaged, "boolean", "--")}</span></li>
                        <li>
                            <label>井盖照片：</label>
                            <span>
							<div class="view-pic-list">
								<c:forEach items="${cover.coverImageList}" var="images">
<%--                                    <img src="${images.url}" onclick="showImg('${images.url}');" width="100px"--%>
<%--                                         class="img-rounded" alt="">--%>

                                    <a data-magnify="gallery" data-caption="井盖编号：${cover.no}" href="${images.url}">
                                        <img src="${images.url}" alt="">
                                    </a>
                                </c:forEach>
							</div>
						</span>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="details-view-right">
                <div class="view-form row-all">
                    <ul>
                        <li><label>坐标：</label><span>${cover.longitude},${cover.latitude}</span></li>
                        <li>
                            <div>
                                <%--放地图--%>
                                <div id="container" style="height: 280px;width: 100%; border: 1px solid #e5e5e5"></div>
                                <script type="text/javascript">
                                    var map = new AMap.Map('container', {
                                        resizeEnable: true,
                                        zoom: 14,//级别
                                    });

                                    var m1 = new AMap.Icon({
                                        image: '${ctxStatic}/common/images/cover.png',  // Icon的图像
                                        size: new AMap.Size(26, 30),    // 原图标尺寸
                                        imageSize: new AMap.Size(26, 30), //实际使用的大小
                                        offset: new AMap.Pixel(-13, -15),
                                        anchor: 'center'
                                    });

                                    var lng = '${cover.longitude}';
                                    var lat = '${cover.latitude}';
                                    map.setCenter([lng, lat]);

                                    var lnglat = new AMap.LngLat(lng, lat); //一个点
                                    var markericon = m1;
                                    //构建一个标注点
                                    var marker = new AMap.Marker({
                                        icon: markericon,
                                        position: lnglat
                                    });

                                    marker.setMap(map);  //把标注点放到地图上
                                    // map.setCenter([lng, lat]);
                                    //map.setZoom(14);
                                </script>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="common-clear"></div>
        </div>

        <c:forEach var="item" items="${belllist}">
            <c:choose>
                <c:when test="${item.bellType == 'normal'}">
                    <div class="details-view is-one">
                        <div class="pic-text-title">
                            <h3>井卫IMEI：${item.imei}</h3>
                            <div>
                                <shiro:hasPermission name="cb:equinfo:cover:untying">
                                    <button class="common-btn common-btn-danger" type="button"
                                            style="padding: 3px 12px;" onclick="unbindGuard('${item.id}');">解除井卫
                                    </button>
                                </shiro:hasPermission>
                            </div>
                        </div>
                        <div class="pic-text-module">
                            <ul class="pic-text-list">
                                <li>
                                    <div class="list-left">
                                        <div class="icons fc-1">
                                            <i class="iconfont tt-icon-jinggai"></i>
                                        </div>
                                        <div class="title">井卫型号</div>
                                    </div>
                                    <div class="list-right">通用性</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons fc-2">
                                            <i class="iconfont tt-icon-jiekuanzhuangtai"></i>
                                        </div>
                                        <div class="title">在线状态</div>
                                    </div>
                                    <div class="list-right">${fns:getDictLabel (item.workStatus, "bell_work_status", "--")}</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons fc-3">
                                            <i class="iconfont tt-icon-fanghu"></i>
                                        </div>
                                        <div class="title">设防状态</div>
                                    </div>
                                    <div class="list-right">${fns:getDictLabel (item.defenseStatus, "defense_status", "--")}</div>
                                </li>
                            </ul>
                            <ul class="pic-text-list margin-top-large">
                                <li>
                                    <div class="list-left">
                                        <div class="icons fc-4">
                                            <i class="iconfont tt-icon-xuanzhuanjiaodu"></i>
                                        </div>
                                        <div class="title">当前角度</div>
                                    </div>
                                    <div class="list-right">${item.angle}°</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons fc-5">
                                            <i class="iconfont tt-icon-jinggai"></i>
                                        </div>
                                        <div class="title">当前温度</div>
                                    </div>
                                    <div class="list-right">${item.temperature}℃</div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </c:when>
                <c:when test="${item.bellType == 'ranging'}">
                    <div class="details-view is-one">
                        <div class="pic-text-title">
                            <h3>井卫IMEI：${item.imei}</h3>
                            <div>
                                <shiro:hasPermission name="cb:equinfo:cover:untying">
                                    <button class="common-btn common-btn-danger" type="button"
                                            style="padding: 3px 12px;" onclick="unbindGuard('${item.id}');">解绑井卫
                                    </button>
                                </shiro:hasPermission>
                            </div>
                        </div>
                        <div class="pic-text-module">
                            <ul class="pic-text-list">
                                <li>
                                    <div class="list-left">
                                        <div class="icons fc-1">
                                            <i class="iconfont tt-icon-jinggai"></i>
                                        </div>
                                        <div class="title">井卫型号</div>
                                    </div>
                                    <div class="list-right">水距型</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons fc-2">
                                            <i class="iconfont tt-icon-jiekuanzhuangtai"></i>
                                        </div>
                                        <div class="title">在线状态</div>
                                    </div>
                                    <div class="list-right">${fns:getDictLabel (item.workStatus, "bell_work_status", "--")}</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons fc-3">
                                            <i class="iconfont tt-icon-fanghu"></i>
                                        </div>
                                        <div class="title">设防状态</div>
                                    </div>
                                    <div class="list-right">${fns:getDictLabel (item.defenseStatus, "defense_status", "--")}</div>
                                </li>
                            </ul>
                            <ul class="pic-text-list margin-top-large">
                                <li>
                                    <div class="list-left">
                                        <div class="icons fc-4">
                                            <i class="iconfont tt-icon-dixiashuiwei"></i>
                                        </div>
                                        <div class="title">当前水位</div>
                                    </div>
                                    <div class="list-right">${item.waterLevel}m</div>
                                </li>
                            </ul>
                            <ul class="details-search-bar">
                                <li>
                                    <span class="search-title">选择日期：</span>
                                    <div class="search-content">
                                        <div class='input-group date'
                                             style="height: 30px;width: 200px;float:left;margin-left: 5px;">
                                            <input type='text' id='startTime_${item.bellNo}' name="startTime"
                                                   class="form-control startTime"/>
                                            <span class="input-group-addon">
                            					<span class="glyphicon glyphicon-calendar"></span>
                        					</span>
                                        </div>
                                        <div class="search-date-cut" style="float:left;">至</div>
                                        <div class='input-group date' style="height: 30px;width: 200px;float:left;">
                                            <input type='text' id='endTime_${item.bellNo}' name="endTime"
                                                   class="form-control endTime"/>
                                            <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                        </div>
                                    </div>
                                </li>
                                <li>
                                    <a id="search" class="common-btn common-btn-primary is-round small"
                                       onclick="searchT('${item.bellNo}');"><i class="fa fa-search"></i> 查询</a>
                                    <a id="reset" class="common-btn common-btn-primary is-round small"
                                       onclick="clearText('${item.bellNo}');"><i class="fa fa-refresh"></i>
                                        重置</a>
                                </li>
                            </ul>

                            <div class="details-chart-container">
                                <div id="water_${item.bellNo}" style="height:330px;width: 100%"></div>
                            </div>
                            <script>
                                setWaterChart([], [], echarts.init(document.getElementById('water_${item.bellNo}'), 'macarons'))
                            </script>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="details-view is-one">
                        <div class="pic-text-title">
                            <h3>井卫IMEI：${item.imei}【未关联设备类型,默认显示】</h3>
                            <div>
                                <shiro:hasPermission name="cb:equinfo:cover:untying">
                                    <button class="common-btn common-btn-danger" type="button"
                                            style="padding: 3px 12px;" onclick="unbindGuard('${item.id}');">解除井卫
                                    </button>
                                </shiro:hasPermission>
                            </div>
                        </div>
                        <div class="pic-text-module">
                            <ul class="pic-text-list">
                                <li>
                                    <div class="list-left">
                                        <div class="icons fc-1">
                                            <i class="iconfont tt-icon-jinggai"></i>
                                        </div>
                                        <div class="title">井卫型号</div>
                                    </div>
                                    <div class="list-right">通用型</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons fc-2">
                                            <i class="iconfont tt-icon-jiekuanzhuangtai"></i>
                                        </div>
                                        <div class="title">在线状态</div>
                                    </div>
                                    <div class="list-right">${fns:getDictLabel (item.workStatus, "bell_work_status", "--")}</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons fc-3">
                                            <i class="iconfont tt-icon-fanghu"></i>
                                        </div>
                                        <div class="title">设防状态</div>
                                    </div>
                                    <div class="list-right">${fns:getDictLabel (item.defenseStatus, "defense_status", "--")}</div>
                                </li>
                            </ul>
                            <ul class="pic-text-list margin-top-large">
                                <li>
                                    <div class="list-left">
                                        <div class="icons fc-4">
                                            <i class="iconfont tt-icon-xuanzhuanjiaodu"></i>
                                        </div>
                                        <div class="title">当前角度</div>
                                    </div>
                                    <div class="list-right">${item.angle}°</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons fc-5">
                                            <i class="iconfont tt-icon-wendu"></i>
                                        </div>
                                        <div class="title">当前温度</div>
                                    </div>
                                    <div class="list-right">${item.temperature}℃</div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <div class="details-view is-one">
            <h1 class="view-title">历史工单</h1>
            <div class="view-content">
                <table id="coverWorkTable" data-toolbar="#toolbar"></table>
            </div>
        </div>

        <div class="details-footer">
            <shiro:hasPermission name="cv:equinfo:cover:del">
                <button id="del" type="button" class="btn btn-danger">删除井盖</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="cb:equinfo:cover:defense">
                <button id="fortify" class="btn btn-success" type="button">设防</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="cb:equinfo:cover:defense">
                <button id="chefang" class="btn btn-danger" type="button">撤防</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="cb:work:coverWork:add">
                <button id="work" class="btn btn-primary" type="button">创建工单</button>
            </shiro:hasPermission>
            <button onclick="javascript:history.back(-1);" class="btn btn-info " type="button">返回</button>
        </div>
    </div>
</div>
<%@ include file="/webpage/include/bootstraptable.jsp" %>
<script>


    function showImg(img){
        jp.showPic(img);
    }

    <%--//获取井卫数据--%>
    <%--function bellList(coverId){--%>
    <%--    jp.loading();--%>
    <%--    jp.get("${ctx}/cv/equinfo/cover/bellList?coverId=" + coverId, function (data) {--%>
    <%--        if (data.success) {--%>
    <%--            jp.success(data.msg);--%>
    <%--            console.log(window.location.href);--%>
    <%--            console.log(data.data);--%>
    <%--        } else {--%>
    <%--            jp.error(data.msg);--%>
    <%--        }--%>
    <%--    })--%>
    <%--}--%>

    //解绑井卫
    function unbindGuard(bid) {
        jp.confirm('确认要解绑该井卫吗？', function () {
            jp.loading();
            jp.get("${ctx}/cv/equinfo/cover/unbindGuard?bid=" + bid, function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    location.reload();
                } else {
                    jp.error(data.msg);
                }
            })
        })
    }

    $(function () {

        let coverId = "${cover.id}";
        let no = "${cover.no}";

        $("#del").click(function () {
            jp.confirm('确认要删除该井盖吗？', function () {
                jp.loading();
                jp.get("${ctx}/cv/equinfo/cover/delete?id=" + coverId, function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        history.back(-1);
                    } else {
                        jp.error(data.msg);
                    }
                })
            })
        });

        $("#fortify").click(function () {
            jp.confirm('确认要设防吗？', function () {
                jp.loading();
                jp.get("${ctx}/cv/equinfo/cover/fortifySingle?id=" + coverId + "&coverno=" + no, function (data) {
                    if (data.success) {
                        jp.success("设防成功");
                        location.reload();
                    } else {
                        jp.error("设防失败");
                    }
                })

            })
        });

        $("#chefang").click(function () {

            jp.confirm('确认要撤防吗？', function () {
                jp.loading();
                jp.get("${ctx}/cv/equinfo/cover/chefangSingle?id=" + coverId + "&coverno=" + no, function (data) {
                    if (data.success) {
                        jp.success("撤防成功");
                        location.reload();
                    } else {
                        jp.error("撤防失败");
                    }
                })
            })


        });

        $("#work").click(function () {
            jp.openDialog('生成工单', "${ctx}/cv/equinfo/cover/createWorkPageNew?ids=" + coverId + "&coverNos=" + no, '900px', '550px', $("#coverWorkTable"));

        });


        $('#coverWorkTable').bootstrapTable({

            //请求方法
            method: 'get',
            //类型json
            dataType: "json",
            //显示刷新按钮
            showRefresh: true,
            //显示切换手机试图按钮
            showToggle: true,
            //显示 内容列下拉框
            showColumns: false,
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
            url: "${ctx}/cb/work/coverWork/data",
            //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
            //queryParamsType:'',
            ////查询参数,每次调用是会带上这个参数，可自定义
            queryParams: function (params) {
                var searchParam = $("#searchForm").serializeJSON();
                searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                let coverNo = "${cover.no}";
                searchParam.coverNo = coverNo;
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
            columns: [
                {
                    field: 'workNum',
                    title: '工单编号',
                    sortable: true

                }
                , {
                    field: 'workType',
                    title: '工单类型',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('work_type'))}, value, "-");
                    }

                }
                , {
                    field: 'workLevel',
                    title: '紧急程度',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('work_level'))}, value, "-");
                    }

                }
                , {
                    field: 'lifeCycle',
                    title: '工单状态',
                    sortable: true
                }
                , {
                    field: 'constructionUser.name',
                    title: '处理人员',
                    sortable: true

                }
                , {
                    field: 'createDate',
                    title: '创建日期',
                    sortable: true

                }
            ]

        });
    });


</script>
</body>
</html>
