/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.entity.task.CoverTableField;
import com.jeeplus.modules.cv.entity.task.CoverTaskProcess;
import com.jeeplus.modules.cv.mapper.equinfo.CoverMapper;
import com.jeeplus.modules.cv.mapper.statis.CoverCollectStatisMapper;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import com.jeeplus.modules.cv.vo.UserCollectionVO;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.task.CoverTaskInfo;
import com.jeeplus.modules.cv.mapper.task.CoverTaskInfoMapper;

/**
 * 井盖数据处理任务Service
 * @author crj
 * @version 2019-05-16
 */
@Service
@Transactional(readOnly = true)
public class CoverTaskInfoService extends CrudService<CoverTaskInfoMapper, CoverTaskInfo> {
	@Autowired
	private CoverMapper coverMapper;
	@Autowired
	private CoverTableFieldService coverTableFieldService;
	@Autowired
	private CoverTaskProcessService coverTaskProcessService;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CoverService coverService;

	public CoverTaskInfo get(String id) {
		return super.get(id);
	}
	
	public List<CoverTaskInfo> findList(CoverTaskInfo coverTaskInfo) {
		return super.findList(coverTaskInfo);
	}
	
	public Page<CoverTaskInfo> findPage(Page<CoverTaskInfo> page, CoverTaskInfo coverTaskInfo) {
		Page<CoverTaskInfo> pageValue=super.findPage(page, coverTaskInfo);
		List<CoverTaskInfo> list=pageValue.getList();
		if(null!=list&&list.size()>0){
			for(CoverTaskInfo c:list){
				User oldUser = userMapper.get(c.getCreateBy());
				c.setCreateBy(oldUser);
			}
		}
		//return super.findPage(page, cover);
		return pageValue;
		//return super.findPage(page, coverTaskInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverTaskInfo coverTaskInfo) {
		logger.info("************生成任务开始时间*******************"+new Date());
		//任务初始状态为已分配
		coverTaskInfo.setTaskStatus(CodeConstant.TASK_STATUS.ASSIGN);

		/**************获取任务过滤的数据start************************/
		Cover cover=coverTaskInfo.getCover();
		//拼装任务查询条件
		String queryContent=getTaskQuery(cover);
		if(StringUtils.isNotEmpty(queryContent)){
			String sqlValue=cover.getSqlValue();
			List<Cover> coverList=new ArrayList<Cover>();
			if(StringUtils.isNotEmpty(sqlValue)){//通过sql语句创建任务
				coverList=coverService.obtainCoverList(sqlValue);
			}else{
				coverList=coverMapper.findList(cover);
			}
			//List<Cover> coverList=coverMapper.findList(cover);
			coverTaskInfo.setTaskNum(String.valueOf(coverList.size()));
			coverTaskInfo.setTaskContent(queryContent);
			super.save(coverTaskInfo);
			if(null!=coverList&&coverList.size()>0){
				coverTaskProcessService.generateTaskPro(coverTaskInfo,coverList);
			}
/**************获取任务过滤的数据end************************/
			coverTableFieldService.generateTaskField(coverTaskInfo, "cover", "井盖基础信息");
		}
		logger.info("************生成任务结束时间*******************"+new Date());

	}
	
	@Transactional(readOnly = false)
	public void delete(CoverTaskInfo coverTaskInfo) {
		super.delete(coverTaskInfo);
	}
public String getTaskQuery(Cover cover){
		StringBuffer sb=new StringBuffer();
		if(null!=cover){
			String sqlValue=cover.getSqlValue();
			if(StringUtils.isNotEmpty(sqlValue)){
				sb.append("任务SQL ：").append(sqlValue).append(";");
			}
			String coverStatus=cover.getCoverStatus();//状态 字典：cover_status
			if(StringUtils.isNotEmpty(coverStatus)){
				String value=DictUtils.getDictLabel(coverStatus, "cover_status", null);
				sb.append("井盖状态 ：").append(value).append(";");
			}
			String no=cover.getNo();//编号
			if(StringUtils.isNotEmpty(no)){
				sb.append("编号 ：").append(no).append(";");
			}
			String coverType=cover.getCoverType();//井盖类型 字典：cover_type
			if(StringUtils.isNotEmpty(coverType)){
				String value=DictUtils.getDictLabel(coverType, "cover_type", null);
				sb.append("井盖类型 ：").append(value).append(";");
			}
			String city=cover.getCity();//市
			if(StringUtils.isNotEmpty(city)){
				sb.append("市 ：").append(city).append(";");
			}
			String district=cover.getDistrict();//区
			if(StringUtils.isNotEmpty(district)){
				sb.append("区 ：").append(district).append(";");
			}
			String township=cover.getTownship();//街道（办事处）
			if(StringUtils.isNotEmpty(township)){
				sb.append("街道（办事处） ：").append(township).append(";");
			}
			String street=cover.getStreet();//地址：路（街巷）
			if(StringUtils.isNotEmpty(street)){
				sb.append("地址：路（街巷） ：").append(street).append(";");
			}
			String purpose=cover.getPurpose();//井位用途 字典：cover_purpose
			if(StringUtils.isNotEmpty(purpose)){
				String value=DictUtils.getDictLabel(purpose, "cover_purpose", null);
				sb.append("井位用途 ：").append(value).append(";");
			}
			String situation=cover.getSituation();//井位地理场合  字典：cover_situation
			if(StringUtils.isNotEmpty(situation)){
				String value=DictUtils.getDictLabel(situation, "cover_situation", null);
				sb.append("井位地理场合 ：").append(situation).append(";");
			}
			String manufacturer=cover.getManufacturer();//制造商
			if(StringUtils.isNotEmpty(manufacturer)){
				sb.append("制造商 ：").append(manufacturer).append(";");
			}
			String sizeSpec=cover.getSizeSpec();//尺寸规格   字典：cover_size_spec
			if(StringUtils.isNotEmpty(sizeSpec)){
				String value=DictUtils.getDictLabel(sizeSpec, "cover_size_spec", null);
				sb.append("尺寸规格 ：").append(value).append(";");
			}
			String sizeRule=cover.getSizeRule();//井盖规格   字典：cover_size_rule
			if(StringUtils.isNotEmpty(sizeRule)){
				String value=DictUtils.getDictLabel(sizeRule, "cover_size_rule", null);
				sb.append("井盖规格：").append(value).append(";");
			}
			String material=cover.getMaterial();//井盖材质  字典： cover_material
			if(StringUtils.isNotEmpty(material)){
				String value=DictUtils.getDictLabel(material, "cover_material", null);
				sb.append("井盖材质：").append(value).append(";");
			}
			String ownerDepart=cover.getOwnerDepart();//权属单位  字典：cover_owner_depart
			if(StringUtils.isNotEmpty(ownerDepart)){
				String value=DictUtils.getDictLabel(ownerDepart, "cover_owner_depart", null);
				sb.append("权属单位：").append(value).append(";");
			}
			String damageType=cover.getDamageType();//井盖损坏形式  cover_damage
			if(StringUtils.isNotEmpty(damageType)){
				String value=DictUtils.getDictLabel(damageType, "cover_damage", null);
				sb.append("井盖损坏形式：").append(value).append(";");
			}
			String isDamaged=cover.getIsDamaged();//是否损毁  boolean
			if(StringUtils.isNotEmpty(isDamaged)){
				String value=DictUtils.getDictLabel(isDamaged, "boolean", null);
				sb.append("是否损毁：").append(value).append(";");
			}
		/*	BigDecimal altitudeIntercept=cover.getAltitudeIntercept();//高度差 字典：cover_altitude_intercept
			if(null!=altitudeIntercept){
				String value=DictUtils.getDictLabel(String.valueOf(altitudeIntercept), "cover_altitude_intercept", null);
				sb.append("高度差：").append(value).append(";");
			}*/
			BigDecimal beginAltitudeIntercept=cover.getBeginAltitudeIntercept();//开始高度差
			BigDecimal endAltitudeIntercept=cover.getEndAltitudeIntercept();//结束高度差
			if(null!=beginAltitudeIntercept&&null!=endAltitudeIntercept){
				sb.append("高度差：").append(beginAltitudeIntercept).append("至").append(endAltitudeIntercept).append(";");
			}
			String createBy=cover.getCreateBy().getName();//创建人
			if(StringUtils.isNotEmpty(createBy)){
				sb.append("创建人：").append(createBy).append(";");
			}


			Date beginCreateDate=cover.getBeginCreateDate();//创建时间 开始时间
			Date endCreateDate=cover.getEndCreateDate();//创建时间 结束时间
			if(null!=beginCreateDate){
			String  beginCreate=DateUtils.formatDate(beginCreateDate,"yyyy-MM-dd HH:mm:ss");
			sb.append("创建开始时间：").append(beginCreate).append(";");
			}
			if(null!=endCreateDate){
				String  endCreate=DateUtils.formatDate(endCreateDate,"yyyy-MM-dd HH:mm:ss");
				sb.append("创建结束时间：").append(endCreate).append(";");
			}

		}
return sb.toString();
}


	
}