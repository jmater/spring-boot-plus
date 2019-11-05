-- ----------------------------
-- Table structure for foo_bar
-- ----------------------------
CREATE TABLE IF NOT EXISTS `account_info`
(
    `id`            bigint(20)  NOT NULL COMMENT '主键',
    `acct_no`          varchar(20) NOT NULL COMMENT '账号',
    `acct_name`           varchar(20)  NOT NULL COMMENT '账户名称',
    `balance`           decimal(12,2) NOT NULL COMMENT '余额',
    `remark`        varchar(256)         DEFAULT NULL COMMENT '备注',
    `create_time`   timestamp   NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   timestamp   NULL     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO account_info (id, acct_no, acct_name, balance, remark, create_time, update_time)
    VALUES (1, '10001', '张三', 0.0, 'remark...', '2019-11-01 14:05:14', null);
INSERT INTO account_info (id, acct_no, acct_name, balance, remark, create_time, update_time)
    VALUES (2, '200001', '李四', 1000.0, null, '2019-11-05 14:05:14', null);