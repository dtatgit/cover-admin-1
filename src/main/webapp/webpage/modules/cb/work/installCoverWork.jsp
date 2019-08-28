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
           // if(checkUser()){
                validateForm = $("#inputForm").validate({
                    submitHandler: function(form){
                        jp.post("${ctx}/cb/work/coverWork/createWork",$('#inputForm').serialize(),function(data){
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

            // }else{
            //     alert("必须选择一个用户或者部门。");
            // }



        });

        function  checkUser() {
           var user= $("#constructionUserName").value();
           alert(user);
           var depart= $("#constructionDepartName").value();
           if(user==null&&depart==null){
                return false;
           }else{
               return true;
           }
        }
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="coverWork" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="coverIds"/>
    <form:hidden path="coverNos"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-15 active"><label class="pull-right">井盖编号：</label></td>
            <td class="width-35" colspan="3">
                <form:input path="coverNos" htmlEscape="false"  readonly="true"   class="form-control "/>
            </td>
        </tr>
<%--        <tr>
            <td class="width-15 active"><label class="pull-right">工单编号：</label></td>
            <td class="width-35">
                <form:input path="workNum" htmlEscape="false"  readonly="true"   class="form-control "/>
            </td>
        </tr>--%>
        <tr>
            <td class="width-15 active"><label class="pull-right">工单类型：</label></td>
            <td class="width-35">
                <select name="workType" class="form-control " readonly="true">
                    <option  value ="install">安装工单</option>
                </select>
            </td>
            <td class="width-15 active"><label class="pull-right">工单状态：</label></td>
            <td class="width-35">
                <select name="workStatus" class="form-control " readonly="true">
                    <option  value ="wait_receive">已指派</option>
                </select>
<%--                <form:select path="workStatus" class="form-control " readonly="true" disabled="true" >
                    <form:options items="${fns:getDictList('work_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>--%>
            </td>
        </tr>
        <tr>

            <td class="width-15 active"><label class="pull-right">施工人员：</label></td>
            <td class="width-35">
                <sys:userselect id="constructionUser" name="constructionUser.id" value="${coverWork.constructionUser.id}" labelName="constructionUser.name" labelValue="${coverWork.constructionUser.name}"
                                cssClass="form-control "/>
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
           <%-- <td class="width-15 active"><label class="pull-right">联系电话：</label></td>
            <td class="width-35">
                <form:input path="phone" htmlEscape="false"    class="form-control required isTel"/>
            </td>--%>
                <td class="width-15 active"><label class="pull-right">施工部门：</label></td>
                <td class="width-35">
                    <sys:treeselect id="constructionDepart" name="constructionDepart.id" value="${coverWork.constructionDepart.id}" labelName="constructionDepart.name" labelValue="${coverWork.constructionDepart.name}"
                        title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
                </td>
        </tr>
        <tr>


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