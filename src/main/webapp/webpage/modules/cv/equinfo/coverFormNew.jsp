<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>井盖基础信息管理</title>
    <meta name="decorator" content="ani"/>

    <!-- ZUI 标准版压缩后的 CSS 文件 -->
    <%--	<link rel="stylesheet" href="//cdn.bootcss.com/zui/1.9.2/css/zui.min.css">--%>

    <!-- ZUI Javascript 依赖 jQuery -->
    <%--	<script src="//cdn.bootcss.com/zui/1.9.2/lib/jquery/jquery.js"></script>--%>
    <!-- ZUI 标准版压缩后的 JavaScript 文件 -->
    <%--	<script src="//cdn.bootcss.com/zui/1.9.2/js/zui.min.js"></script>--%>
    <script src="http://webapi.amap.com/maps?v=1.4.6&key=06de357afd269944d97de0abcde0f4e0"></script>
    <script src="${ctxStatic}/plugin/echarts4/echarts.min.js"></script>

    <link href="${ctxStatic}/plugin/bootstrap-datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet">

    <script src="${ctxStatic}/plugin/bootstrap-datetimepicker/moment-with-locales.min.js"></script>
    <script src="${ctxStatic}/plugin/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js"></script>

    <link href="${ctxStatic}/common/iconFonts/iconfont.css" rel="stylesheet" type="text/css"/>
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


    <style>
        .example-popover-static .popover {
            position: relative;
            display: block;
            float: left;
            width: 160px;
            margin: 20px;
            z-index: 0;
        }
    </style>

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
                        <li><label>扩展编号：</label><span>...</span></li>
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
									<a href="${images.url}" data-toggle="lightbox"
                                       data-group="image-group-1">
										<img src="${images.url}" class="img-rounded" alt="">
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
                                <button class="common-btn common-btn-danger" type="button" style="padding: 3px 12px;">解除井卫</button>
                            </div>
                        </div>
                        <div class="pic-text-module">
                            <ul class="pic-text-list row-5">
                                <li>
                                    <div class="list-left">
                                        <div class="icons">
                                            <i class="iconfont tt-icon-jinggai"></i>
                                        </div>
                                        <div class="title">井卫型号</div>
                                    </div>
                                    <div class="list-right">通用性</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons">
                                            <i class="iconfont tt-icon-jinggai"></i>
                                        </div>
                                        <div class="title">在线状态</div>
                                    </div>
                                    <div class="list-right">在线</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons">
                                            <i class="iconfont tt-icon-jinggai"></i>
                                        </div>
                                        <div class="title">设防状态</div>
                                    </div>
                                    <div class="list-right">设防</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons">
                                            <i class="iconfont tt-icon-jinggai"></i>
                                        </div>
                                        <div class="title">当前角度</div>
                                    </div>
                                    <div class="list-right">18°</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons">
                                            <i class="iconfont tt-icon-jinggai"></i>
                                        </div>
                                        <div class="title">当前温度</div>
                                    </div>
                                    <div class="list-right">22℃</div>
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
                                <button class="common-btn common-btn-danger" type="button" style="padding: 3px 12px;">解除井卫</button>
                            </div>
                        </div>
                        <div class="pic-text-module">
                            <ul class="pic-text-list">
                                <li>
                                    <div class="list-left">
                                        <div class="icons">
                                            <i class="iconfont tt-icon-jinggai"></i>
                                        </div>
                                        <div class="title">井卫型号</div>
                                    </div>
                                    <div class="list-right">水距型</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons">
                                            <i class="iconfont tt-icon-jiekuanzhuangtai"></i>
                                        </div>
                                        <div class="title">在线状态</div>
                                    </div>
                                    <div class="list-right">${fns:getDictLabel (item.workStatus, "bell_work_status", "--")}</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons">
                                            <i class="iconfont tt-icon-fanghu"></i>
                                        </div>
                                        <div class="title">设防状态</div>
                                    </div>
                                    <div class="list-right">${fns:getDictLabel (item.workStatus, "defense_status", "--")}</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons">
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
                                        <div class='input-group date' style="height: 30px;width: 200px;float:left;margin-left: 5px;">
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
                                <button class="common-btn common-btn-danger" type="button" style="padding: 3px 12px;">解除井卫</button>
                            </div>
                        </div>
                        <div class="pic-text-module">
                            <ul class="pic-text-list row-5">
                                <li>
                                    <div class="list-left">
                                        <div class="icons">
                                            <i class="iconfont tt-icon-jinggai"></i>
                                        </div>
                                        <div class="title">井卫型号</div>
                                    </div>
                                    <div class="list-right">通用型</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons">
                                            <i class="iconfont tt-icon-jiekuanzhuangtai"></i>
                                        </div>
                                        <div class="title">在线状态</div>
                                    </div>
                                    <div class="list-right">${fns:getDictLabel (item.workStatus, "bell_work_status", "--")}</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons">
                                            <i class="iconfont tt-icon-fanghu"></i>
                                        </div>
                                        <div class="title">设防状态</div>
                                    </div>
                                    <div class="list-right">${fns:getDictLabel (item.workStatus, "defense_status", "--")}</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons">
                                            <i class="iconfont tt-icon-dixiashuiwei"></i>
                                        </div>
                                        <div class="title">当前角度</div>
                                    </div>
                                    <div class="list-right">${item.angle}°</div>
                                </li>
                                <li>
                                    <div class="list-left">
                                        <div class="icons">
                                            <i class="iconfont tt-icon-dixiashuiwei"></i>
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
            <button id="del" type="button" class="common-btn common-btn-danger">删除井盖</button>
            <button id="fortify" class="common-btn common-btn-danger " type="button">设防</button>
            <button id="chefang" class="common-btn common-btn-success " type="button">撤防</button>
            <button id="work" class="common-btn common-primary " type="button">创建工单</button>
            <button onclick="javascript:history.back(-1);" class="common-btn common-btn-primary " type="button">返回</button>
        </div>
    </div>
    <div class="row" style="display: none">
        <div class="col-md-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        井盖详情>井盖详情
                    </h3>
                </div>
                <br/>
                <div class="panel-group">
                    <div class="panel panel-primary">
                        <div class="panel-heading">普查信息</div>
                        <div class="panel-body">
                            <div style="width:49%;float: left;height: 100%;">

                                <table class="table table-borderless">
                                    <tbody>
                                    <tr>
                                        <td width="72px">井盖编号:</td>
                                        <td width="100px">${cover.no}</td>
                                        <td width="72px">扩展编号:</td>
                                        <td>...</td>
                                    </tr>
                                    <tr>
                                        <td>地区:</td>
                                        <td>${cover.province}${cover.city}${cover.district}</td>
                                        <td>详细地址:</td>
                                        <td>${cover.addressDetail}</td>
                                    </tr>
                                    <tr>
                                        <td>环境位置:</td>
                                        <td>${cover.situation}</td>
                                        <td>窨井用途:</td>
                                        <td>${cover.purpose}</td>
                                    </tr>
                                    <tr>
                                        <td>权属机构:</td>
                                        <td>${cover.ownerDepart}</td>
                                        <td>管理机构:</td>
                                        <td>${cover.superviseDepart}</td>
                                    </tr>
                                    <tr>
                                        <td>井盖材质:</td>
                                        <td>${cover.material}</td>
                                        <td>井盖规格:</td>
                                        <td>${fns:getDictLabel (cover.sizeRule, "cover_size_rule", "--")}</td>
                                    </tr>
                                    <tr>
                                        <td>损毁情况:</td>
                                        <td>${fns:getDictLabel (cover.isDamaged, "boolean", "--")}</td>
                                        <td>井盖照片:</td>
                                        <td>
                                            <div class="row">
                                                <c:forEach items="${cover.coverImageList}" var="images">
                                                    <div class="col-xs-6 col-sm-4 col-md-3">
                                                        <a href="${images.url}" data-toggle="lightbox"
                                                           data-group="image-group-1"><img src="${images.url}"
                                                                                           class="img-rounded" alt=""
                                                                                           width="200px"></a>
                                                    </div>
                                                </c:forEach>
                                            </div>

                                        </td>
                                    </tr>
                                    </tbody>
                                </table>

                            </div>


                            <div style="width:49%;float: right;height: 100%;">

                                <div>坐标：${cover.longitude},${cover.latitude}</div>
                                <div>
                                    <%--放地图--%>
                                    <div id="container" style="height: 320px;width: 100%; position: relative"></div>
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

                            </div>


                        </div>
                    </div>
                    <c:forEach var="item" items="${belllist}">

                        <c:choose>
                            <c:when test="${item.bellType == 'normal'}">
                                <div class="panel panel-primary">
                                    <div class="panel-heading" contenteditable="">井卫IMEI:${item.imei}</div>
                                    <div class="panel-body" contenteditable="">
                                        <table>
                                            <tr>
                                                <td>井卫型号<h2>通用性</h2></td>
                                                <td>在线状态<h2>在线</h2></td>
                                                <td>设防状态<h2>设防</h2></td>
                                            </tr>
                                            <tr>
                                                <td>当前角度<h2>18°</h2></td>
                                                <td>当前温度<h2>22℃</h2></td>
                                                <td></td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                            </c:when>
                            <c:when test="${item.bellType == 'ranging'}">
                                <div class="panel panel-primary">
                                    <div class="panel-heading" contenteditable="">井卫IMEI:${item.imei}</div>
                                    <div class="panel-body">
                                        <div style="width: 100%;border: 1px">
                                            <div class="example-popover-static">
                                                <div class="popover left">
                                                    <h3 class="popover-title">井卫型号</h3>
                                                    <div class="popover-content">
                                                        <h4>水距型</h4>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="example-popover-static">
                                                <div class="popover left">
                                                    <h3 class="popover-title">在线状态</h3>
                                                    <div class="popover-content">
                                                        <h4>${fns:getDictLabel (item.workStatus, "bell_work_status", "--")}</h4>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="example-popover-static">
                                                <div class="popover left">
                                                    <h3 class="popover-title">设防状态</h3>
                                                    <div class="popover-content">
                                                        <h4>${fns:getDictLabel (item.workStatus, "defense_status", "--")}</h4>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="example-popover-static">
                                                <div class="popover left">
                                                    <h3 class="popover-title">当前水位</h3>
                                                    <div class="popover-content">
                                                        <h4>${item.waterLevel}m</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div>
                                            <div>选择日期:</div>
                                            <div class="col-xs-12 col-sm-4">
                                                <div class='input-group date'>
                                                    <input type='text' id='startTime_${item.bellNo}' name="startTime"
                                                           class="form-control startTime"/>
                                                    <span class="input-group-addon">
                            					<span class="glyphicon glyphicon-calendar"></span>
                        					</span>
                                                </div>
                                            </div>
                                            <div class="col-xs-12 col-sm-4">
                                                <div class='input-group date'>
                                                    <input type='text' id='endTime_${item.bellNo}' name="endTime"
                                                           class="form-control endTime"/>
                                                    <span class="input-group-addon">
                            					<span class="glyphicon glyphicon-calendar"></span>
                        					</span>
                                                </div>
                                            </div>
                                            <div>
                                                <a id="search" class="btn btn-primary btn-rounded   btn-sm"
                                                   onclick="searchT('${item.bellNo}');"><i class="fa fa-search"></i> 查询</a>
                                                <a id="reset" class="btn btn-primary btn-rounded  btn-sm"
                                                   onclick="clearText('${item.bellNo}');"><i class="fa fa-refresh"></i>
                                                    重置</a>
                                            </div>

                                            <div class="chart-container">
                                                <div id="water_${item.bellNo}" style="height:330px;"></div>
                                            </div>
                                            <script>
                                                setWaterChart([], [], echarts.init(document.getElementById('water_${item.bellNo}'), 'macarons'))
                                            </script>
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="panel panel-primary">
                                    <div class="panel-heading" contenteditable="">井卫IMEI:${item.imei}【未关联设备类型,默认显示】
                                    </div>
                                    <div class="panel-body">

                                        <div class="example-popover-static">
                                            <div class="popover left">
                                                <h3 class="popover-title">井卫型号</h3>
                                                <div class="popover-content">
                                                    <h4>通用型</h4>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="example-popover-static">
                                            <div class="popover left">
                                                <h3 class="popover-title">在线状态</h3>
                                                <div class="popover-content">
                                                    <h4>${fns:getDictLabel (item.workStatus, "bell_work_status", "--")}</h4>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="example-popover-static">
                                            <div class="popover left">
                                                <h3 class="popover-title">设防状态</h3>
                                                <div class="popover-content">
                                                    <h4>${fns:getDictLabel (item.workStatus, "defense_status", "--")}</h4>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="example-popover-static">
                                            <div class="popover left">
                                                <h3 class="popover-title">当前角度</h3>
                                                <div class="popover-content">
                                                    <h4>${item.angle}°</h4>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="example-popover-static">
                                            <div class="popover left">
                                                <h3 class="popover-title">当前温度</h3>
                                                <div class="popover-content">
                                                    <h4>${item.temperature}℃</h4>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <div class="panel panel-primary">
                        <div class="panel-heading" contenteditable="">历史工单</div>
                        <div class="panel-body">
                            <!-- 表格 -->
<%--                            <table id="coverWorkTable" data-toolbar="#toolbar"></table>--%>
                        </div>
                    </div>
                    <%--			<div class="panel panel-primary">--%>
                    <%--				<div class="panel-heading" contenteditable="">.panel-primary</div>--%>
                    <%--				<div class="panel-body" contenteditable="">面板内容</div>--%>
                    <%--			</div>--%>
                </div>
                <div class="panel-body">
                    <div class="col-lg-3"></div>
                    <div class="col-lg-6">
                        <div class="form-group text-center">

<%--                            <button id="del" type="button" class="btn btn-danger">删除井盖</button>--%>
<%--                            <button id="fortify" class="btn btn-danger " type="button">设防</button>--%>
<%--                            <button id="chefang" class="btn btn-success " type="button">撤防</button>--%>
<%--                            <button id="work" class="btn btn-primary " type="button">创建工单</button>--%>
<%--                            <button onclick="javascript:history.back(-1);" class="btn btn-info " type="button">返回--%>
<%--                            </button>--%>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/webpage/include/bootstraptable.jsp" %>
<script>
    $(function () {

        let coverId = "${cover.id}";
        let no = "${cover.no}";

        $("#del").click(function () {
            jp.confirm('确认要删除该井盖吗？', function () {
                jp.loading();
                jp.get("${ctx}/cv/equinfo/cover/delete?id=" + coverId, function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        window.location.href = "${ctx}/cv/equinfo/cover";
                    } else {
                        jp.error(data.msg);
                    }
                })
            })
        });

        $("#fortify").click(function () {
            jp.confirm('确认要设防吗？', function () {
                jp.loading();
                jp.get("${ctx}/cv/equinfo/cover/fortify?ids=" + coverId, function (data) {
                    if (data.success) {
                        jp.success("设防成功");
                    } else {
                        jp.error("设防失败");
                    }
                })

            })
        });

        $("#chefang").click(function () {
            jp.loading();
            jp.get("${ctx}/cv/equinfo/cover/revoke?ids=" + coverId, function (data) {
                if (data.success) {
                    jp.success("撤防成功");
                } else {
                    jp.error("撤防失败");
                }
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
