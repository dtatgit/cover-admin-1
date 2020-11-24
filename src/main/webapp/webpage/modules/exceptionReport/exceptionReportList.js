<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#exceptionReportTable').bootstrapTable({
		 
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
               url: "${ctx}/cb/report/exceptionReport/data",
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
                   if($el.data("item") == "check"){
                   	window.location = "${ctx}/cb/report/exceptionReport/check?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该异常上报记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/cb/report/exceptionReport/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#exceptionReportTable').bootstrapTable('refresh');
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
		        field: 'coverWorkNo',
		        title: '工单号',
		        sortable: true,
		        /*,formatter:function(value, row , index){
		        	return "<a href='${ctx}/cb/report/exceptionReport/view?id="+row.id+"'>"+value+"</a>";
		         }*/
				formatter:function(value, row , index){
					return "<a href='javascript:showView(\""+row.id+"\")'>"+value+"</a>";
				}
		    }
			,{
		        field: 'address',
		        title: '所在区域',
		        sortable: true
		       
		    }
			,{
				field: 'createByName',
				title: '上报人',
				sortable: true

			}
			,{
				field: 'createDate',
				title: '上报时间',
				sortable: true

			}
			,{
		        field: 'checkByName',
		        title: '审核人',
		        sortable: true
		       
		    }
			,{
		        field: 'checkStatus',
		        title: '审核状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('exception_report_status'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#exceptionReportTable').bootstrapTable("toggleView");
		}
	  
	  $('#exceptionReportTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#exceptionReportTable').bootstrapTable('getSelections').length);
            $('#check').prop('disabled', $('#exceptionReportTable').bootstrapTable('getSelections').length!=1);
		  $('#createWorks').prop('disabled', ! $('#exceptionReportTable').bootstrapTable('getSelections').length);
		  $('#edit').prop('disabled', $('#exceptionReportTable').bootstrapTable('getSelections').length!=1);
	  });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/cb/report/exceptionReport/import/template';
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
		  $('#exceptionReportTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#exceptionReportTable').bootstrapTable('refresh');
		});
		
		$('#beginCreateDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endCreateDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});

		$('#beginCheckDate').datetimepicker({
			format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endCheckDate').datetimepicker({
			format: "YYYY-MM-DD HH:mm:ss"
		});
	});
		
  function getIdSelections() {
        return $.map($("#exceptionReportTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  function getCheckStatusSelections() {
	return $.map($("#exceptionReportTable").bootstrapTable('getSelections'), function (row) {
		return row.checkStatus
	});
}

  function deleteAll(){

		jp.confirm('确认要删除该异常上报记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/cb/report/exceptionReport/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#exceptionReportTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

function createWorks(){
	var ids = getIdSelections();
	jp.openDialog('创建工单', "${ctx}/cb/report/exceptionReport/createWorkPage?ids=" + ids,'800px', '500px', $('#exceptionReportTable'));

}
  function check(){
	  var checkStatus = getCheckStatusSelections();
	  if (checkStatus!='0') {
		  alert("不能再次审核！");
		  return;
	  }
	  window.location = "${ctx}/cb/report/exceptionReport/check?id=" + getIdSelections();
  }
function showView(id){//查看详情
	jp.openDialogView('查看异常上报信息', "${ctx}/cb/report/exceptionReport/view?id=" + id,'1000px', '700px', $('#exceptionReportTable'));
}

function edit() {
	window.location = "${ctx}/cb/report/exceptionReport/form?id=" + getIdSelections();
}
</script>