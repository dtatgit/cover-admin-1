
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<c:set var="ctx" value="${pageContext.request.contextPath}${fns:getAdminPath()}"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>徐州地图</title>
    <!--<script src="http://webapi.amap.com/maps?v=1.4.6&key=06de357afd269944d97de0abcde0f4e0&plugin=AMap.DistrictSearch"></script>-->
    <script src="http://webapi.amap.com/maps?v=1.4.6&key=1350cad6a9086d35e9478ab8c3f7afe0&plugin=AMap.DistrictSearch"></script>

    <script type="text/javascript" src="https://a.amap.com/jsapi_demos/static/demo-center/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="https://a.amap.com/jsapi_demos/static/demo-center/js/underscore-min.js"></script>
    <script type="text/javascript" src="https://a.amap.com/jsapi_demos/static/demo-center/js/backbone-min.js"></script>
    <script type="text/javascript" src='https://a.amap.com/jsapi_demos/static/demo-center/js/prety-json.js'></script>

    <style type="text/css">
        .statistics-box {
            position: fixed;
            left: 30px;
            top: 120px;
            z-index: 10;
        }

        .statistics-header {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
            display: none;
        }

        .statistics-header:before {
            content: "";
            background: #37adc4;
            width: 4px;
            height: 15px;
        }

        .statistics-header h2 {
            padding-left: 10px;
            font-size: 14px;
            margin: 0;
        }

        .statistics-list {
            padding-left: 0;
            margin: 0;
        }

        .statistics-list li {
            font-size: 14px;
            padding: 0px 5px;
            display: flex;
            align-items: center;
            color: #555;
            cursor: pointer;
            margin-bottom: 10px;
            background: #fff;
            box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
            -webkit-border-radius: 5px;
            -moz-border-radius: 5px;
            border-radius: 5px;
            padding: 15px;
        }
        .statistics-list li.active{
            background: #e7f8d6;
        }

        .statistics-list li:last-child {
            margin-bottom: 0;
        }

        .statistics-list li .icon-svg{
            margin-left: -10px;
        }
        .statistics-list li .icon-svg svg{
            width: 45px;
            height: 45px;
            fill: #ffc268;
        }

        .statistics-list li .icon-svg.oranger svg{
            fill: #ffc268;
        }
        .statistics-list li .icon-svg.red svg{
            fill: #f07676;
        }
        .statistics-list li .icon-svg.blue svg{
            fill: #6fd1e4;
        }
        .statistics-list li .icon-svg.green svg{
            fill: #66FF7F;
        }
        .statistics-infor h2{
            font-size: 14px;
            color: #555;
            margin: 0;
            font-weight: 400;
        }
        .infor-num{
            color: #555;
        }
        .infor-num span{
            font-size: 24px;
            margin-right: 5px;;
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
            <li id="云龙区">
                <div class="icon-svg red">
                    <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" t="1555925634759" class="icon" style="" viewBox="0 0 1024 1024" version="1.1" p-id="9264">
                        <defs>
                            <style type="text/css" />
                        </defs>
                        <path d="M538.46 836.86c158.05-12.97 284.25-140.36 297.23-298.41H806.2c-14.15 0-25.95-11.79-25.95-25.95s11.79-25.95 25.95-25.95h29.49C822.72 328.5 696.51 201.12 537.28 188.14v30.67c0 14.15-11.79 25.95-25.95 25.95-14.15 0-25.95-11.79-25.95-25.95v-30.67c-159.23 11.79-285.43 139.18-298.41 298.41h31.85c14.15 0 25.95 11.79 25.95 25.95s-11.79 25.95-25.95 25.95h-31.85c12.97 159.23 140.36 285.43 299.59 298.41v-30.67c0-14.15 11.79-25.95 25.95-25.95s25.95 11.79 25.95 25.95v30.67zM187.83 514.95v-4.81 4.81z m323.5-386.96c212.31 0 384.51 172.2 384.51 384.51s-172.2 384.51-384.51 384.51-384.51-172.2-384.51-384.51 172.21-384.51 384.51-384.51z m199.33 421.07l2.36-8.26h64.87l-2.36 11.79c-17.69 109.7-107.33 199.34-217.02 217.04l-11.79 2.36v-60.15l7.08-2.36c79.02-20.06 140.35-82.57 156.86-160.42z m-199.33 41.29c42.46 0 75.49-33.03 75.49-75.49 0-42.46-33.03-75.49-75.49-75.49s-75.49 33.03-75.49 75.49c0.01 42.46 33.03 75.49 75.49 75.49z m138-73.13c0 75.49-61.33 135.64-135.64 138-77.85 0-140.36-61.33-140.36-136.82 0-36.56 15.33-73.13 41.28-99.08 27.13-27.13 61.33-41.28 99.08-41.28 73.13 0 130.92 56.61 135.64 129.74v9.44z m57.8-42.46c-14.15-77.85-75.49-139.18-156.87-156.87l-8.26-1.18v-64.87l11.79 2.36c109.69 17.69 199.33 107.33 217.02 217.02l2.36 11.79H708.3l-1.17-8.25z m-393.95 1.18l-2.36 9.44h-64.87l2.36-11.79C266 363.89 355.64 274.25 465.33 256.55l11.79-2.36v61.33l-8.26 1.18c-79.01 16.52-140.35 79.03-155.68 159.24zM312 549.06c12.97 81.38 74.31 143.9 156.87 160.41l8.26 1.18v61.33l-11.79-2.36C355.64 751.93 266 662.29 248.31 552.6l-2.36-11.79h64.87l1.18 8.25z" p-id="9265" />
                    </svg>
                </div>
                <div class="statistics-infor">
                    <h2>云龙区</h2>
                    <div class="infor-num"><span id="ylq">129,850</span>个</div>
                </div>
            </li>
            <li id="鼓楼区">
                <div class="icon-svg oranger">
                    <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" t="1555925634759" class="icon" style="" viewBox="0 0 1024 1024" version="1.1" p-id="9264">
                        <defs>
                            <style type="text/css" />
                        </defs>
                        <path d="M538.46 836.86c158.05-12.97 284.25-140.36 297.23-298.41H806.2c-14.15 0-25.95-11.79-25.95-25.95s11.79-25.95 25.95-25.95h29.49C822.72 328.5 696.51 201.12 537.28 188.14v30.67c0 14.15-11.79 25.95-25.95 25.95-14.15 0-25.95-11.79-25.95-25.95v-30.67c-159.23 11.79-285.43 139.18-298.41 298.41h31.85c14.15 0 25.95 11.79 25.95 25.95s-11.79 25.95-25.95 25.95h-31.85c12.97 159.23 140.36 285.43 299.59 298.41v-30.67c0-14.15 11.79-25.95 25.95-25.95s25.95 11.79 25.95 25.95v30.67zM187.83 514.95v-4.81 4.81z m323.5-386.96c212.31 0 384.51 172.2 384.51 384.51s-172.2 384.51-384.51 384.51-384.51-172.2-384.51-384.51 172.21-384.51 384.51-384.51z m199.33 421.07l2.36-8.26h64.87l-2.36 11.79c-17.69 109.7-107.33 199.34-217.02 217.04l-11.79 2.36v-60.15l7.08-2.36c79.02-20.06 140.35-82.57 156.86-160.42z m-199.33 41.29c42.46 0 75.49-33.03 75.49-75.49 0-42.46-33.03-75.49-75.49-75.49s-75.49 33.03-75.49 75.49c0.01 42.46 33.03 75.49 75.49 75.49z m138-73.13c0 75.49-61.33 135.64-135.64 138-77.85 0-140.36-61.33-140.36-136.82 0-36.56 15.33-73.13 41.28-99.08 27.13-27.13 61.33-41.28 99.08-41.28 73.13 0 130.92 56.61 135.64 129.74v9.44z m57.8-42.46c-14.15-77.85-75.49-139.18-156.87-156.87l-8.26-1.18v-64.87l11.79 2.36c109.69 17.69 199.33 107.33 217.02 217.02l2.36 11.79H708.3l-1.17-8.25z m-393.95 1.18l-2.36 9.44h-64.87l2.36-11.79C266 363.89 355.64 274.25 465.33 256.55l11.79-2.36v61.33l-8.26 1.18c-79.01 16.52-140.35 79.03-155.68 159.24zM312 549.06c12.97 81.38 74.31 143.9 156.87 160.41l8.26 1.18v61.33l-11.79-2.36C355.64 751.93 266 662.29 248.31 552.6l-2.36-11.79h64.87l1.18 8.25z" p-id="9265" />
                    </svg>
                </div>
                <div class="statistics-infor">
                    <h2>鼓楼区</h2>
                    <div class="infor-num"><span id="glq">59,475</span>个</div>
                </div>
            </li>
            <li id="泉山区">
                <div class="icon-svg blue">
                    <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" t="1555925634759" class="icon" style="" viewBox="0 0 1024 1024" version="1.1" p-id="9264">
                        <defs>
                            <style type="text/css" />
                        </defs>
                        <path d="M538.46 836.86c158.05-12.97 284.25-140.36 297.23-298.41H806.2c-14.15 0-25.95-11.79-25.95-25.95s11.79-25.95 25.95-25.95h29.49C822.72 328.5 696.51 201.12 537.28 188.14v30.67c0 14.15-11.79 25.95-25.95 25.95-14.15 0-25.95-11.79-25.95-25.95v-30.67c-159.23 11.79-285.43 139.18-298.41 298.41h31.85c14.15 0 25.95 11.79 25.95 25.95s-11.79 25.95-25.95 25.95h-31.85c12.97 159.23 140.36 285.43 299.59 298.41v-30.67c0-14.15 11.79-25.95 25.95-25.95s25.95 11.79 25.95 25.95v30.67zM187.83 514.95v-4.81 4.81z m323.5-386.96c212.31 0 384.51 172.2 384.51 384.51s-172.2 384.51-384.51 384.51-384.51-172.2-384.51-384.51 172.21-384.51 384.51-384.51z m199.33 421.07l2.36-8.26h64.87l-2.36 11.79c-17.69 109.7-107.33 199.34-217.02 217.04l-11.79 2.36v-60.15l7.08-2.36c79.02-20.06 140.35-82.57 156.86-160.42z m-199.33 41.29c42.46 0 75.49-33.03 75.49-75.49 0-42.46-33.03-75.49-75.49-75.49s-75.49 33.03-75.49 75.49c0.01 42.46 33.03 75.49 75.49 75.49z m138-73.13c0 75.49-61.33 135.64-135.64 138-77.85 0-140.36-61.33-140.36-136.82 0-36.56 15.33-73.13 41.28-99.08 27.13-27.13 61.33-41.28 99.08-41.28 73.13 0 130.92 56.61 135.64 129.74v9.44z m57.8-42.46c-14.15-77.85-75.49-139.18-156.87-156.87l-8.26-1.18v-64.87l11.79 2.36c109.69 17.69 199.33 107.33 217.02 217.02l2.36 11.79H708.3l-1.17-8.25z m-393.95 1.18l-2.36 9.44h-64.87l2.36-11.79C266 363.89 355.64 274.25 465.33 256.55l11.79-2.36v61.33l-8.26 1.18c-79.01 16.52-140.35 79.03-155.68 159.24zM312 549.06c12.97 81.38 74.31 143.9 156.87 160.41l8.26 1.18v61.33l-11.79-2.36C355.64 751.93 266 662.29 248.31 552.6l-2.36-11.79h64.87l1.18 8.25z" p-id="9265" />
                    </svg>
                </div>
                <div class="statistics-infor">
                    <h2>泉山区</h2>
                    <div class="infor-num"><span id="qsq">21,732</span>个</div>
                </div>
            </li>
            <li id="铜山区">
                <div class="icon-svg green">
                    <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" t="1555925634759" class="icon" style="" viewBox="0 0 1024 1024" version="1.1" p-id="9264">
                        <defs>
                            <style type="text/css" />
                        </defs>
                        <path d="M538.46 836.86c158.05-12.97 284.25-140.36 297.23-298.41H806.2c-14.15 0-25.95-11.79-25.95-25.95s11.79-25.95 25.95-25.95h29.49C822.72 328.5 696.51 201.12 537.28 188.14v30.67c0 14.15-11.79 25.95-25.95 25.95-14.15 0-25.95-11.79-25.95-25.95v-30.67c-159.23 11.79-285.43 139.18-298.41 298.41h31.85c14.15 0 25.95 11.79 25.95 25.95s-11.79 25.95-25.95 25.95h-31.85c12.97 159.23 140.36 285.43 299.59 298.41v-30.67c0-14.15 11.79-25.95 25.95-25.95s25.95 11.79 25.95 25.95v30.67zM187.83 514.95v-4.81 4.81z m323.5-386.96c212.31 0 384.51 172.2 384.51 384.51s-172.2 384.51-384.51 384.51-384.51-172.2-384.51-384.51 172.21-384.51 384.51-384.51z m199.33 421.07l2.36-8.26h64.87l-2.36 11.79c-17.69 109.7-107.33 199.34-217.02 217.04l-11.79 2.36v-60.15l7.08-2.36c79.02-20.06 140.35-82.57 156.86-160.42z m-199.33 41.29c42.46 0 75.49-33.03 75.49-75.49 0-42.46-33.03-75.49-75.49-75.49s-75.49 33.03-75.49 75.49c0.01 42.46 33.03 75.49 75.49 75.49z m138-73.13c0 75.49-61.33 135.64-135.64 138-77.85 0-140.36-61.33-140.36-136.82 0-36.56 15.33-73.13 41.28-99.08 27.13-27.13 61.33-41.28 99.08-41.28 73.13 0 130.92 56.61 135.64 129.74v9.44z m57.8-42.46c-14.15-77.85-75.49-139.18-156.87-156.87l-8.26-1.18v-64.87l11.79 2.36c109.69 17.69 199.33 107.33 217.02 217.02l2.36 11.79H708.3l-1.17-8.25z m-393.95 1.18l-2.36 9.44h-64.87l2.36-11.79C266 363.89 355.64 274.25 465.33 256.55l11.79-2.36v61.33l-8.26 1.18c-79.01 16.52-140.35 79.03-155.68 159.24zM312 549.06c12.97 81.38 74.31 143.9 156.87 160.41l8.26 1.18v61.33l-11.79-2.36C355.64 751.93 266 662.29 248.31 552.6l-2.36-11.79h64.87l1.18 8.25z" p-id="9265" />
                    </svg>
                </div>
                <div class="statistics-infor">
                    <h2>铜山区</h2>
                    <div class="infor-num"><span id="tsq">16,546</span>个</div>
                </div>
            </li>
        </ul>
    </div>
</div>
<!--全部统计//-->
<input id='district' type="hidden" value='鼓楼区'>
<div id="container" style="height: 710px;width: 100%;padding: 0;margin: 0"></div>
<script type="text/javascript">



    var glq=0;
    var tsq=0;
    var ylq=0;
    var qsq=0;

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=UTF-8",
        url: "${ctx}/cv/equinfo/cover/mapdatas",
        async:false,
        dataType: "json",
        success: function(data){

            glq=data.data.glq;
            tsq=data.data.tsq;
            ylq=data.data.ylq;
            qsq=data.data.qsq;
            //document.getElementById("glq").html(glq);
            $("#glq").html(glq);
            $("#tsq").html(tsq);
            $("#ylq").html(ylq);
            $("#qsq").html(qsq);
        }
    });
    var map = new AMap.Map('container', {
        resizeEnable: true,
        mapStyle: 'amap://styles/73e23b40d62ba5964eb6f389719580cb',
        zooms: [9, 11]
        //zoom:14,//级别
    });
    map.setCity('徐州');
    //map.on('click', logMapinfo);


    $(function() {
        addClassName();
        drawBounds("鼓楼区");
        drawBounds("铜山区");
        drawBounds("泉山区");
        drawBounds("云龙区");
    })

    //点击加class
    function addClassName() {
        $(".statistics-list li").bind("click", function() {
            $(this).addClass("active").siblings().removeClass("active");
            var id = $(this).attr("id");
            $("#district").val(id);
            //drawBounds(id);
        })
    }

    // function showInfoClick(e){
    //     var text = '您在 [ '+e.lnglat.getLng()+','+e.lnglat.getLat()+' ] 的位置单击了地图！'
    //     alert(text);
    //     alert(e.city);
    //     //document.querySelector("#text").innerText = text;
    // }
    //获取并展示当前城市信息
    function logMapinfo() {
        map.getCity(function(info) {
            //省：province，市;city，citycode,district
            $("#district").val(info.district);
            // alert(info.district);
            drawBounds(getColor(info.district));
        });
    }

    function drawBounds(areaName) {
        var district = null;
        var polygons = [];
        var color = getColor(areaName);
        //加载行政区划插件
        if(!district) {
            //实例化DistrictSearch
            var opts = {
                subdistrict: 0, //获取边界不需要返回下级行政区
                extensions: 'all', //返回行政区边界坐标组等具体信息
                level: 'district' //查询行政级别为 市
            };
            district = new AMap.DistrictSearch(opts);
        }

        //行政区查询

        district.setLevel("district")
        district.search(areaName, function(status, result) {
            //map.remove(polygons)//清除上次结果
            polygons = [];
            for(var i = 0; i < result.districtList.length; i++) {

                if(result.districtList[i].citycode == "0516") {
                    var bounds = result.districtList[i].boundaries;
                    if(bounds) {
                        for(var i = 0, l = bounds.length; i < l; i++) {
                            //生成行政区划polygon
                            var polygon = new AMap.Polygon({
                                strokeWeight: 1,
                                path: bounds[i],
                                fillOpacity: 0.5,
                                fillColor: color,
                                strokeColor: color
                            });
                            polygons.push(polygon);
                        }
                    }
                    map.add(polygons)
                    // map.setFitView(polygons);//视口自适应
                }

                // alert(result.districtList[i].name);
                // alert(result.districtList[i].citycode);
            }

        });
    }
    //drawBounds();
    function getColor(name) {
        if(name == "鼓楼区") {
            return "#fc9939";
        } else if(name == "泉山区") {
            return "#6fd1e4";
        } else if(name == "云龙区") {
            return "#FF7547";
        } else if(name == "铜山区") {
            return "#66FFCC";
        } else {
            return "#7F66FF";
        }
    }
</script>
</body>

</html>