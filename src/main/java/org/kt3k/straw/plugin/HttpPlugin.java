package org.kt3k.straw.plugin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.kt3k.straw.StrawDrink;
import org.kt3k.straw.StrawPlugin;
import org.kt3k.straw.PluginAction;

public class HttpPlugin extends StrawPlugin {

	static final String URL_MALFORMED_ERROR = "0";
	static final String CANNOT_CONNECT_ERROR = "1";
	static final String CANNOT_READ_ERROR = "2";
	static final String SSL_UNAVAILABLE = "3";
	static final String TIMEOUT = "4";

	/**
	 * TrustManager checks nothing and trust everything
	 */
	static final X509TrustManager TRUST_ALL = new X509TrustManager() {

		public void checkClientTrusted(X509Certificate[] chain,String authType) {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	};

	/**
	 * HostnameVerifier verify every host name
	 */
	static final HostnameVerifier NO_VERIFIER = new HostnameVerifier() {

		@Override
		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}

	};

	@Override
	public String getName() {
		return "http";
	}

	/**
	 * Parameter class for `get` and `post` actions
	 */
	public static class HttpParam {

		public String url;
		public String data;
		public String charset;
		public Integer timeout;

		public Boolean isHttpsRequest() {
			return this.url != null && this.url.startsWith("https");
		}

		public HttpConnection createConnection() throws MalformedURLException, IOException {
			if (this.url == null) {
				throw new IOException("url is null");
			}

			URL url = new URL(this.url);

			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			return new HttpConnection(conn, this);

		}

		public Integer getTimeout() {
			return this.timeout;
		}
	}

	/**
	 * Wrapper class for Http(s)URLConnection class
	 */
	public static class HttpConnection {

		private HttpURLConnection conn;

		private HttpParam param;

		public HttpConnection(HttpURLConnection conn, HttpParam param) {
			this.param = param;
			this.conn = conn;

			this.setTimeout(param.getTimeout());
		}

		public Boolean isHttpsConnection() {
			return this.param.isHttpsRequest();
		}

		public String getContents() throws IOException {
			return this.inputStreamToString(this.conn.getInputStream());
		}

		private String inputStreamToString(java.io.InputStream stream) {
			Scanner scanner = new Scanner(stream).useDelimiter("\\A");

			return scanner.hasNext() ? scanner.next() : "";
		}

		private void setTimeout(Integer timeout) {

			if (timeout == null) {
				return;
			}

			this.conn.setConnectTimeout(timeout);
			this.conn.setReadTimeout(timeout);

		}

		public void setSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
			setSSLContext(this.conn);
		}

		private static void setSSLContext(HttpURLConnection conn) throws NoSuchAlgorithmException, KeyManagementException {
			setSSLContext((HttpsURLConnection)conn);
		}

		private static void setSSLContext(HttpsURLConnection conn) throws NoSuchAlgorithmException, KeyManagementException {

			SSLContext ctx = SSLContext.getInstance("TLS");

			ctx.init(null, new X509TrustManager[]{TRUST_ALL}, null);

			conn.setSSLSocketFactory(ctx.getSocketFactory());

			conn.setHostnameVerifier(NO_VERIFIER);

		}

	}


	public static class HttpResult {
		public String content;

		public HttpResult(String content) {
			this.content = content;
		}
	}


	/**
	 * http get param.url
	 * @param param
	 * @subparam param.url request url
	 * @subparam param.timeout request timeout
	 * @subparam param.charset decoding charset for response body
	 * @param drink
	 */
	@PluginAction
	public void get(HttpParam param, StrawDrink drink) {
		HttpConnection conn;

		try {
			conn = param.createConnection();

		} catch (MalformedURLException e) {
			drink.fail(URL_MALFORMED_ERROR, "URL format is wrong: " + param.url + "\n" + e.toString());

			return;

		} catch (IOException e) {
			drink.fail(CANNOT_CONNECT_ERROR, "cannot connect to url: " + param.url + "\n" + e.toString());

			return;
		}

		if (conn.isHttpsConnection()) {

			try {
				conn.setSSLContext();

			} catch (NoSuchAlgorithmException e) {
				drink.fail(SSL_UNAVAILABLE, "SSL connection is unavailable: " + param.url + "\n" + e.toString());

				return;

			} catch (KeyManagementException e) {
				drink.fail(SSL_UNAVAILABLE, "SSL connection is unavailable: " + param.url + "\n" + e.toString());

				return;
			}

		}

		try {
			drink.success(new HttpResult(conn.getContents()));

		} catch (SocketTimeoutException e) {
			drink.fail(TIMEOUT, "connection timed out: " + param.url + "\n" + e.toString());

			return;
		} catch (IOException e) {
			drink.fail(CANNOT_READ_ERROR, "input stream cannot open: " + param.url + "\n" + e.toString());

			return;
		}

	}

	/*
	@PluginAction
	public void post(HttpParam param, StrawDrink drink) {
	}
	 */

}
