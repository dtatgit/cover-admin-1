
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>徐州地图</title>
    <script src="http://webapi.amap.com/maps?v=1.4.6&key=06de357afd269944d97de0abcde0f4e0&plugin=AMap.DistrictSearch"></script>
    <script type="text/javascript" src="https://a.amap.com/jsapi_demos/static/demo-center/js/jquery-1.11.1.min.js" ></script>
    <script type="text/javascript" src="https://a.amap.com/jsapi_demos/static/demo-center/js/underscore-min.js" ></script>
    <script type="text/javascript" src="https://a.amap.com/jsapi_demos/static/demo-center/js/backbone-min.js" ></script>
    <script type="text/javascript" src='https://a.amap.com/jsapi_demos/static/demo-center/js/prety-json.js'></script>

   <%--<script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.14&06de357afd269944d97de0abcde0f4e0&plugin=AMap.DistrictSearch"></script>--%>

    <%--//<link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>--%>
<%--    <script type="text/javascript" src="js/jquery1.42.min.js"></script>
    <script type="text/javascript" src="js/echarts-all-2.js"></script>
    <script type="text/javascript" src="js/esl.js"></script>
    <script src="js/bmap.js" ></script>--%>

  <style type="text/css">
      .statistics-box{
          position: fixed;
          left: 20px;
          bottom: 20px;
          background: #fff;
          box-shadow: 2px 2px 5px rgba(0,0,0,0.2);
          -webkit-border-radius: 5px;
          -moz-border-radius: 5px;
          border-radius: 5px;
          padding: 15px;
          z-index: 10;
      }
      .statistics-header{
          display: flex;
          align-items: center;
          margin-bottom: 15px;
      }
      .statistics-header:before{
          content: "";
          background: #37adc4;
          width: 4px;
          height: 15px;
      }
      .statistics-header h2{
          padding-left: 10px;
          font-size: 14px;
          margin: 0;
      }
      .statistics-list{
          padding-left: 0;
          margin: 0;
      }
      .statistics-list li{
          font-size: 14px;
          padding:0px 5px;
          display: flex;
          align-items: center;
          color: #555;
          cursor: pointer;
          margin-bottom: 10px;
      }
      .statistics-list li:last-child{
          margin-bottom: 0;
      }
      .statistics-list li span{
          color:#f85f40;
          margin: 0 5px;
      }
      .statistics-list li:before{
          width: 16px;
          height: 8px;
          content: "";
          background: #ffc268;
          margin-right: 5px;
          border-radius: 5px;
      }
      .statistics-list li:nth-child(2):before{
          background: #6fd1e4;
      }
      .statistics-list li:nth-child(3):before{
          background: #f07676;
      }
      .statistics-list li:nth-child(4):before{
          background: #99d96f;
      }
      .statistics-list li.active{
           background: #37adc4;
           color: #fff;
           border-radius: 10px;
       }
      .statistics-list li.active span{
          color: #fff;
      }
      .statistics-list li.active:before{
          background: #fff;
      }
  </style>
</head>
<body>
<!--//全部统计-->
    <div class="statistics-box">
        <div class="statistics-header">
            <h2>井盖统计</h2>
        </div>
        <div class="statistics-item">
            <ul class="statistics-list">
                <li class="active" id="鼓楼区">鼓楼区:<span><div id="glq"></div></span>个</li>
                <li id="泉山区">泉山区:<span><div id="qsq"></div></span>个</li>
                <li id="云龙区">云龙区:<span><div id="ylq"></div></span>个</li>
                <li id="铜山区">铜山区:<span><div id="tsq"></div></span>个</li>
            </ul>
        </div>
    </div>
<!--全部统计//-->
    <input id='district'  type="hidden" value='鼓楼区'>
    <div id="container" style="height: 380px;width: 100%;padding: 0;margin: 0" ></div>
    <script type="text/javascript">
        //url，将需要的数据查询出来，放到固定的的位置即可。

        var glq=0;
        var tsq=0;
        var ylq=0;
        var qsq=0;
        $.ajax({
            type: "POST",
            url: "${ctx}/cv/equinfo/cover/mapdatas",
            async:false,
            dataType: "json",
            success: function(data){

                glq=data.data.glq;
                tsq=data.data.tsq;
                ylq=data.data.ylq;
                qsq=data.data.qsq;
                $("#glq").html(glq);
                $("#tsq").html(tsq);
                $("#ylq").html(ylq);
                $("#qsq").html(qsq);
            }
        });

        var map = new AMap.Map('container', {
            resizeEnable: true,
            zooms:[10,15]
            //zoom:14,//级别
        });
        map.setCity('徐州');
        map.on('click', logMapinfo);
        map.on('dblclick', logMapinfo);
        map.on('zoomstart', mapZoomstart);
        map.on('zoomchange', mapZoom);
        map.on('zoomend', mapZoomend);


        $(function() {
            addClassName();
            drawBounds();
        })

        //点击加class
        function addClassName() {
            $(".statistics-list li").bind("click",function () {
                $(this).addClass("active").siblings().removeClass("active");
                var id = $(this).attr("id");
                $("#district").val(id);
                drawBounds(getColor(id));
            })
        }
        function mapZoomstart(){
            // alert("缩放开始");

        }
        function mapZoom(){
            logMapzoom();
            //alert("正在缩放");

        }
        function mapZoomend(){
            // alert("缩放结束");

        }
        var logMapzoom = function (){
            var zoom = map.getZoom(); //获取当前地图级别
            //alert(zoom);
            var bounds = map.getBounds();

            var n= bounds.getNorthEast().lat; // 北
            var e= bounds.getNorthEast().lng; // 东
            var s= bounds.getSouthWest().lat; // 南
            var w=bounds.getSouthWest().lng; // 西
            /*            alert(n);
                        alert(e);
                        alert(s);
                        alert(w);*/

        };
        // function showInfoClick(e){
        //     var text = '您在 [ '+e.lnglat.getLng()+','+e.lnglat.getLat()+' ] 的位置单击了地图！'
        //     alert(text);
        //     alert(e.city);
        //     //document.querySelector("#text").innerText = text;
        // }
        //获取并展示当前城市信息
        function logMapinfo(){
            map.getCity( function(info){
               //省：province，市;city，citycode,district
                $("#district").val(info.district);
               // alert(info.district);
                drawBounds(getColor(info.district));
            });
        }
        var district = null;
        var polygons=[];

        function drawBounds(color) {
            //加载行政区划插件
            if(!district){
                //实例化DistrictSearch
                var opts = {
                    subdistrict: 0,   //获取边界不需要返回下级行政区
                    extensions: 'all',  //返回行政区边界坐标组等具体信息
                    level: 'district'  //查询行政级别为 市
                };
                district = new AMap.DistrictSearch(opts);
            }

            //行政区查询

            district.setLevel("district")
            district.search(document.getElementById('district').value, function(status, result) {
                map.remove(polygons)//清除上次结果
                polygons = [];
                for(var i=0;i<result.districtList.length;i++){

                    if(result.districtList[i].citycode=="0516"){
                        var bounds = result.districtList[i].boundaries;
                        if (bounds) {
                            for (var i = 0, l = bounds.length; i < l; i++) {
                                //生成行政区划polygon
                                var polygon = new AMap.Polygon({
                                    strokeWeight: 1,
                                    path: bounds[i],
                                    fillOpacity: 0.4,
                                    fillColor: color,
                                    strokeColor:color
                                });
                                polygons.push(polygon);
                            }
                        }
                        map.add(polygons)
                        map.setFitView(polygons);//视口自适应
                    }

                    // alert(result.districtList[i].name);
                    // alert(result.districtList[i].citycode);
                }

            });
        }
        //drawBounds();
function   getColor(name){
    if(name=="鼓楼区"){
        return "#ffc268";
    }else if(name=="泉山区"){
        return "#6fd1e4";
    }else if(name=="云龙区"){
        return "#f07676";
    }else if(name=="铜山区"){
        return "#99d96f";
    }else {
        return "#80d8ff";
    }
}
        function initMapData(data) {
            $.each(data,function(key,value){

                if(!value.lng || !value.lat){
                    return;
                }

                var lng =value.lng;
                var lat =value.lat;
                // var address =value.address;
                // var lastUpdateTime = value.lastUpdateTime;
                //var status = value.status;

                var lnglat = new AMap.LngLat(lng, lat); //一个点

                var markericon = m1;
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
            info.push("井盖编号 ："+value.no);
            info.push("详细地址 ："+ value.address + "</div>");
            var infoWindow = new AMap.InfoWindow({
                offset: new AMap.Pixel(0, -29),
                content:  info.join("<br/>"),  //使用默认信息窗体框样式，显示信息内容
            });

            marker.on("mouseover", function(e) {
                infoWindow.open(map, e.target.getPosition());
            });
            marker.on("mouseout", function() {
                infoWindow.close();
            });

        }
    </script>
</body>
</html>
