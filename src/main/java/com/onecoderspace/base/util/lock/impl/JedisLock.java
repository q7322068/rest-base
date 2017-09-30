package com.onecoderspace.base.util.lock.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.onecoderspace.base.util.SpringContextUtil;
import com.onecoderspace.base.util.lock.DistributedLock;

/**
 * Title:基于redis的分布式锁实现--互斥锁
 * @author yangwenkui
 * @version v1.0
 * @time 2016年5月6日 下午2:19:28
 */
public class JedisLock implements DistributedLock{
	
	private static Logger logger = LoggerFactory.getLogger(JedisLock.class);
	
	private static StringRedisTemplate redisTemplate;

    /**
     * 分布式锁的键值
     */
    String lockKey; //锁的键值
    int expireMsecs  = 10 * 1000; //锁超时，防止线程在入锁以后，无限的执行等待
    int     timeoutMsecs = 10 * 1000; //锁等待，防止线程饥饿
    boolean locked = false; //是否已经获取锁

    /**
     * 获取指定键值的锁
     * @param lockKey 锁的键值
     */
    public JedisLock(String lockKey) {
        this.lockKey = lockKey;
    }

    /**
     * 获取指定键值的锁,同时设置获取锁超时时间
     * @param lockKey 锁的键值
     * @param timeoutMsecs 获取锁超时时间
     */
    public JedisLock(String lockKey, int timeoutMsecs) {
        this.lockKey = lockKey;
        this.timeoutMsecs = timeoutMsecs;
    }

    /**
     * 获取指定键值的锁,同时设置获取锁超时时间和锁过期时间
     * @param lockKey 锁的键值
     * @param timeoutMsecs 获取锁超时时间
     * @param expireMsecs 锁失效时间
     */
    public JedisLock(String lockKey, int timeoutMsecs, int expireMsecs) {
        this.lockKey = lockKey;
        this.timeoutMsecs = timeoutMsecs;
        this.expireMsecs = expireMsecs;
    }

    public String getLockKey() {
        return lockKey;
    }

    /**
     * 
     * @return true if lock is acquired, false acquire timeouted
     * @throws InterruptedException
     *             in case of thread interruption
     */
    public synchronized boolean acquire() {
        int timeout = timeoutMsecs;
        if(redisTemplate == null){
        	redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);
		}
        try {
			while (timeout >= 0) {
			    long expires = System.currentTimeMillis() + expireMsecs + 1;
			    String expiresStr = String.valueOf(expires); //锁到期时间

			    if (redisTemplate.opsForValue().setIfAbsent(lockKey, expiresStr)) {
			        // lock acquired
			        locked = true;
			        return true;
			    }

			    String currentValueStr = redisTemplate.opsForValue().get(lockKey); //redis里的时间
			    if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
			        //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
			        // lock is expired

			        String oldValueStr = redisTemplate.opsForValue().getAndSet(lockKey, expiresStr);
			        //获取上一个锁到期时间，并设置现在的锁到期时间，
			        //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
			        if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
			            //如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
			            // lock acquired
			            locked = true;
			            return true;
			        }
			    }
			    timeout -= 100;
			    Thread.sleep(100);
			}
        } catch (Exception e) {
			logger.error("release lock due to error",e);
		} 
        return false;
    }

    /**
     * 释放锁
     */
    public synchronized void release() {
    	if(redisTemplate == null){
         	redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);
 		}
		try {
		    if (locked) {
		    	redisTemplate.delete(lockKey);;
				locked = false;
		    }
		} catch (Exception e) {
			logger.error("release lock due to error",e);
		}
    }
}
