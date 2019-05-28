<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#coverTable').bootstrapTable({
		 
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
               url: "${ctx}/cv/equinfo/cover/data",
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
                       	jp.get("${ctx}/cv/equinfo/cover/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#coverTable').bootstrapTable('refresh');
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
		        title: '编号',
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
		        field: 'province',
		        title: '省',
		        sortable: true
		       
		    }
			,{
		        field: 'city',
		        title: '市',
		        sortable: true
		       
		    }

			,{
		        field: 'district',
		        title: '区',
		        sortable: true
		       
		    }
			,{
		        field: 'township',
		        title: '街道',
		        sortable: true
		       
		    }
			,{
		        field: 'street',
		        title: '路（街巷）',
		        sortable: true
		       
		    }
			,{
		        field: 'streetNumber',
		        title: '门牌号',
		        sortable: true
		       
		    }

/*			,{
		        field: 'coordinateType',
		        title: '坐标类型',
		        sortable: true
		       
		    }*/
			,{
		        field: 'longitude',
		        title: '经度',
		        sortable: true
		       
		    }
			,{
		        field: 'latitude',
		        title: '纬度',
		        sortable: true
		       
		    }
			,{
		        field: 'altitude',
		        title: '海拔（m）',
		        sortable: true
		       
		    }
			,{
		        field: 'locationAccuracy',
		        title: '定位精度（m）',
		        sortable: true
		       
		    }
			,{
		        field: 'altitudeAccuracy',
		        title: '海拔精度（m）',
		        sortable: true
		       
		    }
			,{
		        field: 'purpose',
		        title: '井位用途',
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
		        field: 'manufacturer',
		        title: '制造商',
		        sortable: true
		       
		    }
			,{
		        field: 'sizeSpec',
		        title: '尺寸规格',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cover_size_spec'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'sizeRule',
		        title: '井盖规格',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cover_size_rule'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'sizeDiameter',
		        title: '直径（mm）',
		        sortable: true
		       
		    }
			,{
		        field: 'sizeRadius',
		        title: '半径（mm）',
		        sortable: true
		       
		    }
			,{
		        field: 'sizeLength',
		        title: '长度（mm）',
		        sortable: true
		       
		    }
			,{
		        field: 'sizeWidth',
		        title: '宽度（mm）',
		        sortable: true
		       
		    }
			,{
		        field: 'material',
		        title: '井盖材质',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cover_material'))}, value, "-");
		        }
		       
		    }
/*			,{
		        field: 'ownerDepart',
		        title: '权属单位',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cover_owner_depart'))}, value, "-");
		        }
		       
		    }*/
			,{
		        field: 'superviseDepart',
		        title: '监管单位',
		        sortable: true
		       
		    }
			,{
		        field: 'marker',
		        title: '地图标记',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cover_damage'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'isDamaged',
		        title: '是否损毁',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('boolean'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'manholeDamageDegree',
		        title: '井筒破损深度（m）',
		        sortable: true
		       
		    }
			,{
		        field: 'damageRemark',
		        title: '损毁情况备注',
		        sortable: true
		       
		    }
/*			,{
		        field: 'altitudeIntercept',
		        title: '高度差',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cover_altitude_intercept'))}, value, "-");
		        }
		       
		    }*/
                   ,{
                       field: 'altitudeIntercept',
                       title: '高度差',
                       sortable: true

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
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#coverTable').bootstrapTable("toggleView");
		}
	  
	  $('#coverTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#coverTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#coverTable').bootstrapTable('getSelections').length!=1);
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
		  $('#coverTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#coverTable').bootstrapTable('refresh');
		});
		
		$('#beginCreateDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endCreateDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		
	});
		
  function getIdSelections() {
        return $.map($("#coverTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该井盖基础信息记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/cv/equinfo/cover/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#coverTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
   function add(){
	  jp.openDialog('新增井盖基础信息', "${ctx}/cv/equinfo/cover/form",'800px', '500px', $('#coverTable'));
  }
  function edit(id){//没有权限时，不显示确定按钮
  	  if(id == undefined){
			id = getIdSelections();
		}
	   <shiro:hasPermission name="cv:equinfo:cover:edit">
	  jp.openDialog('编辑井盖基础信息', "${ctx}/cv/equinfo/cover/form?id=" + id,'800px', '500px', $('#coverTable'));
	   </shiro:hasPermission>
	  <shiro:lacksPermission name="cv:equinfo:cover:edit">
	  jp.openDialogView('查看井盖基础信息', "${ctx}/cv/equinfo/cover/form?id=" + id,'800px', '500px', $('#coverTable'));
	  </shiro:lacksPermission>
  }
function view(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getIdSelections();
    }
        jp.openDialogView('查看井盖基础信息', "${ctx}/cv/equinfo/cover/view?id=" + id,'800px', '500px', $('#coverTable'));

}

</script>