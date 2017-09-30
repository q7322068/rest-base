package com.onecoderspace.base.util.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestByParametersCreator implements RequestCreator {

	private static final String parameterValuesDelimiter = ",";

	public RequestByParametersCreator(String url, Map<String, ?> parameters) {
		this.url = url;
		this.parameters = parameters;
	}

	final String url;
	final Map<String, ?> parameters;

	@Override
	public HttpRequestBase create() throws Exception {
		HttpPost post = new HttpPost(url);
		post.setConfig(CONFIG);
		if (parameters != null) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			for (String name : parameters.keySet()) {
				Object value = parameters.get(name);

				if (value == null) {
					value = StringUtils.EMPTY;
				}

				if (value instanceof String[]) {
					String[] _values = (String[]) value;
					value = _values.length > 1
							? StringUtils.join(value, parameterValuesDelimiter)
							: _values[0];
				}

				pairs.add(new BasicNameValuePair(name, (String) value));
			}
			post.setEntity(new UrlEncodedFormEntity(pairs, Consts.UTF_8));
		}

		return post;
	}

}
