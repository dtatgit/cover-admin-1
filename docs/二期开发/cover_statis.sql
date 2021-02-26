/*
Navicat MySQL Data Transfer

Source Server         : 本地开发数据库（新）
Source Server Version : 80021
Source Host           : 192.168.1.31:3306
Source Database       : cover

Target Server Type    : MYSQL
Target Server Version : 80021
File Encoding         : 65001

Date: 2021-02-20 08:53:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `cover_statis`
-- ----------------------------
DROP TABLE IF EXISTS `cover_statis`;
CREATE TABLE `cover_statis` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注信息',
  `del_flag` varchar(64) DEFAULT NULL COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `district` varchar(64) DEFAULT NULL COMMENT '区域',
  `cover_type` varchar(64) DEFAULT NULL COMMENT '井盖类型',
  `owner_depart` varchar(64) DEFAULT NULL COMMENT '权属单位',
  `cover_num` varchar(64) DEFAULT NULL COMMENT '井盖数',
  `install_equ` varchar(64) DEFAULT NULL COMMENT '已安装设备数',
  `online_num` varchar(64) DEFAULT NULL COMMENT '当前在线数',
  `offline_num` varchar(64) DEFAULT NULL COMMENT '当前离线数',
  `cover_alarm_num` varchar(64) DEFAULT NULL COMMENT '报警井盖数',
  `alarm_total_num` varchar(64) DEFAULT NULL COMMENT '报警总数',
  `add_work_num` varchar(64) DEFAULT NULL COMMENT '工单总数（当天新增）',
  `complete_work_num` varchar(64) DEFAULT NULL COMMENT '已完成工单总数（当天）',
  `pro_work_num` varchar(64) DEFAULT NULL COMMENT '未完成工单总数（累计）',
  `statis_time` varchar(64) DEFAULT NULL COMMENT '统计时间',
  `flag` varchar(64) DEFAULT NULL COMMENT '信息标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='井盖相关统计';

ALTER TABLE `cover_statis`
  ADD COLUMN `work_num_total` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `flag`,
  ADD COLUMN `complete_work_num_total` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `work_num_total`;
