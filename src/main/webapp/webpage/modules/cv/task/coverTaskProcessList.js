<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#coverTaskProcessTable').bootstrapTable({
		 
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
    	       showExport: true,
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
               url: "${ctx}/cv/task/coverTaskProcess/data",
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
                        jp.confirm('确认要删除该任务处理明细记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/cv/task/coverTaskProcess/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#coverTaskProcessTable').bootstrapTable('refresh');
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
		        field: 'coverTaskInfo.taskNo',
		        title: '所属任务',
		        sortable: true
		       
		    }
			,{
		        field: 'taskStatus',
		        title: '任务状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('task_status'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'cover.no',
		        title: '井盖信息',
		        sortable: true
		       
		    }
			,{
		        field: 'auditTime',
		        title: '审核时间',
		        sortable: true
		       
		    }
			,{
		        field: 'auditUser.name',
		        title: '审核人',
		        sortable: true
		       
		    }
/*			,{
		        field: 'auditStatus',
		        title: '审核状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('audit_status'))}, value, "-");
		        }
		       
		    }*/
		/*	,{
		        field: 'auditResult',
		        title: '审核结果',
		        sortable: true
		       
		    }*/
/*			,{
		        field: 'applyItem',
		        title: '申请事项',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('apply_item'))}, value, "-");
		        }*/
                   ,{
                       field: 'createDate',
                       title: '创建时间',
                       sortable: true


                   }
		       
		   // }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#coverTaskProcessTable').bootstrapTable("toggleView");
		}
	  
	  $('#coverTaskProcessTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#coverTaskProcessTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#coverTaskProcessTable').bootstrapTable('getSelections').length!=1);
            $('#audit').prop('disabled', $('#coverTaskProcessTable').bootstrapTable('getSelections').length!=1);
            $('#assignOwner').prop('disabled', $('#coverTaskProcessTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/cv/task/coverTaskProcess/import/template';
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
		  $('#coverTaskProcessTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#coverTaskProcessTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#coverTaskProcessTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该任务处理明细记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/cv/task/coverTaskProcess/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#coverTaskProcessTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
   function add(){
	  jp.openDialog('新增任务处理明细', "${ctx}/cv/task/coverTaskProcess/form",'800px', '500px', $('#coverTaskProcessTable'));
  }
  function edit(id){//没有权限时，不显示确定按钮
  	  if(id == undefined){
			id = getIdSelections();
		}
	   <shiro:hasPermission name="cv:task:coverTaskProcess:edit">
	  jp.openDialog('编辑任务处理明细', "${ctx}/cv/task/coverTaskProcess/form?id=" + id,'800px', '500px', $('#coverTaskProcessTable'));
	   </shiro:hasPermission>
	  <shiro:lacksPermission name="cv:task:coverTaskProcess:edit">
	  jp.openDialogView('查看任务处理明细', "${ctx}/cv/task/coverTaskProcess/form?id=" + id,'800px', '500px', $('#coverTaskProcessTable'));
	  </shiro:lacksPermission>
  }

function obtainCoverPage(){
    var  hiddenFlag=  $("#hiddenFlag").val();
    if(hiddenFlag==0){
        $("#obtainDiv").show();
        $("#hiddenFlag").val(1);
    }else{
        $("#obtainDiv").hide();
        $("#hiddenFlag").val(0);
    }
    //jp.openDialogView('获取待审核井盖信息', "${ctx}/cv/equinfo/coverAudit/obtainCoverPage",'800px', '500px', $('#coverAuditTable'));
}


function obtainCover(){
  	//必须选择任务
    var  task=  $("#coverTaskInfoName").val();
    if(task==""){
    	alert("请选择一条任务！");
	}else{
        jp.loading();
        jp.post("${ctx}/cv/task/coverTaskProcess/obtainCover",$('#obtainForm').serialize(),function(data){
            if(data.success){

                jp.success("获取任务成功！");
                // alert(data.msg);
                //打开审核界面
                // $('#coverAuditTable').bootstrapTable('refresh');
                jp.openDialog('井盖任务处理', "${ctx}/cv/task/coverTaskProcess/auditPage?id=" + data.msg,'1200px', '820px', $('#coverTaskProcessTable'));
            }else{
                jp.error(data.msg);
            }
        })

    }


}

function auditPage(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getIdSelections();
    }
<shiro:hasPermission name="cv:task:coverTaskProcess:audit">
        jp.openDialog('井盖审核信息', "${ctx}/cv/task/coverTaskProcess/auditPage?id=" + id,'1200px', '820px', $('#coverTaskProcessTable'));
</shiro:hasPermission>
    <shiro:lacksPermission name="cv:task:coverTaskProcess:audit">
        jp.openDialogView('查看井盖审核信息', "${ctx}/cv/task/coverTaskProcess/auditPage?id=" + id,'1200px', '820px', $('#coverTaskProcessTable'));
</shiro:lacksPermission>
}


function assignOwnerPage(id){//没有权限时，不显示按钮
    if(id == undefined){
        id = getIdSelections();
    }
<shiro:hasPermission name="cv:task:coverTaskProcess:assignOwner">
        jp.openDialog('归属权限单位', "${ctx}/cv/task/coverTaskProcess/assignOwnerPage?id=" + id,'1200px', '820px', $('#coverTaskProcessTable'));
</shiro:hasPermission>
    <shiro:lacksPermission name="cv:task:coverTaskProcess:assignOwner">
        jp.openDialogView('归属权限单位', "${ctx}/cv/task/coverTaskProcess/assignOwnerPage?id=" + id,'1200px', '820px', $('#coverTaskProcessTable'));
</shiro:lacksPermission>
}

function obtainAssignOwnerPage(){
    var  hiddenFlag=  $("#hiddenFlagOwner").val();
    if(hiddenFlag==0){
        $("#obtainDivOwner").show();
        $("#hiddenFlagOwner").val(1);
    }else{
        $("#obtainDivOwner").hide();
        $("#hiddenFlagOwner").val(0);
    }
}

function obtainOwnerCover(){
    jp.loading();
    jp.post("${ctx}/cv/task/coverTaskProcess/obtainAssignOwnerPage",$('#obtainOwnerForm').serialize(),function(data){
        if(data.success){

            jp.success("获取任务成功！");
            // alert(data.msg);
            //打开审核界面
            // $('#coverAuditTable').bootstrapTable('refresh');
            jp.openDialog('归属权限单位', "${ctx}/cv/task/coverTaskProcess/assignOwnerPage?id=" + data.msg,'1200px', '820px', $('#coverTaskProcessTable'));
        }else{
            jp.error(data.msg);
        }
    })
}

</script>