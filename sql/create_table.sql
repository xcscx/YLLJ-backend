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

-- 图片表
create table if not exists picture
(
    id          bigint auto_increment comment 'id' primary key,
    url         varchar(512)                not null comment '图片url',
    name        varchar(128)                not null comment '图片名称',
    introduction    varchar(512)            null comment '简介',
    category        varchar(64)             null comment '分类',
    tags            varchar(512)            null comment '标签（JSON数组）',
    picSize         bigint                  null comment '图片体积',
    picWidth        int                     null comment '图片宽度',
    picHeight       int                     null comment '图片高度',
    picScale        double                  null comment '图片宽高比',
    picFormat       varchar(32)             null comment '图片格式',
    userId          bigint                  not null comment '创建用户 id',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    editTime        datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint  default 0      not null comment '是否删除 0-否 1-是',
    INDEX idx_name(name),       -- 提升基于图片名称的查询性能
    INDEX idx_introduction(introduction),       -- 用于模糊搜索图片简介
    INDEX idx_category(category),       -- 提升基于分类的查询性能
    INDEX idx_tags(tags),       -- 提升基于标签的查询性能
    INDEX idx_userId(userId)        -- 提升基于用户ID的查询性能
)comment '图片' collate = utf8mb4_unicode_ci;

