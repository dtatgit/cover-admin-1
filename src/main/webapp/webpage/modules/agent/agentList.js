<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#projectInfoTable').bootstrapTable({
		 
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
               url: "${ctx}/project/projectInfo/data?t=agent",
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
                   	window.location = "${ctx}/project/projectInfo/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该项目管理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/project/projectInfo/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#projectInfoTable').bootstrapTable('refresh');
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
				   field: 'loginName',
				   title: '账号',
				   sortable: true
			   }
			// ,{
		    //     field: 'projectNo',
		    //     title: '代理商编号',
		    //     sortable: true
		    //     ,formatter:function(value, row , index){
			// 		return "<a href='javascript:showView(\""+row.id+"\")'>"+value+"</a>";
		    //      }
		    //
		    // }
			,{
		        field: 'projectName',
		        title: '代理商简称',
		        sortable: true
		       
		    }
		   ,{
			   field: 'projectAllName',
			   title: '代理商全称',
			   sortable: true

		   }
		   ,{
			   field: 'createDate',
			   title: '开通时间',
			   sortable: true

		   }
		   ,{
			   field: 'customer',
			   title: '联系人',
			   sortable: true

		   }
		   ,{
			   field: 'customerPhone',
			   title: '联系人电话',
			   sortable: true

		   }
		   ,{
			   field: 'business',
			   title: '业务负责人',
			   sortable: true

		   }
		   ,{
			   field: 'businessPhone',
			   title: '负责人电话',
			   sortable: true

		   }
		   ,{
			   field: 'contractNo',
			   title: '合同编号',
			   sortable: true

		   }

            // ,{
            //            field: 'version',
            //            title: '平台版本',
            //            sortable: true,
            //            formatter:function(value, row , index){
            //                return jp.getDictLabel(${fns:toJson(fns:getDictList('version_type'))}, value, "-");
            //            }
            // }
			// ,{
			// 	field: 'office.name',
			// 	title: '部门',
			// 	sortable: true
		    //
		    // }
			,{
		        field: 'status',
		        title: '账号状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('on_off'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'remarks',
		        title: '备注',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#projectInfoTable').bootstrapTable("toggleView");
		}
	  
	  $('#projectInfoTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#projectInfoTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#projectInfoTable').bootstrapTable('getSelections').length!=1);
          	$('#enable').prop('disabled', ! $('#projectInfoTable').bootstrapTable('getSelections').length);
          	$('#disable').prop('disabled', ! $('#projectInfoTable').bootstrapTable('getSelections').length);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/project/projectInfo/import/template';
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
		  $('#projectInfoTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#projectInfoTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#projectInfoTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该项目管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/project/projectInfo/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#projectInfoTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
	function add(){
		jp.openDialog('创建代理商', '${ctx}/project/projectInfo/agentForm','1000px', '550px', $('#projectInfoTable'))
	}
  function edit(){
	  jp.openDialog('编辑代理商', "${ctx}/project/projectInfo/agentForm?id=" + getIdSelections(),'1000px', '550px', $('#projectInfoTable'))
	  //window.location = "${ctx}/project/projectInfo/form?id=" + getIdSelections();
  }

function showView(id){//查看详情
	jp.openDialogView('查看代理商', "${ctx}/project/projectInfo/agentView?id=" + id,'1000px', '550px', $('#projectInfoTable'));
}


function disableAll(){

    jp.confirm('确认要禁用吗？', function(){
        jp.loading();
        jp.get("${ctx}/project/projectInfo/disableAll?ids=" + getIdSelections(), function(data){
            if(data.success){
                $('#projectInfoTable').bootstrapTable('refresh');
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })

    })
}

function enableAll(){

    jp.confirm('确认要启用吗？', function(){
        jp.loading();
        jp.get("${ctx}/project/projectInfo/enableAll?ids=" + getIdSelections(), function(data){
            if(data.success){
                $('#projectInfoTable').bootstrapTable('refresh');
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })

    })
}

</script>