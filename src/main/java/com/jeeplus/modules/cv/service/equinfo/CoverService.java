/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.equinfo;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cb.mapper.work.CoverWorkMapper;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.*;
import com.jeeplus.modules.cv.mapper.equinfo.CoverDamageMapper;
import com.jeeplus.modules.cv.mapper.equinfo.CoverHistoryMapper;
import com.jeeplus.modules.cv.mapper.equinfo.CoverMapper;
import com.jeeplus.modules.cv.mapper.equinfo.CoverOwnerMapper;
import com.jeeplus.modules.cv.service.task.CoverTaskProcessService;
import com.jeeplus.modules.cv.utils.EntityUtils;
import com.jeeplus.modules.cv.vo.CountVo;
import com.jeeplus.modules.projectInfo.entity.ProjectInfo;
import com.jeeplus.modules.projectInfo.service.ProjectInfoService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 井盖基础信息Service
 * @author crj
 * @version 2019-04-19
 */
@Service
@Transactional(readOnly = true)
public class CoverService extends CrudService<CoverMapper, Cover> {
	@Autowired
	private CoverImageService coverImageService;
	@Autowired
	private CoverDamageService coverDamageService;
	@Autowired
	private CoverOwnerService coverOwnerService;
	@Autowired
	private CoverDamageMapper coverDamageMapper;
	@Autowired
	private CoverOwnerMapper coverOwnerMapper;
	@Autowired
	private CoverHistoryMapper coverHistoryMapper;
	@Autowired
	private CoverTaskProcessService coverTaskProcessService;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CoverMapper coverMapper;
	@Autowired
	private CoverWorkMapper coverWorkMapper;

	@Autowired
	private CoverAuditService auditService;

	@Autowired
	private ProjectInfoService projectInfoService;
	public Cover get(String id) {
		Cover cover = super.get(id);
		if (cover != null) {
			cover.setCoverImageList(coverImageService.obtainImage(id));
			cover.setCoverDamageList(coverDamageService.obtainDamage(id));
			cover.setCoverOwnerList(coverOwnerService.obtainOwner(id));

//			cover.setCoverDamageList(coverDamageMapper.findList(new CoverDamage(cover)));
//			cover.setCoverOwnerList(coverOwnerMapper.findList(new CoverOwner(cover)));
		}
		return cover;
	}
	
	public List<Cover> findList(Cover cover) {
		return super.findList(cover);
	}

	/**
	 * 代理商  绑定了 井卫的  井盖
	 * @param cover
	 * @param subOfficeId
	 * @return
	 */
	public List<Cover> findListNew(String coverStatus,String subOfficeId){
		return mapper.findListNew(coverStatus,subOfficeId);
	}

	public List<Cover> findList2(Cover cover) {
		return coverMapper.findList(cover);
	}

	public List<Cover> findAllCovers(Cover cover) {
		return coverMapper.findAllCovers(cover);
	}
	
	public Page<Cover> findPage(Page<Cover> page, Cover cover) {
		Page<Cover> pageValue=super.findPage(page, cover);
		List<Cover> list=pageValue.getList();
		if(null!=list&&list.size()>0){
			for(Cover c:list){
				User oldUser = userMapper.get(c.getCreateBy());
				c.setCreateBy(oldUser);

				//获取未完成的工单
				CoverWork byCoverId = coverWorkMapper.getByCoverId(c.getId());
				c.setCoverWork(byCoverId);
			}
		}
		//return super.findPage(page, cover);
		return pageValue;
	}
	
	@Transactional(readOnly = false)
	public void save(Cover cover) {
		super.save(cover);
	}


	
	@Transactional(readOnly = false)
	public void delete(Cover cover) {
		super.delete(cover);
	}


	@Transactional(readOnly = false)
	public void repairSave(Cover cover,String source) {
		super.save(cover);
		CoverHistory coverHistory= EntityUtils.copyData(cover,CoverHistory.class);
		//coverHistory.setId(IdGen.uuid());
		coverHistory.setCoverId(cover.getId());
		coverHistory.preInsert();
		StringBuffer damageSB=new StringBuffer();
		StringBuffer ownerSB=new StringBuffer();
		for (CoverDamage coverDamage : cover.getCoverDamageList()){
			if (coverDamage.getId() == null){
				continue;
			}
			damageSB.append(coverDamage.getDamage()).append(",");
			if (CoverDamage.DEL_FLAG_NORMAL.equals(coverDamage.getDelFlag())){
				if (StringUtils.isBlank(coverDamage.getId())){
					coverDamage.setCoverId(cover.getId());
					coverDamage.preInsert();
					coverDamage.setStatus("normal");
					coverDamageMapper.insert(coverDamage);
				}else{
					coverDamage.preUpdate();
					coverDamageMapper.update(coverDamage);
				}

			}else{
				coverDamageMapper.delete(coverDamage);
			}
		}
		for (CoverOwner coverOwner : cover.getCoverOwnerList()){
			if (coverOwner.getId() == null){
				continue;
			}
			ownerSB.append(coverOwner.getOwnerName()).append(",");
			if (CoverOwner.DEL_FLAG_NORMAL.equals(coverOwner.getDelFlag())){
				if (StringUtils.isBlank(coverOwner.getId())){
					coverOwner.setCoverId(cover.getId());
					coverOwner.preInsert();
					coverOwner.setOwnerType("org");
					coverOwner.setProjectId(cover.getProjectId());
					coverOwner.setProjectName(cover.getProjectName());
					coverOwnerMapper.insert(coverOwner);
				}else{
					coverOwner.preUpdate();
					coverOwnerMapper.update(coverOwner);
				}
			}else{
				coverOwnerMapper.delete(coverOwner);
			}
		}
		String damageStr=damageSB.toString();
		String ownerStr=ownerSB.toString();
		if(StringUtils.isNotEmpty(damageStr)){
			coverHistory.setCoverDamage(damageStr.substring(0, damageStr.length()-1));
		}
		if(StringUtils.isNotEmpty(ownerStr)){
			coverHistory.setCoverOwner(ownerStr.substring(0, ownerStr.length()-1));
		}

		if(StringUtils.isNotEmpty(source)&&source.equals(CodeConstant.SOURCE.TASK)){
			//增加任务明细
			coverHistory.setSource(cover.getCoverTaskProcessId());
		}
		coverHistoryMapper.insert(coverHistory);
		//来源于任务处理中心
		if(StringUtils.isNotEmpty(source)&&source.equals(CodeConstant.SOURCE.TASK)){


			coverTaskProcessService.taskProcessComplete(cover);
		}

	}

	public  List<Cover>  obtainCoverList(String sqlValue){
		List<Cover> coverList=new ArrayList<Cover>();
		List<Map<String, Object>> coverListMap = coverMapper.selectBySql(sqlValue);
		if(null!=coverListMap&&coverListMap.size()>0){
			for(int i=0;i<coverListMap.size();i++){
				Map<String, Object> map=coverListMap.get(i);
				String coverId=String.valueOf(map.get("id"));
				//Cover cover=super.get(coverId);
				Cover cover=new Cover();
				cover.setId(coverId);
				coverList.add(cover);
			}
		}
		return coverList;
	}

	/**
	 * <!--根据id修改安装工单状态-->
	 * @param id
	 * @param state    <option value="N">未安装</option>
	 *                 <option value="Z">安装中</option>
	 *                 <option value="Y">已安装</option>
	 */
	@Transactional(readOnly = false)
	public void updateGwoById(String id,String state){
		mapper.updateGwoById(id,state);
	}

	@Transactional(readOnly = false)
	public void updateWorkStatus(String id,String state){
		mapper.updateWorkStatus(id,state);
	}

	@Transactional(readOnly = false)
	public void updateStatus(String id,String status){
		mapper.updateStatus(id,status);
	}


	@Transactional(readOnly = false)
	public void updateJcStatus(String id,String status){
		mapper.updateJcStatus(id,status);
	}

	public List<Cover> checkFindList(Cover cover) {
		dataRuleFilter(cover);
		return mapper.checkFindList(cover);
	}

	public void cloneCoverForProject(String coverNo,String projectNo){
		ProjectInfo project=null;
		if(StringUtils.isNotEmpty(projectNo)){
			ProjectInfo projectInfo=new ProjectInfo();
			projectInfo.setProjectNo(projectNo);
			List<ProjectInfo> projectList=projectInfoService.findList(projectInfo);
			if(null!=projectList&&projectList.size()>0){
				project=projectList.get(0);
			}
		}

		Cover query=new Cover();
		query.setNo(coverNo);
		List<Cover> coverList=mapper.checkFindList(query);
		if(null!=coverList&&coverList.size()>0&&null!=project){
			Cover cover=coverList.get(0);//克隆井盖数据到该项目
			cover.setId(IdGen.uuid());
			cover.setIsNewRecord(true);
			cover.setProjectId(project.getId());
			cover.setProjectName(project.getProjectName());
			super.save(cover);
		}
	}

	/**
	 * 根据状态 查询 数量
	 * @param status
	 * @return
	 */
	public int selectCountOfStatus(String status){
		return mapper.selectCountOfStatus(status);
	}


	/**
	 * 根据状态和创建人 查询 数量   （不过滤项目）
	 * @param status
	 * @param userId
	 * @return
	 */
	public int countByStatusAndUserIdNoFilter(String status,String userId){
		return mapper.countByStatusAndUserIdNoFilter(status,userId);
	}

	public int countByIsDamaged(String isdamaged){
		return mapper.countByIsDamaged(isdamaged);
	}

	public List<CountVo> countSql(){
		return mapper.countSql();
	}

	/**
	 * 井盖审核
	 * @param status
	 * @param audit
	 * @param coverId
	 */
	@Transactional(readOnly = false)
	public void audit(CoverAudit audit, String coverId, String status) {

		//审核记录
		auditService.save(audit);

		//修改井盖状态
		this.updateStatus(coverId,status);
	}


	/**
	 * 代理商 井盖统计
	 * @param status
	 * @param subOfficeId
	 * @return
	 */
	public int coverCountAgent(String status,String subOfficeId){
		return mapper.coverCountAgent(status,subOfficeId);
	}

	/**
	 * 代理商 井盖统计
	 * @param subOfficeId
	 * @return
	 */
	public int coverBellCountAgent(String subOfficeId){
		return mapper.coverBellCountAgent(subOfficeId);
	}
}