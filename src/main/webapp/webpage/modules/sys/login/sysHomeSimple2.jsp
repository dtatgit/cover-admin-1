<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>

<html>
<head>
    <title>首页</title>
    <meta name="decorator" content="ani"/>
    <style type="text/css">
        #body-container {
            /*padding: 10px;*/
            padding: 0;
            box-sizing: border-box;
            margin-left: 0px !important;
            margin-top: 0px !important;
            overflow-x: hidden !important;
            transition: all 0.2s ease-in-out !important;
            height: 100% !important;
            position: relative;
        }


        /*页面头部*/
        .home-title {
            position: absolute;
            top: 0;
            display: flex;
            justify-content: space-between;
            height: 91px;
            /*padding: 0 10px;*/
            color: #ffffff;
            width: 100%;
            z-index: 3;
            background:url('${ctxStatic}/common/images/banner.png') no-repeat;
            background-size:100% 100%;
        }

        .home-title img{
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 91px;
            z-index: 1;
        }
        .home-title .title{
            margin-left: 9%;
            font-size: 38px;
            margin-top: 25px;
            /*flex: 0 0 auto;*/
        }
        .home-title > .right {
            margin-right: 20px;
            display: flex;
            /*overflow: hidden;*/
        }

        .home-title > .right .content {
            font-size: 16px;
            line-height: 40px;
            z-index:10;
            color: #fff;
        }

        .home-title > .right .btn {

            height: 30px;
            line-height: 30px;
            border: none;
            font-size: 14px;
            padding: 0 10px;
            margin: 5px 0 0 10px;
            color: #ffffff;
            background: url('${ctxStatic}/common/images/rectangle.png') no-repeat;
            background-size: 100% 100%;
        }
        .home-title > .right .btn:focus {
            outline: none;
        }
        #mapContainer {
            /*width: 500px;*/
            height: 100%;
            z-index: 1;
        }

        /*右侧数据*/
        .data-wrapper {
            /*border: 1px solid red;*/
            position: absolute;
            right: 59px;
            bottom: 34px;
            z-index:4;
        }

        .data-wrapper .top {
            /*background-color: rgba(20, 63, 86,0.5);*/
            /*backdrop-filter: blur(20px);*/
            background: rgba(255, 250, 250, 0.2);
            position: relative;
            box-shadow: 3px 3px 6px 3px rgba(0, 0, 0, .3);
            border-radius: 10px;
            padding: 23px 18px 23px 27px;
            margin-bottom: 10px;
        }

        .data-wrapper .top::before {
            content: '';
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            filter: blur(20px);
            z-index: 2;
        }

        .top .item {
            font-size: 13px;
            line-height: 27px;
            color: #1dd4ff;
            margin-bottom: 20px;
            font-weight: bold;
            display: flex;
            align-items: center;

        }

        .top .item:last-child {
            margin-bottom: 0px;

        }

        .top .item .icon {
            display: inline-block;
            width:20px;
            height: 20px;
            margin-right: 2px;
            background: url('${ctxStatic}/common/images/arrow.png') no-repeat;
            background-size: 100% 100%;
        }

        .top .item .title {
            margin-right: 18px;
        }

        .top .item .number {
            font-size: 26px;
            line-height: 27px;
            color: #ffffff;
        }

        .icon-legend {
            display: flex;
        }

        .icon-legend .wrapper {
            margin-left: 5px;
        }

        .icon-legend .wrapper > img {
            width: 13px;
            height: 13px;
        }

        .icon-legend .wrapper .breakdown {
            color: #D7432F;
        }

        .icon-legend .wrapper .maintenance {
            color: #F6D54A;
        }

        .icon-legend .wrapper .online {
            color: #00FD96;
        }

        .icon-legend .wrapper .offline {
            color: #A5A5A5;
        }
    </style>
    <script src="http://webapi.amap.com/maps?v=2.0&key=ff9b9cc314e61bee3311dcafd0f1cf39"></script>
    <script src="${ctxStatic}/common/js/dayjs.min.js"></script>
</head>
<body>
<div id="body-container">
    <div class="home-title">
<%--        <img src="${ctxStatic}/common/images/banner.png" alt="">--%>
        <span class="title">智慧井盖监测大数据平台</span>
        <div class="right">
            <div class="content" >${currentTime}</div>

            <button class="btn" onclick="handleFullScreen()">
                <i class="glyphicon glyphicon-fullscreen"></i>
            </button>
            <button class="btn" onclick="btnRefresh();">
                <i class="glyphicon glyphicon-refresh"></i>
            </button>
        </div>
    </div>
    <div id="mapContainer">
    </div>
    <div class="data-wrapper">
        <div class="top">
            <div class="item">
                <span class="icon"></span>
                <span class="title">普查窨井</span>
                <span class="number">19292</span>
            </div>
            <div class="item">
                <span class="icon"></span>
                <span class="title">井卫部署</span>
                <span class="number">19292</span>
            </div>
        </div>
        <div class="top">
            <div class="item">
                <span class="icon"></span>
                <span class="title">实时在线</span>
                <span class="number">19292</span>
            </div>
            <div class="item">
                <span class="icon"></span>
                <span class="title">实时离线</span>
                <span class="number">19292</span>
            </div>
            <div class="item">
                <span class="icon"></span>
                <span class="title">故障排行</span>
                <span class="number">92</span>
            </div>
            <div class="item">
                <span class="icon"></span>
                <span class="title">维护进行</span>
                <span class="number">73</span>
            </div>
        </div>
        <div class="top">
            <div class="item">
                <span class="icon"></span>
                <span class="title">报警总计</span>
                <span class="number">19292</span>
            </div>
            <div class="item">
                <span class="icon"></span>
                <span class="title">维护总计</span>
                <span class="number">19292</span>
            </div>
        </div>
        <div class="icon-legend">
            <div class="wrapper">
                <img src="${ctxStatic}/common/images/red.png"/>
                <span class="breakdown">排障</span>
            </div>
            <div class="wrapper">
                <img src="${ctxStatic}/common/images/yellow.png"/>
                <span class="maintenance">维护</span>
            </div>
            <div class="wrapper">
                <img src="${ctxStatic}/common/images/green.png"/>
                <span class="online">在线</span>
            </div>
            <div class="wrapper">
                <img src="${ctxStatic}/common/images/grey.png"/>
                <span class="offline">离线</span>
            </div>


        </div>
    </div>


</div>
<%--<script src="vendor/ckeditor/ckeditor.js" type="text/javascript"></script>--%>
<script src="js/vendor.js"></script>
<script>
    setInterval(()=>{
        let currentTime=dayjs().format('YYYY-MM-DD HH:mm:ss')
        document.getElementsByClassName('content')[0].innerText=currentTime
    },1000)
</script>
<script>
    let myMap
    const loadMap = new Promise((resolve) => {
        myMap = new AMap.Map('mapContainer', {
            resizeEnable: true,
            zoom: 10,
            center: [116.397428, 39.90923],
            mapStyle: 'amap://styles/3cc9499ea59541092d9e37366a3f565e'
        });
        resolve('promise')
    })
    loadMap.then(res => {
        console.log(res, 'res')
    })

    function initData() {
        jp.loading('正在加载，请稍等...');
        $.post('${ctx}/act/task/claim', {taskId: taskId}, function (data) {
            if (data === 'true') {
                jp.success('数据加载完成');
            } else {
                jp.error('数据加载失败');
            }
        });
    }


    function getIcon(iconIndex) {
        //获取图标地址
        const iconUrl = [
            '${ctxStatic}/common/images/red.png',
            '${ctxStatic}/common/images/yellow.png',
            '${ctxStatic}/common/images/green.png',
            '${ctxStatic}/common/images/grey.png'
        ]
        return (
            {
                url: iconUrl[iconIndex],  // 图标地址
                size: new AMap.Size(13, 13),     // 图标大小
                anchor: new AMap.Pixel(5, 5) // 图标显示位置偏移量，基准点为图标左上角
            }
        )
    }

    function getMarkerStyle() {
        const redIcon = getIcon(0)
        const yellowIcon = getIcon(1)
        const greenIcon = getIcon(2)
        const greyIcon = getIcon(3)
        return [redIcon, yellowIcon, greenIcon, greyIcon]
    }

    // 样式对象数组
    let styleObjectArr = getMarkerStyle();
    // 实例化 AMap.MassMarks
    let massMarks = new AMap.MassMarks([], {
        zIndex: 5,  // 海量点图层叠加的顺序
        zooms: [1, 20],  // 在指定地图缩放级别范围内展示海量点图层
        style: styleObjectArr  // 设置样式对象
    });

    const rawData = [
        {
            lnglat: [116.425924, 39.902909],
            type: "red",
        },
        {
            lnglat: [116.350736, 39.94293],
            type: "yellow",
        },
        {
            lnglat: [116.343526, 39.900539],
            type: "green",
        },
        {
            lnglat: [116.353483, 39.94372],
            type: "grey",
        }
    ]

    function getMarkerType(type) {
        const typeMap = {
            'red': 0,
            'yellow': 1,
            'green': 2,
            'grey': 3
        }
        return typeMap[type]
    }

    // 设置了样式索引的点标记数组
    const markerData = rawData.map(item => {
        return {...item, style: getMarkerType(item.type)}
    })
    console.log(markerData, 'markerData')
    // 将数组设置到 massMarks 图层
    massMarks.setData(markerData);
    massMarks.setMap(myMap);
</script>
<script>
    function btnRefresh() {
        location.reload();
    }
    // 打开全屏api
    function getreqfullscreen() {
        const root = document.documentElement
        return (
            root.requestFullscreen ||
            root.webkitRequestFullscreen ||
            root.mozRequestFullScreen ||
            root.msRequestFullscreen
        )
    }
    // 关闭全屏api
    function   getexitfullscreen() {
        return (
            document.exitFullscreen ||
            document.webkitExitFullscreen ||
            document.mozCancelFullScreen ||
            document.msExitFullscreen
        )
    }
    // 获取全屏元素
    function getfullscreenelement() {
        return (
            document.fullscreenElement ||
            document.webkitFullscreenElement ||
            document.mozFullScreenElement ||
            document.msFullscreenElement
        )
    }
    function  handleFullScreen() {
        const globalreqfullscreen = getreqfullscreen() // 得到支持的版本
        const globalexitfullscreen = getexitfullscreen() // 获取支持的版本
        const centerDom = document.getElementById('body-container') // 地图区域
        const fullscreenElement = getfullscreenelement()
        if (!fullscreenElement) { 	// 非全屏
            globalreqfullscreen.call(centerDom)
        } else {
            globalexitfullscreen.call(document) // 退出
        }
    }
</script>
</body>
</html>
