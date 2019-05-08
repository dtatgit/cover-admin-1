/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.entity.equinfo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 井盖基础信息Entity
 * @author crj
 * @version 2019-04-19
 */
public class Cover extends DataEntity<Cover> {
	
	private static final long serialVersionUID = 1L;
	private String coverStatus;		// 井盖状态
	private String no;		// 编号
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
	private String coordinateType;		// 坐标类型：gcj02: 国测局坐标系gps: WGS-84
	private String longitude;		// 经度
	private String latitude;		// 纬度
	private String altitude;		// 海拔（m）
	private String locationAccuracy;		// 定位精度（m）
	private String altitudeAccuracy;		// 海拔精度（m）
	private String purpose;		// 井位用途
	private String situation;		// 井位地理场合
	private String manufacturer;		// 制造商
	private String sizeSpec;		// 尺寸规格D800 : 圆形直径800mmR800x600 : 矩形 H800（长）W600（宽）
	private String sizeRule;		// 井盖规格（尺寸类型）
	private String sizeDiameter;		// 尺寸：直径（mm）
	private String sizeRadius;		// 尺寸：半径（mm）** 已废弃，使用diameter字段 **
	private String sizeLength;		// 尺寸：长度（mm）
	private String sizeWidth;		// 尺寸：宽度（mm）
	private String material;		// 井盖材质
	private String ownerDepart;		// 权属单位
	private String superviseDepart;		// 监管单位
	private String marker;		// 地图标记
	private String isDamaged;		// 是否损毁
	private String manholeDamageDegree;		// 井筒破损深度（m）
	private String damageRemark;		// 损毁情况备注
	private String altitudeIntercept;		// 高度差，井中心与周边路面（1.5m范围）
	private String auditBy;		// 审核人
	private Date auditDate;		// 审核时间
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private String beginLongitude;		// 开始 经度
	private String endLongitude;		// 结束 经度
	private String beginLatitude;		// 开始 纬度
	private String endLatitude;		// 结束 纬度
	private List<CoverImage> CoverImageList;//井盖图片信息

	private List<CoverDamage> coverDamageList;//井盖损坏形式

	public Cover() {
		super();
	}

	public Cover(String id){
		super(id);
	}

	@ExcelField(title="井盖状态", dictType="cover_status", align=2, sort=1)
	public String getCoverStatus() {
		return coverStatus;
	}

	public void setCoverStatus(String coverStatus) {
		this.coverStatus = coverStatus;
	}
	
	@ExcelField(title="编号", align=2, sort=2)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
	
	@ExcelField(title="井盖类型", dictType="cover_type", align=2, sort=3)
	public String getCoverType() {
		return coverType;
	}

	public void setCoverType(String coverType) {
		this.coverType = coverType;
	}
	
	@ExcelField(title="地址：省", align=2, sort=4)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@ExcelField(title="地址：市", align=2, sort=5)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@ExcelField(title="地址：城市代码（0516）", align=2, sort=6)
	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	@ExcelField(title="地址：行政区划代码（320312）", align=2, sort=7)
	public String getAdCode() {
		return adCode;
	}

	public void setAdCode(String adCode) {
		this.adCode = adCode;
	}
	
	@ExcelField(title="地址：区", align=2, sort=8)
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
	
	@ExcelField(title="地址：街道（办事处）", align=2, sort=9)
	public String getTownship() {
		return township;
	}

	public void setTownship(String township) {
		this.township = township;
	}
	
	@ExcelField(title="地址：路（街巷）", align=2, sort=10)
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	@ExcelField(title="地址：门牌号", align=2, sort=11)
	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
	
	@ExcelField(title="地址：详细地址", align=2, sort=12)
	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}
	
	@ExcelField(title="坐标类型：gcj02: 国测局坐标系gps: WGS-84", align=2, sort=13)
	public String getCoordinateType() {
		return coordinateType;
	}

	public void setCoordinateType(String coordinateType) {
		this.coordinateType = coordinateType;
	}
	
	@ExcelField(title="经度", align=2, sort=14)
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	@ExcelField(title="纬度", align=2, sort=15)
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	@ExcelField(title="海拔（m）", align=2, sort=16)
	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	
	@ExcelField(title="定位精度（m）", align=2, sort=17)
	public String getLocationAccuracy() {
		return locationAccuracy;
	}

	public void setLocationAccuracy(String locationAccuracy) {
		this.locationAccuracy = locationAccuracy;
	}
	
	@ExcelField(title="海拔精度（m）", align=2, sort=18)
	public String getAltitudeAccuracy() {
		return altitudeAccuracy;
	}

	public void setAltitudeAccuracy(String altitudeAccuracy) {
		this.altitudeAccuracy = altitudeAccuracy;
	}
	
	@ExcelField(title="井位用途", dictType="cover_purpose", align=2, sort=19)
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	@ExcelField(title="井位地理场合", dictType="cover_situation", align=2, sort=20)
	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}
	
	@ExcelField(title="制造商", align=2, sort=21)
	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	@ExcelField(title="尺寸规格D800 : 圆形直径800mmR800x600 : 矩形 H800（长）W600（宽）", dictType="cover_size_spec", align=2, sort=22)
	public String getSizeSpec() {
		return sizeSpec;
	}

	public void setSizeSpec(String sizeSpec) {
		this.sizeSpec = sizeSpec;
	}
	
	@ExcelField(title="井盖规格（尺寸类型）", dictType="cover_size_rule", align=2, sort=23)
	public String getSizeRule() {
		return sizeRule;
	}

	public void setSizeRule(String sizeRule) {
		this.sizeRule = sizeRule;
	}
	
	@ExcelField(title="尺寸：直径（mm）", align=2, sort=24)
	public String getSizeDiameter() {
		return sizeDiameter;
	}

	public void setSizeDiameter(String sizeDiameter) {
		this.sizeDiameter = sizeDiameter;
	}
	
	@ExcelField(title="尺寸：半径（mm）** 已废弃，使用diameter字段 **", align=2, sort=25)
	public String getSizeRadius() {
		return sizeRadius;
	}

	public void setSizeRadius(String sizeRadius) {
		this.sizeRadius = sizeRadius;
	}
	
	@ExcelField(title="尺寸：长度（mm）", align=2, sort=26)
	public String getSizeLength() {
		return sizeLength;
	}

	public void setSizeLength(String sizeLength) {
		this.sizeLength = sizeLength;
	}
	
	@ExcelField(title="尺寸：宽度（mm）", align=2, sort=27)
	public String getSizeWidth() {
		return sizeWidth;
	}

	public void setSizeWidth(String sizeWidth) {
		this.sizeWidth = sizeWidth;
	}
	
	@ExcelField(title="井盖材质", dictType="cover_material", align=2, sort=28)
	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}
	
	@ExcelField(title="权属单位", dictType="cover_owner_depart", align=2, sort=29)
	public String getOwnerDepart() {
		return ownerDepart;
	}

	public void setOwnerDepart(String ownerDepart) {
		this.ownerDepart = ownerDepart;
	}
	
	@ExcelField(title="监管单位", align=2, sort=30)
	public String getSuperviseDepart() {
		return superviseDepart;
	}

	public void setSuperviseDepart(String superviseDepart) {
		this.superviseDepart = superviseDepart;
	}
	
	@ExcelField(title="地图标记", dictType="cover_damage", align=2, sort=31)
	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}
	
	@ExcelField(title="是否损毁", dictType="boolean", align=2, sort=32)
	public String getIsDamaged() {
		return isDamaged;
	}

	public void setIsDamaged(String isDamaged) {
		this.isDamaged = isDamaged;
	}
	
	@ExcelField(title="井筒破损深度（m）", align=2, sort=33)
	public String getManholeDamageDegree() {
		return manholeDamageDegree;
	}

	public void setManholeDamageDegree(String manholeDamageDegree) {
		this.manholeDamageDegree = manholeDamageDegree;
	}
	
	@ExcelField(title="损毁情况备注", align=2, sort=34)
	public String getDamageRemark() {
		return damageRemark;
	}

	public void setDamageRemark(String damageRemark) {
		this.damageRemark = damageRemark;
	}
	
	@ExcelField(title="高度差，井中心与周边路面（1.5m范围）", dictType="cover_altitude_intercept", align=2, sort=35)
	public String getAltitudeIntercept() {
		return altitudeIntercept;
	}

	public void setAltitudeIntercept(String altitudeIntercept) {
		this.altitudeIntercept = altitudeIntercept;
	}
	
	@ExcelField(title="审核人", align=2, sort=40)
	public String getAuditBy() {
		return auditBy;
	}

	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="审核时间", align=2, sort=41)
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

	public List<CoverImage> getCoverImageList() {
		return CoverImageList;
	}

	public void setCoverImageList(List<CoverImage> coverImageList) {
		CoverImageList = coverImageList;
	}

	public List<CoverDamage> getCoverDamageList() {
		return coverDamageList;
	}

	public void setCoverDamageList(List<CoverDamage> coverDamageList) {
		this.coverDamageList = coverDamageList;
	}

	public String getBeginLongitude() {
		return beginLongitude;
	}

	public void setBeginLongitude(String beginLongitude) {
		this.beginLongitude = beginLongitude;
	}

	public String getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(String endLongitude) {
		this.endLongitude = endLongitude;
	}

	public String getBeginLatitude() {
		return beginLatitude;
	}

	public void setBeginLatitude(String beginLatitude) {
		this.beginLatitude = beginLatitude;
	}

	public String getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(String endLatitude) {
		this.endLatitude = endLatitude;
	}
}