<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#coverBellTable').bootstrapTable({
		 
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
               url: "${ctx}/cb/equinfo/coverBell/data",
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
                        jp.confirm('确认要删除该井铃设备信息记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/cb/equinfo/coverBell/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#coverBellTable').bootstrapTable('refresh');
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
		        field: 'bellNo',
		        title: '井铃编号',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'coverNo',
		        title: '井盖编号',
		        sortable: true ,
                       formatter:function(value, row , index){
                           if(value == null){
                               return "<a href='javascript:showCover(\""+row.coverId+"\")'>-</a>";
                           }else{
                               return "<a href='javascript:showCover(\""+row.coverId+"\")'>"+value+"</a>";
                           }
                       }
		       
		    }
			,{
		        field: 'bellModel',
		        title: '井铃型号',
		        sortable: true
		       
		    }
			,{
		        field: 'imei',
		        title: '设备IMEI号',
		        sortable: true
		       
		    }
			,{
		        field: 'sim',
		        title: '设备SIM卡号',
		        sortable: true
		       
		    }
			,{
		        field: 'bellStatus',
		        title: '生命周期',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('bell_status'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'workStatus',
		        title: '工作状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('bell_work_status'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'defenseStatus',
		        title: '设防状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('defense_status'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'createDate',
		        title: '创建时间',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#coverBellTable').bootstrapTable("toggleView");
		}
	  
	  $('#coverBellTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#coverBellTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#coverBellTable').bootstrapTable('getSelections').length!=1);
            $('#alarm').prop('disabled', $('#coverBellTable').bootstrapTable('getSelections').length!=1);
            $('#operation').prop('disabled', $('#coverBellTable').bootstrapTable('getSelections').length!=1);
            $('#bellState').prop('disabled', $('#coverBellTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/cb/equinfo/coverBell/import/template';
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
		  $('#coverBellTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#coverBellTable').bootstrapTable('refresh');
		});
		
		$('#beginCreateDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endCreateDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		
	});
		
  function getIdSelections() {
        return $.map($("#coverBellTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该井铃设备信息记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/cb/equinfo/coverBell/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#coverBellTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
   function add(){
	  jp.openDialog('新增井铃设备信息', "${ctx}/cb/equinfo/coverBell/form",'800px', '500px', $('#coverBellTable'));
  }
  function edit(id){//没有权限时，不显示确定按钮
  	  if(id == undefined){
			id = getIdSelections();
		}
	   <shiro:hasPermission name="cb:equinfo:coverBell:edit">
	  jp.openDialog('编辑井铃设备信息', "${ctx}/cb/equinfo/coverBell/form?id=" + id,'800px', '500px', $('#coverBellTable'));
	   </shiro:hasPermission>
	  <shiro:lacksPermission name="cb:equinfo:coverBell:view">
	  jp.openDialogView('查看井铃设备信息', "${ctx}/cb/equinfo/coverBell/form?id=" + id,'800px', '500px', $('#coverBellTable'));
	  </shiro:lacksPermission>
  }

function view(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getIdSelections();
    }

    <shiro:hasPermission name="cb:equinfo:coverBell:view">
        jp.openDialogView('查看井铃设备信息', "${ctx}/cb/equinfo/coverBell/form?id=" + id,'800px', '500px', $('#coverBellTable'));
	</shiro:hasPermission>
}

function showCover(coverId){//查看井盖信息
    jp.openDialogView('查看井盖基础信息', "${ctx}/cv/equinfo/cover/view?id=" + coverId,'800px', '500px', $('#coverBellAlarmTable'));
}

/*function alarmInfo(id){//报警记录
    if(id == undefined){
        id = getIdSelections();
    }
<shiro:hasPermission name="cb:alarm:coverBellAlarm:list">
        jp.openDialogView('查看报警记录', "${ctx}/cb/alarm/coverBellAlarm/alarmlist?id=" + id,'800px', '500px', $('#coverBellTable'));
</shiro:hasPermission>
}*/
function alarmInfo(id){//井铃报警记录
    if(id == undefined){
        id = getIdSelections();
    }
<shiro:hasPermission name="cb:equinfo:coverBell:alarmlist">
        jp.openDialogView('查看报警记录', "${ctx}/cb/equinfo/coverBell/alarmlist?id=" + id,'800px', '500px', $('#coverBellTable'));
</shiro:hasPermission>
}

function operationInfo(id){//井铃操作记录
    if(id == undefined){
        id = getIdSelections();
    }
<shiro:hasPermission name="cb:equinfo:coverBell:operationList">
        jp.openDialogView('查看操作记录', "${ctx}/cb/equinfo/coverBell/operationList?id=" + id,'800px', '500px', $('#coverBellTable'));
</shiro:hasPermission>
}

function bellStateInfo(id){//井铃状态上报历史数据
    if(id == undefined){
        id = getIdSelections();
    }
<shiro:hasPermission name="cb:equinfo:coverBell:bellStateList">
        jp.openDialogView('井铃状态历史数据', "${ctx}/cb/equinfo/coverBell/bellStateList?id=" + id,'800px', '500px', $('#coverBellTable'));
</shiro:hasPermission>
}

</script>