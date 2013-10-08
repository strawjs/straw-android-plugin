package org.kt3k.straw.plugin;

import static org.kt3k.straw.plugin.HttpPlugin.*;

import org.junit.Test;
import org.junit.Rule;
import org.kt3k.straw.StrawDrink;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import com.github.tomakehurst.wiremock.junit.*;

public class HttpPluginTest {

	//@Rule
	//public WireMockRule wireMockRule = new WireMockRule(8089);

	@Test
	public void testGetName() {
		assertEquals("http", new HttpPlugin().getName());
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

	/*
	@Test
	public void testGet() {
		stubFor(get(urlEqualTo("/http/stub"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "text/plain")
						.withBody("This is response text.")));

		HttpParam httpParam = new HttpParam();
		httpParam.url = "http://localhost:8089/http/stub";

		HttpPlugin plugin = new HttpPlugin();
		StrawDrink drink = mock(StrawDrink.class);
		plugin.get(httpParam, drink);

		verify(drink).success(isA(HttpResult.class));
	}
	*/

}
