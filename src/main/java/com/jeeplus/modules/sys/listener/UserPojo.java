package com.jeeplus.modules.sys.listener;

/**
 * 大兴用户同步
 */
public class UserPojo {
    private Long userId;//用户ID
    private String userName;//用户姓名
    private String userNo;//用户编号（登录名）
    private String createTime;//创建时间
    private Long deptId;//部门ID
    private Long projectId;//站点ID
    private String userGrade;//用户等级
    private Integer version;//版本号
    private Boolean status;//true：启用，false：禁用
    //private String cardNo;//用户卡号
    private Short sex;//性别（0:未说明，1:男，2:女）
//    telephone String 手机号
//    address String 联系人地址
//    email String 联系人邮箱
//    birthday Date 出生年月
//    userGrade Short 用户等级
//    postId Integer 职务标识
//    type Short 用户类型
//    remark String 备注
//    officeTel String 办公室电话
//    iconFilePath String 头像路径
//    createTime Date 创建时间

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getUserGrade() {
        return userGrade;
    }

    public void setUserGrade(String userGrade) {
        this.userGrade = userGrade;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

//    public String getCardNo() {
//        return cardNo;
//    }
//
//    public void setCardNo(String cardNo) {
//        this.cardNo = cardNo;
//    }

    public Short getSex() {
        return sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }
}
