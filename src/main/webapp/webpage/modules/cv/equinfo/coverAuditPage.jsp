<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>井盖审核信息管理</title>
    <meta name="decorator" content="ani"/>
    <link href="${ctxStatic}/common/fonts/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet"
          type="text/css"/>
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
            validateForm = $("#inputForm").validate({
                submitHandler: function (form) {
                    jp.post("${ctx}/cv/equinfo/coverAudit/saveAudit", $('#inputForm').serialize(), function (data) {
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
<form:form id="inputForm" modelAttribute="coverAudit" class="form-horizontal">
    <form:hidden path="id"/>
    <input type="hidden" id="longId" value="${coverAudit.cover.longitude}"/>
    <input type="hidden" id="latId" value="${coverAudit.cover.latitude}"/>
    <input type="hidden" id="showFlag" value="${coverAudit.cover.isDamaged}"/>
    <sys:message content="${message}"/>
    <div class="examinebox examinebox1 examinebox-s2">
        <div class="map">
                <%--放地图--%>
            <div id="container" style="height: 220px;width: 100%; position: relative"></div>
            <script type="text/javascript">

                var map = new AMap.Map('container', {
                    resizeEnable: true,
                    //zoom:14,//级别
                });
                map.setCity('徐州');

                var m1 = new AMap.Icon({
                    image: '${ctxStatic}/common/images/cover.png',  // Icon的图像
                    size: new AMap.Size(38, 63),    // 原图标尺寸
                    imageSize: new AMap.Size(19, 33) //实际使用的大小
                });

                var lng = $("#longId").val();
                var lat = $("#latId").val();

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
        <div class="container imgsbox">
            <div class="image-set">
                <c:forEach items="${coverAudit.cover.coverImageList}" var="images">
                    <a data-magnify="gallery" data-caption="井盖编号：${coverAudit.cover.no}" href="${images.url}">
                        <img src="${images.url}" alt="">
                    </a>
                </c:forEach>
            </div>
        </div>
            <%--<div class="imgsbox">--%>
            <%--<ul>--%>

            <%--<c:forEach items="${coverAudit.cover.coverImageList}" var="images">--%>
            <%--<li><img src="${images.url}"></li>--%>
            <%--</c:forEach>--%>

            <%--&lt;%&ndash;<li><img src="http://123.58.240.194:9002/cover-gather-service/sys/file/download/204ba4b068b811e99c067db71fbe6c7a"></li>--%>
            <%--<li><img src="http://123.58.240.194:9002/cover-gather-service/sys/file/download/204ba4b068b811e99c067db71fbe6c7a"></li>--%>
            <%--<li><img src="http://123.58.240.194:9002/cover-gather-service/sys/file/download/f5fee73a696411e994f0534d1f6af1a0"></li>&ndash;%&gt;--%>
            <%--</ul>--%>
            <%--</div>--%>
    </div>
    <div class="examinebox">
        <h1 class="title2">井盖信息</h1>
        <div class="inforbox">
            <ul>
                <li><label>井盖编号:</label><span>${coverAudit.cover.no}</span></li>
                <li><label>详细地址:</label><span>${coverAudit.cover.addressDetail}</span></li>

                <li><label>井盖用途:</label><span>${coverAudit.cover.purpose}</span></li>
                <li><label>井位地理场合:</label><span>${coverAudit.cover.situation}</span></li>

                    <%--<li><label>井盖规格:</label><span>${coverAudit.cover.sizeRule}</span></li>--%>

                <li><label>尺寸规格:</label><span>${coverAudit.cover.sizeSpec}</span></li>
                <li>
                    <label>井盖规格:</label><span>${fns:getDictLabel (coverAudit.cover.sizeRule, "cover_size_rule", "--")}</span>
                </li>
                <li><label>直径（mm）:</label><span>${coverAudit.cover.sizeDiameter}</span></li>
                <li><label>半径（mm）:</label><span>${coverAudit.cover.sizeRadius}</span></li>
                <li><label>长度（mm）:</label><span>${coverAudit.cover.sizeLength}</span></li>
                <li><label>宽度（mm）:</label><span>${coverAudit.cover.sizeWidth}</span></li>

                <li><label>井盖材质:</label><span>${coverAudit.cover.material}</span></li>

                <li><label>井盖类型:</label><span>${coverAudit.cover.coverType}</span></li>
                <li><label>高度差:</label><span>${coverAudit.cover.altitudeIntercept}</span></li>
                    <%--<li><label>高度差:</label><span>${fns:getDictLabel (coverAudit.cover.altitudeIntercept, "cover_altitude_intercept", "--")}</span></li>--%>
                <li><label>是否损毁:</label><span>${fns:getDictLabel (coverAudit.cover.isDamaged, "boolean", "--")}</span>
                </li>
                <li><label>损毁情况备注:</label><span>${coverAudit.cover.damageRemark}</span></li>
                <li><label>采集人员:</label><span>${coverAudit.cover.createBy.name}</span></li>
                <li><label>申请时间:</label><span><fmt:formatDate value="${coverAudit.createDate}"
                                                              pattern="yyyy-MM-dd HH:mm:ss"/></span></li>
            </ul>
        </div>
    </div>
    <div class="examinebox" id="ownerId">
        <h1 class="title2">权属单位</h1>
        <div class="inforbox">

            <div class="damage">
                <c:forEach items="${coverAudit.cover.coverOwnerList}" var="owner">
                    <label class="t">${owner.ownerName}</label>
                </c:forEach>
            </div>

        </div>
    </div>
    <div class="examinebox" id="damagedId">
        <h1 class="title2">损坏形式</h1>
        <div class="inforbox">

            <div class="damage">
                <c:forEach items="${coverAudit.cover.coverDamageList}" var="damage">
                    <label class="t">${fns:getDictLabel (damage.damage, "cover_damage", "--")}</label>
                </c:forEach>
            </div>

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
				<input id="auditResult2" name="auditStatus" type="radio" value="audit_fail"/>
				<label for="auditResult2">不通过</label>
			</span>
                    <%--			<span class="mui-checkbox">
                                    <input id="checkbox1" type="checkbox" value="1" checked/>
                                    <label for="checkbox1">下一条</label>
                                </span>--%>
            </div>
            <div class="mui-input-row">
                <label class="t">申请事项</label>
                <form:select path="applyItem" class="form-control " disabled="true">
                    <form:options items="${fns:getDictList('apply_item')}" itemLabel="label" itemValue="value"
                                  htmlEscape="false"/>
                </form:select>
            </div>
            <div class="mui-input-row">
                <label class="t">结果描述</label>
                <form:textarea path="auditResult" htmlEscape="false" rows="4" class="form-control "/>
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
