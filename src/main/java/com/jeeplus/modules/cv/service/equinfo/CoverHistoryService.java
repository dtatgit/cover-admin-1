/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.equinfo;

import java.util.List;

import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.equinfo.CoverHistory;
import com.jeeplus.modules.cv.mapper.equinfo.CoverHistoryMapper;

/**
 * 井盖历史记录Service
 * @author crj
 * @version 2019-05-15
 */
@Service
@Transactional(readOnly = true)
public class CoverHistoryService extends CrudService<CoverHistoryMapper, CoverHistory> {
	@Autowired
	private UserMapper userMapper;
	public CoverHistory get(String id) {
		return super.get(id);
	}
	
	public List<CoverHistory> findList(CoverHistory coverHistory) {
		return super.findList(coverHistory);
	}
	
	public Page<CoverHistory> findPage(Page<CoverHistory> page, CoverHistory coverHistory) {
		//return super.findPage(page, coverHistory);
		Page<CoverHistory> pageValue=super.findPage(page, coverHistory);
		List<CoverHistory> list=pageValue.getList();
		if(null!=list&&list.size()>0){
			for(CoverHistory c:list){
				User oldUser = userMapper.get(c.getCreateBy());
				c.setCreateBy(oldUser);
			}
		}
		return pageValue;
	}
	
	@Transactional(readOnly = false)
	public void save(CoverHistory coverHistory) {
		super.save(coverHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverHistory coverHistory) {
		super.delete(coverHistory);
	}
	
}