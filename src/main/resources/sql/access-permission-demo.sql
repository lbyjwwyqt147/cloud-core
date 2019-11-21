/*
 Navicat Premium Data Transfer

 Source Server         : 101.132.136.225
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : 101.132.136.225:3306
 Source Schema         : access-permission-demo

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 21/11/2019 15:12:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for AUTH_MODULE
-- ----------------------------
DROP TABLE IF EXISTS `AUTH_MODULE`;
CREATE TABLE `AUTH_MODULE`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '模块ID',
  `MODULE_CODE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模块编号',
  `MODULE_NAME` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `MODULE_TYPE` tinyint(4) NOT NULL COMMENT '模块类型：1:目录  2：菜单界面   3：功能按钮 ',
  `MENU_ICON` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `MENU_URL` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源路径',
  `MODULE_PID` bigint(20) NULL DEFAULT NULL COMMENT '父ID',
  `SEQUENCE_NUMBER` tinyint(4) NULL DEFAULT 0 COMMENT '排序号',
  `AUTHORIZED_SIGNS` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权标识(多个用逗号分隔，如：user:list,user:create)',
  `STATUS` tinyint(4) NULL DEFAULT NULL COMMENT '状态：状态：0：正常  1：禁用',
  `CREATE_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `UPDATE_TIME` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `UPDATE_USER_ID` bigint(20) NULL DEFAULT NULL COMMENT '最后更新人ID',
  `DESCRIPTION` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `MODULE_PID_INDEX`(`MODULE_PID`, `STATUS`) USING BTREE COMMENT '根据父ID获取下级'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单模块表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for AUTH_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `AUTH_ROLE`;
CREATE TABLE `AUTH_ROLE`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `ROLE_CODE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
  `ROLE_NAME` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `ROLE_DESCRIPTION` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `STATUS` tinyint(4) NULL DEFAULT NULL COMMENT '状态 状态  0：正常  1：禁用',
  `AUTHORIZED_SIGNS` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权标识',
  `CREATE_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `UPDATE_TIME` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `UPDATE_USER_ID` bigint(20) NULL DEFAULT NULL COMMENT '最后更新人ID',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for AUTH_ROLE_MODULE
-- ----------------------------
DROP TABLE IF EXISTS `AUTH_ROLE_MODULE`;
CREATE TABLE `AUTH_ROLE_MODULE`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID',
  `MODULE_ID` bigint(20) NOT NULL COMMENT '菜单模块ID',
  `CREATE_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `ROLE_MODULE_ROLE_ID_INDEX`(`ROLE_ID`) USING BTREE COMMENT '根据角色ID查询'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色资源模块表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for AUTH_USER_INFO
-- ----------------------------
DROP TABLE IF EXISTS `AUTH_USER_INFO`;
CREATE TABLE `AUTH_USER_INFO`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USER_CODE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户编码',
  `USER_ACCOUNT` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账户',
  `USER_PASSWORD` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码',
  `USER_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名称',
  `USER_EMAIL` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `BINDING_PHONE` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '绑定的手机号',
  `USER_SEX` tinyint(4) NULL DEFAULT NULL COMMENT '性别 1:男   2：女  3：其他',
  `CREATE_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `UPDATE_TIME` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `UPDATE_USER_ID` bigint(20) NULL DEFAULT NULL COMMENT '最后更新人ID',
  `LAST_PASSWORD_RESET_DATE` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改密码时间',
  `STATUS` tinyint(4) NULL DEFAULT 0 COMMENT '用户状态：0：正常  1：锁定',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `USER_ACCOUNT_INDEX`(`USER_ACCOUNT`) USING BTREE COMMENT '用户账户唯一索引',
  UNIQUE INDEX `USER_CODE_INDEX`(`USER_CODE`) USING BTREE COMMENT '用户编号唯一索引',
  INDEX `USER_LOGIN_INDEX`(`USER_ACCOUNT`, `USER_PASSWORD`) USING BTREE COMMENT '登录索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for AUTH_USER_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `AUTH_USER_ROLE`;
CREATE TABLE `AUTH_USER_ROLE`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `USER_ID` bigint(20) NOT NULL COMMENT '用户ID',
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID',
  `CREATE_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `USER_ROLE_USER_ID_INDEX`(`USER_ID`) USING BTREE COMMENT '根据用户ID 查询'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
