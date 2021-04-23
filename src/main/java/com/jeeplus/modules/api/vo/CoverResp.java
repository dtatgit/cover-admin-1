package com.jeeplus.modules.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.jeeplus.modules.cv.entity.equinfo.CoverImage;
import com.jeeplus.modules.cv.entity.equinfo.CoverOwner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CoverResp {

    private String id;
    private String no;		// 编号
    private String tagNo; //标签编号
    private String coverStatus;
    private String coverType;		// 井盖类型
    private String province;		// 地址：省
    private String city;		// 地址：市
    private String cityCode;		// 地址：城市代码（0516）
    private String adCode;		// 地址：行政区划代码（320312）
    private String district;		// 地址：区
    private String township;		// 地址：街道（办事处）
    private String street;		// 地址：路（街巷）
    private String streetNumber;		// 地址：门牌号
    private String addressDetail;		// 地址：详细地址
    private BigDecimal longitude;		// 经度
    private BigDecimal latitude;		// 纬度
    private BigDecimal altitude;		// 海拔（m）
    private BigDecimal locationAccuracy;		// 定位精度（m）
    private BigDecimal altitudeAccuracy;		// 海拔精度（m）
    private String purpose;		// 管网用途
    private String situation;		// 井位地理场合
    private String manufacturer;		// 制造商
    private String sizeSpec;		// 尺寸规格D800 : 圆形直径800mmR800x600 : 矩形 H800（长）W600（宽）
    private String sizeRule;		// 井盖规格（尺寸类型）
    private BigDecimal sizeDiameter;		// 尺寸：直径（mm）
    private BigDecimal sizeLength;		// 尺寸：长度（mm）
    private BigDecimal sizeWidth;		// 尺寸：宽度（mm）
    private String material;		// 井盖材质
    private String ownerDepart;		// 权属单位
    private String superviseDepart;		// 监管单位
    private String jurisdiction; //辖区
    private String marker;		// 地图标记
    private String isDamaged;		// 是否损毁

    private BigDecimal manholeDamageDegree;		// 井筒破损深度（m）
    private String damageRemark;		// 损毁情况备注
    private BigDecimal altitudeIntercept;		// 高度差，井中心与周边路面（1.5m范围）

    private List<CoverImage> coverImageList;//井盖图片信息
    private List<CoverOwner> coverOwnerList = Lists.newArrayList();		// 子表列表
    private List<DamageResp> coverDamageList = Lists.newArrayList();		// 子表列表

    private List<GuardResp> guardList = Lists.newArrayList();		// 子表列表
    //private GuardResp guard;

    protected String remarks;	// 备注
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createDate;	// 创建日期

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTagNo() {
        return tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
    }

    public String getCoverStatus() {
        return coverStatus;
    }

    public void setCoverStatus(String coverStatus) {
        this.coverStatus = coverStatus;
    }

    public String getCoverType() {
        return coverType;
    }

    public void setCoverType(String coverType) {
        this.coverType = coverType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTownship() {
        return township;
    }

    public void setTownship(String township) {
        this.township = township;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getAltitude() {
        return altitude;
    }

    public void setAltitude(BigDecimal altitude) {
        this.altitude = altitude;
    }

    public BigDecimal getLocationAccuracy() {
        return locationAccuracy;
    }

    public void setLocationAccuracy(BigDecimal locationAccuracy) {
        this.locationAccuracy = locationAccuracy;
    }

    public BigDecimal getAltitudeAccuracy() {
        return altitudeAccuracy;
    }

    public void setAltitudeAccuracy(BigDecimal altitudeAccuracy) {
        this.altitudeAccuracy = altitudeAccuracy;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSizeSpec() {
        return sizeSpec;
    }

    public void setSizeSpec(String sizeSpec) {
        this.sizeSpec = sizeSpec;
    }

    public String getSizeRule() {
        return sizeRule;
    }

    public void setSizeRule(String sizeRule) {
        this.sizeRule = sizeRule;
    }

    public BigDecimal getSizeDiameter() {
        return sizeDiameter;
    }

    public void setSizeDiameter(BigDecimal sizeDiameter) {
        this.sizeDiameter = sizeDiameter;
    }

    public BigDecimal getSizeLength() {
        return sizeLength;
    }

    public void setSizeLength(BigDecimal sizeLength) {
        this.sizeLength = sizeLength;
    }

    public BigDecimal getSizeWidth() {
        return sizeWidth;
    }

    public void setSizeWidth(BigDecimal sizeWidth) {
        this.sizeWidth = sizeWidth;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getOwnerDepart() {
        return ownerDepart;
    }

    public void setOwnerDepart(String ownerDepart) {
        this.ownerDepart = ownerDepart;
    }

    public List<CoverOwner> getCoverOwnerList() {
        return coverOwnerList;
    }

    public void setCoverOwnerList(List<CoverOwner> coverOwnerList) {
        this.coverOwnerList = coverOwnerList;
    }

    public String getSuperviseDepart() {
        return superviseDepart;
    }

    public void setSuperviseDepart(String superviseDepart) {
        this.superviseDepart = superviseDepart;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public String getIsDamaged() {
        return isDamaged;
    }

    public void setIsDamaged(String isDamaged) {
        this.isDamaged = isDamaged;
    }

    public List<DamageResp> getCoverDamageList() {
        return coverDamageList;
    }

    public void setCoverDamageList(List<DamageResp> coverDamageList) {
        this.coverDamageList = coverDamageList;
    }

    public BigDecimal getManholeDamageDegree() {
        return manholeDamageDegree;
    }

    public void setManholeDamageDegree(BigDecimal manholeDamageDegree) {
        this.manholeDamageDegree = manholeDamageDegree;
    }

    public String getDamageRemark() {
        return damageRemark;
    }

    public void setDamageRemark(String damageRemark) {
        this.damageRemark = damageRemark;
    }

    public BigDecimal getAltitudeIntercept() {
        return altitudeIntercept;
    }

    public void setAltitudeIntercept(BigDecimal altitudeIntercept) {
        this.altitudeIntercept = altitudeIntercept;
    }

    public List<CoverImage> getCoverImageList() {
        return coverImageList;
    }

    public void setCoverImageList(List<CoverImage> coverImageList) {
        this.coverImageList = coverImageList;
    }

//    public GuardResp getGuard() {
//        return guard;
//    }
//
//    public void setGuard(GuardResp guard) {
//        this.guard = guard;
//    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<GuardResp> getGuardList() {
        return guardList;
    }

    public void setGuardList(List<GuardResp> guardList) {
        this.guardList = guardList;
    }
}
