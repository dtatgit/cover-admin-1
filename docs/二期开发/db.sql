
/* cover表新增虚拟井盖*/
INSERT INTO `cover` (`ID`, `no`, `tag_no`, `cover_type`, `province`, `city`, `city_code`, `ad_code`, `district`, `district_origin`, `township`, `street`, `street_number`, `address_detail`, `coordinate_type`, `longitude`, `latitude`, `altitude`, `wgs84_x`, `wgs84_y`, `location_accuracy`, `altitude_accuracy`, `purpose`, `situation`, `manufacturer`, `size_spec`, `size_rule`, `size_diameter`, `size_radius`, `size_length`, `size_width`, `material`, `owner_depart`, `supervise_depart`, `marker`, `is_damaged`, `manhole_damage_degree`, `damage_remark`, `altitude_intercept`, `data_source`, `cover_status`, `create_by`, `create_date`, `update_by`, `update_date`, `audit_by`, `audit_date`, `del_flag`, `remarks`, `is_gwo`, `flag`, `jurisdiction`) VALUES('c1','001',NULL,'虚拟井盖','江苏省','徐州市','0516','320302','鼓楼区',NULL,'','','','',NULL,'0','0',NULL,'0','0',NULL,NULL,'','','','','1','0',NULL,NULL,NULL,'','sys','sys','','N',NULL,'','0','gather','audit_pass','1','2020-12-06 15:33:12','1','2020-12-18 10:03:22',NULL,NULL,'0','','N',NULL,'sys');


/* exception_report表新增字段*/
ALTER TABLE  exception_report ADD  lng  DECIMAL(10,7) NULL COMMENT '经度'; 
ALTER TABLE  exception_report ADD  lat  DECIMAL(10,7) NULL COMMENT '纬度'; 
ALTER TABLE  exception_report ADD  create_work_id  VARCHAR(64) DEFAULT NULL COMMENT '生成工单号'; 


/* 新增cover_work_attr （工单对应属性表）*/
 CREATE TABLE `cover_work_attr` (
  `id` VARCHAR(65) NOT NULL COMMENT '主键',
  `cover_work_id` VARCHAR(65) DEFAULT NULL COMMENT '工单ID',
  `title` VARCHAR(65) DEFAULT NULL COMMENT '标题',
  `name` VARCHAR(65) DEFAULT NULL COMMENT '名称',
  `value` VARCHAR(65) DEFAULT NULL COMMENT '值',
  `data_type` VARCHAR(65) DEFAULT NULL COMMENT '数据类型',
  `no` INT(65) DEFAULT NULL COMMENT '序号',
  `create_by` VARCHAR(65) DEFAULT NULL COMMENT '创建者',
  `create_date` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
  `update_date` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remarks` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注信息',
  `del_flag` VARCHAR(64) DEFAULT NULL COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='工单属性'


 
 