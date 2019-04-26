<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>井盖审核信息管理</title>
	<meta name="decorator" content="ani"/>
	<link href="${ctxStatic}/common/fonts/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
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
	<div class="examinebox examinebox1">
        <div class="map">
			放地图
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
				<li><label>详细地址:</label><span>${coverAudit.cover.no}</span></li>
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
				<input id="auditResult" name="auditResult" type="radio" value="1">
				<label for="auditResult">通过</label>
			</span>
			<span class="mui-radio">
				<input id="auditResult2" name="auditResult" type="radio" value="0" checked/>
				<label for="auditResult2">不通过</label>
			</span>
			<span class="mui-checkbox">
				<input id="checkbox1" type="checkbox" value="1" checked/>
				<label for="checkbox1">下一条</label>
			</span>
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