package io.geekidea.springbootplus.test.concurrent;

public class AccountLockerFactory {

    private static RandomAccountLocker accountLocker;

    public static AccountLocker getAccountLocker(){
        if(accountLocker == null)
            accountLocker = new RandomAccountLocker();
        return  accountLocker;
    }
}
