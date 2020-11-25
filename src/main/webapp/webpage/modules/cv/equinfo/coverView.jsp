<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>井盖基础信息</title>
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

                var map = new AMap.Map('container', {
                    resizeEnable: true,
                    //zoom:14,//级别
                });
                map.setCity('徐州');

                var m1 = new AMap.Icon({
                    image: '${ctxStatic}/common/images/cover.png',  // Icon的图像
                    size: new AMap.Size(26, 30),    // 原图标尺寸
                    imageSize: new AMap.Size(26, 30), //实际使用的大小
                    offset: new AMap.Pixel(-13, -15),
                    anchor: 'center'
                });

                var lng = $("#longId").val();
                var lat = $("#latId").val();

                var lnglat = new AMap.LngLat(lng, lat); //一个点
                var markericon = m1;
                //构建一个标注点
                var marker = new AMap.Marker({
                    icon: markericon,
                    position: lnglat
                });

                marker.setMap(map);  //把标注点放到地图上
                map.setCenter([lng, lat]);
                map.setZoom(14);
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
                <li><label>标签号:</label><span>${cover.tagNo}</span></li>
                <li><label>井盖状态:</label><span>${fns:getDictLabel (cover.coverStatus, "cover_status", "--")}</span></li>

                <li><label>省份:</label><span>${cover.province}</span></li>
                <li><label>市:</label><span>${cover.city}</span></li>
                <li><label>辖区:</label><span>${fns:getDictLabel (cover.jurisdiction, "cover_jurisdiction", "--")}</span>
                </li>
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
                <li><label>井盖用途:</label><span>${cover.purpose}</span></li>

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
        <h1 class="title2">损坏形式</h1>
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
