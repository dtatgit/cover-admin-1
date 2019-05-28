<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>井盖数据处理任务管理</title>
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
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
					jp.post("${ctx}/cv/task/coverTaskInfo/save",$('#inputForm').serialize(),function(data){
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

            $('#beginCreateDate').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
            $('#endCreateDate').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });

		});
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="coverTaskInfo" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">任务编号：</label></td>
					<td class="width-35">
						<form:input path="taskNo" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">所属部门：</label></td>
					<td class="width-35">
						<sys:treeselect id="office" name="office.id" value="${coverTaskInfo.office.id}" labelName="office.name" labelValue="${coverTaskInfo.office.name}"
										title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">任务名称：</label></td>
					<td class="width-35">
						<form:input path="taskName" htmlEscape="false"    class="form-control  required"/>
					</td>
				</tr>
				<%--<tr>
					<td class="width-15 active"><label class="pull-right">任务状态：</label></td>
					<td class="width-35">
						<form:select path="taskStatus" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('task_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">任务类型：</label></td>
					<td class="width-35">
						<form:input path="taskType" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>--%>
				<%--<tr>
					<td class="width-15 active"><label class="pull-right">任务数量：</label></td>
					<td class="width-35">
						<form:input path="taskNum" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">任务内容：</label></td>
					<td class="width-35">
						<form:input path="taskContent" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>--%>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35" colspan="3">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<%--<td class="width-15 active"><label class="pull-right">备用：</label></td>
					<td class="width-35">
						<form:input path="spare" htmlEscape="false"    class="form-control "/>
					</td>--%>
				</tr>
		 	</tbody>
		</table>

			<!-- 搜索 -->
		<%--	<div class="accordion-group">--%>
		<%--		<div  class="accordion-body collapse">--%>
				<%--	<div class="accordion-inner">--%>
			<div class="examinebox"><h1 class="title2">筛选任务内容</h1></div>
			<div class="taskinfosx">
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="状态：">状态：</label>
								<form:select path="cover.coverStatus"  class="form-control m-b">
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('cover_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="编号：">编号：</label>
								<form:input path="cover.no" htmlEscape="false" maxlength="20"  class=" form-control"/>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="井盖类型：">井盖类型：</label>
								<form:select path="cover.coverType"  class="form-control m-b">
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('cover_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="市：">市：</label>
								<form:input path="cover.city" htmlEscape="false" maxlength="40"  class=" form-control"/>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="区：">区：</label>
								<form:input path="cover.district" htmlEscape="false" maxlength="40"  class=" form-control"/>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="街道（办事处）：">街道（办事处）：</label>
								<form:input path="cover.township" htmlEscape="false" maxlength="40"  class=" form-control"/>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="地址：路（街巷）：">地址：路（街巷）：</label>
								<form:input path="cover.street" htmlEscape="false" maxlength="80"  class=" form-control"/>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="井位用途：">井位用途：</label>
								<form:select path="cover.purpose"  class="form-control m-b">
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('cover_purpose')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="井位地理场合：">井位地理场合：</label>
								<form:select path="cover.situation"  class="form-control m-b">
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('cover_situation')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="制造商：">制造商：</label>
								<form:input path="cover.manufacturer" htmlEscape="false" maxlength="200"  class=" form-control"/>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="尺寸规格：">尺寸规格</label>
								<form:select path="cover.sizeSpec"  class="form-control m-b">
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('cover_size_spec')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="井盖规格：">井盖规格：</label>
								<form:select path="cover.sizeRule"  class="form-control m-b">
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('cover_size_rule')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="井盖材质：">井盖材质：</label>
								<form:select path="cover.material"  class="form-control m-b">
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('cover_material')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="权属单位：">权属单位：</label>
								<form:select path="cover.ownerDepart"  class="form-control m-b">
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('cover_owner_depart')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>

							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="井盖损坏形式：">井盖损坏形式：</label>
								<form:select path="cover.damageType"  class="form-control m-b">
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('cover_damage')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>

							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="是否损毁：">是否损毁：</label>
								<form:select path="cover.isDamaged"  class="form-control m-b">
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('boolean')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="高度差：">高度差：</label>
								<div class="col-xs-12">
									<div class="col-xs-12 col-sm-5">
										<div class='input-group' id='beginAltitudeIntercept' style="left: -10px;" >
											<form:input path="cover.beginAltitudeIntercept" htmlEscape="false"  class=" form-control"/>
										</div>
									</div>
									<div class="col-xs-12 col-sm-1">
										~
									</div>
									<div class="col-xs-12 col-sm-5">
										<div class='input-group' id='endAltitudeIntercept' style="left: -10px;" >
											<form:input path="cover.endAltitudeIntercept" htmlEscape="false"  class=" form-control"/>

										</div>
									</div>
								</div>
								<%--<form:input path="cover.altitudeIntercept" htmlEscape="false"    class="form-control "/>--%>
								<%--<form:select path="cover.altitudeIntercept"  class="form-control m-b">
									<form:option value="" label=""/>
									<form:options items="${fns:getDictList('cover_altitude_intercept')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>--%>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="创建人：">创建人：</label>
								<sys:userselect  id="createBy" name="createBy.id" value="${cover.createBy.id}" labelName="cover.createBy.name" labelValue="${cover.createBy.name}"
												cssClass="form-control "/>
							</div>
							<div class="col-xs-12 col-sm-6">
								<div class="form-group">
									<label style="width: 82px" class="label-item single-overflow pull-left" title="创建时间：">&nbsp;创建时间：</label>
									<div style="width: 80%;display: flex;align-items: center">
										<div class="">
											<div class='input-group date' id='beginCreateDate' style="width: 100%!important;" >
												<input type='text'  name="cover.beginCreateDate" class="form-control"  />
												<span class="input-group-addon">
			                        				<span class="glyphicon glyphicon-calendar"></span>
			                    				</span>
											</div>
										</div>
										<div style="margin:0 5px">
											~
										</div>
										<div class="">
											<div class='input-group date' id='endCreateDate' style="width: 100%!important;" >
												<input type='text'  name="cover.endCreateDate" class="form-control" />
												<span class="input-group-addon">
			                        			<span class="glyphicon glyphicon-calendar"></span>
			                    				</span>
											</div>
										</div>
									</div>
								</div>
							</div>

			</div>
			<%--		</div>--%>
			<%--	</div>--%>
	<%--		</div>--%>


	</form:form>
</body>
</html>