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
<%--                                    <img src="${images.url}" onclick="jp.showPic('${images.url}')" width="100px" class="img-rounded" alt="">--%>


                                    <a data-magnify="gallery" data-caption="井盖编号：${cover.no}" href="${images.url}">
                                        <img src="${images.url}" alt="">
                                    </a>
                                </c:forEach>
							</div>
						    </span>
                        </li>

                        <li><label>审核意见：</label>
                            <textarea id="txtDesc"  rows="4"   class="form-control "></textarea>
                        </li>
                    </ul>
                    <div><label>审核图片：</label>
                        <div class="upload-box clear">
                            <p class="upload-tip">最多上传3张图片(包含已上传的)，每个图片不能超过1M，向后追加图片，不允许放大</p>
                            <div class="image-box"></div>
                        </div>
                    </div>
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
            <shiro:hasPermission name="cv:equinfo:coverAudit:batchPass">
            <button id="approve" class="btn btn-success" type="button">审核通过</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="cv:equinfo:coverAudit:batchReject">
            <button id="overrule" class="btn btn-danger" type="button">驳回</button>
            </shiro:hasPermission>
            <button onclick="javascript:history.back(-1);" class="btn btn-info " type="button">返回</button>
        </div>
    </div>
</div>
<script>

    $(function () {

        let coverId = "${cover.id}";
        

        $("#approve").click(function () {

            jp.confirm('确定要对该井盖进行审核通过吗？', function () {
                jp.loading();
                let img_ids =[];
                $("input[name='file_id']").each(function(){
                    img_ids.push($(this).val());
                })
                let ids = img_ids.join(",");

                jp.post("${ctx}/cv/equinfo/cover/audit",{coverId:coverId,desc:$("#txtDesc").val(), status:"audit_pass",imgIds:ids},function(result){
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


            jp.confirm('确定要对该井盖进行审核驳回吗？', function () {
                jp.loading();
                let img_ids =[];
                $("input[name='file_id']").each(function(){
                    img_ids.push($(this).val());
                })
                let ids = img_ids.join(",");
                jp.post("${ctx}/cv/equinfo/cover/audit",{coverId:coverId,desc:$("#txtDesc").val(), status:"audit_fail",imgIds:ids},function(result){
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

<script src="${ctxStatic}/plugin/jquery-upload/js/jquery.upload.js"></script>

<script>

    $(".image-box").ajaxImageUpload({
        fileInput : 'file',
        postUrl : '${ctx}/api/file/uploadimg', //上传的服务器地址
        width : 180,
        height : 180,
        imageUrl: [],
        postData : { category:'cover_audit' },
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
</html>
