package org.kt3k.straw.plugin;

import static org.kt3k.straw.plugin.HttpPlugin.*;

import org.junit.Test;
import org.junit.Rule;
import org.junit.Before;
import org.kt3k.straw.StrawDrink;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import com.github.tomakehurst.wiremock.junit.*;

public class HttpPluginTest {

	HttpParam param;
	HttpPlugin plugin;
	StrawDrink drink;

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089, 8443);


	@Before
	public void setUp() {
		this.param = new HttpParam();
		this.plugin = new HttpPlugin();
		this.drink = mock(StrawDrink.class);
	}


	@Test
	public void testGetName() {
		HttpPlugin plugin = new org.kt3k.straw.plugin.HttpPlugin();
		assertEquals("http", plugin.getName());
	}


	@Test
	public void testHttpParam() {
		assertNotNull(new HttpParam());
	}


	@Test
	public void testHttpResult() {
		HttpResult httpResult = new HttpResult("abc");

		assertNotNull(httpResult);
		assertEquals(httpResult.content, "abc");
	}


	@Test
	public void testGet() {

		// stub http url
		stubFor(get(urlEqualTo("/http/stub"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "text/plain")
						.withBody("This is response text.")));

		// prepare argument captor
		ArgumentCaptor<HttpResult> captor = ArgumentCaptor.forClass(HttpResult.class);

		// create http param
		HttpParam httpParam = new HttpParam();
		httpParam.url = "http://localhost:8089/http/stub";

		// mock straw drink
		HttpPlugin plugin = new HttpPlugin();
		StrawDrink drink = mock(StrawDrink.class);

		// get request to stub server
		plugin.get(httpParam, drink);

		// capture response
		verify(drink).success(captor.capture());

		// assert response text
		assertEquals("This is response text.", captor.getValue().content);

	}


	@Test
	public void testGetWithHttps() throws InterruptedException {
		stubFor(get(urlEqualTo("/https/stub"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "text/plain")
						.withBody("This is response text.")));

		ArgumentCaptor<HttpResult> captor = ArgumentCaptor.forClass(HttpResult.class);

		HttpParam httpParam = new HttpParam();
		httpParam.url = "https://localhost:8443/https/stub";

		HttpPlugin plugin = new HttpPlugin();
		StrawDrink drink = mock(StrawDrink.class);
		plugin.get(httpParam, drink);

		verify(drink).success(captor.capture());
		assertEquals("This is response text.", captor.getValue().content);
	}


	@Test
	public void testGetWithMalformedUrl() {

		HttpParam httpParam = new HttpParam();
		httpParam.url = "zzzZZZ";

		HttpPlugin plugin = new HttpPlugin();
		StrawDrink drink = mock(StrawDrink.class);
		plugin.get(httpParam, drink);

		verify(drink).fail(URL_MALFORMED_ERROR, "URL format is wrong: zzzZZZ\n" +
				"java.net.MalformedURLException: no protocol: zzzZZZ");
	}


	@Test
	public void testGetWithIOErrorWhenConnect() {

		HttpParam httpParam = new HttpParam();
		httpParam.url = "http://localhost:333/";

		HttpPlugin plugin = new HttpPlugin();

		StrawDrink drink = mock(StrawDrink.class);

		plugin.get(httpParam, drink);

		verify(drink).fail(CANNOT_READ_ERROR, "input stream cannot open: http://localhost:333/\n" +
				"java.net.ConnectException: Connection refused");
	}

	@Test
	public void testGetWithUrlNull() {

		HttpParam httpParam = new HttpParam();
		httpParam.url = null;

		HttpPlugin plugin = new HttpPlugin();

		StrawDrink drink = mock(StrawDrink.class);

		plugin.get(httpParam, drink);

		verify(drink).fail(CANNOT_CONNECT_ERROR, "cannot connect to url: null\n" +
				"java.io.IOException: url is null");
	}


	@Test
	public void testGet404() {

		stubFor(get(urlEqualTo("/404")).willReturn(aResponse().withStatus(404)));

		this.param.url = "http://localhost:8089/404";

		this.plugin.get(this.param, this.drink);

		verify(this.drink).fail(CANNOT_READ_ERROR, "input stream cannot open: http://localhost:8089/404\n" +
				"java.io.FileNotFoundException: http://localhost:8089/404");

	}

	@Test
	public void testGetTimeout() {

		stubFor(get(urlEqualTo("/http/stub")).willReturn(aResponse().withStatus(200).withFixedDelay(2000)));

		this.param.url = "http://localhost:8089/http/stub";
		this.param.timeout = 1000;

		this.plugin.get(this.param, this.drink);

		verify(this.drink).fail(TIMEOUT, "connection timed out: http://localhost:8089/http/stub\n" +
				"java.net.SocketTimeoutException: Read timed out");

	}

	@Test
	public void testGetNoTimeout() {

		stubFor(get(urlEqualTo("/http/stub")).willReturn(aResponse().withStatus(200).withFixedDelay(2000)));

		this.param.url = "http://localhost:8089/http/stub";

		this.plugin.get(this.param, this.drink);

		verify(this.drink).success(isA(HttpResult.class));

	}

	@Test
	public void testGetInTimeout() {

		stubFor(get(urlEqualTo("/http/stub")).willReturn(aResponse().withStatus(200).withFixedDelay(2000)));

		this.param.url = "http://localhost:8089/http/stub";
		this.param.timeout = 3000;

		this.plugin.get(this.param, this.drink);

		verify(this.drink).success(isA(HttpResult.class));

	}

}
