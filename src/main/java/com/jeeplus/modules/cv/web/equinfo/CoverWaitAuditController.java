/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.web.equinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.collection.CollectionUtil;
import com.jeeplus.common.utils.collection.MapUtil;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.CoverImage;
import com.jeeplus.modules.cv.service.equinfo.CoverDamageService;
import com.jeeplus.modules.cv.service.equinfo.CoverImageService;
import com.jeeplus.modules.cv.service.statis.CoverCollectStatisService;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.service.equinfo.CoverService;

/**
 * 井盖基础信息Controller
 * @author crj
 * @version 2019-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/equinfo/coverWaitAudit")
public class CoverWaitAuditController extends BaseController {

    @Autowired
    private CoverService coverService;
    @Autowired
    private CoverCollectStatisService coverCollectStatisService;
    @Autowired
    private CoverImageService coverImageService;
    @Autowired
    private CoverDamageService coverDamageService;


    @ModelAttribute
    public Cover get(@RequestParam(required=false) String id) {
        Cover entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = coverService.get(id);
        }
        if (entity == null){
            entity = new Cover();
        }
        return entity;
    }

    /**
     * 井盖基础信息列表页面
     */
    @RequiresPermissions("cv:equinfo:coverWaitAudit:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cv/equinfo/coverWaitAudit";
    }

    /**
     * 井盖基础信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("cv:equinfo:coverWaitAudit:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(Cover cover, HttpServletRequest request, HttpServletResponse response, Model model) {
        cover.setCoverStatus(CodeConstant.COVER_STATUS.WAIT_AUDIT);//只展示待审核的数据
        Page<Cover> page = coverService.findPage(new Page<Cover>(request, response), cover);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑井盖基础信息表单页面
     */
    @RequiresPermissions(value={"cv:equinfo:coverWaitAudit:view","cv:equinfo:coverWaitAudit:add","cv:equinfo:coverWaitAudit:edit"},logical=Logical.OR)
    @RequestMapping(value = "form")
    public String form(Cover cover, Model model) {
        model.addAttribute("cover", cover);
        return "modules/cv/equinfo/coverForm";
    }

    /**
     * 查看，增加，编辑井盖基础信息表单页面
     */
    @RequiresPermissions(value={"cv:equinfo:cover:view","cv:equinfo:cover:add","cv:equinfo:cover:edit"},logical=Logical.OR)
    @RequestMapping(value = "view")
    public String view(Cover cover, Model model) {
        cover=coverService.get(cover.getId());
        model.addAttribute("cover", cover);
        return "modules/cv/equinfo/coverView";
    }







}