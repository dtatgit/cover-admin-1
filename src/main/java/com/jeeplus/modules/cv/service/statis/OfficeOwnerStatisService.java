/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.statis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.collection.CollectionUtil;
import com.jeeplus.core.security.Digests;
import com.jeeplus.modules.cv.entity.equinfo.CoverOfficeOwner;
import com.jeeplus.modules.cv.entity.statis.CoverStatis;
import com.jeeplus.modules.cv.mapper.equinfo.CoverOfficeOwnerMapper;
import com.jeeplus.modules.cv.mapper.statis.CoverCollectStatisMapper;
import com.jeeplus.modules.cv.service.equinfo.CoverOfficeOwnerService;
import com.jeeplus.modules.sys.entity.Office;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.statis.OfficeOwnerStatis;
import com.jeeplus.modules.cv.mapper.statis.OfficeOwnerStatisMapper;

/**
 * 维护部门权属单位统计Service
 * @author crj
 * @version 2021-03-22
 */
@Service
@Transactional
public class OfficeOwnerStatisService extends CrudService<OfficeOwnerStatisMapper, OfficeOwnerStatis> {
	@Autowired
	private OfficeOwnerStatisMapper officeOwnerStatisMapper;
	@Autowired
	private CoverOfficeOwnerMapper coverOfficeOwnerMapper;
	@Autowired
	private CoverCollectStatisMapper coverCollectStatisMapper;
	public OfficeOwnerStatis get(String id) {
		return super.get(id);
	}
	
	public List<OfficeOwnerStatis> findList(OfficeOwnerStatis officeOwnerStatis) {
		return super.findList(officeOwnerStatis);
	}
	
	public Page<OfficeOwnerStatis> findPage(Page<OfficeOwnerStatis> page, OfficeOwnerStatis officeOwnerStatis) {
		return super.findPage(page, officeOwnerStatis);
	}

	public Page<OfficeOwnerStatis> findPageByOffice(Page<OfficeOwnerStatis> page, OfficeOwnerStatis officeOwnerStatis) {
		officeOwnerStatis.setPage(page);
		page.setList(mapper.findOfficeList(officeOwnerStatis));
		return page;
	}

	public Page<OfficeOwnerStatis> findPageByOwnerDepart(Page<OfficeOwnerStatis> page, OfficeOwnerStatis officeOwnerStatis) {
		officeOwnerStatis.setPage(page);
		page.setList(mapper.findOwnerDepartList(officeOwnerStatis));
		return page;
	}

	@Transactional(readOnly = false)
	public void save(OfficeOwnerStatis officeOwnerStatis) {
		super.save(officeOwnerStatis);
	}
	
	@Transactional(readOnly = false)
	public void delete(OfficeOwnerStatis officeOwnerStatis) {
		super.delete(officeOwnerStatis);
	}

	public String getInfoFlag(String officeId,String ownerDepart){
		StringBuffer sb=new StringBuffer();
		String statisTime=DateUtils.getDate();// 统计时间
		sb.append(officeId);
		sb.append(ownerDepart);
		sb.append(statisTime);
		return Digests.string2MD5(sb.toString());
	}

	public void statisWorkRanking(){

		// 工单总数（当天新增）AddWorkNum
		StringBuffer sql1 = new StringBuffer("SELECT  a.owner_depart AS ownerDepart,count(w.id) AS amount  FROM cover a LEFT JOIN cover_work w  ON a.id=w.cover and to_days(w.create_date) = to_days(now())  and a.del_flag='0' group by a.owner_depart");
		List<Map<String, Object>> collectList1 = coverCollectStatisMapper.selectBySql(sql1.toString());

		// 已完成工单总数（当天）
		StringBuffer sql2 = new StringBuffer("SELECT  a.owner_depart AS ownerDepart,count(w.id) AS amount  FROM cover a LEFT JOIN cover_work w  ON a.id=w.cover and to_days(w.create_date) = to_days(now()) and w.life_cycle='complete'  and a.del_flag='0' group by a.owner_depart ");
		List<Map<String, Object>> collectList2 = coverCollectStatisMapper.selectBySql(sql2.toString());


		// 未完成工单总数（累计）
		StringBuffer sql3 = new StringBuffer("SELECT  a.owner_depart AS ownerDepart,count(w.id) AS amount  FROM cover a LEFT JOIN cover_work w  ON a.id=w.cover  and w.life_cycle!='complete'  and a.del_flag='0' group by a.owner_depart");
		List<Map<String, Object>> collectList3 = coverCollectStatisMapper.selectBySql(sql3.toString());

		// 工单总数（累计总共）workNumTotal
		StringBuffer sql4 = new StringBuffer("SELECT  a.owner_depart AS ownerDepart,count(w.id) AS amount  FROM cover a LEFT JOIN cover_work w  ON a.id=w.cover and a.del_flag='0' group by a.owner_depart");
		List<Map<String, Object>> collectList4 = coverCollectStatisMapper.selectBySql(sql4.toString());

		// 已完成工单总数（累计总共）completeWorkNumTotal
		StringBuffer sql5 = new StringBuffer("SELECT  a.owner_depart AS ownerDepart,count(w.id) AS amount  FROM cover a LEFT JOIN cover_work w  ON a.id=w.cover  and w.life_cycle='complete'  and a.del_flag='0' group by a.owner_depart");
		List<Map<String, Object>> collectList5 = coverCollectStatisMapper.selectBySql(sql5.toString());

		List<CoverOfficeOwner> officeOwnerList=coverOfficeOwnerMapper.findList(new CoverOfficeOwner());
		if(CollectionUtil.isNotEmpty(officeOwnerList)){
			for(CoverOfficeOwner officeOwner:officeOwnerList){
				Office office=officeOwner.getOffice();

				if(null!=office){
					String ownerDepart=officeOwner.getOwnerDepart();	// 权属单位
					boolean isNew=true;
					OfficeOwnerStatis statis=null;
					String statisTime= DateUtils.getDate();// 统计时间
					String flag = getInfoFlag(office.getId(),ownerDepart);

					OfficeOwnerStatis query=new OfficeOwnerStatis();
					query.setFlag(flag);
					List<OfficeOwnerStatis> list=officeOwnerStatisMapper.findList(query);
					if (CollectionUtils.isNotEmpty(list)) {//修改
						isNew=false;
					}
					if(isNew){//新增
						statis=new OfficeOwnerStatis();
						statis.setId(IdGen.uuid());
						statis.setIsNewRecord(true);
						statis.setCreateDate(new Date());
						statis.setUpdateDate(new Date());
						statis.setFlag(flag);
						statis.setStatisTime(statisTime);
					}else{//修改
						statis=list.get(0);
						statis.setIsNewRecord(false);
						statis.setUpdateDate(new Date());
					}
					statis.setOfficeId(office.getId());
					statis.setOfficeName(office.getName());
					statis.setOwnerDepart(ownerDepart);
					if(CollectionUtil.isNotEmpty(collectList1)){
						// 工单总数（当天新增）AddWorkNum
						for (Map<String, Object> resultMap:collectList1) {
							String ownerDepart1 = String.valueOf(resultMap.get("ownerDepart"));//权属单位
							String amount = String.valueOf(resultMap.get("amount"));//
							if(StringUtils.isNotEmpty(ownerDepart)&&ownerDepart.equals(ownerDepart1)){
								statis.setAddWorkNum(amount);
							}
						}
					}

					if(CollectionUtil.isNotEmpty(collectList2)){
						// 已完成工单总数（当天）completeWorkNum
						for (Map<String, Object> resultMap:collectList2) {
							String ownerDepart2 = String.valueOf(resultMap.get("ownerDepart"));//权属单位
							String amount = String.valueOf(resultMap.get("amount"));//
							if(StringUtils.isNotEmpty(ownerDepart)&&ownerDepart.equals(ownerDepart2)){
								statis.setCompleteWorkNum(amount);
							}
						}
					}

					if(CollectionUtil.isNotEmpty(collectList3)){
						// 未完成工单总数（累计）proWorkNum
						for (Map<String, Object> resultMap:collectList3) {
							String ownerDepart3 = String.valueOf(resultMap.get("ownerDepart"));//权属单位
							String amount = String.valueOf(resultMap.get("amount"));//
							if(StringUtils.isNotEmpty(ownerDepart)&&ownerDepart.equals(ownerDepart3)){
								statis.setProWorkNum(amount);
							}
						}
					}

					if(CollectionUtil.isNotEmpty(collectList4)){
						// 工单总数（累计总共）workNumTotal
						for (Map<String, Object> resultMap:collectList4) {
							String ownerDepart4 = String.valueOf(resultMap.get("ownerDepart"));//权属单位
							String amount = String.valueOf(resultMap.get("amount"));//
							if(StringUtils.isNotEmpty(ownerDepart)&&ownerDepart.equals(ownerDepart4)){
								statis.setWorkNumTotal(amount);
							}
						}
					}

					if(CollectionUtil.isNotEmpty(collectList5)){
						// 已完成工单总数（累计总共）completeWorkNumTotal
						for (Map<String, Object> resultMap:collectList5) {
							String ownerDepart5 = String.valueOf(resultMap.get("ownerDepart"));//权属单位
							String amount = String.valueOf(resultMap.get("amount"));//
							if(StringUtils.isNotEmpty(ownerDepart)&&ownerDepart.equals(ownerDepart5)){
								statis.setCompleteWorkNumTotal(amount);
							}
						}
					}
					if(isNew) {//新增
						officeOwnerStatisMapper.insert(statis);
					}else{
						officeOwnerStatisMapper.update(statis);
					}

				}

			}
		}

	}


//	public void statisWorkRanking(Office office,String ownerDepart){
//		OfficeOwnerStatis statis=new OfficeOwnerStatis();
//
//		String statisTime= DateUtils.getDate();// 统计时间
//		String flag = getInfoFlag(office.getId(),ownerDepart);
//		statis.setFlag(flag);
//		statis.setStatisTime(statisTime);
//
//		// 工单总数（当天新增）
//		StringBuffer sql1 = new StringBuffer("SELECT  a.owner_depart AS ownerDepart,count(w.id) AS amount  FROM cover a LEFT JOIN cover_work w  ON a.id=w.cover and to_days(w.create_date) = to_days(now())  and a.del_flag='0' ");
//		sql1.append("  and a.owner_depart='").append(ownerDepart).append("'");
//		List<Map<String, Object>> collectList1 = coverCollectStatisMapper.selectBySql(sql1.toString());
//		if(CollectionUtil.isNotEmpty(collectList1)){
//			Map<String, Object> resultMap1=collectList1.get(0);
//			String amount=String.valueOf(resultMap1.get("amount"));//
//			statis.setAddWorkNum(amount);
//		}
//
//		// 已完成工单总数（当天）
//		StringBuffer sql2 = new StringBuffer("SELECT  a.owner_depart AS ownerDepart,count(w.id) AS amount  FROM cover a LEFT JOIN cover_work w  ON a.id=w.cover and to_days(w.create_date) = to_days(now()) and w.life_cycle='complete'  and a.del_flag='0' ");
//		sql2.append("  and a.owner_depart='").append(ownerDepart).append("'");
//		List<Map<String, Object>> collectList2 = coverCollectStatisMapper.selectBySql(sql2.toString());
//		if(CollectionUtil.isNotEmpty(collectList2)){
//			Map<String, Object> resultMap1=collectList2.get(0);
//			String amount=String.valueOf(resultMap1.get("amount"));//
//			statis.setCompleteWorkNum(amount);
//		}
//
//
//		// 未完成工单总数（累计）
//		StringBuffer sql8 = new StringBuffer("SELECT  a.cover_type AS coverType,a.district AS district,a.owner_depart AS ownerDepart,count(w.id) AS amount  FROM cover a LEFT JOIN cover_work w  ON a.id=w.cover  and w.life_cycle!='complete'  and a.del_flag='0' group by a.cover_type,a.district,a.owner_depart");
//		statisDataBySQL(sql8.toString(), "proWorkNum");
//
//		// 工单总数（累计总共）
//		StringBuffer sql9 = new StringBuffer("SELECT  a.cover_type AS coverType,a.district AS district,a.owner_depart AS ownerDepart,count(w.id) AS amount  FROM cover a LEFT JOIN cover_work w  ON a.id=w.cover and a.del_flag='0' group by a.cover_type,a.district,a.owner_depart");
//		statisDataBySQL(sql9.toString(), "workNumTotal");
//
//		// 已完成工单总数（累计总共）
//		StringBuffer sql10 = new StringBuffer("SELECT  a.cover_type AS coverType,a.district AS district,a.owner_depart AS ownerDepart,count(w.id) AS amount  FROM cover a LEFT JOIN cover_work w  ON a.id=w.cover  and w.life_cycle='complete'  and a.del_flag='0' group by a.cover_type,a.district,a.owner_depart");
//		statisDataBySQL(sql10.toString(), "completeWorkNumTotal");
//
//
//	}

}