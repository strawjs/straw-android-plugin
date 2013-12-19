package org.kt3k.straw.plugin;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kt3k.straw.StrawDrink;
import org.kt3k.straw.StrawPlugin.SingleStringParam;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

@RunWith(RobolectricTestRunner.class)
public class BrowserPluginTest {

	@Test
	public void testGetName() {
		assertEquals("browser", new BrowserPlugin().getName());
	}


	@Test
	public void testOpenSuccess() {
		StrawDrink drink = mock(StrawDrink.class);
		Activity activity = mock(Activity.class);

		BrowserPlugin plugin = new BrowserPlugin();
		plugin.setContext(activity);

		SingleStringParam param = new SingleStringParam();

		// proper url parameter
		param.value = "http://google.com/";

		plugin.open(param, drink);

		// startActivity is called
		verify(activity).startActivity(eq(new Intent(Intent.ACTION_VIEW, Uri.parse(param.value))));

		// success() is called
		verify(drink).success();
	}


	@Test
	public void testOpenFail() {
		StrawDrink drink = mock(StrawDrink.class);
		Activity activity = mock(Activity.class);

		BrowserPlugin plugin = new BrowserPlugin();
		plugin.setContext(activity);

		SingleStringParam param = new SingleStringParam();

		// wrong format url
		param.value = "zzzz";

		plugin.open(param, drink);

		// startActivity is not called
		verify(activity, never()).startActivity(any(Intent.class));

		// plugin action failed
		verify(drink).fail("12001", "not url: " + param.value);
	}

}
