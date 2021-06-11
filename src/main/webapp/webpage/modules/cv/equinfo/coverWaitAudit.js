<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#coverAuditTable').bootstrapTable({
		 
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
               url: "${ctx}/cv/equinfo/coverWaitAudit/data",
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
                        jp.confirm('确认要删除该井盖基础信息记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/cv/equinfo/coverWaitAudit/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#coverAuditTable').bootstrapTable('refresh');
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
		        field: 'coverStatus',
		        title: '状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return "<a href='javascript:view(\""+row.id+"\")'>"+jp.getDictLabel(${fns:toJson(fns:getDictList('cover_status'))}, value, "-")+"</a>";
		        }
		       
		    }
			,{
		        field: 'no',
		        title: '井盖编号',
		        sortable: true,
                       formatter:function(value, row , index){
                           return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
                       }
		       
		    }
			,{
		        field: 'coverType',
		        title: '井盖类型',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cover_type'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'purpose',
		        title: '窨井用途',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cover_purpose'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'situation',
		        title: '井位地理场合',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cover_situation'))}, value, "-");
		        }
		       
		    }

			,{
		        field: 'material',
		        title: '井盖材质',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cover_material'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'createBy.name',
		        title: '采集人',
		        sortable: true
		       
		    }
			,{
		        field: 'createDate',
		        title: '采集时间',
		        sortable: true
		       
		     },
                   //{
             //     title: '操作',
             //     formatter:function(value, row , index){
             //               return '<button id="audit" class="btn btn-success" style="background-color: red" onclick="auditPage(\'' + row.id + '\')"><i class="glyphicon glyphicon-audit"></i> 审核 </button>'
				//  }
            //
             //      },
				   {
                       field: 'operate',
                       title: '操作',
                       align: 'center',
                       events: {
                           'click .audit': function (e, value, row, index) {
                               jp.openDialog('井盖审核信息', "${ctx}/cv/equinfo/coverAudit/auditPage2?id=" + row.id,'1200px', '820px', $('#coverAuditTable'));
                           }
                       },
                       formatter:  function operateFormatter(value, row, index) {
                           return [
                           <shiro:hasPermission name="cv:equinfo:coverAudit:audit">
                               '<a href="#" class="audit"  title="审核">审核 </a>'
                               </shiro:hasPermission>
                       ].join('');
                       }
                   }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#coverAuditTable').bootstrapTable("toggleView");
		}
	  
	  $('#coverAuditTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#coverAuditTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#coverAuditTable').bootstrapTable('getSelections').length!=1);
          	$('#batchPass').prop('disabled', ! $('#coverAuditTable').bootstrapTable('getSelections').length);
          	$('#batchReject').prop('disabled', ! $('#coverAuditTable').bootstrapTable('getSelections').length);
            $('#bell').prop('disabled', $('#coverAuditTable').bootstrapTable('getSelections').length!=1);
            $('#alarm').prop('disabled', $('#coverAuditTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/cv/equinfo/cover/import/template';
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
		  $('#coverAuditTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#coverAuditTable').bootstrapTable('refresh');
		});
		
		$('#beginCreateDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endCreateDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		
	});
		
  function getIdSelections() {
        return $.map($("#coverAuditTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该井盖基础信息记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/cv/equinfo/cover/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#coverAuditTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
   function add(){
	  jp.openDialog('新增井盖基础信息', "${ctx}/cv/equinfo/cover/form",'800px', '500px', $('#coverAuditTable'));
  }
  function edit(id){//没有权限时，不显示确定按钮
  	  if(id == undefined){
			id = getIdSelections();
		}
	   <shiro:hasPermission name="cv:equinfo:cover:edit">
	  jp.openDialog('编辑井盖基础信息', "${ctx}/cv/equinfo/cover/form?id=" + id,'800px', '500px', $('#coverAuditTable'));
	   </shiro:hasPermission>
	  <shiro:lacksPermission name="cv:equinfo:cover:edit">
	  jp.openDialogView('查看井盖基础信息', "${ctx}/cv/equinfo/cover/form?id=" + id,'800px', '500px', $('#coverAuditTable'));
	  </shiro:lacksPermission>
  }
function view(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getIdSelections();
    }
        jp.openDialogView('查看井盖基础信息', "${ctx}/cv/equinfo/cover/view?id=" + id,'800px', '500px', $('#coverAuditTable'));

}

function getCoverNoSelections() {
    return $.map($("#coverAuditTable").bootstrapTable('getSelections'), function (row) {
        return row.no
    });
}

function getIsGwoSelections() {
    return $.map($("#coverAuditTable").bootstrapTable('getSelections'), function (row) {
        return row.isGwo
    });
}

function batchPass(ids,coverNos){
    jp.confirm('确认要批量通过吗？', function(){
        jp.loading();
        jp.get("${ctx}/cv/equinfo/coverAudit/batchPass?ids=" + getIdSelections(), function(data){
            if(data.success){
                $('#coverAuditTable').bootstrapTable('refresh');
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })

    })

}
function batchReject(){
    jp.confirm('确认要批量驳回吗？', function(){
        jp.loading();
        jp.get("${ctx}/cv/equinfo/coverAudit/batchReject?ids=" + getIdSelections(), function(data){
            if(data.success){
                $('#coverAuditTable').bootstrapTable('refresh');
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })

    })

}

function auditPage(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getIdSelections();
    }
<shiro:hasPermission name="cv:equinfo:coverAudit:audit">
        jp.openDialog('井盖审核信息', "${ctx}/cv/equinfo/coverAudit/auditPage2?id=" + id,'1200px', '820px', $('#coverAuditTable'));
</shiro:hasPermission>
    <shiro:lacksPermission name="cv:equinfo:coverAudit:audit">
        jp.openDialogView('查看井盖审核信息', "${ctx}/cv/equinfo/coverAudit/auditPage2?id=" + id,'1200px', '820px', $('#coverAuditTable'));
</shiro:lacksPermission>
}
function bellInfo(id){//井卫信息
    if(id == undefined){
        id = getIdSelections();
    }
<shiro:hasPermission name="cv:equinfo:cover:bell">
        jp.openDialogView('查看井卫信息', "${ctx}/cv/equinfo/cover/belllist?id=" + id,'800px', '500px', $('#coverBellTable'));
</shiro:hasPermission>
}

function alarmInfo(id){//报警信息
    if(id == undefined){
        id = getIdSelections();
    }
<shiro:hasPermission name="cv:equinfo:cover:alarm">
        jp.openDialogView('查看报警记录', "${ctx}/cv/equinfo/cover/alarmlist?id=" + id,'800px', '500px', $('#coverBellTable'));
</shiro:hasPermission>
}

function exportAll(){//后台导出
    			$("#searchForm").submit();
 }





</script>