/*
 Navicat Premium Data Transfer

 Source Server         : 101.132.136.225
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : 101.132.136.225:3306
 Source Schema         : jwwl_attachment

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 21/11/2019 15:09:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attachment_uploading_record
-- ----------------------------
DROP TABLE IF EXISTS `attachment_uploading_record`;
CREATE TABLE `attachment_uploading_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `attachment_original_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件原始名称',
  `attachment_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件上传后名称',
  `file_directory` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件所在目录',
  `absolute_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件绝对地址',
  `call_on_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件访问地址',
  `attachment_postfix` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件后缀',
  `attachment_type` tinyint(4) NULL DEFAULT NULL COMMENT '附件类型1：图片 2：文档 3：视频 4:zip 5:其他',
  `attachment_size` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件大小(kb)',
  `system_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编码 例如：1001：设备系统',
  `business_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务编码 例如：1001001：访客管理',
  `business_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型 例如： 1001001001：访客头像',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '上传时间',
  `uploader_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传者名称',
  `uploader_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传者ID',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `attribute_one` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段1',
  `attribute_two` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段2',
  `attribute_three` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段3',
  `attribute_four` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段4',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上传附件管理' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
