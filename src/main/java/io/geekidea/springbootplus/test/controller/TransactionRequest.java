package io.geekidea.springbootplus.test.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "交易信息", description = "交易信息")
public class TransactionRequest {

    /**
     * 交易类型，入款
     */
    public static final Integer DEBE_IN = 1;
    /**
     * 交易类型，出款
     */
    public static final Integer DEBE_OUT = 2;
    /**
     * 交易类型，转账
     */
    public static final Integer DEBE_转账 = 3;

    /**
     * 当前账户ID
     */
    @ApiModelProperty(value = "当前账号ID")
    private Long acctId;
    /**
     * 当前账号
     */
    @ApiModelProperty(value = "当前账号")
    private String acctNo;
    /**
     * 当前账号名称
     */
    @ApiModelProperty(value = "当前账号名称")
    private String acctName;
    /**
     * 交易金额
     */
    @ApiModelProperty(value = "交易金额")
    private String amount;
    /**
     * 对方账号id
     */
    @ApiModelProperty(value = "对方账号ID")
    private Long targetAcctId;
    /**
     * 对方账号
     */
    @ApiModelProperty(value = "对方账号")
    private String targetAcctNo;
    /**
     * 对方账号名称
     */
    @ApiModelProperty(value = "对方账号名称")
    private String targetAcctName;
    /**
     * 交易类型
     */
    @ApiModelProperty(value = "交易类型")
    private Integer type;
    /**
     * 交易备注
     */
    @ApiModelProperty(value = "交易备注")
    private String remark;

    public Long getAcctId() {
        return acctId;
    }

    public void setAcctId(Long acctId) {
        this.acctId = acctId;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Long getTargetAcctId() {
        return targetAcctId;
    }

    public void setTargetAcctId(Long targetAcctId) {
        this.targetAcctId = targetAcctId;
    }

    public String getTargetAcctNo() {
        return targetAcctNo;
    }

    public void setTargetAcctNo(String targetAcctNo) {
        this.targetAcctNo = targetAcctNo;
    }

    public String getTargetAcctName() {
        return targetAcctName;
    }

    public void setTargetAcctName(String targetAcctName) {
        this.targetAcctName = targetAcctName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "acctId=" + acctId +
                ", acctNo='" + acctNo + '\'' +
                ", acctName='" + acctName + '\'' +
                ", amount='" + amount + '\'' +
                ", targetAcctId=" + targetAcctId +
                ", targetAcctNo='" + targetAcctNo + '\'' +
                ", targetAcctName='" + targetAcctName + '\'' +
                ", type=" + type +
                ", remark='" + remark + '\'' +
                '}';
    }
}
