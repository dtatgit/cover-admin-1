package com.jeeplus.modules.sys.listener;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.antu.mq.activemq.ActivemqAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.time.LocalDateTime;
import java.util.*;

//@Service
public class TopicMessageListen  {
    private static Logger logger = LoggerFactory.getLogger(TopicMessageListen.class);
    public static String ENCODING_UTF8 = "UTF-8";
    @Autowired
    private SystemService systemService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    public TopicMessageListen(ActivemqAdapter adapter, @Value("${activemq_topic}") String subscribeTopic) {
        adapter.addListener(subscribeTopic, (t, m) -> {
            JSONObject jsonParam = JSON.parseObject(new String(m.getPayload()));
            boolean success= (boolean)jsonParam.get("success");
            String operation= (String)jsonParam.get("operation");//操作类型（created：新建/导入，updated:修改，deleted:删除）
            String objectType= (String)jsonParam.get("objectType");//对象类型（dept：部门，user：用户）
            Object data= jsonParam.get("data");//变更数据
            Integer projectId= (Integer)jsonParam.get("projectId");//站点id
            try{
            if(success&&StringUtils.isNotEmpty(objectType)&&objectType.equals("user")&&operation.equals("created")){
                JSONArray arrayData= JSONObject.parseArray(data.toString());
                Iterator<Object> it = arrayData.iterator();
                while (it.hasNext()) {
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(it.next());
                    UserPojo userPojo = JSONObject.toJavaObject(jsonObject, UserPojo.class);
                    String userName = userPojo.getUserName();
                    try{
                        userName = new String(userName.getBytes(ENCODING_UTF8), ENCODING_UTF8);
                        userPojo.setUserName(userName);
                        created(userPojo);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }


            }
            if(success&&StringUtils.isNotEmpty(objectType)&&objectType.equals("user")&&operation.equals("updated")){
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
                UserPojo userPojo = JSONObject.toJavaObject(jsonObject, UserPojo.class);
                String userName = userPojo.getUserName();
                try{
                userName = new String(userName.getBytes(ENCODING_UTF8), ENCODING_UTF8);
                userPojo.setUserName(userName);
                    updated(userPojo);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            if(success&&StringUtils.isNotEmpty(objectType)&&objectType.equals("user")&&operation.equals("deleted")){
                Map  resultMap=getMapForJson(data.toString());
                if(null!=resultMap){
                    List  userInfoList= (ArrayList)resultMap.get("userDepts");
                    userInfoList.forEach(info-> {
                        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(info);
                        Map  deleteInfoMap=getMapForJson(jsonObject.toString());
                        Integer userId = (Integer) deleteInfoMap.get("userId");
                        System.out.println("***********" + userId);
                        System.out.println("***********" + userId);
                        System.out.println("***********" + userId);
                        deleted(userId.toString());
                    });
                }
            }

            }catch (Exception e){
                e.printStackTrace();
            }

        });
    }



    public void created(UserPojo userPojo){
        User user=new User();
        user.setSource(CodeConstant.user_source.AUTH);
        user.setLoginName(userPojo.getUserNo());
        user.setName(userPojo.getUserName());
        user.setId(userPojo.getUserId().toString());
        user.setOffice(officeService.getByCode("18"));
        user.setCompany(officeService.get("1"));//总公司
        user.setPassword(SystemService.entryptPassword("123456"));
        user.setNo(userPojo.getUserId().toString());
        user.setCreateDate(new Date());
        List<Role> roleList = Lists.newArrayList();
        Role role=systemService.getRoleByEnname("auth");
        roleList.add(role);
        user.setRoleList(roleList);
        User userOld = systemService.getUserByLoginNameForAuth(userPojo.getUserNo());

        if(null==userOld){
            systemService.saveUserAuth(user);
        }

    }

    public void deleted(String userId){
        User userOld = systemService.getUser(userId);
        systemService.deleteUser(userOld);//删除用户成功
    }

    public void updated(UserPojo userPojo){
        User userOld = systemService.getUserByLoginNameForAuth(userPojo.getUserNo());
        if(null!=userOld){
            userOld.setLoginName(userPojo.getUserNo());
            userOld.setName(userPojo.getUserName());
            List<Role> roleList = Lists.newArrayList();
            Role role=systemService.getRoleByEnname("auth");
            roleList.add(role);
            userOld.setRoleList(roleList);
            systemService.saveUser(userOld);
        }else{
            created(userPojo);
        }

    }

    /**
     * Json 转成 Map<>
     * @param jsonStr
     * @return
     */
    public  Map<String, Object> getMapForJson(String jsonStr){

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map  resultMap = mapper.readValue(jsonStr, Map.class);
            return resultMap;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }
}
