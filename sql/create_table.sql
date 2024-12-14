-- 创建数据库
create database if not exists yllj_sql;

-- 切换库
use yllj_sql;

-- 用户表
create table if not exists user
(
    id						bigint auto_increment comment 'id' primary key,
    userAccount		varchar(256)			not null comment '用户账号',
    userPassword	varchar(512)			not null comment '密文密码',
    userName			varchar(256)			null comment '用户昵称',
    userAvatar		varchar(1024)			null comment '用户头像',
    userProfile  	varchar(512)			null comment '用户简介',
    userRole 			varchar(256) default 'user' not null comment '用户角色 user/admin',

    editTime 			datetime  default CURRENT_TIMESTAMP  not null comment '最后编辑时间',
    createTime  	datetime  default CURRENT_TIMESTAMP  not null comment '创建时间',
    updateTime    datetime  default CURRENT_TIMESTAMP  not null comment '最后更新时间',
#     editBy				bigint 						not null comment '最后编辑人',
#     createBy			bigint						not null comment '创建人',
#     updateBy 			bigint						not null comment '最后更新人',
    removeFlag		TINYINT		default 0		not null comment '是否删除 0-否 1-是',
    UNIQUE KEY uk_userAccount(userAccount),
    INDEX idx_userName(userName)
    ) comment '用户表' collate = utf8mb4_unicode_ci;