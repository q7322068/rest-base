package com.onecoderspace.base.util.redis;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.Validate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.onecoderspace.base.util.SpringContextUtil;

/**
 * redis的list为双向链表，使用左出右进的策略构建一个queue
 *
 */
public class RedisBlockingQueue implements BlockingQueue<String> {
	
	public static RedisBlockingQueue get(String queueName){
		return new RedisBlockingQueue(queueName);
	}
	
	public RedisBlockingQueue(String queueName) {
		this.queueName = queueName;
		synchronized (RedisBlockingQueue.class) {
			if(redisTemplate == null){
				redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);
			}
		}
	}

	private String queueName;

	private static StringRedisTemplate redisTemplate;

	@Override
	public String remove() {
		String r = this.poll();
		Validate.notNull(r);
		return r;
	}

	@Override
	public String poll() {
		return redisTemplate.opsForList().leftPop(queueName);
	}

	@Override
	public String element() {
		String r = this.peek();
		Validate.notNull(r);
		return r;
	}

	@Override
	public String peek() {
		return redisTemplate.opsForList().index(queueName, 0);
	}

	@Override
	public int size() {
		long size = redisTemplate.opsForList().size(queueName);
		return (int)size;
	}

	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	@Override
	public Iterator<String> iterator() {
		throw new RuntimeException("Redis queue not support iterator method");
	}

	@Override
	public Object[] toArray() {
		return redisTemplate.opsForList().range(queueName, 0, -1).toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new RuntimeException("Redis queue not support toArray method");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new RuntimeException("Redis queue not support containsAll method");
	}

	@Override
	public boolean addAll(Collection<? extends String> c) {
		throw new RuntimeException("Redis queue not support addAll method");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new RuntimeException("Redis queue not support removeAll method");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new RuntimeException("Redis queue not support retainAll method");
	}

	@Override
	public void clear() {
		redisTemplate.expire(queueName, 1L, TimeUnit.SECONDS);
	}

	@Override
	public boolean add(final String e) {
		long length = redisTemplate.opsForList().rightPush(queueName, e);
		return length > 0;
	}

	@Override
	public boolean offer(String e) {
		return this.add(e);
	}

	@Override
	public void put(String e) throws InterruptedException {
		this.add(e);
	}

	@Override
	public boolean offer(String e, long timeout, TimeUnit unit) throws InterruptedException {
		return this.add(e);
	}

	@Override
	public String take() throws InterruptedException {
		return redisTemplate.opsForList().leftPop(queueName, Integer.MAX_VALUE, TimeUnit.SECONDS);
	}

	@Override
	public String poll(final long timeout, final TimeUnit unit) throws InterruptedException {
		return redisTemplate.opsForList().leftPop(queueName, timeout, unit);
	}

	@Override
	public int remainingCapacity() {
		throw new RuntimeException("Redis queue not support remainingCapacity method");
	}

	@Override
	public boolean remove(final Object value) {
		long count = redisTemplate.opsForList().remove(queueName, 0, value);
		return count > 0;
	}

	@Override
	public boolean contains(Object o) {
		throw new RuntimeException("Redis queue not support contains method");
	}

	@Override
	public int drainTo(Collection<? super String> c) {
		throw new RuntimeException("Redis queue not support drainTo method");
	}

	@Override
	public int drainTo(Collection<? super String> c, int maxElements) {
		throw new RuntimeException("Redis queue not support drainTo method");
	}

}
