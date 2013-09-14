package org.kt3k.straw.plugin;

import static org.kt3k.straw.plugin.HttpPlugin.*;

import org.junit.runner.RunWith;

import org.junit.Test;
import org.kt3k.straw.StrawDrink;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HttpPluginTest {

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

	@Test
	public void testGet() {
		HttpParam httpParam = new HttpParam();
		httpParam.url = "http://github.com/";

		HttpPlugin plugin = new HttpPlugin();
		StrawDrink drink = mock(StrawDrink.class);
		plugin.get(httpParam, drink);

		verify(drink).success(isA(HttpResult.class));
	}

}
