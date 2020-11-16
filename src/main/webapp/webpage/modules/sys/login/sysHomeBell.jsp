<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%--<%
    //页面每隔30秒自动刷新一遍
    response.setHeader("refresh","30");
%>--%>
<html>
<head>
    <title>首页</title>
    <meta name="decorator" content="ani"/>

    <style>
        #body-container {
            margin-left: 0px !important;
            /*padding: 10px;*/
            margin-top: 0px !important;
            overflow-x: hidden!important;
            transition: all 0.2s ease-in-out !important;
            height: 100% !important;
        }
        .stat.hvr-wobble-horizontal:last-child {
            margin-bottom: 10px;
        }
        .right-worklist-box{
            background: #fff;
            height: 495px;

        }
        .right-worklist-box .item{
            padding: 10px 30px;
            height: 110px;
            margin: 10px 10px;
            display: flex;
            justify-content: start;
            border: 1px solid rgba(0,0,0,0.1);
            align-items: center;
        }
        .right-worklist-box .item img{
            width: 60px;
            height: 60px;
            max-width: 100%;
            overflow: hidden;
            margin-right: 20px;
        }
        .right-worklist-box .item dl{
            margin: 0;
            padding: 0;
            /*width: 60%;*/
            text-align: center;
            color: #6d6d6d;
        }
        .right-worklist-box .item dl dt{
            font-size: 30px;
            padding: 0;
            margin: 0;
            font-weight: normal;
        }
        .right-worklist-box .item dl dt span{
            font-size: 14px;
        }
        .right-worklist-box .item dd{
            margin-inline-start:0;
            font-size: 14px;
            margin: 0px;
            padding: 0;
        }
        /*.right-worklist-box .item:nth-child(1){*/
            /*background: #fff;*/
        /*}*/
        /*.right-worklist-box .item:nth-child(2){*/
            /*background: grey;*/
        /*}*/
        /*.right-worklist-box .item:nth-child(3){*/
            /*background: #fff;*/
        /*}*/
        /*.right-worklist-box .item:nth-child(4){*/
            /*background: grey;*/
        /*}*/
    </style>
    <script src="http://webapi.amap.com/maps?v=1.4.6&key=06de357afd269944d97de0abcde0f4e0">
        <script type="text/javascript">
            function openMap() {
                window.open('${pageContext.request.contextPath}/webpage/modules/sys/login/bellMap.jsp', '_blank', 'height=1200, width=1200, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
            }
    </script>

</head>
<body class="">
<div id="body-container" class="wrapper wrapper-content">
    <div class="conter-wrapper home-container">
        <div class="row home-row">
            <div class="col-md-9 col-lg-9">
                <%--<div class="home-charts-middle">--%>
                <%--<div class="home-panel-heading panel-heading">--%>
                <%--<h2>井盖数据提报</h2>--%>
                <%--&lt;%&ndash;<button onclick="openMap()">更多</button>&ndash;%&gt;--%>
                <%--</div>--%>

                <div class="box">
                    <%--<iframe src="${pageContext.request.contextPath}/webpage/modules/sys/login/bellMap.jsp" scrolling="no" frameborder="0" style="width:100%; height: 765px;"></iframe>--%>
                    <div id="container" style="height: 750px;width: 100%;padding: 0;margin: 0" ></div>

                    <script type="text/javascript">
                        // $(function(){
                        //     setInterval(mapAlarmData,60000);
                        // })

                        mapAlarmData();

                        var map = new AMap.Map('container', {
                            resizeEnable: true,
                            //zoom:14,//级别
                        });
                        map.setCity('徐州');
                        var m1 = new AMap.Icon({
                            image: '${ctxStatic}/common/images/m1.png',  // Icon的图像
                            size: new AMap.Size(38, 63),    // 原图标尺寸
                            imageSize: new AMap.Size(19,33) //实际使用的大小
                        });
                        var m2 = new AMap.Icon({
                            image: '${ctxStatic}/common/images/bell.png',  // Icon的图像
/*                            size: new AMap.Size(38, 63),    // 原图标尺寸
                            imageSize: new AMap.Size(19,33) //实际使用的大小*/
                        });
                        var markers = [];

                        function mapAlarmData(){
                            console.log('mapAlarmData');
                            $.ajax({
                                url: "${ctx}/cb/equinfo/coverBell/mapAlarmdata",
                                type: "POST",
                                dataType: "json",
                                async: true,
                                success: function(data) {
                                    console.log(data);
                                    map.remove(markers);
                                    //jp.close(index);
                                    if(data.success){
                                        console.log(data.data);
                                        initMapData(data.data);
                                    }else{
                                        //jp.warning('没有数据！');
                                    }
                                }
                            });
                        }

                        function initMapData(data) {

                            $.each(data,function(key,value){

                                if(!value.lng || !value.lat){
                                    return;
                                }
                                var lng =value.lng;
                                var lat =value.lat;
                                var lnglat = new AMap.LngLat(lng, lat); //一个点

                                var markericon = m2;
                                /*       if(status=="正常"){
                                           markericon = m1;
                                       }else{
                                           markericon = m2;
                                       }*/

                                //构建一个标注点
                                var marker = new AMap.Marker({
                                    icon: markericon,
                                    position: lnglat
                                });

                                marker.setMap(map);  //把标注点放到地图上
                                markers.push(marker);
                                //构建信息窗体
                                var infoWindow = openInfo(value,marker);


                                // marker.on("mouseover", function(e) {
                                //     infoWindow.open(map, e.target.getPosition());
                                // });
                                // marker.on("mouseout", function() {
                                //     infoWindow.close()
                                // });
                            });

                            map.setZoom(14);
                        }

                        //在指定位置打开信息窗体
                        function openInfo(value,marker) {
                            //构建信息窗体中显示的内容
                            var info = [];
                            info.push("<div style='line-height:1.6em;font-size:12px;'>");
                            // info.push("编号 ："+value.number);
                            //info.push("状态 ："+value.status);
                            // info.push("时间 ："+value.lastUpdateTime);

                            //info.push("报警编号 ："+value.alarmNum);
                            //info.push("报警类型 ："+value.alarmTypeName);
                            info.push("井卫编号 ："+value.bellNo);
                            info.push("井盖编号 ："+value.no);
                            info.push("详细地址 ："+ value.address );
                            info.push( "<input id='coverInfoId' type='button' class='btn' value='井盖详情' onclick=\"coverInfo('"+value.coverId+"')\"/> "+"<input id='alarmWorkId' type='button' class='btn' value='报警详情' onclick=\"alarmWork('"+value.coverId+"')\"/> "+"<input id='createWorkId' type='button' class='btn' value='生成工单' onclick=\"createWork('"+value.bellId+"')\"/> "+ "</div>");
                            var infoWindow = new AMap.InfoWindow({
                                offset: new AMap.Pixel(0, -29),
                                content:  info.join("<br/>"),  //使用默认信息窗体框样式，显示信息内容
                            });

                            marker.on("mouseover", function(e) {
                                infoWindow.open(map, e.target.getPosition());
                            });
                        /*    marker.on("mouseout", function() {
                                infoWindow.close();
                            });*/

                        }

                        function createWork(bellId){

                          //window.open("${ctx}/cb/work/coverWork");
                            jp.confirm('确认要生成工单记录吗？', function(){
                                jp.loading();
                                //jp.get("${ctx}/cb/alarm/coverBellAlarm/createWork?ids=" + alarmId, function(data){
                                jp.get("${ctx}/cb/equinfo/coverBell/createWork?id=" + bellId, function(data){
                                        if(data.success){
                                            jp.close();
                                            //window.location.href="${ctx}/cb/work/coverWork";//当前页面打开
                                            //window.open("${ctx}/cb/work/coverWork");
                                            top.menuItem2("${ctx}/cb/work/coverWork",18,"工单信息",null);//选项卡
                                            //jp.success(data.msg);
                                        }else{
                                            jp.error(data.msg);
                                        }
                                    })
                               // }
                        })
                        }


                        function coverInfo(coverId){
                            jp.openDialogView('查看井盖基础信息', "${ctx}/cv/equinfo/cover/view?id=" + coverId,'800px', '500px', $('#coverTable'));
                        }

                        function alarmWork(coverId){
                            jp.openDialogView('查看报警记录', "${ctx}/cv/equinfo/cover/alarmlist?id=" + coverId,'800px', '500px', $('#coverBellTable'));
                            //jp.openDialogView('查看井卫报警信息', "${ctx}/cb/alarm/coverBellAlarm/form?id=" + alarmId,'800px', '500px', $('#coverBellAlarmTable'));

                        }
                    </script>
                </div>
            </div>
            <%--</div>--%>
            <div class="col-md-3 col-lg-3">
                <div class="home-stats">
                    <a href="#" class="stat hvr-wobble-horizontal">
                        <div class=" stat-icon red">
                            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" t="1555926046803" class="icon" style="" viewBox="0 0 1024 1024" version="1.1" p-id="11224"><defs><style type="text/css"/></defs><path d="M276.48 564.906667l-1.706667-10.24h-78.506666l3.413333 13.653333c22.186667 133.12 131.413333 244.053333 264.533333 264.533333l13.653334 3.413334v-75.093334l-10.24-1.706666c-98.986667-18.773333-174.08-95.573333-191.146667-194.56z m1.706667-88.746667c18.773333-97.28 93.866667-174.08 189.44-194.56l10.24-1.706667V204.8l-13.653334 3.413333c-133.12 22.186667-242.346667 131.413333-264.533333 264.533334l-3.413333 13.653333h78.506666l3.413334-10.24z m481.28-1.706667l1.706666 10.24h78.506667l-3.413333-13.653333c-20.48-134.826667-129.706667-244.053333-264.533334-266.24l-13.653333-3.413333v78.506666l10.24 1.706667c98.986667 22.186667 174.08 97.28 191.146667 192.853333z m-69.973334 51.2v-11.946666c-5.12-88.746667-76.8-158.72-165.546666-158.72-46.08 0-87.04 17.066667-121.173334 51.2-32.426667 32.426667-51.2 76.8-51.2 121.173333 0 92.16 76.8 167.253333 170.666667 167.253333 92.16-3.413333 167.253333-76.8 167.253333-168.96z m-168.96 90.453334c-51.2 0-92.16-40.96-92.16-92.16s40.96-92.16 92.16-92.16 92.16 40.96 92.16 92.16-40.96 92.16-92.16 92.16z m244.053334-51.2c-20.48 95.573333-95.573333 170.666667-191.146667 196.266666l-10.24 3.413334v73.386666l13.653333-3.413333c133.12-22.186667 244.053333-131.413333 264.533334-264.533333l3.413333-13.653334h-78.506667l-1.706666 8.533334z" p-id="11225"/><path d="M520.533333 51.2C261.12 51.2 51.2 261.12 51.2 520.533333S261.12 989.866667 520.533333 989.866667 989.866667 779.946667 989.866667 520.533333 779.946667 51.2 520.533333 51.2zM122.88 520.533333v-3.413333 3.413333z m430.08 395.946667v-37.546667c0-17.066667-13.653333-32.426667-32.426667-32.426666s-32.426667 13.653333-32.426666 32.426666v37.546667c-194.56-15.36-349.866667-170.666667-365.226667-363.52h39.253333c17.066667 0 32.426667-13.653333 32.426667-32.426667s-13.653333-32.426667-32.426667-32.426666H124.586667c15.36-194.56 170.666667-349.866667 363.52-363.52v37.546666c0 17.066667 13.653333 32.426667 32.426666 32.426667s32.426667-13.653333 32.426667-32.426667V124.586667c194.56 15.36 348.16 170.666667 363.52 363.52h-35.84c-17.066667 0-32.426667 13.653333-32.426667 32.426666s13.653333 32.426667 32.426667 32.426667h35.84c-15.36 192.853333-170.666667 348.16-363.52 363.52z" p-id="11226"/></svg>
                        </div>
                        <div class=" stat-label">
                            <div class="label-header">
                                <span>${indexStatisVO.coverTotalNum}</span>个
                            </div>
                            <div class="clearfix stat-detail">
                                <div class="label-body">
                                    井盖总数
                                </div>
                            </div>
                        </div>
                    </a>
                    <a href="#" class="stat hvr-wobble-horizontal">
                        <div class=" stat-icon blue">
                            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" t="1555926046803" class="icon" style="" viewBox="0 0 1024 1024" version="1.1" p-id="11224"><defs><style type="text/css"/></defs><path d="M276.48 564.906667l-1.706667-10.24h-78.506666l3.413333 13.653333c22.186667 133.12 131.413333 244.053333 264.533333 264.533333l13.653334 3.413334v-75.093334l-10.24-1.706666c-98.986667-18.773333-174.08-95.573333-191.146667-194.56z m1.706667-88.746667c18.773333-97.28 93.866667-174.08 189.44-194.56l10.24-1.706667V204.8l-13.653334 3.413333c-133.12 22.186667-242.346667 131.413333-264.533333 264.533334l-3.413333 13.653333h78.506666l3.413334-10.24z m481.28-1.706667l1.706666 10.24h78.506667l-3.413333-13.653333c-20.48-134.826667-129.706667-244.053333-264.533334-266.24l-13.653333-3.413333v78.506666l10.24 1.706667c98.986667 22.186667 174.08 97.28 191.146667 192.853333z m-69.973334 51.2v-11.946666c-5.12-88.746667-76.8-158.72-165.546666-158.72-46.08 0-87.04 17.066667-121.173334 51.2-32.426667 32.426667-51.2 76.8-51.2 121.173333 0 92.16 76.8 167.253333 170.666667 167.253333 92.16-3.413333 167.253333-76.8 167.253333-168.96z m-168.96 90.453334c-51.2 0-92.16-40.96-92.16-92.16s40.96-92.16 92.16-92.16 92.16 40.96 92.16 92.16-40.96 92.16-92.16 92.16z m244.053334-51.2c-20.48 95.573333-95.573333 170.666667-191.146667 196.266666l-10.24 3.413334v73.386666l13.653333-3.413333c133.12-22.186667 244.053333-131.413333 264.533334-264.533333l3.413333-13.653334h-78.506667l-1.706666 8.533334z" p-id="11225"/><path d="M520.533333 51.2C261.12 51.2 51.2 261.12 51.2 520.533333S261.12 989.866667 520.533333 989.866667 989.866667 779.946667 989.866667 520.533333 779.946667 51.2 520.533333 51.2zM122.88 520.533333v-3.413333 3.413333z m430.08 395.946667v-37.546667c0-17.066667-13.653333-32.426667-32.426667-32.426666s-32.426667 13.653333-32.426666 32.426666v37.546667c-194.56-15.36-349.866667-170.666667-365.226667-363.52h39.253333c17.066667 0 32.426667-13.653333 32.426667-32.426667s-13.653333-32.426667-32.426667-32.426666H124.586667c15.36-194.56 170.666667-349.866667 363.52-363.52v37.546666c0 17.066667 13.653333 32.426667 32.426666 32.426667s32.426667-13.653333 32.426667-32.426667V124.586667c194.56 15.36 348.16 170.666667 363.52 363.52h-35.84c-17.066667 0-32.426667 13.653333-32.426667 32.426666s13.653333 32.426667 32.426667 32.426667h35.84c-15.36 192.853333-170.666667 348.16-363.52 363.52z" p-id="11226"/></svg>
                        </div>
                        <div class=" stat-label">
                            <div class="label-header">
                              <%--  <span>${indexStatisVO.coverTodayNum}</span>个--%>
                                <span>${coverBellNum}</span>个
                            </div>
                            <div class="clearfix stat-detail">
                                <div class="label-body">
                                    监控总数
                                </div>
                            </div>
                        </div>
                    </a>
 <%--                   <a href="#" class="stat hvr-wobble-horizontal">
                        <div class=" stat-icon green">
                            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" t="1555926046803" class="icon" style="" viewBox="0 0 1024 1024" version="1.1" p-id="11224"><defs><style type="text/css"/></defs><path d="M276.48 564.906667l-1.706667-10.24h-78.506666l3.413333 13.653333c22.186667 133.12 131.413333 244.053333 264.533333 264.533333l13.653334 3.413334v-75.093334l-10.24-1.706666c-98.986667-18.773333-174.08-95.573333-191.146667-194.56z m1.706667-88.746667c18.773333-97.28 93.866667-174.08 189.44-194.56l10.24-1.706667V204.8l-13.653334 3.413333c-133.12 22.186667-242.346667 131.413333-264.533333 264.533334l-3.413333 13.653333h78.506666l3.413334-10.24z m481.28-1.706667l1.706666 10.24h78.506667l-3.413333-13.653333c-20.48-134.826667-129.706667-244.053333-264.533334-266.24l-13.653333-3.413333v78.506666l10.24 1.706667c98.986667 22.186667 174.08 97.28 191.146667 192.853333z m-69.973334 51.2v-11.946666c-5.12-88.746667-76.8-158.72-165.546666-158.72-46.08 0-87.04 17.066667-121.173334 51.2-32.426667 32.426667-51.2 76.8-51.2 121.173333 0 92.16 76.8 167.253333 170.666667 167.253333 92.16-3.413333 167.253333-76.8 167.253333-168.96z m-168.96 90.453334c-51.2 0-92.16-40.96-92.16-92.16s40.96-92.16 92.16-92.16 92.16 40.96 92.16 92.16-40.96 92.16-92.16 92.16z m244.053334-51.2c-20.48 95.573333-95.573333 170.666667-191.146667 196.266666l-10.24 3.413334v73.386666l13.653333-3.413333c133.12-22.186667 244.053333-131.413333 264.533334-264.533333l3.413333-13.653334h-78.506667l-1.706666 8.533334z" p-id="11225"/><path d="M520.533333 51.2C261.12 51.2 51.2 261.12 51.2 520.533333S261.12 989.866667 520.533333 989.866667 989.866667 779.946667 989.866667 520.533333 779.946667 51.2 520.533333 51.2zM122.88 520.533333v-3.413333 3.413333z m430.08 395.946667v-37.546667c0-17.066667-13.653333-32.426667-32.426667-32.426666s-32.426667 13.653333-32.426666 32.426666v37.546667c-194.56-15.36-349.866667-170.666667-365.226667-363.52h39.253333c17.066667 0 32.426667-13.653333 32.426667-32.426667s-13.653333-32.426667-32.426667-32.426666H124.586667c15.36-194.56 170.666667-349.866667 363.52-363.52v37.546666c0 17.066667 13.653333 32.426667 32.426666 32.426667s32.426667-13.653333 32.426667-32.426667V124.586667c194.56 15.36 348.16 170.666667 363.52 363.52h-35.84c-17.066667 0-32.426667 13.653333-32.426667 32.426666s13.653333 32.426667 32.426667 32.426667h35.84c-15.36 192.853333-170.666667 348.16-363.52 363.52z" p-id="11226"/></svg>
                        </div>
                        <div class=" stat-label">
                            <div class="label-header">
                                <span>${indexStatisVO.coverNoDepartNum}</span>个
                            </div>
                            <div class="clearfix stat-detail">
                                <div class="label-body">
                                    无组别井盖数
                                </div>
                            </div>
                        </div>
                    </a>--%>
                </div>
                <div class="todo-container bg-blue" style="background-color: #ffffff">
                    <div class="panel-heading">
                        <div class="todo-header text-center">
                            <h4>工单数据明细</h4>
                        </div>
                    </div>
                    <div class="right-worklist-box" style="height: 489px">
                        <a class="item">
                            <img src="${ctxStatic}/common/images/gongdan.png" alt="" />
                            <dl>
                                <dt>${assignNum}<span>单</span> </dt>
                                <dd>今日派单数</dd>
                            </dl>
                        </ a>

                        <a class="item">
                            <img src="${ctxStatic}/common/images/gongdan.png" alt="" />
                            <dl>
                                <dt>${completeNum} <span>单</span> </dt>
                                <dd>已完成数</dd>
                            </dl>
                        </a>

                        <a class="item">
                            <img src="${ctxStatic}/common/images/gongdan.png" alt="" />
                            <dl>
                                <dt>${processingNum}<span>单</span> </dt>
                                <dd>待完成数</dd>
                            </dl>
                        </a>

                        <a class="item">
                            <img src="${ctxStatic}/common/images/gongdan.png" alt="" />
                            <dl>
                                <dt>${overtimeNum}<span>单</span> </dt>
                                <dd>超时工单数</dd>
                            </dl>
                        </a>

                    </div>
                </div>
            </div>
        </div>
        <div class="row home-row">
            <div class="col-md-6 col-lg-6">
                <div class="home-charts-middle">
                    <div class="home-panel-heading panel-heading">
                        <h2>报警类型占比</h2>
                    </div>
                    <div class="chart-container">
                        <div id="damage" style="height: 200px;">
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6 col-lg-6">
                <div class="home-charts-middle">
                    <div class="home-panel-heading panel-heading">
                        <h2>一周报警数据统计</h2>
                    </div>
                    <div class="chart-container">
                        <div id="Workload" style="height:225px"></div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
<script src="vendor/ckeditor/ckeditor.js" type="text/javascript"></script>
<script src="js/vendor.js"></script>
<script src="${ctxStatic}/plugin/echarts3/echarts.min.js"></script>

<script>
    $(function(){
        $('#calendar2').fullCalendar({
            eventClick: function(calEvent, jsEvent, view) {
                alert('Event: ' + calEvent.title);
                alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
                alert('View: ' + view.name);
            }
        });

        $('#rtlswitch').click(function() {
            console.log('hello');
            $('body').toggleClass('rtl');

            var hasClass = $('body').hasClass('rtl');

            $.get('/api/set-rtl?rtl='+ (hasClass ? 'rtl': ''));

        });
        $('.theme-picker').click(function() {
            changeTheme($(this).attr('data-theme'));
        });
        $('#showMenu').click(function() {
            $('body').toggleClass('push-right');
        });

    });
    function changeTheme(the) {
        $("#current-theme").remove();
        $('<link>')
            .appendTo('head')
            .attr('id','current-theme')
            .attr({type : 'text/css', rel : 'stylesheet'})
            .attr('href', '/css/app-'+the+'.css');
    }
</script>

<script>
    $(function(){
        setTimeout(function() {
            $('#world-map').vectorMap({
                backgroundColor: '#FFF',
                regionStyle: {
                    initial: {
                        fill: 'black',
                        "fill-opacity": 1,
                        stroke: 'none',
                        "stroke-width": 0,
                        "stroke-opacity": 1
                    },
                    hover: {
                        "fill-opacity": 0.8,
                        cursor: 'pointer',
                    },
                    selected: {
                        fill: 'red'
                    },
                    selectedHover: {
                    }
                }
            });
        }, 275);
        // 柱状图
        var option1 = {
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            //控制4周margin
            grid:{
                x:50,
                x2:50,
                y:40,
                y2:20
            },
            legend: {
                data: ['报警数量', '报警日期']
            },
            //x坐标轴
            xAxis: [
                {
                    name: '报警日期',
                    type: 'category',
                    data: [${indexStatisVO.alarmDateArr[6]}, ${indexStatisVO.alarmDateArr[5]}, ${indexStatisVO.alarmDateArr[4]}, ${indexStatisVO.alarmDateArr[3]}, ${indexStatisVO.alarmDateArr[2]}, ${indexStatisVO.alarmDateArr[1]},${indexStatisVO.alarmDateArr[0]}],
                    axisPointer: {
                        type: 'shadow'
                    },
                    //设置轴线的属性
                    axisLine:{
                        lineStyle:{
                            color:'#ddd',
                        }
                    },
                    axisLabel: {
                        show: true,
                        textStyle: {
                            color: '#333'
                        }
                    }
                }
            ],
            //y坐标轴
            yAxis: [
                {
                    type: 'value',
                    name: '报警数量',
                    min: 0,
                    max: 60,
                    interval: 5,
                    axisLabel: {
                        formatter: '{value}'
                    },
                    //设置轴线的属性
                    axisLine:{
                        lineStyle:{
                            color:'#ddd',
                        }
                    },
                    //颜色
                    axisLabel: {
                        show: true,
                        textStyle: {
                            color: '#333'
                        }
                    },
                    //分割线
                    splitLine: {
                        show: true,
                        lineStyle:{
                            type:'dashed',
                            color:'#eee',
                        }
                    }

                }
            ],
            series: [
                {
                    name: '报警数量',
                    type: 'bar',
                    barWidth: 45, //柱图宽度
                    itemStyle:{
                        normal:{
                            color:'#69d2e7'
                        }
                    },
                    data: [${indexStatisVO.alarmNumArr[6]}, ${indexStatisVO.alarmNumArr[5]}, ${indexStatisVO.alarmNumArr[4]}, ${indexStatisVO.alarmNumArr[3]}, ${indexStatisVO.alarmNumArr[2]}, ${indexStatisVO.alarmNumArr[1]},${indexStatisVO.alarmNumArr[0]}]
                }
            ]
        };
        var myChart1 = echarts.init(document.getElementById('Workload')); //应用dark主题
        myChart1.setOption(option1);

        //环形图
        var labelTop = {
            normal : {
                label : {
                    show : false,
                    position : 'center',
                    formatter : '{b}',
                    textStyle: {
                        baseline : 'bottom'
                    }
                },
                labelLine : {
                    show : false
                }
            }
        };
        var labelFromatter = {
            normal : {
                label : {
                    formatter : function (params){
                        return (100 - params.value).toFixed(2) + '%'
                        //return params.name
                    },
                    textStyle: {
                        baseline : 'middle',
                        color: '#333'
                    }
                }
            },
        }
        var labelBottom = {
            normal : {
                color: '#ccc',
                label : {
                    show : true,
                    position : 'center'
                },
                labelLine : {
                    show : false
                }
            },
            emphasis: {
                //color: 'rgba(0,0,0,0)'
            }
        };
        var radius = [30, 48];
      /*  var option2 = {
            backgroundColor:'#fff',
            legend: {
                x : 'center',
                y : 'bottom',
                data:[
                    '完好','井盖缺失','井盖破坏','井周沉降龟裂','井筒本身破坏',
                    '其他'
                ]
            },
            toolbox: {
                show : true,
                feature : {
                    magicType : {
                        show: true,
                        type: ['pie', 'funnel'],
                        option: {
                            funnel: {
                                width: '20%',
                                height: '30%',
                                itemStyle : {
                                    normal : {
                                        label : {
                                            formatter : function (params){
                                                return 'other\n' + (params.value).toFixed(2) + '%\n'
                                            },
                                            textStyle: {
                                                baseline : 'middle'
                                            }
                                        }
                                    },
                                }
                            }
                        }
                    }
                }
            },
            series : [
                {
                    type : 'pie',
                    center : ['9%', '40%'],
                    radius : radius,
                    x: '0%', // for funnel
                    itemStyle : labelFromatter,
                    data : [
                        {name:'全部', value:100-${indexStatisVO.perDamageGoodNum}, itemStyle : labelBottom},
                        {name:'完好', value:${indexStatisVO.perDamageGoodNum}, itemStyle : labelTop}
                    ]
                },
                {
                    type : 'pie',
                    center : ['26%', '40%'],
                    radius : radius,
                    x: '16%', // for funnel
                    itemStyle : labelFromatter,
                    data : [
                        {name:'全部', value:100-${indexStatisVO.perDamageDefectNum}, itemStyle : labelBottom},
                        {name:'井盖缺失', value:${indexStatisVO.perDamageDefectNum},itemStyle : labelTop}
                    ]
                },
                {
                    type : 'pie',
                    center : ['43%', '40%'],
                    radius : radius,
                    x: '32%', // for funnel
                    itemStyle : labelFromatter,
                    data : [
                        {name:'全部', value:100-${indexStatisVO.perDamageDestroyNum}, itemStyle : labelBottom},
                        {name:'井盖破坏', value:${indexStatisVO.perDamageDestroyNum},itemStyle : labelTop}
                    ]
                },

                {
                    type : 'pie',
                    center : ['59%', '40%'],
                    radius : radius,
                    x: '64%', // for funnel
                    itemStyle : labelFromatter,
                    data : [
                        {name:'全部', value:100-${indexStatisVO.perDamageRiftNum}, itemStyle : labelBottom},
                        {name:'井周沉降龟裂', value:${indexStatisVO.perDamageRiftNum},itemStyle : labelTop}
                    ]
                },
                {
                    type : 'pie',
                    center : ['76%', '40%'],
                    radius : radius,
                    x: '80%', // for funnel
                    itemStyle : labelFromatter,
                    data : [
                        {name:'全部', value:100-${indexStatisVO.perDamageOwnerNum}, itemStyle : labelBottom},
                        {name:'井筒本身破坏', value:${indexStatisVO.perDamageOwnerNum},itemStyle : labelTop}
                    ]
                },
                {
                    type : 'pie',
                    center : ['92%', '40%'],
                    radius : radius,
                    x: '96%', // for funnel
                    itemStyle : labelFromatter,
                    data : [
                        {name:'全部', value:100-${indexStatisVO.perDamageOtherNum}, itemStyle : labelBottom},
                        {name:'其他', value:${indexStatisVO.perDamageOtherNum},itemStyle : labelTop}
                    ]
                }
            ]
        };*/

        var option3 = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                x: 'left',
                data:[${data1}]
            },
            series: [
                {
                    name:'报警类型',
                    type:'pie',
                    radius: ['50%', '70%'],
                    avoidLabelOverlap: false,
                    label: {
                        normal: {
                            show: false,
                            position: 'center'
                        },
                        emphasis: {
                            show: true,
                            textStyle: {
                                fontSize: '30',
                                fontWeight: 'bold'
                            }
                        }
                    },
                    labelLine: {
                        normal: {
                            show: false
                        }
                    },
                    data:[
                        ${data2}
                    ]
                }
            ]
        };
        var myChart2 = echarts.init(document.getElementById('damage')); //应用dark主题
        myChart2.setOption(option3);

    });
</script>

</body>
</html>
