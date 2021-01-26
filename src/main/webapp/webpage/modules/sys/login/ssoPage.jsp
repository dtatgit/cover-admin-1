<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="ani"/>
    <title>井盖基础平台</title>
    <script src="${ctxStatic}/plugin/jquery/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            var userName = $("#userName").val();
            var token = $("#token").val();
            var time = $("#time").val();
            window.location.href='${ctx}/sso?userName='+userName+'&token='+token+'&time='+time;
        });
    </script>
</head>
<body>
<form:form id="inputForm"   class="form-horizontal">
<input type="hidden" id="userName" value="${userName}" />
<input type="hidden" id="token" value="${token}" />
<input type="hidden" id="time" value="${time}" />
</form:form>
<h4>跳转中......</h4>
</body>
</html>
