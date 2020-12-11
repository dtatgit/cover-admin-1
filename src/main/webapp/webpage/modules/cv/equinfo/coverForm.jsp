<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>井盖基础信息管理</title>
    <meta name="decorator" content="ani"/>
    <link href="${ctxStatic}/common/fonts/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <script src="http://webapi.amap.com/maps?v=1.4.6&key=06de357afd269944d97de0abcde0f4e0"></script>
    <!-- Bootstrap -->
    <link href="https://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="${ctxStatic}/plugin/imagesPlug/jquery.magnify.js"></script>
    <link href="${ctxStatic}/plugin/imagesPlug/jquery.magnify.css" rel="stylesheet">
    <script src="${ctxStatic}/plugin/jquery-validation\1.14.0/jquery.validate.js"></script>
    <script>
        $('[data-magnify]').magnify({
            headToolbar: [
                'minimize',
                'maximize',
                'close'
            ],
            footToolbar: [
                //'prev',
                //'next',
                'zoomIn',
                'zoomOut',
                //'fullscreen',
                //'actualSize',
                'rotateLeft',
                'rotateRight'
            ],
            modalWidth: 400,
            modalHeight: 400
        });

    </script>
    <script type="text/javascript">
        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            if(validateForm.form()){
                $table = table;
                $topIndex = index;
                jp.loading();
                $("#inputForm").submit();
                return true;
            }

            return false;
        }

        $(document).ready(function() {
            validateForm = $("#inputForm").validate({
                submitHandler: function(form){
                    jp.post("${ctx}/cv/equinfo/cover/repairSave",$('#inputForm').serialize(),function(data){
                        if(data.success){
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog

                        }else{
                            jp.error(data.msg);
                        }
                    })
                },
                errorContainer: "#messageBox",
                errorPlacement: function(error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });

        });

        function addRow(list, idx, tpl, row){
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list+idx).find("select").each(function(){
                $(this).val($(this).attr("data-value"));
            });
            $(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
                var ss = $(this).attr("data-value").split(',');
                for (var i=0; i<ss.length; i++){
                    if($(this).val() == ss[i]){
                        $(this).attr("checked","checked");
                    }
                }
            });
            $(list+idx).find(".form_datetime").each(function(){
                $(this).datetimepicker({
                    format: "YYYY-MM-DD HH:mm:ss"
                });
            });
        }
        function delRow(obj, prefix){
            var id = $(prefix+"_id");
            var delFlag = $(prefix+"_delFlag");
            if (id.val() == ""){
                $(obj).parent().parent().remove();
            }else if(delFlag.val() == "0"){
                delFlag.val("1");
                $(obj).html("&divide;").attr("title", "撤销删除");
                $(obj).parent().parent().addClass("error");
            }else if(delFlag.val() == "1"){
                delFlag.val("0");
                $(obj).html("&times;").attr("title", "删除");
                $(obj).parent().parent().removeClass("error");
            }
        }
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="cover" action="${ctx}/cv/equinfo/cover/repairSave" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <input type="hidden" id="longId" value="${cover.longitude}"/>
    <input type="hidden" id="latId" value="${cover.latitude}"/>
    <input type="hidden" id="showFlag" value="${cover.isDamaged}"/>
    <sys:message content="${message}"/>
<div class="examinebox examinebox1 examinebox-s2">
    <div class="map">
            <%--放地图--%>
        <div id="container" style="height: 200px;position: relative;top:10px; margin:0 2%;width: 96%"></div>
        <script type="text/javascript">

            var map = new AMap.Map('container', {
                resizeEnable: true,
                //zoom:14,//级别
            });
            // map.setCity('徐州');

            var m1 = new AMap.Icon({
                image: '${ctxStatic}/common/images/cover.png',  // Icon的图像
                size: new AMap.Size(38, 63),    // 原图标尺寸
                imageSize: new AMap.Size(19,33) //实际使用的大小
            });

            var lng= $("#longId").val();
            var lat= $("#latId").val();

            var lnglat = new AMap.LngLat(lng, lat); //一个点
            var markericon = m1;
            //构建一个标注点
            var marker = new AMap.Marker({
                icon: markericon,
                position: lnglat
            });

            marker.setMap(map);  //把标注点放到地图上
            map.setZoom(20);
            map.setCenter([lng, lat]); //设置地图中心点
            //构建信息窗体
            //var infoWindow = openInfo(value,marker);


            //在指定位置打开信息窗体
            // function openInfo(value,marker) {
            //     //构建信息窗体中显示的内容
            //     var info = [];
            //     info.push("<div style='line-height:1.6em;font-size:12px;'>");
            //
            //     info.push("小区名称 ："+value.name);
            //     info.push("详细地址 ："+ value.address + "</div>");
            //     var infoWindow = new AMap.InfoWindow({
            //         offset: new AMap.Pixel(0, -29),
            //         content:  info.join("<br/>"),  //使用默认信息窗体框样式，显示信息内容
            //     });
            //
            //     marker.on("mouseover", function(e) {
            //         infoWindow.open(map, e.target.getPosition());
            //     });
            //     marker.on("mouseout", function() {
            //         infoWindow.close();
            //     });
            //
            // }
        </script>
    </div>
    <div class="container imgsbox">
        <div class="image-set">
            <c:forEach items="${cover.coverImageList}" var="images">
                <a data-magnify="gallery" data-caption="井盖编号：${coverAudit.cover.no}" href="${images.url}">
                    <img  src="${images.url}" alt="">
                </a>
            </c:forEach>
        </div>
    </div>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-15 active"><label class="pull-right">井盖状态：</label></td>
            <td class="width-35">
                <form:select path="coverStatus" class="form-control ">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('cover_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-15 active"><label class="pull-right">编号：</label></td>
            <td class="width-35">
                <form:input path="no" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">标签号：</label></td>
            <td class="width-35">
                <form:input path="tagNo" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>井盖类型：</label></td>
            <td class="width-35">
                <form:select path="coverType" class="form-control required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('cover_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>省：</label></td>
            <td class="width-35">
                <form:input path="province" htmlEscape="false"    class="form-control required"/>
            </td>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>市：</label></td>
            <td class="width-35">
                <form:input path="city" htmlEscape="false"    class="form-control required"/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>辖区：</label></td>
            <td class="width-35">
                <form:select path="jurisdiction"  class="form-control m-b">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('cover_jurisdiction')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-15 active"><label class="pull-right">城市代码（0516）：</label></td>
            <td class="width-35">
                <form:input path="cityCode" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">行政区划代码（320312）：</label></td>
            <td class="width-35">
                <form:input path="adCode" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>区：</label></td>
            <td class="width-35">
                <form:input path="district" htmlEscape="false"    class="form-control required"/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">街道（办事处）：</label></td>
            <td class="width-35">
                <form:input path="township" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">路（街巷）：</label></td>
            <td class="width-35">
                <form:input path="street" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">门牌号：</label></td>
            <td class="width-35">
                <form:input path="streetNumber" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">详细地址：</label></td>
            <td class="width-35">
                <form:input path="addressDetail" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">坐标类型：</label></td>
            <td class="width-35">
                <form:input path="coordinateType" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>经度：</label></td>
            <td class="width-35">
                <form:input path="longitude" htmlEscape="false"    class="form-control required"/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>纬度：</label></td>
            <td class="width-35">
                <form:input path="latitude" htmlEscape="false"    class="form-control required"/>
            </td>
            <td class="width-15 active"><label class="pull-right">海拔（m）：</label></td>
            <td class="width-35">
                <form:input path="altitude" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">定位精度（m）：</label></td>
            <td class="width-35">
                <form:input path="locationAccuracy" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">海拔精度（m）：</label></td>
            <td class="width-35">
                <form:input path="altitudeAccuracy" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>井位用途：</label></td>
            <td class="width-35">
                <form:select path="purpose" class="form-control required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('cover_purpose')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>井位地理场合：</label></td>
            <td class="width-35">
                <form:select path="situation" class="form-control required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('cover_situation')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">制造商：</label></td>
            <td class="width-35">
                <form:input path="manufacturer" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">尺寸规格：</label></td>
            <td class="width-35">
                <form:select path="sizeSpec" class="form-control ">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('cover_size_spec')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>井盖规格：</label></td>
            <td class="width-35">
                <form:select path="sizeRule" class="form-control required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('cover_size_rule')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-15 active"><label class="pull-right">直径（mm）：</label></td>
            <td class="width-35">
                <form:input path="sizeDiameter" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>
<%--            <td class="width-15 active"><label class="pull-right">尺寸：半径（mm）** 已废弃，使用diameter字段 **：</label></td>
            <td class="width-35">
                <form:input path="sizeRadius" htmlEscape="false"    class="form-control "/>
            </td>--%>
            <td class="width-15 active"><label class="pull-right">长度（mm）：</label></td>
            <td class="width-35">
                <form:input path="sizeLength" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">宽度（mm）：</label></td>
            <td class="width-35">
                <form:input path="sizeWidth" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>

            <td class="width-15 active"><label class="pull-right">井盖材质：</label></td>
            <td class="width-35">
                <form:select path="material" class="form-control ">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('cover_material')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">权属单位：</label></td>
            <td class="width-35">
                <form:select path="ownerDepart" class="form-control ">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('cover_owner_depart')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-15 active"><label class="pull-right">监管单位：</label></td>
            <td class="width-35">
                <form:input path="superviseDepart" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">地图标记：</label></td>
            <td class="width-35">
                <form:select path="marker" class="form-control ">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('cover_damage')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否损毁：</label></td>
            <td class="width-35">
                <form:select path="isDamaged" class="form-control required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('boolean')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">井筒破损深度（m）：</label></td>
            <td class="width-35">
                <form:input path="manholeDamageDegree" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">损毁情况备注：</label></td>
            <td class="width-35">
                <form:input path="damageRemark" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">高度差，井中心与周边路面（1.5m范围）：</label></td>
            <td class="width-35">
                <form:select path="altitudeIntercept" class="form-control ">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('cover_altitude_intercept')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-15 active"></td>
            <td class="width-35" ></td>
        </tr>
        </tbody>
    </table>
    <div class="tabs-container">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">井盖损坏形式：</a>
            </li>
            <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">井盖权属单位：</a>
            </li>
        </ul>
        <div class="tab-content">
            <div id="tab-1" class="tab-pane fade in  active">
                <a class="btn btn-white btn-sm" onclick="addRow('#coverDamageList', coverDamageRowIdx, coverDamageTpl);coverDamageRowIdx = coverDamageRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr>
                        <th class="hide"></th>
                        <th>破损形式</th>
                        <th width="10">&nbsp;</th>
                    </tr>
                    </thead>
                    <tbody id="coverDamageList">
                    </tbody>
                </table>
                <script type="text/template" id="coverDamageTpl">//<!--
				<tr id="coverDamageList{{idx}}">
					<td class="hide">
						<input id="coverDamageList{{idx}}_id" name="coverDamageList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="coverDamageList{{idx}}_delFlag" name="coverDamageList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<select id="coverDamageList{{idx}}_damage" name="coverDamageList[{{idx}}].damage" data-value="{{row.damage}}" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('cover_damage')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>

					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#coverDamageList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
                </script>
                <script type="text/javascript">
                    var coverDamageRowIdx = 0, coverDamageTpl = $("#coverDamageTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                    $(document).ready(function() {
                        var data = ${fns:toJson(cover.coverDamageList)};
                        for (var i=0; i<data.length; i++){
                            addRow('#coverDamageList', coverDamageRowIdx, coverDamageTpl, data[i]);
                            coverDamageRowIdx = coverDamageRowIdx + 1;
                        }
                    });
                </script>
            </div>
            <div id="tab-2" class="tab-pane fade">
                <a class="btn btn-white btn-sm" onclick="addRow('#coverOwnerList', coverOwnerRowIdx, coverOwnerTpl);coverOwnerRowIdx = coverOwnerRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr>
                        <th class="hide"></th>
                        <th>权属单位</th>
                        <th width="10">&nbsp;</th>
                    </tr>
                    </thead>
                    <tbody id="coverOwnerList">
                    </tbody>
                </table>
                <script type="text/template" id="coverOwnerTpl">//<!--
				<tr id="coverOwnerList{{idx}}">
					<td class="hide">
						<input id="coverOwnerList{{idx}}_id" name="coverOwnerList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="coverOwnerList{{idx}}_delFlag" name="coverOwnerList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<select id="coverOwnerList{{idx}}_ownerName" name="coverOwnerList[{{idx}}].ownerName" data-value="{{row.ownerName}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('cover_owner_depart')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>

					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#coverOwnerList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
                </script>
                <script type="text/javascript">
                    var coverOwnerRowIdx = 0, coverOwnerTpl = $("#coverOwnerTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                    $(document).ready(function() {
                        var data = ${fns:toJson(cover.coverOwnerList)};
                        for (var i=0; i<data.length; i++){
                            addRow('#coverOwnerList', coverOwnerRowIdx, coverOwnerTpl, data[i]);
                            coverOwnerRowIdx = coverOwnerRowIdx + 1;
                        }
                    });
                </script>
            </div>
        </div>
    </div>
</form:form>
</body>
</html>