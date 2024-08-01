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
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    accessKey    varchar(512)                           not null comment 'accessKey',
    secretKey    varchar(512)                           not null comment 'secretKey',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';

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

insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('余远航', '严锦程', 'www.stephan-hettinger.io', '叶浩宇', '段浩宇', '袁文昊', 0, 4);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('吕博超', '潘锦程', 'www.paris-kohler.org', '宋鹏', '朱展鹏', '熊晟睿', 0, 3);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('谢志强', '许正豪', 'www.dexter-dickinson.info', '彭明哲', '傅聪健', '叶潇然', 0, 10414);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('陈弘文', '许钰轩', 'www.salome-von.biz', '胡金鑫', '蒋健雄', '崔昊强', 0, 141);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('韩彬', '何语堂', 'www.diedra-moore.biz', '田苑博', '覃绍辉', '许鸿煊', 0, 931578);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('张懿轩', '魏昊然', 'www.katerine-wolf.info', '江锦程', '熊鑫鹏', '顾烨霖', 0, 838728);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('郝风华', '戴烨霖', 'www.elliott-hansen.biz', '陈黎昕', '叶雨泽', '彭擎苍', 0, 39);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('石烨磊', '谭黎昕', 'www.zona-smith.info', '孙思', '邓文轩', '孟越彬', 0, 38);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('顾琪', '彭修杰', 'www.mike-connelly.net', '袁泽洋', '袁峻熙', '钱鹏煊', 0, 775244);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('姚航', '孔昊然', 'www.rochel-koepp.net', '刘昊焱', '薛炎彬', '陆黎昕', 0, 337);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('张旭尧', '刘天翊', 'www.jazmin-rutherford.io', '彭浩宇', '雷明哲', '胡胤祥', 0, 63857845);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('李浩轩', '廖思源', 'www.adrian-kshlerin.io', '曹明杰', '胡文博', '钟雨泽', 0, 9264722);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('邵明哲', '沈楷瑞', 'www.argentina-mosciski.org', '冯彬', '蔡明', '廖烨磊', 0, 46);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('曾潇然', '高浩轩', 'www.herb-blick.com', '钟雨泽', '蒋潇然', '秦钰轩', 0, 6);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('万瑾瑜', '崔绍齐', 'www.jeffry-lindgren.org', '韩航', '杨天宇', '江文博', 0, 5365);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('蒋文轩', '万熠彤', 'www.mohamed-padberg.biz', '冯昊焱', '郑哲瀚', '卢煜城', 0, 58008276);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('林晓博', '潘鹏煊', 'www.mignon-kohler.name', '赖伟泽', '于越泽', '潘天磊', 0, 84);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('宋天宇', '严越泽', 'www.verlene-von.co', '方峻熙', '金鹤轩', '史鑫磊', 0, 594215);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('程弘文', '潘文昊', 'www.frankie-romaguera.com', '郑修洁', '姚晋鹏', '任天宇', 0, 76);
insert into `interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `status`,
                              `userId`)
values ('贺弘文', '雷炫明', 'www.nestor-cormier.org', '杨智渊', '蒋越泽', '郑雪松', 0, 7510253);


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