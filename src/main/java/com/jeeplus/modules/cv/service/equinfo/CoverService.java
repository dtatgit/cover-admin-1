/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.equinfo;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.entity.equinfo.CoverDamage;
import com.jeeplus.modules.cv.entity.equinfo.CoverHistory;
import com.jeeplus.modules.cv.entity.equinfo.CoverOwner;
import com.jeeplus.modules.cv.mapper.equinfo.CoverDamageMapper;
import com.jeeplus.modules.cv.mapper.equinfo.CoverHistoryMapper;
import com.jeeplus.modules.cv.mapper.equinfo.CoverMapper;
import com.jeeplus.modules.cv.mapper.equinfo.CoverOwnerMapper;
import com.jeeplus.modules.cv.service.task.CoverTaskProcessService;
import com.jeeplus.modules.cv.utils.EntityUtils;
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
	
	public Page<Cover> findPage(Page<Cover> page, Cover cover) {
		Page<Cover> pageValue=super.findPage(page, cover);
		List<Cover> list=pageValue.getList();
		if(null!=list&&list.size()>0){
			for(Cover c:list){
				User oldUser = userMapper.get(c.getCreateBy());
				c.setCreateBy(oldUser);
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
}