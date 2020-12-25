/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.equinfo;

import java.util.List;

import com.jeeplus.modules.sys.entity.SystemConfig;
import com.jeeplus.modules.sys.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.equinfo.CoverImage;
import com.jeeplus.modules.cv.mapper.equinfo.CoverImageMapper;

/**
 * 井盖图片信息Service
 * @author crj
 * @version 2019-04-28
 */
@Service
@Transactional(readOnly = true)
public class CoverImageService extends CrudService<CoverImageMapper, CoverImage> {
	@Autowired
	private SystemConfigService systemConfigService;
	public CoverImage get(String id) {
		return super.get(id);
	}
	
	public List<CoverImage> findList(CoverImage coverImage) {
		return super.findList(coverImage);
	}
	
	public Page<CoverImage> findPage(Page<CoverImage> page, CoverImage coverImage) {
		return super.findPage(page, coverImage);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverImage coverImage) {
		super.save(coverImage);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverImage coverImage) {
		super.delete(coverImage);
	}

	public List<CoverImage> obtainImage(String coverId){
		CoverImage coverImage=new CoverImage();
		coverImage.setCoverId(coverId);
		List<CoverImage> imageList=super.findList(coverImage);
		if(null!=imageList&&imageList.size()>0){
			for(CoverImage image:imageList){
				String uploadid=image.getUploadid();
				SystemConfig entity = systemConfigService.get("1");
				if(null!=entity){
					StringBuffer sb=new StringBuffer(entity.getUrl());
					sb.append(uploadid);
					image.setUrl(sb.toString());
				}

//				StringBuffer sb=new StringBuffer("http://123.58.240.194:9002/cover-gather-service/sys/file/download/");
//				sb.append(uploadid);
//				image.setUrl(sb.toString());
			}
		}
		return imageList;
	}

	
}