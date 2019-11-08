package io.geekidea.springbootplus.test.concurrent;


import io.geekidea.springbootplus.util.RedisCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * 账户随机锁，用于实现对账户的锁定，对于读锁，不进行限制,lock信息存储与redis，多个请求其中随机一个得到锁
 */
@Service
public class RandomAccountLocker implements  AccountLocker{

    public static Logger logger = LoggerFactory.getLogger(RandomAccountLocker.class);

    public static final String WRIGHT_LOCK_KEY = "write_lock_key_{0}_{1}";
    public static final String WRIGHT_LOCK_VALUE = "write_locked";
    //默认的锁超时时间，单位是豪秒
    //最小锁定时间，单位豪秒
    private static final int MIN_LOCK_TIME_MILLSECONDS = 100;
    //最大锁定时间，单位豪秒
    private static final int MAX_LOCK_TIME_MILLSECONDS = 36000;
    /**
     * 重复获取锁的间隔时间
     */
    private static final int LOCKER_GET_DELAY_MILLSECONDS = 1000;

    /**
     * 重复释放锁的间隔时间
     */
    private static final int LOCKER_RELEASE_DELAY_MILLSECONDS = 300;

    @Autowired
    private StringRedisTemplate redisTemplate;
    public RandomAccountLocker(){

    }

    private final String LOCKVALUE = "lockvalue";

    private boolean locked = false;

    public synchronized boolean lock(long acctId,String acctNo,int timeoutTime){
        String lockKey = MessageFormat.format(WRIGHT_LOCK_KEY,acctId,acctNo);
        /*该方法会在没有key时，设置key;存在key时返回false；因此可以通过该方法及设置key的有效期，判断是否有其它线程持有锁*/
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey,LOCKVALUE);
        if(success != null && success){
            redisTemplate.expire(lockKey,timeoutTime,TimeUnit.MILLISECONDS);
            locked = true;
        }else{
            locked = false;
        }
        return locked;
    }

    /**
     * 尝试获取指定账户的写锁，对账户进行写操作之前 必须获取写锁，锁最大时间1800秒，最小时间1秒，如果该账户未被加锁，则该
     * 操作会为账户加上写锁，，并设定锁的最大锁定时间，如果该账户已经加写锁，则会在最大尝试时间内每隔1秒钟尝试重新获取锁。
     * 如果多个请求同时要求加锁，则其中随机一个请求会得到写锁；
     * 最大锁定时间：3000豪秒，最小锁定时间：100豪秒；
     * 锁数据由redis来存储；
     * @param acctId
     * @param timeoutTime 获取锁的最大尝试时间，单位是豪秒，超过该时间则不会尝试继续获取
     * @param maxLockTime 锁的最大锁定时间 单位是豪秒，最小锁定时间1秒钟
     * @return lockerId 成功返回锁的ID，null，无法获取写锁则返回null，需要后续重试
     */
    @Override
    public String getWriteLock(long acctId,String acctNo,int timeoutTime,int maxLockTime){
        String lockerId = StrUtils.getUUID();
        String key = MessageFormat.format(WRIGHT_LOCK_KEY,acctId,acctNo);
        Boolean res = false;
        long start = System.currentTimeMillis();
        long end;
        long totalTime = 0;
        boolean check = true;
        String success = null;
        long lockTime = MIN_LOCK_TIME_MILLSECONDS;
        try {
            while (check) {
                if(maxLockTime >= MIN_LOCK_TIME_MILLSECONDS && maxLockTime <= MAX_LOCK_TIME_MILLSECONDS){
                    lockTime = maxLockTime;
                }else if(maxLockTime > MAX_LOCK_TIME_MILLSECONDS){
                    lockTime = MAX_LOCK_TIME_MILLSECONDS;
                }else{
                    lockTime = MIN_LOCK_TIME_MILLSECONDS;
                }
                res = redisTemplate.opsForValue().setIfAbsent(key, lockerId,lockTime,TimeUnit.MILLISECONDS);
                try {
                    if (res == true) {
                        success = lockerId;
                        check = false;
                    }else{
                        logger.debug("Sleep for {} ms ,and try get Locker of acctId:{} again.",LOCKER_GET_DELAY_MILLSECONDS,acctId);
                        Thread.sleep(LOCKER_GET_DELAY_MILLSECONDS);
                    }
                    end = System.currentTimeMillis();
                    totalTime = (end - start);
                    if (totalTime > timeoutTime) {
                        check = false;
                    }
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }

            if (res == true) {
                success = lockerId;
                logger.info("Success!! Got write lock:{},timeoutInSeconds is:{},lockedId is:{}", key, timeoutTime, success);
            }else {
                success = null;
                logger.error("Failed!! Can not get write lock:{},timeoutInSeconds is:{},lockedId is:{}", key, timeoutTime, success);
            }

        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }

        return  success;

    }

    /**
     * 根据锁ID释放指定账户的写锁，对账户的写操作完成之后，必须释放写锁；
     * 如果该锁释放成功，则返回成功；
     * 如果账户锁不存在，则返回成功；
     * 如果锁存在，但是锁ID不一致，则返回失败；
     * @param acctId
     * @param acctNo
     * @param lockerId
     * @return
     */
    @Override
    public boolean releaseWriteLock(long acctId,String acctNo,String lockerId){
        boolean lockReleased = false;
        int releaseCount = 0;
        while (!lockReleased && releaseCount < AcctConstant.MAX_RELEASE_LOCK_RETRY) {
            lockReleased = doLockRelease(acctId, acctNo, lockerId);
            releaseCount += 1;
            if (!lockReleased && releaseCount >= 1) {
                try {
                    Thread.sleep(LOCKER_RELEASE_DELAY_MILLSECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return  lockReleased;
    }

    private boolean doLockRelease(long acctId,String acctNo,String lockerId){
        String key = MessageFormat.format(WRIGHT_LOCK_KEY,acctId,acctNo);
        boolean success = false;
        String locker;
        try {
            locker = redisTemplate.opsForValue().get(key);
            if(locker == null){ //锁已经被释放
                success = true;
            }else if(locker != null && lockerId != null && locker.equals(lockerId)){
                redisTemplate.delete(key);
                success = true;
            }else{
                logger.error("release locker:{} failed,locker id:{} not match with key!",key,lockerId);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        if(success == true){
            logger.info("release locker:{} success!",key);
        }
        return success;
    }

    /**
     * 检查锁ID与账户是否匹配
     * 如果锁存在并且id匹配，则返回成功；
     * 如果账户的锁不存在，返回成功，
     * 如果锁存在并且锁ID不一致，则返回失败；
     * @param acctId
     * @param acctNo
     * @param lockerId
     * @return
     */
    @Override
    public boolean checkLocker(int acctId,String acctNo,String lockerId){
        String key = MessageFormat.format(WRIGHT_LOCK_KEY,acctId,acctNo);
        boolean success = false;
        String locker;
        try {
            locker = redisTemplate.opsForValue().get(key);
            if(locker != null && lockerId != null &&  locker.equals(lockerId)){
                success = true;
            } else if (locker == null){
                success = true;
            } else{
                logger.error("check locker:{} failed,locker id:{} not match with key!",key,lockerId);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }finally {

        }
        if(success == true){
            logger.info("check locker:{} success, lockerId:{}",key,lockerId);
        }
        return success;
    }

}
