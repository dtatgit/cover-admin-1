
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <title>徐州地图</title>
    <script src="http://webapi.amap.com/maps?v=1.4.6&key=06de357afd269944d97de0abcde0f4e0"></script>
    <script type="text/javascript" src="js/jquery1.42.min.js"></script>
    <!--<script type="text/javascript" src="js/echarts.min.js"></script>-->
    <script type="text/javascript" src="js/echarts-all-2.js"></script>
    <script type="text/javascript" src="js/esl.js"></script>
    <script src="js/bmap.js" ></script>
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
                    }
                });




        var option = {
            backgroundColor: 'transparent', //'rgba(27,27,27,0.2)', // 底色
            color: ['gold', 'aqua', 'lime'],
            title: {
                text: '',
                subtext: '',
                x: 'center',
                textStyle: {
                    color: 'black'
                }
            },
            tooltip: {

                trigger: 'item',//axis
                formatter: '{b}' + '提报数据量' + '{c}'// 可以设置回调
            },
            legend: {
                show: false,
                orient: 'vertical',
                data: ['数据提报'],
                selectedMode: 'single',
                textStyle: {
                    color: '#fffffff'
                }
            },
            toolbox: {
                show: false
            },
            dataRange: {
                min: 0,
                max: 10000,
                calculable: true,
                color: ['#ff3333', 'orange', 'yellow', 'lime', 'aqua'],
                textStyle: {
                    color: '#333'
                }
            },
            series: [{
                name: '徐州市',
                type: 'map',
                map: "徐州",
                roam: true,
                hoverable: false,
                mapType: '徐州',
                itemStyle: {
                    normal: {
                        borderColor: 'rgba(105,210,231,1)',
                        borderWidth: 0.5,
                        areaStyle: {
                            color: '#37adc4'
                        },
                        label: {
                            show: true
                        }
                    },
                },
                data: [],
                markLine: {
                    smooth: true,
                    symbol: ['none', 'circle'],
                    symbolSize: 1,
                    itemStyle: {
                        normal: {
                            color: '#fff',
                            borderWidth: 1,
                            borderColor: 'rgba(30,144,255,0.5)'
                        }
                    },
                    data: [],
                },
                geoCoord: {


                    '泉山区': [117.19383, 34.24422],
                    '铜山区': [117.16898, 34.18044],
                    '云龙区': [117.22942, 34.249],
                    '鼓楼区': [117.18554, 34.28823]
                }
            }, {
                name: '江苏',
                type: 'map',
                map: "徐州",
                mapType: '徐州',
                data: [],
                markLine: {
                    smooth: true,
                    effect: {
                        show: true,
                        scaleSize: 1,
                        period: 30,
                        color: '#fff',
                        shadowBlur: 10
                    },
                    itemStyle: {
                        normal: {
                            borderWidth: 1,
                            label: {
                                show: false
                            },
                            lineStyle: {
                                type: 'solid',
                                shadowBlur: 10
                            }
                        },
                    },
                    data: []
                },
                markPoint: {
                    symbol: 'circle',// 标注类型
                    symbolSize: 12,
             symbolSize: function (v) { // 标准大小
                return 10 + v / 50
              },
                    effect: {
                        show: true,
                        shadowBlur: 0
                    },
                    itemStyle: {
                        normal: {
                            label: {
                                show: true
                            }
                        },
                        emphasis: {
                            label: {
                                show: true
                            }
                        }
                    },
                    data: [{
                        name: '铜山区',
                        value: tsq
                    }, {
                        name: '云龙区',
                        value: ylq
                    }, {
                        name: '鼓楼区',
                        value: glq
                    }, {
                        name: '泉山区',
                        value: qsq

                    }]
                }
            }]
        };
        //console.log("11111");

        $(function() {
            echarts.util.mapData.params.params.徐州 = {
                getGeoJson: function (callback) {
                    $.getJSON('js/320300.json', function (data) {
                        // 压缩后的地图数据必须使用 decode 函数转换
                        //console.log(">> %o", data);
                        callback(echarts.util.mapData.params.decode(data));
                    });
                }
            };
            //console.log("22222");

            var elem = document.getElementById("map");
            var chart = echarts.init(document.getElementById("map"));
            chart.setOption(option, true);

             //点击事件,根据点击某个省份计算出这个省份的数据
            chart.on('click', function (params) {
                      console.log(params.name);
                     // alert(params.name);
                      //逻辑控制
                     $("#map").attr("hidden", 'hidden');
                     $("#areaMap").removeAttr("hidden");
                $.ajax({
                    type: "POST",
                    url: "${ctx}/cv/equinfo/cover/mapdata",
                    data: {areaName:params.name},
                    dataType: "json",
                    success: function(data){
                        //alert(data.success);
                        if(data.success){
                            initMapData(data.data);
                        }else{
                            alert("该区域暂无数据！");
                            $("#areaMap").attr("hidden", 'hidden');
                            $("#map").removeAttr("hidden");
                        }

                    }
                });


                  });
        });

        function initMapData(data) {
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

            $.each(data,function(key,value){

                if(!value.lng || !value.lat){
                    return;
                }

                var lng =value.lng;
                var lat =value.lat;

                var lnglat = new AMap.LngLat(lng, lat); //一个点

                var markericon = m1;

                //构建一个标注点
                var marker = new AMap.Marker({
                    icon: markericon,
                    position: lnglat
                });

                marker.setMap(map);  //把标注点放到地图上

                //构建信息窗体
                var infoWindow = openInfo(value,marker,map);

        });

            map.setZoom(14);
        }

        //在指定位置打开信息窗体
        function openInfo(value,marker,map) {
            //构建信息窗体中显示的内容
            var info = [];
            info.push("<div style='line-height:1.6em;font-size:12px;'>");
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
        
        function returnPage() {
            $("#areaMap").attr("hidden", 'hidden');
            $("#map").removeAttr("hidden");
        }
    </script>
  <style>
      .btn-primary {
          color: #fff;
          background-color: #3ca2e0;
          border-color: #2698dd;
      }
      .btn {
          display: inline-block;
          margin-bottom: 0;
          font-weight: normal;
          text-align: center;
          vertical-align: middle;
          -ms-touch-action: manipulation;
          touch-action: manipulation;
          cursor: pointer;
          background-image: none;
          border: 1px solid transparent;
          white-space: nowrap;
          padding: 6px 12px;
          font-size: 14px;
          line-height: 1.42857143;
          border-radius: 0px;
          -webkit-user-select: none;
          -moz-user-select: none;
          -ms-user-select: none;
          user-select: none;
          box-shadow: 3px 3px 5px rgba(0,0,0,0.2);
      }
      #areaMap{
          position: relative;
      }
  </style>
</head>
<body>
<div id="map" style="width:100%; height: 380px;"></div>
<div id="areaMap" style="hidden: true" >
    <button style="position: absolute;right: 10px;top:10px;z-index: 100" class="btn btn-primary" onclick="returnPage()">返回</button>
    <div id="container" style="height: 380px;width: 100%;padding: 0;margin: 0" ></div>
</div>
</body>
</html>
