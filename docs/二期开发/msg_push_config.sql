/*
Navicat MySQL Data Transfer

Source Server         : 本地开发数据库（新）
Source Server Version : 80021
Source Host           : 192.168.1.31:3306
Source Database       : cover

Target Server Type    : MYSQL
Target Server Version : 80021
File Encoding         : 65001

Date: 2021-02-20 08:54:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `msg_push_config`
-- ----------------------------
DROP TABLE IF EXISTS `msg_push_config`;
CREATE TABLE `msg_push_config` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(64) DEFAULT NULL COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `title` varchar(64) DEFAULT NULL COMMENT '通知标题',
  `state` varchar(64) DEFAULT NULL COMMENT '使用状态',
  `notice_type` varchar(64) DEFAULT NULL COMMENT '通知类型',
  `push_mode` varchar(64) DEFAULT NULL COMMENT '推送方式',
  `notice_person` varchar(64) DEFAULT NULL COMMENT '通知人员',
  `create_user_name` varchar(64) DEFAULT NULL COMMENT '创建用户',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注信息',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息推送配置';

