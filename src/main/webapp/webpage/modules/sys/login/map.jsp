
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

  <%--<style>--%>
      <%--.btn-primary {--%>
          <%--color: #fff;--%>
          <%--background-color: #3ca2e0;--%>
          <%--border-color: #2698dd;--%>
      <%--}--%>
      <%--.btn {--%>
          <%--display: inline-block;--%>
          <%--margin-bottom: 0;--%>
          <%--font-weight: normal;--%>
          <%--text-align: center;--%>
          <%--vertical-align: middle;--%>
          <%---ms-touch-action: manipulation;--%>
          <%--touch-action: manipulation;--%>
          <%--cursor: pointer;--%>
          <%--background-image: none;--%>
          <%--border: 1px solid transparent;--%>
          <%--white-space: nowrap;--%>
          <%--padding: 6px 12px;--%>
          <%--font-size: 14px;--%>
          <%--line-height: 1.42857143;--%>
          <%--border-radius: 0px;--%>
          <%---webkit-user-select: none;--%>
          <%---moz-user-select: none;--%>
          <%---ms-user-select: none;--%>
          <%--user-select: none;--%>
          <%--box-shadow: 3px 3px 5px rgba(0,0,0,0.2);--%>
      <%--}--%>
      <%--#areaMap{--%>
          <%--position: relative;--%>
      <%--}--%>
  <%--</style>--%>
</head>
<body>
    <input id='district'  type="hidden" value=''>
    <div id="container" style="height: 380px;width: 100%;padding: 0;margin: 0" ></div>
    <script type="text/javascript">

        var map = new AMap.Map('container', {
            resizeEnable: true,
            //zoom:14,//级别
        });
        map.setCity('徐州');
        map.on('click', logMapinfo);
        map.on('dblclick', logMapinfo);



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
                alert(info.district);
                drawBounds();
            });
        }
        var district = null;
        var polygons=[];

        function drawBounds() {
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
                                    fillColor: '#80d8ff',
                                    strokeColor: '#0091ea'
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
