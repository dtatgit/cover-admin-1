<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>工单审核信息管理</title>
    <meta name="decorator" content="ani"/>
    <link href="${ctxStatic}/common/fonts/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
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
        .nav-tabs{clear: both;overflow: hidden; margin: 0;padding: 0; border: 0;    position: relative;top: 1px;}
        .nav-tabs li a{margin-right: 0px;line-height: 1.42857143;border: 1px solid #ddd;border-radius: 0;
            background-color: #fff;padding: 5px 10px;border-right: none;color:#555;display: block}
        .nav-tabs li:last-child a{border-right:1px solid #ddd;}
        .nav-tabs > li.active > a, .nav-tabs > li.active > a:hover, .nav-tabs > li.active > a:focus {
            color: #3ca2e0; border-top:2px solid #3ca2e0;
        }


    </style>
    <script type="text/javascript">
        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            if(validateForm.form()){
                $table = table;
                $topIndex = index;
                jp.loading();
                $("#inputForm").submit();
                return true;
            }

            return false;
        }

        $(document).ready(function() {

            $(".nav-tabs").on("click","a",function () {
                $(".nav-tabs li").removeClass("active");
                $(this).parent().addClass("active");
                var title = $(this).attr("title");
                $(".panel").hide();
                $("#" + title).show();
            });

            validateForm = $("#inputForm").validate({
                submitHandler: function(form){
                    jp.post("${ctx}/cb/work/coverWork/saveAudit",$('#inputForm').serialize(),function(data){
                        if(data.success){
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog

                        }else{
                            jp.error(data.msg);
                        }
                    })
                },
                errorContainer: "#messageBox",
                errorPlacement: function(error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
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
        $(document).ready(function() {
            var   flag=  $("#showFlag").val();
            if(flag=="Y"){

                $("#damagedId").removeAttr("hidden");
            }else{

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
            <div class="map">
                    <%--放地图--%>
                <div id="container" style="height: 200px;position: relative;top:10px; margin:0 2%;width: 96%"></div>
                <script type="text/javascript">

                    var map = new AMap.Map('container', {
                        resizeEnable: true,
                        //zoom:14,//级别
                    });
                    map.setCity('徐州');

                    var m1 = new AMap.Icon({
                        image: '${ctxStatic}/common/images/cover.png',  // Icon的图像
                        size: new AMap.Size(38, 63),    // 原图标尺寸
                        imageSize: new AMap.Size(19,33) //实际使用的大小
                    });

                    var lng= $("#longId").val();
                    var lat= $("#latId").val();

                    var lnglat = new AMap.LngLat(lng, lat); //一个点
                    var markericon = m1;
                    //构建一个标注点
                    var marker = new AMap.Marker({
                        icon: markericon,
                        position: lnglat
                    });

                    marker.setMap(map);  //把标注点放到地图上
                    map.setZoom(14);
                </script>
            </div>
            <div class="container imgsbox">
                <div class="image-set">
                    <c:forEach items="${cover.coverImageList}" var="images">
                        <a data-magnify="gallery" data-caption="井盖编号：${coverBell.cover.no}" href="${images.url}">
                            <img  src="${images.url}" alt="">
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="inforbox">
            <ul>
                <li><label>井盖编号:</label><span>${coverBell.cover.no}</span></li>
                <li><label>详细地址:</label><span>${coverBell.cover.addressDetail}</span></li>

                <li><label>井盖用途:</label><span>${coverBell.cover.purpose}</span></li>
                <li><label>井位地理场合:</label><span>${coverBell.cover.situation}</span></li>

                    <%--<li><label>井盖规格:</label><span>${coverAudit.cover.sizeRule}</span></li>--%>

                <li><label>尺寸规格:</label><span>${coverBell.cover.sizeSpec}</span></li>
                <li><label>井盖规格:</label><span>${fns:getDictLabel (coverBell.cover.sizeRule, "cover_size_rule", "--")}</span></li>
                <li><label>直径（mm）:</label><span>${coverBell.cover.sizeDiameter}</span></li>
                <li><label>半径（mm）:</label><span>${coverBell.cover.sizeRadius}</span></li>
                <li><label>长度（mm）:</label><span>${coverBell.cover.sizeLength}</span></li>
                <li><label>宽度（mm）:</label><span>${coverBell.cover.sizeWidth}</span></li>

                <li><label>井盖材质:</label><span>${coverBell.cover.material}</span></li>

                <li><label>井盖类型:</label><span>${coverBell.cover.coverType}</span></li>
                <li><label>高度差:</label><span>${coverBell.cover.altitudeIntercept}</span></li>
                <li><label>是否损毁:</label><span>${fns:getDictLabel (coverBell.cover.isDamaged, "boolean", "--")}</span></li>
                <li><label>损毁情况备注:</label><span>${coverBell.cover.damageRemark}</span></li>
                <li><label>采集人员:</label><span>${coverBell.cover.createBy.name}</span></li>
                <li><label>采集时间:</label><span><fmt:formatDate value="${coverBell.cover.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span></li>
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
                <li><label>工作状态:</label><span>${fns:getDictLabel (coverBell.workStatus, "bell_work_status", "--")}</span></li>
                <li><label>生命周期:</label><span>${fns:getDictLabel (coverBell.bellStatus, "bell_status", "--")}</span></li>
                <li><label>设防状态:</label><span>${fns:getDictLabel (coverBell.defenseStatus, "defense_status", "--")}</span></li>
            </ul>
        </div>
    </div>




</form:form>
</body>
</html>