package org.kt3k.straw.plugin;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.Before;

import static org.mockito.Mockito.*;

import org.kt3k.straw.StrawDrink;
import static org.kt3k.straw.plugin.UIPlugin.*;

import static org.junit.Assert.*;

import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class UIPluginTest {

	UIPlugin plugin;
	ToastParam param;
	StrawDrink drink;

	@Before
	public void setUp() {
		plugin = new UIPlugin();
		param = new ToastParam();
		drink = mock(StrawDrink.class);
	}


	@Test
	public void testGetName() {
		assertEquals("ui", new UIPlugin().getName());
	}


	@Test
	public void testToast() {

		param.text = "abc";

		plugin.toast(param, drink);
		plugin.toastLong(param, drink);

	}

}
