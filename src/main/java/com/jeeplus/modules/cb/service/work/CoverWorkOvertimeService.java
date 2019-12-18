/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.work;

import java.util.Date;
import java.util.List;

import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cb.entity.work.CoverWorkConfig;
import com.jeeplus.modules.cb.mapper.work.CoverWorkConfigMapper;
import com.jeeplus.modules.cb.mapper.work.CoverWorkMapper;
import com.jeeplus.modules.flow.entity.opt.FlowWorkOpt;
import com.jeeplus.modules.flow.mapper.opt.FlowWorkOptMapper;
import com.jeeplus.modules.flow.service.opt.FlowWorkOptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.work.CoverWorkOvertime;
import com.jeeplus.modules.cb.mapper.work.CoverWorkOvertimeMapper;

/**
 * 超时工单Service
 * @author crj
 * @version 2019-11-07
 */
@Service
@Transactional(readOnly = true)
public class CoverWorkOvertimeService extends CrudService<CoverWorkOvertimeMapper, CoverWorkOvertime> {
	@Autowired
	private CoverWorkMapper coverWorkMapper;

	@Autowired
	private FlowWorkOptMapper flowWorkOptMapper;

	@Autowired
	private CoverWorkConfigMapper coverWorkConfigMapper;
	@Autowired
	private CoverWorkOvertimeMapper coverWorkOvertimeMapper;



	public CoverWorkOvertime get(String id) {
		return super.get(id);
	}
	
	public List<CoverWorkOvertime> findList(CoverWorkOvertime coverWorkOvertime) {
		return super.findList(coverWorkOvertime);
	}
	
	public Page<CoverWorkOvertime> findPage(Page<CoverWorkOvertime> page, CoverWorkOvertime coverWorkOvertime) {
		return super.findPage(page, coverWorkOvertime);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverWorkOvertime coverWorkOvertime) {
		super.save(coverWorkOvertime);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverWorkOvertime coverWorkOvertime) {
		super.delete(coverWorkOvertime);
	}

	/**
	 * 超时工单订单任务
	 */
	@Transactional(readOnly = false)
	public void workOverTimeTask(){
		//查询所有生命周期为：处理中的的工单
		CoverWork coverWork=new CoverWork();
		coverWork.setLifeCycle("processing");
		List<CoverWork> workList=coverWorkMapper.findList(coverWork);

		if(null!=workList&&workList.size()>0){
			for(CoverWork work: workList){
				FlowWorkOpt flowWorkOpt=new FlowWorkOpt();
				flowWorkOpt.setBillId(work);
				flowWorkOpt.setOptName("到达现场");
				List<FlowWorkOpt>  optList=flowWorkOptMapper.findList(flowWorkOpt);
				boolean flag=checkOverTime(work,optList);
				if(flag){//true为超时工单
					CoverWorkOvertime coverWorkOvertime=new CoverWorkOvertime();
					coverWorkOvertime.setWorkType(work.getWorkType());// 工单类型
					coverWorkOvertime.setWorkNum(work.getWorkNum());// 工单编号
					coverWorkOvertime.setCoverWorkId(work.getId());// 工单ID
					coverWorkOvertime.setWorkStatus(work.getWorkStatus());// 工单状态
					coverWorkOvertime.setConstructionUser(work.getConstructionUser());// 施工人员
					coverWorkOvertime.setConstructionDepart(work.getConstructionDepart());// 施工部门
					coverWorkOvertime.setOverType("arrive");// 超时类型,字典项：over_type，目前就一个值：arrive，抵达现场超时
					coverWorkOvertime.setOverTime(compareTime(work,optList));// 超时时长（分）
					//先判断下是否已经生成过超时工单
					CoverWorkOvertime queryOver=new CoverWorkOvertime();
					queryOver.setCoverWorkId(work.getId());
					List<CoverWorkOvertime> 	queryList=coverWorkOvertimeMapper.findList(queryOver);
					if(null!=queryList&&queryList.size()>0){
						//已经生成超时工单，无需再次操作
					}else{
						super.save(coverWorkOvertime);
					}

				}
			}

		}

	}

	/**
	 * 判断工单是否超时
	 * @param work 工单
	 * @param optList 超时工单操作记录
	 * @return
	 */
	public boolean checkOverTime(CoverWork work,List<FlowWorkOpt>  optList){
		boolean flag=false;//未超时
		CoverWorkConfig coverWorkConfig=new CoverWorkConfig();
		coverWorkConfig.setDelFlag("0");
		List<CoverWorkConfig> configList = coverWorkConfigMapper.findList(coverWorkConfig);
		if(null!=configList&&configList.size()>0){
         for(CoverWorkConfig config:configList){
         	if(config.getWorkType().equals(work.getWorkType())){//工单类型匹配上才能比对
				flag= compareTime(config.getArrivalTime(),work,optList);
			}

		 }
		}else{
			flag=false;//未超时
		}
		return flag;
	}

	/**
	 * 工单是否超时
	 * @param arrivalTime
	 * @param work
	 * @param optList
	 * @return
	 */
	public boolean compareTime(Integer arrivalTime,CoverWork work,List<FlowWorkOpt>  optList){
		boolean flag=false;//未超时
		Date useDate=new Date();
		if(null!=optList&&optList.size()>0){//到达现场工单操作记录
			FlowWorkOpt opt=optList.get(0);
			if(null!=opt){
				useDate=opt.getCreateDate();
			/*	long betweenTime = Math.abs(opt.getCreateDate().getTime() - (work.getCreateDate().getTime())) / 1000;//除以1000是为了转换成秒
				long useTime = betweenTime / 60;
				if(useTime>arrivalTime){
					flag=true;
				}*/
			}
		}
		long betweenTime = Math.abs(useDate.getTime() - (work.getCreateDate().getTime())) / 1000;//除以1000是为了转换成秒
		long useTime = betweenTime / 60;
		if(useTime>arrivalTime){
			flag=true;//超时
		}
		return flag;
	}

	/**
	 * 工单超时时长
	 * @param work
	 * @param optList
	 * @return
	 */
	public Integer compareTime(CoverWork work,List<FlowWorkOpt>  optList){
		Date useDate=new Date();
		if(null!=optList&&optList.size()>0){//到达现场工单操作记录
			FlowWorkOpt opt=optList.get(0);
			if(null!=opt){
				useDate=opt.getCreateDate();
			}
		}
		long betweenTime = Math.abs(useDate.getTime() - (work.getCreateDate().getTime())) / 1000;//除以1000是为了转换成秒
		long useTime = betweenTime / 60;

		return (int)useTime;
	}

}