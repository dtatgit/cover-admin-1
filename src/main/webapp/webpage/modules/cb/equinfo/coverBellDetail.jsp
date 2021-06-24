<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>

<html>
<head>
    <title>工单审核信息管理</title>
    <meta name="decorator" content="ani"/>
    <link href="${ctxStatic}/common/fonts/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet"
          type="text/css"/>
    <link href="${ctxStatic}/plugin/bootstrap/bootstrap.min.css" rel="stylesheet">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->


    <script src="${ctxStatic}/plugin/jquery/jquery.min.js"></script>
    <script src="${ctxStatic}/plugin/bootstrap/bootstrap.min.js"></script>
    <script src="${ctxStatic}/plugin/imagesPlug/jquery.magnify.js"></script>
    <link href="${ctxStatic}/plugin/imagesPlug/jquery.magnify.css" rel="stylesheet">
    <script src="${ctxStatic}/plugin/jquery-validation\1.14.0/jquery.validate.js"></script>

    <%--     引入datetimepicker样式--%>
    <link href="${ctxStatic}/plugin/bootstrap-datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet">
    <%--     引入datetimepicker脚本--%>
    <script src="${ctxStatic}/plugin/bootstrap-datetimepicker/moment-with-locales.min.js"></script>
    <script src="${ctxStatic}/plugin/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js"></script>


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
    <script src="${ctxStatic}/plugin/echarts4/echarts.min.js"></script>
    <script src="${ctxStatic}/plugin/echarts4/macarons.js"></script>

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
        .input-wrapper{
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

                <li><label>管网用途:</label><span>${coverBell.cover.purpose}</span></li>
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

                <li><label>井盖病害:</label>
                    <c:forEach items="${coverBell.cover.coverDamageList}" var="damage">
                        <span class="t">${fns:getDictLabel (damage.damage, "cover_damage", "--")}</span>
                    </c:forEach>
                </li>
            </ul>
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
                <li><label>设备类型:</label><span>${fns:getDictLabel (coverBell.bellType, " bell_type", "--")}</span></li>
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
    <div class="examinebox">
        <h1 class="title2">水位数据</h1>
        <div class='input-wrapper' >
            <span>选择日期:</span>
            <div class="col-xs-12 col-sm-4">
                <div class='input-group date'  >
                    <input type='text'  id='startTime' name="startTime" class="form-control"  />
                    <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
                </div>

            </div>
            <div  class="col-xs-12 col-sm-4">
                <div class='input-group date'  >
                    <input type='text'  id='endTime' name="endTime" class="form-control"  />
                    <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
                </div>
            </div>
            <div>
                <a  id="search" class="btn btn-primary btn-rounded   btn-sm"><i class="fa fa-search"></i> 查询</a>
                <a  id="reset" class="btn btn-primary btn-rounded  btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
            </div>
        </div>
        <div class="chart-container">
            <div id="water" style="height:330px;"></div>
        </div>
    </div>
    <div class="examinebox">
        <h1 class="title2">温度数据</h1>
        <div class='input-wrapper' >
            <span>选择日期:</span>
            <div class="col-xs-12 col-sm-4">
                <div class='input-group date'  >
                    <input type='text'  id='startTimeTemp'  class="form-control"  />
                    <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
                </div>
            </div>
            <div  class="col-xs-12 col-sm-4">
                <div class='input-group date'  >
                    <input type='text'  id='endTimeTemp'  class="form-control"  />
                    <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
                </div>
            </div>
            <div>
                <a  id="searchTemp" class="btn btn-primary btn-rounded   btn-sm"><i class="fa fa-search"></i> 查询</a>
                <a  id="resetTemp" class="btn btn-primary btn-rounded  btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
            </div>

        </div>
        <div class="chart-container">
            <div id="temperature" style="height:330px;"></div>
        </div>
    </div>


</form:form>
<script>
    // 水位
    let waterChart
    function initWaterChart() {
        waterChart= echarts.init(document.getElementById('water'),'macarons');
        setWaterChart([[]])
    }
    function setWaterChart(waterSeries) {
        const   waterOptions = {
            tooltip: {
                trigger: 'item',
                formatter: '{b}:{c} '
            },
            xAxis: {
                type: 'time',
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
                    color:'rgba(46,199,211,0.7)'
                }
            }]
        };
        waterChart.setOption(waterOptions);
    }
    initWaterChart()
    function getQueryStr(startTime,endTime){
        let devNo='${coverBell.bellNo}'
        return '?devNo='+devNo+'&startDateTime='+startTime+'&endDateTime='+endTime
    }
    $("#search").click("click", function() {// 绑定查询按扭
        let startTime= $("#startTime").val();
        let endTime= $("#endTime").val();
        jp.get("${ctx}/cb/equinfo/coverBell/queryDistanceData"+getQueryStr(startTime,endTime), function(data){
            if(data.success){
                const temp=data.data
                console.log(temp,'temp')
                let waterSeries=[]
                if(temp.length>0){
                    temp.forEach(function(item){
                        waterSeries.push([item.dtime,item.distance])
                    })
                    setWaterChart(waterSeries)
                }
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })
    });
    $("#reset").click("click", function() {// 绑定重置
        $("#startTime").val("");
        $("#endTime").val("");
    });
    // 温度
    let tempChart
    function initTempChart() {
        tempChart= echarts.init(document.getElementById('temperature'),'macarons');
        setTempChart([[]])// 初始化数据
    }
    function setTempChart(tempSeries) {
        const   tempOptions = {
            tooltip: {
                trigger: 'item',
                formatter: '{b}:{c} '
            },
            xAxis: {
                type: 'time',
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
                data: tempSeries,
                type: 'line',
                areaStyle: {
                    color:'rgba(46,199,211,0.7)'
                }
            }]
        };
        tempChart.setOption(tempOptions);
    }
    initTempChart()
    $("#searchTemp").click("click", function() {// 绑定查询按扭
        let startTime= $("#startTimeTemp").val();
        let endTime= $("#endTimeTemp").val();
        jp.get("${ctx}/cb/equinfo/coverBell/queryTemperatureData"+getQueryStr(startTime,endTime), function(data){
            if(data.success){
                const temp=data.data
                let Series=[]
                if(temp.length>0){
                    temp.forEach(function(item){
                        Series.push([item.dtime,item.temperature])
                    })
                    setTempChart(Series)
                }
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })
    });
    $("#resetTemp").click("click", function() {// 绑定重置
        $("#startTimeTemp").val("");
        $("#endTimeTemp").val("");
    });

    // 监听缩放
    setTimeout(function () {
        window.onresize = function () {
            waterChart.resize();
            tempChart.resize();
        }
    }, 200);
    function getDay(day){
        var today = new Date();
        var targetday_milliseconds=today.getTime() + 1000*60*60*24*day;
        today.setTime(targetday_milliseconds); //注意，这行是关键代码
        var tYear = today.getFullYear();
        var tMonth = today.getMonth();
        var tDate = today.getDate();
        tMonth = doHandleMonth(tMonth + 1);
        tDate = doHandleMonth(tDate);
        return tYear+"-"+tMonth+"-"+tDate;
    }
    function doHandleMonth(month){
        var m = month;
        if(month.toString().length == 1){
            m = "0" + month;
        }
        return m;
    }

    $(function(){
        let now=getDay(0)
        let  past=getDay(-7)
        $("#startTime").val(past);
        $("#endTime").val(now);
        $("#startTimeTemp").val(past);
        $("#endTimeTemp").val(now);
        $("#searchTemp").click();
        $("#search").click();
    });
</script>
<script>

</script>
</body>
</html>
