<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>井盖基础信息</title>
    <meta name="decorator" content="ani"/>
    <link href="${ctxStatic}/common/fonts/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet"
          type="text/css"/>
    <%--	<script src="http://webapi.amap.com/maps?v=1.4.6&key=06de357afd269944d97de0abcde0f4e0"></script>--%>
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
<form:form id="inputForm" modelAttribute="cover" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="isDamaged" id="showFlag"/>
    <input type="hidden" id="longId" value="${cover.longitude}"/>
    <input type="hidden" id="latId" value="${cover.latitude}"/>

    <sys:message content="${message}"/>
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
                        maxBounds: L.latLngBounds(L.latLng(MAP_EXT[0][0], MAP_EXT[0][1]), L.latLng(MAP_EXT[1][0], MAP_EXT[1][1])),
                        crs: L.Proj.CRS("EPSG:4326", {
                            origin: L.point(MAP_EXT[0][1], MAP_EXT[1][0]),
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
                    <a data-magnify="gallery" data-caption="井盖编号：${cover.no}" href="${images.url}">
                        <img src="${images.url}" alt="">
                    </a>
                </c:forEach>
            </div>
        </div>
    </div>
    <div class="examinebox">
        <h1 class="title2">井盖基础信息</h1>
        <div class="inforbox">
            <ul>
                <li><label>井盖编号:</label><span>${cover.no}</span></li>
                <li><label>井盖状态:</label><span>${fns:getDictLabel (cover.coverStatus, "cover_status", "--")}</span></li>

                <li><label>省份:</label><span>${cover.province}</span></li>
                <li><label>市:</label><span>${cover.city}</span></li>

                <li><label>城市代码:</label><span>${cover.cityCode}</span></li>
                <li><label>行政区划代码:</label><span>${cover.adCode}</span></li>

                <li><label>区:</label><span>${cover.district}</span></li>
                <li><label>街道（办事处）:</label><span>${cover.township}</span></li>
                <li><label>路（街巷）:</label><span>${cover.street}</span></li>
                <li><label>门牌号:</label><span>${cover.streetNumber}</span></li>
                <li><label>详细地址:</label><span>${cover.addressDetail}</span></li>

                <li><label>坐标类型:</label><span>${cover.coordinateType}</span></li>
                <li><label>井盖经度:</label><span>${cover.longitude}</span></li>
                <li><label>井盖纬度:</label><span>${cover.latitude}</span></li>


                <li><label>定位精度（m）:</label><span>${cover.locationAccuracy}</span></li>
                <li><label>海拔精度（m）:</label><span>${cover.altitudeAccuracy}</span></li>
                <li><label>海拔（m）:</label><span>${cover.altitude}</span></li>


                <li><label>井盖类型:</label><span>${cover.coverType}</span></li>
                <li><label>管网用途:</label><span>${cover.purpose}</span></li>

                <li><label>井位地理场合:</label><span>${cover.situation}</span></li>
                <li><label>制造商:</label><span>${cover.manufacturer}</span></li>
                <li><label>尺寸规格:</label><span>${cover.sizeSpec}</span></li>

                    <%--<li><label>井盖规格:</label><span>${cover.sizeRule}</span></li>--%>
                <li><label>井盖规格:</label><span>${fns:getDictLabel (cover.sizeRule, "cover_size_rule", "--")}</span></li>
                <li><label>直径（mm）:</label><span>${cover.sizeDiameter}</span></li>
                <li><label>半径（mm）:</label><span>${cover.sizeRadius}</span></li>
                <li><label>长度（mm）:</label><span>${cover.sizeLength}</span></li>
                <li><label>宽度（mm）:</label><span>${cover.sizeWidth}</span></li>
                <li><label>地图标记:</label><span>${cover.marker}</span></li>
                    <%--<li><label> 高度差:</label><span>${cover.altitudeIntercept}</span></li>--%>
                <li>
                    <label>高度差:</label><span>${fns:getDictLabel (cover.altitudeIntercept, "cover_altitude_intercept", "--")}</span>
                </li>

                <li><label>井盖材质:</label><span>${cover.material}</span></li>
                <li><label>监管单位:</label><span>${cover.superviseDepart}</span></li>

                <li><label>是否损毁:</label><span>${fns:getDictLabel (cover.isDamaged, "boolean", "--")}</span></li>
                <li><label>井筒破损深度（m）:</label><span>${cover.manholeDamageDegree}</span></li>
                <li><label>损毁情况备注:</label><span>${cover.damageRemark}</span></li>

                <li><label>采集时间:</label><span><fmt:formatDate value="${cover.createDate}"
                                                              pattern="yyyy-MM-dd HH:mm:ss"/></span></li>

                    <%--				private String auditBy;		// 审核人
                                    private Date auditDate;		// 审核时间--%>


            </ul>
        </div>
    </div>

    <div class="examinebox" id="ownerId">
        <h1 class="title2">权属单位</h1>
        <div class="inforbox">

            <div class="damage">
                    <%--<c:forEach items="${cover.coverOwnerList}" var="owner">
                        <label class="t">${owner.ownerName}</label>
                    </c:forEach>--%>
                <label class="t">${cover.ownerDepart}</label>
            </div>

        </div>
    </div>

    <div class="examinebox" id="damagedId">
        <h1 class="title2">井盖病害</h1>
        <div class="inforbox">

            <div class="damage">
                <c:forEach items="${cover.coverDamageList}" var="damage">
                    <label class="t">${fns:getDictLabel (damage.damage, "cover_damage", "--")}</label>
                </c:forEach>
            </div>

        </div>
    </div>
</form:form>
</body>
</html>
