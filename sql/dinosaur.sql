-- ----------------------------
-- 小说表
-- ----------------------------
drop table if exists per_novel;
create table per_novel
(
  novel_id    bigint(20) not null auto_increment comment '小说id',
  book_name   varchar(40)  default null comment '书名',
  book_author varchar(20)  default null comment '作者',
  book_code   varchar(50)  default null comment '书籍code',
  book_status varchar(50)  default null comment '书籍状态',
  book_image  varchar(80)  default null comment '书籍图片',
  book_intro  varchar(2000) default null comment '书籍简介',
  cate_type   tinyint(2)   default NULL comment '书籍分类（0=完本,1=玄幻,2=武侠,3=都市,4=历史,5=网游,6=科幻,7=女生）',
	cate_name   varchar(80)  default null comment '分类名称',
  del_flag    varchar(2)   default 'N'  comment '删除标志（N代表存在 Y代表删除）',
  create_by   varchar(64)  default ''   comment '创建者',
  create_time datetime comment '创建时间',
  update_by   varchar(64)  default '' comment '更新者',
  update_time datetime comment '更新时间',
  primary key (novel_id)
) engine = innodb
  auto_increment = 200 comment = '小说表';

-- ----------------------------
-- 小说章节表
-- ----------------------------
drop table if exists per_catalogue;
create table per_catalogue
(
  catalogue_id   bigint(20) not null auto_increment comment '章节id',
  novel_id       bigint(20) not null comment '小说id',
  catalogue_name varchar(80) default null comment '章节名称',
  catalogue_code integer     default null comment '章节编码',
  catalogue_text mediumtext COMMENT '章节内容',
  del_flag       varchar(2)  default 'N' comment '删除标志（N代表存在 Y代表删除）',
  create_by      varchar(64) default '' comment '创建者',
  create_time    datetime comment '创建时间',
  update_by      varchar(64) default '' comment '更新者',
  update_time    datetime comment '更新时间',
  primary key (catalogue_id)
) engine = innodb
  auto_increment = 200 comment = '小说章节表';

-- ----------------------------
-- 相册表
-- ----------------------------
drop table if exists per_photo;
create table per_photo
(
  photo_id     bigint(20)  not null auto_increment comment '相册id',
  parent_id    bigint(20)   default NULL comment '父级id',
  user_id      bigint(20)  not null comment '相册所属人id',
  photo_name   varchar(60) not null comment '相册名称',
  remark       varchar(255) default NULL comment '备注',
  visible_type tinyint(2)   default NULL comment '是否公开(0=公开,1=朋友可见,3=私人可见)',
  del_flag     varchar(2)   default 'N' comment '删除标志（N代表存在 Y代表删除）',
  create_by    varchar(64)  default '' comment '创建者',
  create_time  datetime comment '创建时间',
  update_by    varchar(64)  default '' comment '更新者',
  update_time  datetime comment '更新时间',
  primary key (photo_id)
) engine = innodb
  auto_increment = 200 comment = '相册表';

-- ----------------------------
-- 文件表
-- ----------------------------
drop table if exists cms_upload;
create table cms_upload
(
  upload_id     bigint(20)   not null auto_increment comment '文件id',
  absolute_path varchar(255) not null comment '文件绝对路径',
  biz_type      varchar(50) default NULL comment '业务分类',
  old_file_name varchar(255) not null comment '原文件名称',
  latitude      float       default NULL comment '纬度',
  longitude     float       default NULL comment '经度',
  new_file_name varchar(80)  not null comment '新文件名称',
  owner_id      bigint(20)   not null comment '所属Id',
  owner_type    varchar(20)  not null comment '所属类型',
  size          integer      not null comment '文件大小',
  seq           integer     default null comment '排序',
  type          varchar(50)  not null comment '文件类型',
  upload_time   datetime(0)  not null comment '上传时间',
  del_flag      varchar(2)  default 'N' comment '删除标志（N代表存在 Y代表删除）',
  create_by     varchar(64) default '' comment '创建者',
  create_time   datetime comment '创建时间',
  update_by     varchar(64) default '' comment '更新者',
  update_time   datetime comment '更新时间',
  primary key (upload_id)
) engine = innodb
  auto_increment = 200 comment = '文件表';

-- ----------------------------
-- 视频表
-- ----------------------------
drop table if exists per_video;
create table per_video
(
  video_id    bigint(20) not null auto_increment comment '视频id',
  video_name   varchar(100)  default null comment '视频名',
  video_author varchar(20)  default null comment '作者',
  video_code   varchar(50)  default null comment '视频code',
  video_status varchar(50)  default null comment '视频状态',
  video_image  varchar(120)  default null comment '视频封面图片',
  video_url  varchar(200) default null comment '视频链接地址',
  video_intro  varchar(2000) default null comment '视频简介',
  cate_str   varchar(80)   default NULL comment '视频分类',
	cate_name   varchar(80)  default null comment '分类名称',
  del_flag    varchar(2)   default 'N'  comment '删除标志（N代表存在 Y代表删除）',
  create_by   varchar(64)  default ''   comment '创建者',
  create_time datetime comment '创建时间',
  update_by   varchar(64)  default '' comment '更新者',
  update_time datetime comment '更新时间',
  primary key (video_id)
) engine = innodb auto_increment = 1 comment = '视频表';


DROP TABLE IF EXISTS `wechat_user`;
CREATE TABLE `wechat_user`  (
  `video_id`   bigint(20) not null auto_increment comment '微信用户id',
  `openid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户的标识，对当前公众号唯一',
  `subscribe` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户的昵称',
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户的性别，值为1时是男性，值为2时是女性，值为0时是未知',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '	用户所在城市',
  `country` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户所在国家',
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户所在省份',
  `language` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户的语言，简体中文为zh_CN',
  `headimgurl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。',
  `subscribe_time` timestamp NULL DEFAULT NULL COMMENT '用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间',
  `unionid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注',
  `groupid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户所在的分组ID（兼容旧的用户分组接口）',
  `tagid_list` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户被打上的标签ID列表',
  `subscribe_scene` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_OTHERS 其他',
  `qr_scene` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二维码扫码场景（开发者自定义）',
  `qr_scene_str` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二维码扫码场景描述（开发者自定义）',
  PRIMARY KEY (`openid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of wechat_user
-- ----------------------------
INSERT INTO `wechat_user` VALUES ('oqGTlwydOH8ZVYlQGddLKD_AFWfY', '1', '随穆', '1', '南京', '中国', '江苏', 'zh_CN', 'http://thirdwx.qlogo.cn/mmopen/ajNVdqHZLLBIldXjLlWibttrM7qxYKw64rmH9z2kd7tluhVmvZtaZOibicNxVFvaD0aJJiafA15y5GpvxyHzG5ewbQ/132', '1970-01-20 02:32:16', NULL, '', '0', '[]', 'ADD_SCENE_PROFILE_CARD', '0', '');
