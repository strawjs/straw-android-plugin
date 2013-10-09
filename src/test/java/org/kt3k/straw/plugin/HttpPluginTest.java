package org.kt3k.straw.plugin;

import static org.kt3k.straw.plugin.HttpPlugin.*;

import org.junit.Test;
import org.junit.Rule;
import org.kt3k.straw.StrawDrink;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.junit.*;

public class HttpPluginTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);

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
		stubFor(get(urlEqualTo("/http/stub"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "text/plain")
						.withBody("This is response text.")));

		ArgumentCaptor<HttpResult> captor = ArgumentCaptor.forClass(HttpResult.class);

		HttpParam httpParam = new HttpParam();
		httpParam.url = "http://localhost:8089/http/stub";

		HttpPlugin plugin = new HttpPlugin();
		StrawDrink drink = mock(StrawDrink.class);
		plugin.get(httpParam, drink);

		verify(drink).success(captor.capture());

		assertEquals("This is response text.", captor.getValue().content);
	}

	@Test
	public void testGetMalformedUrl() {

		HttpParam httpParam = new HttpParam();
		httpParam.url = "zzzZZZ";

		HttpPlugin plugin = new HttpPlugin();
		StrawDrink drink = mock(StrawDrink.class);
		plugin.get(httpParam, drink);

		verify(drink).fail(URL_MALFORMED_ERROR, "URL format is wrong: zzzZZZ");
	}

	@Test
	public void testGetIOError() {

		HttpParam httpParam = new HttpParam();
		httpParam.url = "http://localhost:333/";

		HttpPlugin plugin = new HttpPlugin();
		StrawDrink drink = mock(StrawDrink.class);
		plugin.get(httpParam, drink);

		verify(drink).fail(CANNOT_READ_ERROR, "input stream cannot open: http://localhost:333/");
	}

}
