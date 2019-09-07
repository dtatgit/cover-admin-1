/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.alarm;

import java.text.SimpleDateFormat;
import java.util.*;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.statis.CoverCollectStatis;
import com.jeeplus.modules.cv.mapper.statis.CoverCollectStatisMapper;
import com.jeeplus.modules.cv.vo.CollectionStatisVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.alarm.CoverBellAlarm;
import com.jeeplus.modules.cb.mapper.alarm.CoverBellAlarmMapper;

/**
 * 井铃报警信息Service
 * @author crj
 * @version 2019-06-24
 */
@Service
@Transactional(readOnly = true)
public class CoverBellAlarmService extends CrudService<CoverBellAlarmMapper, CoverBellAlarm> {
	@Autowired
	private CoverWorkService coverWorkService;
	@Autowired
	private CoverCollectStatisMapper coverCollectStatisMapper;
	public CoverBellAlarm get(String id) {
		return super.get(id);
	}
	
	public List<CoverBellAlarm> findList(CoverBellAlarm coverBellAlarm) {
		return super.findList(coverBellAlarm);
	}
	
	public Page<CoverBellAlarm> findPage(Page<CoverBellAlarm> page, CoverBellAlarm coverBellAlarm) {
		return super.findPage(page, coverBellAlarm);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverBellAlarm coverBellAlarm) {
		super.save(coverBellAlarm);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverBellAlarm coverBellAlarm) {
		super.delete(coverBellAlarm);
	}

	@Transactional(readOnly = false)
	public void createWork(CoverBellAlarm coverBellAlarm) {
		coverWorkService.createWork(coverBellAlarm);
		//标记报警信息为生成工单
		coverBellAlarm.setIsGwo(CodeConstant.BOOLEAN.YES);
		super.save(coverBellAlarm);
	}


	/**
	 * 获取最近几天的报警数据
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
		Integer alarmNum=0;		// 报警数量
		Date date = new Date();
		//获取指定日期时间
		Date backupTime= org.apache.commons.lang3.time.DateUtils.addDays(date,day);
		SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd 23:59:59");

		logger.info("开始时间为：" +sdfBegin.format(backupTime));
		logger.info("结束时间为：" +sdfEnd.format(backupTime));
		StringBuffer sb=new StringBuffer("select  COUNT(*) as S from cover_bell_alarm where 1=1 ");
		sb.append(" and DATEDIFF(alarm_date,NOW())=").append(day);
		String alarmSQL=sb.toString();
		List<Map<String, Object>> alarmList=coverCollectStatisMapper.selectBySql(alarmSQL);
		alarmNum=indexStatisJobData(alarmList,"S");

		SimpleDateFormat resultDate = new SimpleDateFormat("yyyyMMdd");
		result.setAlarmNum(alarmNum);
		result.setAlarmTime(resultDate.format(backupTime));
		return result;
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
}