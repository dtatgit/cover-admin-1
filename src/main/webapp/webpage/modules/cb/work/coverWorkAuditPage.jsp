<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工单审核信息管理</title>
	<meta name="decorator" content="ani"/>
	<link href="${ctxStatic}/common/fonts/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
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
	<style>
		.nav-tabs{clear: both;overflow: hidden; margin: 0;padding: 0; border: 0;    position: relative;top: 1px;}
		.nav-tabs li a{margin-right: 0px;line-height: 1.42857143;border: 1px solid #ddd;border-radius: 0;
			background-color: #fff;padding: 5px 10px;border-right: none;color:#555;display: block}
		.nav-tabs li:last-child a{border-right:1px solid #ddd;}
		.nav-tabs > li.active > a, .nav-tabs > li.active > a:hover, .nav-tabs > li.active > a:focus {
			color: #3ca2e0; border-top:2px solid #3ca2e0;
		}


	</style>
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
            $(".nav-tabs").on("click","a",function () {
                $(".nav-tabs li").removeClass("active");
                $(this).parent().addClass("active");
                var title = $(this).attr("title");
                $(".panel").hide();
                $("#" + title).show();
            });

            validateForm = $("#inputForm").validate({
                submitHandler: function(form){
                    jp.post("${ctx}/cb/work/coverWork/saveAudit",$('#inputForm').serialize(),function(data){
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

	<script type="text/javascript">
        $(document).ready(function() {
            var   flag=  $("#showFlag").val();
            if(flag=="Y"){

                $("#damagedId").removeAttr("hidden");
            }else{

                $("#damagedId").attr("hidden", 'hidden');

            }
        });
	</script>

</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="coverWork" class="form-horizontal">
	<form:hidden path="id"/>
	<input type="hidden" id="longId" value="${cover.longitude}"/>
	<input type="hidden" id="latId" value="${cover.latitude}"/>
	<%--<input type="hidden" id="showFlag" value="${cover.isDamaged}"/>--%>
	<sys:message content="${message}"/>

	<div class="examinebox">
		<h1 class="title2">工单信息</h1>
		<div class="inforbox">
			<ul>
				<li><label>工单编号:</label><span>${coverWork.workNum}</span></li>
				<li><label>工单类型:</label><span>${fns:getDictLabel (coverWork.workType, "work_type", "--")}</span></li>
		<%--		<li><label>工单状态:</label><span>${fns:getDictLabel (coverWork.workStatus, "work_status", "--")}</span></li>--%>
				<li><label>工单状态:</label><span>${coverWork.workStatus}</span></li>
				<li><label>紧急程度:</label><span>${fns:getDictLabel (coverWork.workLevel, "work_level", "--")}</span></li>
				<li><label>施工内容:</label><span>${coverWork.constructionContent}</span></li>
				<li><label>施工人员:</label><span>${coverWork.constructionUser.name}</span></li>
				<li><label>联系电话:</label><span>${coverWork.phone}</span></li>
				<li><label>施工部门:</label><span>${coverWork.constructionDepart.name}</span></li>
			</ul>
		</div>
	</div>

	<div class="examinebox">
		<h1 class="title2">井盖信息</h1>
		<div class="examinebox examinebox1">
			<div class="map">
					<%--放地图--%>
				<div id="container" style="height: 200px;position: relative;top:10px; margin:0 2%;width: 96%"></div>
				<script type="text/javascript">

                    var map = new AMap.Map('container', {
                        resizeEnable: true,
                        //zoom:14,//级别
                    });
                    map.setCity('北京');

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
				</script>
			</div>
			<div class="container imgsbox">
				<div class="image-set">
					<c:forEach items="${cover.coverImageList}" var="images">
						<a data-magnify="gallery" data-caption="井盖编号：${cover.no}" href="${images.url}">
							<img  src="${images.url}" alt="">
						</a>
					</c:forEach>
				</div>
			</div>
		</div>
		<div class="inforbox">
			<ul>
				<li><label>井盖编号:</label><span>${cover.no}</span></li>
				<li><label>详细地址:</label><span>${cover.addressDetail}</span></li>

				<li><label>井盖用途:</label><span>${cover.purpose}</span></li>
				<li><label>井位地理场合:</label><span>${cover.situation}</span></li>

				<%--<li><label>井盖规格:</label><span>${coverAudit.cover.sizeRule}</span></li>--%>

				<li><label>尺寸规格:</label><span>${cover.sizeSpec}</span></li>
				<li><label>井盖规格:</label><span>${fns:getDictLabel (cover.sizeRule, "cover_size_rule", "--")}</span></li>
				<li><label>直径（mm）:</label><span>${cover.sizeDiameter}</span></li>
				<li><label>半径（mm）:</label><span>${cover.sizeRadius}</span></li>
				<li><label>长度（mm）:</label><span>${cover.sizeLength}</span></li>
				<li><label>宽度（mm）:</label><span>${cover.sizeWidth}</span></li>

				<li><label>井盖材质:</label><span>${cover.material}</span></li>

				<li><label>井盖类型:</label><span>${cover.coverType}</span></li>
				<li><label>高度差:</label><span>${cover.altitudeIntercept}</span></li>
				<li><label>是否损毁:</label><span>${fns:getDictLabel (cover.isDamaged, "boolean", "--")}</span></li>
				<li><label>损毁情况备注:</label><span>${cover.damageRemark}</span></li>
				<li><label>采集人员:</label><span>${cover.createBy.name}</span></li>
				<li><label>采集时间:</label><span><fmt:formatDate value="${cover.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span></li>
				<li><label></label><span></span></li>
				<li><label>权属单位:</label><span class="t">${cover.ownerDepart}</span>
					<%--<c:forEach items="${cover.coverOwnerList}" var="owner">
					<span class="t">${owner.ownerName}</span>
					</c:forEach>--%>
				</li>
				<li><label>损坏形式:</label>
					<c:forEach items="${cover.coverDamageList}" var="damage">
						<span class="t">${fns:getDictLabel (damage.damage, "cover_damage", "--")}</span>
					</c:forEach>
				</li>
			</ul>
		</div>


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
				<li><label>设备类型:</label><span>${fns:getDictLabel (coverBell.bellType, " bellType", "--")}</span></li>
				<li><label>工作状态:</label><span>${fns:getDictLabel (coverBell.workStatus, "bell_work_status", "--")}</span></li>
				<li><label>生命周期:</label><span>${fns:getDictLabel (coverBell.bellStatus, "bell_status", "--")}</span></li>
				<li><label>设防状态:</label><span>${fns:getDictLabel (coverBell.defenseStatus, "defense_status", "--")}</span></li>
			</ul>
		</div>
	</div>

	<div class="examinebox">
		<h1 class="title2">工单记录</h1>
		<ul class="nav-tabs">
				<li class="active"><a title="czjl">操作记录</a></li>
			<%--	<li><a title="azjl">安装记录</a></li>
				<li><a title="whjl">维护记录</a></li>--%>
		</ul>

<%--		<div id="czjl" class="panel panel-primary" style="display: block;">
			<table class="table table-ullist">
				<tr><td class="width-10 active">操作类型</td><td class="width-10 active">操作状态</td><td class="width-10 active">结果</td><td class="width-10 active">用户</td><td class="width-10 active">部门</td><td class="width-10 active">时间</td></tr>
			<c:forEach items="${workOperationList}" var="operation">
				<tr>
					<td>${fns:getDictLabel (operation.operationType, "work_operation_Type", "--")}</td>
					<td>${fns:getDictLabel (operation.operationStatus, "work_operation_status", "--")}</td>
					<td>${operation.operationResult}</td>

					<td>${operation.createBy.name}</td>
					<td>${operation.createDepart.name}</td>
					<td><fmt:formatDate value="${operation.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
			</c:forEach>
			</table>
		</div>--%>

		<div id="czjl" class="panel panel-primary" style="display: block;">
			<table class="table table-ullist">
				<tr><td class="width-10 active">流程信息</td><td class="width-10 active">操作信息</td><td class="width-10 active">原状态</td><td class="width-10 active">结果状态</td><td class="width-10 active">操作人</td><td class="width-10 active">操作部门</td><td class="width-10 active">操作时间</td><td class="width-10 active">目标部门</td></tr>
				<c:forEach items="${flowOptList}" var="operation">
					<tr>
						<td>${operation.flowId.flowNo}</td>
						<td>${operation.optId.optName}</td>
						<td>${operation.originState}</td>
						<td>${operation.resultState}</td>

						<td>${operation.createBy.name}</td>
						<td>${operation.optOrg.name}</td>
						<td><fmt:formatDate value="${operation.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td>${operation.targetOrg.name}</td>
					</tr>
				</c:forEach>
			</table>
		</div>

		<div id="azjl" class="panel panel-primary" style="display: none;">
			<c:forEach items="${installDetailList}" var="install">
			<h1 class="title2">常规核实项目</h1>
			<div class="inforbox">
				<ul>
					<li><label>井盖权属:</label><span>${fns:getDictLabel (install.isOwnerDepart, "boolean", "--")}</span></li>
					<li><label>井盖用途:</label><span>${fns:getDictLabel (install.isPurpose, "boolean", "--")}</span></li>
					<li><label>地理场合:</label><span>${fns:getDictLabel (install.isSituation, "boolean", "--")}</span></li>
					<li><label>损坏形式:</label><span>${fns:getDictLabel (install.isDamaged, "boolean", "--")}</span></li>
					<li><label>核实日期:</label><span><fmt:formatDate value="${install.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span></li>
					<li><label>备注:</label><span>${install.remarks}</span></li>
				</ul>
			</div>
				<h1 class="title2">现场图片</h1>
			<div class="container imgsbox">
				<div class="image-set">
					<c:forEach items="${install.imageList}" var="images">
						<a data-magnify="gallery" data-caption="井盖编号：${cover.no}" href="${images}">
							<img  src="${images}" alt="">
						</a>
					</c:forEach>
				</div>
			</div>
			</c:forEach>
		</div>
		<div id="whjl" class="panel panel-primary" style="display: none;">
			暂无数据!
		</div>
	</div>


<div class="examinebox">
	<h1 class="title2">工单审核</h1>
	<div class="inforbox">
		<div class="mui-input-row mui-radio-checkbox-row">
			<span class="mui-radio">
				<input id="auditResult" name="operationStatus" type="radio" value="success" checked>
				<label for="auditResult">审核通过</label>
			</span>
			<span class="mui-radio">
				<input id="auditResult2" name="operationStatus" type="radio" value="fail" />
				<label for="auditResult2">审核失败</label>
			</span>
<%--			<span class="mui-checkbox">
				<input id="checkbox1" type="checkbox" value="1" checked/>
				<label for="checkbox1">下一条</label>
			</span>--%>
		</div>

		<div class="mui-input-row">
			<label class="t">审核描述</label>
			<form:textarea path="operationResult" htmlEscape="false"  rows="4"    class="form-control "/>
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