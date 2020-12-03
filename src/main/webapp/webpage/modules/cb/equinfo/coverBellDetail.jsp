<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>工单审核信息管理</title>
    <meta name="decorator" content="ani"/>
    <link href="${ctxStatic}/common/fonts/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet"
          type="text/css"/>
    <%--    <script src="http://webapi.amap.com/maps?v=1.4.6&key=06de357afd269944d97de0abcde0f4e0"></script>--%>
    <!-- Bootstrap -->
    <%-- <link href="https://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
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

    <link href="${ctxStatic}/plugin/leaflet/leaflet.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/plugin/superMap/leaflet/iclient-leaflet.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${ctxStatic}/plugin/superMap/jquery-i18next.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/plugin/superMap/locales/zh-CN/resources.js"></script>
    <script type="text/javascript" src="${ctxStatic}/plugin/leaflet/leaflet.js"></script>
    <script src="${ctxStatic}/plugin/leaflet/leaflet.rotatedMarker.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/plugin/superMap/localization.js"></script>
    <script type="text/javascript" src="${ctxStatic}/plugin/superMap/tokengenerator.js"></script>
    <script type="text/javascript" src="${ctxStatic}/plugin/superMap/utils.js"></script>
    <script type="text/javascript" src="${ctxStatic}/plugin/superMap/i18next.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/plugin/superMap/leaflet/iclient-leaflet-es6.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/plugin/superMap/leaflet/iclient-leaflet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/common/js/super-map.js"></script>
    <script type="text/javascript" src="${ctxStatic}/common/js/coordtransform.js"></script>
    <script type="text/javascript" src="${ctxStatic}/common/js/utils.js"></script>
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
<form:form id="inputForm" modelAttribute="coverBell" class="form-horizontal">
    <form:hidden path="id"/>
    <input type="hidden" id="longId" value="${coverBell.cover.longitude}"/>
    <input type="hidden" id="latId" value="${coverBell.cover.latitude}"/>
    <%--<input type="hidden" id="showFlag" value="${cover.isDamaged}"/>--%>
    <sys:message content="${message}"/>


    <div class="examinebox">
        <h1 class="title2">井盖信息</h1>
        <div class="examinebox examinebox1">
            <div class="map" style="width: 80.5%">
                    <%--放地图--%>
                <div id="container" style="height: 220px;width: 100%; position: relative"></div>
                <script type="text/javascript">
                    let map = null;
                    initMap();

                    function initMap() {
                        if (!checkUrl(MAP_URL)) {
                            return;
                        }
                        // supermap.securitymanager.registerkey(map_url, map_key);
                        map = L.map('container', {
                            crs: L.CRS.EPSG4326,
                            preferCanvas: true,
                            zoomControl: false,
                            center: [116.20934993335362, 39.441570142096126],
                            maxZoom: 12,
                            zoom: 4,
                            maxBounds: L.latLngBounds(L.latLng(39.441570142096126,116.20934993335362), L.latLng(39.83089881098218,116.72111943584403)),
                            crs: L.Proj.CRS("EPSG:4326", {
                                origin: L.point(116.20934993335362, 39.83089881098218),
                                resolutions: MAP_RES
                            })
                        });
                        L.supermap.wmtsLayer("http://172.25.117.10:8081/geoesb/proxy/db28d8d25a5b4ef2b6b4e2c44bed0e6f/452a43316547454a9614d7c16b8c1d2d",
                            {
                                layer: "DX_DLG_2020",
                                style: "default",
                                tilematrixSet: "Custom_DX_DLG_2020",
                                format: "image/png",
                                requestEncoding: 'REST',
                                attribution: ""
                            }
                        ).addTo(map);


                        let lng = $("#longId").val();
                        let lat = $("#latId").val();
                        let icon = L.icon({
                            iconUrl: '${ctxStatic}/common/images/cover.png',
                            iconSize: [22, 28]
                        });
                        let wgsPoint = transWgsLngLat(lng, lat);
                        let currentMarker = L.latLng(wgsPoint.lat, wgsPoint.lng);
                        map.panTo(currentMarker);
                        let currentMarkerLayer = L.marker(currentMarker, {
                            icon: icon
                        });
                        currentMarkerLayer.addTo(map);
                    }
                </script>
            </div>
            <div class="container imgsbox" style="margin: initial;">
                <div class="image-set">
                    <c:forEach items="${cover.coverImageList}" var="images">
                        <a data-magnify="gallery" data-caption="井盖编号：${coverBell.cover.no}" href="${images.url}">
                            <img src="${images.url}" alt="">
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="inforbox" style="margin-top: 15px">
            <ul>
                <li><label>井盖编号:</label><span>${coverBell.cover.no}</span></li>
                <li><label>详细地址:</label><span>${coverBell.cover.addressDetail}</span></li>

                <li><label>井盖用途:</label><span>${coverBell.cover.purpose}</span></li>
                <li><label>井位地理场合:</label><span>${coverBell.cover.situation}</span></li>

                    <%--<li><label>井盖规格:</label><span>${coverAudit.cover.sizeRule}</span></li>--%>

                <li><label>尺寸规格:</label><span>${coverBell.cover.sizeSpec}</span></li>
                <li>
                    <label>井盖规格:</label><span>${fns:getDictLabel (coverBell.cover.sizeRule, "cover_size_rule", "--")}</span>
                </li>
                <li><label>直径（mm）:</label><span>${coverBell.cover.sizeDiameter}</span></li>
                <li><label>半径（mm）:</label><span>${coverBell.cover.sizeRadius}</span></li>
                <li><label>长度（mm）:</label><span>${coverBell.cover.sizeLength}</span></li>
                <li><label>宽度（mm）:</label><span>${coverBell.cover.sizeWidth}</span></li>

                <li><label>井盖材质:</label><span>${coverBell.cover.material}</span></li>

                <li><label>井盖类型:</label><span>${coverBell.cover.coverType}</span></li>
                <li><label>高度差:</label><span>${coverBell.cover.altitudeIntercept}</span></li>
                <li><label>是否损毁:</label><span>${fns:getDictLabel (coverBell.cover.isDamaged, "boolean", "--")}</span>
                </li>
                <li><label>损毁情况备注:</label><span>${coverBell.cover.damageRemark}</span></li>
                <li><label>采集人员:</label><span>${coverBell.cover.createBy.name}</span></li>
                <li><label>采集时间:</label><span><fmt:formatDate value="${coverBell.cover.createDate}"
                                                              pattern="yyyy-MM-dd HH:mm:ss"/></span></li>
                <li><label></label><span></span></li>
                <li><label>权属单位:</label><span>${coverBell.cover.ownerDepart}</span></li>

                <li><label>损坏形式:</label>
                    <c:forEach items="${coverBell.cover.coverDamageList}" var="damage">
                        <span class="t">${fns:getDictLabel (damage.damage, "cover_damage", "--")}</span>
                    </c:forEach>
                </li>
            </ul>
        </div>


    </div>

    </div>


    <div class="examinebox">
        <h1 class="title2">井卫信息</h1>
        <div class="inforbox">
            <ul>
                <li><label>井卫编号:</label><span>${coverBell.bellNo}</span></li>
                <li><label>井卫型号:</label><span>${coverBell.bellModel}</span></li>
                <li><label>固件版本号:</label><span>${coverBell.version}</span></li>
                <li><label>IMEI:</label><span>${coverBell.imei}</span></li>
                <li><label>SIM:</label><span>${coverBell.sim}</span></li>
                <li><label>设备类型:</label><span>${fns:getDictLabel (coverBell.bellType, " bellType", "--")}</span></li>
                <li>
                    <label>工作状态:</label><span>${fns:getDictLabel (coverBell.workStatus, "bell_work_status", "--")}</span>
                </li>
                <li><label>生命周期:</label><span>${fns:getDictLabel (coverBell.bellStatus, "bell_status", "--")}</span>
                </li>
                <li>
                    <label>设防状态:</label><span>${fns:getDictLabel (coverBell.defenseStatus, "defense_status", "--")}</span>
                </li>
            </ul>
        </div>
    </div>


</form:form>
</body>
</html>
