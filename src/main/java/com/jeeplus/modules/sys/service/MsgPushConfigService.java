/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.collection.CollectionUtil;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.api.pojo.Result;
import com.jeeplus.modules.api.utils.HttpClientUtil;
import com.jeeplus.modules.cb.entity.bizAlarm.BizAlarm;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.sys.entity.MsgPushConfig;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.MsgPushConfigMapper;
import com.jeeplus.modules.sys.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 消息推送配置Service
 *
 * @author crj
 * @version 2021-02-08
 */
@Service
@Transactional(readOnly = true)
public class MsgPushConfigService extends CrudService<MsgPushConfigMapper, MsgPushConfig> {
    public static final String APP_TOKEN = "appToken";
    @Autowired
    private SystemService systemService;

    private final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0,TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    public MsgPushConfig get(String id) {
        return super.get(id);
    }

    public List<MsgPushConfig> findList(MsgPushConfig msgPushConfig) {
        return super.findList(msgPushConfig);
    }

    public Page<MsgPushConfig> findPage(Page<MsgPushConfig> page, MsgPushConfig msgPushConfig) {
        return super.findPage(page, msgPushConfig);
    }

    @Transactional(readOnly = false)
    public void save(MsgPushConfig msgPushConfig) {
        super.save(msgPushConfig);
    }

    @Transactional(readOnly = false)
    public void delete(MsgPushConfig msgPushConfig) {
        super.delete(msgPushConfig);
    }

    /**
     * @param noticeOfficeId 消息推送部门id
     * @param bizAlarm       业务报警数据
     */
    public void pushMsg(String noticeOfficeId, BizAlarm bizAlarm) {
        String noticeType = bizAlarm.getAlarmType();//消息推送类型
        MsgPushConfig msgPushConfig = new MsgPushConfig();
        msgPushConfig.setNoticeOfficeId(noticeOfficeId);
        msgPushConfig.setNoticeType(noticeType);
        msgPushConfig.setState(CodeConstant.on_off.one);//字典类型为：on_off,1启用，0禁用
        //根据部门和消息类型，查询消息配置推送人员信息和推送方式
        List<MsgPushConfig> msgPushConfigList = super.findList(msgPushConfig);
        if (CollectionUtil.isNotEmpty(msgPushConfigList)) {
            StringBuilder sb = new StringBuilder();
            sb.append("【井卫云管理平台】您有新的报警").append(bizAlarm.getAlarmNo());
            sb.append("，类型为").append(DictUtils.getDictLabel(noticeType, "biz_alarm_type", ""));
            sb.append("，报警产生时间：").append(bizAlarm.getAlarmTime()).append(";");
            sb.append("请尽快进行处理!");
            String content = sb.toString();//推送内容,
            logger.debug("推送信息内容: {}", content);

            // 【井卫云管理平台】您有新的报警（23425223）类型为${开盖报警}报警产生时间：2020.10.20 24:32:21;请尽快进行处理
            for (MsgPushConfig pushConfig : msgPushConfigList) {
                String pushMode = pushConfig.getPushMode();// 推送方式
                User noticePerson = pushConfig.getNoticePerson();// 通知人员

                if (StringUtils.isNotEmpty(pushMode) && pushMode.equals(CodeConstant.push_mode.message)) {//短息推送
                    User user = systemService.getUser(noticePerson.getId());
                    this.executorService.submit(() -> pushMessage(user.getMobile(), content));
                } else if (StringUtils.isNotEmpty(pushMode) && pushMode.equals(CodeConstant.push_mode.mobile)) {//手机APP推送
                    this.executorService.submit(() -> pushAppMessage(noticePerson.getId(), content));
                }
            }
        }
    }

    /**
     * @param phones  手机号码：空格、逗号、分号分隔，最多50个
     * @param content 短信内容
     */
    public void pushMessage(String phones, String content) {
        try {
            logger.debug("短信推送：[{}] {}", phones, content);

            if (StringUtils.isNotEmpty(phones)) {
                String smsUrl = Global.getConfig("dbSms.server.url") + "/sms/db/send";
                Map<String, String> param = new HashMap<>();
                param.put("phones", phones);
                param.put("content", content);
                String str = HttpClientUtil.doPost(smsUrl, param);
                logger.info("大兴db短信接口返回内容：********" + str);
                //Result result = JSONObject.parseObject(str,Result.class);
            }
        } catch (Throwable e) {
            logger.warn("[大兴db短信] 处理异常: {}", e.getMessage(), e);
        }
    }

    public String pushAppLogin() {
        String accessToken = "";
        try {
            String appSmsUrl = Global.getConfig("app.server.url") + "/one-page-service/api/login";
            String privateKey = Global.getConfig("app.private.key");//私钥
            String publicKey = Global.getConfig("app.public.key");//公钥
            Map<String, String> param = new HashMap<>();
            param.put("privateKey ", privateKey);
            param.put("publicKey  ", publicKey);
            String str = HttpClientUtil.doPost(appSmsUrl, param);
            logger.info("APP消息对接接口返回内容：********" + str);
            Result result = JSONObject.parseObject(str, Result.class);
            if (result.getCode().equals(200)) {//200成功
                Object data = result.getData();
                accessToken = data.toString();
                CacheUtils.put(APP_TOKEN, accessToken);
            } else if (result.getCode().equals(400)) {//参数校验失败
                logger.info("APP消息对接接口：参数校验失败" + param.values());
            } else {
                logger.warn("未知错误: [{}] message: {}", result.getCode(), result.getMsg());
            }
        } catch (Throwable e) {
            logger.warn("[大兴APP登录] 处理异常: {}", e.getMessage(), e);
        }
        return accessToken;
    }

    public void pushAppMessage(String ids, String message) {
        try {
            logger.debug("APP消息推送：[{}] {}", ids, message);

            String publicKey = Global.getConfig("app.public.key");//公钥
            String appToken = (String) CacheUtils.get(APP_TOKEN);
            if (StringUtils.isEmpty(appToken)) {
                appToken = pushAppLogin();
            }
            String appSmsUrl = Global.getConfig("app.server.url") + "/one-page-service/api/send/message";
            Map<String, String> headParam = new HashMap<>();
            headParam.put("publicKey", publicKey);
            headParam.put("accessToken", appToken);

            Map<String, String> params = new HashMap<>();
            params.put("ids", ids);//接收人格式: id1.id2.id3 (用.分隔开的用户id)
            params.put("message", message);
            String str = HttpClientUtil.sendGet(appSmsUrl, headParam, params);
            logger.info("APP消息对接接口返回内容：********" + str);
            Result result = JSONObject.parseObject(str, Result.class);
            if (result.getCode().equals(200)) {//200成功
                logger.info("APP消息对接接口成功" + result.getMsg());
            } else if (result.getCode().equals(400)) {//参数校验失败
                logger.info("APP消息对接接口：参数校验失败" + result.getMsg());
            } else if (result.getCode().equals(401)) {//token异常
                logger.info("APP消息对接接口：token异常" + result.getMsg());
            }
        } catch (Throwable e) {
            logger.warn("[大兴APP消息推送] 处理异常: {}", e.getMessage(), e);
        }
    }
}