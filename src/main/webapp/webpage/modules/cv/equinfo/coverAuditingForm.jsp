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
</head>
<body class="bg-white">
<div class="wrapper wrapper-content">
    <div class="details-header">
        <span>待审核井盖</span>
        <span class="division">/</span>
        <span>井盖审核</span>
    </div>
    <div>
        <div class="details-view is-one">
            <h1 class="view-title">井盖详情</h1>
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
                                    <img src="${images.url}" onclick="jp.showPic('${images.url}')" width="100px" class="img-rounded" alt="">
                                </c:forEach>
							</div>
						    </span>
                        </li>

                        <li><label>审核意见：</label>
                            <textarea id="txtDesc"  rows="4"   class="form-control "></textarea>
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

        <div class="details-footer">
            <button id="approve" class="btn btn-success" type="button">审核通过</button>
            <button id="overrule" class="btn btn-danger" type="button">驳回</button>
            <button onclick="javascript:history.back(-1);" class="btn btn-info " type="button">返回</button>
        </div>
    </div>
</div>
<script>

    $(function () {

        let coverId = "${cover.id}";
        

        $("#approve").click(function () {

            jp.confirm('确认通过吗？', function () {
                jp.loading();

                jp.post("${ctx}/cv/equinfo/cover/audit",{coverId:coverId,desc:$("#txtDesc").val(), status:"audit_pass"},function(result){
                    if(result.success){
                        jp.success(result.msg);
                        history.back(-1);
                    }else{
                        jp.error(result.msg);
                    }
                });
            })

        });


        $("#overrule").click(function () {


            jp.confirm('确认驳回吗？', function () {
                jp.loading();

                jp.post("${ctx}/cv/equinfo/cover/audit",{coverId:coverId,desc:$("#txtDesc").val(), status:"audit_fail"},function(result){
                    if(result.success){
                        jp.success(result.msg);
                        history.back(-1);
                    }else{
                        jp.error(result.msg);
                    }
                });
            })
        });

    });


</script>
</body>
</html>
