<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#coverWorkTable').bootstrapTable({
		 
		  //请求方法
               method: 'get',
               //类型json
               dataType: "json",
               //显示刷新按钮
               showRefresh: true,
               //显示切换手机试图按钮
               showToggle: true,
               //显示 内容列下拉框
    	       showColumns: true,
    	       //显示到处按钮
    	       showExport: false,
    	       //显示切换分页按钮
    	       showPaginationSwitch: true,
    	       //最低显示2行
    	       minimumCountColumns: 2,
               //是否显示行间隔色
               striped: true,
               //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）     
               cache: false,    
               //是否显示分页（*）  
               pagination: true,   
                //排序方式 
               sortOrder: "asc",  
               //初始化加载第一页，默认第一页
               pageNumber:1,   
               //每页的记录行数（*）   
               pageSize: 10,  
               //可供选择的每页的行数（*）    
               pageList: [10, 25, 50, 100],
               //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
               url: "${ctx}/cb/work/coverWork/data",
               //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
               //queryParamsType:'',   
               ////查询参数,每次调用是会带上这个参数，可自定义                         
               queryParams : function(params) {
               	var searchParam = $("#searchForm").serializeJSON();
               	searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
               	searchParam.pageSize = params.limit === undefined? -1 : params.limit;
               	searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
                   return searchParam;
               },
               //分页方式：client客户端分页，server服务端分页（*）
               sidePagination: "server",
               contextMenuTrigger:"right",//pc端 按右键弹出菜单
               contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
               contextMenu: '#context-menu',
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   	edit(row.id);
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该工单信息记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/cb/work/coverWork/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#coverWorkTable').bootstrapTable('refresh');
                   	  			jp.success(data.msg);
                   	  		}else{
                   	  			jp.error(data.msg);
                   	  		}
                   	  	})
                   	   
                   	});
                      
                   } 
               },
              
               onClickRow: function(row, $el){
               },
               columns: [{
		        checkbox: true
		       
		    }
			,{
                field: 'workNum',
                title: '工单编号',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='javascript:workDetail(\""+row.id+"\")'>"+value+"</a>";
		         }
		       
		    }
			/*,{
				field: 'projectName',
				title: '项目名称',
				sortable: true
			}*/
			,{
                field: 'coverNo',
				title: '井盖编号',
		        sortable: true  ,
					   formatter:function(value, row , index){
                           if(value == null){
                               return "<a href='javascript:showCover(\""+row.cover.id+"\")'>-</a>";
                           }else{
                               return "<a href='javascript:showCover(\""+row.cover.id+"\")'>"+value+"</a>";
                           }
                       }
		       
		    }/*,{
                       field: 'flowNo',
                       title: '流程编号',
                       sortable: true  ,
                       formatter:function(value, row , index){
                           if(value == null){
                               return "<a href='javascript:showFlow(\""+row.flowProId+"\")'>-</a>";
                           }else{
                               return "<a href='javascript:showFlow(\""+row.flowProId+"\")'>"+value+"</a>";
                           }
                       }

                   }*/
			,{
		        field: 'workType',
		        title: '工单类型',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('work_type'))}, value, "-");
		        }
		       
		    }
                 /*  ,{
                       field: 'workStatus',
                       title: '工单状态',
                       sortable: true
                   }*/
/*			,{
		        field: 'workStatus',
		        title: '工单状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('work_status'))}, value, "-");
		        }
		       
		    }*/
				   ,{
					   field: 'createDate',
					   title: '创建日期',
					   sortable: true

				   }
                  /* ,{
                       field: 'lifeCycle',
                       title: '生命周期',
                       sortable: true,
                       formatter:function(value, row , index){
                           return jp.getDictLabel(${fns:toJson(fns:getDictList('lifecycle'))}, value, "-");
                       }

                   }
			,{
		        field: 'constructionContent',
		        title: '施工内容',
		        sortable: true
		       
		    }
			,{
		        field: 'constructionUser.name',
		        title: '施工人员',
		        sortable: true
		       
		    }
			,{
		        field: 'phone',
		        title: '联系电话',
		        sortable: true
		       
		    }
			,{
		        field: 'constructionDepart.name',
		        title: '施工部门',
		        sortable: true
		       
		    }*/
			,{
		        field: 'workLevel',
		        title: '紧急程度',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('work_level'))}, value, "-");
		        }
		       
		    }
				   ,{
					   field: 'constructionUser.name',
					   title: '处理人员',
					   sortable: true

				   }

				   ,{
					   field: 'workStatus',
					   title: '工单状态',
					   sortable: true
				   }

		    /* ,{
                       field: 'projectName',
                       title: '所属项目',
                       sortable: true
             }
             ,{
                       field: 'createBy.name',
                       title: '创建人员',
                       sortable: true

             }
			,{
		        field: 'createDepart',
		        title: '创建部门',
		        sortable: true
		       
		    }*/
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#coverWorkTable').bootstrapTable("toggleView");
		}
	  
	  $('#coverWorkTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#coverWorkTable').bootstrapTable('getSelections').length);
            $('#assign').prop('disabled', ! $('#coverWorkTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#coverWorkTable').bootstrapTable('getSelections').length!=1);
            $('#workOperation').prop('disabled', $('#coverWorkTable').bootstrapTable('getSelections').length!=1);
          	$('#audit').prop('disabled', $('#coverWorkTable').bootstrapTable('getSelections').length!=1);
		  	$('#discard').prop('disabled', $('#coverWorkTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/cb/work/coverWork/import/template';
				  },
			    btn2: function(index, layero){
				        var inputForm =top.$("#importForm");
				        var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				        inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				        inputForm.onsubmit = function(){
				        	jp.loading('  正在导入，请稍等...');
				        }
				        inputForm.submit();
					    jp.close(index);
				  },
				 
				  btn3: function(index){ 
					  jp.close(index);
	    	       }
			}); 
		});
		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#coverWorkTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#coverWorkTable').bootstrapTable('refresh');
		});

	$('#beginDate').datetimepicker({
		format: "YYYY-MM-DD HH:mm:ss"
	});
	$('#endDate').datetimepicker({
		format: "YYYY-MM-DD HH:mm:ss"
	});
		
	});
		
  function getIdSelections() {
        return $.map($("#coverWorkTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该工单信息记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/cb/work/coverWork/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#coverWorkTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
   function add(){
	  jp.openDialog('新增工单信息', "${ctx}/cb/work/coverWork/form",'800px', '500px', $('#coverWorkTable'));
  }
  function edit(id){//没有权限时，不显示确定按钮
  	  if(id == undefined){
			id = getIdSelections();
		}
	   <shiro:hasPermission name="cb:work:coverWork:edit">
	  jp.openDialog('编辑工单信息', "${ctx}/cb/work/coverWork/edit?id=" + id,'800px', '500px', $('#coverWorkTable'));
	   </shiro:hasPermission>
	  <shiro:lacksPermission name="cb:work:coverWork:edit">
	  jp.openDialogView('查看工单信息', "${ctx}/cb/work/coverWork/form?id=" + id,'800px', '500px', $('#coverWorkTable'));
	  </shiro:lacksPermission>
  }

function showCover(coverId){//查看井盖信息
    jp.openDialogView('查看井盖基础信息', "${ctx}/cv/equinfo/cover/view?id=" + coverId,'800px', '500px', $('#coverWorkTable'));
}
function showFlow(flowId){//查看流程信息
    jp.openDialogView('查看流程信息', "${ctx}/flow/base/flowProc/form?id=" + flowId,'800px', '500px', $('#coverWorkTable'));
}


function getworkNumsSelections() {
    return $.map($("#coverWorkTable").bootstrapTable('getSelections'), function (row) {
        return row.workNum
    });
}

function getWorkStatusSelections() {
    return $.map($("#coverWorkTable").bootstrapTable('getSelections'), function (row) {
        return row.workStatus
    });
}
function getWorkFlowIdSelections() {
    return $.map($("#coverWorkTable").bootstrapTable('getSelections'), function (row) {
        return row.flowId.id
    });
}

function workAssign(ids,workNums){

    if(ids == undefined){
        ids = getIdSelections();
    }
    if(workNums == undefined){
        workNums = getworkNumsSelections();
    }
    var workStatus=getWorkStatusSelections();

    if(workStatus.indexOf("scrap") != -1||workStatus.indexOf("processing") != -1||workStatus.indexOf("process_complete") != -1||workStatus.indexOf("process_fail") != -1||workStatus.indexOf("complete") != -1){

        jp.alert("该状态下的工单不允许被指派！");
    }else{
		<shiro:hasPermission name="cb:work:coverWork:assign">
				jp.openDialog('工单派单', "${ctx}/cb/work/coverWork/toWorkAssign?ids=" + ids +"&workNums="+workNums,'800px', '500px', $('#coverWorkTable'));
		</shiro:hasPermission>
    }


}

function workOperation(id){//工单操作记录
    if(id == undefined){
        id = getIdSelections();
    }
<shiro:hasPermission name="cb:work:coverWork:workOperationList">
        jp.openDialogView('工单操作记录', "${ctx}/cb/work/coverWork/workOperationList?id=" + id,'800px', '500px', $('#coverWorkTable'));
</shiro:hasPermission>
}


function workDetail(id){//工单操作记录
    if(id == undefined){
        id = getIdSelections();
    }
<shiro:hasPermission name="cb:work:coverWork:view">
        jp.openDialogView('工单详情记录', "${ctx}/cb/work/coverWork/workDetail?id=" + id,'1200px', '820px', $('#coverWorkTable'));
</shiro:hasPermission>
}

/*function auditPage(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getIdSelections();
    }
    var workStatus=getWorkStatusSelections();

    if(workStatus.indexOf("init") != -1||workStatus.indexOf("assign") != -1||workStatus.indexOf("processing") != -1||workStatus.indexOf("complete") != -1||workStatus.indexOf("scrap") != -1){

        jp.alert("只有状态为：处理完成和处理失败的工单才能进行审核！");
    }else{
    <shiro:hasPermission name="cb:work:coverWork:audit">
            jp.openDialog('工单审核信息', "${ctx}/cb/work/coverWork/auditPage?id=" + id,'1200px', '820px', $('#coverWorkTable'));
    </shiro:hasPermission>
    }



}*/

function auditPage(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getIdSelections();
    }
    var workStatus=getWorkStatusSelections();
    var workFlowId=getWorkFlowIdSelections();
   $.ajax({
        url: "${ctx}/flow/opt/flowOpt/ajaxFlowByOpt",
        type: "POST",
        dataType: "json",
        data: {
            "workFlowId": workFlowId
        },
        async: false,
        success: function(data) {
			var fromState=null;
            $.each(data, function(index, element) {
                fromState= element.fromState

            });

            if(fromState==workStatus){
            <shiro:hasPermission name="cb:work:coverWork:audit">
                    jp.openDialog('工单审核信息', "${ctx}/cb/work/coverWork/auditPage?id=" + id,'1200px', '820px', $('#coverWorkTable'));
            </shiro:hasPermission>

            }else{
                jp.alert("当前工单状态无需进行审核！");

            }
        },
        error: function() {
            alert("error");
        }
    });

/*    if(workStatus.indexOf("init") != -1||workStatus.indexOf("assign") != -1||workStatus.indexOf("processing") != -1||workStatus.indexOf("complete") != -1||workStatus.indexOf("scrap") != -1){

        jp.alert("只有状态为：处理完成和处理失败的工单才能进行审核！");
    }else{
    <shiro:hasPermission name="cb:work:coverWork:audit">
            jp.openDialog('工单审核信息', "${ctx}/cb/work/coverWork/auditPage?id=" + id,'1200px', '820px', $('#coverWorkTable'));
    </shiro:hasPermission>
    }*/

function discard() {
	return;
}
}

</script>