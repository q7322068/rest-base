package com.onecoderspace.base.util.ognl;

import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onecoderspace.base.util.JacksonHelper;

import ognl.Ognl;
import ognl.OgnlException;

@SuppressWarnings("unchecked")
public class OgnlWrapper {

	private static Logger logger = LoggerFactory.getLogger(OgnlWrapper.class);

	private static ObjectMapper om = new ObjectMapper();
	private Map<String, Object> payload;

	public OgnlWrapper(Map<String, Object> playload) {
		Validate.notEmpty(playload, "can not construct with none playload!");
		this.payload = playload;
	}

	
	public OgnlWrapper(Object playload) {
		this.payload = om.convertValue(playload, Map.class);
	}

	public <T> T get(String expression) {
		try {
			return (T) Ognl.getValue(expression, this.payload);
		} catch (OgnlException e) {
			logger.trace(String.format("get value with expression:[%s] due to error, return null instead of",
				expression), e);
			return null;
		}
	}

	public Long getLong(String expression) {
		try {
			Object obj = Ognl.getValue(expression, this.payload);
			if (null == obj)
				return null;
			try {
				return Long.parseLong(obj.toString());
			} catch (NumberFormatException nfe) {
				logger.warn(String.format("get value with expression:[%s] due to error, return null. value[%s] cannot be cast to java.lang.Long",
					expression,
					obj.toString()));
				return null;
			}
		} catch (OgnlException e) {
			logger.trace(String.format("get value with expression:[%s] due to error, return null instead of",
				expression), e);
			return null;
		}
	}

	public Integer getInt(String expression) {
		try {
			Object obj = Ognl.getValue(expression, this.payload);
			if (null == obj)
				return null;
			try {
				return Integer.parseInt(obj.toString());
			} catch (NumberFormatException nfe) {
				logger.warn(String.format("get value with expression:[%s] due to error, return null. value[%s] cannot be cast to java.lang.Integer",
					expression,
					obj.toString()));
				return null;
			}
		} catch (OgnlException e) {
			logger.trace(String.format("get value with expression:[%s] due to error, return null instead of",
				expression), e);
			return null;
		}
	}

	@Override
	public String toString() {
		return String.format("OgnlWrapper[%s]", this.payload.toString());
	}
	
	public static void main(String[] args) {
		String json = "{\"user\":{\"name\":\"123\"}}";
		Map<?, ?> map = JacksonHelper.fromJson(json, Map.class);
		OgnlWrapper ognlWrapper = new OgnlWrapper(map);
		System.err.println(ognlWrapper.get("user.name"));
	}

}
