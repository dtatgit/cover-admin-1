/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.equinfo;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.api.constant.Constants;
import com.jeeplus.modules.api.pojo.DeviceInfo;
import com.jeeplus.modules.api.pojo.DeviceSimpleParam;
import com.jeeplus.modules.api.pojo.Result;
import com.jeeplus.modules.api.service.DeviceService;
import com.jeeplus.modules.api.utils.bellUtils;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.mapper.equinfo.CoverBellMapper;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 井卫设备信息Service
 * @author crj
 * @version 2019-06-24
 */
@Service
@Transactional(readOnly = true)
public class CoverBellService extends CrudService<CoverBellMapper, CoverBell> {
	@Autowired
	private CoverService coverService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private CoverBellMapper coverBellMapper;


	public CoverBell get(String id) {
		CoverBell bell=null;
		if(StringUtils.isNotEmpty(id)){
			 bell=super.get(id);
			if(StringUtils.isNotEmpty(bell.getCoverId())){
				bell.setCover(coverService.get(bell.getCoverId()));
			}
		}

		return bell;
	}
	
	public List<CoverBell> findList(CoverBell coverBell) {
		return super.findList(coverBell);
	}
	
	public Page<CoverBell> findPage(Page<CoverBell> page, CoverBell coverBell) {
		return super.findPage(page, coverBell);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverBell coverBell) {
		Cover cover=coverBell.getCover();
		if(null!=cover){
			coverBell.setCoverId(cover.getId());
			coverBell.setCoverNo(cover.getNo());
		}
		String projectId= UserUtils.getProjectId();//获取当前登录用户的所属项目
		String projectName= UserUtils.getProjectName();//获取当前登录用户的所属项目
		if(com.jeeplus.common.utils.StringUtils.isNotEmpty(projectId)) {
			coverBell.setProjectId(projectId);
		}
		if(com.jeeplus.common.utils.StringUtils.isNotEmpty(projectName)) {
			coverBell.setProjectName(projectName);
		}
		super.save(coverBell);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverBell coverBell) {
		super.delete(coverBell);
	}

	/**
	 * 井卫设备设防撤防操作
	 * @param coverBell
	 * @param defenseStatus
	 * @return
	 */
	@Transactional(readOnly = false)
	public  Result  setDefense(CoverBell coverBell,String defenseStatus){
		Result result = deviceService.setDefense(coverBell.getBellNo(), defenseStatus);
		if(null!=result){
			String success=result.getSuccess();
			if(StringUtils.isNotEmpty(success)&&success.equals("true")){
				coverBell.setDefenseStatus(defenseStatus);
				super.save(coverBell);
			}
		}

		return result;
	}

	/**
	 * 井卫报废操作
	 * @param coverBell
	 * @param defenseStatus
	 * @return
	 */
	@Transactional(readOnly = false)
	public  Result  setScrap(CoverBell coverBell,String defenseStatus){
		Result result = deviceService.deviceScrap(coverBell.getBellNo());
		if(null!=result){
			String success=result.getSuccess();
			if(StringUtils.isNotEmpty(success)&&success.equals("true")){
				coverBell.setBellStatus(CodeConstant.BELL_STATUS.scrap);
				super.save(coverBell);
			}
		}

		return result;
	}

	/**
	 * 设备上下线
	 * @param deviceId
	 * @return
	 */
	@Transactional(readOnly = false)
	public String processWorkStatus(String deviceId,String workStatus) {
		String retMsg = "";
		try{
			CoverBell bell=super.findUniqueByProperty("a.bell_no", deviceId);
			if(null==bell){

			//注册设备
				//井卫数据为空，则自动注册
				//DeviceResult deviceResult= deviceService.getDeviceInfo(deviceId);
				DeviceInfo deviceInfo= deviceService.getDeviceInfo2(deviceId);  //update by ffy  从上面的方法改成这里,获取设备基础信息（包括imei和iccid）
				bell=new CoverBell();
				bell.setBellNo(deviceId);//设备编号

				bell.setDefenseStatus(CodeConstant.DEFENSE_STATUS.REVOKE);  //先给默认撤防状态，如果能查到数据，则会修改
				if(deviceInfo!=null){
					bell.setBellType(deviceInfo.getDeviceType());//设备类型
					bell.setVersion(deviceInfo.getVersion());//版本号
					bell.setImei(deviceInfo.getImei());   //add by ffy
					bell.setSim(deviceInfo.getIccid());   //add by ffy
					bell.setDefenseStatus(bellUtils.changeDefenseStatus(deviceInfo.getFortifyState()));//设防状态
				}
				bell.setWorkStatus(workStatus);// 工作状态
				bell.setBellStatus(CodeConstant.BELL_STATUS.init);// 生命周期

				//再次验证设备已经存在，imei必须唯一
				CoverBell query=new CoverBell();
				if(StringUtils.isNotBlank(bell.getImei())) {
					query.setImei(bell.getImei());
				}
				List<CoverBell> bellList=checkFindList(query);
				if(null!=bellList&&bellList.size()>0){
					return "imei已经存在";
				}
			}else{
				bell.setWorkStatus(workStatus);// 工作状态
			}

			super.save(bell);

		//设备上线
		if(StringUtils.isNotEmpty(workStatus)&&workStatus.equals(CodeConstant.BELL_WORK_STATUS.ON)){
			logger.info("#####################设备上线######################"+deviceId);

		//设备下线
		}else if(StringUtils.isNotEmpty(workStatus)&&workStatus.equals(CodeConstant.BELL_WORK_STATUS.OFF)){
			logger.info("#####################设备离线######################"+deviceId);
		}
		retMsg = Constants.MSG.SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			retMsg = Constants.MSG.FAIL;
		}

		return retMsg;
	}


	/**
	 * 井卫解绑操作
	 * @param coverBell
	 * @return
	 */
	@Transactional(readOnly = false)
	public  boolean  untying(CoverBell coverBell){
		boolean flag=false;
		try {
			if (null != coverBell) {
				//井盖信息不为空，进行解绑操作
				if (StringUtils.isNotEmpty(coverBell.getCoverId())) {
					//1.把井盖安装工单状态改为未安装
					coverService.updateGwoById(coverBell.getCoverId(), CodeConstant.cover_gwo.not_install);
					//2.解绑
					coverBell.setCoverId("");
					coverBell.setCoverNo("");
					coverBell.setDefenseStatus(CodeConstant.DEFENSE_STATUS.REVOKE);//设防状态 改撤防
					coverBell.setBellStatus(CodeConstant.BELL_STATUS.notinstalled);//改未安装
					super.save(coverBell);
					flag=true;
				}

			}
		}catch (Exception e){
			e.printStackTrace();
			flag=false;
		}

		return flag;
	}

	@Transactional(readOnly = false)
	public void updateByDevNo(DeviceSimpleParam deviceSimpleParam){
		mapper.updateByDevNo(deviceSimpleParam);
	}

	public int selCountByDevNo(String devNo){
		return mapper.selCountByDevNo(devNo);
	}

	public CoverBell queryCoverBell(Map<String, Object> map) {
		return coverBellMapper.queryCoverBell(map);
	}

	public List<CoverBell> getByCoverId(String coverId){
		return mapper.getByCoverId(coverId);
	}

	@Transactional(readOnly = false)
	public void updateState(String id,String state){
		mapper.updateState(id,state);
	}


	public List<CoverBell> checkFindList(CoverBell coverBell) {
		//dataRuleFilter(coverBell);
		return mapper.checkFindList(coverBell);
	}


	@Transactional(readOnly = false)
	public CoverBell processCoverBellExt(String bellNo,CoverBell coverBell) {
		try {
			if (null != coverBell) {
				DeviceInfo deviceInfo = deviceService.getDeviceInfo2(bellNo);  //update by ffy  从上面的方法改成这里,获取设备基础信息（包括imei和iccid）
				if (deviceInfo != null) {
					coverBell.setBellType(deviceInfo.getDeviceType());//设备类型
					coverBell.setVersion(deviceInfo.getVersion());//版本号
					coverBell.setImei(deviceInfo.getImei());   //add by ffy
					coverBell.setSim(deviceInfo.getIccid());   //add by ffy
					coverBell.setDefenseStatus(bellUtils.changeDefenseStatus(deviceInfo.getFortifyState()));//设防状态
					coverBell.setWorkStatus(deviceInfo.getOnlineState()==0?CodeConstant.BELL_WORK_STATUS.ON:CodeConstant.BELL_WORK_STATUS.OFF);//
				}


			}
		}catch (Exception e){
			e.printStackTrace();
			logger.info("**********井卫导入同步相关数据失败*********************"+e.getMessage());
		}
		return coverBell;
	}


	@Transactional(readOnly = false)
	public void synBellState(String bellNo){
		List<CoverBell> bellList=null;
		if(StringUtils.isNotEmpty(bellNo)){
			CoverBell query=new CoverBell();
			query.setBellNo(bellNo);
			bellList=mapper.checkFindList(query);
		}else{
			bellList=mapper.checkFindList(new CoverBell());
		}

		if(null!=bellList&&bellList.size()>0){
			for(CoverBell bell:bellList){
				DeviceInfo deviceInfo = deviceService.getDeviceInfo2(bell.getBellNo());
				if (deviceInfo != null) {
					String state=deviceInfo.getOnlineState()==0?CodeConstant.BELL_WORK_STATUS.ON:CodeConstant.BELL_WORK_STATUS.OFF;
					mapper.updateWorkStatus(bell.getId(),state);
				}
			}
		}
	}

}