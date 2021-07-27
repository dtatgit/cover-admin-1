package com.jeeplus.modules.sys.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.antu.mq.activemq.ActivemqAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TopicMessageListenNet {

    private static final Logger logger = LoggerFactory.getLogger(TopicMessageListen.class);

    public static String ENCODING_UTF8 = "UTF-8";
    @Autowired
    private SystemService systemService;
    @Autowired
    private OfficeService officeService;

    private final String orgCode;
    private final String companyId;
    private final String defaultPassword;
    private final String defaultRoleEnname;

    @Autowired
    @SuppressWarnings({"rawtypes", "unchecked"})
    public TopicMessageListenNet(
            @Qualifier("jobActiveMqttFactory2") ActivemqAdapter adapter2,
            @Value("${activemq_topic_net}") String subscribeTopic,
            @Value("${net.portal.import.org.code}") String orgCode,
            @Value("${net.portal.import.company.id}") String companyId,
            @Value("${net.portal.import.role.enname}") String defaultRoleEnname,
            @Value("${net.portal.import.default.password}") String defaultPassword
    ) {
        this.orgCode = orgCode;
        this.companyId = companyId;
        this.defaultPassword = defaultPassword;
        this.defaultRoleEnname = defaultRoleEnname;
        adapter2.addListener(subscribeTopic, (t, m) -> {
            logger.info("[互联网认证用户数据同步开始] ");
            JSONObject jsonParam = JSON.parseObject(new String(m.getPayload()));
            boolean success = (boolean) jsonParam.get("success");
            String operation = (String) jsonParam.get("operation");//操作类型（created：新建/导入，updated:修改，deleted:删除）
            String objectType = (String) jsonParam.get("objectType");//对象类型（dept：部门，user：用户）
            Object data = jsonParam.get("data");//变更数据
            logger.info("[互联网认证用户数据同步] : {}"+success+"操作类型："+operation+",同步内容："+data);
            // Integer projectId = (Integer) jsonParam.get("projectId");//站点id
            try {
                if (success && StringUtils.isNotEmpty(objectType) && objectType.equals("user") && operation.equals("created")) {
                    JSONArray arrayData = JSONObject.parseArray(data.toString());
                    for (Object arrayDatum : arrayData) {
                        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(arrayDatum);
                        UserPojo userPojo = JSONObject.toJavaObject(jsonObject, UserPojo.class);
                        String userName = userPojo.getUserName();
                        try {
                            userName = new String(userName.getBytes(ENCODING_UTF8), ENCODING_UTF8);
                            userPojo.setUserName(userName);
                            created(userPojo);
                        } catch (Throwable e) {
                            logger.warn("[统一门户] 创建用户异常: {}", e.getMessage(), e);
                        }
                    }
                }

                if (success && StringUtils.isNotEmpty(objectType) && objectType.equals("user") && operation.equals("updated")) {
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
                    UserPojo userPojo = JSONObject.toJavaObject(jsonObject, UserPojo.class);
                    String userName = userPojo.getUserName();
                    try {
                        userName = new String(userName.getBytes(ENCODING_UTF8), ENCODING_UTF8);
                        userPojo.setUserName(userName);
                        updated(userPojo);
                    } catch (Throwable e) {
                        logger.warn("[统一门户] 更新用户异常: {}", e.getMessage(), e);
                    }
                }

                if (success && StringUtils.isNotEmpty(objectType) && objectType.equals("user") && operation.equals("deleted")) {
                    Map resultMap = getMapForJson(data.toString());
                    if (null != resultMap) {
                        List userInfoList = (ArrayList) resultMap.get("userDepts");
                        userInfoList.forEach(info -> {
                            try {
                                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(info);
                                Map deleteInfoMap = getMapForJson(jsonObject.toString());
                                Integer userId = (Integer) deleteInfoMap.get("userId");
                                String sysUserId=getSysUserId(CodeConstant.user_source.AUTHNET,userId.toString());
                                deleted(sysUserId);
                            } catch (Throwable e) {
                                logger.warn("[统一门户] 删除用户异常: {}", e.getMessage(), e);
                            }
                        });
                    }
                }
            } catch (Throwable e) {
                logger.warn("[统一门户] 消息处理异常: {}", e.getMessage(), e);
            }
        });
    }


    public void created(UserPojo userPojo) { ;
        User user = new User();
        user.setSource(CodeConstant.user_source.AUTHNET);
        user.setLoginName(userPojo.getUserNo());
        user.setName(userPojo.getUserName());
        //user.setId(userPojo.getUserId().toString());
        user.setId(getSysUserId(CodeConstant.user_source.AUTHNET,userPojo.getUserId().toString()));
        user.setOffice(officeService.getByCode(this.orgCode));
        user.setCompany(officeService.get(this.companyId));//总公司
        user.setPassword(SystemService.entryptPassword(this.defaultPassword));
        user.setNo(userPojo.getUserId().toString());
        user.setCreateDate(new Date());
        List<Role> roleList = Lists.newArrayList();
        Role role = systemService.getRoleByEnname(this.defaultRoleEnname);
        roleList.add(role);
        user.setRoleList(roleList);
        User userOld = systemService.getUserByLoginNameForAuth(userPojo.getUserNo(),CodeConstant.user_source.AUTHNET);

        if (null == userOld) {
            systemService.saveUserAuth(user);
        }
    }

    public void deleted(String userId) {
        User userOld = systemService.getUser(userId);
        systemService.deleteUser(userOld);//删除用户成功
    }

    public void updated(UserPojo userPojo) {
        User userOld = systemService.getUserByLoginNameForAuth(userPojo.getUserNo(),CodeConstant.user_source.AUTHNET);
        if (null != userOld) {
            userOld.setLoginName(userPojo.getUserNo());
            userOld.setName(userPojo.getUserName());
            List<Role> roleList = Lists.newArrayList();
            Role role = systemService.getRoleByEnname(this.defaultRoleEnname);
            roleList.add(role);
            userOld.setRoleList(roleList);
            systemService.saveUser(userOld);
        } else {
            created(userPojo);
        }
    }

    /**
     * Json 转成 Map<>
     *
     * @param jsonStr JSON字符串
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMapForJson(String jsonStr) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonStr, Map.class);
        } catch (Throwable e) {
            logger.warn("[统一门户] 解析JSON异常: {}", e.getMessage(), e);
        }
        return null;
    }


    public String getSysUserId(String source,  String userId){
        StringBuffer sb=new StringBuffer(source);
        sb.append("-").append(userId);
        return sb.toString();
    }
}
