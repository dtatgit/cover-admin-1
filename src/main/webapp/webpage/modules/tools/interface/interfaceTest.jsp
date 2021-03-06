<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<meta charset="utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="interfaceTest.js" %>
	</head> 
<body>
<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">接口测试</h3>
	</div>
		<div class="row">
            <div class="col-sm-12">
                <h5>&nbsp;&nbsp; ${testInterface.name == null?'服务器':testInterface.name}接口测试</h5>
                        <form method="get" class="form-horizontal">
                        
                        	<div class="form-group">
                                <label class="col-sm-2 control-label">接口类型</label>

                                <div class="col-sm-10">
                                		<input type="hidden" name="S_TYPE" id="S_TYPE" value="${testInterface.type eq 'post'?'POST':'GET'}"/>
                                       <input name="form-field-radio"  class="form-control i-checks" type="radio" value="POST" <c:if test="${testInterface.type eq 'post'}">checked="checked"</c:if> >POST
										<input name="form-field-radio"  class="form-control i-checks" type="radio" value="GET" <c:if test="${testInterface.type eq 'get'}">checked="checked"</c:if> >GET
                                </div>
                            </div>
                            <div class="hr-line-dashed"></div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">请求url</label>
                                <div class="col-sm-8">
                                         <input type="text" id="serverUrl" title="输入请求地址" value="${testInterface.url }"  class="form-control" >
                                          <span><font color="gray">如果URL包括JSESSIONID字段，请先调用登录接口，把生成的JSESSIONID参数替换此处的值，否则会提示没有登录。</font></span>
                                </div>
                                <div class="col-sm-2">
                                          <a class="btn btn-small btn-success" onclick="sendSever();">请求</a>
										 <a class="btn btn-small btn-info" onclick="gReload();">重置</a>
                                </div>
                                        
                              
                            </div>
                                <div class="hr-line-dashed"></div>
                              <div class="form-group">
                                <label class="col-sm-2 control-label">post body</label>
                                <div class="col-sm-8">
                                         <input type="text" id="requestBody" title="输入请求地址" value="${testInterface.body }"  class="form-control" >
                                          <span><font color="gray">格式:  name1=value1&name2=value2, 如果是get请求请留空。</font></span>
                                </div>
                                <div class="col-sm-2">
                                        
                                </div>
                                        
                              
                            </div>
                            <div class="hr-line-dashed"></div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">返回结果</label>

                                <div class="col-sm-8">
                                   <textarea id="json-field" title="返回结果" class="form-control span12"></textarea>
                                </div>
                                <div class="col-sm-2">
                                </div>
                            </div>
                            <div class="hr-line-dashed"></div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">服务器响应：</label>
                                <div class="col-sm-10">
                                	<font color="red" id="stime">-</font>&nbsp;毫秒
                                </div>
                             </div>
                            <div class="hr-line-dashed"></div>
                            <div class="form-group">
                                 <label class="col-sm-2 control-label">客户端请求：</label>
                                <div class="col-sm-10">
                                	<font color="red" id="ctime">-</font>&nbsp;毫秒
                                </div>
                           </div>
                            <div class="hr-line-dashed"></div>
                         </form>
                    </div>
                </div>
            </div>
        </div>
		

		<!--引入属于此页面的js -->
		<script type="text/javascript" src="${ctxStatic }/common/js/interfaceTest.js"></script>
	</body>
</html>

