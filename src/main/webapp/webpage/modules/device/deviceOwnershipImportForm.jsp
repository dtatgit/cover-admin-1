<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备归属管理</title>
	<meta name="decorator" content="ani"/>
	<link href="${ctxStatic}/plugin/select2/css/select2.min.css" rel="stylesheet" type="text/css"/>
	<script src="${ctxStatic}/plugin/select2/js/select2.min.js" type="text/javascript"></script>
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

		    var interfaceUrl = "${interfaceUrl}";


		    $("#serverUrl").select2();


			validateForm = $("#inputForm").validate({
				submitHandler: function(form){


                    var importFile = $("#uploadFile").val();
                    if(importFile==''){
                        jp.alert("请选择excel文件");
                        return;
                    }
                    var formData = new FormData($('#inputForm')[0]);

                    $.ajax({
                        url :  interfaceUrl,
                        type : 'POST',
                        async : false,
                        data : formData,
                        // 告诉jQuery不要去处理发送的数据
                        processData : false,
                        // 告诉jQuery不要去设置Content-Type请求头
                        contentType : false,
                        beforeSend:function(){
                            console.log("正在进行，请稍候");
                        },
                        success : function(data) {
                            if(data.success){
                                jp.success("导入成功");
                                $table.bootstrapTable('refresh');
                                jp.close($topIndex);//关闭dialog
                            }else{
                                jp.alert(data.msg);
                            }
                        },
                        error: function () {
                            jp.error('导入失败!');
                        }
                    });

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
		<form:form id="inputForm" modelAttribute="deviceOwnership" class="form-horizontal" method="post" enctype="multipart/form-data">
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>设备：</label></td>
					<td class="width-35">
						<input id="uploadFile" name="file" type="file" class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>设备厂家：</label></td>
					<td class="width-35">
						<form:select path="dtype" class="form-control required">
							<form:options items="${fns:getDictList('bellType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>服务器：</label></td>
					<td class="width-35">
						<select id="serverUrlId" name="serverUrlId" class="form-control required">
							<option value=""></option>
							<c:forEach items="${serverList}" var="item">
								<option value="${item.id}">${item.name}</option>
							</c:forEach>
						</select>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>