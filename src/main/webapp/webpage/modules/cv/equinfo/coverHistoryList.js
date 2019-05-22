<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#coverHistoryTable').bootstrapTable({
		 
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
               url: "${ctx}/cv/equinfo/coverHistory/data",
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
                        jp.confirm('确认要删除该井盖历史记录记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/cv/equinfo/coverHistory/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#coverHistoryTable').bootstrapTable('refresh');
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
		        field: 'no',
		        title: '编号',
		        sortable: true
                ,formatter:function(value, row , index){
                           if(value == null){
                               return "<a href='javascript:showCover(\""+row.coverId+"\")'>-</a>";
                           }else{
                               return "<a href='javascript:showCover(\""+row.coverId+"\")'>"+value+"</a>";
                           }
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
		        title: '地址：省',
		        sortable: true
		       
		    }
			,{
		        field: 'city',
		        title: '地址：市',
		        sortable: true
		       
		    }
			,{
		        field: 'district',
		        title: '地址：区',
		        sortable: true
		       
		    }
			,{
		        field: 'township',
		        title: '地址：街道（办事处）',
		        sortable: true
		       
		    }
			,{
		        field: 'street',
		        title: '地址：路（街巷）',
		        sortable: true
		       
		    }
			,{
		        field: 'streetNumber',
		        title: '地址：门牌号',
		        sortable: true
		       
		    }
			,{
		        field: 'addressDetail',
		        title: '地址：详细地址',
		        sortable: true
		       
		    }
			,{
		        field: 'coordinateType',
		        title: '坐标类型：gcj02: 国测局坐标系gps: WGS-84',
		        sortable: true
		       
		    }
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
		        field: 'wgs84X',
		        title: 'WGS84坐标系X轴坐标',
		        sortable: true
		       
		    }
			,{
		        field: 'wgs84Y',
		        title: 'WGS84坐标系Y轴坐标',
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
		        title: '尺寸规格D800 : 圆形直径800mmR800x600 : 矩形 H800（长）W600（宽）',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cover_size_spec'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'sizeRule',
		        title: '井盖规格（尺寸类型）',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cover_size_rule'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'sizeDiameter',
		        title: '尺寸：直径（mm）',
		        sortable: true
		       
		    }
			,{
		        field: 'sizeRadius',
		        title: '尺寸：半径（mm）** 已废弃，使用diameter字段 **',
		        sortable: true
		       
		    }
			,{
		        field: 'sizeLength',
		        title: '尺寸：长度（mm）',
		        sortable: true
		       
		    }
			,{
		        field: 'sizeWidth',
		        title: '尺寸：宽度（mm）',
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
			,{
		        field: 'altitudeIntercept',
		        title: '高度差，井中心与周边路面（1.5m范围）',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cover_altitude_intercept'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'coverStatus',
		        title: '井盖状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('cover_status'))}, value, "-");
		        }
		       
		    }
			/*,{
		        field: 'remarks',
		        title: 'remarks',
		        sortable: true
		       
		    }
			,{
		        field: 'source',
		        title: '数据来源',
		        sortable: true
		       
		    }*/
			,{
		        field: 'updateDate',
		        title: '更新时间',
		        sortable: true
		       
		    }
			,{
		        field: 'updateBy.name',
		        title: '更新人',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#coverHistoryTable').bootstrapTable("toggleView");
		}
	  
	  $('#coverHistoryTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#coverHistoryTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#coverHistoryTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/cv/equinfo/coverHistory/import/template';
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
		  $('#coverHistoryTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#coverHistoryTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#coverHistoryTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该井盖历史记录记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/cv/equinfo/coverHistory/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#coverHistoryTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
   function add(){
	  jp.openDialog('新增井盖历史记录', "${ctx}/cv/equinfo/coverHistory/form",'800px', '500px', $('#coverHistoryTable'));
  }
  function edit(id){//没有权限时，不显示确定按钮
  	  if(id == undefined){
			id = getIdSelections();
		}
	   <shiro:hasPermission name="cv:equinfo:coverHistory:edit">
	  jp.openDialog('编辑井盖历史记录', "${ctx}/cv/equinfo/coverHistory/form?id=" + id,'800px', '500px', $('#coverHistoryTable'));
	   </shiro:hasPermission>
	  <shiro:lacksPermission name="cv:equinfo:coverHistory:edit">
	  jp.openDialogView('查看井盖历史记录', "${ctx}/cv/equinfo/coverHistory/form?id=" + id,'800px', '500px', $('#coverHistoryTable'));
	  </shiro:lacksPermission>
  }


function showCover(coverId){//查看井盖信息
    //jp.openDialog('井盖审核信息', "${ctx}/cv/equinfo/coverAudit/auditPage?id=" +coverId,'800px', '500px', $('#coverAuditTable'));
    jp.openDialogView('查看井盖基础信息', "${ctx}/cv/equinfo/cover/view?id=" + coverId,'800px', '500px', $('#coverAuditTable'));
    //jp.openDialogView('查看井盖基础信息', "${ctx}/cv/equinfo/cover/form?id=" + coverId,'800px', '500px', $('#coverAuditTable'));

}

</script>