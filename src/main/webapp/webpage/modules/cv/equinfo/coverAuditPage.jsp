<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>井盖审核信息管理</title>
	<meta name="decorator" content="ani"/>
	<link href="${ctxStatic}/common/fonts/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	<script src="http://webapi.amap.com/maps?v=1.4.6&key=06de357afd269944d97de0abcde0f4e0"></script>
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
            validateForm = $("#inputForm").validate({
                submitHandler: function(form){
                    jp.post("${ctx}/cv/equinfo/coverAudit/saveAudit",$('#inputForm').serialize(),function(data){
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



</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="coverAudit" class="form-horizontal">
	<form:hidden path="id"/>
	<input type="hidden" id="longId" value="${coverAudit.cover.longitude}"/>
	<input type="hidden" id="latId" value="${coverAudit.cover.latitude}"/>

	<sys:message content="${message}"/>
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
                    //构建信息窗体
                    //var infoWindow = openInfo(value,marker);


                    //在指定位置打开信息窗体
                    // function openInfo(value,marker) {
                    //     //构建信息窗体中显示的内容
                    //     var info = [];
                    //     info.push("<div style='line-height:1.6em;font-size:12px;'>");
                    //
                    //     info.push("小区名称 ："+value.name);
                    //     info.push("详细地址 ："+ value.address + "</div>");
                    //     var infoWindow = new AMap.InfoWindow({
                    //         offset: new AMap.Pixel(0, -29),
                    //         content:  info.join("<br/>"),  //使用默认信息窗体框样式，显示信息内容
                    //     });
                    //
                    //     marker.on("mouseover", function(e) {
                    //         infoWindow.open(map, e.target.getPosition());
                    //     });
                    //     marker.on("mouseout", function() {
                    //         infoWindow.close();
                    //     });
                    //
                    // }
				</script>
		</div>
		<div class="imgsbox">
            <ul>
				<li><img src="${ctxStatic}/common/images/timg1.jpg"></li>
				<li><img src="${ctxStatic}/common/images/timg2.jpg"></li>
				<li><img src="${ctxStatic}/common/images/timg3.jpg"></li>
			</ul>
		</div>
	</div>
	<div class="examinebox">
		<h1 class="title2">井盖信息</h1>
		<div class="inforbox">
			<ul>
				<li><label>井盖编号:</label><span>${coverAudit.cover.no}</span></li>
				<li><label>详细地址:</label><span>${coverAudit.cover.addressDetail}</span></li>
				<li><label>井盖经度:</label><span>${coverAudit.cover.longitude}</span></li>
				<li><label>井盖纬度:</label><span>${coverAudit.cover.latitude}</span></li>
				<li><label>井盖用途:</label><span>${coverAudit.cover.purpose}</span></li>
				<li><label>申请时间:</label><span><fmt:formatDate value="${coverAudit.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span></li>
			</ul>
		</div>
	</div>

<div class="examinebox">
	<h1 class="title2">审核结果</h1>
	<div class="inforbox">
		<div class="mui-input-row mui-radio-checkbox-row">
			<span class="mui-radio">
				<input id="auditResult" name="auditStatus" type="radio" value="audit_pass" checked>
				<label for="auditResult">通过</label>
			</span>
			<span class="mui-radio">
				<input id="auditResult2" name="auditStatus" type="radio" value="audit_fail" />
				<label for="auditResult2">不通过</label>
			</span>
<%--			<span class="mui-checkbox">
				<input id="checkbox1" type="checkbox" value="1" checked/>
				<label for="checkbox1">下一条</label>
			</span>--%>
		</div>
		<div class="mui-input-row">
			<label class="t">申请事项</label>
			<form:select path="applyItem" class="form-control ">
				<form:options items="${fns:getDictList('apply_item')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
		</div>
		<div class="mui-input-row">
			<label class="t">结果描述</label>
			<form:textarea path="auditResult" htmlEscape="false"  rows="4"    class="form-control "/>
		</div>
	</div>
</div>


	<%--<table class="table table-ullist">--%>
		<%--<tbody>--%>

		<%--<tr>--%>
			<%--<td class="width-15 active"><label class="pull-right">审核状态：</label></td>--%>
			<%--<td class="width-35">--%>
				<%--<select id="refundStatus" name="refundStatus" class="form-control ">--%>
					<%--<option value="2">审核通过</option>--%>
					<%--<option value="3">审核不通过</option>--%>
				<%--</select>--%>

			<%--</td>--%>
			<%--<td class="width-15 active"><label class="pull-right">审核时间：</label></td>--%>
			<%--<td class="width-35">--%>
				<%--<p class="input-group">--%>
				<%--<div class='input-group form_datetime' id='auditTime'>--%>
					<%--<input type='text'  name="auditTime" class="form-control"  readonly="true"  value="<fmt:formatDate value="${cgRefundInfo.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>--%>
					<%--<span class="input-group-addon">--%>
			                        <%--<span class="glyphicon glyphicon-calendar"></span>--%>
			                    <%--</span>--%>
				<%--</div>--%>
				<%--</p>--%>
			<%--</td>--%>
		<%--</tr>--%>
		<%--<tr>--%>
			<%--<td class="width-15 active"><label class="pull-right">审核用户：</label></td>--%>
			<%--<td class="width-35">--%>
				<%--&lt;%&ndash;<form:hidden path="auditPersonId" value="${fns:getUser().id}" />&ndash;%&gt;--%>
				<%--&lt;%&ndash;<form:input path="auditPersonName" htmlEscape="false" value="${fns:getUser().name}"  readonly="true"  class="form-control "/>&ndash;%&gt;--%>
			<%--</td>--%>

		<%--</tr>--%>
		<%--<tr>--%>
			<%--<td class="width-15 active"><label class="pull-right">审核描述：</label></td>--%>
			<%--<td class="width-35">--%>
				<%--<form:textarea path="auditResult" htmlEscape="false"  rows="4"    class="form-control "/>--%>
			<%--</td>--%>
			<%--<td class="width-15 active"><label class="pull-right">备注信息：</label></td>--%>
			<%--<td class="width-35">--%>
				<%--<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>--%>
			<%--</td>--%>
			<%--<td class="width-15 active"></td>--%>
			<%--<td class="width-35" ></td>--%>
		<%--</tr>--%>
		<%--</tbody>--%>
	<%--</table>--%>

</form:form>
</body>
</html>