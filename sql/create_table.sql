# 数据库初始化

-- 创建库
create database if not exists gbbdxstxapi_db;

-- 切换库
use gbbdxstxapi_db;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    accessKey    varchar(512)                           not null comment 'accessKey',
    secretKey    varchar(512)                           not null comment 'secretKey',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '用户' collate = utf8mb4_unicode_ci;

-- 接口信息表
create table if not exists `interface_info`
(
    `id`             bigint                             not null auto_increment comment '主键' primary key,
    `name`           varchar(256)                       not null comment '接口名称',
    `description`    varchar(256)                       null comment '接口描述',
    `url`            varchar(512)                       not null comment '接口地址',
    `method`         varchar(256)                       not null comment '请求方法',
    `requestParams`  varchar(256)                       not null comment '请求参数',
    `requestHeader`  text                               null comment '请求头',
    `responseHeader` text                               null comment '响应头',
    `status`         int      default 0                 not null comment '接口状态 0-关闭(默认) 1-打开',
    `userId`         bigint                             not null comment '创建人',
    `createTime`     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDeleted`      tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '接口信息表';

-- 用户接口关系表
create table if not exists `user_interface_info`
(
    `id`              bigint                             not null auto_increment comment '主键' primary key,
    `userId`          bigint                             not null comment '用户id',
    `interfaceInfoId` bigint                             not null comment '接口id',
    `totalNum`        int      default 0                 not null comment '拥有调用次数',
    `invokeNum`       int      default 0                 not null comment '已调用次数',
    `status`          int      default 0                 not null comment '0-正常(默认) 1-禁用(次数用光)',
    `createTime`      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDeleted`       tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户接口关系表';