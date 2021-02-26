ALTER TABLE cover.sys_user ADD source varchar(20);
ALTER TABLE cover.sys_user MODIFY COLUMN source varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT 'sys' NOT NULL COMMENT '用户来源';
