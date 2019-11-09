package io.geekidea.springbootplus.test;

import io.geekidea.springbootplus.SpringBootPlusApplication;
import io.geekidea.springbootplus.test.concurrent.RandomAccountLocker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={SpringBootPlusApplication.class})
public class TestAcctLocker {
    public static Logger logger = LoggerFactory.getLogger(TestAcctLocker.class);
    @Autowired
    RandomAccountLocker randomAccountLocker;

    @Test
    public void testLock() {

        long acctId = 100L;
        String acctNo = "2000";
        int timeOut = 30000;
        int maxLock = 10000;
        //定义线程池
        ThreadPoolExecutor pool = new ThreadPoolExecutor(0, 10,
                1, TimeUnit.SECONDS,
                new SynchronousQueue<>());

        //添加10个线程获取锁
        for (int i = 0; i < 5; i++) {
            pool.submit(() -> {
                try {
                    String locker = randomAccountLocker.getWriteLock(acctId,acctNo,timeOut,maxLock);
                    Thread.sleep(new Random().nextInt(10) * 1000);
                    randomAccountLocker.releaseWriteLock(acctId,acctNo,locker);
                } catch (Exception e){
                    e.printStackTrace();
                }
            });
        }

        //当线程池中的线程数为0时，退出
        while (pool.getPoolSize() != 0) {}
    }


}
