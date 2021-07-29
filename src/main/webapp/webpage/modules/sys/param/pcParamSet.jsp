<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>普查参数设置</title>
	<meta name="decorator" content="ani"/>

</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">普查参数设置</h3>
			</div>
			<div class="panel-body">
                <table class="table" id="">
                    <tbody>
                    <c:forEach items="${items}" var="item">
                        <tr>
                            <td width="150px">${item.description}：</td>
                            <td>
                                <c:forEach items="${item.dictValueList}" var="value">
                                    <div class="col-md-2">
                                        <h4>${value.label}
                                            <a href="#" onclick="edit('${item.id}','${value.id}')" class="edit" title="修改"><i class="fa fa-edit"></i> </a>
                                            <a href="#" onclick="del('${value.id}');" class="del" title="删除"><i class="fa fa-times"></i> </a>
                                        </h4>
                                    </div>
                                </c:forEach>
                                <div>
                                    <button id="edit" class="btn btn-primary" onclick="add('${item.id}')">
                                        <i class="glyphicon glyphicon-plus"></i> 添加
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
			</div>
		</div>
	</div>
    <!-- 请保留，表单界面使用到了这个name="setParam"-->
    <div id="importBox" name="setParam" class="hide">
    </div>
    <script>

        function add(typeid){

            jp.openDialog('添加参数', '${ctx}/sys/dict/dictValueForm?dictTypeId='+typeid+"&f=1",'800px', '500px',$("#importBox"));
        }

        function del(id){
            //删除
            jp.confirm('确认要删除该参数吗？',function(){
                jp.loading();
                $.get('${ctx}/sys/dict/deleteDictValue?dictValueId='+id, function(data){
                    if(data.success){
                        reload();
                        jp.success("删除该参数成功");
                    }else{
                        jp.error(data.msg);
                    }
                })
            });
        }


        function edit(typeId,id){
            jp.openDialog('修改参数', '${ctx}/sys/dict/dictValueForm?dictTypeId='+typeId+"&dictValueId="+id+"&f=1",'800px', '500px',$("#importBox"));
        }


        //刷新当前页
        function reload(){
            location.reload();
        }

    </script>
</body>
</html>