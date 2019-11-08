package io.geekidea.springbootplus.test.concurrent;

public class AccountLockerBean {

    private long acctId;
    private String acctNo;
    private String lockerId;

    public AccountLockerBean(long acctId, String acctNo, String lockerId) {
        this.acctId = acctId;
        this.acctNo = acctNo;
        this.lockerId = lockerId;
    }

    public long getAcctId() {
        return acctId;
    }

    public void setAcctId(long acctId) {
        this.acctId = acctId;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getLockerId() {
        return lockerId;
    }

    public void setLockerId(String lockerId) {
        this.lockerId = lockerId;
    }
}
