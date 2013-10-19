package org.kt3k.straw.plugin;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import static org.kt3k.straw.plugin.LogPlugin.*;
import org.kt3k.straw.StrawDrink;

import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class LogPluginTest {

	LogPlugin plugin;
	LogParam param;
	StrawDrink drink;


	@Before
	public void setUp() {
		plugin = new LogPlugin();
		param = new LogParam();
		drink = mock(StrawDrink.class);
	}


	@Test
	public void testGetName() {
		assertEquals("log", plugin.getName());
	}


	@Test
	public void testLogPram() {

		assertEquals(DEFAULT_SERVICE_NAME, param.getLabel());

		param.label = "abc";

		assertEquals("abc", param.getLabel());

	}

	@Test
	public void testLogMethods() {

		param.message = "abc";

		plugin.verbose(param, drink);
		plugin.debug(param, drink);
		plugin.info(param, drink);
		plugin.warn(param, drink);
		plugin.error(param, drink);

	}

}
