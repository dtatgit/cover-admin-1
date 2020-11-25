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
                    jp.post("${ctx}/cv/task/coverTaskProcess/assignOwnerSave",$('#inputForm').serialize(),function(data){
                        if(data.success){
                            //$table.bootstrapTable('refresh');
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
<form:form id="inputForm" modelAttribute="cover" action="${ctx}/cv/task/coverTaskProcess/assignOwnerSave" method="post" class="form-horizontal">
<form:hidden path="id"/>
<form:hidden path="coverTaskProcessId"/>
<input type="hidden" id="longId" value="${cover.longitude}"/>
<input type="hidden" id="latId" value="${cover.latitude}"/>
<input type="hidden" id="showFlag" value="${cover.isDamaged}"/>
<sys:message content="${message}"/>
<div class="examinebox examinebox1 examinebox-s2">
    <div class="map">
            <%--放地图--%>
        <div id="container" style="height: 220px;width: 100%; position: relative"></div>
        <script type="text/javascript">

            var map = new AMap.Map('container', {
                resizeEnable: true,
                //zoom:14,//级别
            });
            map.setCity('徐州');

            var m1 = new AMap.Icon({
                image: '${ctxStatic}/common/images/cover.png',  // Icon的图像
                size: new AMap.Size(26, 30),    // 原图标尺寸
                imageSize: new AMap.Size(26, 30), //实际使用的大小
                offset: new AMap.Pixel(-13, -15),
                anchor: 'center'
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
            map.setCenter([lng, lat]);
            map.setZoom(14);
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
    <div class="examinebox">
        <h1 class="title2">井盖信息</h1>
        <div class="inforbox">
            <ul>
                <c:if test="${fn:contains(fieldList, 'cover_status')}">
                    <li><label>井盖状态:</label><span>${fns:getDictLabel (cover.coverStatus, "cover_status", "--")}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'no')}">
                    <li><label>井盖编号:</label><span>${cover.no}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'cover_type')}">
                    <li><label>井盖类型:</label><span>${fns:getDictLabel (cover.coverType, "cover_type", "--")}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'province')}">
                <li><label>省:</label><span>${cover.province}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'city')}">
                <li><label>市:</label><span>${cover.city}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'city_code')}">
                    <li><label>城市代码:</label><span>${cover.cityCode}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'ad_code')}">
                    <li><label>行政区划代码:</label><span>${cover.adCode}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'district')}">
                    <li><label>区:</label><span>${cover.district}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'township')}">
                    <li><label>街道（办事处）:</label><span>${cover.township}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'street')}">
                    <li><label>路（街巷）:</label><span>${cover.street}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'street_number')}">
                    <li><label>门牌号:</label><span>${cover.streetNumber}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'address_detail')}">
                    <li><label>详细地址:</label><span>${cover.addressDetail}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'coordinate_type')}">
                    <li><label>坐标类型:</label><span>${cover.coordinateType}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'longitude')}">
                    <li><label>经度:</label><span>${cover.longitude}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'latitude')}">
                    <li><label>纬度:</label><span>${cover.latitude}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'altitude')}">
                    <li><label>海拔（m）:</label><span>${cover.altitude}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'location_accuracy')}">
                    <li><label>定位精度（m）:</label><span>${cover.locationAccuracy}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'altitude_accuracy')}">
                    <li><label>海拔精度（m）:</label><span>${cover.altitudeAccuracy}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'cover_purpose')}">
                    <li><label>井位用途:</label><span>${fns:getDictLabel (cover.purpose, "cover_purpose", "--")}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'cover_situation')}">
                    <li><label>井位地理场合:</label><span>${fns:getDictLabel (cover.situation, "cover_situation", "--")}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'manufacturer')}">
                    <li><label>制造商:</label><span>${cover.manufacturer}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'size_spec')}">
                    <li><label>尺寸规格:</label><span>${fns:getDictLabel (cover.sizeSpec, "cover_size_spec", "--")}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'size_rule')}">
                    <li><label>井盖规格:</label><span>${fns:getDictLabel (cover.sizeRule, "cover_size_rule", "--")}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'size_diameter')}">
                    <li><label>直径（mm）:</label><span>${cover.sizeDiameter}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'size_length')}">
                    <li><label>长度（mm）:</label><span>${cover.sizeLength}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'size_width')}">
                    <li><label>宽度（mm）:</label><span>${cover.sizeWidth}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'material')}">
                    <li><label>井盖材质:</label><span>${fns:getDictLabel (cover.material, "cover_material", "--")}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'supervise_depart')}">
                    <li><label>监管单位:</label><span>${cover.superviseDepart}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'marker')}">
                    <li><label>地图标记:</label><span>${fns:getDictLabel (cover.material, "cover_damage", "--")}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'is_damaged')}">
                    <li><label>是否损毁:</label><span>${fns:getDictLabel (cover.isDamaged, "boolean", "--")}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'manhole_damage_degree')}">
                    <li><label>井筒破损深度（m）:</label><span>${cover.manholeDamageDegree}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'damage_remark')}">
                    <li><label>损毁情况备注:</label><span>${cover.damageRemark}</span></li>
                </c:if>

                <c:if test="${fn:contains(fieldList, 'altitude_intercept')}">
                    <li><label>高度差:</label><span>${cover.altitudeIntercept}</span></li>
                </c:if>
            </ul>
        </div>
    </div>

    <div class="examinebox"  id="ownerId">
        <h1 class="title2">权属单位</h1>
        <div class="inforbox">

            <div class="damage">
                <c:forEach items="${cover.coverOwnerList}" var="owner">
                    <label class="t">${owner.ownerName}</label>
                </c:forEach>
            </div>

        </div>
    </div>


    <div class="examinebox">
        <h1 class="title2">归属结果</h1>
        <div class="inforbox">
            <div class="mui-input-row mui-radio-checkbox-row">
			<span class="mui-radio">
				<input id="auditResult" name="ownerResult" type="radio" value="Y" checked>
				<label for="auditResult">确认</label>
			</span>
                <span class="mui-radio">
				<input id="auditResult2" name="ownerResult" type="radio" value="N" />
				<label for="auditResult2">取消</label>
			</span>
            </div>

        </div>
    </div>
  <%--  <div class="tabs-container">
        <ul class="nav nav-tabs">
 &lt;%&ndash;           <li class=""><a data-toggle="tab" href="#tab-1" aria-expanded="true">井盖损坏形式：</a>
            </li>&ndash;%&gt;
            <li class="active"><a data-toggle="tab" href="#tab-2" aria-expanded="false">井盖权属单位：</a>
            </li>
        </ul>
        <div class="tab-content">
      &lt;%&ndash;      <div id="tab-1" class="tab-pane fade in  active" class="tab-pane fade" >
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
            </div>&ndash;%&gt;
            <div id="tab-2" class="tab-pane fade in  active">
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
    </div>--%>
    </form:form>
</body>
</html>
