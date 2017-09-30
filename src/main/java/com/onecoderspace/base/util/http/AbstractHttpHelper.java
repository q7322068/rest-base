package com.onecoderspace.base.util.http;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public abstract class AbstractHttpHelper {

	private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);

	public static class GZIPRequestInterceptor implements HttpRequestInterceptor {

		public static final GZIPRequestInterceptor DEFAULT = new GZIPRequestInterceptor();

		public static final String GZIP = "gzip";

		public static final String ACCEPT_ENCODING = "Accept-Encoding";

		@Override
		public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
			if (!request.containsHeader(ACCEPT_ENCODING)) {
				request.addHeader(ACCEPT_ENCODING, GZIP);
			}
		}

	}

	public static class GZIPResponseInterceptor implements HttpResponseInterceptor {

		public static final GZIPResponseInterceptor DEFAULT = new GZIPResponseInterceptor();

		@Override
		public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return;
			}

			Header header = entity.getContentEncoding();
			if (header == null) {
				return;
			}

			HeaderElement[] codecs = header.getElements();

			for (int i = 0; i < codecs.length; i++) {
				if (codecs[i].getName().equalsIgnoreCase(GZIPRequestInterceptor.GZIP)) {
					logger.debug("the content encoding is:{}, use gzip decompressing", Arrays.toString(codecs));
					response.setEntity(new GzipDecompressingEntity(entity));
					return;
				}
			}
		}

	}

	public static class ResponseToString implements ResponseHandler<String> {

		public static final ResponseToString DEFAULT = new ResponseToString(CharEncoding.UTF_8);

		public ResponseToString(String charset) {
			this.charset = charset;
		}

		private String charset;

		@Override
		public String handleResponse(HttpResponse response) throws IOException {
			return IOUtils.toString(response.getEntity().getContent(), charset);
		}
	}

	public <T> T doPost(final String url, final byte[] body, ResponseHandler<T> handler) {
		return executeRequest(new RequestByBodyCreator(url, body), handler);
	}

	public <T> T doPost(final String url, final Map<String, ?> parameters, ResponseHandler<T> handler) {
		return executeRequest(new RequestByParametersCreator(url, parameters), handler);
	}

	public <T> T doGet(final String url, ResponseHandler<T> handler) {
		return executeRequest(new RequestByURLCreator(url), handler);
	}

	protected abstract HttpClient getHttpClient();

	public <T> T executeRequest(RequestCreator creator, final ResponseHandler<T> handler) {

		HttpRequestBase request = null;
		try {
			request = creator.create();
			T response = (T) getHttpClient().execute(request, new DelegateResponseHandler<T>(handler));
			return response;
		} catch (UnexpectedHttpStatusException e) {
			throw e;
		} catch (Exception e) {
			logger.error("execute request:[{}] due to error:[{}]", request.getURI(), ExceptionUtils.getStackTrace(e));
			throw new HttpRuntimeException(e);
		} finally {
			request.releaseConnection();
		}
	}

	protected void validateResponseStatus(HttpResponse response) throws UnexpectedHttpStatusException, IOException {
		int status = response.getStatusLine().getStatusCode();
		if (status == 200) {
			return;
		}

		throw new UnexpectedHttpStatusException(status,
				String.format("the request failed, the status is:[%s], the response is:[%s]",
						response.getStatusLine(),
						IOUtils.toString(response.getEntity().getContent()))
		);
	}

	protected class DelegateResponseHandler<T> implements ResponseHandler<T> {

		public DelegateResponseHandler(ResponseHandler<T> delegate) {
			this.delegate = delegate;
		}

		private ResponseHandler<T> delegate;

		@Override
		public T handleResponse(HttpResponse response) throws IOException {
			validateResponseStatus(response);
			return delegate.handleResponse(response);
		}
	}

}