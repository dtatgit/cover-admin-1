<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>井盖基础信息管理</title>
	<meta name="decorator" content="ani"/>
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
					jp.post("${ctx}/cv/equinfo/cover/save",$('#inputForm').serialize(),function(data){
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
			
		});
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="cover" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">井盖状态：</label></td>
					<td class="width-35">
						<form:select path="coverStatus" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('cover_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">编号：</label></td>
					<td class="width-35">
						<form:input path="no" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>井盖类型：</label></td>
					<td class="width-35">
						<form:select path="coverType" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('cover_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>地址：省：</label></td>
					<td class="width-35">
						<form:input path="province" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>地址：市：</label></td>
					<td class="width-35">
						<form:input path="city" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">地址：城市代码（0516）：</label></td>
					<td class="width-35">
						<form:input path="cityCode" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">地址：行政区划代码（320312）：</label></td>
					<td class="width-35">
						<form:input path="adCode" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>地址：区：</label></td>
					<td class="width-35">
						<form:input path="district" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">地址：街道（办事处）：</label></td>
					<td class="width-35">
						<form:input path="township" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">地址：路（街巷）：</label></td>
					<td class="width-35">
						<form:input path="street" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">地址：门牌号：</label></td>
					<td class="width-35">
						<form:input path="streetNumber" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">地址：详细地址：</label></td>
					<td class="width-35">
						<form:input path="addressDetail" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">坐标类型：gcj02: 国测局坐标系gps: WGS-84：</label></td>
					<td class="width-35">
						<form:input path="coordinateType" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>经度：</label></td>
					<td class="width-35">
						<form:input path="longitude" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>纬度：</label></td>
					<td class="width-35">
						<form:input path="latitude" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">海拔（m）：</label></td>
					<td class="width-35">
						<form:input path="altitude" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">定位精度（m）：</label></td>
					<td class="width-35">
						<form:input path="locationAccuracy" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">海拔精度（m）：</label></td>
					<td class="width-35">
						<form:input path="altitudeAccuracy" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>井位用途：</label></td>
					<td class="width-35">
						<form:select path="purpose" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('cover_purpose')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>井位地理场合：</label></td>
					<td class="width-35">
						<form:select path="situation" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('cover_situation')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">制造商：</label></td>
					<td class="width-35">
						<form:input path="manufacturer" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">尺寸规格D800 : 圆形直径800mmR800x600 : 矩形 H800（长）W600（宽）：</label></td>
					<td class="width-35">
						<form:select path="sizeSpec" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('cover_size_spec')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>井盖规格（尺寸类型）：</label></td>
					<td class="width-35">
						<form:select path="sizeRule" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('cover_size_rule')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">尺寸：直径（mm）：</label></td>
					<td class="width-35">
						<form:input path="sizeDiameter" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">尺寸：半径（mm）** 已废弃，使用diameter字段 **：</label></td>
					<td class="width-35">
						<form:input path="sizeRadius" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">尺寸：长度（mm）：</label></td>
					<td class="width-35">
						<form:input path="sizeLength" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">尺寸：宽度（mm）：</label></td>
					<td class="width-35">
						<form:input path="sizeWidth" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">井盖材质：</label></td>
					<td class="width-35">
						<form:select path="material" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('cover_material')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">权属单位：</label></td>
					<td class="width-35">
						<form:select path="ownerDepart" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('cover_owner_depart')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">监管单位：</label></td>
					<td class="width-35">
						<form:input path="superviseDepart" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">地图标记：</label></td>
					<td class="width-35">
						<form:select path="marker" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('cover_damage')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否损毁：</label></td>
					<td class="width-35">
						<form:select path="isDamaged" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('boolean')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">井筒破损深度（m）：</label></td>
					<td class="width-35">
						<form:input path="manholeDamageDegree" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">损毁情况备注：</label></td>
					<td class="width-35">
						<form:input path="damageRemark" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">高度差，井中心与周边路面（1.5m范围）：</label></td>
					<td class="width-35">
						<form:select path="altitudeIntercept" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('cover_altitude_intercept')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>