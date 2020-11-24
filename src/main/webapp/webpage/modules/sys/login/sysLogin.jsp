pull-right btn btn-info btn-circle<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<!-- _login_page_ --><!--登录超时标记 勿删-->
<!DOCTYPE html>
<html>

	<head>
	<meta name="decorator" content="ani"/>
		<title>${fns:getConfig('productName')} 登录</title>
		<script>
			if (window.top !== window.self) {
				window.top.location = window.location;
			}
		</script>
		<script type="text/javascript">
				$(document).ready(function() {
					$("#loginForm").validate({
						rules: {
							validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
						},
						messages: {
							username: {required: "请填写用户名."},password: {required: "请填写密码."},
							validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
						},
						errorLabelContainer: "#messageBox",
						errorPlacement: function(error, element) {
							error.appendTo($("#loginError").parent());
						}
					});
				});
				// 如果在框架或在对话框中，则弹出提示并跳转到首页
				if(self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0){
					alert('未登录或登录超时。请重新登录，谢谢！');
					top.location = "${ctx}";
				}
		</script>
		<style type="text/css">
			body{
				background: -webkit-linear-gradient(#005bc5, #5bcdf3); /* Safari 5.1 - 6.0 */
				background: -o-linear-gradient(#005bc5, #5bcdf3); /* Opera 11.1 - 12.0 */
				background: -moz-linear-gradient(#005bc5, #5bcdf3); /* Firefox 3.6 - 15 */
				background: linear-gradient(#005bc5, #5bcdf3); /* 标准的语法 */
			}
            .form-group .form-control{
                width: 80%;
                margin-left: 15px;
                font-size: 16px;
                color: #555;
            }
		</style>

	</head>


	<body>


	<div class="login-page">
		<div class="login">
				<h1>窨井盖查勘鉴权管理后台</h1>
				<sys:message content="${message}" showType="1"/>
				<form id="loginForm" role="form" action="${ctx}/login" method="post">
					<div class="form-content">
						<div class="form-group">
							<label class="icon user"></label>
							<input type="text" id="username" name="username" class="form-control input-underline required"  value="" placeholder="请输入您的账号">
						</div>
						<div class="form-group">
							<label class="icon pwd"></label>
							<input type="password" id="password" name="password" value="" class="form-control input-underline required" placeholder="请输入您的密码">
						</div>
						<c:if test="${isValidateCodeLogin}">
						<div class="form-group  text-muted">
							<label class="inline"><font>验证码:</font></label>
							<sys:validateCode name="validateCode" inputCssStyle="margin-bottom:5px;" buttonCssStyle="color:#555"/>
						</div>
						</c:if>
						<ul class="pull-right btn btn-info btn-circle" style="background-color:white;height:45px;width:46px;display: none">
								<li class="dropdown color-picker" >
									<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
										<span><i class="fa fa-circle"></i></span>
									</a>
									<ul class="dropdown-menu pull-right animated fadeIn" role="menu">
										<li class="padder-h-xs">
											<table class="table color-swatches-table text-center no-m-b">
												<tr>
													<td class="text-center colorr">
														<a href="#" data-theme="blue" class="theme-picker">
															<i class="fa fa-circle blue-base"></i>
														</a>
													</td>
													<td class="text-center colorr">
														<a href="#" data-theme="green" class="theme-picker">
															<i class="fa fa-circle green-base"></i>
														</a>
													</td>
													<td class="text-center colorr">
														<a href="#" data-theme="red" class="theme-picker">
															<i class="fa fa-circle red-base"></i>
														</a>
													</td>
												</tr>
												<tr>
													<td class="text-center colorr">
														<a href="#" data-theme="purple" class="theme-picker">
															<i class="fa fa-circle purple-base"></i>
														</a>
													</td>
													<td class="text-center color">
														<a href="#" data-theme="midnight-blue" class="theme-picker">
															<i class="fa fa-circle midnight-blue-base"></i>
														</a>
													</td>
													<td class="text-center colorr">
														<a href="#" data-theme="lynch" class="theme-picker">
															<i class="fa fa-circle lynch-base"></i>
														</a>
													</td>
												</tr>
											</table>
										</li>
									</ul>
								</li>
						</ul>
						<div class="form-group">
							<input  type="checkbox" id="rememberMe" name="rememberMe" ${rememberMe ? 'checked' : ''} class="ace" />
							<span class="lbl">记住密码</span>
						</div>
					</div>
					<input type="submit" class="btn btn-login"  value="登录">
					<%--<a href="${ctx}/sys/register" class="btn btn-white btn-outline btn-lg btn-rounded progress-login">注册</a>--%>
				</form>
		</div>
		<footer>版权所有©大唐安途</footer>
	</div>

	<script>


$(function(){
		$('.theme-picker').click(function() {
			changeTheme($(this).attr('data-theme'));
		});

});

function changeTheme(theme) {
	$('<link>')
	.appendTo('head')
	.attr({type : 'text/css', rel : 'stylesheet'})
	.attr('href', '${ctxStatic}/common/css/app-'+theme+'.css');
	//$.get('api/change-theme?theme='+theme);
	 $.get('${pageContext.request.contextPath}/theme/'+theme+'?url='+window.top.location.href,function(result){  });
}
</script>
<style>
li.color-picker i {
    font-size: 24px;
    line-height: 30px;
}
.red-base {
    color: #D24D57;
}
.blue-base {
    color: #3CA2E0;
}
.green-base {
    color: #27ae60;
}
.purple-base {
    color: #957BBD;
}
.midnight-blue-base {
    color: #2c3e50;
}
.lynch-base {
    color: #6C7A89;
}
</style>
</body>
</html>
