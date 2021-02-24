/*
Navicat MySQL Data Transfer

Source Server         : 本地开发数据库（新）
Source Server Version : 80021
Source Host           : 192.168.1.31:3306
Source Database       : cover

Target Server Type    : MYSQL
Target Server Version : 80021
File Encoding         : 65001

Date: 2021-02-20 08:55:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `cover_work_inspection`
-- ----------------------------
DROP TABLE IF EXISTS `cover_work_inspection`;
CREATE TABLE `cover_work_inspection` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(64) DEFAULT NULL COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `cover_work_num` varchar(64) DEFAULT NULL COMMENT '工单编号',
  `cover_work_id` varchar(64) DEFAULT NULL COMMENT '工单ID',
  `area` varchar(64) DEFAULT NULL COMMENT '所属区域',
  `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
  `signin_person_id` varchar(64) DEFAULT NULL COMMENT '签到人',
  `signin_person_name` varchar(64) DEFAULT NULL COMMENT '签到人',
  `signin_time` datetime DEFAULT NULL COMMENT '签到时间',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工单巡检';

