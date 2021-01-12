/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.equinfo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.collection.CollectionUtil;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.sys.entity.SystemConfig;
import com.jeeplus.modules.sys.service.SystemConfigService;
import com.jeeplus.modules.sys.utils.UserUtils;
import net.oschina.j2cache.ehcache.EhCacheProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.equinfo.CoverImage;
import com.jeeplus.modules.cv.mapper.equinfo.CoverImageMapper;

/**
 * 井盖图片信息Service
 *
 * @author crj
 * @version 2019-04-28
 */
@Service
@Transactional
public class CoverImageService extends CrudService<CoverImageMapper, CoverImage> {
    private final static Logger logger = LoggerFactory.getLogger(CoverImageService.class);

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private CoverService coverService;

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

    public List<CoverImage> obtainImage(String coverId) {
        CoverImage coverImage = new CoverImage();
        coverImage.setCoverId(coverId);
        List<CoverImage> imageList = super.findList(coverImage);
        if (null != imageList && imageList.size() > 0) {
            for (CoverImage image : imageList) {
                String uploadid = image.getUploadid();
                SystemConfig entity = systemConfigService.get("1");
                if (null != entity) {
                    StringBuffer sb = new StringBuffer(entity.getUrl());
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


    public boolean cloneCoverImage(Cover sourceCover, Cover targetCover) {
        try {
            //查找井盖对应的图片
            CoverImage param = new CoverImage();
            param.setCoverId(sourceCover.getId());
            List<CoverImage> imageList = super.findList(param);
            if (CollectionUtil.isNotEmpty(imageList)) {
                for (CoverImage coverImage : imageList) {
                    CoverImage obj = new CoverImage();
                    BeanUtils.copyProperties(coverImage, obj);
                    obj.setId(IdGen.uuid());
                    obj.setCoverId(targetCover.getId());
                    obj.setIsNewRecord(true);
                    this.save(obj);
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("上传井盖克隆图片失败：" + e.getMessage());
            return false;
        }
    }


    public boolean cloneCoverImage(String coverId, String imageInfoStr) {
        try {
            if (StringUtils.isNotBlank(coverId) && StringUtils.isNotBlank(imageInfoStr)) {
                //多个图片信息逗号分隔
                List<String> infos = Arrays.asList(imageInfoStr.split(","));
                for (String info : infos) {
                    CoverImage coverImage = new CoverImage();
                    coverImage.setId(IdGen.uuid());
                    coverImage.setCoverId(coverId);
                    coverImage.setIsNewRecord(true);
                    coverImage.setUploadBy(UserUtils.getUser().getId());
					coverImage.setUploadDate(new Date());
                    String[] imageInfo = info.split(";");
                    //按照 "uploadId-url-status"解析
                    if (imageInfo != null) {
                        coverImage.setUploadid(imageInfo[0]);
                        coverImage.setUrl(imageInfo[1]);
                        coverImage.setStatus(imageInfo[2]);
                    }
                    this.save(coverImage);
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("上传井盖克隆图片失败（cloneCoverImage）：" + e.getMessage());
            return false;
        }
    }

}