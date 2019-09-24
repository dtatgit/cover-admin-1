/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.equinfo;

import java.util.List;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.api.constant.Constants;
import com.jeeplus.modules.api.pojo.DeviceResult;
import com.jeeplus.modules.api.pojo.Result;
import com.jeeplus.modules.api.service.DeviceService;
import com.jeeplus.modules.api.utils.bellUtils;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.mapper.equinfo.CoverBellMapper;

/**
 * 井铃设备信息Service
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
	public String processWorkStatus(String deviceId,String workStatus) {
		String retMsg = "";
		try{
			CoverBell bell=super.findUniqueByProperty("bell_no", deviceId);
			if(null==bell){
			//注册设备
				//井卫数据为空，则自动注册
				DeviceResult deviceResult= deviceService.getDeviceInfo(deviceId);
				bell=new CoverBell();
				bell.setBellNo(deviceId);//设备编号
				bell.setBellType(deviceResult.getdType());//设备类型（sy,sz,wx）
				bell.setVersion(deviceResult.getVersion());//版本号
				bell.setDefenseStatus(bellUtils.changeDefenseStatus(deviceResult.getFortifyState()));//设防状态
				bell.setWorkStatus(workStatus);// 工作状态
				bell.setBellStatus(CodeConstant.BELL_STATUS.init);// 生命周期

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

	
}