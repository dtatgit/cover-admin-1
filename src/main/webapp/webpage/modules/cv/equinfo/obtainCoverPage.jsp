<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>获取井盖信息</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
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
                    jp.post("${ctx}/cv/equinfo/coverAudit/obtainCover",$('#inputForm').serialize(),function(data){
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

        function obtainCover(){
            jp.loading();
            jp.post("${ctx}/cv/equinfo/coverAudit/obtainCover",$('#inputForm').serialize(),function(data){
                if(data.success){

                    jp.success(data.msg);
                    $('#coverAuditTable').bootstrapTable('refresh');
                }else{
                    jp.error(data.msg);
                }
            })
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">获取井盖信息</h3>
		</div>
		<div class="panel-body">


			<div >
				<div >
					<div >
						<form:form id="inputForm" modelAttribute="coverAudit" class="form form-horizontal well clearfix">
							<div class="col-xs-12 col-sm-6 col-md-4">
								<label class="label-item single-overflow pull-left" title="区域：">区域：</label>
								<sys:treeselect id="area" name="area" value="${coverAudit.area.id}" labelName="" labelValue="${coverAudit.area.name}"
												title="区域" url="/sys/area/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
							</div>

							<div class="col-xs-12 col-sm-6 col-md-4">
								<div style="margin-top:26px">
									<a  onclick="obtainCover()" id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 获取任务</a>
									<a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>

	</div>
</div>
</body>
</html>