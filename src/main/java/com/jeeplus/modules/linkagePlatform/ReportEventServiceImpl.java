package com.jeeplus.modules.linkagePlatform;

import com.alibaba.fastjson.JSONObject;

import com.jeeplus.common.utils.reflect.ClassUtil;
import com.jeeplus.modules.api.utils.HttpClientUtil;
import com.jeeplus.modules.cb.constant.coverWork.WorkConstant;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportEventServiceImpl implements ReportEventService{

    private static Logger logger = LoggerFactory.getLogger(ReportEventServiceImpl.class);

    @Value("${linkage.public.key}")
    private final String publicKey;

    @Value("${linkage.third.source}")
    private final String thirdSource;

    @Value("${linkage.platform.url}")
    private final String url;

    private final String path = "/cg-api/third/simpleUp";
    private CoverService coverSerice;

    public ReportEventServiceImpl(String publicKey, String thirdSource, String url, CoverService coverSerice) {
        this.publicKey = publicKey;
        this.thirdSource = thirdSource;
        this.url = url;
        this.coverSerice = coverSerice;
    }

    @Override
    public boolean reportEvent(CoverWork coverWork) {
        //生成accessToken
        try {
            String accessToken = JwtUtil.create();
            //参数
            Map<String, String> param = new HashMap<>();
            //hearder 参数
            Map<String, String> headerParam = new HashMap<>();
            headerParam.put("accessToken", accessToken);
            headerParam.put("publicKey", publicKey);
            headerParam.put("Content-Type", "application/json");

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
            String typeName = transferWorkTypeName(coverWork.getWorkType());
            Cover cover = coverSerice.get(coverWork.getCover());
            if (cover != null) {
                param.put("addr", cover.getAddressDetail());
            }
            param.put("dataCode", thirdSource);
            param.put("reportTime", sdf.format(date));
            param.put("description", typeName);

            param.put("longitude", coverWork.getLongitude().toString());
            param.put("latitude", coverWork.getLatitude().toString());
            param.put("thirdSource", thirdSource);
            param.put("reportUserCode", UserUtils.getUser().getId());
            param.put("reportUserName", UserUtils.getUser().getLoginName());

            String res = HttpClientUtil.post(url + path, param, headerParam);
            if (StringUtils.isNotBlank(res)) {
                JSONObject obj = JSONObject.parseObject(res);
                if ("0".equals(obj.getString("code"))) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("reportEvent 协调联动平台上传事件失败：" + e.getMessage());
            return false;
        }
    }


    public String transferWorkTypeName(String workType) {
        switch (workType) {
            case CodeConstant.WORK_TYPE.INSTALL :
                return "安装工单";
            case CodeConstant.WORK_TYPE.CHECK :
                return "巡检工单";
            case CodeConstant.WORK_TYPE.BIZ_ALARM :
                return "报警工单";
            case CodeConstant.WORK_TYPE.SPOT_CHECK :
                return "抽检工单";
            case CodeConstant.WORK_TYPE.EXCEPTION_REPORT :
                return "异常上报工单";
            case CodeConstant.WORK_TYPE.MAINTAIN :
                return "维护工单";
            default:
                return null;
        }
    }
}
