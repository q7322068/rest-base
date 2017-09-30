package com.onecoderspace.base.util.http;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {

	public static final Logger LOGGER = LoggerFactory.getLogger(DefaultConnectionKeepAliveStrategy.class);

	public static final DefaultConnectionKeepAliveStrategy DEFAULT = new DefaultConnectionKeepAliveStrategy();

	public static final String KEY_NAME = "timeout";

	public static final long DEFAULT_TIMEOUT = 120 * 1000;

	@Override
	public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
		if (response == null) {
			return DEFAULT_TIMEOUT;
		}

		final HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
		while (it.hasNext()) {
			final HeaderElement he = it.nextElement();
			final String param = he.getName();
			final String value = he.getValue();
			if (value != null && param.equalsIgnoreCase(KEY_NAME)) {
				try {
					return Long.parseLong(value) * 1000;
				} catch (final NumberFormatException ignore) {
					LOGGER.warn("invalid timeout value:[{}]", value);
				}
			}
		}

		return DEFAULT_TIMEOUT;
	}

}
