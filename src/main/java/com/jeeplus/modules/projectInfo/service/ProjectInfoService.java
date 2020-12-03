/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.projectInfo.service;

import java.util.List;

import com.jeeplus.modules.projectInfo.mapper.ProjectInfoMapper;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.projectInfo.entity.ProjectInfo;


/**
 * 项目管理Service
 *
 * @author Peter
 * @version 2020-12-02
 */
@Service
@Transactional(readOnly = true)
public class ProjectInfoService extends CrudService<ProjectInfoMapper, ProjectInfo> {

    @Autowired
    private OfficeService officeService;


    public ProjectInfo get(String id) {
        return super.get(id);
    }

    public List<ProjectInfo> findList(ProjectInfo projectInfo) {
        return super.findList(projectInfo);
    }

    public Page<ProjectInfo> findPage(Page<ProjectInfo> page, ProjectInfo projectInfo) {
        return super.findPage(page, projectInfo);
    }

    @Transactional(readOnly = false)
    public void save(ProjectInfo projectInfo) {
        super.save(projectInfo);
    }

    @Transactional(readOnly = false)
    public void delete(ProjectInfo projectInfo) {
        super.delete(projectInfo);
    }


    @Transactional(readOnly = false)
    public void saveProject(ProjectInfo projectInfo) throws Exception {
		//新增项目
        if (projectInfo.getIsNewRecord()) {
            //生成新的部门
            Office office = officeService.createProjectOffice(projectInfo, UserUtils.getUser());
            //生成新的项目
            projectInfo.setOffice(office);
            this.save(projectInfo);
            //关联新部门对应的项目
            office.setProjectId(projectInfo.getId());
            office.setProjectName(projectInfo.getProjectName());
			officeService.save(office);
		//编辑表单保存
        } else {
            this.save(projectInfo);//保存
        }
    }


}