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


            $('#constructionUser').select2();

            $('#latestCompleteDate').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss",
                locale: moment.locale('zh-CN')
            });

            let workType = '${coverWork.workType}';
            if(!!workType){
                $("input[name=workType]").val(workType);
            }


           // if(checkUser()){
                validateForm = $("#inputForm").validate({
                    submitHandler: function(form){

                        let v =  $("#constructionUser").val();
                        $("#constructionUser2").val(v);

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
<div class="row">
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
                <c:forEach items="${coverWork.coverNos}" var="t">
                    <div>${t}</div>
                </c:forEach>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">工单类型：</label></td>
            <td class="width-35">
<%--                <form:select path="workType" class="form-control ">--%>
<%--                    <form:options items="${fns:getDictList('work_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
<%--                </form:select>--%>
                <input name="workType" type="radio" checked="checked" value="install"/>安装
                <input name="workType" type="radio" value="maintain"/>维护
            </td>
            <td class="width-15 active"><label class="pull-right">紧急程度：</label></td>
            <td class="width-35">
                <form:select path="workLevel" class="form-control ">
                    <form:options items="${fns:getDictList('work_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr style="display: none">
            <td class="width-15 active"><label class="pull-right">工单状态：</label></td>
            <td class="width-35">
                <form:input path="workStatus" value="S0" htmlEscape="false"  readonly="true"   class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>流程信息：</label></td>
            <td class="width-35">
                <sys:gridselect url="${ctx}/flow/base/flowProc/data" id="flowId" name="flowId.id" value="${coverWork.flowId.id}" labelName="flowId.flowNo" labelValue="${coverWork.flowId.flowNo}"
                                title="选择流程信息" cssClass="form-control" fieldLabels="流程编号|流程名称|版本" fieldKeys="flowNo|flowName|version" searchLabels="流程编号|流程名称|版本" searchKeys="flowNo|flowName|version" ></sys:gridselect>

            </td>

        </tr>
        <tr style="display: none">
            <td class="width-15 active"><label class="pull-right">生命周期：</label></td>
            <td class="width-35">
                <form:select path="lifeCycle" class="form-control " readonly="true" >
                    <form:option value="notAssign" label="未接单"/>
                    <%--		<form:options items="${fns:getDictList('lifecycle')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
                </form:select>
            </td>
            <td class="width-15 active"><label class="pull-right">父工单：</label></td>
            <td class="width-35">
                <sys:gridselect url="${ctx}/cb/work/coverWork/data" id="parentWorkId" name="parentWorkId.id" value="${coverWork.parentWorkId.id}" labelName="parentWorkId.workNum" labelValue="${coverWork.parentWorkId.workNum}"
                                title="选择父工单" cssClass="form-control " fieldLabels="井盖编号|工单编号|联系电话" fieldKeys="coverNo|workNum|phone" searchLabels="井盖编号|工单编号|联系电话" searchKeys="coverNo|workNum|phone" ></sys:gridselect>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">详细说明：</label></td>
            <td class="width-35" colspan="3">
                <form:textarea path="constructionContent" htmlEscape="false" rows="4"    class="form-control "/>
            </td>
<%--            <td class="width-15 active"><label class="pull-right">备注信息：</label></td>--%>
<%--            <td class="width-35">--%>
<%--                <form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>--%>
<%--            </td>--%>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">最迟完成时间：</label></td>
            <td class="width-35">
                <div class='input-group date'  >
                    <input type='text'  id='latestCompleteDate' name="latestCompleteDate" class="form-control"  />
                    <span class="input-group-addon">
                            					<span class="glyphicon glyphicon-calendar"></span>
                        					</span>
                </div>
            </td>
            <td class="width-15 active"><label class="pull-right">处理人：</label></td>
            <td class="width-35" >
<%--                <sys:userselect id="constructionUser" name="constructionUser.id" value="${coverWork.constructionUser.id}" labelName="constructionUser.name" labelValue="${coverWork.constructionUser.name}"--%>
<%--                                cssClass="form-control"/>--%>

                <select id="constructionUser" class="form-control">
                    <option value=""></option>
                    <c:forEach items="${userList}" var="user">
                        <option value="${user.id}">${user.name}/${user.loginName}</option>
                    </c:forEach>
                </select>
                <input type="hidden" id="constructionUser2" name="constructionUser.id" />
            </td>
        </tr>
        <tr>

        </tr>
        </tbody>
    </table>
</form:form>
</div>
</body>
</html>
