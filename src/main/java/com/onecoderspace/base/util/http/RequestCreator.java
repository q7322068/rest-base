package com.onecoderspace.base.util.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;

public interface RequestCreator {

	static RequestConfig CONFIG = RequestConfig.custom()
			.setConnectionRequestTimeout(1000)
			.setConnectTimeout(1000 * 5)
			.setSocketTimeout(1000 * 30)
			.build();

	HttpRequestBase create() throws Exception;

}
