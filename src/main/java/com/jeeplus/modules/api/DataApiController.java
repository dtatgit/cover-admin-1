package com.jeeplus.modules.api;


import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.modules.api.vo.AlarmResp;
import com.jeeplus.modules.api.vo.CoverResp;
import com.jeeplus.modules.api.vo.DamageResp;
import com.jeeplus.modules.api.vo.GuardResp;
import com.jeeplus.modules.cb.entity.bizAlarm.BizAlarm;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.service.bizAlarm.BizAlarmService;
import com.jeeplus.modules.cb.service.equinfo.CoverBellService;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.entity.equinfo.CoverDamage;
import com.jeeplus.modules.cv.service.equinfo.CoverDamageService;
import com.jeeplus.modules.cv.service.equinfo.CoverImageService;
import com.jeeplus.modules.cv.service.equinfo.CoverOwnerService;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 目前大兴共享数据的接口
 */
@RestController
@RequestMapping("/data/sharing/api")
public class DataApiController {

    private static Logger logger = LoggerFactory.getLogger(DataApiController.class);

    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private CoverService coverService;
    @Autowired
    private CoverBellService coverBellService;
    @Autowired
    private CoverImageService coverImageService;
    @Autowired
    private CoverDamageService coverDamageService;
    @Autowired
    private CoverOwnerService coverOwnerService;
    @Autowired
    private BizAlarmService bizAlarmService;

    @RequestMapping("/cover/list")
    public AppResult coverList(@RequestParam(value = "coverNo",required = false)String coverNo,
                                      @RequestParam(value = "coverType",required = false)String coverType,
                                      @RequestParam(value = "street",required = false)String street,
                                      @RequestParam(value = "purpose",required = false)String purpose,
                                      @RequestParam(value = "situation",required = false)String situation,
                                      @RequestParam(value = "isDamaged",required = false)String isDamaged,
                                      @RequestParam(value = "material",required = false)String material,
                                      @RequestParam(value = "pageNo", required = true) Integer pageNo,
                                      @RequestParam(value = "pageSize", required = true) Integer pageSize,
                                      HttpServletRequest request, HttpServletResponse response) {

        AppResult result = new AppResult();

        Cover cover = new Cover();
        cover.setNo(coverNo);
        cover.setCoverType(coverType);
        cover.setStreet(street);
        cover.setPurpose(purpose);
        cover.setSituation(situation);
        cover.setIsDamaged(isDamaged);
        cover.setMaterial(material);

        Page<Cover> page = coverService.findPage2(new Page<Cover>(request, response), cover);
        List<CoverResp> resultList = new ArrayList<>();

        List<Cover> list = page.getList();
        if(list!=null && !list.isEmpty()){
            list.forEach(item->{
                String id = item.getId();
                CoverResp coverResp = new CoverResp();
                BeanUtils.copyProperties(item,coverResp);
                CoverBell byCoverId = coverBellService.getByCoverId(id);
                if(byCoverId!=null){
                    GuardResp guardResp = new GuardResp();
                    BeanUtils.copyProperties(byCoverId,guardResp);
                    coverResp.setGuard(guardResp);
                }
                coverResp.setCoverImageList(coverImageService.obtainImage(id));
                List<CoverDamage> coverDamages = coverDamageService.obtainDamage(id);
                if(coverDamages!=null&&!coverDamages.isEmpty()){
                    List<DamageResp> collect = coverDamages.stream().map(damage -> {
                        DamageResp resp = new DamageResp();
                        resp.setDamage(damage.getDamage());
                        resp.setStatus(damage.getStatus());
                        return resp;
                    }).collect(Collectors.toList());
                    coverResp.setCoverDamageList(collect);
                }
                coverResp.setCoverOwnerList(coverOwnerService.obtainOwner(id));
                resultList.add(coverResp);
            });
        }

        if(resultList.isEmpty()){
            result.setSuccess(false);
            result.setMsg("无记录");
        }else{
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rows", resultList);
            map.put("total", page.getCount());

            result.setData(map);
        }
        return result;
    }


    @RequestMapping("/alarm/list")
    public AppResult alarmList(@RequestParam(value = "coverNo",required = false) String coverNo,
                               @RequestParam(value = "startTime",required = false) String startTime,
                               @RequestParam(value = "endTime",required = false) String endTime,
                               @RequestParam(value = "pageNo", required = true) Integer pageNo,
                               @RequestParam(value = "pageSize", required = true) Integer pageSize,
                               HttpServletRequest request, HttpServletResponse response){
        AppResult result = new AppResult();


        BizAlarm bizAlarm = new BizAlarm();
        if(StringUtils.isNotBlank(coverNo)){
            bizAlarm.setCoverNo(coverNo);
        }

        try {
            if(StringUtils.isNotBlank(startTime)){
                bizAlarm.setBeginDate(ft.parse(startTime));
            }

            if(StringUtils.isNotBlank(endTime)){
                bizAlarm.setEndDate(ft.parse(endTime));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Page<BizAlarm> page = bizAlarmService.findPage2(new Page<BizAlarm>(request, response), bizAlarm);

        List<AlarmResp> resultList = new ArrayList<>();

        List<BizAlarm> list = page.getList();
        if(list!=null && !list.isEmpty()){
            list.forEach(item->{
                AlarmResp alarmResp = new AlarmResp();
                BeanUtils.copyProperties(item,alarmResp);
                alarmResp.setIsCreateWork(item.getIsCreateWork());
                String coverId = item.getCoverId();
                Cover cover = coverService.get(coverId);
                alarmResp.setAddress(cover.getAddressDetail());
                resultList.add(alarmResp);
            });
        }

        if(resultList.isEmpty()){
            result.setSuccess(false);
            result.setMsg("无记录");
        }else{
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rows", resultList);
            map.put("total", page.getCount());

            result.setData(map);
        }
        return result;
    }


}
