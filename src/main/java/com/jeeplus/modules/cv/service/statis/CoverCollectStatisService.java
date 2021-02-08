/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.statis;

import java.text.SimpleDateFormat;
import java.util.*;

import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cb.service.alarm.CoverBellAlarmService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.utils.DoubleUtil;
import com.jeeplus.modules.cv.vo.CollectionStatisVO;
import com.jeeplus.modules.cv.vo.IndexStatisVO;
import com.jeeplus.modules.cv.vo.UserCollectionVO;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.statis.CoverCollectStatis;
import com.jeeplus.modules.cv.mapper.statis.CoverCollectStatisMapper;

/**
 * 窨井盖采集统计Service
 * @author crj
 * @version 2019-04-22
 */
@Service
@Transactional(readOnly = true)
public class CoverCollectStatisService extends CrudService<CoverCollectStatisMapper, CoverCollectStatis> {
	@Autowired
	private CoverCollectStatisMapper coverCollectStatisMapper;
	@Autowired
	private CoverBellAlarmService coverBellAlarmService;

	public CoverCollectStatis get(String id) {
		return super.get(id);
	}

	public List<CoverCollectStatis> findList(CoverCollectStatis coverCollectStatis) {
		return super.findList(coverCollectStatis);
	}

	public Page<CoverCollectStatis> findPage(Page<CoverCollectStatis> page, CoverCollectStatis coverCollectStatis) {
		return super.findPage(page, coverCollectStatis);
	}

	@Transactional(readOnly = false)
	public void save(CoverCollectStatis coverCollectStatis) {
		super.save(coverCollectStatis);
	}

	@Transactional(readOnly = false)
	public void delete(CoverCollectStatis coverCollectStatis) {
		super.delete(coverCollectStatis);
	}


	/**
	 * 井盖采集统计 定时任务
	 * @param beginTime
	 * @param endTime
	 */
	@Transactional(readOnly = false)
	public void collectStatisTask(String beginTime,String endTime){
		try{
			//统计明细之前，先判断当天有没有统计过
			SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
			CoverCollectStatis queryCover=new CoverCollectStatis();

			queryCover.setBeginStatisDate(sdfBegin.parse(sdfBegin.format(new Date())));
			//queryCover.setBeginStatisDate(DateUtils.parseDate(beginTime));
			queryCover.setEndStatisDate(new Date());
			List<CoverCollectStatis> statisList=coverCollectStatisMapper.findList(queryCover);
			if(null!=statisList&&statisList.size()>0){

			}else{
				//SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
				Map<String,Object> map = new HashMap<>();
				map.put("beginTime",beginTime);
				map.put("endTime",endTime);
				List<Map<String, Object>> collectList=coverCollectStatisMapper.collectStatis(map);
				if(null!=collectList&&collectList.size()>0){
					for (Map<String, Object> resultMap:collectList) {
						CoverCollectStatis coverCollectStatis=new CoverCollectStatis();
						String userId = resultMap.get("userId").toString();
						String amount  = resultMap.get("amount").toString();

						coverCollectStatis.setId(IdGen.uuid());
						coverCollectStatis.setCollectNum(amount);
						coverCollectStatis.setStatisDate(new Date());
						coverCollectStatis.setStartDate(beginTime.substring(0 ,10));
						//coverCollectStatis.setEndDate(endTime);
						coverCollectStatis.setCollectUser(UserUtils.get(userId));
						coverCollectStatisMapper.insert(coverCollectStatis);
					}
				}
			}


		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 井盖采集统计 定时任务
	 * @param beginTime
	 * @param endTime
	 */
	@Transactional(readOnly = false)
	public void collectStatisTask2(String statisStartDate,String statisEndDate,String beginTime,String endTime){
		try{

			//统计明细之前，先判断当天有没有统计过
			SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
			CoverCollectStatis queryCover=new CoverCollectStatis();

			//queryCover.setBeginStatisDate(sdfBegin.parse(sdfBegin.format(new Date())));
			queryCover.setBeginStatisDate(DateUtils.parseDate(statisStartDate));
			queryCover.setEndStatisDate(DateUtils.parseDate(statisEndDate));
			List<CoverCollectStatis> statisList=coverCollectStatisMapper.findList(queryCover);
			if(null!=statisList&&statisList.size()>0){

			}else{
				//SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
				Map<String,Object> map = new HashMap<>();
				map.put("beginTime",beginTime);
				map.put("endTime",endTime);
				List<Map<String, Object>> collectList=coverCollectStatisMapper.collectStatis(map);
				if(null!=collectList&&collectList.size()>0){
					for (Map<String, Object> resultMap:collectList) {
						CoverCollectStatis coverCollectStatis=new CoverCollectStatis();
						String userId = resultMap.get("userId").toString();
						String amount  = resultMap.get("amount").toString();

						coverCollectStatis.setId(IdGen.uuid());
						coverCollectStatis.setCollectNum(amount);
						coverCollectStatis.setStatisDate(DateUtils.parseDate(statisStartDate));
						coverCollectStatis.setStartDate(beginTime.substring(0 ,10));
						//coverCollectStatis.setEndDate(endTime);
						coverCollectStatis.setCollectUser(UserUtils.get(userId));
						coverCollectStatisMapper.insert(coverCollectStatis);
					}
				}
			}


		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public Page<CoverCollectStatis> findSummaryPage(Page<CoverCollectStatis> page, CoverCollectStatis coverCollectStatis) {
		//return super.findPage(page, coverCollectStatis);
		List<CoverCollectStatis> summaryList = new ArrayList<>();
		Map<String,Object> map = new HashMap<>();
		if(null!=coverCollectStatis.getCollectUser()&&!coverCollectStatis.getCollectUser().getId().equals("")){
			map.put("collect_user_id", coverCollectStatis.getCollectUser().getId());
		}

		if(null!=coverCollectStatis.getBeginStatisDate()){
			map.put("beginTime", DateUtils.formatDate(coverCollectStatis.getBeginStatisDate()));
		}
		if(null!=coverCollectStatis.getEndStatisDate()){
			map.put("endTime",DateUtils.formatDate(coverCollectStatis.getEndStatisDate()));
		}


		List<Map<String, Object>> collectList=coverCollectStatisMapper.collectSummary(map);
		if(null!=collectList&&collectList.size()>0){
			for (Map<String, Object> resultMap:collectList) {
				CoverCollectStatis statis=new CoverCollectStatis();
				String userId = resultMap.get("userId").toString();
				String amount  =String.valueOf(resultMap.get("amount"));
				statis.setCollectNum(amount);
				statis.setCollectUser(UserUtils.get(userId));
				if(null!=coverCollectStatis.getBeginStatisDate()){
					statis.setStartDate(DateUtils.formatDate(coverCollectStatis.getBeginStatisDate()));
				}
				if(null!=coverCollectStatis.getEndStatisDate()){
					statis.setEndDate(DateUtils.formatDate(coverCollectStatis.getEndStatisDate()));
				}


				summaryList.add(statis);

			}
			page.setCount(collectList.size());
		}
		page.setList(summaryList);
		return page;
	}


	public IndexStatisVO statisIndex(){
		IndexStatisVO indexVO=new IndexStatisVO();
		//总勘察数
		Integer coverTotalNum=getCoverTotalNum(false);
		//今日勘察数
		Integer coverTodayNum=getCoverTotalNum(true);
		//无权属单位
		Integer coverNoDepartNum=getCoverNoOwnerNum();
		indexVO.setCoverTotalNum(coverTotalNum);
		indexVO.setCoverTodayNum(coverTodayNum);
		indexVO.setCoverNoDepartNum(coverNoDepartNum);

		// 损坏形式-完好0
		Integer damageGoodNum=getCoverByDamage(CodeConstant.COVER_DAMAGE.GOOD);
		// 损坏形式-井盖缺失1
		Integer damageDefectNum=getCoverByDamage(CodeConstant.COVER_DAMAGE.DEFECT);
		// 损坏形式-井盖破坏2
		Integer damageDestroyNum=getCoverByDamage(CodeConstant.COVER_DAMAGE.DESTROY);
		// 损坏形式-井周沉降、龟裂3
		Integer damageRiftNum=getCoverByDamage(CodeConstant.COVER_DAMAGE.RIFT);
		// 损坏形式-井筒本身破坏4
		Integer damageOwnerNum=getCoverByDamage(CodeConstant.COVER_DAMAGE.OWNER);
		//其他9
		Integer damageOtherNum=getCoverByDamage(CodeConstant.COVER_DAMAGE.OTHER);
		indexVO.setDamageGoodNum(damageGoodNum);
		indexVO.setDamageDefectNum(damageDefectNum);
		indexVO.setDamageDestroyNum(damageDestroyNum);
		indexVO.setDamageRiftNum(damageRiftNum);
		indexVO.setDamageOwnerNum(damageOwnerNum);
		indexVO.setDamageOtherNum(damageOtherNum);

		Double m1=DoubleUtil.div(Double.valueOf(damageGoodNum), Double.valueOf(coverTotalNum), 4);
		indexVO.setPerDamageGoodNum(DoubleUtil.mul(m1, 100));

		Double m2=DoubleUtil.div(Double.valueOf(damageDefectNum), Double.valueOf(coverTotalNum), 4);
		indexVO.setPerDamageDefectNum(DoubleUtil.mul(m2, 100));

		Double m3=DoubleUtil.div(Double.valueOf(damageDestroyNum), Double.valueOf(coverTotalNum), 4);
		indexVO.setPerDamageDestroyNum(DoubleUtil.mul(m3, 100));

		Double m4=DoubleUtil.div(Double.valueOf(damageOwnerNum), Double.valueOf(coverTotalNum), 4);
		indexVO.setPerDamageOwnerNum(DoubleUtil.mul(m4, 100));

		Double m5=DoubleUtil.div(Double.valueOf(damageRiftNum), Double.valueOf(coverTotalNum), 4);
		indexVO.setPerDamageRiftNum(DoubleUtil.mul(m5, 100));

		Double m6=DoubleUtil.div(Double.valueOf(damageOtherNum), Double.valueOf(coverTotalNum), 4);
		indexVO.setPerDamageOtherNum(DoubleUtil.mul(m6, 100));


		indexVO.setUserCollectionList(getCoverByUser());

		Integer numArr[]=new Integer[7];//首页采集数量数组
		String dateArr[]=new String[7];//首页日期数组
		//获取最近7天数据
		List<CollectionStatisVO> dataList=getDataListByTime(7);
		if(null!=dataList&&dataList.size()>0){
			for(int i=0;i<dataList.size();i++){
				CollectionStatisVO dateStatis=dataList.get(i);
				numArr[i]=dateStatis.getCoverTotalNum();
				dateArr[i]=dateStatis.getCollectionTime();
				//String d=dateStatis.getCollectionTime();
				//dateArr[i]=d.substring(4,d.length() );
			}
		}
		indexVO.setNumArr(numArr);
		indexVO.setDateArr(dateArr);

		//获取最近7天数据
		Integer alarmNum[]=new Integer[7];//首页采集数量数组
		String alarmTime[]=new String[7];//首页日期数组
		List<CollectionStatisVO> alarmList=coverBellAlarmService.getDataListByTime(7);
		if(null!=alarmList&&alarmList.size()>0){
			for(int i=0;i<alarmList.size();i++){
				CollectionStatisVO dateStatis=alarmList.get(i);
				alarmNum[i]=dateStatis.getAlarmNum();
				alarmTime[i]= dateStatis.getAlarmTime();
			}
		}
		indexVO.setAlarmNumArr(alarmNum);
		indexVO.setAlarmDateArr(alarmTime);
//		indexVO.setNum1(numArr[0].toString());
//		indexVO.setDate1(dateArr[0]);
		return indexVO;

	}

	public  List<UserCollectionVO> getCoverByUser(){
		List<UserCollectionVO> userCollectionList=new ArrayList<UserCollectionVO>();
		Integer damageNum=0;		// 损坏总数
		StringBuffer lineSQL=new StringBuffer("SELECT COUNT(r.id) AS amount ,r.create_by AS userId FROM cover r ");
		lineSQL.append(" WHERE r.id  IS NOT NULL  and r.data_source !='import' ");
		lineSQL.append("  GROUP BY r.create_by  order by amount desc ");
		String coverSQL=lineSQL.toString();
		List<Map<String, Object>> coverUserList = coverCollectStatisMapper.selectBySql(coverSQL);
		if(null!=coverUserList&&coverUserList.size()>0){
			for(int i=0;i<coverUserList.size();i++){
				Map<String, Object> map=coverUserList.get(i);
				Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
				String userId=String.valueOf(map.get("userId"));
				User user=UserUtils.get(userId);
				UserCollectionVO vo=new UserCollectionVO();
				vo.setCollectNum(amount);
				vo.setCollectionName(user.getName());
				userCollectionList.add(vo);
				if(i==9){
					break;
				}
			}
		}

		return userCollectionList;
	}


	public Integer getCoverByDamage(String coverDamage){
		Integer damageNum=0;		// 损坏总数
		StringBuffer lineSQL=new StringBuffer("SELECT  COUNT(c.id) as S FROM  cover_damage c ");
		lineSQL.append(" where c.status='normal' ");
		lineSQL.append("  and c.damage= ").append(coverDamage);
		String coverSQL=lineSQL.toString();
		List<Map<String, Object>> coverList = coverCollectStatisMapper.selectBySql(coverSQL);
		damageNum=indexStatisJobData(coverList,"S");
		return damageNum;
	}

	/**
	 *总勘察数
	 * isToday 是否获取当日数据
	 * @return
	 */
	public Integer getCoverTotalNum(Boolean isToday){
		Integer coverTotalNum=0;		// 总勘察数
		StringBuffer lineSQL=new StringBuffer("SELECT  COUNT(c.id) as S FROM cover  c ");
		lineSQL.append(" where c.del_flag='0' and c.data_source !='import' ");
		if(isToday){
			lineSQL.append(" and to_days(c.create_date) = to_days(now()) ");
		}
		//获取当日数据
		//SELECT  COUNT(c.id)as S FROM cover  c WHERE  c.del_flag='0' and to_days(c.create_date) = to_days(now());
		String coverSQL=lineSQL.toString();
		List<Map<String, Object>> coverList = coverCollectStatisMapper.selectBySql(coverSQL);
		coverTotalNum=indexStatisJobData(coverList,"S");
		return coverTotalNum;
	}
	/**无权属单位井盖数据
	 * @return
	 */
	public Integer getCoverNoOwnerNum(){
		Integer coverNum=0;		//
		StringBuffer lineSQL=new StringBuffer("SELECT  COUNT(c.id) as S FROM cover  c LEFT JOIN cover_owner o on c.id=o.cover_id ");
		lineSQL.append(" where c.del_flag='0' and c.data_source !='import' and o.id is null ");
		String coverSQL=lineSQL.toString();
		List<Map<String, Object>> coverList = coverCollectStatisMapper.selectBySql(coverSQL);
		coverNum=indexStatisJobData(coverList,"S");
		return coverNum;
	}

	private  Integer indexStatisJobData(List<Map<String, Object>> rsList,String name ){

		Integer num=0 ;
		if(null!=rsList&&rsList.size()>=0){
			Map<String, Object> result = rsList.get(0);

			if (result == null || !result.containsKey(name)){
				num = 0;
			}else {
				num = Integer.parseInt(String.valueOf(result.get(name)));
			}
		}
		return num;
	}


	/**
	 * 获取最近几点的统计数据
	 * @param m
	 * @return
	 */
	public List<CollectionStatisVO> getDataListByTime(int m){
		List<CollectionStatisVO> list=new ArrayList<CollectionStatisVO>();
			for(int i=0;i<m;i++){
				CollectionStatisVO vo=getDataByTime(-i);
				list.add(vo);

			}
			return list;
	}
	/**
	 *
	 * @param day  -1 昨天，最近七天是-7，-6，-5，-4，-3，-2，-1
	 * @return
	 */
	public CollectionStatisVO getDataByTime(int day){
		CollectionStatisVO result=new CollectionStatisVO() ;
		Integer coverTotalNum=0;		// 采集数量
		Date date = new Date();
		//获取指定日期时间
		Date backupTime= org.apache.commons.lang3.time.DateUtils.addDays(date,day);
		SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd 23:59:59");

		logger.info("开始时间为：" +sdfBegin.format(backupTime));
		logger.info("结束时间为：" +sdfEnd.format(backupTime));


		List<CoverCollectStatis> summaryList = new ArrayList<>();
		Map<String,Object> map = new HashMap<>();
		map.put("beginTime", sdfBegin.format(backupTime));
		map.put("endTime",sdfEnd.format(backupTime));
		//List<Map<String, Object>> collectList=coverCollectStatisMapper.collectSummary(map);
		List<Map<String, Object>> collectList=coverCollectStatisMapper.collectStatis(map);
		if(null!=collectList&&collectList.size()>0){
			for (Map<String, Object> resultMap:collectList) {
				CoverCollectStatis coverCollectStatis=new CoverCollectStatis();
				//String userId = resultMap.get("userId").toString();
				Integer amount=	Integer.parseInt(String.valueOf(resultMap.get("amount")));
				coverTotalNum=coverTotalNum+amount;
			}
		}

		//SimpleDateFormat resultDate = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat resultDate = new SimpleDateFormat("yyyyMMdd");
		result.setCoverTotalNum(coverTotalNum);
		result.setCollectionTime(resultDate.format(backupTime));
		return result;
	}

	public Integer statisByArea(String districtName){
		Integer num=0;		// 总勘察数
		StringBuffer lineSQL=new StringBuffer("SELECT  COUNT(c.id) as S FROM cover  c ");
		lineSQL.append(" where c.del_flag='0' and c.data_source !='import' ");
		if(StringUtils.isNotEmpty(districtName)){
			lineSQL.append("  and c.district= ").append("'"+districtName+"'");
		}
		String coverSQL=lineSQL.toString();
		List<Map<String, Object>> coverList = coverCollectStatisMapper.selectBySql(coverSQL);
		num=indexStatisJobData(coverList,"S");
		return num;
	}
	public Map statisGroupbyArea(){
		Map<String,String> map = new HashMap<String,String>();
		StringBuffer lineSQL=new StringBuffer("select count(*) as num,district from cover where del_flag='0' group by district");
		String coverSQL=lineSQL.toString();
		List<Map<String, Object>> collectList = coverCollectStatisMapper.selectBySql(coverSQL);
		if(null!=collectList&&collectList.size()>0){
			for (Map<String, Object> resultMap:collectList) {
				String num=String.valueOf(resultMap.get("num"));
				String district=String.valueOf(resultMap.get("district"));
				map.put(district, num);
			}
		}
		return map;
	}

	/**
	 * add by 2019-05-10
	 * 井位用途数据统计
	 * @return
	 */
	public  List<CollectionStatisVO> getCoverByPurpose(){
		List<CollectionStatisVO> purposeList=new ArrayList<CollectionStatisVO>();

		StringBuffer lineSQL=new StringBuffer("SELECT  purpose AS purposeName,count(id) AS amount  FROM cover");
		lineSQL.append("  where del_flag='0' and data_source !='import' ");
		lineSQL.append("  group by purpose ");
		String coverSQL=lineSQL.toString();
		List<Map<String, Object>> coverPurposeList = coverCollectStatisMapper.selectBySql(coverSQL);
		if(null!=coverPurposeList&&coverPurposeList.size()>0){
			for(int i=0;i<coverPurposeList.size();i++){
				Map<String, Object> map=coverPurposeList.get(i);
				Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
				String purposeName=String.valueOf(map.get("purposeName"));

				if(StringUtils.isNotEmpty(purposeName)){
					CollectionStatisVO vo=new CollectionStatisVO();
					vo.setCoverTotalNum(amount);
					vo.setPurpose(purposeName);
					purposeList.add(vo);
				}


			}
		}
		return purposeList;
	}
	/**
	 * add by 2019-05-28
	 * 井位损坏形式数据汇总
	 * @return
	 */
	public  List<CollectionStatisVO> statisByDamage(){
		List<CollectionStatisVO> damageList=new ArrayList<CollectionStatisVO>();

		StringBuffer lineSQL=new StringBuffer("SELECT  damage AS laberValue,count(id) AS amount  FROM cover_damage ");
		lineSQL.append("  where status='normal' ");
		lineSQL.append("  group by damage  order by amount desc ");
		String damageSQL=lineSQL.toString();
		List<Map<String, Object>> coverDamageList = coverCollectStatisMapper.selectBySql(damageSQL);
		Integer amountTotal=0;
		if(null!=coverDamageList&&coverDamageList.size()>0){
			for(int i=0;i<coverDamageList.size();i++){
				Map<String, Object> map=coverDamageList.get(i);
				Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
				String laberValue=String.valueOf(map.get("laberValue"));

				if(StringUtils.isNotEmpty(laberValue)){
					CollectionStatisVO vo=new CollectionStatisVO();
					vo.setCoverTotalNum(amount);
					vo.setDamageLaber(laberValue);
					String name= DictUtils.getDictLabel(laberValue, "cover_damage", null);
					vo.setDamageName(name);
					damageList.add(vo);
					amountTotal=amountTotal+amount;
				}


			}
		}
		if(null!=damageList&&damageList.size()>0){
			for(CollectionStatisVO vo:damageList){
				Integer coverTotalNum=vo.getCoverTotalNum();
				Double m=DoubleUtil.div(Double.valueOf(coverTotalNum), Double.valueOf(amountTotal), 4);
				vo.setDamagePerNum(String.valueOf(DoubleUtil.mul(m, 100)));
			}
		}
		return damageList;
	}


	/**
	 * add by 2019-05-13
	 * 权属单位数据统计
	 * @return
	 */
	public  List<CollectionStatisVO> getNumByOwner(){
		List<CollectionStatisVO> purposeList=new ArrayList<CollectionStatisVO>();
		//select count(o.id) AS amount ,o.owner_name AS ownerName  from cover_owner o group by o.owner_name order by count(o.id) desc
		StringBuffer lineSQL=new StringBuffer("select count(o.id) AS amount ,o.owner_name AS ownerName  from cover_owner o");
		//lineSQL.append("  where del_flag='0' and data_source !='import' ");
		lineSQL.append("  group by o.owner_name order by count(o.id) desc ");
		String coverSQL=lineSQL.toString();
		List<Map<String, Object>> coverPurposeList = coverCollectStatisMapper.selectBySql(coverSQL);
		if(null!=coverPurposeList&&coverPurposeList.size()>0){
			for(int i=0;i<coverPurposeList.size();i++){
				Map<String, Object> map=coverPurposeList.get(i);
				Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
				String ownerName=String.valueOf(map.get("ownerName"));

				if(StringUtils.isNotEmpty(ownerName)){
					CollectionStatisVO vo=new CollectionStatisVO();
					vo.setCoverTotalNum(amount);
					vo.setPurpose(ownerName);
					purposeList.add(vo);
				}
			}
		}
		return purposeList;
	}


	/**
	 * add by 2021-2-8
	 * 井盖材质数据汇总
	 * @return
	 */
	public  List<CollectionStatisVO> statisByMaterial(){
		List<CollectionStatisVO> dataList=new ArrayList<CollectionStatisVO>();

		StringBuffer lineSQL=new StringBuffer("select a.material AS material,count(a.id) AS amount from cover a ");
		lineSQL.append("  where 1=1 ");
		lineSQL.append("  group by a.material ");
		String dataSQL=lineSQL.toString();
		List<Map<String, Object>> coverDataList = coverCollectStatisMapper.selectBySql(dataSQL);
		Integer amountTotal=0;
		if(null!=coverDataList&&coverDataList.size()>0){
			for(int i=0;i<coverDataList.size();i++){
				Map<String, Object> map=coverDataList.get(i);
				Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
				String material=String.valueOf(map.get("material"));

				if(StringUtils.isNotEmpty(material)){
					CollectionStatisVO vo=new CollectionStatisVO();
					vo.setCoverTotalNum(amount);
					vo.setMaterial(material);
					dataList.add(vo);
				}


			}
		}
		return dataList;
	}
	/**
	 * add by 2021-2-8
	 * 井盖地理场合数据汇总
	 * @return
	 */
	public  List<CollectionStatisVO> statisBySituation(){
		List<CollectionStatisVO> dataList=new ArrayList<CollectionStatisVO>();

		StringBuffer lineSQL=new StringBuffer("select a.situation AS situation,count(a.id) AS amount from cover a ");
		lineSQL.append("  where 1=1 ");
		lineSQL.append("  group by a.situation ");
		String dataSQL=lineSQL.toString();
		List<Map<String, Object>> coverDataList = coverCollectStatisMapper.selectBySql(dataSQL);
		Integer amountTotal=0;
		if(null!=coverDataList&&coverDataList.size()>0){
			for(int i=0;i<coverDataList.size();i++){
				Map<String, Object> map=coverDataList.get(i);
				Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
				String situation=String.valueOf(map.get("situation"));

				if(StringUtils.isNotEmpty(situation)){
					CollectionStatisVO vo=new CollectionStatisVO();
					vo.setCoverTotalNum(amount);
					vo.setSituation(situation);
					dataList.add(vo);
				}

			}
		}
		return dataList;
	}
}