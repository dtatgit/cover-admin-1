<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>代理商管理</title>
	<meta name="decorator" content="ani"/>
	<%@include file="/webpage/include/treeview.jsp" %>
	<link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>
	<script src="http://webapi.amap.com/maps?v=1.4.10&key=608d75903d29ad471362f8c58c550daf&plugin=AMap.Geocoder"></script>
	<style>
		#locationMap {
			height: 520px;
			width: 850px;
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
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		
		$(document).ready(function(){
			$("#name").focus();

            validateForm = $("#inputForm").validate({
                rules: {
                    loginName: {remote: "${ctx}/project/projectInfo/checkLoginName?oldLoginName=" + encodeURIComponent('${projectInfo.loginName}')}
                },
                messages: {
                    loginName: {remote: "账号已存在"}
                },
				submitHandler: function(form){
					var x = $('#longitude').val();
					var y = $('#latitude').val();
					// if (!x || !y) {
					// 	alert("请选择项目位置");
					// 	return;
					// }
					jp.loading();
					jp.post("${ctx}/project/projectInfo/save",$('#inputForm').serialize(),function(data){
						if(data.success){
	                    	$table.bootstrapTable('refresh');
	                    	jp.success(data.msg);
	                    }else{
            	  			jp.error(data.msg);
	                    }
	                    
	                    jp.close($topIndex);//关闭dialog
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

			map = new AMap.Map('locationMap', {
				resizeEnable: true,
				zoom: 12,
				center: [117.185756, 34.285926]
			});
			var geocoder = new AMap.Geocoder({
				city: "0516", //城市设为北京，默认：“全国”
				radius: 500 //范围，默认：500
			});

			function initMapPoint() {
				var x = $('#longitude').val();
				var y = $('#latitude').val();
				if (id) {
					if (x && y) {
						var marker = new AMap.Marker({
							icon: "https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png",
							position: [x, y]
						});
						marker.setMap(map);
						map.setCenter([x, y]);
					}
				}
			}

			initMapPoint();

			/**
			 * 点击获取坐标
			 * @param e
			 */
			function getLnglat(e) {
				map.clearMap();
				var x = e.lnglat.getLng();
				var y = e.lnglat.getLat();
				$('#longitude').val(x);
				$('#latitude').val(y);
				var marker = new AMap.Marker({
					icon: "https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png",
					position: [x, y]
				});
				marker.setMap(map);
			}

			AMap.event.addListener(map, 'click', getLnglat); //点击事件
            // 地图进行缩放的时候监听该函数
            AMap.event.addListener(map,'zoomend',function(){
                var zoom = map.getZoom();
                $('#zoom').val(zoom);
            });


		});

	</script>
</head>
<body class="bg-white">
	<form:form id="inputForm" modelAttribute="projectInfo" autocomplete="off"  method="post" class="form-horizontal" >
		<form:hidden path="id"/>
		<form:hidden path="longitude"/>
		<form:hidden path="latitude"/>
		<form:hidden path="zoom"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td class="width-15 active"><label class="pull-right">代理商编号:</label></td>
                  <td>
				   <form:input path="projectNo" htmlEscape="false"  class="form-control " readonly="true"/>
				 </td>
		         <td  class="width-15 active" class="active"><label class="pull-right"><font color="red">*</font>客户类型：</label>
				 </td>
		         <td class="width-35">
					 <form:select path="customerType" class="form-control ">
						 <option value="agent">代理商</option>
<%--						 <form:options items="${fns:getDictList('customer_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
					 </form:select>
				 </td>
			  </tr>
			  <tr>

				  <td  class="width-15 active" class="active"><label class="pull-right"><font color="red">*</font>代理商简称：</label>
				  </td>
				  <td class="width-35">
					  <form:input path="projectName" htmlEscape="false"    class="form-control required"/>
				  </td>
				  <td class="width-15 active"><label class="pull-right">代理商全称:</label></td>
				  <td>
					  <form:input path="projectAllName" htmlEscape="false"  class="form-control " />
				  </td>
			  </tr>
			  <tr>
				  <td  class="width-15 active" class="active"><label class="pull-right"><font color="red">*</font>平台版本：</label>
				  </td>
				  <td class="width-35">
					  <form:select path="version" class="form-control ">
						  <form:options items="${fns:getDictList('version_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					  </form:select>
				  </td>
				  <td class="width-15 active"><label class="pull-right">合同编号:</label></td>
				  <td>
					  <form:input path="contractNo" htmlEscape="false"  class="form-control " />
				  </td>
			  </tr>
			  <tr>
				  <td class="width-15 active" class="active"><label class="pull-right"><font color="red">*</font>账号:</label></td>
				  <td>
					  <form:input path="loginName" htmlEscape="false"  class="form-control required" />
				  </td>
				  <td  class="width-15 active" class="active"><label class="pull-right">账号状态：</label></td>
				  <td class="width-35">
					  <form:select path="status" class="form-control ">
						  <form:options items="${fns:getDictList('on_off')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					  </form:select>
				  </td>

			  </tr>

			  <tr>
				  <td  class="width-15 active" class="active"><label class="pull-right"><font color="red">*</font>联系人：</label>
				  </td>
				  <td class="width-35">
					  <form:input path="customer" htmlEscape="false"    class="form-control required"/>
				  </td>
				  <td class="width-15 active"><label class="pull-right">联系人电话:</label></td>
				  <td>
					  <form:input path="customerPhone" htmlEscape="false"  class="form-control " />
				  </td>
			  </tr>

			  <tr>
				  <td  class="width-15 active" class="active"><label class="pull-right"><font color="red">*</font>业务负责人：</label>
				  </td>
				  <td class="width-35">
					  <form:input path="business" htmlEscape="false"    class="form-control required"/>
				  </td>
				  <td class="width-15 active"><label class="pull-right">负责人电话:</label></td>
				  <td>
					  <form:input path="businessPhone" htmlEscape="false"  class="form-control " />
				  </td>
			  </tr>


		      <tr>
				 <td class="width-15 active"><label class="pull-right">备注:</label></td>
		         <td class="width-35"><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control "/></td>
		      </tr>
			  <tr style="display: none;">
				  <td  class="width-15 active" class="active"><label class="pull-right">子域位置：</label></td>
				  <td class="width-35" colspan="3">
					  <div id="locationMap"></div>
				  </td>
			  </tr>
			</tbody>
			</table>
		<%--<sys:message content="${message}"/>
		<c:if test="${fns:hasPermission('project:projectInfo:edit') || isAdd}">
			<div class="col-lg-3"></div>
			<div class="col-lg-6">
				<div class="form-group text-center">
					<div>
						<button class="btn btn-primary btn-block btn-lg btn-parsley"
								data-loading-text="正在提交...">提 交
						</button>
					</div>
				</div>
			</div>
		</c:if>--%>
	</form:form>
</body>
</html>