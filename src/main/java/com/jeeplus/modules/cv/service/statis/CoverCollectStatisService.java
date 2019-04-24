/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.statis;

import java.text.SimpleDateFormat;
import java.util.*;

import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
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
		SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
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
				String amount  = resultMap.get("amount").toString();
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



}