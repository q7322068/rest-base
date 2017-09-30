package com.onecoderspace.base.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JacksonHelper {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	private static Logger logger = LoggerFactory.getLogger(JacksonHelper.class);
	
	
	/**
	 * 将对象转化为json
	 * @author yangwenkui
	 * @time 2017年3月16日 下午2:55:10
	 * @param obj 待转化的对象
	 * @return 当转化发生异常时返回null
	 */
	public static String toJson(Object obj){
		if(obj == null){
			return null;
		}
		try {
			return mapper.writeValueAsString(obj);
		} catch (IOException e) {
			logger.error(String.format("obj=[%s]", obj.toString()), e);
		}
		return null;
	}
	
	/**
	 * 将json转化为对象
	 * @author yangwenkui
	 * @time 2017年3月16日 下午2:56:26
	 * @param json json对象
	 * @param clazz 待转化的对象类型
	 * @return 当转化发生异常时返回null
	 */
	public static <T> T fromJson(String json,Class<T> clazz){
		if(json == null){
			return null;
		}
		try {
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			logger.error(String.format("json=[%s]", json), e);
		}
		return null;
	}
	
	/**
	 * 将json对象转化为集合类型
	 * @author yangwenkui
	 * @time 2017年3月16日 下午2:57:15
	 * @param json json对象
	 * @param collectionClazz 具体的集合类的class，如：ArrayList.class
	 * @param clazz 集合内存放的对象的class
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> Collection<T> fromJson(String json,Class<? extends Collection> collectionClazz,Class<T> clazz){
		if(json == null){
			return null;
		}
		try {
			Collection<T> collection =  mapper.readValue(json, getCollectionType(collectionClazz,clazz));
			return collection;
		} catch (IOException e) {
			logger.error(String.format("json=[%s]", json), e);
		}
		return null;
	}
	
	private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {   
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);   
	}
	
	public static void main(String[] args) {
		String s = "{\"ss\":\"12\",\"dd\":\"33\",\"tt\":23}";
		@SuppressWarnings("unchecked")
		Map<String, Object> map = fromJson(s, Map.class);
		for (Entry<String, Object> entry : map.entrySet()) {
			System.out.println(entry);
		}
	}
}
