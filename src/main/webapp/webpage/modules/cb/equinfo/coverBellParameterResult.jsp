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
                    var fields = $("#inputForm").serializeArray();
                    <%--jp.post("${ctx}/cb/equinfo/coverBell/setParam2",str,function(data){--%>
                    <%--    if(data.success){--%>
                    <%--        $table.bootstrapTable('refresh');--%>
                    <%--        jp.success(data.msg);--%>
                    <%--        jp.close($topIndex);//关闭dialog--%>

                    <%--    }else{--%>
                    <%--        jp.error(data.msg);--%>
                    <%--    }--%>
                    <%--})--%>

                    var obj = {}; //声明一个对象
                    $.each(fields, function(index, field) {
                        obj[field.name] = field.value; //通过变量，将属性值，属性一起放到对象中
                    })
                    $.ajax({
                        type: "post",
                        url: "${ctx}/cb/equinfo/coverBell/setParam2/"+$("#devNo").val(),
                        contentType: 'application/json',
                        dataType: 'json', //返回的数据格式：json/xml/html/script/jsonp/text
//                  data:$('#ff').serialize(),//这两种方式都不能直接将表单数据转换为json格式
//                  data:$('#ff').serializeArray(),
                        data: JSON.stringify(obj),//将对象转为json字符串
                        success: function(data) {
                            console.log("data.msg:",data);
                            if (data.success) {
                                $table.bootstrapTable('refresh');
                                jp.success(data.msg);
                                jp.close($topIndex);//关闭dialog

                            } else {
                                jp.error(data.msg);
                            }
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
<%--        <tr>--%>
<%--            <td class="width-15 active"><label class="pull-right">心跳时间(分)：</label></td>--%>
<%--            <td class="width-35">--%>
<%--                <form:input path="heartbeatTime" htmlEscape="false"    class="form-control "/>--%>
<%--            </td>--%>
<%--            <td class="width-15 active"><label class="pull-right">角度阈值：</label></td>--%>
<%--            <td class="width-35">--%>
<%--                <form:input path="angleThreshold" htmlEscape="false"    class="form-control "/>--%>
<%--            </td>--%>
<%--        </tr>--%>
        <c:forEach items="${paramList}" var="item">
            <tr>
                <td class="width-15 active"><label class="pull-right">${item.name}：</label></td>
                <td class="width-35" colspan="3">
                    <input type="text" class="form-control " name="${item.field}" value="${item.value}">
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</form:form>
</body>
</html>