/*
 Navicat Premium Data Transfer

 Source Server         : 101.132.136.225
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : 101.132.136.225:3306
 Source Schema         : auth-demo

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 21/11/2019 15:11:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for AUTH_USER_INFO
-- ----------------------------
DROP TABLE IF EXISTS `AUTH_USER_INFO`;
CREATE TABLE `AUTH_USER_INFO`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USER_CODE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户编码',
  `USER_ACCOUNT` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账户',
  `USER_PASSWORD` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码',
  `USER_NAME` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名称',
  `USER_EMAIL` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `BINDING_PHONE` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '绑定的手机号',
  `USER_SEX` tinyint(4) NULL DEFAULT NULL COMMENT '性别 1:男   2：女  3：其他',
  `CREATE_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `UPDATE_TIME` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `UPDATE_USER_ID` bigint(20) NULL DEFAULT NULL COMMENT '最后更新人ID',
  `LAST_PASSWORD_RESET_DATE` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改密码时间',
  `STATUS` tinyint(4) NOT NULL DEFAULT 0 COMMENT '用户状态：0：正常  1：锁定',
  `LAST_LOGIN_DATE` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后登录时间',
  `USER_TYPE` tinyint(4) NOT NULL DEFAULT 0 COMMENT '用户类型：0 普通  -1 超级管理员   ',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `USER_ACCOUNT_INDEX`(`USER_ACCOUNT`) USING BTREE COMMENT '用户账户唯一索引',
  UNIQUE INDEX `USER_CODE_INDEX`(`USER_CODE`) USING BTREE COMMENT '用户编号唯一索引',
  INDEX `USER_LOGIN_INDEX`(`USER_ACCOUNT`, `USER_PASSWORD`) USING BTREE COMMENT '登录索引'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户基础信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
