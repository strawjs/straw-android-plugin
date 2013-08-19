package org.kt3k.straw.plugin;

import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.*;

import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class LogPluginTest {

	@Test
	public void testGetName() {
		assertEquals("log", new LogPlugin().getName());
	}

}
