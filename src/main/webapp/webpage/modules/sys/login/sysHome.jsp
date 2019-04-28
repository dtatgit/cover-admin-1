<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
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
	</style>
</head>
<body class="">
<div id="body-container" class="wrapper wrapper-content">
	<div class="conter-wrapper home-container">
		<div class="row home-row">
			<div class="col-md-4 col-lg-3">
				<div class="home-stats">
					<a href="#" class="stat hvr-wobble-horizontal">
						<div class=" stat-icon red">
							<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" t="1555926046803" class="icon" style="" viewBox="0 0 1024 1024" version="1.1" p-id="11224"><defs><style type="text/css"/></defs><path d="M276.48 564.906667l-1.706667-10.24h-78.506666l3.413333 13.653333c22.186667 133.12 131.413333 244.053333 264.533333 264.533333l13.653334 3.413334v-75.093334l-10.24-1.706666c-98.986667-18.773333-174.08-95.573333-191.146667-194.56z m1.706667-88.746667c18.773333-97.28 93.866667-174.08 189.44-194.56l10.24-1.706667V204.8l-13.653334 3.413333c-133.12 22.186667-242.346667 131.413333-264.533333 264.533334l-3.413333 13.653333h78.506666l3.413334-10.24z m481.28-1.706667l1.706666 10.24h78.506667l-3.413333-13.653333c-20.48-134.826667-129.706667-244.053333-264.533334-266.24l-13.653333-3.413333v78.506666l10.24 1.706667c98.986667 22.186667 174.08 97.28 191.146667 192.853333z m-69.973334 51.2v-11.946666c-5.12-88.746667-76.8-158.72-165.546666-158.72-46.08 0-87.04 17.066667-121.173334 51.2-32.426667 32.426667-51.2 76.8-51.2 121.173333 0 92.16 76.8 167.253333 170.666667 167.253333 92.16-3.413333 167.253333-76.8 167.253333-168.96z m-168.96 90.453334c-51.2 0-92.16-40.96-92.16-92.16s40.96-92.16 92.16-92.16 92.16 40.96 92.16 92.16-40.96 92.16-92.16 92.16z m244.053334-51.2c-20.48 95.573333-95.573333 170.666667-191.146667 196.266666l-10.24 3.413334v73.386666l13.653333-3.413333c133.12-22.186667 244.053333-131.413333 264.533334-264.533333l3.413333-13.653334h-78.506667l-1.706666 8.533334z" p-id="11225"/><path d="M520.533333 51.2C261.12 51.2 51.2 261.12 51.2 520.533333S261.12 989.866667 520.533333 989.866667 989.866667 779.946667 989.866667 520.533333 779.946667 51.2 520.533333 51.2zM122.88 520.533333v-3.413333 3.413333z m430.08 395.946667v-37.546667c0-17.066667-13.653333-32.426667-32.426667-32.426666s-32.426667 13.653333-32.426666 32.426666v37.546667c-194.56-15.36-349.866667-170.666667-365.226667-363.52h39.253333c17.066667 0 32.426667-13.653333 32.426667-32.426667s-13.653333-32.426667-32.426667-32.426666H124.586667c15.36-194.56 170.666667-349.866667 363.52-363.52v37.546666c0 17.066667 13.653333 32.426667 32.426666 32.426667s32.426667-13.653333 32.426667-32.426667V124.586667c194.56 15.36 348.16 170.666667 363.52 363.52h-35.84c-17.066667 0-32.426667 13.653333-32.426667 32.426666s13.653333 32.426667 32.426667 32.426667h35.84c-15.36 192.853333-170.666667 348.16-363.52 363.52z" p-id="11226"/></svg>
						</div>
						<div class=" stat-label">
							<div class="label-header">
								<span>201,0054</span>个
							</div>
							<div class="clearfix stat-detail">
								<div class="label-body">
									总勘察数
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
							<span>5,054</span>个
						</div>
						<div class="clearfix stat-detail">
							<div class="label-body">
								今日勘察数
							</div>
						</div>
					</div>
				</a>
					<a href="#" class="stat hvr-wobble-horizontal">
					<div class=" stat-icon green">
						<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" t="1555926046803" class="icon" style="" viewBox="0 0 1024 1024" version="1.1" p-id="11224"><defs><style type="text/css"/></defs><path d="M276.48 564.906667l-1.706667-10.24h-78.506666l3.413333 13.653333c22.186667 133.12 131.413333 244.053333 264.533333 264.533333l13.653334 3.413334v-75.093334l-10.24-1.706666c-98.986667-18.773333-174.08-95.573333-191.146667-194.56z m1.706667-88.746667c18.773333-97.28 93.866667-174.08 189.44-194.56l10.24-1.706667V204.8l-13.653334 3.413333c-133.12 22.186667-242.346667 131.413333-264.533333 264.533334l-3.413333 13.653333h78.506666l3.413334-10.24z m481.28-1.706667l1.706666 10.24h78.506667l-3.413333-13.653333c-20.48-134.826667-129.706667-244.053333-264.533334-266.24l-13.653333-3.413333v78.506666l10.24 1.706667c98.986667 22.186667 174.08 97.28 191.146667 192.853333z m-69.973334 51.2v-11.946666c-5.12-88.746667-76.8-158.72-165.546666-158.72-46.08 0-87.04 17.066667-121.173334 51.2-32.426667 32.426667-51.2 76.8-51.2 121.173333 0 92.16 76.8 167.253333 170.666667 167.253333 92.16-3.413333 167.253333-76.8 167.253333-168.96z m-168.96 90.453334c-51.2 0-92.16-40.96-92.16-92.16s40.96-92.16 92.16-92.16 92.16 40.96 92.16 92.16-40.96 92.16-92.16 92.16z m244.053334-51.2c-20.48 95.573333-95.573333 170.666667-191.146667 196.266666l-10.24 3.413334v73.386666l13.653333-3.413333c133.12-22.186667 244.053333-131.413333 264.533334-264.533333l3.413333-13.653334h-78.506667l-1.706666 8.533334z" p-id="11225"/><path d="M520.533333 51.2C261.12 51.2 51.2 261.12 51.2 520.533333S261.12 989.866667 520.533333 989.866667 989.866667 779.946667 989.866667 520.533333 779.946667 51.2 520.533333 51.2zM122.88 520.533333v-3.413333 3.413333z m430.08 395.946667v-37.546667c0-17.066667-13.653333-32.426667-32.426667-32.426666s-32.426667 13.653333-32.426666 32.426666v37.546667c-194.56-15.36-349.866667-170.666667-365.226667-363.52h39.253333c17.066667 0 32.426667-13.653333 32.426667-32.426667s-13.653333-32.426667-32.426667-32.426666H124.586667c15.36-194.56 170.666667-349.866667 363.52-363.52v37.546666c0 17.066667 13.653333 32.426667 32.426666 32.426667s32.426667-13.653333 32.426667-32.426667V124.586667c194.56 15.36 348.16 170.666667 363.52 363.52h-35.84c-17.066667 0-32.426667 13.653333-32.426667 32.426666s13.653333 32.426667 32.426667 32.426667h35.84c-15.36 192.853333-170.666667 348.16-363.52 363.52z" p-id="11226"/></svg>
					</div>
					<div class=" stat-label">
						<div class="label-header">
							<span>54</span>个
						</div>
						<div class="clearfix stat-detail">
							<div class="label-body">
								无组别井盖数
							</div>
						</div>
					</div>
				</a>
				</div>
			</div>
			<div class="col-md-8 col-lg-9">
				<div class="home-charts-middle">
					<div class="home-panel-heading panel-heading">
						<h2>一周工作量统计</h2>
					</div>
					<div class="chart-container">
						<div id="lineChart" style="height:232px"></div>
					</div>
				</div>
			</div>
		</div>
		<div class="row home-row">
			<div class="col-md-4 col-lg-2">
				111
			</div>
			<div class="col-md-4 col-lg-2">
				111
			</div>
			<div class="col-md-4 col-lg-2">
				111
			</div>
			<div class="col-md-4 col-lg-2">
				111
			</div>
			<div class="col-md-4 col-lg-2">
				111
			</div>
			<div class="col-md-4 col-lg-2">
				111
			</div>
		</div>
		<div class="row home-row">
			<div class="col-md-8 col-lg-9">
				<div class="home-charts-middle">
					<div class="home-panel-heading panel-heading">
						<h2>地图</h2>
					</div>
					<div class="map-container box padder">
						<div id="world-map" style="width: 100%; height: 300px"></div>
					</div>
				</div>
			</div>
			<div class="col-md-4 col-lg-3">
				<div class="todo-container bg-blue">
					<div class="panel-heading">
						<div class="todo-header text-center">
							<h4>优秀员工TOP8</h4>
						</div>
					</div>
					<div class="panel-body bg-blue">
						<div class="todo-body">
							<div class="todo-list-wrap">
								<ul class="todo-list">
									<li>
										<label>排序</label>
										<span class="user-name">姓名</span>
										<span class="work-num">勘察总数</span>
									</li>
									<li>
										<label><i>1</i></label>
										<span class="user-name">张正恺</span>
										<span class="work-num">17,800个</span>
									</li>
									<li>
										<label><i>2</i></label>
										<span class="user-name">张正恺</span>
										<span class="work-num">17,800个</span>
									</li>
									<li>
										<label><i>3</i></label>
										<span class="user-name">张正恺</span>
										<span class="work-num">17,800个</span>
									</li>
									<li>
										<label><i>4</i></label>
										<span class="user-name">张正恺</span>
										<span class="work-num">17,800个</span>
									</li>
									<li>
										<label><i>5</i></label>
										<span class="user-name">张正恺</span>
										<span class="work-num">17,800个</span>
									</li>
									<li>
										<label><i>6</i></label>
										<span class="user-name">张正恺</span>
										<span class="work-num">17,800个</span>
									</li>
									<li>
										<label><i>7</i></label>
										<span class="user-name">张正恺</span>
										<span class="work-num">17,800个</span>
									</li>
									<li>
										<label><i>8</i></label>
										<span class="user-name">张正恺</span>
										<span class="work-num">17,800个</span>
									</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="vendor/ckeditor/ckeditor.js" type="text/javascript"></script>
<script src="js/vendor.js"></script>

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

        var option = {
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            //控制4周margin
            grid:{
                //x:50,
                //x2:50,
                y:60,
                y2:30
            },
            legend: {
                data: ['用户数量', '月份']
            },
            //x坐标轴
            xAxis: [
                {
                    name: '月份',
                    type: 'category',
                    data: ['9月', '10月', '11月', '12月', '1月', '2月'],
                    axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
            //y坐标轴
            yAxis: [
                {
                    type: 'value',
                    name: '用户数量',
                    min: 0,
                    max: 500,
                    interval: 100,
                    axisLabel: {
                        formatter: '{value}'
                    }
                }
            ],
            series: [
                {
                    name: '用户数量',
                    type: 'bar',
                    barWidth: 30, //柱图宽度
                    data: [308, 500, 400, 320, 180, 361]
                }
            ]
        };
       var myChart = echarts.init(document.getElementById('lineChart')); //应用dark主题
        myChart.setOption(option);

    });
</script>

</body>
</html>