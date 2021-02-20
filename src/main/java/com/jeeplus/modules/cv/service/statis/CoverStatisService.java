/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.statis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.collection.CollectionUtil;
import com.jeeplus.core.security.Digests;
import com.jeeplus.modules.cv.mapper.statis.CoverCollectStatisMapper;
import com.jeeplus.modules.cv.vo.CoverStatisVO;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.mapper.DictTypeMapper;
import com.jeeplus.modules.sys.mapper.DictValueMapper;
import com.jeeplus.modules.sys.utils.DictUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.statis.CoverStatis;
import com.jeeplus.modules.cv.mapper.statis.CoverStatisMapper;

/**
 * 井盖相关统计Service
 * @author crj
 * @version 2021-02-08
 */
@Service
@Transactional
public class CoverStatisService extends CrudService<CoverStatisMapper, CoverStatis> {
	@Autowired
	private CoverCollectStatisMapper coverCollectStatisMapper;
    @Autowired
    private CoverStatisMapper coverStatisMapper;
    @Autowired
    private DictValueMapper dictValueMapper;
    @Autowired
    private DictTypeMapper dictTypeMapper;

	public CoverStatis get(String id) {
		return super.get(id);
	}
	
	public List<CoverStatis> findList(CoverStatis coverStatis) {
		return super.findList(coverStatis);
	}
	
	public Page<CoverStatis> findPage(Page<CoverStatis> page, CoverStatis coverStatis) {
		return super.findPage(page, coverStatis);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverStatis coverStatis) {
		super.save(coverStatis);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverStatis coverStatis) {
		super.delete(coverStatis);
	}



public void  statisCover(){

		//1.统计井盖区域数据
	Map<String,String> map = new HashMap<String,String>();
	StringBuffer lineSQL=new StringBuffer("select count(*) as num,district from cover where del_flag='0' group by district");
	String coverSQL=lineSQL.toString();
	List<Map<String, Object>> collectList = coverCollectStatisMapper.selectBySql(coverSQL);
	if(null!=collectList&&collectList.size()>0){
		//3.获取井盖类型

        List<DictValue> coverTypeList=dictValueMapper.findList(new DictValue(dictTypeMapper.findUniqueByProperty("type", "cover_type")));
		//List<DictValue> coverTypeList=DictUtils.getDictList("cover_type");
		for(DictValue coverType:coverTypeList){
			for (Map<String, Object> resultMap:collectList) {
				String num=String.valueOf(resultMap.get("num"));//数量
				String district=String.valueOf(resultMap.get("district"));//区域
				//2.获取井盖权属单位
				//List<DictValue> ownerList=DictUtils.getDictList("cover_owner_depart");
                List<DictValue> ownerList=dictValueMapper.findList(new DictValue(dictTypeMapper.findUniqueByProperty("type", "cover_owner_depart")));
				for(DictValue owner:ownerList){
					statisCoverPro(coverType.getValue(),district,owner.getValue());
				}
			}
		}

	}
}

    /**
     * 统计井盖数据
     * @param coverType 井盖类型
     * @param district 井盖区域
     * @param owner 井盖权属单位
     * @return
     */
    public String statisCoverNum(String coverType,String district,String  owner){
        String coverNum="0";
        StringBuffer lineSQL=new StringBuffer("SELECT  count(a.id) AS amount  FROM cover a where a.del_flag='0' ");
        if(StringUtils.isNotEmpty(coverType)){
            lineSQL.append("  and a.cover_type='").append(coverType).append("'");
        }
        if(StringUtils.isNotEmpty(district)){
            lineSQL.append("  and a.district='").append(district).append("'");
        }
        if(StringUtils.isNotEmpty(owner)){
            lineSQL.append("  and a.owner_depart='").append(owner).append("'");
        }
        String dataSQL=lineSQL.toString();
        List<Map<String, Object>> coverDataList = coverCollectStatisMapper.selectBySql(dataSQL);
        if(null!=coverDataList&&coverDataList.size()>0){
            Map<String, Object> map=coverDataList.get(0);
            Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
            coverNum=amount+"";// 井盖数
        }
        return coverNum;
    }

    /**
     * 已安装设备数
     * @param coverType 井盖类型
     * @param district 井盖区域
     * @param owner 井盖权属单位
     * @return
     */
    public String statisInstallEqu(String coverType,String district,String  owner){
        String installEqu="0";
        //select count(a.id) AS amount from cover_bell a  where a.del_flag='0' and a.cover_id in (SELECT  b.id  FROM cover b where b.del_flag='0' and b.district='鼓楼区' and b.cover_type='普通井盖' and b.owner_depart='鼓楼区城管')
        StringBuffer lineSQL=new StringBuffer("select count(a.id) AS amount from cover_bell a  where a.del_flag='0' and a.cover_id in (SELECT  b.id  FROM cover b where b.del_flag='0' ");
        if(StringUtils.isNotEmpty(coverType)){
            lineSQL.append("  and b.cover_type=' ").append(coverType).append("'");
        }
        if(StringUtils.isNotEmpty(district)){
            lineSQL.append("  and b.district=' ").append(district).append("'");
        }
        if(StringUtils.isNotEmpty(owner)){
            lineSQL.append("  and b.owner_depart=' ").append(owner).append("')");
        }
        String dataSQL=lineSQL.toString();
        List<Map<String, Object>> coverDataList = coverCollectStatisMapper.selectBySql(dataSQL);
        if(null!=coverDataList&&coverDataList.size()>0){
            Map<String, Object> map=coverDataList.get(0);
            Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
            installEqu=amount+"";// 已安装设备数
        }
        return installEqu;
    }

    /**
     * 当前在线数
     * @param coverType 井盖类型
     * @param district 井盖区域
     * @param owner 井盖权属单位
     * @return
     */
    public String statisOnlineNum(String coverType,String district,String  owner){
        String onlineNum="0";
        //select count(a.id) AS amount from cover_bell a  where a.del_flag='0' and a.work_status='on' and a.cover_id in (SELECT  b.id  FROM cover b where b.del_flag='0' and b.district='鼓楼区' and b.cover_type='普通井盖' and b.owner_depart='鼓楼区城管')
        StringBuffer lineSQL=new StringBuffer("select count(a.id) AS amount from cover_bell a  where a.del_flag='0' and a.work_status='on' and a.cover_id in (SELECT  b.id  FROM cover b where b.del_flag='0' ");
        if(StringUtils.isNotEmpty(coverType)){
            lineSQL.append("  and b.cover_type=' ").append(coverType).append("'");
        }
        if(StringUtils.isNotEmpty(district)){
            lineSQL.append("  and b.district=' ").append(district).append("'");
        }
        if(StringUtils.isNotEmpty(owner)){
            lineSQL.append("  and b.owner_depart=' ").append(owner).append("')");
        }
        String dataSQL=lineSQL.toString();
        List<Map<String, Object>> coverDataList = coverCollectStatisMapper.selectBySql(dataSQL);
        if(null!=coverDataList&&coverDataList.size()>0){
            Map<String, Object> map=coverDataList.get(0);
            Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
            onlineNum=amount+"";// 当前在线数
        }
        return onlineNum;
    }


    /**
     * 报警井盖数
     * @param coverType 井盖类型
     * @param district 井盖区域
     * @param owner 井盖权属单位
     * @return
     */
    public String statisCoverAlarmNum(String coverType,String district,String  owner){
        String coverAlarmNum="0";
       //select count(a.id) AS amount from cover_bell a  where a.del_flag='0' and a.cover_id in(select c.cover_id from cover_biz_alarm c group by c.cover_id) and a.cover_id in (SELECT  b.id  FROM cover b where b.del_flag='0' and b.district='鼓楼区' and b.cover_type='普通井盖' and b.owner_depart='鼓楼区城管')
        StringBuffer lineSQL=new StringBuffer("select count(a.id) AS amount from cover_bell a  where a.del_flag='0'  and a.cover_id in(select c.cover_id from cover_biz_alarm c group by c.cover_id) and a.cover_id in (SELECT  b.id  FROM cover b where b.del_flag='0' ");
        if(StringUtils.isNotEmpty(coverType)){
            lineSQL.append("  and b.cover_type=' ").append(coverType).append("'");
        }
        if(StringUtils.isNotEmpty(district)){
            lineSQL.append("  and b.district=' ").append(district).append("'");
        }
        if(StringUtils.isNotEmpty(owner)){
            lineSQL.append("  and b.owner_depart=' ").append(owner).append("')");
        }
        String dataSQL=lineSQL.toString();
        List<Map<String, Object>> coverDataList = coverCollectStatisMapper.selectBySql(dataSQL);
        if(null!=coverDataList&&coverDataList.size()>0){
            Map<String, Object> map=coverDataList.get(0);
            Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
            coverAlarmNum=amount+"";// 报警井盖数
        }
        return coverAlarmNum;
    }

    /**
     * 报警总数
     * @param coverType 井盖类型
     * @param district 井盖区域
     * @param owner 井盖权属单位
     * @return
     */
    public String statisAlarmTotalNum(String coverType,String district,String  owner){
        String alarmTotalNum="0";
        //select count(a.id) AS amount from cover_biz_alarm  a where a.cover_id in (SELECT  b.id  FROM cover b where b.del_flag='0' and b.district='鼓楼区' and b.cover_type='普通井盖' and b.owner_depart='鼓楼区城管')
        StringBuffer lineSQL=new StringBuffer("select count(a.id) AS amount from cover_biz_alarm  a where a.cover_id in (SELECT  b.id  FROM cover b where b.del_flag='0' ");
        if(StringUtils.isNotEmpty(coverType)){
            lineSQL.append("  and b.cover_type=' ").append(coverType).append("'");
        }
        if(StringUtils.isNotEmpty(district)){
            lineSQL.append("  and b.district=' ").append(district).append("'");
        }
        if(StringUtils.isNotEmpty(owner)){
            lineSQL.append("  and b.owner_depart=' ").append(owner).append("')");
        }
        String dataSQL=lineSQL.toString();
        List<Map<String, Object>> coverDataList = coverCollectStatisMapper.selectBySql(dataSQL);
        if(null!=coverDataList&&coverDataList.size()>0){
            Map<String, Object> map=coverDataList.get(0);
            Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
            alarmTotalNum=amount+"";// 报警总数
        }
        return alarmTotalNum;
    }


    /**
     * 工单总数（当天新增）
     * @param coverType 井盖类型
     * @param district 井盖区域
     * @param owner 井盖权属单位
     * @return
     */
    public String statisAddWorkNum(String coverType,String district,String  owner){
        String addWorkNum="0";
        //select count(a.id) AS amount from cover_work a where a.del_flag='0' and to_days(a.create_date) = to_days(now()) and a.cover in (SELECT  b.id  FROM cover b where b.del_flag='0' and b.district='鼓楼区' and b.cover_type='普通井盖' and b.owner_depart='鼓楼区城管')
        StringBuffer lineSQL=new StringBuffer("select count(a.id) AS amount from cover_work a where a.del_flag='0' and to_days(a.create_date) = to_days(now()) and a.cover in (SELECT  b.id  FROM cover b where b.del_flag='0' ");
        if(StringUtils.isNotEmpty(coverType)){
            lineSQL.append("  and b.cover_type=' ").append(coverType).append("'");
        }
        if(StringUtils.isNotEmpty(district)){
            lineSQL.append("  and b.district=' ").append(district).append("'");
        }
        if(StringUtils.isNotEmpty(owner)){
            lineSQL.append("  and b.owner_depart=' ").append(owner).append("')");
        }
        String dataSQL=lineSQL.toString();
        List<Map<String, Object>> coverDataList = coverCollectStatisMapper.selectBySql(dataSQL);
        if(null!=coverDataList&&coverDataList.size()>0){
            Map<String, Object> map=coverDataList.get(0);
            Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
            addWorkNum=amount+"";// 工单总数（当天新增）
        }
        return addWorkNum;
    }


    /**
     * 已完成工单总数（当天）
     * @param coverType 井盖类型
     * @param district 井盖区域
     * @param owner 井盖权属单位
     * @return
     */
    public String statisCompleteWorkNum(String coverType,String district,String  owner){
        String completeWorkNum="0";
        //select count(a.id) AS amount from cover_work a where a.del_flag='0' and a.life_cycle='complete' and to_days(a.create_date) = to_days(now()) and a.cover in (SELECT  b.id  FROM cover b where b.del_flag='0' and b.district='鼓楼区' and b.cover_type='普通井盖' and b.owner_depart='鼓楼区城管')
        StringBuffer lineSQL=new StringBuffer("select count(a.id) AS amount from cover_work a where a.del_flag='0' and a.life_cycle='complete' and to_days(a.create_date) = to_days(now()) and a.cover in (SELECT  b.id  FROM cover b where b.del_flag='0' ");
        if(StringUtils.isNotEmpty(coverType)){
            lineSQL.append("  and b.cover_type=' ").append(coverType).append("'");
        }
        if(StringUtils.isNotEmpty(district)){
            lineSQL.append("  and b.district=' ").append(district).append("'");
        }
        if(StringUtils.isNotEmpty(owner)){
            lineSQL.append("  and b.owner_depart=' ").append(owner).append("')");
        }
        String dataSQL=lineSQL.toString();
        List<Map<String, Object>> coverDataList = coverCollectStatisMapper.selectBySql(dataSQL);
        if(null!=coverDataList&&coverDataList.size()>0){
            Map<String, Object> map=coverDataList.get(0);
            Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
            completeWorkNum=amount+"";//已完成工单总数（当天）
        }
        return completeWorkNum;
    }

    /**
     * 未完成工单总数（累计）
     * @param coverType 井盖类型
     * @param district 井盖区域
     * @param owner 井盖权属单位
     * @return
     */
    public String statisProWorkNum(String coverType,String district,String  owner){
        String proWorkNum="0";
        //select count(a.id) AS amount from cover_work a where a.del_flag='0' and a.life_cycle!='complete'  and a.cover in (SELECT  b.id  FROM cover b where b.del_flag='0' and b.district='鼓楼区' and b.cover_type='普通井盖' and b.owner_depart='鼓楼区城管')
        StringBuffer lineSQL=new StringBuffer("select count(a.id) AS amount from cover_work a where a.del_flag='0' and a.life_cycle!='complete' and a.cover in (SELECT  b.id  FROM cover b where b.del_flag='0' ");
        if(StringUtils.isNotEmpty(coverType)){
            lineSQL.append("  and b.cover_type=' ").append(coverType).append("'");
        }
        if(StringUtils.isNotEmpty(district)){
            lineSQL.append("  and b.district=' ").append(district).append("'");
        }
        if(StringUtils.isNotEmpty(owner)){
            lineSQL.append("  and b.owner_depart=' ").append(owner).append("')");
        }
        String dataSQL=lineSQL.toString();
        List<Map<String, Object>> coverDataList = coverCollectStatisMapper.selectBySql(dataSQL);
        if(null!=coverDataList&&coverDataList.size()>0){
            Map<String, Object> map=coverDataList.get(0);
            Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
            proWorkNum=amount+"";//未完成工单总数（累计）
        }
        return proWorkNum;
    }

    public String getInfoFlag(String coverType,String district,String  owner){
        StringBuffer sb=new StringBuffer();
        String statisTime=DateUtils.getDate();// 统计时间
        sb.append(coverType);
        sb.append(district);
        sb.append(owner);
        sb.append(statisTime);
        return Digests.string2MD5(sb.toString());
    }

public void statisCoverPro(String coverType,String district,String  owner){
    String statisTime=DateUtils.getDate();// 统计时间
    String flag = getInfoFlag(coverType,district,owner);
    CoverStatis coverStatis=new CoverStatis();
    coverStatis.setFlag(flag);
    coverStatis.setCoverType(coverType);// 井盖类型
    coverStatis.setDistrict(district);// 区域
    coverStatis.setOwnerDepart(owner);// 权属单位
    coverStatis.setStatisTime(statisTime);// 统计时间
    String coverNum=statisCoverNum(coverType,district,owner);
    coverStatis.setCoverNum(coverNum);// 井盖数
    String installEqu=statisInstallEqu(coverType,district,owner);// 已安装设备数
    coverStatis.setInstallEqu(installEqu);
    String onlineNum=statisOnlineNum(coverType,district,owner);// 当前在线数
    coverStatis.setOnlineNum(onlineNum);

    String offlineNum=(Integer.parseInt(installEqu)-Integer.parseInt(onlineNum))+"";		// 当前离线数
    coverStatis.setOfflineNum(offlineNum);

    String coverAlarmNum=statisCoverAlarmNum(coverType,district,owner);// 报警井盖数
    coverStatis.setCoverAlarmNum(coverAlarmNum);

    String alarmTotalNum=statisAlarmTotalNum(coverType,district,owner);// 报警总数
    coverStatis.setAlarmTotalNum(alarmTotalNum);

    String addWorkNum=statisAddWorkNum(coverType,district,owner);// 工单总数（当天新增）
    coverStatis.setAddWorkNum(addWorkNum);

    String completeWorkNum=statisCompleteWorkNum(coverType,district,owner);		// 已完成工单总数（当天）
    coverStatis.setCompleteWorkNum(completeWorkNum);

    String proWorkNum=statisProWorkNum(coverType,district,owner);		// 未完成工单总数（累计）
    coverStatis.setProWorkNum(proWorkNum);

    CoverStatis query=new CoverStatis();
    query.setFlag(flag);
    List<CoverStatis> list=coverStatisMapper.findList(query);
    if (CollectionUtils.isNotEmpty(list)) {//修改
        CoverStatis older=  list.get(0);
        coverStatis.setId(older.getId());
        coverStatis.setIsNewRecord(false);
        coverStatis.setUpdateDate(new Date());
        coverStatisMapper.update(coverStatis);
    }else{
        coverStatis.setId(IdGen.uuid());
        coverStatis.setIsNewRecord(true);
        coverStatis.setCreateDate(new Date());
        coverStatis.setUpdateDate(new Date());
        coverStatisMapper.insert(coverStatis);
    }

}

public  List<CoverStatisVO> queryStatisData(CoverStatis param){
//select a.district AS district, sum(a.cover_num) AS coverNum,sum(a.install_equ) AS installEqu,sum(a.online_num) AS onlineNum,sum(a.offline_num) AS offlineNum,sum(a.cover_alarm_num) AS coverAlarmNum,sum(a.alarm_total_num) AS alarmTotalNum,sum(a.add_work_num) AS addWorkNum,sum(a.complete_work_num) AS completeWorkNum,sum(a.pro_work_num) AS proWorkNum, a.statis_time AS statisTime from cover_statis a where a.district='鼓楼区' and a.cover_type='普通井盖' and a.owner_depart='鼓楼区城管' group by a.district;
 return   coverStatisMapper.queryStatisData(param);
}


public String queryMaxStatisDate(){
    String updateDate="";
    StringBuffer lineSQL=new StringBuffer("select update_date AS updateDate FROM cover_statis ORDER BY update_date DESC LIMIT 0,1 ");
    String dataSQL=lineSQL.toString();
    List<Map<String, Object>> coverDataList = coverCollectStatisMapper.selectBySql(dataSQL);
    if(null!=coverDataList&&coverDataList.size()>0){
        Map<String, Object> map=coverDataList.get(0);
        updateDate=String.valueOf(map.get("updateDate"));
        if(StringUtils.isNotEmpty(updateDate)){
            updateDate=updateDate.substring(0, updateDate.length()-2);
        }

    }
    return updateDate;
    }

    public void deleteCoverStatis(){
        String statisTime=DateUtils.getGivenTime(30,"yyyy-MM-dd");// 删除统计时间为20天之前的数据
        CoverStatis query=new CoverStatis();
        query.setStatisTime(statisTime);
        List<CoverStatis> list=coverStatisMapper.findList(query);
        if(CollectionUtil.isNotEmpty(list)){
            for(CoverStatis coverStatis:list){
                coverStatisMapper.delete(coverStatis);
            }
        }

        }
}