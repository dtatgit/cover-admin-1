<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工单信息管理</title>
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
					jp.post("${ctx}/cb/work/coverWork/save",$('#inputForm').serialize(),function(data){
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

        function selectFlowId(){
            //拿到要操作的下拉列表的引用
            var select = document.getElementById("flowProcId");
            var coverId= $("#coverId").val();
            var initFlowId= $("#initFlowId").val();

            if(coverId==""){
                alert("请选择井盖信息！");
            }else if(select.length<=1){

                $.ajax({
                    url: "${ctx}/flow/base/flowProc/ajaxFlowByCover",
                    type: "POST",
                    dataType: "json",
                    data: {
                        "coverId": coverId
                    },
                    async: false,
                    success: function(data) {

                        //拿到要操作的下拉列表的引用
                        /*     var select = document.getElementById("flowProcId");*/
                        select.length = 0//清空下拉列表
                        $.each(data, function(index, element) {
                            var op = new Option(element.flowNo, element.flowId);//Value和文本可以通过数组传入
							if(initFlowId==element.flowId){
                                op.selected=true;
							}

                            select.appendChild(op);//将一个选项添加到该下拉列表中
                        });
                    },
                    error: function() {
                        alert("error");
                    }
                });

            }else{
                /* alert("已经加载过数据了！");*/
            }

        }
	</script>
	<style>
		.my_select{

		}
	</style>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="coverWork" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">井盖信息：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/cv/equinfo/cover/data" id="cover" name="cover.id" value="${coverWork.cover.id}" labelName="cover.no" labelValue="${coverWork.cover.no}"
							 title="选择井盖信息" cssClass="form-control required" fieldLabels="编号|地址" fieldKeys="no|addressDetail" searchLabels="编号" searchKeys="no" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">工单编号：</label></td>
					<td class="width-35">
						<form:input path="workNum" htmlEscape="false"  readonly="true"   class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">工单类型：</label></td>
					<td class="width-35">
						<form:select path="workType" class="form-control ">
						<%--	<form:option value="" label=""/>--%>
							<form:options items="${fns:getDictList('work_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">工单状态：</label></td>
					<td class="width-35">
						<form:input path="workStatus"  htmlEscape="false"  class="form-control "/>
						<%--<form:select path="workStatus" class="form-control " readonly="true" disabled="true" >
							<form:options items="${fns:getDictList('work_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>--%>
					</td>
				</tr>
				<tr>

					<td class="width-15 active"><label class="pull-right">施工人员：</label></td>
					<td class="width-35">
						<sys:userselect id="constructionUser" name="constructionUser.id" value="${coverWork.constructionUser.id}" labelName="constructionUser.name" labelValue="${coverWork.constructionUser.name}"
							    cssClass="form-control"/>
					</td>
					<td class="width-15 active"><label class="pull-right">紧急程度：</label></td>
					<td class="width-35">
						<form:select path="workLevel" class="form-control ">
			<%--				<form:option value="" label=""/>--%>
							<form:options items="${fns:getDictList('work_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<%--<td class="width-15 active"><label class="pull-right">联系电话：</label></td>
					<td class="width-35">
						<form:input path="phone" htmlEscape="false"    class="form-control required isTel"/>
					</td>--%>
						<td class="width-15 active"><label class="pull-right">施工部门：</label></td>
						<td class="width-35">
							<sys:treeselect id="constructionDepart" name="constructionDepart.id" value="${coverWork.constructionDepart.id}" labelName="constructionDepart.name" labelValue="${coverWork.constructionDepart.name}"
											title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
						</td>
					<%--<td class="width-15 active"><label class="pull-right">施工部门：</label></td>
					<td class="width-35">
						<sys:treeselect id="constructionDepart" name="constructionDepart.id" value="${coverWork.constructionDepart.id}" labelName="constructionDepart.name" labelValue="${coverWork.constructionDepart.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</td>--%>
						<td class="width-15 active"><label class="pull-right">生命周期：</label></td>
						<td class="width-35">
							<form:select path="lifeCycle" class="form-control ">

							<form:options items="${fns:getDictList('lifecycle')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</td>
				</tr>
				<tr>


				</tr>

				<tr>
					<td class="width-15 active"><label class="pull-right">流程信息：</label></td>
					<td class="width-35">
					<%--	<input type="hidden" id="initFlowId" value="${coverWork.flowId.id}">
						<select class="form-control valid" id="flowProcId" name="flowId" onclick="selectFlowId()">
							<option value="${coverWork.flowId.id}">${coverWork.flowId.flowNo}</option>
						</select>--%>

					<%--	<form:input path="flowId.flowNo" htmlEscape="false"  readonly="true"   class="form-control "/>--%>
						<sys:gridselect url="${ctx}/flow/base/flowProc/data" id="flowId" name="flowId.id" value="${coverWork.flowId.id}" labelName="flowId.flowNo" labelValue="${coverWork.flowId.flowNo}"
							 title="选择流程信息" cssClass="form-control required" fieldLabels="流程编号|流程名称|版本" fieldKeys="flowNo|flowName|version" searchLabels="流程编号|流程名称|版本" searchKeys="flowNo|flowName|version" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">父工单：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/cb/work/coverWork/data" id="parentWorkId" name="parentWorkId.id" value="${coverWork.parentWorkId.id}" labelName="parentWorkId.workNum" labelValue="${coverWork.parentWorkId.workNum}"
							 title="选择父工单" cssClass="form-control " fieldLabels="井盖编号|工单编号|联系电话" fieldKeys="coverNo|workNum|phone" searchLabels="井盖编号|工单编号|联系电话" searchKeys="coverNo|workNum|phone" ></sys:gridselect>
					</td>
				</tr>

				<tr>
					<td class="width-15 active"><label class="pull-right">施工内容：</label></td>
					<td class="width-35">
						<form:textarea path="constructionContent" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>