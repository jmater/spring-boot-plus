package io.geekidea.springbootplus.test.concurrent;

public interface AccountLocker {

    String getWriteLock(long acctId, String acctNo, int timeoutTime, int maxLockTime);

    boolean releaseWriteLock(long acctId, String acctNo, String lockerId);

    boolean checkLocker(int acctId, String acctNo, String lockerId);


}
