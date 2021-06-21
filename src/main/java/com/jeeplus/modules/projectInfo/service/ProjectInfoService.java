/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.projectInfo.service;

import java.util.List;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.Collections3;
import com.jeeplus.common.utils.collection.CollectionUtil;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.flow.service.base.FlowProcService;
import com.jeeplus.modules.flow.service.base.FlowStateService;
import com.jeeplus.modules.flow.service.opt.FlowOptService;
import com.jeeplus.modules.projectInfo.mapper.ProjectInfoMapper;
import com.jeeplus.modules.sys.constant.RoleConstant;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.DictTypeService;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemService;
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
    private SystemService systemService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private DictTypeService dictTypeService;
    @Autowired
    private FlowOptService flowOptService;//工单流程操作定义
    @Autowired
    private FlowProcService flowProcService;//工单流程定义
    @Autowired
    private FlowStateService flowStateService;//工单流程状态

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
            //创建项目内管理用户
            saveProjectUser(projectInfo,office);
            //生成新的项目
            projectInfo.setOffice(office);
            this.save(projectInfo);
            //关联新部门对应的项目
            office.setProjectId(projectInfo.getId());
            office.setProjectName(projectInfo.getProjectName());
			officeService.save(office);
            //创建子部门
            officeService.createDownOffice(office);
            synStandardProject(projectInfo);
		//编辑表单保存
        } else {
            this.save(projectInfo);//保存
        }
    }

    /**
     *
     * @param projectInfo 新增项目
     */
    public void synStandardProject(ProjectInfo projectInfo){
        String standardProjectNo = Global.getConfig("standard.project.no");
        ProjectInfo queryProjectInfo=new ProjectInfo();
        queryProjectInfo.setProjectNo(standardProjectNo);
        List<ProjectInfo> projectList=super.findList(queryProjectInfo);
        if(CollectionUtil.isNotEmpty(projectList)){
          String  standardProjectId= projectList.get(0).getId();
            // 字典值同步
            dictTypeService.synData(standardProjectId, projectInfo);
            flowProcService.synData(standardProjectId, projectInfo);//工单流程定义
            flowStateService.synData(standardProjectId, projectInfo);//工单流程状态
            flowOptService.synData(standardProjectId, projectInfo);//工单流程操作定义
        }

    }

    public User saveProjectUser(ProjectInfo projectInfo,Office office){
        User user=new User();
        user.setOffice(office);
        user.setLoginFlag(CodeConstant.on_off.one);
        Role role=systemService.getRoleByEnname(RoleConstant.RoleType.manage);
        List<Role> roleList = Lists.newArrayList();
        roleList.add(role);
        user.setRoleList(roleList);
        user.setRolesName(Collections3.extractToString(roleList, "name", ","));
        user.setRole(role);
        user.setLoginName(projectInfo.getLoginName());
        user.setPassword(SystemService.entryptPassword(projectInfo.getLoginName()));
        user.setName(office.getName());
        user.setPhone(projectInfo.getCustomerPhone());
        user.setMobile(projectInfo.getCustomerPhone());
        // 保存用户信息
        systemService.saveUser(user);
        return  user;
    }


}