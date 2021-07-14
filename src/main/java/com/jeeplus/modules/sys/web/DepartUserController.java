/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.Collections3;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.jeeplus.modules.sys.entity.User;


/**
 * 系统用户信息Controller
 * @author crj
 * @version 2019-04-10
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/departUser")
public class DepartUserController extends BaseController {

	@Autowired
	private SystemService systemService;
	@Autowired
	private UserMapper userMapper;
	
	@ModelAttribute
	public User get(@RequestParam(required=false) String id) {
		User entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = systemService.getUser(id);
		}
		if (entity == null){
			entity = new User();
		}
		return entity;
	}
	
	/**
	 * 系统用户信息列表页面
	 */
	@RequiresPermissions("sys:departUser:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/sys/user/departUserList";
	}
	
		/**
	 * 系统用户信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("sys:departUser:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(User departUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<User> page = systemService.findUser(new Page<User>(request, response), departUser);
		return getBootstrapData(page);
	}


	/**
	 * 查看，增加，编辑系统用户信息表单页面
	 */
	@RequiresPermissions(value={"sys:departUser:view","sys:departUser:add","sys:departUser:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(User departUser, Model model) {
		model.addAttribute("departUser", departUser);
		model.addAttribute("allRoles", systemService.findAllRole());
		return "modules/sys/user/departUserForm";
	}

	/**
	 * 保存系统用户信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"sys:departUser:add","sys:departUser:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(User user, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (user.getCompany()==null || user.getCompany().getId()==null){
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice()==null || user.getOffice().getId()==null){
			user.setOffice(UserUtils.getUser().getOffice());
		}
		AjaxJson j = new AjaxJson();
//		if (!beanValidator(model, departUser)){
//			j.setSuccess(false);
//			j.setMsg("非法参数！");
//			return j;
//		}
		user.setLoginFlag(CodeConstant.on_off.one);//是否允许登录.新版平台默认允许登录
		// 岗位数据有效性验证，过滤不在授权内的岗位
		List<Role> roleList = Lists.newArrayList();
		List<String> roleIdList = user.getRoleIdList();
		for (Role r : systemService.findAllRole()){
			if (roleIdList.contains(r.getId())){
				roleList.add(r);
			}
		}
		user.setOffice(UserUtils.getUser().getOffice());
		user.setRoleList(roleList);
		user.setRolesName(Collections3.extractToString(roleList, "name", ","));
		systemService.saveUser(user);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存系统用户信息成功");
		return j;
	}
	
	/**
	 * 删除系统用户信息
	 */
	@ResponseBody
	@RequiresPermissions("sys:departUser:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(User departUser, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		systemService.deleteUser(departUser);
		j.setMsg("删除系统用户信息成功");
		return j;
	}
	
	/**
	 * 批量删除系统用户信息
	 */
	@ResponseBody
	@RequiresPermissions("sys:departUser:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			systemService.deleteUser(systemService.getUser(id));
		}
		j.setMsg("删除系统用户信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("sys:departUser:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(User departUser, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "系统用户信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<User> page = systemService.findUser(new Page<User>(request, response, -1), departUser);
    		new ExportExcel("系统用户信息", User.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出系统用户信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:departUser:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<User> list = ei.getDataList(User.class);
			for (User departUser : list){
				try{
					systemService.saveUser(departUser);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条系统用户信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条系统用户信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入系统用户信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/user/departUser/?repage";
    }
	
	/**
	 * 下载导入系统用户信息数据模板
	 */
	@RequiresPermissions("sys:departUser:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "系统用户信息数据导入模板.xlsx";
    		List<User> list = Lists.newArrayList();
    		new ExportExcel("系统用户信息数据", User.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/user/departUser/?repage";
    }

	/**
	 * 启用 1是，0否
	 * @param departUser
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("sys:departUser:enable")
	@RequestMapping(value = "enable")
	public AjaxJson enable(User departUser, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try{

		User user=systemService.getUser(departUser.getId());
		user.setLoginFlag(CodeConstant.on_off.one);
		systemService.saveUser(user);
			j.setSuccess(true);
			j.setMsg("启用成功!");
		}catch (Exception e){
			j.setSuccess(false);
			j.setMsg("启用失败:"+e.getMessage());
		}
		return j;
	}

	/**
	 * 禁用 1是，0否
	 * @param departUser
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("sys:departUser:disable")
	@RequestMapping(value = "disable")
	public AjaxJson disable(User departUser, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try{

			User user=systemService.getUser(departUser.getId());
			user.setLoginFlag(CodeConstant.on_off.zero);
			systemService.saveUser(user);
			j.setSuccess(true);
			j.setMsg("禁用成功!");
		}catch (Exception e){
			j.setSuccess(false);
			j.setMsg("禁用失败:"+e.getMessage());
		}
		return j;
	}
}