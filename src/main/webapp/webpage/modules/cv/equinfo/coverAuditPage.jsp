<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>井盖审核信息管理</title>
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
                    jp.post("${ctx}/cg/putforward/cgRefundInfo/saveAudit",$('#inputForm').serialize(),function(data){
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
	<sys:message content="${message}"/>
	<h1 class="title2">井盖信息</h1>
	<table class="table table-ullist">
		<tbody>
		<tr>
			<td class="width-15 active"><label class="pull-right">井盖编号：</label></td>
			<td class="width-35">
					${coverAudit.cover.no}
			</td>
			<td class="width-15 active"><label class="pull-right">详细地址：</label></td>
			<td class="width-35">
					${coverAudit.cover.addressDetail}
			</td>
		</tr>
		<tr>
			<td class="width-15 active"><label class="pull-right">经度：</label></td>
			<td class="width-35">
					${coverAudit.cover.longitude}
			</td>
			<td class="width-15 active"><label class="pull-right">纬度：</label></td>
			<td class="width-35">
					${coverAudit.cover.latitude}
			</td>
		</tr>
		<tr>
			<td class="width-15 active"><label class="pull-right">井盖用途：</label></td>
			<td class="width-35">
					${coverAudit.cover.purpose}
			</td>
			<td class="width-15 active"><label class="pull-right">申请时间：</label></td>
			<td class="width-35">
				<fmt:formatDate value="${coverAudit.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</td>

		</tr>



		</tbody>
	</table>
	<h1 class="title2">审核信息</h1>
	<table class="table table-ullist">
		<tbody>

		<tr>
			<td class="width-15 active"><label class="pull-right">审核状态：</label></td>
			<td class="width-35">
				<select id="refundStatus" name="refundStatus" class="form-control ">
					<option value="2">审核通过</option>
					<option value="3">审核不通过</option>
				</select>

			</td>
			<td class="width-15 active"><label class="pull-right">审核时间：</label></td>
			<td class="width-35">
				<p class="input-group">
				<div class='input-group form_datetime' id='auditTime'>
					<input type='text'  name="auditTime" class="form-control"  readonly="true"  value="<fmt:formatDate value="${cgRefundInfo.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					<span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
				</div>
				</p>
			</td>
		</tr>
		<tr>
			<td class="width-15 active"><label class="pull-right">审核用户：</label></td>
			<td class="width-35">
				<%--<form:hidden path="auditPersonId" value="${fns:getUser().id}" />--%>
				<%--<form:input path="auditPersonName" htmlEscape="false" value="${fns:getUser().name}"  readonly="true"  class="form-control "/>--%>
			</td>

		</tr>
		<tr>
			<td class="width-15 active"><label class="pull-right">审核描述：</label></td>
			<td class="width-35">
				<form:textarea path="auditResult" htmlEscape="false"  rows="4"    class="form-control "/>
			</td>
			<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
			<td class="width-35">
				<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
			</td>
			<td class="width-15 active"></td>
			<td class="width-35" ></td>
		</tr>
		</tbody>
	</table>

</form:form>
</body>
</html>