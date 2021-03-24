<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>业务报警详情</title>
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
			/*validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					jp.post("${ctx}/oilSmokeData/oilSmokeData/save",$('#inputForm').serialize(),function(data){
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
			});*/

		});
	</script>
</head>
<body class="bg-white">
<div class="wrapper wrapper-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">
						<a class="panelButton" href="${ctx}/cb/alarm/bizAlarm/list"><i class="ti-angle-left"></i> 返回</a>
					</h3>
				</div>
				<div class="panel-body">
					<form:form id="inputForm" modelAttribute="cover" class="form-horizontal">
						<form:hidden path="id"/>
						<sys:message content="${message}"/>
						<table class="table table-bordered">
							<tbody>
							<tr>
								<td class="width-15 active" colspan="4"><label class="pull-left"><h4>井盖信息</h4></label></td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">井盖编号：</label></td>
								<td class="width-35">
										${cover.no}
								</td>
								<td class="width-15 active"><label class="pull-right">详细地址：</label></td>
								<td class="width-35">
										${cover.addressDetail}
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">管网用途：</label></td>
								<td class="width-35">
										${cover.purpose}
								</td>
								<td class="width-15 active"><label class="pull-right">井卫地理场合：</label></td>
								<td class="width-35">
										${cover.situation}
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">尺寸规格：</label></td>
								<td class="width-35">
										${cover.sizeSpec}
								</td>
								<td class="width-15 active"><label class="pull-right">井盖规格：</label></td>
								<td class="width-35">
										${cover.sizeRule}
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">直径：</label></td>
								<td class="width-35">
										${cover.sizeDiameter}
								</td>
								<td class="width-15 active"><label class="pull-right">半径：</label></td>
								<td class="width-35">
										${cover.sizeRadius}
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">长度：</label></td>
								<td class="width-35">
										${cover.sizeLength}
								</td>
								<td class="width-15 active"><label class="pull-right">宽度：</label></td>
								<td class="width-35">
										${cover.sizeWidth}
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">井盖材质：</label></td>
								<td class="width-35">
										${cover.material}
								</td>
								<td class="width-15 active"><label class="pull-right">井盖类型：</label></td>
								<td class="width-35">
										${cover.coverType}
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">高度差：</label></td>
								<td class="width-35">
										${cover.altitudeIntercept}
								</td>
								<td class="width-15 active"><label class="pull-right">是否损坏：</label></td>
								<td class="width-35">
										${fns:getDictLabel (cover.isDamaged, "boolean", "--")}
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">损毁情况备注：</label></td>
								<td class="width-35">
										${cover.damageRemark}
								</td>
								<td class="width-15 active"><label class="pull-right">采集人：</label></td>
								<td class="width-35">
										${cover.createBy.name}
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">采集时间:：</label></td>
								<td class="width-35">
									<fmt:formatDate value="${cover.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</td>
								<td class="width-15 active"><label class="pull-right">权属单位:</label></td>
								<td class="width-35">
										${cover.ownerDepart}
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">井盖病害：</label></td>
								<td class="width-35" colspan="3">
									<c:forEach items="${cover.coverDamageList}" var="damage">
										<span class="t">${fns:getDictLabel (damage.damage, "cover_damage", "--")}</span>
									</c:forEach>
								</td>

							</tr>
							</tbody>
						</table>
					</form:form>
				</div>
				<div class="panel-body">
					<form:form id="inputForm" modelAttribute="coverBell" class="form-horizontal">
						<form:hidden path="id"/>
						<sys:message content="${message}"/>
						<table class="table table-bordered">
							<tbody>
							<tr>
								<td class="width-15 active" colspan="4"><label class="pull-left"><h4>井卫信息</h4></label></td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">井卫编号：</label></td>
								<td class="width-35">
										${coverBell.coverNo}
								</td>
								<td class="width-15 active"><label class="pull-right">警铃类型：</label></td>
								<td class="width-35">
										${coverBell.bellModel}
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">固件版本号：</label></td>
								<td class="width-35">
										${coverBell.version}
								</td>
								<td class="width-15 active"><label class="pull-right">IMEI：</label></td>
								<td class="width-35">
										${coverBell.imei}
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">设备类型:</label></td>
								<td class="width-35">
										${fns:getDictLabel (coverBell.bellType, " bellType", "--")}
								</td>
								<td class="width-15 active"><label class="pull-right">工作状态:</label></td>
								<td class="width-35">
										${fns:getDictLabel (coverBell.workStatus, "bell_work_status", "--")}
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">生命周期:</label></td>
								<td class="width-35">
										${fns:getDictLabel (coverBell.bellStatus, "bell_status", "--")}
								</td>
								<td class="width-15 active"><label class="pull-right">设防状态:</label></td>
								<td class="width-35">
										${fns:getDictLabel (coverBell.defenseStatus, "defense_status", "--")}
								</td>
							</tr>
							</tbody>
						</table>
					</form:form>
				</div>
				<div class="panel-body">
					<form:form id="inputForm" modelAttribute="bizAlarm" class="form-horizontal">
						<form:hidden path="id"/>
						<sys:message content="${message}"/>
						<table class="table table-bordered">
							<tbody>
							<tr>
								<td class="width-15 active" colspan="4"><label class="pull-left"><h4>业务报警信息</h4></label></td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">报警编号：</label></td>
								<td class="width-35">
										${bizAlarm.alarmNo}
								</td>
								<td class="width-15 active"><label class="pull-right">报警类型：</label></td>
								<td class="width-35">
										${fns:getDictLabel (bizAlarm.alarmType, "biz_alarm_type", "--")}
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">报警时间：</label></td>
								<td class="width-35" colspan="3">
									<fmt:formatDate value="${bizAlarm.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</td>
							</tr>
							</tbody>
						</table>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>