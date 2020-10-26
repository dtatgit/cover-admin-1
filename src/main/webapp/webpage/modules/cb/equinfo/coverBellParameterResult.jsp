<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>设备参数管理</title>
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
                    jp.post("${ctx}/cb/equinfo/coverBell/setParam",$('#inputForm').serialize(),function(data){
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
<form:form id="inputForm" modelAttribute="deviceParameterResult" class="form-horizontal">
 <%--   <form:hidden path="id"/>--%>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-15 active"><label class="pull-right">设备编号：</label></td>
            <td class="width-35" colspan="3">
                <form:input path="devNo" htmlEscape="false" readonly="true"    class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">心跳周期(小时)：</label></td>
            <td class="width-35">
                <form:input path="durationMinutes" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">震动上报最小间隔时间(分钟)：</label></td>
            <td class="width-35">
                <form:input path="shakeAlarmDurationMinutes" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">震动触发等级：</label></td>
            <td class="width-35">
                <form:select path="gSensorLevel" class="form-control ">
                    <%--				<form:option value="" label=""/>--%>
                    <form:options items="${fns:getDictList('guard_sensor_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-15 active"><label class="pull-right">倾斜角度阈值：</label></td>
            <td class="width-35">
                <form:input path="angleThreshold" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">深度阈值：</label></td>
            <td class="width-35">
                <form:input path="depthThreshold" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">温度阈值：</label></td>
            <td class="width-35">
                <form:input path="temperatureThreshold" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>